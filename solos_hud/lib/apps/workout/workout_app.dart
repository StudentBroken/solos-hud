import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/gps/gps_service.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/solos_protocol.dart';
import '../../core/render_utils.dart';
import '../../core/workout/workout_service.dart';
import 'workout_widget.dart';

class WorkoutApp extends GlassesApp {
  final WorkoutService workout;
  final GpsService gps;
  final HeartRateService hr;
  final AppSettings settings;

  WorkoutApp({
    required this.workout,
    required this.gps,
    required this.hr,
    required this.settings,
  });

  @override String get id => 'workout';
  @override String get name => 'Workout';
  @override int get preferredCodec => SolosProtocol.codecJPEG;
  @override IconData get icon => Icons.directions_run;
  @override String get description => 'Track runs, rides and workouts';

  @override
  void onActivate() {
    gps.init();
  }

  @override
  void onDeactivate() {
    if (workout.isActive) workout.pause();
  }

  @override
  String? buildGlassesPayload() => null; // always use custom frame

  @override
  Future<Uint8List?> buildCustomFrame() async {
    return _renderWorkoutFrame();
  }

  Color _goalPaceBgColor() {
    if (workout.goalPaceSecPerKm <= 0) return const Color(0xFF000000);
    final livePace = workout.currentLivePaceSecPerKm;
    if (livePace <= 0) return const Color(0xFF000000);
    final deviation = livePace - workout.goalPaceSecPerKm;
    final t = (deviation.abs() / 60.0).clamp(0.0, 1.0);
    if (deviation > 0) {
      return Color.lerp(const Color(0xFF002200), const Color(0xFF3A0000), t)!;
    } else {
      return Color.lerp(const Color(0xFF002200), const Color(0xFF00003A), t)!;
    }
  }

  Future<Uint8List?> _renderWorkoutFrame() async {
    const w = SolosProtocol.displayWidth;
    const h = SolosProtocol.displayHeight;

    final recorder = ui.PictureRecorder();
    final canvas = ui.Canvas(recorder, Rect.fromLTWH(0, 0, w.toDouble(), h.toDouble()));

    // Background — goal-pace tinted
    canvas.drawRect(
      Rect.fromLTWH(0, 0, w.toDouble(), h.toDouble()),
      ui.Paint()..color = _goalPaceBgColor(),
    );

    if (!workout.isRunning) {
      // Idle state
      _drawCenteredText(canvas, workout.state == WorkoutState.finished ? 'DONE' : 'READY',
          y: 80, size: 60, color: const Color(0xFF0088FF));
      _drawCenteredText(canvas, workout.type.label, y: 155, size: 26, color: const Color(0xFF4477AA));
    } else {
      // Active / paused
      _drawWorkoutActive(canvas, w.toDouble(), h.toDouble());
    }

    final picture = recorder.endRecording();
    final image = await picture.toImage(w, h);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (byteData == null) return null;
    return encodeJpegAsync(byteData.buffer.asUint8List(), w, h, quality: 80);
  }

  void _drawWorkoutActive(ui.Canvas canvas, double w, double h) {
    final isPaused = workout.isPaused;
    final accentColor = isPaused ? const Color(0xFFFFAA00) : const Color(0xFF0066FF);

    // Pause indicator
    if (isPaused) {
      _drawCenteredText(canvas, '⏸ PAUSED', y: 4, size: 18, color: const Color(0xFFFFAA00));
    }

    // Top: elapsed time
    _drawCenteredText(canvas, workout.elapsedLabel,
        y: isPaused ? 26 : 8, size: 38, color: const Color(0xFFFFFFFF));

    // Accent line
    canvas.drawRect(Rect.fromLTWH(0, isPaused ? 72 : 54, w, 2),
        ui.Paint()..color = accentColor);

    // Big center: distance
    _drawCenteredText(canvas,
        workout.distanceKm >= 1
            ? workout.distanceKm.toStringAsFixed(2)
            : '${workout.distanceM.toStringAsFixed(0)} m',
        y: isPaused ? 80 : 62, size: 76, color: const Color(0xFF00CCFF));
    _drawCenteredText(canvas,
        workout.distanceKm >= 1 ? 'km' : 'm',
        y: isPaused ? 162 : 144, size: 22, color: const Color(0xFF4477AA));

    // Accent line
    canvas.drawRect(Rect.fromLTWH(0, isPaused ? 192 : 174, w, 2),
        ui.Paint()..color = accentColor);

    // Bottom row: current live pace | HR
    final paceY = isPaused ? 198 : 180;
    _drawText(canvas, workout.currentLivePaceLabel,
        x: w / 4, y: paceY.toDouble(), size: 22, color: const Color(0xFFFFFFFF));
    _drawText(canvas, hr.heartRate != null ? '♥ ${hr.heartRate}' : '♥ --',
        x: w * 3 / 4, y: paceY.toDouble(), size: 22,
        color: hr.heartRate != null && hr.heartRate! > 160
            ? const Color(0xFFFF3333)
            : const Color(0xFF00FF88));

    // Labels
    _drawText(canvas, 'PACE', x: w / 4, y: (paceY + 26).toDouble(),
        size: 12, color: const Color(0xFF4477AA));
    _drawText(canvas, 'HR', x: w * 3 / 4, y: (paceY + 26).toDouble(),
        size: 12, color: const Color(0xFF4477AA));
  }

  void _drawCenteredText(ui.Canvas canvas, String text,
      {required double y, required double size, required Color color}) {
    _drawText(canvas, text, x: SolosProtocol.displayWidth / 2, y: y,
        size: size, color: color, center: true);
  }

  void _drawText(ui.Canvas canvas, String text,
      {required double x, required double y, required double size,
      required Color color, bool center = false}) {
    final pb = ui.ParagraphBuilder(ui.ParagraphStyle(
      textAlign: center ? ui.TextAlign.center : ui.TextAlign.center,
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
  Widget buildPhoneWidget(BuildContext context) => WorkoutPhoneWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    return Text(
      workout.isRunning
          ? '${workout.elapsedLabel}  ${workout.distanceKm.toStringAsFixed(2)} km'
          : workout.type.label,
      style: Theme.of(context)
          .textTheme
          .bodySmall
          ?.copyWith(fontFamily: 'monospace', fontWeight: FontWeight.bold),
    );
  }
}
