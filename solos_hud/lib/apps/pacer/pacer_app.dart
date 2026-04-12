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

    // ── Layout ────────────────────────────────────────────────────────────
    // Left 130px: thick vertical rail + tracker
    // Right 350px: pace number + goal
    const railX   = 65.0;
    const railTop = 12.0;
    const railBot = 228.0;
    const centerY = (railTop + railBot) / 2; // 120
    const halfRange = (railBot - railTop) / 2 - 8;

    // ── Calculations ──────────────────────────────────────────────────────
    final currentSpeedMs = fusion.speedMs;
    final currentPaceSec = currentSpeedMs > 0.2 ? 1000.0 / currentSpeedMs : 0.0;
    double trackerY = centerY;
    if (goalPaceSecPerKm > 0 && currentPaceSec > 0) {
      final fraction = ((currentPaceSec - goalPaceSecPerKm) / 60.0).clamp(-1.0, 1.0);
      trackerY = centerY + fraction * halfRange;
    }

    // Color driven by deviation — blue=fast, green=on, red=slow
    final trackerColor = trackerY < centerY - 6
        ? const Color(0xFF3399FF)
        : trackerY > centerY + 6
            ? const Color(0xFFFF3333)
            : const Color(0xFF00FF66);

    // ── Thick rail ────────────────────────────────────────────────────────
    canvas.drawLine(
      Offset(railX, railTop), Offset(railX, railBot),
      ui.Paint()
        ..color = const Color(0xFF1A2E48)
        ..strokeWidth = 8
        ..strokeCap = ui.StrokeCap.round,
    );

    // Tick marks (wider)
    for (final frac in [-1.0, -0.5, 0.5, 1.0]) {
      final ty = centerY + frac * halfRange;
      canvas.drawLine(
        Offset(railX - 14, ty), Offset(railX + 14, ty),
        ui.Paint()
          ..color = const Color(0xFF2A4466)
          ..strokeWidth = 3
          ..strokeCap = ui.StrokeCap.round,
      );
    }

    // Goal circle — large and bright
    canvas.drawCircle(Offset(railX, centerY), 22,
        ui.Paint()..color = const Color(0xFF00AA33));
    canvas.drawCircle(Offset(railX, centerY), 22,
        ui.Paint()
          ..color = const Color(0xFF00FF66).withValues(alpha: 0.4)
          ..style = ui.PaintingStyle.stroke
          ..strokeWidth = 4);

    // Tracker bar — thick and wide
    canvas.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromCenter(center: Offset(railX, trackerY), width: 96, height: 20),
        const Radius.circular(6),
      ),
      ui.Paint()..color = trackerColor,
    );
    // Glow ring around tracker
    canvas.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromCenter(center: Offset(railX, trackerY), width: 96, height: 20),
        const Radius.circular(6),
      ),
      ui.Paint()
        ..color = trackerColor.withValues(alpha: 0.35)
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 5,
    );

    // Deviation label beside tracker
    if (goalPaceSecPerKm > 0 && currentPaceSec > 0) {
      final dev    = currentPaceSec - goalPaceSecPerKm;
      final sign   = dev >= 0 ? '+' : '-';
      final abs    = dev.abs();
      final devStr = abs >= 60
          ? '$sign${(abs ~/ 60)}m${(abs % 60).round().toString().padLeft(2, '0')}s'
          : '$sign${abs.round()}s';
      _drawText(canvas, devStr,
          x: 2, y: trackerY - 11, size: 20, color: trackerColor, maxW: railX - 4);
    }

    // ── Separator ─────────────────────────────────────────────────────────
    canvas.drawLine(
      const Offset(126, 8), const Offset(126, 232),
      ui.Paint()..color = const Color(0xFF0E1A28)..strokeWidth = 1.5,
    );

    // ── Right panel ───────────────────────────────────────────────────────
    const panelX = 134.0;
    final panelW = w.toDouble() - panelX - 8;

    // BIG pace — color changes with state
    final paceStr = _formatPace(currentPaceSec);
    _drawText(canvas, paceStr,
        x: panelX, y: 8, size: 110, color: trackerColor, maxW: panelW);

    // Goal pace — large, prominent, only when set
    if (goalPaceSecPerKm > 0) {
      _drawText(canvas, 'GOAL  ${_formatPace(goalPaceSecPerKm)}',
          x: panelX, y: 168, size: 38, color: const Color(0xFFFFAA00),
          maxW: panelW);
    }

    final picture = recorder.endRecording();
    final image = await picture.toImage(w, h);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (byteData == null) return null;
    return encodeJpegAsync(byteData.buffer.asUint8List(), w, h, quality: 82);
  }

  /// Format pace without leading zero: 5:03 not 05:03
  String _formatPace(double secPerKm) {
    if (secPerKm <= 0) return '--:--';
    final m = (secPerKm ~/ 60).toInt();
    final s = (secPerKm % 60).round().toString().padLeft(2, '0');
    return '$m:$s';
  }

  void _drawText(ui.Canvas canvas, String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    required double maxW,
  }) {
    final pb = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        textAlign: ui.TextAlign.left,
        fontSize: size,
        fontWeight: ui.FontWeight.w700,
        maxLines: 1,
        ellipsis: '…',
      ),
    )
      ..pushStyle(ui.TextStyle(
          color: color, fontSize: size, fontWeight: ui.FontWeight.w700))
      ..addText(text);
    final para = pb.build()..layout(ui.ParagraphConstraints(width: maxW));
    canvas.drawParagraph(para, Offset(x, y));
  }

  @override
  Widget buildPhoneWidget(BuildContext context) => PacerPhoneWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    final spd = fusion.speedMs;
    final pace = spd > 0.2 ? 1000.0 / spd : 0.0;
    final label = pace > 0 ? _formatPace(pace) : '--:--';
    return Text(
      'Pace: $label',
      style: Theme.of(context)
          .textTheme
          .bodySmall
          ?.copyWith(fontFamily: 'monospace', fontWeight: FontWeight.bold),
    );
  }
}
