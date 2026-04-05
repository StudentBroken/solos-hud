import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../solos_protocol.dart';
import '../render_utils.dart';
import 'voice_service.dart';

const _bg     = Color(0xFF050A12);
const _cyan   = Color(0xFF00CCFF);
const _white  = Color(0xFFFFFFFF);
const _dim    = Color(0xFF667788);    // secondary labels
const _green  = Color(0xFF00FF88);
const _orange = Color(0xFFFFAA00);

class VoiceOverlayRenderer {
  static const int _dw = SolosProtocol.displayWidth;
  static const int _dh = SolosProtocol.displayHeight;

  static Future<Uint8List> render(VoiceService v) async {
    final rec    = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()));

    // Solid dark background
    canvas.drawRect(Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
        ui.Paint()..color = _bg..style = ui.PaintingStyle.fill);

    // Cyan top stripe
    canvas.drawRect(Rect.fromLTWH(0, 0, _dw.toDouble(), 3),
        ui.Paint()..color = _cyan..style = ui.PaintingStyle.fill);

    switch (v.state) {
      case VoiceState.idle:
        break;

      // ── Listening ─────────────────────────────────────────────────────────
      case VoiceState.listening:
        final t    = DateTime.now().millisecondsSinceEpoch;
        final dots = switch ((t ~/ 400) % 3) {
          0 => '● ○ ○', 1 => '● ● ○', _ => '● ● ●'
        };

        if (v.partial.isNotEmpty) {
          // Words are coming in — show them large and white
          _text(canvas, v.partial,
              x: 12, y: 36, size: 34, color: _white, bold: true,
              align: ui.TextAlign.center, maxW: _dw - 20.0, maxLines: 3);
          _text(canvas, dots,
              x: 0, y: 194, size: 18, color: _cyan,
              align: ui.TextAlign.center, maxW: _dw.toDouble());
        } else {
          // Waiting for speech
          _text(canvas, dots,
              x: 0, y: 62, size: 36, color: _cyan,
              align: ui.TextAlign.center, maxW: _dw.toDouble());
          _text(canvas, 'Listening…',
              x: 0, y: 116, size: 32, color: _white, bold: true,
              align: ui.TextAlign.center, maxW: _dw.toDouble());
          _text(canvas, 'double-tap FRONT  ·  cancel',
              x: 0, y: 196, size: 13, color: _dim,
              align: ui.TextAlign.center, maxW: _dw.toDouble());
        }

      // ── Processing ────────────────────────────────────────────────────────
      case VoiceState.processing:
        if (v.partial.isNotEmpty) {
          // Show what was heard — bright white so user can confirm
          _text(canvas, v.partial,
              x: 12, y: 28, size: 30, color: _white, bold: true,
              align: ui.TextAlign.center, maxW: _dw - 20.0, maxLines: 3);
        }
        _text(canvas, '✦  Thinking…',
            x: 0,
            y: v.partial.isNotEmpty ? 168 : 90,
            size: 28, color: _orange,
            align: ui.TextAlign.center, maxW: _dw.toDouble());

      // ── Responding ────────────────────────────────────────────────────────
      case VoiceState.responding:
        _text(canvas, v.response,
            x: 12, y: 36, size: 30, color: _white, bold: true,
            align: ui.TextAlign.center, maxW: _dw - 20.0, maxLines: 3);
        _text(canvas, '▶  Speaking…',
            x: 0, y: 196, size: 15, color: _green,
            align: ui.TextAlign.center, maxW: _dw.toDouble());

      // ── Calibrating ───────────────────────────────────────────────────────
      case VoiceState.calibrating:
        _text(canvas, 'CALIBRATING',
            x: 0, y: 36, size: 26, color: _orange, bold: true,
            align: ui.TextAlign.center, maxW: _dw.toDouble());
        _text(canvas, v.calFeedback,
            x: 12, y: 86, size: 22, color: _white,
            align: ui.TextAlign.center, maxW: _dw - 20.0, maxLines: 2);
        _text(canvas, '${v.calSuccess}/3 recognised',
            x: 0, y: 148, size: 18,
            color: v.calSuccess > 0 ? _green : _dim,
            align: ui.TextAlign.center, maxW: _dw.toDouble());
    }

    final picture = rec.endRecording();
    final image   = await picture.toImage(_dw, _dh);
    final bd      = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(bd!.buffer.asUint8List(), _dw, _dh);
  }

  static void _text(
    ui.Canvas c,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    bool bold = false,
    required ui.TextAlign align,
    required double maxW,
    int maxLines = 2,
  }) {
    final b = ui.ParagraphBuilder(ui.ParagraphStyle(
      textAlign: align,
      fontSize:  size,
      fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      maxLines:  maxLines,
      ellipsis:  '…',
    ))
      ..pushStyle(ui.TextStyle(
        color:      color,
        fontSize:   size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    final para = b.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }
}
