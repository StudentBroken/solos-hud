import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/gps/gps_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import 'speedometer_widget.dart';

class SpeedometerApp extends GlassesApp {
  final GpsService gps;
  final AppSettings settings;

  SpeedometerApp({required this.gps, required this.settings});

  @override String   get id          => 'speedometer';
  @override String   get name        => 'Speedometer';
  @override IconData get icon        => Icons.speed;
  @override String   get description => 'GPS speed displayed on glasses HUD';
  @override int      get preferredRefreshMs => 500;

  @override
  void onActivate() => gps.init();

  @override
  void onDeactivate() {}

  // No text payload — we render a custom frame
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

  Future<Uint8List> _renderFrame() async {
    final dw = SolosProtocol.displayWidth.toDouble();
    final dh = SolosProtocol.displayHeight.toDouble();

    const bg    = Color(0xFF030508);
    const cyan  = Color(0xFF00CCFF);
    const white = Color(0xFFFFFFFF);
    const muted = Color(0xFF446688);
    const red   = Color(0xFFFF3333);

    final rec    = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, dw, dh));
    canvas.drawRect(Rect.fromLTWH(0, 0, dw, dh),
        ui.Paint()..color = bg..style = ui.PaintingStyle.fill);

    final showHeading  = settings.getAppValue('speedometer_show_heading', false) as bool;
    final showAltitude = settings.getAppValue('speedometer_show_altitude', false) as bool;

    if (gps.status != GpsStatus.available) {
      // Status screen
      final label = switch (gps.status) {
        GpsStatus.uninitialized || GpsStatus.requesting => 'GPS starting…',
        GpsStatus.denied   => 'GPS denied',
        GpsStatus.disabled => 'GPS off',
        GpsStatus.available => '',
      };
      _text(canvas, label,
          x: 0, y: dh / 2 - 20, size: 36, color: muted,
          align: ui.TextAlign.center, maxW: dw);
      return _encode(rec, dw.toInt(), dh.toInt());
    }

    final data  = gps.data;
    final speed = data.speedIn(settings.speedUnit);
    final unit  = settings.speedUnit;

    // How much vertical space does the bottom row need?
    final hasBottom = showHeading || showAltitude;
    final numY  = hasBottom ? 10.0  : 18.0;
    final numSz = hasBottom ? 130.0 : 148.0;
    final unitY = numY + numSz + 4;

    // ── Giant speed number ────────────────────────────────────────────────
    _text(canvas, speed.toStringAsFixed(1),
        x: 0, y: numY, size: numSz, color: cyan, bold: true,
        align: ui.TextAlign.center, maxW: dw);

    // ── Unit ──────────────────────────────────────────────────────────────
    _text(canvas, unit,
        x: 0, y: unitY, size: 30, color: muted,
        align: ui.TextAlign.center, maxW: dw);

    // ── Bottom row: heading + altitude ────────────────────────────────────
    if (hasBottom) {
      final parts = <String>[];
      if (showHeading && data.heading != null) {
        parts.add('${data.heading!.round()}°');
      }
      if (showAltitude && data.altitude != null) {
        parts.add('${data.altitude!.round()} m');
      }
      _text(canvas, parts.join('   '),
          x: 0, y: dh - 38, size: 28, color: const Color(0xFF335577),
          align: ui.TextAlign.center, maxW: dw);
    }

    // GPS accuracy dot (top-right corner)
    final acc = data.accuracy ?? 999.0;
    final dotColor = acc < 10 ? const Color(0xFF00FF66)
        : acc < 25 ? const Color(0xFFFFAA00)
        : red;
    canvas.drawCircle(
      Offset(dw - 16, 16),
      7,
      ui.Paint()..color = dotColor,
    );

    return _encode(rec, dw.toInt(), dh.toInt());
  }

  Future<Uint8List> _encode(ui.PictureRecorder rec, int w, int h) async {
    final picture = rec.endRecording();
    final image   = await picture.toImage(w, h);
    final bd      = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(bd!.buffer.asUint8List(), w, h);
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
  }) {
    final b = ui.ParagraphBuilder(
          ui.ParagraphStyle(
            textAlign:  align,
            fontSize:   size,
            fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            maxLines:   1,
            ellipsis:   '…',
          ),
        )
      ..pushStyle(ui.TextStyle(
        color:      color,
        fontSize:   size,
        fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
      ))
      ..addText(text);
    final para = b.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  @override
  Widget buildPhoneWidget(BuildContext context) => SpeedometerWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (!gps.available) {
      return Text(gps.status.name,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey));
    }
    final speed = gps.data.speedIn(settings.speedUnit);
    return Text(
      '${speed.toStringAsFixed(1)} ${settings.speedUnit}',
      style: Theme.of(context).textTheme.bodySmall
          ?.copyWith(fontFamily: 'monospace', fontWeight: FontWeight.bold),
    );
  }

  @override
  List<AppSettingEntry> get settingEntries => [
    const AppSettingEntry(
      key: 'speedometer_show_heading',
      label: 'Show Heading on Glasses',
      type: AppSettingType.toggle,
      defaultValue: false,
    ),
    const AppSettingEntry(
      key: 'speedometer_show_altitude',
      label: 'Show Altitude on Glasses',
      type: AppSettingType.toggle,
      defaultValue: false,
    ),
  ];
}
