import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/vesc/vesc_service.dart';
import '../../core/vesc/vesc_packet.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import 'vesc_widget.dart';

/// Focus slots shown large on the glasses display.
enum VescFocus {
  speed(0, 'Speed'),
  power(1, 'Power'),
  phaseCurrent(2, 'Phase A'),
  battery(3, 'Battery');

  final int slot;
  final String label;
  const VescFocus(this.slot, this.label);
}

class VescApp extends GlassesApp {
  final VescService vesc;
  VescApp({required this.vesc});

  VescFocus _focus = VescFocus.speed;
  VescFocus get focus => _focus;

  void cycleFocusNext() {
    _focus =
        VescFocus.values[(_focus.slot + 1) % VescFocus.values.length];
  }

  void cycleFocusPrev() {
    _focus = VescFocus.values[
        (_focus.slot - 1 + VescFocus.values.length) %
            VescFocus.values.length];
  }

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

  // FRONT short = prev focus, BACK short = next focus
  @override
  bool onButtonEvent(GlassesEvent event) {
    if (event.action != GlassesActionType.buttonShort) return false;
    if (event.button == GlassesButton.front) {
      cycleFocusPrev();
      return true;
    }
    if (event.button == GlassesButton.back) {
      cycleFocusNext();
      return true;
    }
    return false;
  }

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

  String _focusValue(VescFocus f, VescValues v, double speed, double batPct) {
    switch (f) {
      case VescFocus.speed:
        return speed.isFinite ? speed.toStringAsFixed(1) : '0.0';
      case VescFocus.power:
        return v.watts.isFinite ? v.watts.round().toString() : '0';
      case VescFocus.phaseCurrent:
        return v.motorCurrentA.isFinite
            ? v.motorCurrentA.abs().toStringAsFixed(1)
            : '0.0';
      case VescFocus.battery:
        return '${(batPct * 100).round()}';
    }
  }

  String _focusUnit(VescFocus f) {
    switch (f) {
      case VescFocus.speed:        return 'km/h';
      case VescFocus.power:        return 'W';
      case VescFocus.phaseCurrent: return 'A phase';
      case VescFocus.battery:      return '% bat';
    }
  }

  Color _focusColor(VescFocus f, double batPct) {
    const cyan   = Color(0xFF00CCFF);
    const white  = Color(0xFFFFFFFF);
    const orange = Color(0xFFFFAA00);
    const green  = Color(0xFF00FF88);
    const red    = Color(0xFFFF3333);
    switch (f) {
      case VescFocus.speed:        return cyan;
      case VescFocus.power:        return white;
      case VescFocus.phaseCurrent: return orange;
      case VescFocus.battery:      return batPct > 0.3 ? green : red;
    }
  }

  Future<Uint8List> _renderFrame() async {
    final v = vesc.values;
    final dw = SolosProtocol.displayWidth.toDouble();
    final dh = SolosProtocol.displayHeight.toDouble();

    const bg      = Color(0xFF06080F);
    const cyan    = Color(0xFF00CCFF);
    const muted   = Color(0xFF88AABB);
    const green   = Color(0xFF00FF88);
    const orange  = Color(0xFFFFAA00);
    const red     = Color(0xFFFF3333);
    const divider = Color(0xFF111820);

    final rec    = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, dw, dh));

    canvas.drawRect(Rect.fromLTWH(0, 0, dw, dh),
        ui.Paint()..color = bg..style = ui.PaintingStyle.fill);

    if (v == null || !vesc.isConnected) {
      _text(canvas, 'VESC',
          x: 0, y: 60, size: 52, color: muted, bold: true,
          align: ui.TextAlign.center, maxW: dw);
      _text(canvas, vesc.scanning ? 'Scanning…' : 'Not connected',
          x: 0, y: 126, size: 22, color: muted,
          align: ui.TextAlign.center, maxW: dw);
      _text(canvas, 'Connect in the VESC app card',
          x: 0, y: 158, size: 15, color: const Color(0xFF223344),
          align: ui.TextAlign.center, maxW: dw);
    } else {
      final speed = v.speedKmh(
        motorPoles: vesc.motorPoles,
        wheelDiameterMm: vesc.wheelDiameterMm,
      );

      // Battery
      final s          = vesc.settings;
      final adjustedV  = v.voltageV + s.vescVoltageOffset;
      final cellV      = adjustedV / s.vescCellCount;
      final isLiFe     = s.vescBatteryChem == 'lifepo4';
      final emptyV     = isLiFe ? 2.8 : 3.2;
      final fullV      = isLiFe ? 3.6 : 4.2;
      final batPct     = ((cellV - emptyV) / (fullV - emptyV)).clamp(0.0, 1.0);
      final batPctInt  = (batPct * 100).round();

      final voltColor  = cellV > emptyV + 0.5
          ? green
          : cellV > emptyV + 0.2
              ? orange
              : red;

      // ── Top bar: voltage + battery bar + battery % ─────────────────────
      _text(canvas,
          '${adjustedV.isFinite ? adjustedV.toStringAsFixed(1) : "0.0"}V',
          x: 12, y: 8, size: 22, color: voltColor, bold: true,
          align: ui.TextAlign.left, maxW: 100);

      canvas.drawRRect(
        RRect.fromRectAndRadius(Rect.fromLTWH(116, 14, 90, 12),
            const Radius.circular(3)),
        ui.Paint()..color = const Color(0xFF1A2A3A)..style = ui.PaintingStyle.fill,
      );
      canvas.drawRRect(
        RRect.fromRectAndRadius(
            Rect.fromLTWH(116, 14, 90 * batPct, 12), const Radius.circular(3)),
        ui.Paint()
          ..color = batPct > 0.3 ? green : red
          ..style = ui.PaintingStyle.fill,
      );
      _text(canvas, '$batPctInt%',
          x: 212, y: 8, size: 18,
          color: batPct > 0.3 ? green : red, bold: true,
          align: ui.TextAlign.left, maxW: 60);

      final faultColor = v.faultCode == 0 ? muted : red;
      _text(canvas, v.faultLabel,
          x: dw - 120, y: 8, size: 18, color: faultColor,
          bold: v.faultCode != 0, align: ui.TextAlign.right, maxW: 118);

      canvas.drawRect(Rect.fromLTWH(0, 38, dw, 1),
          ui.Paint()..color = divider..style = ui.PaintingStyle.fill);

      // ── Focus tabs ─────────────────────────────────────────────────────
      final tabW = dw / VescFocus.values.length;
      for (final f in VescFocus.values) {
        final isSelected = f == _focus;
        _text(canvas, f.label,
            x: tabW * f.slot, y: 42, size: 13,
            color: isSelected ? cyan : const Color(0xFF334455),
            bold: isSelected,
            align: ui.TextAlign.center, maxW: tabW);
        if (isSelected) {
          canvas.drawRect(
            Rect.fromLTWH(tabW * f.slot + 4, 59, tabW - 8, 2),
            ui.Paint()..color = cyan..style = ui.PaintingStyle.fill,
          );
        }
      }

      canvas.drawRect(Rect.fromLTWH(0, 63, dw, 1),
          ui.Paint()..color = divider..style = ui.PaintingStyle.fill);

      // ── Big focused metric ─────────────────────────────────────────────
      final bigVal   = _focusValue(_focus, v, speed, batPct);
      final bigUnit  = _focusUnit(_focus);
      final bigColor = _focusColor(_focus, batPct);

      _text(canvas, bigVal,
          x: 0, y: 66, size: 96, color: bigColor, bold: true,
          align: ui.TextAlign.center, maxW: dw);
      _text(canvas, bigUnit,
          x: 0, y: 168, size: 22, color: muted,
          align: ui.TextAlign.center, maxW: dw);

      canvas.drawRect(Rect.fromLTWH(0, 196, dw, 1),
          ui.Paint()..color = divider..style = ui.PaintingStyle.fill);

      // ── Bottom bar: the other 3 metrics ────────────────────────────────
      final others =
          VescFocus.values.where((f) => f != _focus).toList();
      final colW = dw / 3;
      for (int i = 0; i < others.length; i++) {
        final f     = others[i];
        final val   = _focusValue(f, v, speed, batPct);
        final unit  = _focusUnit(f);
        final color = _focusColor(f, batPct);
        _text(canvas, '${f.label}  $val $unit',
            x: colW * i + 4, y: 203, size: 16, color: color, bold: true,
            align: ui.TextAlign.left, maxW: colW - 4);
      }
    }

    final picture = rec.endRecording();
    final image   = await picture.toImage(
        SolosProtocol.displayWidth, SolosProtocol.displayHeight);
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(
        bd!.buffer.asUint8List(),
        SolosProtocol.displayWidth,
        SolosProtocol.displayHeight);
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
            textAlign: align,
            fontSize: size,
            fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            maxLines: 2,
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

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      VescPhoneWidget(vesc: vesc, app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (!vesc.isConnected) {
      return Text(
        vesc.scanning ? 'Scanning…' : 'Not connected',
        style:
            Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey),
      );
    }
    final v     = vesc.values;
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
