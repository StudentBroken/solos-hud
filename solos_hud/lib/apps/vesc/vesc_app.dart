import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/vesc/vesc_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import 'vesc_widget.dart';

class VescApp extends GlassesApp {
  final VescService vesc;
  VescApp({required this.vesc});

  @override
  String get id => 'vesc';
  @override
  String get name => 'VESC';
  @override
  IconData get icon => Icons.electric_bolt;
  @override
  String get description => 'Motor telemetry: speed, voltage, watts, temp';

  @override
  void onActivate() {}
  @override
  void onDeactivate() {}

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
    final v = vesc.values;
    final dw = SolosProtocol.displayWidth.toDouble();
    final dh = SolosProtocol.displayHeight.toDouble();

    const bg = Color(0xFF06080F);
    const cyan = Color(0xFF00CCFF);
    const white = Color(0xFFFFFFFF);
    const muted = Color(0xFF88AABB);
    const green = Color(0xFF00FF88);
    const orange = Color(0xFFFFAA00);
    const red = Color(0xFFFF3333);
    const divider = Color(0xFF111820);

    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, dw, dh));

    // Background
    canvas.drawRect(
      Rect.fromLTWH(0, 0, dw, dh),
      ui.Paint()
        ..color = bg
        ..style = ui.PaintingStyle.fill,
    );

    if (v == null || !vesc.isConnected) {
      // Not connected screen
      _text(
        canvas,
        'VESC',
        x: 0,
        y: 60,
        size: 52,
        color: muted,
        bold: true,
        align: ui.TextAlign.center,
        maxW: dw,
      );
      _text(
        canvas,
        vesc.scanning ? 'Scanning…' : 'Not connected',
        x: 0,
        y: 126,
        size: 22,
        color: muted,
        align: ui.TextAlign.center,
        maxW: dw,
      );
      _text(
        canvas,
        'Connect in the VESC app card',
        x: 0,
        y: 158,
        size: 15,
        color: const Color(0xFF223344),
        align: ui.TextAlign.center,
        maxW: dw,
      );
    } else {
      final speed = v.speedKmh(
        motorPoles: vesc.motorPoles,
        wheelDiameterMm: vesc.wheelDiameterMm,
      );
      final faultColor = v.faultCode == 0 ? muted : red;

      // Battery Logic
      final s = vesc.settings;
      final adjustedV = v.voltageV + s.vescVoltageOffset;
      final cellV = adjustedV / s.vescCellCount;

      final isLiFe = s.vescBatteryChem == 'lifepo4';
      final emptyV = isLiFe ? 2.8 : 3.2;
      final fullV = isLiFe ? 3.6 : 4.2;
      final batPct = ((cellV - emptyV) / (fullV - emptyV)).clamp(0.0, 1.0);

      final voltColor = (cellV > (emptyV + 0.5))
          ? green
          : (cellV > (emptyV + 0.2))
          ? orange
          : red;

      // Top bar: voltage + battery-bar + fault
      _text(
        canvas,
        '${adjustedV.isFinite ? adjustedV.toStringAsFixed(1) : "0.0"}V',
        x: 12,
        y: 8,
        size: 22,
        color: voltColor,
        bold: true,
        align: ui.TextAlign.left,
        maxW: 110,
      );
      // Battery bar
      canvas.drawRRect(
        RRect.fromRectAndRadius(
          Rect.fromLTWH(120, 14, 100, 12),
          const Radius.circular(3),
        ),
        ui.Paint()
          ..color = const Color(0xFF1A2A3A)
          ..style = ui.PaintingStyle.fill,
      );
      canvas.drawRRect(
        RRect.fromRectAndRadius(
          Rect.fromLTWH(120, 14, 100 * batPct, 12),
          const Radius.circular(3),
        ),
        ui.Paint()
          ..color = batPct > 0.3 ? green : red
          ..style = ui.PaintingStyle.fill,
      );
      // Duty cycle
      final duty = (v.dutyCycle * 100).clamp(0, 100).toInt();
      _text(
        canvas,
        'Duty $duty%',
        x: 230,
        y: 8,
        size: 18,
        color: white,
        align: ui.TextAlign.left,
        maxW: 100,
      );
      // Fault
      _text(
        canvas,
        v.faultLabel,
        x: dw - 120,
        y: 8,
        size: 18,
        color: faultColor,
        bold: v.faultCode != 0,
        align: ui.TextAlign.right,
        maxW: 118,
      );

      canvas.drawRect(
        Rect.fromLTWH(0, 38, dw, 1),
        ui.Paint()
          ..color = divider
          ..style = ui.PaintingStyle.fill,
      );

      // Big speed centre
      final speedStr = speed.isFinite ? speed.toStringAsFixed(1) : "0.0";
      _text(
        canvas,
        speedStr,
        x: 0,
        y: 48,
        size: 100,
        color: cyan,
        bold: true,
        align: ui.TextAlign.center,
        maxW: dw,
      );
      _text(
        canvas,
        'km/h',
        x: 0,
        y: 154,
        size: 22,
        color: muted,
        align: ui.TextAlign.center,
        maxW: dw,
      );

      canvas.drawRect(
        Rect.fromLTWH(0, 184, dw, 1),
        ui.Paint()
          ..color = divider
          ..style = ui.PaintingStyle.fill,
      );

      // Bottom bar: watts · motor A · motor °C · FET °C
      final watts = v.watts.isFinite ? v.watts.round().toString() : "0";
      final amps = v.motorCurrentA.isFinite
          ? v.motorCurrentA.abs().toStringAsFixed(1)
          : "0.0";
      final mTemp = v.tempMotor.isFinite ? v.tempMotor.round().toString() : "0";
      final fTemp = v.tempFet.isFinite ? v.tempFet.round().toString() : "0";

      final bottomStats = [
        ('$watts W', white),
        ('$amps A', white),
        (
          'M $mTemp°C',
          v.tempMotor > 80
              ? orange
              : v.tempMotor > 60
              ? const Color(0xFFFFDD88)
              : muted,
        ),
        ('FET $fTemp°C', v.tempFet > 70 ? orange : muted),
      ];
      final colW = dw / bottomStats.length;
      for (int i = 0; i < bottomStats.length; i++) {
        _text(
          canvas,
          bottomStats[i].$1,
          x: colW * i + 4,
          y: 192,
          size: 19,
          color: bottomStats[i].$2,
          bold: true,
          align: ui.TextAlign.left,
          maxW: colW - 4,
        );
      }
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

  @override
  Widget buildPhoneWidget(BuildContext context) => VescPhoneWidget(vesc: vesc);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (!vesc.isConnected) {
      return Text(
        vesc.scanning ? 'Scanning…' : 'Not connected',
        style: Theme.of(
          context,
        ).textTheme.bodySmall?.copyWith(color: Colors.grey),
      );
    }
    final v = vesc.values;
    final speed = v?.speedKmh(
      motorPoles: vesc.motorPoles,
      wheelDiameterMm: vesc.wheelDiameterMm,
    );
    return Text(
      '${speed?.toStringAsFixed(1) ?? '--'} km/h  '
      '${v?.voltageV.toStringAsFixed(1) ?? '--'}V  '
      '${v?.watts.round() ?? '--'}W',
      style: Theme.of(context).textTheme.bodySmall?.copyWith(
        fontFamily: 'monospace',
        color: Colors.cyanAccent,
      ),
    );
  }
}
