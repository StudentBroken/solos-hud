import 'dart:async';
import 'dart:ui' as ui;
import 'package:flutter/foundation.dart';
import 'package:flutter/painting.dart';
import '../rfcomm/glasses_event_service.dart';
import '../settings/app_settings.dart';
import 'notification_service.dart';
import 'notification_event.dart';
import 'notification_action_service.dart';
import '../hud_controller.dart';
import '../solos_protocol.dart';

class NotificationOverlayController extends ChangeNotifier {
  final NotificationService notifications;
  final HudController hud;
  final AppSettings settings;

  StreamSubscription? _sub;
  NotificationEvent? _current;
  Timer? _dismissTimer;
  int _replyIndex = 0;

  bool get isActive => _current != null;
  bool get enabled  => settings.notifEnabled;
  NotificationEvent? get current => _current;

  static const _quickReplies = [
    'OK',
    'On my way!',
    "Can't talk right now",
    "I'll call you back",
    'Yes',
    'No',
    'Talk later?',
  ];
  String get currentReply => _quickReplies[_replyIndex % _quickReplies.length];

  NotificationOverlayController({
    required this.notifications,
    required this.hud,
    required this.settings,
  }) {
    _sub = notifications.stream.listen(_onNotification);
  }

  // kept for backwards-compat call sites that toggle via this method
  void setEnabled(bool value) => settings.setNotifEnabled(value);

  void _onNotification(NotificationEvent event) {
    if (!settings.notifEnabled) return;

    if (event.isRemoval) {
      if (_current?.key == event.key) _dismiss();
      return;
    }

    if (event.isMedia || event.isNavigation) return;
    if (event.title.isEmpty && event.text.isEmpty) return;

    _current = event;
    _replyIndex = 0;
    _dismissTimer?.cancel();

    final secs = event.isCall
        ? settings.notifDurationCall
        : event.isMessage
            ? settings.notifDurationMsg
            : settings.notifDurationGen;

    _dismissTimer = Timer(Duration(seconds: secs), _dismiss);

    notifyListeners();
    _pushFrame();
  }

  bool handleButton(GlassesEvent event) {
    final n = _current;
    if (n == null) return false;
    if (event.action != GlassesActionType.buttonShort) return false;
    final btn = event.button;
    if (btn == null) return false;

    if (btn == GlassesButton.back) {
      if (n.isCall) {
        NotificationActionService.invoke(n.key, n.answerAction?.index ?? 0);
      } else if (n.isMessage) {
        final r = n.replyAction;
        if (r != null) {
          NotificationActionService.invoke(n.key, r.index, replyText: currentReply);
        }
      }
      _dismiss();
      return true;
    }

    if (btn == GlassesButton.front) {
      if (n.isCall) {
        NotificationActionService.invoke(n.key, n.declineAction?.index ?? 1);
      }
      _dismiss();
      return true;
    }

    if (btn == GlassesButton.main) {
      if (n.isMessage) {
        _replyIndex++;
        _pushFrame();
        notifyListeners();
      }
      return true;
    }

    return false;
  }

  void dismiss() => _dismiss();

  void _dismiss() {
    _dismissTimer?.cancel();
    _current = null;
    hud.clearOverlay();
    notifyListeners();
  }

  Future<void> _pushFrame() async {
    final n = _current;
    if (n == null) return;
    try {
      final rle = await _renderFrame(n);
      final secs = n.isCall
          ? settings.notifDurationCall + 5
          : n.isMessage
              ? settings.notifDurationMsg + 3
              : settings.notifDurationGen + 1;
      hud.showNotificationFrame(rle, duration: Duration(seconds: secs));
    } catch (e) {
      debugPrint('[NotifOverlay] render error: $e');
    }
  }

  // ── Renderer ─────────────────────────────────────────────────────────────

  static const _w = SolosProtocol.displayWidth;
  static const _h = SolosProtocol.displayHeight;

  // App-name → accent colour (consistent per app)
  static Color _appColor(String name) {
    const palette = [
      Color(0xFF00AAFF), // blue
      Color(0xFF00EE88), // green
      Color(0xFFFFAA00), // amber
      Color(0xFFDD44FF), // purple
      Color(0xFFFF6644), // orange
      Color(0xFF44DDFF), // cyan
      Color(0xFFFF4488), // pink
    ];
    final idx = name.isEmpty ? 0 : name.codeUnitAt(0) % palette.length;
    return palette[idx];
  }

  /// Draw a coloured circle with the first letter of the app name.
  static void _drawAppBadge(
    ui.Canvas canvas,
    String appName,
    double cx,
    double cy,
    double radius,
    Color accent,
  ) {
    // Filled circle
    canvas.drawCircle(
      Offset(cx, cy),
      radius,
      ui.Paint()..color = accent.withValues(alpha: 0.25),
    );
    // Border ring
    canvas.drawCircle(
      Offset(cx, cy),
      radius,
      ui.Paint()
        ..color = accent.withValues(alpha: 0.8)
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 2.5,
    );

    // First letter
    final letter = appName.isNotEmpty ? appName[0].toUpperCase() : '?';
    final pb = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        textAlign: ui.TextAlign.center,
        fontSize: radius * 1.1,
        fontWeight: ui.FontWeight.w700,
      ),
    )
      ..pushStyle(ui.TextStyle(
        color: accent,
        fontSize: radius * 1.1,
        fontWeight: ui.FontWeight.w700,
      ))
      ..addText(letter);
    final para = pb.build()
      ..layout(ui.ParagraphConstraints(width: radius * 2));
    canvas.drawParagraph(
      para,
      Offset(cx - radius, cy - radius * 0.72),
    );
  }

  Future<Uint8List> _renderFrame(NotificationEvent n) async {
    final recorder = ui.PictureRecorder();
    final canvas   = ui.Canvas(recorder);

    const bg = Color(0xFF030508);
    canvas.drawRect(
      Rect.fromLTWH(0, 0, _w.toDouble(), _h.toDouble()),
      ui.Paint()..color = bg,
    );

    if (n.isCall) {
      _renderCallFrame(canvas, n);
    } else if (n.isMessage) {
      _renderMessageFrame(canvas, n);
    } else {
      _renderGeneralFrame(canvas, n);
    }

    final picture  = recorder.endRecording();
    final image    = await picture.toImage(_w, _h);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (byteData == null) throw Exception('toByteData returned null');
    return SolosProtocol.encodeRLE565(byteData.buffer.asUint8List(), _w, _h);
  }

  void _renderCallFrame(ui.Canvas canvas, NotificationEvent n) {
    const accent = Color(0xFF00FF88);

    // Top label
    _text(canvas, 'INCOMING CALL', x: 14, y: 10, size: 22,
        color: accent, bold: true, maxW: _w - 28.0);

    canvas.drawRect(
      Rect.fromLTWH(14, 42, _w - 28.0, 1.5),
      ui.Paint()..color = accent.withValues(alpha: 0.4),
    );

    // App badge
    _drawAppBadge(canvas, n.appName, 44, 110, 28, accent);

    // Caller name — very large
    final name = n.title.isNotEmpty ? n.title : 'Unknown';
    _text(canvas, name, x: 84, y: 52, size: 60, bold: true,
        color: const Color(0xFFFFFFFF), maxW: _w - 98.0);

    if (n.text.isNotEmpty && n.text != n.title) {
      _text(canvas, n.text, x: 84, y: 122, size: 22,
          color: const Color(0xFF889999), maxW: _w - 98.0);
    }

    // Button hints
    _text(canvas, 'BACK = Answer', x: 14, y: 192, size: 22,
        color: accent, bold: true, maxW: 220);
    _text(canvas, 'FRONT = Decline', x: 240, y: 192, size: 22,
        color: const Color(0xFFFF4444), bold: true, maxW: 200);
  }

  void _renderMessageFrame(ui.Canvas canvas, NotificationEvent n) {
    final accent = _appColor(n.appName);

    // App badge (top-left)
    _drawAppBadge(canvas, n.appName, 36, 36, 26, accent);

    // App name beside badge
    _text(canvas, n.appName, x: 72, y: 18, size: 20,
        color: accent, maxW: _w - 86.0);

    canvas.drawRect(
      Rect.fromLTWH(14, 66, _w - 28.0, 1),
      ui.Paint()..color = accent.withValues(alpha: 0.25),
    );

    // Sender name — large
    _text(canvas, n.title, x: 14, y: 72, size: 34, bold: true,
        color: const Color(0xFFFFFFFF), maxW: _w - 28.0);

    // Message body — bigger
    final body = n.text.isNotEmpty ? n.text : n.bigText;
    _text(canvas, body, x: 14, y: 116, size: 26,
        color: const Color(0xFFCCCCCC), maxW: _w - 28.0, maxLines: 2);

    // Quick reply area
    if (n.replyAction != null) {
      canvas.drawRRect(
        RRect.fromRectAndRadius(
          Rect.fromLTWH(12, 174, _w - 24.0, 38),
          const Radius.circular(6),
        ),
        ui.Paint()
          ..color = accent.withValues(alpha: 0.15)
          ..style = ui.PaintingStyle.fill,
      );
      canvas.drawRRect(
        RRect.fromRectAndRadius(
          Rect.fromLTWH(12, 174, _w - 24.0, 38),
          const Radius.circular(6),
        ),
        ui.Paint()
          ..color = accent.withValues(alpha: 0.5)
          ..style = ui.PaintingStyle.stroke
          ..strokeWidth = 1,
      );
      _text(canvas, '"$currentReply"', x: 20, y: 181, size: 20,
          color: accent, maxW: _w - 32.0);
    } else {
      _text(canvas, 'FRONT = dismiss', x: 14, y: 206, size: 18,
          color: const Color(0xFF556677), maxW: _w - 28.0);
    }
  }

  void _renderGeneralFrame(ui.Canvas canvas, NotificationEvent n) {
    final accent = _appColor(n.appName);

    // App badge
    _drawAppBadge(canvas, n.appName, 36, 36, 26, accent);

    // App name
    _text(canvas, n.appName, x: 72, y: 18, size: 20,
        color: accent, maxW: _w - 86.0);

    canvas.drawRect(
      Rect.fromLTWH(14, 66, _w - 28.0, 1),
      ui.Paint()..color = accent.withValues(alpha: 0.25),
    );

    // Title — large
    if (n.title.isNotEmpty) {
      _text(canvas, n.title, x: 14, y: 74, size: 36, bold: true,
          color: const Color(0xFFFFFFFF), maxW: _w - 28.0);
    }

    // Body text — big
    final body = n.text.isNotEmpty ? n.text : n.bigText;
    if (body.isNotEmpty) {
      final yOff = n.title.isNotEmpty ? 120.0 : 74.0;
      _text(canvas, body, x: 14, y: yOff, size: 28,
          color: const Color(0xFFCCCCCC), maxW: _w - 28.0, maxLines: 3);
    }

    _text(canvas, 'FRONT = dismiss', x: 14, y: 210, size: 16,
        color: const Color(0xFF334455), maxW: _w - 28.0);
  }

  // ── Text helper ───────────────────────────────────────────────────────────

  static void _text(
    ui.Canvas canvas,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    required double maxW,
    bool bold    = false,
    int maxLines = 1,
  }) {
    final pb = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        fontSize:   size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
        maxLines:   maxLines,
        ellipsis:   '…',
      ),
    )
      ..pushStyle(ui.TextStyle(
        color:      color,
        fontSize:   size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    canvas.drawParagraph(
      pb.build()..layout(ui.ParagraphConstraints(width: maxW)),
      Offset(x, y),
    );
  }

  @override
  void dispose() {
    _sub?.cancel();
    _dismissTimer?.cancel();
    super.dispose();
  }
}
