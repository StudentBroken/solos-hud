import 'dart:math' as math;
import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/notifications/notification_event.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';

// ── Palette ───────────────────────────────────────────────────────────────────
const _bg = Color(0xFF000000);
const _cyan = Color(0xFF00CCFF);
const _white = Color(0xFFFFFFFF);
const _muted = Color(0xFF556688);
const _green = Color(0xFF00FF88);
const _divider = Color(0xFF1A2A40);
const _destBg = Color(0xFF080F18);

class NavigationRenderer {
  static final double _w = SolosProtocol.displayWidth.toDouble();
  static final double _h = SolosProtocol.displayHeight.toDouble();

  static const double _arrowZoneW = 148.0;
  static const double _infoX = 158.0;

  /// Render the navigation HUD frame → RLE565 bytes.
  static Future<Uint8List> render({
    NavInstruction? instruction,
    String? destinationLabel,
    bool arrived = false,
  }) async {
    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder, Rect.fromLTWH(0, 0, _w, _h));

    _drawBg(canvas);

    if (arrived) {
      _drawArrived(canvas);
    } else if (instruction != null) {
      _drawArrow(canvas, instruction.maneuver);
      _drawDivider(canvas);
      _drawInstruction(canvas, instruction);
      if (destinationLabel != null) _drawDestStrip(canvas, destinationLabel);
    } else if (destinationLabel != null) {
      _drawStandby(canvas, destinationLabel);
    } else {
      _drawIdle(canvas);
    }

    final picture = recorder.endRecording();
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

  // ── Background & chrome ───────────────────────────────────────────────────

  static void _drawBg(ui.Canvas c) =>
      c.drawRect(Rect.fromLTWH(0, 0, _w, _h), _fill(_bg));

  static void _drawDivider(ui.Canvas c) => c.drawRect(
    Rect.fromLTWH(_arrowZoneW + 1, 0, 2, _h - 40),
    _fill(_divider),
  );

  static void _drawDestStrip(ui.Canvas c, String dest) {
    c.drawRect(Rect.fromLTWH(0, _h - 38, _w, 38), _fill(_destBg));
    c.drawRect(Rect.fromLTWH(0, _h - 38, _w, 1), _fill(_divider));
    _text(
      c,
      '▶  ${dest.length > 48 ? '${dest.substring(0, 45)}…' : dest}',
      x: 10,
      y: _h - 28,
      size: 15,
      color: _muted,
      align: ui.TextAlign.left,
      maxW: _w - 12,
    );
  }

  // ── Content sections ──────────────────────────────────────────────────────

  static void _drawInstruction(ui.Canvas c, NavInstruction nav) {
    final infoW = _w - _infoX - 10;
    double y = 18;

    _text(
      c,
      nav.maneuverLabel,
      x: _infoX,
      y: y,
      size: 20,
      color: _cyan,
      bold: true,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
    y += 30;

    if (nav.distanceText.isNotEmpty) {
      _text(
        c,
        nav.distanceText,
        x: _infoX,
        y: y,
        size: 54,
        color: _white,
        bold: true,
        align: ui.TextAlign.left,
        maxW: infoW,
      );
      y += 68;
    }

    if (nav.streetName.isNotEmpty) {
      _text(
        c,
        nav.streetName,
        x: _infoX,
        y: y,
        size: 20,
        color: _white,
        align: ui.TextAlign.left,
        maxW: infoW,
      );
    }
  }

  static void _drawStandby(ui.Canvas c, String dest) {
    _drawStraightArrow(c, color: _muted);
    _drawDivider(c);
    final infoW = _w - _infoX - 10;
    _text(
      c,
      'NAVIGATING TO',
      x: _infoX,
      y: 28,
      size: 16,
      color: _muted,
      bold: true,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
    _text(
      c,
      dest,
      x: _infoX,
      y: 58,
      size: 22,
      color: _white,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
    _text(
      c,
      'Awaiting first turn…',
      x: _infoX,
      y: 140,
      size: 15,
      color: _muted,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
  }

  static void _drawArrived(ui.Canvas c) {
    _drawCheckmark(c);
    _drawDivider(c);
    final infoW = _w - _infoX - 10;
    _text(
      c,
      'ARRIVED',
      x: _infoX,
      y: 72,
      size: 42,
      color: _green,
      bold: true,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
    _text(
      c,
      'You have reached\nyour destination',
      x: _infoX,
      y: 126,
      size: 18,
      color: _muted,
      align: ui.TextAlign.left,
      maxW: infoW,
    );
  }

  static void _drawIdle(ui.Canvas c) {
    _text(
      c,
      'NAVIGATION',
      x: 0,
      y: 60,
      size: 38,
      color: _muted,
      bold: true,
      align: ui.TextAlign.center,
      maxW: _w,
    );
    _text(
      c,
      'Share an address from Google Maps',
      x: 0,
      y: 114,
      size: 18,
      color: const Color(0xFF223344),
      align: ui.TextAlign.center,
      maxW: _w,
    );
  }

  // ── Arrow dispatcher ──────────────────────────────────────────────────────

  static void _drawArrow(ui.Canvas c, Maneuver m) {
    switch (m) {
      case Maneuver.straight:
      case Maneuver.unknown:
      case Maneuver.merge:
        _drawStraightArrow(c);
      case Maneuver.turnRight:
      case Maneuver.slightRight:
      case Maneuver.sharpRight:
        _drawTurnArrow(c, right: true);
      case Maneuver.turnLeft:
      case Maneuver.slightLeft:
      case Maneuver.sharpLeft:
        _drawTurnArrow(c, right: false);
      case Maneuver.uTurn:
        _drawUTurnArrow(c);
      case Maneuver.roundabout:
        _drawRoundaboutArrow(c);
      case Maneuver.arrive:
        _drawCheckmark(c);
    }
  }

  // ── Arrow geometry ────────────────────────────────────────────────────────

  static const double _cx = _arrowZoneW / 2; // 74 — horizontal centre
  static const double _top = 32.0;
  static const double _bot = 190.0;

  static ui.Paint _stroke({double w = 22, Color? color}) => ui.Paint()
    ..color = color ?? _cyan
    ..style = ui.PaintingStyle.stroke
    ..strokeWidth = w
    ..strokeCap = ui.StrokeCap.round
    ..strokeJoin = ui.StrokeJoin.round;

  static void _drawStraightArrow(ui.Canvas c, {Color? color}) {
    c.drawLine(
      Offset(_cx, _bot),
      Offset(_cx, _top + 24),
      _stroke(color: color),
    );
    _arrowhead(c, Offset(_cx, _top), -math.pi / 2, color: color ?? _cyan);
  }

  static void _drawTurnArrow(ui.Canvas c, {required bool right}) {
    final sign = right ? 1.0 : -1.0;
    final endX = _cx + sign * 46;

    // Shaft up
    c.drawLine(Offset(_cx, _bot), Offset(_cx, 92), _stroke());

    // Curve (cubic bezier)
    final path = ui.Path()
      ..moveTo(_cx, 92)
      ..cubicTo(_cx, 52, endX, 52, endX, 88);
    c.drawPath(path, _stroke());

    // Arrowhead pointing down
    _arrowhead(c, Offset(endX, 115), math.pi / 2);
  }

  static void _drawUTurnArrow(ui.Canvas c) {
    const rx = _cx + 22.0;
    const lx = _cx - 22.0;

    c.drawLine(Offset(rx, _bot), Offset(rx, 92), _stroke());

    // Semicircle: left half arc
    final rect = Rect.fromLTRB(lx, 66, rx, 118);
    c.drawArc(rect, -math.pi / 2, -math.pi, false, _stroke());

    c.drawLine(Offset(lx, 92), Offset(lx, 148), _stroke());
    _arrowhead(c, Offset(lx, 175), math.pi / 2);
  }

  static void _drawRoundaboutArrow(ui.Canvas c) {
    final paint = _stroke(w: 11);
    c.drawCircle(Offset(_cx, 108), 42, paint);

    // Entry from the right
    c.drawLine(Offset(_cx + 42, 108), Offset(_cx + 66, 108), paint);
    _arrowhead(c, Offset(_cx + 42, 108), math.pi, size: 16);

    // Exit upward
    c.drawLine(Offset(_cx, 66), Offset(_cx, 42), paint);
    _arrowhead(c, Offset(_cx, 30), -math.pi / 2, size: 16);
  }

  static void _drawCheckmark(ui.Canvas c) {
    c.drawCircle(
      Offset(_cx, 108),
      48,
      ui.Paint()
        ..color = const Color(0xFF001505)
        ..style = ui.PaintingStyle.fill,
    );
    c.drawCircle(Offset(_cx, 108), 48, _stroke(w: 4, color: _green));
    final path = ui.Path()
      ..moveTo(_cx - 22, 108)
      ..lineTo(_cx - 4, 126)
      ..lineTo(_cx + 26, 86);
    c.drawPath(path, _stroke(w: 10, color: _green));
  }

  static void _arrowhead(
    ui.Canvas c,
    Offset tip,
    double angle, {
    Color? color,
    double size = 48,
  }) {
    final paint = ui.Paint()
      ..color = color ?? _cyan
      ..style = ui.PaintingStyle.fill;
    final path = ui.Path()
      ..moveTo(tip.dx, tip.dy)
      ..lineTo(
        tip.dx + size * math.cos(angle + math.pi * 0.75),
        tip.dy + size * math.sin(angle + math.pi * 0.75),
      )
      ..lineTo(
        tip.dx + size * math.cos(angle - math.pi * 0.75),
        tip.dy + size * math.sin(angle - math.pi * 0.75),
      )
      ..close();
    c.drawPath(path, paint);
  }

  // ── Text helper ───────────────────────────────────────────────────────────

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
  }) {
    final builder =
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

    final para = builder.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  static ui.Paint _fill(Color c) => ui.Paint()
    ..color = c
    ..style = ui.PaintingStyle.fill;

  // ── Map mode renderer ─────────────────────────────────────────────────────

  /// Render the full-map HUD frame.
  ///
  /// [mapJpegBytes] is the raw JPEG returned by Mapbox Static Images API.
  /// The map fills the entire 428×240 display. A semi-transparent overlay bar
  /// at the bottom carries the current turn instruction (if any).
  /// When [mapJpegBytes] is null, shows a "loading" placeholder.
  static Future<Uint8List> renderMapMode({
    Uint8List? mapJpegBytes,
    NavInstruction? instruction,
    String? destinationLabel,
    bool arrived = false,
    bool headingUp = true,
  }) async {
    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder, Rect.fromLTWH(0, 0, _w, _h));

    // ── Background ──────────────────────────────────────────────────────────
    canvas.drawRect(
      Rect.fromLTWH(0, 0, _w, _h),
      _fill(const Color(0xFF0A1020)),
    );

    if (mapJpegBytes != null) {
      // Decode and draw the map tile
      final codec = await ui.instantiateImageCodec(
        mapJpegBytes,
        targetWidth: SolosProtocol.displayWidth,
        targetHeight: SolosProtocol.displayHeight,
      );
      final frame = await codec.getNextFrame();
      final mapImage = frame.image;
      canvas.drawImage(mapImage, Offset.zero, ui.Paint());
      mapImage.dispose();
    } else {
      // Placeholder while waiting for first tile
      _text(
        canvas,
        'Loading map…',
        x: 0,
        y: _h / 2 - 14,
        size: 22,
        color: _muted,
        align: ui.TextAlign.center,
        maxW: _w,
      );
    }

    // ── Compass rose (top-right, only in heading-up mode) ───────────────────
    if (headingUp && mapJpegBytes != null) {
      _drawCompassN(canvas);
    }

    // ── Bottom instruction overlay ──────────────────────────────────────────
    const overlayH = 68.0;
    final overlayY = _h - overlayH;

    if (arrived) {
      canvas.drawRect(
        Rect.fromLTWH(0, overlayY, _w, overlayH),
        _fill(const Color(0xDD001A08)),
      );
      _text(
        canvas,
        '✓  ARRIVED — You have reached your destination',
        x: 10,
        y: overlayY + 22,
        size: 18,
        color: _green,
        bold: true,
        align: ui.TextAlign.left,
        maxW: _w - 12,
      );
    } else if (instruction != null) {
      // Gradient-style layered overlay: darker on the very bottom
      canvas.drawRect(
        Rect.fromLTWH(0, overlayY, _w, overlayH),
        _fill(const Color(0xDD000A14)),
      );
      canvas.drawRect(
        Rect.fromLTWH(0, overlayY, _w, 1),
        _fill(const Color(0x4400CCFF)),
      );

      // Maneuver icon strip (geometric, repositioned)
      _drawGeometricManeuver(
        canvas,
        instruction.maneuver,
        160,
        overlayY + overlayH / 2,
      );

      // Distance and Street Name (starting more left of center)
      final textStartX = (_w / 2) - 25; // 189

      // Distance
      if (instruction.distanceText.isNotEmpty) {
        _text(
          canvas,
          instruction.distanceText,
          x: textStartX,
          y: overlayY + 6,
          size: 36,
          color: _white,
          bold: true,
          align: ui.TextAlign.left,
          maxW: 210,
        );
      }

      // Street name
      if (instruction.streetName.isNotEmpty) {
        _text(
          canvas,
          instruction.streetName,
          x: textStartX,
          y: overlayY + 42,
          size: 20,
          color: const Color(0xFFAABBCC),
          align: ui.TextAlign.left,
          maxW: 210,
        );
      }
    } else if (destinationLabel != null) {
      canvas.drawRect(
        Rect.fromLTWH(0, overlayY, _w, overlayH),
        _fill(const Color(0xBB000A14)),
      );
      _text(
        canvas,
        '▶  $destinationLabel',
        x: 10,
        y: overlayY + 12,
        size: 17,
        color: _muted,
        align: ui.TextAlign.left,
        maxW: _w - 12,
      );
    }

    final picture = recorder.endRecording();
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

  // ── Compass N indicator (small, top-right corner) ─────────────────────────

  static void _drawCompassN(ui.Canvas c) {
    final cx = _w - 24.0;
    const cy = 24.0;
    const r = 18.0;
    // Circle background
    c.drawCircle(
      Offset(cx, cy),
      r,
      ui.Paint()
        ..color = const Color(0xAA000A14)
        ..style = ui.PaintingStyle.fill,
    );
    c.drawCircle(
      Offset(cx, cy),
      r,
      ui.Paint()
        ..color = const Color(0x5500CCFF)
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 2,
    );
    // "N" label
    _text(
      c,
      'N',
      x: cx - r,
      y: cy - 12,
      size: 16,
      color: const Color(0xFFFF5533),
      bold: true,
      align: ui.TextAlign.center,
      maxW: r * 2,
    );
  }

  // Draw a scaled/simplified version of the maneuver arrow into the bottom bar
  static void _drawGeometricManeuver(
    ui.Canvas c,
    Maneuver m,
    double centerX,
    double centerY,
  ) {
    final paint = _stroke(w: 10); // Thinner stroke for the small bar
    final size = 32.0;
    final top = centerY - size / 2;
    final bot = centerY + size / 2;

    switch (m) {
      case Maneuver.straight:
      case Maneuver.unknown:
      case Maneuver.merge:
        c.drawLine(Offset(centerX, bot), Offset(centerX, top + 8), paint);
        _arrowhead(c, Offset(centerX, top), -math.pi / 2, size: 16);
      case Maneuver.turnRight:
      case Maneuver.slightRight:
      case Maneuver.sharpRight:
        c.drawLine(
          Offset(centerX - 10, bot),
          Offset(centerX - 10, centerY - 6),
          paint,
        );
        final path = ui.Path()
          ..moveTo(centerX - 10, centerY - 6)
          ..quadraticBezierTo(
            centerX - 10,
            centerY - 14,
            centerX + 10,
            centerY - 14,
          );
        c.drawPath(path, paint);
        _arrowhead(c, Offset(centerX + 18, centerY - 14), 0, size: 16);
      case Maneuver.turnLeft:
      case Maneuver.slightLeft:
      case Maneuver.sharpLeft:
        c.drawLine(
          Offset(centerX + 10, bot),
          Offset(centerX + 10, centerY - 6),
          paint,
        );
        final path = ui.Path()
          ..moveTo(centerX + 10, centerY - 6)
          ..quadraticBezierTo(
            centerX + 10,
            centerY - 14,
            centerX - 10,
            centerY - 14,
          );
        c.drawPath(path, paint);
        _arrowhead(c, Offset(centerX - 18, centerY - 14), math.pi, size: 16);
      case Maneuver.uTurn:
        c.drawLine(
          Offset(centerX + 8, bot),
          Offset(centerX + 8, centerY - 6),
          paint,
        );
        final rect = Rect.fromLTRB(
          centerX - 8,
          centerY - 16,
          centerX + 12,
          centerY,
        );
        c.drawArc(rect, 0, -math.pi, false, paint);
        c.drawLine(
          Offset(centerX - 8, centerY - 6),
          Offset(centerX - 8, bot - 4),
          paint,
        );
        _arrowhead(c, Offset(centerX - 8, bot), math.pi / 2, size: 14);
      case Maneuver.roundabout:
        c.drawCircle(Offset(centerX, centerY), 12, paint);
        _arrowhead(c, Offset(centerX + 12, centerY), math.pi / 2, size: 12);
      case Maneuver.arrive:
        final path = ui.Path()
          ..moveTo(centerX - 12, centerY)
          ..lineTo(centerX - 4, centerY + 8)
          ..lineTo(centerX + 16, centerY - 10);
        c.drawPath(path, _stroke(w: 8, color: _green));
    }
  }
}
