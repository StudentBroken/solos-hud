import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/sensors/speed_fusion_service.dart';
import 'pacer_app.dart';

class PacerPhoneWidget extends StatefulWidget {
  final PacerApp app;
  const PacerPhoneWidget({super.key, required this.app});

  @override
  State<PacerPhoneWidget> createState() => _PacerPhoneWidgetState();
}

class _PacerPhoneWidgetState extends State<PacerPhoneWidget> {
  String _formatPace(double secPerKm) {
    if (secPerKm <= 0) return '--:--';
    final m = (secPerKm ~/ 60).toString().padLeft(2, '0');
    final s = (secPerKm % 60).round().toString().padLeft(2, '0');
    return '$m:$s';
  }

  Color _trackerColor(double currentPaceSec, double goalPaceSec) {
    if (goalPaceSec <= 0 || currentPaceSec <= 0) return Colors.grey;
    final dev = currentPaceSec - goalPaceSec;
    if (dev.abs() <= 4) return Colors.greenAccent;
    return dev > 0 ? Colors.redAccent : Colors.blueAccent;
  }

  @override
  Widget build(BuildContext context) {
    final fusion = context.watch<SpeedFusionService>();
    final hr = context.watch<HeartRateService>();

    final spd = fusion.speedMs;
    final currentPaceSec = spd > 0.2 ? 1000.0 / spd : 0.0;
    final goalPaceSec = widget.app.goalPaceSecPerKm;

    final deviation = (goalPaceSec > 0 && currentPaceSec > 0)
        ? currentPaceSec - goalPaceSec
        : 0.0;
    final fraction = (deviation / 60.0).clamp(-1.0, 1.0);

    final trackerColor = _trackerColor(currentPaceSec, goalPaceSec);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // ── Live pace + HR row ─────────────────────────────────────
        Row(
          children: [
            Expanded(
              child: _BigStat(
                label: 'CURRENT PACE',
                value: currentPaceSec > 0 ? _formatPace(currentPaceSec) : '--:--',
                unit: '/km',
                color: trackerColor,
              ),
            ),
            Expanded(
              child: _BigStat(
                label: 'HEART RATE',
                value: hr.heartRate?.toString() ?? '--',
                unit: 'bpm',
                color: hr.heartRate != null && hr.heartRate! > 160
                    ? Colors.redAccent
                    : Colors.greenAccent,
              ),
            ),
          ],
        ),
        const SizedBox(height: 12),

        // ── Visual tracker ────────────────────────────────────────
        _PacerVisual(
          fraction: fraction,
          trackerColor: trackerColor,
          goalPaceStr: goalPaceSec > 0 ? _formatPace(goalPaceSec) : null,
          deviationSec: goalPaceSec > 0 ? deviation : null,
        ),
        const SizedBox(height: 12),

        // ── Goal pace setter ──────────────────────────────────────
        _GoalPaceSetter(
          goalPaceSec: goalPaceSec,
          onChanged: (v) => setState(() => widget.app.goalPaceSecPerKm = v),
          formatPace: _formatPace,
        ),
        const SizedBox(height: 8),

        // ── HR device status ──────────────────────────────────────
        ListTile(
          dense: true,
          leading: Icon(
            Icons.favorite,
            color: hr.isConnected ? Colors.redAccent : Colors.grey,
            size: 18,
          ),
          title: Text(
            hr.isConnected ? (hr.deviceName ?? 'HR Monitor') : 'No HR monitor',
            style: const TextStyle(fontSize: 12),
          ),
          subtitle: hr.isConnected ? Text('${hr.heartRate ?? "--"} bpm') : null,
        ),
      ],
    );
  }
}

// ── Big stat tile ─────────────────────────────────────────────────────────────

class _BigStat extends StatelessWidget {
  final String label;
  final String value;
  final String unit;
  final Color color;
  const _BigStat({required this.label, required this.value, required this.unit, required this.color});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.all(4),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(label, style: TextStyle(fontSize: 10, color: Colors.grey.shade400)),
            const SizedBox(height: 4),
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  value,
                  style: TextStyle(
                    fontSize: 42,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'monospace',
                    color: color,
                  ),
                ),
                const SizedBox(width: 4),
                Padding(
                  padding: const EdgeInsets.only(bottom: 4),
                  child: Text(unit, style: const TextStyle(fontSize: 11, color: Colors.grey)),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

// ── Visual pacer widget (vertical line + tracker) ─────────────────────────────

class _PacerVisual extends StatelessWidget {
  final double fraction;       // -1 (fastest) … 0 (goal) … +1 (slowest)
  final Color trackerColor;
  final String? goalPaceStr;
  final double? deviationSec;

  const _PacerVisual({
    required this.fraction,
    required this.trackerColor,
    this.goalPaceStr,
    this.deviationSec,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 220,
      child: Row(
        children: [
          // Left 1/3: vertical track
          Expanded(
            flex: 1,
            child: CustomPaint(
              painter: _TrackerPainter(fraction: fraction, trackerColor: trackerColor),
            ),
          ),
          // Right 2/3: labels
          Expanded(
            flex: 2,
            child: Padding(
              padding: const EdgeInsets.only(left: 16),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (goalPaceStr != null) ...[
                    const Text('GOAL', style: TextStyle(fontSize: 11, color: Colors.amber)),
                    Text(
                      '$goalPaceStr /km',
                      style: const TextStyle(
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'monospace',
                        color: Colors.amber,
                      ),
                    ),
                    const SizedBox(height: 12),
                  ],
                  if (deviationSec != null) ...[
                    const Text('DIFF', style: TextStyle(fontSize: 11, color: Colors.grey)),
                    Text(
                      _devLabel(deviationSec!),
                      style: TextStyle(
                        fontSize: 26,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'monospace',
                        color: trackerColor,
                      ),
                    ),
                  ],
                  const SizedBox(height: 8),
                  Row(children: [
                    Icon(Icons.arrow_upward, size: 12, color: Colors.blue.shade300),
                    Text(' faster', style: TextStyle(fontSize: 11, color: Colors.blue.shade300)),
                  ]),
                  Row(children: [
                    Icon(Icons.circle, size: 10, color: Colors.green.shade400),
                    Text(' on pace', style: TextStyle(fontSize: 11, color: Colors.green.shade400)),
                  ]),
                  Row(children: [
                    Icon(Icons.arrow_downward, size: 12, color: Colors.red.shade300),
                    Text(' slower', style: TextStyle(fontSize: 11, color: Colors.red.shade300)),
                  ]),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  String _devLabel(double sec) {
    final sign = sec >= 0 ? '+' : '-';
    final abs = sec.abs();
    final m = (abs ~/ 60);
    final s = (abs % 60).round();
    if (m > 0) return '$sign${m}m${s.toString().padLeft(2, '0')}s /km';
    return '$sign${s}s /km';
  }
}

class _TrackerPainter extends CustomPainter {
  final double fraction; // -1…+1
  final Color trackerColor;
  const _TrackerPainter({required this.fraction, required this.trackerColor});

  @override
  void paint(Canvas canvas, Size size) {
    final cx = size.width / 2;
    const topPad = 10.0;
    const botPad = 10.0;
    final lineTop = topPad;
    final lineBot = size.height - botPad;
    final centerY = (lineTop + lineBot) / 2;
    final halfRange = (lineBot - lineTop) / 2;

    // Vertical guide line
    canvas.drawLine(
      Offset(cx, lineTop),
      Offset(cx, lineBot),
      Paint()
        ..color = Colors.blueGrey.shade800
        ..strokeWidth = 2,
    );

    // Tick marks at ±¼, ±½, ±1 min/km
    for (final tick in [-1.0, -0.5, -0.25, 0.25, 0.5, 1.0]) {
      final ty = centerY + tick * halfRange;
      canvas.drawLine(
        Offset(cx - 8, ty),
        Offset(cx + 8, ty),
        Paint()
          ..color = Colors.blueGrey.shade600
          ..strokeWidth = 1,
      );
    }

    // Green center circle (goal)
    canvas.drawCircle(
      Offset(cx, centerY),
      12,
      Paint()..color = Colors.green.shade600,
    );
    canvas.drawCircle(
      Offset(cx, centerY),
      12,
      Paint()
        ..color = Colors.green.shade900
        ..style = PaintingStyle.stroke
        ..strokeWidth = 2,
    );

    // Tracker rectangle
    final trackerY = centerY + fraction * halfRange;
    final rrect = RRect.fromRectAndRadius(
      Rect.fromCenter(center: Offset(cx, trackerY), width: size.width * 0.75, height: 12),
      const Radius.circular(5),
    );
    canvas.drawRRect(rrect, Paint()..color = trackerColor);
    canvas.drawRRect(
      rrect,
      Paint()
        ..color = trackerColor.withValues(alpha: 0.5)
        ..style = PaintingStyle.stroke
        ..strokeWidth = 2,
    );
  }

  @override
  bool shouldRepaint(_TrackerPainter old) =>
      old.fraction != fraction || old.trackerColor != trackerColor;
}

// ── Goal pace setter ──────────────────────────────────────────────────────────

class _GoalPaceSetter extends StatelessWidget {
  final double goalPaceSec;
  final ValueChanged<double> onChanged;
  final String Function(double) formatPace;

  const _GoalPaceSetter({
    required this.goalPaceSec,
    required this.onChanged,
    required this.formatPace,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 4),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(12, 8, 12, 4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.flag, size: 14, color: Colors.amber),
                const SizedBox(width: 6),
                Text(
                  goalPaceSec > 0
                      ? 'Goal: ${formatPace(goalPaceSec)} /km'
                      : 'Goal Pace: Off',
                  style: const TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.bold,
                    color: Colors.amber,
                  ),
                ),
                const Spacer(),
                if (goalPaceSec > 0)
                  TextButton(
                    onPressed: () => onChanged(0),
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
              divisions: 120,
              value: goalPaceSec.clamp(0, 600),
              label: goalPaceSec < 30 ? 'Off' : '${formatPace(goalPaceSec)} /km',
              activeColor: Colors.amber,
              onChanged: (v) => onChanged(v < 30 ? 0 : v),
            ),
            Padding(
              padding: const EdgeInsets.only(bottom: 4),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: const [
                  Text('3:00', style: TextStyle(fontSize: 10, color: Colors.grey)),
                  Text('6:00', style: TextStyle(fontSize: 10, color: Colors.grey)),
                  Text('10:00', style: TextStyle(fontSize: 10, color: Colors.grey)),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
