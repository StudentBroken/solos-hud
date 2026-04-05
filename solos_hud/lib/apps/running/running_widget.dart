import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/workout/workout_service.dart';
import 'running_app.dart';

// ── Constants ─────────────────────────────────────────────────────────────────
const _kCyan   = Color(0xFF00E5FF);
const _kGreen  = Color(0xFF00E676);
const _kAmber  = Color(0xFFFFB300);
const _kRed    = Color(0xFFFF1744);
const _kBlue   = Color(0xFF40C4FF);
const _kDimmed = Color(0xFF445566);

class RunningWidget extends StatelessWidget {
  final RunningApp app;
  const RunningWidget({super.key, required this.app});

  Color _bgAccent(WorkoutService w) {
    if (w.isPaused) return _kAmber;
    if (!w.isActive) return Colors.transparent;
    if (w.goalPaceSecPerKm <= 0) return Colors.transparent;
    final live = w.currentLivePaceSecPerKm;
    if (live <= 0) return Colors.transparent;
    final dev = live - w.goalPaceSecPerKm;
    if (dev.abs() <= 2) return _kGreen;
    return dev > 0 ? _kRed : _kBlue;
  }

  @override
  Widget build(BuildContext context) {
    final workout = context.watch<WorkoutService>();
    final hr      = context.watch<HeartRateService>();
    final accent  = _bgAccent(workout);
    final hasAccent = accent != Colors.transparent;

    return AnimatedContainer(
      duration: const Duration(milliseconds: 700),
      curve: Curves.easeInOut,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(18),
        color: hasAccent ? accent.withValues(alpha: 0.055) : Colors.transparent,
        border: hasAccent
            ? Border.all(color: accent.withValues(alpha: 0.18), width: 1.5)
            : null,
      ),
      padding: hasAccent ? const EdgeInsets.all(10) : EdgeInsets.zero,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // ── Hero: elapsed time or idle state ─────────────────────────
          _HeroSection(workout: workout, accentColor: accent),
          const SizedBox(height: 12),

          // ── Metric tiles (2×2 grid) ───────────────────────────────────
          _MetricGrid(workout: workout, hr: hr),
          const SizedBox(height: 12),

          // ── Pace deviation bar (when goal is set + running) ───────────
          if (workout.goalPaceSecPerKm > 0 && workout.isRunning) ...[
            _PaceDeviationBar(workout: workout),
            const SizedBox(height: 12),
          ],

          // ── HR zone strip ─────────────────────────────────────────────
          if (hr.heartRate != null) ...[
            _HrZoneStrip(zone: workout.hrZone, hr: hr.heartRate!),
            const SizedBox(height: 12),
          ],

          // ── Controls ──────────────────────────────────────────────────
          _Controls(workout: workout),
          const SizedBox(height: 16),

          // ── Goal pace (always visible — pacer) ────────────────────────
          _GoalPacePanel(workout: workout),
          const SizedBox(height: 10),

          // ── Activity type (only when not mid-workout) ─────────────────
          if (!workout.isRunning && workout.state != WorkoutState.finished) ...[
            _TypeSelector(workout: workout),
            const SizedBox(height: 10),
          ],

          // ── HR monitor status ─────────────────────────────────────────
          _HrStatus(hr: hr),
          const SizedBox(height: 4),
        ],
      ),
    );
  }
}

// ── Hero: big elapsed time display ───────────────────────────────────────────

class _HeroSection extends StatelessWidget {
  final WorkoutService workout;
  final Color accentColor;
  const _HeroSection({required this.workout, required this.accentColor});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    // Finished state
    if (workout.state == WorkoutState.finished) {
      return _FinishedBanner(workout: workout);
    }

    // Running/paused — show big clock with pace state background
    if (workout.isRunning) {
      final hasGoal = workout.goalPaceSecPerKm > 0;
      final borderColor = accentColor == Colors.transparent
          ? (workout.isPaused ? _kAmber.withValues(alpha: 0.3) : _kCyan.withValues(alpha: 0.2))
          : accentColor.withValues(alpha: 0.5);

      return AnimatedContainer(
        duration: const Duration(milliseconds: 700),
        curve: Curves.easeInOut,
        padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 12),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: accentColor == Colors.transparent
                ? [
                    cs.surfaceContainerHighest.withValues(alpha: 0.8),
                    cs.surfaceContainerLow.withValues(alpha: 0.9),
                  ]
                : [
                    accentColor.withValues(alpha: 0.18),
                    accentColor.withValues(alpha: 0.06),
                  ],
          ),
          border: Border.all(color: borderColor, width: 1.5),
        ),
        child: Column(
          children: [
            // State label (PAUSED or pace status)
            if (workout.isPaused)
              const Padding(
                padding: EdgeInsets.only(bottom: 4),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.pause_circle, size: 14, color: _kAmber),
                    SizedBox(width: 5),
                    Text('PAUSED',
                        style: TextStyle(
                            fontSize: 11,
                            fontWeight: FontWeight.w700,
                            color: _kAmber,
                            letterSpacing: 1.5)),
                  ],
                ),
              )
            else if (hasGoal) ...[
              _PaceStatusBadge(workout: workout, accentColor: accentColor),
              const SizedBox(height: 6),
            ],
            // Big elapsed time
            Text(
              workout.elapsedLabel,
              style: TextStyle(
                fontSize: 52,
                fontWeight: FontWeight.w900,
                fontFamily: 'monospace',
                color: workout.isPaused ? _kAmber : Colors.white,
                letterSpacing: 2,
              ),
            ),
            const SizedBox(height: 2),
            Text(
              'ELAPSED',
              style: TextStyle(
                fontSize: 10,
                fontWeight: FontWeight.w700,
                letterSpacing: 2,
                color: cs.onSurfaceVariant.withValues(alpha: 0.5),
              ),
            ),
          ],
        ),
      );
    }

    // Idle — show activity header
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 14, horizontal: 16),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: cs.outlineVariant.withValues(alpha: 0.3)),
        color: cs.surfaceContainerLow,
      ),
      child: Row(
        children: [
          Icon(workout.type == WorkoutType.cycling
                  ? Icons.directions_bike_rounded
                  : workout.type == WorkoutType.walking
                      ? Icons.directions_walk_rounded
                      : Icons.directions_run_rounded,
              size: 22, color: _kCyan),
          const SizedBox(width: 10),
          Text(
            workout.type.label.toUpperCase(),
            style: const TextStyle(
              fontSize: 15,
              fontWeight: FontWeight.w800,
              color: _kCyan,
              letterSpacing: 1.2,
            ),
          ),
          const Spacer(),
          Text('READY TO START',
              style: TextStyle(
                  fontSize: 11,
                  color: cs.onSurfaceVariant.withValues(alpha: 0.5),
                  letterSpacing: 1)),
        ],
      ),
    );
  }
}

// ── Pace status badge (shown in hero when goal is set) ────────────────────────

class _PaceStatusBadge extends StatelessWidget {
  final WorkoutService workout;
  final Color accentColor;
  const _PaceStatusBadge({required this.workout, required this.accentColor});

  @override
  Widget build(BuildContext context) {
    final live = workout.currentLivePaceSecPerKm;
    final goal = workout.goalPaceSecPerKm;
    final hasData = live > 0 && goal > 0;
    final dev = hasData ? live - goal : 0.0;
    final onPace = dev.abs() <= 2;

    String label;
    IconData icon;
    if (!hasData) {
      label = 'WAITING FOR PACE';
      icon = Icons.gps_not_fixed_rounded;
    } else if (onPace) {
      label = 'ON PACE';
      icon = Icons.check_circle_rounded;
    } else {
      final sign = dev > 0 ? '+' : '';
      final abs = dev.abs();
      final m = (abs ~/ 60).round();
      final s = (abs % 60).round();
      final devStr = m > 0 ? '${m}m${s}s' : '${s}s';
      label = dev > 0 ? '$sign$devStr BEHIND' : '${devStr.replaceAll('-', '')} AHEAD';
      icon = dev > 0 ? Icons.arrow_downward_rounded : Icons.arrow_upward_rounded;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 5),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        color: accentColor.withValues(alpha: 0.15),
        border: Border.all(color: accentColor.withValues(alpha: 0.4)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 13, color: accentColor),
          const SizedBox(width: 5),
          Text(
            label,
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.w800,
              letterSpacing: 1,
              color: accentColor,
            ),
          ),
        ],
      ),
    );
  }
}

// ── Finished summary banner ───────────────────────────────────────────────────

class _FinishedBanner extends StatelessWidget {
  final WorkoutService workout;
  const _FinishedBanner({required this.workout});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        gradient: LinearGradient(
          colors: [
            _kGreen.withValues(alpha: 0.12),
            _kCyan.withValues(alpha: 0.08),
          ],
        ),
        border: Border.all(color: _kGreen.withValues(alpha: 0.3)),
      ),
      child: Column(
        children: [
          const Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.check_circle_rounded, color: _kGreen, size: 18),
              SizedBox(width: 6),
              Text('WORKOUT COMPLETE',
                  style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w800,
                      color: _kGreen,
                      letterSpacing: 1.5)),
            ],
          ),
          const SizedBox(height: 10),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              _SummaryItem(
                label: 'DISTANCE',
                value: workout.distanceKm >= 1
                    ? workout.distanceKm.toStringAsFixed(2)
                    : workout.distanceM.toStringAsFixed(0),
                unit: workout.distanceKm >= 1 ? 'km' : 'm',
              ),
              _SummaryItem(
                label: 'TIME',
                value: workout.elapsedLabel,
                unit: '',
              ),
              _SummaryItem(
                label: 'AVG PACE',
                value: workout.paceLabel,
                unit: '',
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _SummaryItem extends StatelessWidget {
  final String label, value, unit;
  const _SummaryItem({required this.label, required this.value, required this.unit});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(label,
            style: const TextStyle(
                fontSize: 9, letterSpacing: 1.2, color: _kDimmed, fontWeight: FontWeight.w700)),
        const SizedBox(height: 3),
        Text(value,
            style: const TextStyle(
                fontSize: 22, fontWeight: FontWeight.w900, fontFamily: 'monospace',
                color: _kGreen)),
        if (unit.isNotEmpty)
          Text(unit,
              style: const TextStyle(fontSize: 10, color: _kDimmed)),
      ],
    );
  }
}

// ── 2×2 metric tile grid ──────────────────────────────────────────────────────

class _MetricGrid extends StatelessWidget {
  final WorkoutService workout;
  final HeartRateService hr;
  const _MetricGrid({required this.workout, required this.hr});

  Color _hrColor() {
    final bpm = hr.heartRate;
    if (bpm == null) return _kDimmed;
    if (bpm > 170) return _kRed;
    if (bpm > 150) return _kAmber;
    return _kGreen;
  }

  Color _paceColor() {
    if (!workout.isRunning) return _kDimmed;
    if (workout.goalPaceSecPerKm <= 0) return _kCyan;
    final live = workout.currentLivePaceSecPerKm;
    if (live <= 0) return _kDimmed;
    final dev = live - workout.goalPaceSecPerKm;
    if (dev.abs() <= 2) return _kGreen;
    return dev > 0 ? _kRed : _kBlue;
  }

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    final distStr = workout.distanceKm >= 1
        ? workout.distanceKm.toStringAsFixed(2)
        : workout.distanceM.toStringAsFixed(0);
    final distUnit = workout.distanceKm >= 1 ? 'km' : 'm';

    return Row(
      children: [
        Expanded(
          child: Column(
            children: [
              _MetricTile(
                label: 'CUR PACE',
                value: workout.currentLivePaceLabel,
                unit: workout.type == WorkoutType.cycling ? '' : 'min/km',
                color: _paceColor(),
                isPrimary: true,
                cs: cs,
              ),
              const SizedBox(height: 6),
              _MetricTile(
                label: 'HEART RATE',
                value: hr.heartRate != null ? '${hr.heartRate}' : '--',
                unit: hr.heartRate != null ? 'bpm  Z${workout.hrZone}' : '',
                color: _hrColor(),
                cs: cs,
              ),
            ],
          ),
        ),
        const SizedBox(width: 6),
        Expanded(
          child: Column(
            children: [
              _MetricTile(
                label: 'DISTANCE',
                value: distStr,
                unit: distUnit,
                color: _kCyan,
                isPrimary: true,
                cs: cs,
              ),
              const SizedBox(height: 6),
              _MetricTile(
                label: 'AVG PACE',
                value: workout.paceLabel,
                unit: '',
                color: cs.onSurfaceVariant,
                cs: cs,
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class _MetricTile extends StatelessWidget {
  final String label, value, unit;
  final Color color;
  final bool isPrimary;
  final ColorScheme cs;

  const _MetricTile({
    required this.label,
    required this.value,
    required this.unit,
    required this.color,
    required this.cs,
    this.isPrimary = false,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: cs.surfaceContainerLow,
        border: Border.all(
          color: isPrimary
              ? color.withValues(alpha: 0.3)
              : cs.outlineVariant.withValues(alpha: 0.25),
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            label,
            style: TextStyle(
              fontSize: 9,
              fontWeight: FontWeight.w700,
              letterSpacing: 1.3,
              color: cs.onSurfaceVariant.withValues(alpha: 0.6),
            ),
          ),
          const SizedBox(height: 5),
          Text(
            value,
            style: TextStyle(
              fontSize: isPrimary ? 34 : 28,
              fontWeight: FontWeight.w900,
              fontFamily: 'monospace',
              color: color,
              height: 1.0,
            ),
          ),
          if (unit.isNotEmpty) ...[
            const SizedBox(height: 2),
            Text(
              unit,
              style: TextStyle(
                fontSize: 10,
                color: cs.onSurfaceVariant.withValues(alpha: 0.5),
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ],
      ),
    );
  }
}

// ── Pace deviation bar ────────────────────────────────────────────────────────

class _PaceDeviationBar extends StatelessWidget {
  final WorkoutService workout;
  const _PaceDeviationBar({required this.workout});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    final goal = workout.goalPaceSecPerKm;
    final live = workout.currentLivePaceSecPerKm;

    if (goal <= 0 || live <= 0) return const SizedBox.shrink();

    final dev = live - goal; // + = slower, - = faster
    final fraction = (dev / 60.0).clamp(-1.0, 1.0);
    final barColor = dev.abs() <= 5
        ? _kGreen
        : dev > 0
            ? _kRed
            : _kBlue;

    final goalM = (goal ~/ 60).toString().padLeft(2, '0');
    final goalS = (goal % 60).round().toString().padLeft(2, '0');
    final sign = dev >= 0 ? '+' : '';
    final devAbs = dev.abs();
    final devM = (devAbs ~/ 60).round();
    final devS = (devAbs % 60).round();
    final devStr = devM > 0 ? '$sign${devM}m${devS}s' : '$sign${devS}s';

    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 12),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: cs.surfaceContainerLow,
        border: Border.all(color: barColor.withValues(alpha: 0.25)),
      ),
      child: Column(
        children: [
          Row(
            children: [
              Text('TARGET  $goalM:$goalS /km',
                  style: const TextStyle(
                      fontSize: 11, fontWeight: FontWeight.w700,
                      color: _kAmber, letterSpacing: 0.5)),
              const Spacer(),
              Text(devStr,
                  style: TextStyle(
                      fontSize: 14, fontWeight: FontWeight.w900,
                      fontFamily: 'monospace', color: barColor)),
            ],
          ),
          const SizedBox(height: 8),
          // Deviation track
          LayoutBuilder(builder: (ctx, box) {
            final trackW = box.maxWidth;
            final center = trackW / 2;
            final barW = (fraction.abs() * center).clamp(2.0, center);
            final barLeft = fraction < 0 ? center - barW : center;

            return Stack(
              clipBehavior: Clip.none,
              children: [
                // Track
                Container(
                  height: 6,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(3),
                    color: cs.surfaceContainerHighest,
                  ),
                ),
                // Filled portion
                Positioned(
                  left: barLeft,
                  child: Container(
                    height: 6,
                    width: barW,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(3),
                      color: barColor,
                    ),
                  ),
                ),
                // Center marker
                Positioned(
                  left: center - 1,
                  child: Container(
                      width: 2, height: 6,
                      color: cs.onSurfaceVariant.withValues(alpha: 0.5)),
                ),
              ],
            );
          }),
          const SizedBox(height: 4),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('FASTER', style: TextStyle(fontSize: 9, color: _kBlue.withValues(alpha: 0.7), letterSpacing: 0.5)),
              Text('ON PACE', style: TextStyle(fontSize: 9, color: _kGreen.withValues(alpha: 0.7), letterSpacing: 0.5)),
              Text('SLOWER', style: TextStyle(fontSize: 9, color: _kRed.withValues(alpha: 0.7), letterSpacing: 0.5)),
            ],
          ),
        ],
      ),
    );
  }
}

// ── HR Zone strip ─────────────────────────────────────────────────────────────

class _HrZoneStrip extends StatelessWidget {
  final int zone;
  final int hr;
  const _HrZoneStrip({required this.zone, required this.hr});

  static const _zoneColors = [
    Colors.grey, Color(0xFF40C4FF), Color(0xFF00E676),
    Color(0xFFFFD740), Color(0xFFFF9100), Color(0xFFFF1744),
  ];
  static const _zoneLabels = [
    '--', 'Z1 Rest', 'Z2 Easy', 'Z3 Aerobic', 'Z4 Threshold', 'Z5 Max',
  ];

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    final color = _zoneColors[zone.clamp(0, 5)];
    final label = _zoneLabels[zone.clamp(0, 5)];

    return Container(
      padding: const EdgeInsets.fromLTRB(12, 8, 12, 10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: cs.surfaceContainerLow,
        border: Border.all(color: color.withValues(alpha: 0.25)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(Icons.favorite_rounded, color: color, size: 14),
              const SizedBox(width: 5),
              Text('$hr BPM',
                  style: TextStyle(
                      fontSize: 13, fontWeight: FontWeight.w800,
                      fontFamily: 'monospace', color: color)),
              const SizedBox(width: 8),
              Text('· $label',
                  style: TextStyle(
                      fontSize: 11, color: cs.onSurfaceVariant, fontWeight: FontWeight.w500)),
            ],
          ),
          const SizedBox(height: 7),
          Row(
            children: List.generate(5, (i) {
              final filled = i < zone;
              return Expanded(
                child: Container(
                  height: 5,
                  margin: const EdgeInsets.symmetric(horizontal: 1.5),
                  decoration: BoxDecoration(
                    color: filled ? _zoneColors[i + 1] : cs.surfaceContainerHighest,
                    borderRadius: BorderRadius.circular(3),
                  ),
                ),
              );
            }),
          ),
        ],
      ),
    );
  }
}

// ── Workout controls ──────────────────────────────────────────────────────────

class _Controls extends StatelessWidget {
  final WorkoutService workout;
  const _Controls({required this.workout});

  @override
  Widget build(BuildContext context) {
    return switch (workout.state) {
      WorkoutState.idle => SizedBox(
        height: 52,
        child: FilledButton.icon(
          onPressed: workout.start,
          icon: const Icon(Icons.play_arrow_rounded, size: 22),
          label: const Text('START', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w800, letterSpacing: 1)),
          style: FilledButton.styleFrom(
            backgroundColor: _kGreen,
            foregroundColor: Colors.black,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
          ),
        ),
      ),
      WorkoutState.active => Row(
        children: [
          Expanded(
            child: SizedBox(
              height: 52,
              child: OutlinedButton.icon(
                onPressed: workout.pause,
                icon: const Icon(Icons.pause_rounded, size: 20),
                label: const Text('PAUSE', style: TextStyle(fontWeight: FontWeight.w700, letterSpacing: 1)),
                style: OutlinedButton.styleFrom(
                  foregroundColor: _kAmber,
                  side: const BorderSide(color: _kAmber),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                ),
              ),
            ),
          ),
          const SizedBox(width: 8),
          SizedBox(
            height: 52,
            width: 52,
            child: OutlinedButton(
              onPressed: () => _confirmStop(context),
              style: OutlinedButton.styleFrom(
                foregroundColor: _kRed,
                side: const BorderSide(color: _kRed),
                padding: EdgeInsets.zero,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              ),
              child: const Icon(Icons.stop_rounded, size: 22),
            ),
          ),
        ],
      ),
      WorkoutState.paused => Row(
        children: [
          Expanded(
            child: SizedBox(
              height: 52,
              child: FilledButton.icon(
                onPressed: workout.resume,
                icon: const Icon(Icons.play_arrow_rounded, size: 22),
                label: const Text('RESUME', style: TextStyle(fontWeight: FontWeight.w700, letterSpacing: 1)),
                style: FilledButton.styleFrom(
                  backgroundColor: _kGreen,
                  foregroundColor: Colors.black,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                ),
              ),
            ),
          ),
          const SizedBox(width: 8),
          SizedBox(
            height: 52,
            width: 52,
            child: FilledButton(
              onPressed: () => _confirmStop(context),
              style: FilledButton.styleFrom(
                backgroundColor: _kRed.withValues(alpha: 0.15),
                foregroundColor: _kRed,
                padding: EdgeInsets.zero,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              ),
              child: const Icon(Icons.stop_rounded, size: 22),
            ),
          ),
        ],
      ),
      WorkoutState.finished => SizedBox(
        height: 52,
        child: OutlinedButton.icon(
          onPressed: workout.reset,
          icon: const Icon(Icons.add_rounded, size: 20),
          label: const Text('NEW ACTIVITY',
              style: TextStyle(fontWeight: FontWeight.w700, letterSpacing: 1)),
          style: OutlinedButton.styleFrom(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
          ),
        ),
      ),
    };
  }

  void _confirmStop(BuildContext ctx) {
    final wo = ctx.read<WorkoutService>();
    showDialog(
      context: ctx,
      builder: (dCtx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        title: const Text('Stop Activity?'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _SummaryRow('Time',     wo.elapsedLabel),
            _SummaryRow('Distance', '${wo.distanceKm.toStringAsFixed(2)} km'),
            _SummaryRow('Avg Pace', wo.paceLabel),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(dCtx), child: const Text('Cancel')),
          FilledButton(
            onPressed: () { Navigator.pop(dCtx); wo.stop(); },
            style: FilledButton.styleFrom(backgroundColor: _kRed),
            child: const Text('Stop'),
          ),
        ],
      ),
    );
  }
}

class _SummaryRow extends StatelessWidget {
  final String label, value;
  const _SummaryRow(this.label, this.value);
  @override
  Widget build(BuildContext context) => Padding(
    padding: const EdgeInsets.symmetric(vertical: 3),
    child: Row(
      children: [
        Text('$label: ', style: const TextStyle(color: _kDimmed, fontSize: 13)),
        Text(value, style: const TextStyle(fontWeight: FontWeight.w700, fontSize: 13)),
      ],
    ),
  );
}

// ── Goal pace panel ───────────────────────────────────────────────────────────

class _GoalPacePanel extends StatelessWidget {
  final WorkoutService workout;
  const _GoalPacePanel({required this.workout});

  String _fmt(double s) {
    if (s <= 0) return 'OFF';
    return '${(s ~/ 60).toString().padLeft(2, '0')}:${(s % 60).round().toString().padLeft(2, '0')} /km';
  }

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    final goal = workout.goalPaceSecPerKm;
    final set = goal > 0;

    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 6),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: cs.surfaceContainerLow,
        border: Border.all(
          color: set
              ? _kAmber.withValues(alpha: 0.3)
              : cs.outlineVariant.withValues(alpha: 0.25),
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(Icons.flag_rounded, size: 14,
                  color: set ? _kAmber : cs.onSurfaceVariant),
              const SizedBox(width: 6),
              Text('TARGET PACE',
                  style: TextStyle(
                      fontSize: 9,
                      fontWeight: FontWeight.w700,
                      letterSpacing: 1.3,
                      color: cs.onSurfaceVariant.withValues(alpha: 0.6))),
              const Spacer(),
              Text(_fmt(goal),
                  style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w800,
                      fontFamily: 'monospace',
                      color: set ? _kAmber : cs.onSurfaceVariant)),
              if (set) ...[
                const SizedBox(width: 6),
                GestureDetector(
                  onTap: () {
                    workout.setGoalPace(0);
                    context.read<AppSettings>().setAppValue(RunningApp.kKeyGoalPace, 0.0);
                  },
                  child: Icon(Icons.close_rounded, size: 14,
                      color: cs.onSurfaceVariant),
                ),
              ],
            ],
          ),
          Slider(
            min: 0,
            max: 600,
            divisions: 120,
            value: goal.clamp(0, 600),
            activeColor: _kAmber,
            inactiveColor: cs.surfaceContainerHighest,
            label: _fmt(goal),
            onChanged: (v) {
              final goal = v < 30 ? 0.0 : v;
              workout.setGoalPace(goal);
              context.read<AppSettings>().setAppValue(RunningApp.kKeyGoalPace, goal);
            },
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 4),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: const [
                Text('3:00', style: TextStyle(fontSize: 9, color: _kDimmed)),
                Text('5:00', style: TextStyle(fontSize: 9, color: _kDimmed)),
                Text('10:00', style: TextStyle(fontSize: 9, color: _kDimmed)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Activity type selector ────────────────────────────────────────────────────

class _TypeSelector extends StatelessWidget {
  final WorkoutService workout;
  const _TypeSelector({required this.workout});

  @override
  Widget build(BuildContext context) {
    return SegmentedButton<WorkoutType>(
      style: SegmentedButton.styleFrom(
        textStyle: const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
        side: BorderSide(
          color: Theme.of(context).colorScheme.outlineVariant.withValues(alpha: 0.4),
        ),
      ),
      segments: WorkoutType.values.map((t) => ButtonSegment(
        value: t,
        label: Text(t.label),
        icon: Icon(_iconFor(t), size: 14),
      )).toList(),
      selected: {workout.type},
      onSelectionChanged: (s) {
        workout.setType(s.first);
        context.read<AppSettings>().setAppValue(
          RunningApp.kKeyWorkoutType, s.first.index);
      },
    );
  }

  IconData _iconFor(WorkoutType t) => switch (t) {
    WorkoutType.running => Icons.directions_run_rounded,
    WorkoutType.cycling => Icons.directions_bike_rounded,
    WorkoutType.walking => Icons.directions_walk_rounded,
    WorkoutType.other   => Icons.fitness_center_rounded,
  };
}

// ── HR monitor status ─────────────────────────────────────────────────────────

class _HrStatus extends StatelessWidget {
  final HeartRateService hr;
  const _HrStatus({required this.hr});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10),
        color: cs.surfaceContainerLow,
        border: Border.all(
          color: hr.isConnected
              ? _kRed.withValues(alpha: 0.25)
              : cs.outlineVariant.withValues(alpha: 0.2),
        ),
      ),
      child: Row(
        children: [
          Icon(
            hr.isConnected ? Icons.favorite_rounded : Icons.favorite_outline_rounded,
            color: hr.isConnected ? _kRed : cs.onSurfaceVariant.withValues(alpha: 0.4),
            size: 16,
          ),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              hr.isConnected
                  ? (hr.deviceName ?? 'HR Monitor')
                  : 'No HR monitor — scan in Dashboard',
              style: TextStyle(
                fontSize: 12,
                color: hr.isConnected ? cs.onSurface : cs.onSurfaceVariant,
              ),
            ),
          ),
          if (hr.isConnected && hr.heartRate != null)
            Text(
              '${hr.heartRate} bpm',
              style: const TextStyle(
                  fontSize: 12, fontWeight: FontWeight.w700,
                  fontFamily: 'monospace', color: _kRed),
            ),
        ],
      ),
    );
  }
}

