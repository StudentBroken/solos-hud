import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/media/media_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import 'music_widget.dart';

class MusicApp extends GlassesApp {
  final MediaService media;
  MusicApp({required this.media});

  @override
  String get id => 'music';
  @override
  String get name => 'Music';
  @override
  IconData get icon => Icons.music_note;
  @override
  String get description =>
      'Now playing · album art · media controls via buttons';

  // Cached decoded album art image to avoid decoding every tick
  ui.Image? _cachedImage;
  Uint8List? _cachedArtBytes;

  @override
  void onActivate() {}
  @override
  void onDeactivate() {
    _cachedImage?.dispose();
    _cachedImage = null;
    _cachedArtBytes = null;
  }

  // ── Button controls: FRONT = prev, BACK = next, MAIN short = play/pause ──
  @override
  // ── Glasses button controls ───────────────────────────────────────────────
  // FRONT short  → Volume up
  // FRONT long   → Skip forward (next track)
  // BACK  short  → Volume down
  // BACK  long   → Skip back (previous track)
  // MAIN  short  → Play / Pause
  @override
  bool onButtonEvent(GlassesEvent event) {
    final action = event.action;
    final btn = event.button;
    if (btn == null) return false;

    // Only react to gesture events (Short = tap, Long = hold)
    if (action != GlassesActionType.buttonShort &&
        action != GlassesActionType.buttonLong) {
      return false;
    }

    switch (btn) {
      case GlassesButton.front:
        if (action == GlassesActionType.buttonShort) {
          media.volumeUp();
          return true;
        }
        if (action == GlassesActionType.buttonLong) {
          media.next();
          return true;
        }
      case GlassesButton.back:
        if (action == GlassesActionType.buttonShort) {
          media.volumeDown();
          return true;
        }
        if (action == GlassesActionType.buttonLong) {
          media.previous();
          return true;
        }
      case GlassesButton.main:
        if (action == GlassesActionType.buttonShort) {
          media.playPause();
          return true;
        }
      default:
        break;
    }
    return false;
  }

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() async {
    try {
      return await _renderFrame();
    } catch (_) {
      return null;
    }
  }

  Future<Uint8List> _renderFrame({bool overlayHint = false}) async {
    final track = media.current;
    final dw = SolosProtocol.displayWidth.toDouble();
    final dh = SolosProtocol.displayHeight.toDouble();

    const bg = Color(0xFF050810);
    const white = Color(0xFFFFFFFF);
    const muted = Color(0xFFBBCCDD); // brighter — was 0xFF88AABB (too dim)

    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, dw, dh));
    canvas.drawRect(
      Rect.fromLTWH(0, 0, dw, dh),
      ui.Paint()
        ..color = bg
        ..style = ui.PaintingStyle.fill,
    );

    if (track == null) {
      _drawText(
        canvas,
        'Nothing playing',
        0,
        116,
        22,
        const Color(0xFF557799),
        false,
        ui.TextAlign.center,
        dw,
      );
    } else {
      // ── Album art (left 240px square) ─────────────────────────────────
      if (track.albumArtJpeg != null) {
        if (_cachedArtBytes != track.albumArtJpeg) {
          _cachedImage?.dispose();
          _cachedImage = await _decodeArt(track.albumArtJpeg!);
          _cachedArtBytes = track.albumArtJpeg;
        }
        if (_cachedImage != null) {
          // Draw album art in a 240×240 box on the left
          canvas.drawImageRect(
            _cachedImage!,
            Rect.fromLTWH(0, 0, 128, 128),
            Rect.fromLTWH(0, 0, 240, 240),
            ui.Paint(),
          );
          // Gradient overlay to blend art with text area
          final grad = ui.Paint()
            ..shader = ui.Gradient.linear(
              const Offset(180, 0),
              const Offset(240, 0),
              [Colors.transparent, bg],
            );
          canvas.drawRect(Rect.fromLTWH(180, 0, 60, dh), grad);
        }
      } else {
        // No art — draw a placeholder
        canvas.drawRect(
          Rect.fromLTWH(0, 0, 240, 240),
          ui.Paint()
            ..color = const Color(0xFF0A1020)
            ..style = ui.PaintingStyle.fill,
        );
        _drawText(
          canvas,
          '♪',
          0,
          72,
          80,
          const Color(0xFF446688),
          false,
          ui.TextAlign.center,
          240,
        );
      }

      // ── Track info (right side) ────────────────────────────────────────
      const infoX = 248.0;
      const infoW = 428 - infoX - 8;

      // App name badge
      _drawText(
        canvas,
        track.appName.toUpperCase(),
        infoX,
        130, // Moved lower
        11,
        muted,
        false,
        ui.TextAlign.left,
        infoW,
      );

      // Title (Scrolling Marquee)
      _drawScrollingText(
        canvas,
        track.displayTitle,
        infoX,
        40, // Slightly lower
        34,
        white,
        true,
        infoW,
      );

      // Artist
      if (track.displayArtist.isNotEmpty) {
        _drawText(
          canvas,
          track.displayArtist,
          infoX,
          95, // Moved lower
          18,
          muted,
          false,
          ui.TextAlign.left,
          infoW,
        );
      }

      // Play/Pause indicator (Centered at bottom)
      const iconSize = 28.0;
      final centerX = infoX + (infoW - iconSize) / 2;
      _drawPlaybackStatus(
        canvas,
        centerX,
        165,
        iconSize,
        white,
        track.isPlaying,
      );
    }

    final picture = rec.endRecording();
    final image = await picture.toImage(
      SolosProtocol.displayWidth,
      SolosProtocol.displayHeight,
    );
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(
      bd!.buffer.asUint8List(),
      SolosProtocol.displayWidth,
      SolosProtocol.displayHeight,
    );
  }

  /// Same as [buildCustomFrame] but adds a "BACK×2 = close" dismiss hint.
  /// Called by HudController when the music quick-overlay is active.
  Future<Uint8List?> buildOverlayFrame() async {
    try {
      return await _renderFrame(overlayHint: true);
    } catch (_) {
      return null;
    }
  }

  Future<ui.Image?> _decodeArt(Uint8List bytes) async {
    try {
      final codec = await ui.instantiateImageCodec(bytes);
      final frame = await codec.getNextFrame();
      return frame.image;
    } catch (_) {
      return null;
    }
  }

  static void _drawText(
    ui.Canvas c,
    String text,
    double x,
    double y,
    double size,
    Color color,
    bool bold,
    ui.TextAlign align,
    double maxW,
  ) {
    final b =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: align,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
              maxLines: 2,
              ellipsis: '…',
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            ),
          )
          ..addText(text);
    final para = b.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  static void _drawScrollingText(
    ui.Canvas c,
    String text,
    double x,
    double y,
    double size,
    Color color,
    bool bold,
    double maxW,
  ) {
    // Build paragraph to measure width
    final b =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: ui.TextAlign.left,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
              maxLines: 1,
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            ),
          )
          ..addText(text);

    final para = b.build()..layout(const ui.ParagraphConstraints(width: 2000));
    final textW = para.minIntrinsicWidth;

    if (textW <= maxW) {
      // No scroll needed
      c.drawParagraph(para, Offset(x, y));
    } else {
      // Marquee effect
      final now = DateTime.now().millisecondsSinceEpoch / 1000.0;
      const speed = 40.0; // pixels per second
      final totalW = textW + 50.0; // Text + gap
      final offset = (now * speed) % totalW;

      c.save();
      c.clipRect(Rect.fromLTWH(x, y, maxW, size * 1.5));
      c.drawParagraph(para, Offset(x - offset, y));
      // Draw second copy for continuous loop
      if (offset > 50) {
        c.drawParagraph(para, Offset(x - offset + totalW, y));
      }
      c.restore();
    }
  }

  static void _drawPlaybackStatus(
    ui.Canvas c,
    double x,
    double y,
    double size,
    Color color,
    bool isPlaying,
  ) {
    final paint = ui.Paint()
      ..color = color
      ..style = ui.PaintingStyle.fill;

    if (!isPlaying) {
      // Draw Pause bars
      final barW = size * 0.3;
      final gap = size * 0.2;
      c.drawRect(Rect.fromLTWH(x, y, barW, size), paint);
      c.drawRect(Rect.fromLTWH(x + barW + gap, y, barW, size), paint);
    } else {
      // Draw Play triangle
      final path = ui.Path()
        ..moveTo(x, y)
        ..lineTo(x + size, y + size / 2)
        ..lineTo(x, y + size)
        ..close();
      c.drawPath(path, paint);
    }
  }

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      MusicPhoneWidget(media: media);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    return ListenableBuilder(
      listenable: media,
      builder: (context, _) {
        final t = media.current;
        if (t == null) {
          return Text(
            'Nothing playing',
            style: Theme.of(
              context,
            ).textTheme.bodySmall?.copyWith(color: Colors.grey),
          );
        }
        return Text(
          '♪  ${t.displayTitle}${t.displayArtist.isNotEmpty ? " — ${t.displayArtist}" : ""}',
          style: Theme.of(
            context,
          ).textTheme.bodySmall?.copyWith(color: Colors.white70),
          overflow: TextOverflow.ellipsis,
        );
      },
    );
  }
}
