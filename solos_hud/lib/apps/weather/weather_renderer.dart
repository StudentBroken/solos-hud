import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/weather/weather_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';

const _bg      = Color(0xFF070D18);
const _white   = Color(0xFFFFFFFF);
const _muted   = Color(0xFF7799BB);
const _dim     = Color(0xFF334455);
const _cyan    = Color(0xFF00CCFF);
const _divider = Color(0xFF111C2A);

class WeatherRenderer {
  static const double _dw = SolosProtocol.displayWidth  + 0.0;  // 480
  static const double _dh = SolosProtocol.displayHeight + 0.0;  // 240

  static Future<Uint8List> render(WeatherData data) async {
    final rec    = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, _dw, _dh));

    // Background
    canvas.drawRect(Rect.fromLTWH(0, 0, _dw, _dh), _fill(_bg));

    _drawCurrent(canvas, data);
    _drawHourly(canvas, data.hourly);

    final picture = rec.endRecording();
    final image   = await picture.toImage(
        SolosProtocol.displayWidth, SolosProtocol.displayHeight);
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(
        bd!.buffer.asUint8List(),
        SolosProtocol.displayWidth,
        SolosProtocol.displayHeight);
  }

  // ── Current conditions — top 148px ───────────────────────────────────────

  static void _drawCurrent(ui.Canvas c, WeatherData d) {
    // Row 1: city (left) + feels-like (right)  y=6
    _text(c, d.city,
        x: 12, y: 6, size: 18, color: _muted,
        align: ui.TextAlign.left, maxW: 260);
    _text(c, d.feelsLabel,
        x: _dw - 220, y: 6, size: 18, color: _muted,
        align: ui.TextAlign.right, maxW: 210);

    // Giant temperature — left anchor  y=28
    _text(c, d.tempLabel,
        x: 10, y: 28, size: 96, color: _white, bold: true,
        align: ui.TextAlign.left, maxW: 240);

    // Condition symbol — right of temp  y=28
    _text(c, d.condition.symbol,
        x: 250, y: 28, size: 88,
        color: _conditionColor(d.condition),
        align: ui.TextAlign.left, maxW: 120);

    // Condition label  y=132
    _text(c, d.condition.label,
        x: 10, y: 132, size: 26, color: _muted,
        align: ui.TextAlign.left, maxW: 240);

    // Wind + rain in one compact line  y=132 right side
    final detail = '${d.windSpeedKmh.round()} km/h ${d.windDirLabel}'
        '  ·  ${d.precipitationPct}% rain';
    _text(c, detail,
        x: _dw - 300, y: 132, size: 20, color: _dim,
        align: ui.TextAlign.right, maxW: 290);

    // Divider
    c.drawRect(Rect.fromLTWH(0, 160, _dw, 1), _fill(_divider));
  }

  // ── Next 4 hours — bottom 79px ───────────────────────────────────────────

  static void _drawHourly(ui.Canvas c, List<HourlyWeather> hourly) {
    if (hourly.isEmpty) return;

    final slots = hourly.take(4).toList();
    final colW  = _dw / slots.length; // 120px per column

    for (int i = 0; i < slots.length; i++) {
      final h  = slots[i];
      final x  = colW * i;
      final hh = _hour12(h.time);

      // Time label
      _text(c, hh,
          x: x + 6, y: 166, size: 16, color: _muted,
          align: ui.TextAlign.left, maxW: colW - 6);

      // Condition symbol (larger)
      _text(c, h.condition.symbol,
          x: x + 6, y: 184, size: 22,
          color: _conditionColor(h.condition),
          align: ui.TextAlign.left, maxW: 36);

      // Temperature — dominant
      _text(c, '${h.tempC.round()}°',
          x: x + 36, y: 184, size: 30, color: _white, bold: true,
          align: ui.TextAlign.left, maxW: colW - 42);

      // Rain % only if notable
      if (h.precipPct != null && h.precipPct! >= 10) {
        _text(c, '${h.precipPct}%',
            x: x + 6, y: 218, size: 16, color: _cyan,
            align: ui.TextAlign.left, maxW: colW - 6);
      }

      // Column divider (skip last)
      if (i < slots.length - 1) {
        c.drawRect(
          Rect.fromLTWH(colW * (i + 1) - 0.5, 164, 1, 76),
          _fill(_divider),
        );
      }
    }
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  static Color _conditionColor(WeatherCondition cond) => switch (cond) {
    WeatherCondition.clear         => const Color(0xFFFFDD44),
    WeatherCondition.mainlyClear   => const Color(0xFFFFDD44),
    WeatherCondition.partlyCloudy  => const Color(0xFFAABBCC),
    WeatherCondition.overcast      => const Color(0xFF667788),
    WeatherCondition.fog           => const Color(0xFF667788),
    WeatherCondition.drizzle       => const Color(0xFF66AACC),
    WeatherCondition.rain          => const Color(0xFF4499FF),
    WeatherCondition.heavyRain     => const Color(0xFF2266DD),
    WeatherCondition.snow          => const Color(0xFFCCEEFF),
    WeatherCondition.heavySnow     => const Color(0xFFCCEEFF),
    WeatherCondition.rainShowers   => const Color(0xFF4499FF),
    WeatherCondition.snowShowers   => const Color(0xFFCCEEFF),
    WeatherCondition.thunderstorm  => const Color(0xFFFFAA00),
  };

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
    final b = ui.ParagraphBuilder(
          ui.ParagraphStyle(
            textAlign: align,
            fontSize: size,
            fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            maxLines: 1,
            ellipsis: '…',
          ),
        )
      ..pushStyle(ui.TextStyle(
        color: color,
        fontSize: size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    final para = b.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  /// "2pm", "12pm", "12am", "1am" — no leading zero, no minutes
  static String _hour12(DateTime t) {
    final h   = t.hour;
    final ampm = h < 12 ? 'am' : 'pm';
    final h12 = h % 12 == 0 ? 12 : h % 12;
    return '$h12$ampm';
  }

  static ui.Paint _fill(Color color) =>
      ui.Paint()..color = color..style = ui.PaintingStyle.fill;
}
