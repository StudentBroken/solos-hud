import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';

// ── Colour palette ────────────────────────────────────────────────────────────
const _bg = Color(0xFF000000);
const _accent = Color(0xFF0066FF); // blue accent lines
const _primary = Color(0xFF00CCFF); // cyan — primary metric
const _white = Color(0xFFFFFFFF); // labels + secondary values
const _muted = Color(0xFF88AABB); // dim labels
const _green = Color(0xFF00FF88); // good HR / charging
const _orange = Color(0xFFFFAA00); // mid HR / warning
const _red = Color(0xFFFF3333); // high HR / low battery

// ── Data bag ──────────────────────────────────────────────────────────────────

class DashboardData {
  final DateTime time;
  final double? speedKmh;
  final String speedUnit;
  final int? heartRate;
  final int? batteryPct;
  final bool batteryCharging;
  final double? headingDeg;
  final String? extraLabel; // optional extra line

  const DashboardData({
    required this.time,
    this.speedKmh,
    this.speedUnit = 'km/h',
    this.heartRate,
    this.batteryPct,
    this.batteryCharging = false,
    this.headingDeg,
    this.extraLabel,
  });
}

class DashboardConfig {
  final bool showTime;
  final bool showBattery;
  final bool showSpeed;
  final bool showHr;
  final bool showCompass;
  final String primaryMetric; // 'speed' | 'hr' | 'time'

  const DashboardConfig({
    this.showTime = true,
    this.showBattery = true,
    this.showSpeed = true,
    this.showHr = true,
    this.showCompass = true,
    this.primaryMetric = 'speed',
  });
}

// ── Renderer ──────────────────────────────────────────────────────────────────

class DashboardRenderer {
  static final _w = SolosProtocol.displayWidth.toDouble(); // 428
  static final _h = SolosProtocol.displayHeight.toDouble(); // 240

  /// Render and return RLE565-encoded bytes ready for buildImagePacket().
  static Future<Uint8List> render(
    DashboardData data,
    DashboardConfig cfg,
  ) async {
    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder, Rect.fromLTWH(0, 0, _w, _h));

    _drawBackground(canvas);
    _drawTopBar(canvas, data, cfg);
    _drawAccentLine(canvas, y: 44);
    _drawPrimary(canvas, data, cfg);
    _drawAccentLine(canvas, y: 185);
    _drawBottomBar(canvas, data, cfg);

    final picture = recorder.endRecording();
    final image = await picture.toImage(
      SolosProtocol.displayWidth,
      SolosProtocol.displayHeight,
    );
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    // JPEG is used instead of RLE565: anti-aliased text creates too many unique
    // pixel colours for RLE to compress well (~30–50 KB) whereas JPEG q=80
    // yields 5–12 KB — safely within the RFCOMM packet budget.
    return encodeJpegAsync(
      byteData!.buffer.asUint8List(),
      SolosProtocol.displayWidth,
      SolosProtocol.displayHeight,
      quality: 80,
    );
  }

  // ── Sections ────────────────────────────────────────────────────────────────

  static void _drawBackground(ui.Canvas canvas) {
    canvas.drawRect(Rect.fromLTWH(0, 0, _w, _h), _fill(_bg));
  }

  static void _drawAccentLine(ui.Canvas canvas, {required double y}) {
    canvas.drawRect(Rect.fromLTWH(0, y, _w, 2), _fill(_accent));
  }

  static void _drawTopBar(
    ui.Canvas canvas,
    DashboardData d,
    DashboardConfig cfg,
  ) {
    // Left: time
    if (cfg.showTime) {
      final h = d.time.hour.toString().padLeft(2, '0');
      final m = d.time.minute.toString().padLeft(2, '0');
      final s = d.time.second.toString().padLeft(2, '0');
      _drawText(
        canvas,
        '$h:$m:$s',
        x: 12,
        y: 6,
        size: 28,
        color: _white,
        bold: true,
        align: ui.TextAlign.left,
        maxW: 180,
      );
    }

    // Right: battery
    if (cfg.showBattery && d.batteryPct != null) {
      final pct = d.batteryPct!;
      final batColor = d.batteryCharging ? _green : (pct > 20 ? _white : _red);
      final charging = d.batteryCharging ? ' ⚡' : '';
      _drawText(
        canvas,
        '$pct%$charging',
        x: _w - 12,
        y: 6,
        size: 24,
        color: batColor,
        align: ui.TextAlign.right,
        maxW: 160,
      );
    }
  }

  static void _drawPrimary(
    ui.Canvas canvas,
    DashboardData d,
    DashboardConfig cfg,
  ) {
    // Determine what to show large in the center
    String bigValue;
    String bigUnit;
    Color bigColor;

    switch (cfg.primaryMetric) {
      case 'hr':
        bigValue = d.heartRate != null ? '${d.heartRate}' : '--';
        bigUnit = 'bpm';
        final hr = d.heartRate ?? 0;
        bigColor = hr > 160
            ? _red
            : hr > 130
            ? _orange
            : _primary;
      case 'time':
        final now = d.time;
        bigValue =
            '${now.hour.toString().padLeft(2, '0')}:${now.minute.toString().padLeft(2, '0')}';
        bigUnit = '';
        bigColor = _primary;
      default: // speed
        if (d.speedKmh != null) {
          bigValue = _formatSpeed(d.speedKmh!, d.speedUnit);
          bigUnit = d.speedUnit;
        } else {
          bigValue = '--';
          bigUnit = '';
        }
        bigColor = _primary;
    }

    // Big value
    _drawText(
      canvas,
      bigValue,
      x: _w / 2,
      y: 52,
      size: 92,
      color: bigColor,
      bold: true,
      align: ui.TextAlign.center,
      maxW: _w - 20,
    );

    // Unit label below
    if (bigUnit.isNotEmpty) {
      _drawText(
        canvas,
        bigUnit,
        x: _w / 2,
        y: 158,
        size: 24,
        color: _muted,
        align: ui.TextAlign.center,
        maxW: _w,
      );
    }
  }

  static void _drawBottomBar(
    ui.Canvas canvas,
    DashboardData d,
    DashboardConfig cfg,
  ) {
    // Up to 3 items distributed across the bottom row (y ≈ 192–235)
    final items = <(String label, String value, Color color)>[];

    if (cfg.showHr && d.heartRate != null) {
      final hr = d.heartRate!;
      final c = hr > 160
          ? _red
          : hr > 130
          ? _orange
          : _green;
      items.add(('HR', '${d.heartRate} bpm', c));
    } else if (cfg.showHr) {
      items.add(('HR', '-- bpm', _muted));
    }

    if (cfg.showSpeed && cfg.primaryMetric != 'speed' && d.speedKmh != null) {
      items.add((
        'SPD',
        '${_formatSpeed(d.speedKmh!, d.speedUnit)} ${d.speedUnit}',
        _white,
      ));
    }

    if (cfg.showCompass && d.headingDeg != null) {
      items.add((
        'HDG',
        '${_compassLabel(d.headingDeg!)} ${d.headingDeg!.toStringAsFixed(0)}°',
        _white,
      ));
    }

    if (items.isEmpty) return;

    final slotW = _w / items.length;
    for (int i = 0; i < items.length; i++) {
      final (label, value, color) = items[i];
      final cx = slotW * i + slotW / 2;

      // Draw vertical separator between slots
      if (i > 0) {
        canvas.drawRect(
          Rect.fromLTWH(slotW * i - 1, 190, 1, 48),
          _fill(_accent.withValues(alpha: 0.5)),
        );
      }

      _drawText(
        canvas,
        label,
        x: cx,
        y: 192,
        size: 14,
        color: _muted,
        align: ui.TextAlign.center,
        maxW: slotW - 8,
      );
      _drawText(
        canvas,
        value,
        x: cx,
        y: 208,
        size: 24,
        color: color,
        bold: true,
        align: ui.TextAlign.center,
        maxW: slotW - 8,
      );
    }
  }

  // ── Helpers ─────────────────────────────────────────────────────────────────

  static String _formatSpeed(double kmh, String unit) {
    switch (unit) {
      case 'mph':
        return (kmh * 0.621371).toStringAsFixed(1);
      case 'm/s':
        return (kmh / 3.6).toStringAsFixed(1);
      default:
        return kmh.toStringAsFixed(1);
    }
  }

  static String _compassLabel(double deg) {
    const dirs = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
    return dirs[((deg + 22.5) / 45).floor() % 8];
  }

  static ui.Paint _fill(Color c) => ui.Paint()..color = c;

  static void _drawText(
    ui.Canvas canvas,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    bool bold = false,
    ui.TextAlign align = ui.TextAlign.left,
    required double maxW,
  }) {
    if (text.isEmpty) return;
    final pb =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: align,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w900 : ui.FontWeight.w400,
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w900 : ui.FontWeight.w400,
            ),
          )
          ..addText(text);
    final para = pb.build()..layout(ui.ParagraphConstraints(width: maxW));

    final drawX = switch (align) {
      ui.TextAlign.center => x - maxW / 2,
      ui.TextAlign.right => x - maxW,
      _ => x,
    };
    canvas.drawParagraph(para, Offset(drawX, y));
  }
}
