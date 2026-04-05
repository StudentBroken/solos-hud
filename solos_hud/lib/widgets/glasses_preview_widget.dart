import 'dart:async';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/hud_controller.dart';
import '../core/hud_ui/glasses_menu_controller.dart';
import '../core/rfcomm/glasses_event_service.dart';

/// Phone-side preview of the glasses display, plus three interactive buttons
/// that simulate the physical FRONT / MAIN / BACK glasses buttons.
///
/// Physical button presses from the glasses animate the on-screen buttons.
/// Tapping / long-pressing the on-screen buttons injects synthetic events into
/// [GlassesEventService.simulateButton] so the full [GlassesMenuController]
/// gesture pipeline handles them identically to real hardware events.
class GlassesPreviewWidget extends StatefulWidget {
  const GlassesPreviewWidget({super.key});

  @override
  State<GlassesPreviewWidget> createState() => _GlassesPreviewWidgetState();
}

class _GlassesPreviewWidgetState extends State<GlassesPreviewWidget> {
  final Map<GlassesButton, _BtnState> _states = {
    GlassesButton.front: _BtnState(),
    GlassesButton.main: _BtnState(),
    GlassesButton.back: _BtnState(),
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
      s.active = true;
      s.label = _actionLabel(event.action);
      s.simulated = false;
      _log.insert(0, _LogEntry(btn: btn, event: event));
      if (_log.length > 8) _log.removeLast();
    });
    Future.delayed(const Duration(milliseconds: 400), () {
      if (mounted) setState(() => _states[btn]!.active = false);
    });
  }

  void _simulate(GlassesButton button, GlassesActionType action) {
    context.read<GlassesEventService>().simulateButton(button, action);
    setState(() {
      final s = _states[button]!;
      s.active = true;
      s.label = _actionLabel(action);
      s.simulated = true;
    });
    Future.delayed(const Duration(milliseconds: 300), () {
      if (mounted) setState(() => _states[button]!.active = false);
    });
  }

  static String _actionLabel(GlassesActionType? a) => switch (a) {
        GlassesActionType.buttonMake => 'PRESS',
        GlassesActionType.buttonBreak => 'release',
        GlassesActionType.buttonShort => 'SHORT',
        GlassesActionType.buttonLong => 'LONG',
        _ => '',
      };

  @override
  void dispose() {
    _sub?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final hud = context.watch<HudController>();
    final menu = context.watch<GlassesMenuController>();
    final inMenu = menu.isOpen;

    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(14, 12, 14, 14),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ── Header ──────────────────────────────────────────────────
            Row(
              children: [
                const Icon(
                  Icons.vrpano_outlined,
                  size: 16,
                  color: Colors.cyanAccent,
                ),
                const SizedBox(width: 6),
                Text(
                  'PREVIEW',
                  style: Theme.of(context).textTheme.labelSmall?.copyWith(
                        color: Colors.cyanAccent,
                        letterSpacing: 1.4,
                        fontWeight: FontWeight.bold,
                      ),
                ),
                const Spacer(),
                if (inMenu)
                  const _PillBadge(label: 'MENU OPEN', color: Colors.cyanAccent)
                else if (hud.activeApp != null)
                  _PillBadge(
                    label: hud.activeApp!.name.toUpperCase(),
                    color: Colors.green,
                  ),
              ],
            ),
            const SizedBox(height: 12),

            // ── Glasses display ──────────────────────────────────────────
            _GlassesDisplay(frame: hud.lastFrame, running: hud.running),
            const SizedBox(height: 14),

            // ── Menu state (only when open) ──────────────────────────────
            if (inMenu) ...[
              _MenuStateBar(menu: menu),
              const SizedBox(height: 12),
            ],

            // ── Simulation buttons ───────────────────────────────────────
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _SimButton(
                  label: 'FRONT',
                  sublabel: inMenu ? '↑ scroll up' : 'tap / hold',
                  state: _states[GlassesButton.front]!,
                  color: Colors.cyanAccent,
                  onShort: () => _simulate(
                    GlassesButton.front,
                    GlassesActionType.buttonShort,
                  ),
                  onLong: () => _simulate(
                    GlassesButton.front,
                    GlassesActionType.buttonLong,
                  ),
                ),
                _SimButton(
                  label: 'MAIN',
                  sublabel: inMenu ? 'select / close' : 'hold = menu',
                  state: _states[GlassesButton.main]!,
                  color: Colors.white,
                  large: true,
                  onShort: () => _simulate(
                    GlassesButton.main,
                    GlassesActionType.buttonShort,
                  ),
                  onLong: () => _simulate(
                    GlassesButton.main,
                    GlassesActionType.buttonLong,
                  ),
                ),
                _SimButton(
                  label: 'BACK',
                  sublabel: inMenu ? '↓ scroll down' : 'tap / hold',
                  state: _states[GlassesButton.back]!,
                  color: Colors.cyanAccent,
                  onShort: () => _simulate(
                    GlassesButton.back,
                    GlassesActionType.buttonShort,
                  ),
                  onLong: () => _simulate(
                    GlassesButton.back,
                    GlassesActionType.buttonLong,
                  ),
                ),
              ],
            ),

            // ── Gesture hint (collapsed when menu is open) ───────────────
            if (!inMenu) ...[
              const SizedBox(height: 12),
              _GestureHint(),
            ],

            // ── Event log ────────────────────────────────────────────────
            if (_log.isNotEmpty) ...[
              const SizedBox(height: 10),
              Text(
                'RECENT',
                style: Theme.of(context).textTheme.labelSmall?.copyWith(
                      color: Colors.grey,
                      letterSpacing: 1.2,
                    ),
              ),
              const SizedBox(height: 4),
              ..._log.take(4).map((e) => _LogRow(entry: e)),
            ],
          ],
        ),
      ),
    );
  }
}

// ── Glasses display canvas ────────────────────────────────────────────────────

class _GlassesDisplay extends StatelessWidget {
  final ui.Image? frame;
  final bool running;
  const _GlassesDisplay({required this.frame, required this.running});

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: 428 / 240,
      child: Container(
        decoration: BoxDecoration(
          color: Colors.black,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(color: Colors.grey.shade800, width: 1.5),
        ),
        clipBehavior: Clip.hardEdge,
        child: frame != null
            ? CustomPaint(painter: _FramePainter(frame!))
            : Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      Icons.tv_off_outlined,
                      color: Colors.grey.shade700,
                      size: 26,
                    ),
                    const SizedBox(height: 6),
                    Text(
                      running ? 'Rendering…' : 'No active display',
                      style: TextStyle(
                        fontSize: 11,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              ),
      ),
    );
  }
}

class _FramePainter extends CustomPainter {
  final ui.Image image;
  const _FramePainter(this.image);

  @override
  void paint(Canvas canvas, Size size) {
    final src = Rect.fromLTWH(
      0, 0, image.width.toDouble(), image.height.toDouble(),
    );
    canvas.drawImageRect(
      image,
      src,
      Offset.zero & size,
      Paint()..filterQuality = FilterQuality.low,
    );
  }

  @override
  bool shouldRepaint(_FramePainter old) => old.image != image;
}

// ── Menu state bar ────────────────────────────────────────────────────────────

class _MenuStateBar extends StatelessWidget {
  final GlassesMenuController menu;
  const _MenuStateBar({required this.menu});

  @override
  Widget build(BuildContext context) {
    final selected = menu.apps.isNotEmpty
        ? menu.apps[menu.selectedIndex].name
        : '—';
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: Colors.cyanAccent.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(6),
        border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.3)),
      ),
      child: Row(
        children: [
          const Icon(Icons.grid_view_rounded, size: 12, color: Colors.cyanAccent),
          const SizedBox(width: 6),
          Expanded(
            child: Text(
              selected,
              style: const TextStyle(fontSize: 11, color: Colors.cyanAccent),
              overflow: TextOverflow.ellipsis,
            ),
          ),
          Text(
            '${menu.selectedIndex + 1} / ${menu.apps.length}',
            style: TextStyle(
              fontSize: 10,
              color: Colors.cyanAccent.withValues(alpha: 0.6),
            ),
          ),
        ],
      ),
    );
  }
}

// ── Simulation button ─────────────────────────────────────────────────────────

class _SimButton extends StatelessWidget {
  final String label;
  final String sublabel;
  final _BtnState state;
  final Color color;
  final bool large;
  final VoidCallback onShort;
  final VoidCallback onLong;

  const _SimButton({
    required this.label,
    required this.sublabel,
    required this.state,
    required this.color,
    required this.onShort,
    required this.onLong,
    this.large = false,
  });

  @override
  Widget build(BuildContext context) {
    final size = large ? 68.0 : 56.0;
    final active = state.active;

    return Column(
      children: [
        GestureDetector(
          onTap: onShort,
          onLongPress: onLong,
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 80),
            width: size,
            height: size,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: active
                  ? color.withValues(alpha: state.simulated ? 0.35 : 0.18)
                  : Colors.grey.shade900,
              border: Border.all(
                color: active ? color : Colors.grey.shade700,
                width: active ? 2.5 : 1.5,
              ),
              boxShadow: active
                  ? [BoxShadow(color: color.withValues(alpha: 0.45), blurRadius: 14)]
                  : const [],
            ),
            child: Center(
              child: Text(
                state.label.isNotEmpty ? state.label : (large ? 'hold' : 'tap'),
                style: TextStyle(
                  fontSize: 10,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 0.3,
                  color: active ? color : Colors.grey.shade600,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ),
        ),
        const SizedBox(height: 6),
        Text(
          label,
          style: TextStyle(
            fontSize: 11,
            fontWeight: FontWeight.bold,
            letterSpacing: 0.5,
            color: active ? color : Colors.grey.shade500,
          ),
        ),
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

// ── Gesture hint ──────────────────────────────────────────────────────────────

class _GestureHint extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
      ),
      child: const Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _HintRow(icon: Icons.menu, text: 'Hold MAIN  →  open app launcher'),
          SizedBox(height: 3),
          _HintRow(
            icon: Icons.check_circle_outline,
            text: 'Tap MAIN  →  launch selected app',
          ),
          SizedBox(height: 3),
          _HintRow(
            icon: Icons.keyboard_arrow_up,
            text: 'FRONT  →  scroll up / prev',
          ),
          SizedBox(height: 3),
          _HintRow(
            icon: Icons.keyboard_arrow_down,
            text: 'BACK  →  scroll down / next',
          ),
          SizedBox(height: 3),
          _HintRow(
            icon: Icons.double_arrow,
            text: 'Double-tap FRONT  →  voice assistant',
          ),
        ],
      ),
    );
  }
}

class _HintRow extends StatelessWidget {
  final IconData icon;
  final String text;
  const _HintRow({required this.icon, required this.text});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Icon(icon, size: 13, color: Colors.grey.shade600),
        const SizedBox(width: 7),
        Expanded(
          child: Text(
            text,
            style: TextStyle(fontSize: 11, color: Colors.grey.shade400),
          ),
        ),
      ],
    );
  }
}

// ── Pill badge ────────────────────────────────────────────────────────────────

class _PillBadge extends StatelessWidget {
  final String label;
  final Color color;
  const _PillBadge({required this.label, required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.12),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Text(
        label,
        style: TextStyle(
          fontSize: 9,
          color: color,
          fontWeight: FontWeight.bold,
          letterSpacing: 0.8,
        ),
      ),
    );
  }
}

// ── Internal state / log helpers ──────────────────────────────────────────────

class _BtnState {
  bool active = false;
  String label = '';
  bool simulated = false;
}

class _LogEntry {
  final GlassesButton btn;
  final GlassesEvent event;
  final DateTime time;
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
      GlassesButton.main => 'MAIN',
      GlassesButton.back => 'BACK',
      _ => '?',
    };
    final actionName = switch (action) {
      GlassesActionType.buttonShort => 'SHORT ✓',
      GlassesActionType.buttonLong => 'LONG ✓',
      GlassesActionType.buttonMake => 'PRESS',
      GlassesActionType.buttonBreak => 'release',
      _ => action?.name ?? '?',
    };
    final isGood = action == GlassesActionType.buttonShort ||
        action == GlassesActionType.buttonLong;
    final color = isGood ? Colors.cyanAccent : Colors.grey.shade600;

    final h = entry.time.hour.toString().padLeft(2, '0');
    final m = entry.time.minute.toString().padLeft(2, '0');
    final s = entry.time.second.toString().padLeft(2, '0');

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 1.5),
      child: Row(
        children: [
          Text(
            '$h:$m:$s',
            style: const TextStyle(
              fontSize: 10,
              fontFamily: 'monospace',
              color: Colors.grey,
            ),
          ),
          const SizedBox(width: 8),
          SizedBox(
            width: 44,
            child: Text(
              btnName,
              style: TextStyle(
                fontSize: 10,
                fontWeight: FontWeight.bold,
                color: color,
              ),
            ),
          ),
          Text(actionName, style: TextStyle(fontSize: 10, color: color)),
        ],
      ),
    );
  }
}
