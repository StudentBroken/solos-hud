import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/glasses_app.dart';
import '../core/hud_controller.dart';

/// Compact app tile. Tap the row to activate/deactivate, tap the chevron to
/// expand the full phone widget controls.
class AppCard extends StatefulWidget {
  final GlassesApp app;
  const AppCard({super.key, required this.app});

  @override
  State<AppCard> createState() => _AppCardState();
}

class _AppCardState extends State<AppCard> {
  bool _expanded = false;

  void _toggle(HudController hud, bool isActive) {
    if (isActive) {
      hud.setActiveApp(null);
      hud.stopLoop();
    } else {
      hud.setActiveApp(widget.app);
      if (!hud.running) hud.startLoop();
    }
  }

  @override
  Widget build(BuildContext context) {
    final hud = context.watch<HudController>();
    final isActive = hud.activeApp?.id == widget.app.id;
    final cs = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 3),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(14),
          border: Border.all(
            color: isActive
                ? cs.primary.withValues(alpha: 0.7)
                : cs.outlineVariant.withValues(alpha: 0.45),
            width: isActive ? 1.5 : 1,
          ),
          color: isActive
              ? cs.primaryContainer.withValues(alpha: 0.18)
              : cs.surfaceContainerLow,
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(13),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // ── Header ────────────────────────────────────────────
              InkWell(
                onTap: () => _toggle(hud, isActive),
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(12, 9, 4, 9),
                  child: Row(
                    children: [
                      // Icon container
                      AnimatedContainer(
                        duration: const Duration(milliseconds: 200),
                        width: 36,
                        height: 36,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: isActive
                              ? cs.primaryContainer
                              : cs.surfaceContainerHighest,
                        ),
                        child: Icon(
                          widget.app.icon,
                          size: 17,
                          color: isActive
                              ? cs.onPrimaryContainer
                              : cs.onSurfaceVariant,
                        ),
                      ),
                      const SizedBox(width: 11),
                      // Name + live preview text
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              widget.app.name,
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.w600,
                                color: isActive ? cs.primary : cs.onSurface,
                              ),
                            ),
                            const SizedBox(height: 1),
                            DefaultTextStyle(
                              style: TextStyle(
                                fontSize: 11,
                                color: cs.onSurfaceVariant,
                              ),
                              child: widget.app.buildPreviewWidget(context),
                            ),
                          ],
                        ),
                      ),
                      // LIVE badge
                      if (isActive) ...[
                        _LiveBadge(color: cs.primary),
                        const SizedBox(width: 2),
                      ],
                      // Expand/collapse
                      IconButton(
                        icon: Icon(
                          _expanded
                              ? Icons.keyboard_arrow_up_rounded
                              : Icons.keyboard_arrow_down_rounded,
                          size: 20,
                          color: cs.onSurfaceVariant,
                        ),
                        visualDensity: VisualDensity.compact,
                        padding: const EdgeInsets.all(8),
                        onPressed: () => setState(() => _expanded = !_expanded),
                      ),
                    ],
                  ),
                ),
              ),
              // ── Expanded controls ─────────────────────────────────
              if (_expanded) ...[
                Divider(
                  height: 1,
                  color: isActive
                      ? cs.primary.withValues(alpha: 0.18)
                      : cs.outlineVariant.withValues(alpha: 0.3),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(12, 12, 12, 16),
                  child: widget.app.buildPhoneWidget(context),
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}

class _LiveBadge extends StatelessWidget {
  final Color color;
  const _LiveBadge({required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.13),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            width: 5,
            height: 5,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: color,
            ),
          ),
          const SizedBox(width: 4),
          Text(
            'LIVE',
            style: TextStyle(
              fontSize: 9,
              fontWeight: FontWeight.w800,
              color: color,
              letterSpacing: 0.6,
            ),
          ),
        ],
      ),
    );
  }
}
