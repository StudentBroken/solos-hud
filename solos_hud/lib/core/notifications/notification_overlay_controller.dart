import 'dart:async';
import 'dart:ui' as ui;
import 'package:flutter/foundation.dart';
import 'package:flutter/painting.dart';
import '../rfcomm/glasses_event_service.dart';
import 'notification_service.dart';
import 'notification_event.dart';
import 'notification_action_service.dart';
import '../hud_controller.dart';
import '../solos_protocol.dart';

class NotificationOverlayController extends ChangeNotifier {
  final NotificationService notifications;
  final HudController hud;

  StreamSubscription? _sub;
  NotificationEvent? _current;
  Timer? _dismissTimer;
  int _replyIndex = 0;
  bool _enabled = true;

  bool get isActive => _current != null;
  bool get enabled => _enabled;
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
  }) {
    _sub = notifications.stream.listen(_onNotification);
  }

  void setEnabled(bool value) {
    _enabled = value;
    notifyListeners();
  }

  void _onNotification(NotificationEvent event) {
    if (!_enabled) return;

    if (event.isRemoval) {
      if (_current?.key == event.key) _dismiss();
      return;
    }

    // Skip media and navigation — handled by their own systems
    if (event.isMedia || event.isNavigation) return;
    // Skip empty notifications
    if (event.title.isEmpty && event.text.isEmpty) return;

    _current = event;
    _replyIndex = 0;
    _dismissTimer?.cancel();

    final duration = event.isCall
        ? const Duration(seconds: 60)
        : event.isMessage
            ? const Duration(seconds: 12)
            : const Duration(seconds: 6);
    _dismissTimer = Timer(duration, _dismiss);

    notifyListeners();
    _pushFrame();
  }

  /// Called by GlassesMenuController priority handler.
  /// Returns true if the event was consumed.
  bool handleButton(GlassesEvent event) {
    final n = _current;
    if (n == null) return false;

    final action = event.action;
    final btn = event.button;
    if (btn == null) return false;

    // Only short presses
    if (action != GlassesActionType.buttonShort) return false;

    if (btn == GlassesButton.back) {
      if (n.isCall) {
        final answerIdx = n.answerAction?.index ?? 0;
        NotificationActionService.invoke(n.key, answerIdx);
      } else if (n.isMessage) {
        final replyAction = n.replyAction;
        if (replyAction != null) {
          NotificationActionService.invoke(
              n.key, replyAction.index, replyText: currentReply);
        }
      }
      _dismiss();
      return true;
    }

    if (btn == GlassesButton.front) {
      if (n.isCall) {
        final declineIdx = n.declineAction?.index ?? 1;
        NotificationActionService.invoke(n.key, declineIdx);
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
      hud.showNotificationFrame(
        rle,
        duration: n.isCall
            ? const Duration(seconds: 65)
            : n.isMessage
                ? const Duration(seconds: 15)
                : const Duration(seconds: 7),
      );
    } catch (e) {
      debugPrint('[NotifOverlay] render error: $e');
    }
  }

  static const _w = SolosProtocol.displayWidth;   // 428
  static const _h = SolosProtocol.displayHeight;  // 240

  Future<Uint8List> _renderFrame(NotificationEvent n) async {
    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder);

    // Background
    canvas.drawRect(
      Rect.fromLTWH(0, 0, _w.toDouble(), _h.toDouble()),
      ui.Paint()..color = const Color(0xFF000000),
    );

    if (n.isCall) {
      _renderCallFrame(canvas, n);
    } else if (n.isMessage) {
      _renderMessageFrame(canvas, n);
    } else {
      _renderGeneralFrame(canvas, n);
    }

    final picture = recorder.endRecording();
    final image = await picture.toImage(_w, _h);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (byteData == null) throw Exception('toByteData returned null');

    return SolosProtocol.encodeRLE565(byteData.buffer.asUint8List(), _w, _h);
  }

  void _renderCallFrame(ui.Canvas canvas, NotificationEvent n) {
    _drawText(canvas, 'INCOMING CALL', x: 20, y: 14, fontSize: 20,
        color: const Color(0xFF00FF88), bold: true, width: _w - 40);

    canvas.drawRect(Rect.fromLTWH(20, 50, _w - 40, 1),
        ui.Paint()..color = const Color(0x6600FF88));

    final name = n.title.isNotEmpty ? n.title : 'Unknown';
    _drawText(canvas, name, x: 20, y: 62, fontSize: 52, bold: true,
        color: const Color(0xFFFFFFFF), width: _w - 40);

    if (n.text.isNotEmpty && n.text != n.title) {
      _drawText(canvas, n.text, x: 20, y: 125, fontSize: 16,
          color: const Color(0xFFAAAAAA), width: _w - 40);
    }

    _drawText(canvas, 'BACK = Answer', x: 20, y: 188, fontSize: 18,
        color: const Color(0xFF00FF88), bold: true, width: 200);
    _drawText(canvas, 'FRONT = Decline', x: 228, y: 188, fontSize: 18,
        color: const Color(0xFFFF4444), bold: true, width: 180);
  }

  void _renderMessageFrame(ui.Canvas canvas, NotificationEvent n) {
    _drawText(canvas, n.appName, x: 20, y: 10, fontSize: 14,
        color: const Color(0xFF00AAFF), width: _w - 40);

    _drawText(canvas, n.title, x: 20, y: 32, fontSize: 20, bold: true,
        color: const Color(0xFFFFFFFF), width: _w - 40);

    final body = n.text.isNotEmpty ? n.text : n.bigText;
    _drawText(canvas, body, x: 20, y: 60, fontSize: 15,
        color: const Color(0xFFCCCCCC), width: _w - 40, maxLines: 3);

    if (n.replyAction != null) {
      canvas.drawRect(Rect.fromLTWH(18, 148, _w - 36, 40),
          ui.Paint()
            ..color = const Color(0x2600AAFF)
            ..style = ui.PaintingStyle.fill);
      canvas.drawRect(Rect.fromLTWH(18, 148, _w - 36, 40),
          ui.Paint()
            ..color = const Color(0x6600AAFF)
            ..style = ui.PaintingStyle.stroke
            ..strokeWidth = 1);
      _drawText(canvas, '"$currentReply"', x: 28, y: 158, fontSize: 14,
          color: const Color(0xFF00AAFF), width: _w - 56);

      _drawText(canvas, 'MAIN=next reply  BACK=send  FRONT=skip',
          x: 20, y: 198, fontSize: 12, color: const Color(0xFF888888), width: _w - 40);
    } else {
      _drawText(canvas, 'FRONT = dismiss', x: 20, y: 205, fontSize: 13,
          color: const Color(0xFF888888), width: _w - 40);
    }
  }

  void _renderGeneralFrame(ui.Canvas canvas, NotificationEvent n) {
    _drawText(canvas, n.appName, x: 20, y: 10, fontSize: 13,
        color: const Color(0xFF888888), width: _w - 40);

    if (n.title.isNotEmpty) {
      _drawText(canvas, n.title, x: 20, y: 32, fontSize: 22, bold: true,
          color: const Color(0xFFFFFFFF), width: _w - 40);
    }

    final body = n.text.isNotEmpty ? n.text : n.bigText;
    if (body.isNotEmpty) {
      final yOffset = n.title.isNotEmpty ? 68.0 : 32.0;
      _drawText(canvas, body, x: 20, y: yOffset, fontSize: 15,
          color: const Color(0xFFCCCCCC), width: _w - 40, maxLines: 4);
    }

    _drawText(canvas, 'FRONT = dismiss', x: 20, y: 210, fontSize: 12,
        color: const Color(0xFF555555), width: _w - 40);
  }

  void _drawText(
    ui.Canvas canvas,
    String text, {
    required double x,
    required double y,
    required double fontSize,
    required Color color,
    required double width,
    bool bold = false,
    int maxLines = 1,
  }) {
    final builder = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        fontSize: fontSize,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
        maxLines: maxLines,
        ellipsis: '…',
      ),
    )
      ..pushStyle(ui.TextStyle(
        color: color,
        fontSize: fontSize,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    final para = builder.build()
      ..layout(ui.ParagraphConstraints(width: width));
    canvas.drawParagraph(para, Offset(x, y));
  }

  @override
  void dispose() {
    _sub?.cancel();
    _dismissTimer?.cancel();
    super.dispose();
  }
}
