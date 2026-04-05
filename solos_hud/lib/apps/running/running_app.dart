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
import 'running_widget.dart';

class RunningApp extends GlassesApp {
  static const kKeyGoalPace    = 'running_goal_pace_sec_per_km';
  static const kKeyWorkoutType = 'running_workout_type';

  final WorkoutService workout;
  final GpsService gps;
  final HeartRateService hr;
  final AppSettings settings;

  RunningApp({
    required this.workout,
    required this.gps,
    required this.hr,
    required this.settings,
  });

  @override String get id          => 'running';
  @override String get name        => 'Running';
  @override IconData get icon      => Icons.directions_run_rounded;
  @override String get description => 'Track pace, distance, HR and effort';
  @override int get preferredCodec => SolosProtocol.codecJPEG;
  @override int get preferredRefreshMs => 500;

  @override
  void onActivate() {
    gps.init();
    final savedPace = (settings.getAppValue(kKeyGoalPace, 0.0) as num).toDouble();
    workout.setGoalPace(savedPace);
    final typeIdx = (settings.getAppValue(kKeyWorkoutType, 0) as num).toInt();
    if (typeIdx >= 0 && typeIdx < WorkoutType.values.length) {
      workout.setType(WorkoutType.values[typeIdx]);
    }
  }

  @override
  void onDeactivate() {
    if (workout.isActive) workout.pause();
  }

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() => _renderFrame();

  // ── Accent color based on pace vs goal ────────────────────────────────────
  Color get _accentColor {
    if (workout.isPaused) return const Color(0xFFFFB300);
    if (!workout.isActive) return const Color(0xFF334455);
    if (workout.goalPaceSecPerKm <= 0) return const Color(0xFF00BCD4);
    final live = workout.currentLivePaceSecPerKm;
    if (live <= 0) return const Color(0xFF00BCD4);
    final dev = live - workout.goalPaceSecPerKm;
    if (dev.abs() <= 2) return const Color(0xFF00E676);  // on pace — green
    if (dev > 0) return const Color(0xFFFF1744);          // too slow — red
    return const Color(0xFF40C4FF);                       // too fast — blue
  }

  // ── Glasses frame (480×240) ───────────────────────────────────────────────
  Future<Uint8List?> _renderFrame() async {
    const w = SolosProtocol.displayWidth;   // 480
    const h = SolosProtocol.displayHeight;  // 240
    final wf = w.toDouble();
    final hf = h.toDouble();

    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(rec, Rect.fromLTWH(0, 0, wf, hf));

    // ── Background ───────────────────────────────────────────────────────────
    canvas.drawRect(Rect.fromLTWH(0, 0, wf, hf),
        ui.Paint()..color = const Color(0xFF060610));

    // ── Pace-tinted background overlay (when active with goal) ──────────────
    final accent = _accentColor;
    if (workout.isActive && !workout.isPaused && workout.goalPaceSecPerKm > 0) {
      canvas.drawRect(
        Rect.fromLTWH(0, 0, wf, hf),
        ui.Paint()..color = accent.withValues(alpha: 0.07),
      );
    }

    // ── Top accent strip ─────────────────────────────────────────────────────
    canvas.drawRect(Rect.fromLTWH(0, 0, wf, 6),
        ui.Paint()..color = accent);

    if (!workout.isRunning && workout.state != WorkoutState.finished) {
      _renderIdleFrame(canvas, wf, hf);
    } else if (workout.state == WorkoutState.finished) {
      _renderFinishedFrame(canvas, wf, hf);
    } else {
      _renderActiveFrame(canvas, wf, hf);
    }

    final picture = rec.endRecording();
    final img = await picture.toImage(w, h);
    final bytes = await img.toByteData(format: ui.ImageByteFormat.rawRgba);
    if (bytes == null) return null;
    return encodeJpegAsync(bytes.buffer.asUint8List(), w, h, quality: 84);
  }

  // ── Idle / ready state ────────────────────────────────────────────────────
  void _renderIdleFrame(ui.Canvas canvas, double w, double h) {
    final typeLabel = workout.type.label.toUpperCase();
    _drawText(canvas, typeLabel,
        x: w / 2, y: 68, size: 20, color: const Color(0xFF00BCD4), center: true);

    final icon = switch (workout.type) {
      WorkoutType.running => '🏃',
      WorkoutType.cycling => '🚴',
      WorkoutType.walking => '🚶',
      WorkoutType.other   => '⚡',
    };
    _drawText(canvas, icon, x: w / 2, y: 90, size: 52, center: true,
        color: const Color(0xFFFFFFFF));

    _drawText(canvas, 'TAP START TO BEGIN',
        x: w / 2, y: 162, size: 14, color: const Color(0xFF445566), center: true);
  }

  // ── Finished summary ──────────────────────────────────────────────────────
  void _renderFinishedFrame(ui.Canvas canvas, double w, double h) {
    _drawText(canvas, 'FINISHED',
        x: w / 2, y: 14, size: 16, color: const Color(0xFF00E676), center: true);

    final dist = workout.distanceKm >= 1
        ? '${workout.distanceKm.toStringAsFixed(2)} km'
        : '${workout.distanceM.toStringAsFixed(0)} m';
    _drawText(canvas, dist,
        x: w / 2, y: 48, size: 72, color: const Color(0xFF00E5FF), center: true);

    canvas.drawRect(Rect.fromLTWH(0, 136, w, 2),
        ui.Paint()..color = const Color(0xFF1A2A3A));

    _drawText(canvas, workout.elapsedLabel,
        x: w / 4, y: 148, size: 28, color: const Color(0xFFFFFFFF));
    _drawText(canvas, workout.paceLabel,
        x: w * 3 / 4, y: 148, size: 22, color: const Color(0xFFAACCFF));

    _drawText(canvas, 'TIME',
        x: w / 4, y: 184, size: 12, color: const Color(0xFF445566));
    _drawText(canvas, 'AVG PACE',
        x: w * 3 / 4, y: 184, size: 12, color: const Color(0xFF445566));

    if (hr.heartRate != null) {
      _drawText(canvas, '♥ ${hr.heartRate} bpm',
          x: w / 2, y: 204, size: 18, color: const Color(0xFFFF7777), center: true);
    }
  }

  // ── Active / paused state ─────────────────────────────────────────────────
  void _renderActiveFrame(ui.Canvas canvas, double w, double h) {
    final isPaused = workout.isPaused;
    final accent = _accentColor; // already computed above, reuse

    // ── Top row: time (left) | state (center) | HR (right) ─────────────────
    _drawText(canvas, workout.elapsedLabel,
        x: w / 4, y: 10, size: 28, color: const Color(0xFFFFFFFF));
    if (isPaused) {
      _drawText(canvas, '⏸ PAUSED',
          x: w / 2, y: 16, size: 16, color: const Color(0xFFFFB300), center: true);
    } else if (workout.goalPaceSecPerKm > 0) {
      final dev = workout.currentLivePaceSecPerKm - workout.goalPaceSecPerKm;
      if (dev.abs() > 2) {
        final sign = dev > 0 ? '+' : '-';
        final abs = dev.abs();
        final m = (abs ~/ 60).round();
        final s = (abs % 60).round();
        final label = m > 0 ? '$sign${m}m${s}s' : '$sign${s}s';
        _drawText(canvas, label,
            x: w / 2, y: 14, size: 16, color: accent, center: true);
      } else {
        _drawText(canvas, '● ON PACE',
            x: w / 2, y: 14, size: 14, color: const Color(0xFF00E676), center: true);
      }
    }

    // HR (right side)
    if (hr.heartRate != null) {
      final hrColor = hr.heartRate! > 160
          ? const Color(0xFFFF4444)
          : const Color(0xFF00FF99);
      _drawText(canvas, '♥ ${hr.heartRate}',
          x: w * 3 / 4, y: 10, size: 24, color: hrColor);
      if (workout.hrZone > 0) {
        _drawText(canvas, 'Z${workout.hrZone}',
            x: w * 3 / 4, y: 38, size: 13, color: hrColor.withValues(alpha: 0.7));
      }
    } else {
      _drawText(canvas, '♥ --',
          x: w * 3 / 4, y: 10, size: 24, color: const Color(0xFF334455));
    }

    // ── Separator ────────────────────────────────────────────────────────────
    canvas.drawRect(Rect.fromLTWH(0, 50, w, 1),
        ui.Paint()..color = const Color(0xFF1A2A3A));

    // ── Center: HUGE live pace ────────────────────────────────────────────────
    final livePace = workout.currentLivePaceLabel;
    final paceColor = isPaused ? const Color(0xFF667788) : accent;
    _drawText(canvas, livePace,
        x: w / 2, y: 56, size: 86, color: paceColor, center: true);
    _drawText(canvas, workout.type == WorkoutType.cycling ? 'km/h' : 'min/km',
        x: w / 2, y: 152, size: 16, color: const Color(0xFF445566), center: true);

    // ── Separator ────────────────────────────────────────────────────────────
    canvas.drawRect(Rect.fromLTWH(0, 178, w, 1),
        ui.Paint()..color = const Color(0xFF1A2A3A));

    // ── Bottom row: distance | avg pace | calories ────────────────────────────
    final dist = workout.distanceKm >= 1
        ? '${workout.distanceKm.toStringAsFixed(2)} km'
        : '${workout.distanceM.toStringAsFixed(0)} m';
    _drawText(canvas, dist,
        x: w / 4, y: 186, size: 24, color: const Color(0xFFCCDDFF));
    _drawText(canvas, workout.paceLabel,
        x: w / 2, y: 188, size: 20, color: const Color(0xFF8899AA), center: true);
    if (workout.estimatedCalories > 0) {
      _drawText(canvas, '${workout.estimatedCalories.toStringAsFixed(0)} kcal',
          x: w * 3 / 4, y: 190, size: 18, color: const Color(0xFF776655));
    }

    // Labels
    _drawText(canvas, 'DIST',
        x: w / 4, y: 216, size: 11, color: const Color(0xFF334455));
    _drawText(canvas, 'AVG',
        x: w / 2, y: 216, size: 11, color: const Color(0xFF334455), center: true);
  }

  // ── Draw helpers ──────────────────────────────────────────────────────────

  void _drawText(
    ui.Canvas canvas,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    bool center = false,
  }) {
    final pb = ui.ParagraphBuilder(
      ui.ParagraphStyle(
        textAlign: center ? ui.TextAlign.center : ui.TextAlign.center,
        fontSize: size,
        fontWeight: ui.FontWeight.w800,
      ),
    )
      ..pushStyle(ui.TextStyle(
        color: color,
        fontSize: size,
        fontWeight: ui.FontWeight.w800,
      ))
      ..addText(text);
    final maxW = SolosProtocol.displayWidth.toDouble();
    final para = pb.build()..layout(ui.ParagraphConstraints(width: maxW));
    canvas.drawParagraph(para, Offset(x - maxW / 2, y));
  }

  // ── Phone UI ──────────────────────────────────────────────────────────────

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      RunningWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (workout.isRunning) {
      return Text(
        '${workout.elapsedLabel}  ${workout.distanceKm.toStringAsFixed(2)} km',
        style: const TextStyle(fontSize: 11, fontFamily: 'monospace'),
      );
    }
    return Text(
      workout.state == WorkoutState.finished
          ? '${workout.distanceKm.toStringAsFixed(2)} km done'
          : workout.type.label,
      style: const TextStyle(fontSize: 11),
    );
  }
}
