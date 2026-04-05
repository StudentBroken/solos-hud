import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/weather/weather_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';

const _bg = Color(0xFF070D18);
const _cyan = Color(0xFF00CCFF);
const _white = Color(0xFFFFFFFF);
const _muted = Color(0xFF88AABB);
const _divider = Color(0xFF111C2A);

class WeatherRenderer {
  static const int _dw = SolosProtocol.displayWidth; // 428
  static const int _dh = SolosProtocol.displayHeight; // 240

  static Future<Uint8List> render(WeatherData data) async {
    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(
      rec,
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
    );

    canvas.drawRect(
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
      _fill(_bg),
    );

    _drawMain(canvas, data);
    _drawDetails(canvas, data);
    _drawHourly(canvas, data.hourly);

    final picture = rec.endRecording();
    final image = await picture.toImage(_dw, _dh);
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(bd!.buffer.asUint8List(), _dw, _dh);
  }

  // ── Top section: condition + temp + city ────────────────────────────────

  static void _drawMain(ui.Canvas c, WeatherData d) {
    // Large condition symbol — left side
    _text(
      c,
      d.condition.symbol,
      x: 16,
      y: 16,
      size: 72,
      color: _conditionColor(d.condition),
      align: ui.TextAlign.left,
      maxW: 90,
    );

    // Temperature — large, right of symbol
    _text(
      c,
      d.tempLabel,
      x: 110,
      y: 18,
      size: 64,
      color: _white,
      bold: true,
      align: ui.TextAlign.left,
      maxW: 160,
    );

    // Condition label
    _text(
      c,
      d.condition.label,
      x: 110,
      y: 90,
      size: 20,
      color: _muted,
      align: ui.TextAlign.left,
      maxW: 200,
    );

    // City — top right
    _text(
      c,
      d.city,
      x: _dw - 200.0,
      y: 18,
      size: 18,
      color: _muted,
      align: ui.TextAlign.right,
      maxW: 190,
    );

    // Feels like
    _text(
      c,
      d.feelsLabel,
      x: _dw - 200.0,
      y: 44,
      size: 16,
      color: _muted,
      align: ui.TextAlign.right,
      maxW: 190,
    );

    // Divider
    c.drawRect(Rect.fromLTWH(0, 118, _dw.toDouble(), 1), _fill(_divider));
  }

  // ── Middle section: wind · UV · humidity · rain ─────────────────────────

  static void _drawDetails(ui.Canvas c, WeatherData d) {
    const y = 126.0;
    final stats = [
      ('Wind', '${d.windSpeedKmh.round()} km/h ${d.windDirLabel}'),
      ('UV', d.uvIndex.round().toString()),
      ('Hum', '${d.humidity}%'),
      ('Rain', '${d.precipitationPct}%'),
    ];

    final colW = _dw / stats.length;
    for (int i = 0; i < stats.length; i++) {
      final x = colW * i + 4;
      _text(
        c,
        stats[i].$1,
        x: x,
        y: y,
        size: 13,
        color: _muted,
        align: ui.TextAlign.left,
        maxW: colW - 4,
      );
      _text(
        c,
        stats[i].$2,
        x: x,
        y: y + 18,
        size: 18,
        color: _cyan,
        bold: true,
        align: ui.TextAlign.left,
        maxW: colW - 4,
      );
    }

    c.drawRect(Rect.fromLTWH(0, 168, _dw.toDouble(), 1), _fill(_divider));
  }

  // ── Bottom: 24-hour forecast strip (every 2h = max 12 visible slots) ───────

  static void _drawHourly(ui.Canvas c, List<HourlyWeather> hourly) {
    if (hourly.isEmpty) return;

    // Sample every 2nd entry so we cover 24 h in 12 columns
    final slots = <HourlyWeather>[];
    for (int i = 0; i < hourly.length && slots.length < 12; i += 2) {
      slots.add(hourly[i]);
    }
    if (slots.isEmpty) return;

    const y = 172.0;
    final colW = _dw / slots.length;

    for (int i = 0; i < slots.length; i++) {
      final h = slots[i];
      final x = colW * i + 2;
      final hh = h.time.hour.toString().padLeft(2, '0');
      _text(c, '$hh:00',
          x: x, y: y, size: 11, color: _muted,
          align: ui.TextAlign.left, maxW: colW - 2);
      _text(c, h.condition.symbol,
          x: x, y: y + 14, size: 14,
          color: _conditionColor(h.condition),
          align: ui.TextAlign.left, maxW: colW - 2);
      _text(c, '${h.tempC.round()}°',
          x: x, y: y + 30, size: 14, color: _white,
          align: ui.TextAlign.left, maxW: colW - 2);
      if (h.precipPct != null && h.precipPct! >= 20) {
        _text(c, '${h.precipPct}%',
            x: x, y: y + 46, size: 10,
            color: const Color(0xFF4499FF),
            align: ui.TextAlign.left, maxW: colW - 2);
      }
    }
  }

  // ── Helpers ──────────────────────────────────────────────────────────────

  static Color _conditionColor(WeatherCondition c) => switch (c) {
    WeatherCondition.clear => const Color(0xFFFFDD44),
    WeatherCondition.mainlyClear => const Color(0xFFFFDD44),
    WeatherCondition.partlyCloudy => const Color(0xFFAABBCC),
    WeatherCondition.overcast => const Color(0xFF667788),
    WeatherCondition.fog => const Color(0xFF667788),
    WeatherCondition.drizzle => const Color(0xFF66AACC),
    WeatherCondition.rain => const Color(0xFF4499FF),
    WeatherCondition.heavyRain => const Color(0xFF2266DD),
    WeatherCondition.snow => const Color(0xFFCCEEFF),
    WeatherCondition.heavySnow => const Color(0xFFCCEEFF),
    WeatherCondition.rainShowers => const Color(0xFF4499FF),
    WeatherCondition.snowShowers => const Color(0xFFCCEEFF),
    WeatherCondition.thunderstorm => const Color(0xFFFFAA00),
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
    final b =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: align,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
              maxLines: 1,
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

  static ui.Paint _fill(Color color) => ui.Paint()
    ..color = color
    ..style = ui.PaintingStyle.fill;
}
