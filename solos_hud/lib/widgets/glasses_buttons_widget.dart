import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/rfcomm/glasses_event_service.dart';

/// Live visual display of the three physical glasses buttons.
/// Shows which button is active, what event type fired, and a recent
/// event log — useful for debugging and for learning the gestures.
class GlassesButtonsWidget extends StatefulWidget {
  const GlassesButtonsWidget({super.key});

  @override
  State<GlassesButtonsWidget> createState() => _GlassesButtonsWidgetState();
}

class _GlassesButtonsWidgetState extends State<GlassesButtonsWidget> {
  // Per-button active state + event label
  final Map<GlassesButton, _BtnState> _states = {
    GlassesButton.front: _BtnState(),
    GlassesButton.main:  _BtnState(),
    GlassesButton.back:  _BtnState(),
  };

  StreamSubscription<GlassesEvent>? _sub;
  final List<_LogEntry> _log = [];

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _sub?.cancel();
    _sub = context.read<GlassesEventService>().buttonEvents.listen(_onEvent);
  }

  void _onEvent(GlassesEvent event) {
    if (!mounted) return;
    final btn = event.button;
    if (btn == null || btn == GlassesButton.unknown) return;

    setState(() {
      final s = _states[btn]!;
      s.active = event.action == GlassesActionType.buttonMake;
      s.label  = _actionLabel(event.action);

      _log.insert(0, _LogEntry(btn: btn, event: event));
      if (_log.length > 12) _log.removeLast();
    });

    // Auto-clear the active state after 300 ms so it doesn't stay lit forever
    if (event.action == GlassesActionType.buttonMake) {
      Future.delayed(const Duration(milliseconds: 300), () {
        if (mounted) {
          setState(() => _states[btn]!.active = false);
        }
      });
    }
  }

  static String _actionLabel(GlassesActionType? a) => switch (a) {
        GlassesActionType.buttonMake   => 'PRESS',
        GlassesActionType.buttonBreak  => 'RELEASE',
        GlassesActionType.buttonShort  => 'SHORT',
        GlassesActionType.buttonLong   => 'LONG',
        GlassesActionType.buttonRepeat => 'REPEAT',
        _                              => '',
      };

  @override
  void dispose() {
    _sub?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(14, 12, 14, 14),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header
            Row(
              children: [
                const Icon(Icons.touch_app_outlined,
                    size: 16, color: Colors.cyanAccent),
                const SizedBox(width: 6),
                Text(
                  'GLASSES BUTTONS',
                  style: Theme.of(context).textTheme.labelSmall?.copyWith(
                        color: Colors.cyanAccent,
                        letterSpacing: 1.4,
                        fontWeight: FontWeight.bold,
                      ),
                ),
              ],
            ),
            const SizedBox(height: 14),

            // Three button visuals
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _ButtonVis(
                  label: 'FRONT',
                  sublabel: '↑ scroll up',
                  hint: 'press',
                  state: _states[GlassesButton.front]!,
                  color: Colors.cyanAccent,
                ),
                _ButtonVis(
                  label: 'MAIN',
                  sublabel: 'short = select\nlong = menu',
                  hint: 'hold',
                  state: _states[GlassesButton.main]!,
                  color: Colors.white,
                  large: true,
                ),
                _ButtonVis(
                  label: 'BACK',
                  sublabel: '↓ scroll down',
                  hint: 'press',
                  state: _states[GlassesButton.back]!,
                  color: Colors.cyanAccent,
                ),
              ],
            ),
            const SizedBox(height: 14),

            // Gesture guide
            Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: Colors.grey.shade900,
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _GuideRow(
                      icon: Icons.menu, text: 'Hold MAIN  →  open app launcher'),
                  SizedBox(height: 4),
                  _GuideRow(
                      icon: Icons.check_circle_outline,
                      text: 'Short MAIN  →  launch selected app'),
                  SizedBox(height: 4),
                  _GuideRow(
                      icon: Icons.keyboard_arrow_up,
                      text: 'FRONT  →  scroll up in launcher'),
                  SizedBox(height: 4),
                  _GuideRow(
                      icon: Icons.keyboard_arrow_down,
                      text: 'BACK  →  scroll down in launcher'),
                ],
              ),
            ),

            // Event log
            if (_log.isNotEmpty) ...[
              const SizedBox(height: 12),
              Text('RECENT EVENTS',
                  style: Theme.of(context).textTheme.labelSmall?.copyWith(
                        color: Colors.grey,
                        letterSpacing: 1.2,
                      )),
              const SizedBox(height: 4),
              ...(_log.take(6).map((e) => _LogRow(entry: e))),
            ],
          ],
        ),
      ),
    );
  }
}

// ── Per-button state ──────────────────────────────────────────────────────────

class _BtnState {
  bool   active = false;
  String label  = '';
}

// ── Button visual ─────────────────────────────────────────────────────────────

class _ButtonVis extends StatelessWidget {
  final String   label;
  final String   sublabel;
  final String   hint;
  final _BtnState state;
  final Color    color;
  final bool     large;

  const _ButtonVis({
    required this.label,
    required this.sublabel,
    required this.hint,
    required this.state,
    required this.color,
    this.large = false,
  });

  @override
  Widget build(BuildContext context) {
    final size    = large ? 64.0 : 52.0;
    final isActive = state.active;

    return Column(
      children: [
        AnimatedContainer(
          duration: const Duration(milliseconds: 80),
          width:  size,
          height: size,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: isActive
                ? color.withValues(alpha: 0.25)
                : Colors.grey.shade900,
            border: Border.all(
              color: isActive ? color : Colors.grey.shade700,
              width: isActive ? 2.5 : 1.5,
            ),
            boxShadow: isActive
                ? [BoxShadow(color: color.withValues(alpha: 0.4), blurRadius: 12)]
                : [],
          ),
          child: Center(
            child: AnimatedDefaultTextStyle(
              duration: const Duration(milliseconds: 80),
              style: TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.bold,
                letterSpacing: 0.5,
                color: isActive ? color : Colors.grey.shade600,
              ),
              child: Text(
                state.label.isNotEmpty ? state.label : hint,
                textAlign: TextAlign.center,
              ),
            ),
          ),
        ),
        const SizedBox(height: 6),
        Text(label,
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.bold,
              color: isActive ? color : Colors.grey.shade500,
              letterSpacing: 0.5,
            )),
        const SizedBox(height: 2),
        Text(
          sublabel,
          style: TextStyle(fontSize: 9, color: Colors.grey.shade600),
          textAlign: TextAlign.center,
        ),
      ],
    );
  }
}

// ── Guide row ─────────────────────────────────────────────────────────────────

class _GuideRow extends StatelessWidget {
  final IconData icon;
  final String   text;
  const _GuideRow({required this.icon, required this.text});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Icon(icon, size: 14, color: Colors.grey.shade500),
        const SizedBox(width: 8),
        Text(text, style: TextStyle(fontSize: 11, color: Colors.grey.shade400)),
      ],
    );
  }
}

// ── Log entry ─────────────────────────────────────────────────────────────────

class _LogEntry {
  final GlassesButton btn;
  final GlassesEvent  event;
  final DateTime      time;
  _LogEntry({required this.btn, required this.event}) : time = DateTime.now();
}

class _LogRow extends StatelessWidget {
  final _LogEntry entry;
  const _LogRow({required this.entry});

  @override
  Widget build(BuildContext context) {
    final action = entry.event.action;
    final btnName = switch (entry.btn) {
      GlassesButton.front => 'FRONT',
      GlassesButton.main  => 'MAIN',
      GlassesButton.back  => 'BACK',
      _                   => '?',
    };
    final actionName = switch (action) {
      GlassesActionType.buttonMake   => 'PRESS',
      GlassesActionType.buttonBreak  => 'release',
      GlassesActionType.buttonShort  => 'SHORT ✓',
      GlassesActionType.buttonLong   => 'LONG ✓',
      GlassesActionType.buttonRepeat => 'repeat…',
      _                              => action?.name ?? '?',
    };
    final isGood = action == GlassesActionType.buttonShort ||
        action == GlassesActionType.buttonLong ||
        action == GlassesActionType.buttonMake;
    final color = isGood ? Colors.cyanAccent : Colors.grey.shade600;

    final h  = entry.time.hour.toString().padLeft(2, '0');
    final m  = entry.time.minute.toString().padLeft(2, '0');
    final s  = entry.time.second.toString().padLeft(2, '0');
    final ms = (entry.time.millisecond ~/ 10).toString().padLeft(2, '0');

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 1),
      child: Row(
        children: [
          Text('$h:$m:$s.$ms',
              style: const TextStyle(
                  fontSize: 10,
                  fontFamily: 'monospace',
                  color: Colors.grey)),
          const SizedBox(width: 8),
          SizedBox(
            width: 44,
            child: Text(btnName,
                style: TextStyle(
                    fontSize: 10,
                    fontWeight: FontWeight.bold,
                    color: color)),
          ),
          Text(actionName,
              style: TextStyle(fontSize: 10, color: color)),
        ],
      ),
    );
  }
}
