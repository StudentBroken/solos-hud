import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../solos_protocol.dart';
import '../render_utils.dart';

/// Renders the "boot" frame shown on the glasses immediately after connecting.
/// Displayed for ~2 seconds before the last active app takes over.
class BootScreenRenderer {
  static const _dw = SolosProtocol.displayWidth;
  static const _dh = SolosProtocol.displayHeight;

  static Future<Uint8List> render(String deviceName) async {
    final rec    = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()));

    // Background
    canvas.drawRect(
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
      ui.Paint()..color = const Color(0xFF060C14)..style = ui.PaintingStyle.fill,
    );

    // Top accent line
    canvas.drawRect(
      Rect.fromLTWH(0, 0, _dw.toDouble(), 2),
      ui.Paint()..color = const Color(0xFF00CCFF)..style = ui.PaintingStyle.fill,
    );

    // Bottom accent line
    canvas.drawRect(
      Rect.fromLTWH(0, _dh - 2.0, _dw.toDouble(), 2),
      ui.Paint()..color = const Color(0xFF00CCFF)..style = ui.PaintingStyle.fill,
    );

    // App name — large
    _text(canvas, 'SOLOS HUD',
        x: 0, y: 44, size: 52, color: const Color(0xFF00CCFF), bold: true,
        align: ui.TextAlign.center, maxW: _dw.toDouble());

    // Device name
    _text(canvas, deviceName,
        x: 0, y: 108, size: 20, color: const Color(0xFFAABBCC),
        align: ui.TextAlign.center, maxW: _dw.toDouble());

    // Connected indicator (green dot + text)
    const dotCx = _dw / 2.0 - 60;
    const dotCy = 150.0;
    canvas.drawCircle(
      const Offset(dotCx, dotCy + 7),
      5,
      ui.Paint()..color = const Color(0xFF00FF88)..style = ui.PaintingStyle.fill,
    );
    _text(canvas, 'Connected',
        x: _dw / 2.0 - 50, y: 143, size: 16, color: const Color(0xFF00FF88),
        align: ui.TextAlign.left, maxW: 120);

    // Button hint — bottom
    _text(canvas, 'Hold main button  =  App Launcher',
        x: 0, y: 196, size: 14, color: const Color(0xFF334455),
        align: ui.TextAlign.center, maxW: _dw.toDouble());

    final picture = rec.endRecording();
    final image   = await picture.toImage(_dw, _dh);
    final bd      = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(bd!.buffer.asUint8List(), _dw, _dh);
  }

  static void _text(ui.Canvas c, String text, {
    required double x, required double y, required double size,
    required Color color, bool bold = false,
    required ui.TextAlign align, required double maxW,
  }) {
    final builder = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        textAlign: align, fontSize: size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
        maxLines: 1, ellipsis: '…',
      ),
    )
      ..pushStyle(ui.TextStyle(
        color: color, fontSize: size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    final para = builder.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }
}
