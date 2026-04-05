import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/gps/gps_service.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/sensors/speed_fusion_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import '../../core/settings/app_settings.dart';
import 'pacer_widget.dart';

class PacerApp extends GlassesApp {
  final GpsService gps;
  final HeartRateService hr;
  final SpeedFusionService fusion;
  final AppSettings settings;

  PacerApp({
    required this.gps,
    required this.hr,
    required this.fusion,
    required this.settings,
  });

  static const _keyGoalPace = 'pacer_goal_pace_sec_per_km';

  // Goal pace in seconds/km — 0 = not set
  double _goalPaceSecPerKm = 0;
  double get goalPaceSecPerKm => _goalPaceSecPerKm;
  set goalPaceSecPerKm(double v) {
    _goalPaceSecPerKm = v;
    settings.setAppValue(_keyGoalPace, v);
  }

  @override String get id => 'pacer';
  @override String get name => 'Pacer';
  @override IconData get icon => Icons.straighten;
  @override String get description => 'Visual pace tracker on glasses';
  @override int get preferredCodec => SolosProtocol.codecJPEG;
  @override int get preferredRefreshMs => 333;

  @override
  void onActivate() {
    gps.init();
    fusion.start(gps);
    _goalPaceSecPerKm = (settings.getAppValue(_keyGoalPace, 0.0) as num).toDouble();
  }

  @override
  void onDeactivate() {}

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() => _renderFrame();

  Future<Uint8List?> _renderFrame() async {
    const w = SolosProtocol.displayWidth;   // 480
    const h = SolosProtocol.displayHeight;  // 240

    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder, Rect.fromLTWH(0, 0, w.toDouble(), h.toDouble()));

    // Background
    canvas.drawRect(
      Rect.fromLTWH(0, 0, w.toDouble(), h.toDouble()),
      ui.Paint()..color = const Color(0xFF050510),
    );

    const lineX = w / 3.0;           // vertical guide line at 1/3 width = 160
    const lineTop = 20.0;
    const lineBottom = 220.0;
    const centerY = (lineTop + lineBottom) / 2; // 120

    // ── Vertical guide line ───────────────────────────────────────────────
    canvas.drawLine(
      Offset(lineX, lineTop),
      Offset(lineX, lineBottom),
      ui.Paint()
        ..color = const Color(0xFF334466)
        ..strokeWidth = 2,
    );

    // ── Green target circle ───────────────────────────────────────────────
    canvas.drawCircle(
      const Offset(lineX, centerY),
      14,
      ui.Paint()..color = const Color(0xFF00CC44),
    );
    canvas.drawCircle(
      const Offset(lineX, centerY),
      14,
      ui.Paint()
        ..color = const Color(0xFF003311)
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 2,
    );

    // ── Tracker position — use Kalman-fused speed for smooth, low-lag pace ──
    final currentSpeedMs = fusion.speedMs;
    final currentPaceSec = currentSpeedMs > 0.2 ? 1000.0 / currentSpeedMs : 0.0;
    double trackerY = centerY;
    if (goalPaceSecPerKm > 0 && currentPaceSec > 0) {
      final deviation = currentPaceSec - goalPaceSecPerKm; // + = slower
      final fraction = (deviation / 60.0).clamp(-1.0, 1.0);
      trackerY = centerY + fraction * 100.0;
    }

    // Tracker color: above center = faster (blue), below = slower (red)
    final trackerColor = trackerY < centerY - 4
        ? const Color(0xFF4499FF)
        : trackerY > centerY + 4
            ? const Color(0xFFFF4444)
            : const Color(0xFF44FF88);

    // Draw tracker as a horizontal rectangle
    canvas.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromCenter(center: Offset(lineX, trackerY), width: 54, height: 10),
        const Radius.circular(4),
      ),
      ui.Paint()..color = trackerColor,
    );

    // ── Right panel: pace + HR ────────────────────────────────────────────
    const rightCenter = (lineX + w) / 2; // midpoint of right section ≈ 320

    // Current pace (large)
    final paceStr = _formatPace(currentPaceSec);
    _drawText(canvas, paceStr,
        x: rightCenter, y: 48, size: 76, color: const Color(0xFFFFFFFF), center: true);

    _drawText(canvas, 'PACE /km',
        x: rightCenter, y: 136, size: 18, color: const Color(0xFF4477AA), center: true);

    // Goal pace indicator
    if (goalPaceSecPerKm > 0) {
      final goalStr = 'GOAL ${_formatPace(goalPaceSecPerKm)}';
      _drawText(canvas, goalStr,
          x: rightCenter, y: 162, size: 20, color: const Color(0xFFFFAA00), center: true);
    }

    // HR
    final hrStr = hr.heartRate != null ? '♥ ${hr.heartRate}' : '♥ --';
    final hrColor = hr.heartRate != null && hr.heartRate! > 160
        ? const Color(0xFFFF4444)
        : const Color(0xFF00FF88);
    _drawText(canvas, hrStr,
        x: rightCenter, y: 198, size: 30, color: hrColor, center: true);

    // ── Pace deviation label (left of line, small) ────────────────────────
    if (goalPaceSecPerKm > 0 && currentPaceSec > 0) {
      final dev = currentPaceSec - goalPaceSecPerKm;
      final sign = dev >= 0 ? '+' : '';
      final devMins = (dev.abs() ~/ 60);
      final devSecs = (dev.abs() % 60).round();
      final devStr = '$sign${devMins > 0 ? '${devMins}m' : ''}${devSecs}s';
      _drawText(canvas, devStr,
          x: lineX / 2, y: trackerY - 10, size: 16,
          color: trackerColor, center: true);
    }

    final picture = recorder.endRecording();
    final image = await picture.toImage(w, h);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (byteData == null) return null;
    return encodeJpegAsync(byteData.buffer.asUint8List(), w, h, quality: 82);
  }

  String _formatPace(double secPerKm) {
    if (secPerKm <= 0) return '--:--';
    final m = (secPerKm ~/ 60).toString().padLeft(2, '0');
    final s = (secPerKm % 60).round().toString().padLeft(2, '0');
    return '$m:$s';
  }

  void _drawText(ui.Canvas canvas, String text,
      {required double x, required double y, required double size,
      required Color color, bool center = false}) {
    final pb = ui.ParagraphBuilder(ui.ParagraphStyle(
      textAlign: ui.TextAlign.center,
      fontSize: size,
      fontWeight: ui.FontWeight.w700,
    ))
      ..pushStyle(ui.TextStyle(color: color, fontSize: size, fontWeight: ui.FontWeight.w700))
      ..addText(text);
    final maxW = SolosProtocol.displayWidth.toDouble();
    final para = pb.build()..layout(ui.ParagraphConstraints(width: maxW));
    canvas.drawParagraph(para, Offset(x - (center ? maxW / 2 : maxW / 2), y));
  }

  @override
  Widget buildPhoneWidget(BuildContext context) => PacerPhoneWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    final spd = fusion.speedMs;
    final pace = spd > 0.2 ? 1000.0 / spd : 0.0;
    final label = pace > 0 ? _formatPace(pace) : '--:--';
    return Text(
      'Pace: $label /km',
      style: Theme.of(context)
          .textTheme
          .bodySmall
          ?.copyWith(fontFamily: 'monospace', fontWeight: FontWeight.bold),
    );
  }
}
