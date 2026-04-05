import 'dart:async';
import 'dart:ui' as ui;
import 'package:flutter/foundation.dart';
import 'package:flutter/painting.dart';
import 'glasses_app.dart';
import 'rfcomm/rfcomm_service.dart';
import 'solos_protocol.dart';
import 'settings/app_settings.dart';
import 'tilt_wake_service.dart';
import 'hud_ui/glasses_menu_controller.dart';
import 'voice/voice_service.dart';
import 'voice/voice_overlay_renderer.dart';

/// Drives the HUD refresh loop:
///   1. Ask the active GlassesApp for a payload string
///   2. Render it to a 428×240 bitmap
///   3. RLE565-encode it
///   4. Wrap in an IMAGE packet and send over RFCOMM
class HudController extends ChangeNotifier {
  final RfcommService rfcomm;
  final AppSettings settings;
  TiltWakeService? tiltWake; // set by app.dart after both are created

  GlassesApp? _activeApp;
  Timer? _timer;
  bool _running = false;
  bool _rendering = false; // prevent re-entrant renders
  int _tickCount = 0;
  String? _lastPayload;

  // ── Notification overlay ─────────────────────────────────────────────────
  // A temporary frame that takes priority over the active app for N seconds.
  Uint8List? _overlayFrame;
  Timer? _overlayTimer;

  /// Show [frame] on the glasses for [duration], then revert to the active app.
  /// Calling again while an overlay is active resets the timer.
  void showNotificationFrame(
    Uint8List frame, {
    Duration duration = const Duration(seconds: 5),
  }) {
    _overlayFrame = frame;
    _overlayTimer?.cancel();
    _overlayTimer = Timer(duration, () {
      _overlayFrame = null;
      _overlayTimer = null;
    });
  }

  void clearOverlay() {
    _overlayTimer?.cancel();
    _overlayFrame = null;
    _overlayTimer = null;
  }

  /// Set by app.dart after construction.
  GlassesMenuController? menu;

  /// When set, the voice assistant overlay appears on top of any active app
  /// whenever the voice state is not idle.
  VoiceService? voice;

  /// When set, called to render a music mini-player frame whenever
  /// [menu.musicOverlayActive] is true.
  Future<Uint8List?> Function()? musicFrameBuilder;

  /// When set, queried each tick to decide whether music is actively playing.
  /// If true the HUD slows to [_musicPlayingMinMs] ms between frames so the
  /// BT radio has headroom for A2DP audio packets.
  bool Function()? isMusicPlaying;
  static const int _musicPlayingMinMs = 1200; // ~0.8 FPS when music is on

  HudController({required this.rfcomm, required this.settings});

  // ── Display keep-alive ────────────────────────────────────────────────────
  // Send WAKE_UP every few seconds so the glasses display doesn't time out.
  DateTime _lastWakeUp = DateTime(1970);
  static const _wakeUpInterval = Duration(seconds: 5);

  // ── Preview frame ─────────────────────────────────────────────────────────
  // Stores the most recently rendered frame for on-screen preview.
  // Text frames are captured directly; custom/menu frames are decoded at ~1 FPS.
  ui.Image? _lastFrame;
  DateTime _lastPreviewDecode = DateTime(1970);
  static const _previewThrottle = Duration(milliseconds: 900);

  /// Most recent rendered frame as a [ui.Image], for the phone-side preview.
  ui.Image? get lastFrame => _lastFrame;

  GlassesApp? get activeApp => _activeApp;
  bool get running => _running;
  int get tickCount => _tickCount;
  String? get lastPayload => _lastPayload;
  bool get tiltWakeActive => tiltWake?.active ?? false;

  void setActiveApp(GlassesApp? app) {
    _activeApp?.onDeactivate();
    _activeApp = app;
    app?.onActivate();
    _lastPayload = null;
    _tickCount = 0;
    notifyListeners();
  }

  void startLoop() {
    if (_running) return;
    _running = true;
    _scheduleNext();
    notifyListeners();
  }

  void stopLoop() {
    _timer?.cancel();
    _running = false;
    notifyListeners();
  }

  // ── Compensated timer ────────────────────────────────────────────────────
  // Schedule the next tick so the TOTAL interval (render + wait) equals the
  // configured rate, rather than adding render time on top of it.
  final Stopwatch _tickSw = Stopwatch();

  void _scheduleNext({int elapsedMs = 0}) {
    // When music is playing, enforce a minimum interval so BT radio time
    // isn't starved of the bandwidth A2DP needs for smooth audio.
    final appMs = _activeApp?.preferredRefreshMs ?? settings.refreshRateMs;
    final targetMs = (isMusicPlaying?.call() == true)
        ? appMs.clamp(_musicPlayingMinMs, 9999)
        : appMs;
    final delay = (targetMs - elapsedMs).clamp(0, targetMs);
    _timer = Timer(Duration(milliseconds: delay), _tick);
  }

  Future<void> _tick() async {
    if (!_running) return;
    _tickSw.reset();
    _tickSw.start();

    // When tilt-wake is active and screen is off, skip rendering.
    final tw = tiltWake;
    final skipFrame = tw != null && tw.active && !tw.screenOn;

    if (!skipFrame && rfcomm.isConnected && !_rendering) {
      _rendering = true;
      try {
        // ── Keep-alive WAKE_UP (prevents display sleep) ───────────────────
        final now = DateTime.now();
        if (now.difference(_lastWakeUp) >= _wakeUpInterval) {
          _lastWakeUp = now;
          await rfcomm.sendBytes(SolosProtocol.buildWakeUp());
        }

        // ── Priority 1: notification overlay ─────────────────────────────
        if (_overlayFrame != null) {
          final packet = SolosProtocol.buildImagePacket(
            imageData: _overlayFrame!,
          );
          await rfcomm.sendBytes(packet);
          _tickCount++;
          notifyListeners();
          _rendering = false;
          if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
          return;
        }

        // ── Priority 2: music quick-overlay (double-tap BACK) ────────────────
        if (menu != null &&
            menu!.musicOverlayActive &&
            musicFrameBuilder != null) {
          try {
            final rle = await musicFrameBuilder!();
            if (rle != null) {
              await rfcomm.sendBytes(
                SolosProtocol.buildImagePacket(imageData: rle),
              );
              _tickCount++;
              notifyListeners();
            }
          } catch (e) {
            debugPrint('[HUD] music overlay error: $e');
          }
          _rendering = false;
          if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
          return;
        }

        // ── Priority 3: voice assistant overlay (shows on top of any app) ──
        if (voice != null && voice!.state != VoiceState.idle) {
          try {
            final rle = await VoiceOverlayRenderer.render(voice!);
            final packet = SolosProtocol.buildImagePacket(imageData: rle);
            await rfcomm.sendBytes(packet);
            _tickCount++;
            notifyListeners();
          } catch (e) {
            debugPrint('[HUD] voice overlay error: $e');
          }
          _rendering = false;
          if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
          return;
        }

        // ── Priority 4: glasses menu ──────────────────────────────────────
        if (menu != null && menu!.isOpen) {
          try {
            final menuRle = await menu!.renderFrame();
            final packet = SolosProtocol.buildImagePacket(imageData: menuRle);
            await rfcomm.sendBytes(packet);
            _tryUpdatePreview(menuRle, 2);
            _tickCount++;
            notifyListeners();
          } catch (e) {
            debugPrint('[HUD] menu render error: $e');
          }
          _rendering = false;
          if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
          return;
        }

        if (_activeApp == null) {
          _rendering = false;
          if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
          return;
        }

        // ── Priority 3: active app ────────────────────────────────────────
        Uint8List? packet;
        try {
          final customData = await _activeApp!.buildCustomFrame();
          if (customData != null) {
            packet = SolosProtocol.buildImagePacket(
              imageData: customData,
              codec: _activeApp!.preferredCodec,
            );
            _tryUpdatePreview(customData, _activeApp!.preferredCodec);
            _lastPayload = '[custom frame]';
          } else {
            final payload = _activeApp!.buildGlassesPayload();
            if (payload != null) {
              packet = await _buildPacket(payload);
              _lastPayload = payload;
            }
          }
        } catch (e) {
          debugPrint('[HUD] render error: $e');
        }

        if (packet != null) {
          await rfcomm.sendBytes(packet);
          _tickCount++;
          notifyListeners();
        }
      } finally {
        _rendering = false;
      }
    }

    if (_running) _scheduleNext(elapsedMs: _tickSw.elapsedMilliseconds);
  }

  /// Force a single frame send immediately (for testing without the loop).
  Future<void> sendNow() async {
    if (_activeApp == null || !rfcomm.isConnected) return;
    Uint8List? packet;
    final customRle = await _activeApp!.buildCustomFrame();
    if (customRle != null) {
      packet = SolosProtocol.buildImagePacket(imageData: customRle);
      _lastPayload = '[custom frame]';
    } else {
      final payload = _activeApp!.buildGlassesPayload();
      if (payload != null) {
        packet = await _buildPacket(payload);
        _lastPayload = payload;
      }
    }
    if (packet != null) {
      await rfcomm.sendBytes(packet);
      _tickCount++;
      notifyListeners();
    }
  }

  /// Send WAKE_UP to wake the display.
  Future<void> sendWakeUp() async {
    if (rfcomm.isConnected) {
      await rfcomm.sendBytes(SolosProtocol.buildWakeUp());
    }
  }

  // ── Rendering ─────────────────────────────────────────────────────────────

  Future<Uint8List?> _buildPacket(String payload) async {
    // Special: solid colour fill
    if (payload.startsWith('COLOR:')) {
      return _buildColorPacket(payload.substring(6));
    }
    // General: render text
    final rle = await _renderTextToRLE565(payload);
    if (rle == null) return null;
    return SolosProtocol.buildImagePacket(imageData: rle);
  }

  Uint8List _buildColorPacket(String colorName) {
    final (r, g, b) = switch (colorName.toUpperCase()) {
      'RED' => (255, 0, 0),
      'GREEN' => (0, 255, 0),
      'BLUE' => (0, 0, 255),
      'WHITE' => (255, 255, 255),
      _ => (0, 0, 0), // BLACK and fallback
    };
    return SolosProtocol.buildSolidColorPacket(r, g, b);
  }

  Future<Uint8List?> _renderTextToRLE565(String text) async {
    final w = SolosProtocol.displayWidth;
    final h = SolosProtocol.displayHeight;

    try {
      final recorder = ui.PictureRecorder();
      final canvas = ui.Canvas(recorder);

      // Black background
      canvas.drawRect(
        Rect.fromLTWH(0, 0, w.toDouble(), h.toDouble()),
        ui.Paint()..color = const Color(0xFF000000),
      );

      // Split into main value and optional unit (e.g. "34.2" and "km/h")
      final parts = text.split('\n');
      final mainText = parts[0];
      final subText = parts.length > 1 ? parts[1] : null;

      // Main value — large
      final mainPara = _buildParagraph(
        mainText,
        fontSize: 96,
        color: const Color(0xFF00FF88), // green HUD color
        fontWeight: ui.FontWeight.w900,
        width: w.toDouble(),
      );
      final mainY = subText != null
          ? (h / 2 - mainPara.height - 4).clamp(0, h.toDouble())
          : (h / 2 - mainPara.height / 2).clamp(0, h.toDouble());
      canvas.drawParagraph(mainPara, Offset(0, mainY.toDouble()));

      // Sub-text / unit — smaller
      if (subText != null) {
        final subPara = _buildParagraph(
          subText,
          fontSize: 36,
          color: const Color(0xFF88FFCC),
          fontWeight: ui.FontWeight.w400,
          width: w.toDouble(),
        );
        canvas.drawParagraph(subPara, Offset(0, mainY + mainPara.height + 4));
      }

      final picture = recorder.endRecording();
      final image = await picture.toImage(w, h);
      // Capture for preview without re-encoding (text apps update live).
      _lastFrame = image;
      final byteData = await image.toByteData(
        format: ui.ImageByteFormat.rawRgba,
      );
      if (byteData == null) return null;

      return SolosProtocol.encodeRLE565(byteData.buffer.asUint8List(), w, h);
    } catch (e) {
      return null;
    }
  }

  ui.Paragraph _buildParagraph(
    String text, {
    required double fontSize,
    required Color color,
    required ui.FontWeight fontWeight,
    required double width,
  }) {
    final builder =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: TextAlign.center,
              fontSize: fontSize,
              fontWeight: fontWeight,
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: fontSize,
              fontWeight: fontWeight,
            ),
          )
          ..addText(text);

    final para = builder.build();
    para.layout(ui.ParagraphConstraints(width: width));
    return para;
  }

  // ── Preview decode ────────────────────────────────────────────────────────

  /// Schedules a background decode of [imageData] for the phone-side preview.
  /// Throttled to ~1 FPS so it doesn't compete with the HUD render loop.
  void _tryUpdatePreview(Uint8List imageData, int codec) {
    final now = DateTime.now();
    if (now.difference(_lastPreviewDecode) < _previewThrottle) return;
    _lastPreviewDecode = now;
    Future.microtask(() async {
      try {
        ui.Image img;
        if (codec == 8 || codec == 32) {
          // JPEG or PNG — delegate to Flutter's codec
          final c = await ui.instantiateImageCodec(imageData);
          final f = await c.getNextFrame();
          img = f.image;
        } else {
          img = await _decodeRLE565ToImage(imageData);
        }
        _lastFrame = img;
        notifyListeners();
      } catch (e) {
        debugPrint('[HUD] preview decode: $e');
      }
    });
  }

  Future<ui.Image> _decodeRLE565ToImage(Uint8List rle) {
    final w = SolosProtocol.displayWidth;
    final h = SolosProtocol.displayHeight;
    final pixels = Uint8List(w * h * 4);
    int di = 0, pi = 0;
    while (di + 2 < rle.length && pi < pixels.length - 3) {
      final count = rle[di++];
      final lo = rle[di++];
      final hi = rle[di++];
      final rgb565 = (hi << 8) | lo;
      final r = ((rgb565 >> 11) & 0x1F) * 255 ~/ 31;
      final g = ((rgb565 >> 5) & 0x3F) * 255 ~/ 63;
      final b = (rgb565 & 0x1F) * 255 ~/ 31;
      for (int i = 0; i < count && pi < pixels.length - 3; i++) {
        pixels[pi++] = r;
        pixels[pi++] = g;
        pixels[pi++] = b;
        pixels[pi++] = 255;
      }
    }
    final completer = Completer<ui.Image>();
    ui.decodeImageFromPixels(
      pixels, w, h, ui.PixelFormat.rgba8888,
      (img) => completer.complete(img),
    );
    return completer.future;
  }

  @override
  void dispose() {
    _timer?.cancel();
    _overlayTimer?.cancel();
    _activeApp?.onDeactivate();
    menu?.dispose();
    _lastFrame?.dispose();
    super.dispose();
  }
}
