import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/workout/workout_service.dart';
import 'workout_app.dart';

class WorkoutPhoneWidget extends StatelessWidget {
  final WorkoutApp app;
  const WorkoutPhoneWidget({super.key, required this.app});

  Color _bgColor(WorkoutService workout) {
    if (workout.goalPaceSecPerKm <= 0) return Colors.transparent;
    final livePace = workout.currentLivePaceSecPerKm;
    if (livePace <= 0) return Colors.transparent;
    final deviation = livePace - workout.goalPaceSecPerKm;
    final t = (deviation.abs() / 60.0).clamp(0.0, 1.0);
    if (deviation > 0) return Colors.red.withValues(alpha: 0.25 * t);
    if (deviation < 0) return Colors.blue.withValues(alpha: 0.25 * t);
    return Colors.green.withValues(alpha: 0.25);
  }

  @override
  Widget build(BuildContext context) {
    final workout = context.watch<WorkoutService>();
    final hr = context.watch<HeartRateService>();

    return AnimatedContainer(
      duration: const Duration(milliseconds: 300),
      decoration: BoxDecoration(
        color: _bgColor(workout),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // ── Type selector (only when idle) ────────────────────────
          if (!workout.isRunning) ...[
            _TypeSelector(workout: workout),
            const SizedBox(height: 12),
          ],

          // ── Stats ─────────────────────────────────────────────────
          _StatsGrid(workout: workout, hr: hr),
          const SizedBox(height: 8),

          // ── Goal pace setter ──────────────────────────────────────
          _GoalPaceRow(workout: workout),
          const SizedBox(height: 8),

          // ── HR zone bar ───────────────────────────────────────────
          if (hr.heartRate != null)
            _HrZoneBar(zone: workout.hrZone, hr: hr.heartRate!),
          const SizedBox(height: 16),

          // ── Controls ──────────────────────────────────────────────
          _ControlRow(workout: workout),

          const SizedBox(height: 12),
          // ── HR device inline ──────────────────────────────────────
          _HrStatus(hr: hr),
        ],
      ),
    );
  }
}

class _TypeSelector extends StatelessWidget {
  final WorkoutService workout;
  const _TypeSelector({required this.workout});

  @override
  Widget build(BuildContext context) {
    return SegmentedButton<WorkoutType>(
      segments: WorkoutType.values
          .map(
            (t) => ButtonSegment(
              value: t,
              label: Text(t.label),
              icon: Icon(_iconFor(t), size: 14),
            ),
          )
          .toList(),
      selected: {workout.type},
      onSelectionChanged: (s) => workout.setType(s.first),
    );
  }

  IconData _iconFor(WorkoutType t) => switch (t) {
    WorkoutType.running => Icons.directions_run,
    WorkoutType.cycling => Icons.directions_bike,
    WorkoutType.walking => Icons.directions_walk,
    WorkoutType.other => Icons.fitness_center,
  };
}

class _StatsGrid extends StatelessWidget {
  final WorkoutService workout;
  final HeartRateService hr;
  const _StatsGrid({required this.workout, required this.hr});

  @override
  Widget build(BuildContext context) {
    return GridView.count(
      crossAxisCount: 3,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      childAspectRatio: 1.4,
      children: [
        _StatTile('Time', workout.elapsedLabel, ''),
        _StatTile(
          'Distance',
          workout.distanceKm >= 1
              ? workout.distanceKm.toStringAsFixed(2)
              : workout.distanceM.toStringAsFixed(0),
          workout.distanceKm >= 1 ? 'km' : 'm',
        ),
        _StatTile('Avg Pace', workout.paceLabel, ''),
        _StatTile(
          'Cur Pace',
          workout.currentLivePaceLabel,
          '',
          color: Theme.of(context).colorScheme.primary,
        ),
        _StatTile(
          'HR',
          hr.heartRate?.toString() ?? '--',
          'bpm',
          color: hr.heartRate != null && hr.heartRate! > 160
              ? Colors.red
              : Colors.greenAccent,
        ),
        _StatTile('Cal', workout.estimatedCalories.toStringAsFixed(0), 'kcal'),
        _StatTile(
          'Avg HR',
          workout.avgHr > 0 ? '${workout.avgHr}' : '--',
          'bpm',
        ),
        _StatTile(
          'Max HR',
          workout.maxHr > 0 ? '${workout.maxHr}' : '--',
          'bpm',
        ),
        _StatTile('Max Spd', workout.maxSpeedKmh.toStringAsFixed(1), 'km/h'),
      ],
    );
  }
}

class _StatTile extends StatelessWidget {
  final String label;
  final String value;
  final String unit;
  final Color? color;
  const _StatTile(this.label, this.value, this.unit, {this.color});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.all(3),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              label,
              style: TextStyle(fontSize: 10, color: Colors.grey.shade400),
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Flexible(
                  child: Text(
                    value,
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'monospace',
                      color: color,
                    ),
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
                if (unit.isNotEmpty) ...[
                  const SizedBox(width: 2),
                  Text(
                    unit,
                    style: const TextStyle(fontSize: 9, color: Colors.grey),
                  ),
                ],
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class _HrZoneBar extends StatelessWidget {
  final int zone;
  final int hr;
  const _HrZoneBar({required this.zone, required this.hr});

  @override
  Widget build(BuildContext context) {
    const zoneColors = [
      Colors.grey,
      Colors.blue,
      Colors.green,
      Colors.yellow,
      Colors.orange,
      Colors.red,
    ];
    const zoneLabels = [
      '--',
      'Z1 Easy',
      'Z2 Fat burn',
      'Z3 Aerobic',
      'Z4 Anaerobic',
      'Z5 Max',
    ];
    final color = zoneColors[zone.clamp(0, 5)];
    final label = zoneLabels[zone.clamp(0, 5)];

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            Icon(Icons.favorite, color: color, size: 16),
            const SizedBox(width: 6),
            Text(
              '$hr bpm — $label',
              style: TextStyle(color: color, fontWeight: FontWeight.bold),
            ),
          ],
        ),
        const SizedBox(height: 6),
        Row(
          children: List.generate(5, (i) {
            final active = i < zone;
            return Expanded(
              child: Container(
                height: 6,
                margin: const EdgeInsets.symmetric(horizontal: 2),
                decoration: BoxDecoration(
                  color: active ? zoneColors[i + 1] : Colors.grey.shade800,
                  borderRadius: BorderRadius.circular(3),
                ),
              ),
            );
          }),
        ),
      ],
    );
  }
}

class _ControlRow extends StatelessWidget {
  final WorkoutService workout;
  const _ControlRow({required this.workout});

  @override
  Widget build(BuildContext context) {
    return switch (workout.state) {
      WorkoutState.idle => FilledButton.icon(
        onPressed: workout.start,
        icon: const Icon(Icons.play_arrow),
        label: const Text('Start Workout'),
        style: FilledButton.styleFrom(backgroundColor: Colors.green),
      ),
      WorkoutState.active => Row(
        children: [
          Expanded(
            child: OutlinedButton.icon(
              onPressed: workout.pause,
              icon: const Icon(Icons.pause),
              label: const Text('Pause'),
            ),
          ),
          const SizedBox(width: 8),
          Expanded(
            child: FilledButton.icon(
              onPressed: () => _confirmStop(context),
              icon: const Icon(Icons.stop),
              label: const Text('Stop'),
              style: FilledButton.styleFrom(backgroundColor: Colors.red),
            ),
          ),
        ],
      ),
      WorkoutState.paused => Row(
        children: [
          Expanded(
            child: FilledButton.icon(
              onPressed: workout.resume,
              icon: const Icon(Icons.play_arrow),
              label: const Text('Resume'),
              style: FilledButton.styleFrom(backgroundColor: Colors.green),
            ),
          ),
          const SizedBox(width: 8),
          Expanded(
            child: OutlinedButton.icon(
              onPressed: () => _confirmStop(context),
              icon: const Icon(Icons.stop),
              label: const Text('Stop'),
            ),
          ),
        ],
      ),
      WorkoutState.finished => Row(
        children: [
          Expanded(
            child: FilledButton.icon(
              onPressed: workout.reset,
              icon: const Icon(Icons.refresh),
              label: const Text('New Workout'),
            ),
          ),
        ],
      ),
    };
  }

  void _confirmStop(BuildContext context) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Stop Workout?'),
        content: Text(
          'Time: ${context.read<WorkoutService>().elapsedLabel}\n'
          'Distance: ${context.read<WorkoutService>().distanceKm.toStringAsFixed(2)} km',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () {
              Navigator.pop(ctx);
              context.read<WorkoutService>().stop();
            },
            style: FilledButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Stop'),
          ),
        ],
      ),
    );
  }
}

class _GoalPaceRow extends StatelessWidget {
  final WorkoutService workout;
  const _GoalPaceRow({required this.workout});

  String _label(double secPerKm) {
    if (secPerKm <= 0) return 'Off';
    final m = (secPerKm ~/ 60).toString().padLeft(2, '0');
    final s = (secPerKm % 60).round().toString().padLeft(2, '0');
    return '$m:$s /km';
  }

  @override
  Widget build(BuildContext context) {
    final goal = workout.goalPaceSecPerKm;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.flag, size: 14, color: Colors.amber),
              const SizedBox(width: 6),
              Text(
                'Goal Pace: ${_label(goal)}',
                style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: Colors.amber),
              ),
              const Spacer(),
              if (goal > 0)
                TextButton(
                  onPressed: () => workout.setGoalPace(0),
                  style: TextButton.styleFrom(
                    padding: EdgeInsets.zero,
                    minimumSize: const Size(40, 24),
                    tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                  ),
                  child: const Text('Clear', style: TextStyle(fontSize: 11)),
                ),
            ],
          ),
          Slider(
            min: 0,
            max: 600,
            divisions: 120, // 5-second steps
            value: goal.clamp(0, 600),
            label: _label(goal),
            activeColor: Colors.amber,
            onChanged: (v) => workout.setGoalPace(v < 30 ? 0 : v),
          ),
        ],
      ),
    );
  }
}

class _HrStatus extends StatelessWidget {
  final HeartRateService hr;
  const _HrStatus({required this.hr});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      dense: true,
      leading: Icon(
        Icons.favorite,
        color: hr.isConnected ? Colors.redAccent : Colors.grey,
        size: 20,
      ),
      title: Text(
        hr.isConnected ? (hr.deviceName ?? 'HR Monitor') : 'No HR monitor',
        style: const TextStyle(fontSize: 13),
      ),
      subtitle: hr.isConnected
          ? Text('${hr.heartRate ?? "--"} bpm')
          : const Text('Connect in Dashboard app'),
    );
  }
}
