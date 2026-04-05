import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/notifications/notification_service.dart';
import '../../core/notifications/notification_event.dart';
import '../../core/notifications/notification_overlay_controller.dart';
import '../../core/notifications/notification_action_service.dart';

class NotificationsWidget extends StatelessWidget {
  const NotificationsWidget({super.key});

  @override
  Widget build(BuildContext context) {
    final overlay = context.watch<NotificationOverlayController>();
    final notifs  = context.watch<NotificationService>();
    final cs      = Theme.of(context).colorScheme;

    final recent = notifs.log
        .where((e) =>
            !e.isMedia &&
            !e.isNavigation &&
            !e.isRemoval &&
            (e.title.isNotEmpty || e.text.isNotEmpty))
        .toList()
        .reversed
        .take(8)
        .toList();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // ── Header row ──────────────────────────────────────────────────────
        Padding(
          padding: const EdgeInsets.fromLTRB(4, 0, 4, 8),
          child: Row(
            children: [
              Text(
                'NOTIFICATIONS',
                style: TextStyle(
                  fontSize: 10,
                  fontWeight: FontWeight.w800,
                  letterSpacing: 2,
                  color: cs.onSurfaceVariant.withValues(alpha: 0.5),
                ),
              ),
              const Spacer(),
              // Forward-to-glasses toggle
              GestureDetector(
                onTap: () => overlay.setEnabled(!overlay.enabled),
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 200),
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20),
                    color: overlay.enabled
                        ? const Color(0xFF00BCD4).withValues(alpha: 0.15)
                        : cs.surfaceContainerHighest,
                    border: Border.all(
                      color: overlay.enabled
                          ? const Color(0xFF00BCD4).withValues(alpha: 0.5)
                          : cs.outlineVariant.withValues(alpha: 0.3),
                    ),
                  ),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        overlay.enabled
                            ? Icons.vrpano_rounded
                            : Icons.vrpano_outlined,
                        size: 12,
                        color: overlay.enabled
                            ? const Color(0xFF00BCD4)
                            : cs.onSurfaceVariant,
                      ),
                      const SizedBox(width: 4),
                      Text(
                        overlay.enabled ? 'ON GLASSES' : 'GLASSES OFF',
                        style: TextStyle(
                          fontSize: 9,
                          fontWeight: FontWeight.w700,
                          letterSpacing: 0.5,
                          color: overlay.enabled
                              ? const Color(0xFF00BCD4)
                              : cs.onSurfaceVariant,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),

        // ── Active / incoming notification ───────────────────────────────────
        if (overlay.isActive && overlay.current != null) ...[
          _LiveNotifCard(overlay: overlay),
          const SizedBox(height: 8),
        ],

        // ── Recent list ──────────────────────────────────────────────────────
        if (recent.isEmpty)
          Container(
            padding: const EdgeInsets.symmetric(vertical: 18),
            decoration: BoxDecoration(
              color: cs.surfaceContainerLow,
              borderRadius: BorderRadius.circular(12),
            ),
            child: Column(
              children: [
                Icon(Icons.notifications_off_outlined,
                    size: 22, color: cs.onSurfaceVariant.withValues(alpha: 0.3)),
                const SizedBox(height: 6),
                Text(
                  'No recent notifications',
                  style: TextStyle(
                    fontSize: 12,
                    color: cs.onSurfaceVariant.withValues(alpha: 0.4),
                  ),
                ),
              ],
            ),
          )
        else
          ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: Column(
              children: [
                for (int i = 0; i < recent.length; i++) ...[
                  _NotifRow(event: recent[i], isLast: i == recent.length - 1),
                ],
              ],
            ),
          ),
      ],
    );
  }
}

// ── Live / incoming card ────────────────────────────────────────────────────

class _LiveNotifCard extends StatelessWidget {
  final NotificationOverlayController overlay;
  const _LiveNotifCard({required this.overlay});

  @override
  Widget build(BuildContext context) {
    final n  = overlay.current!;
    final cs = Theme.of(context).colorScheme;

    final accentColor = n.isCall
        ? const Color(0xFF00E676)
        : n.isMessage
            ? const Color(0xFF00E5FF)
            : const Color(0xFFFFB300);

    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(14),
        color: accentColor.withValues(alpha: 0.07),
        border: Border.all(color: accentColor.withValues(alpha: 0.35)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // App row
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 10, 10, 0),
            child: Row(
              children: [
                _AppIcon(
                  icon: n.isCall
                      ? Icons.call_rounded
                      : n.isMessage
                          ? Icons.chat_bubble_rounded
                          : Icons.notifications_rounded,
                  color: accentColor,
                  size: 14,
                ),
                const SizedBox(width: 8),
                Text(
                  n.appName.toUpperCase(),
                  style: TextStyle(
                    fontSize: 9,
                    fontWeight: FontWeight.w800,
                    letterSpacing: 1.2,
                    color: accentColor,
                  ),
                ),
                const Spacer(),
                _LiveDot(color: accentColor),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: overlay.dismiss,
                  child: Icon(Icons.close_rounded,
                      size: 16, color: cs.onSurfaceVariant),
                ),
              ],
            ),
          ),
          // Content
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 6, 12, 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (n.title.isNotEmpty)
                  Text(
                    n.title,
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w700,
                      color: Colors.white,
                    ),
                  ),
                if (n.text.isNotEmpty) ...[
                  const SizedBox(height: 3),
                  Text(
                    n.text,
                    style: TextStyle(
                      fontSize: 13,
                      color: cs.onSurface.withValues(alpha: 0.7),
                      height: 1.3,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ],
            ),
          ),
          // Call actions
          if (n.isCall) ...[
            Padding(
              padding: const EdgeInsets.fromLTRB(12, 0, 12, 12),
              child: Row(
                children: [
                  Expanded(
                    child: _ActionButton(
                      label: 'Answer',
                      icon: Icons.call_rounded,
                      color: const Color(0xFF00E676),
                      onTap: () {
                        NotificationActionService.invoke(
                            n.key, n.answerAction?.index ?? 0);
                        overlay.dismiss();
                      },
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: _ActionButton(
                      label: 'Decline',
                      icon: Icons.call_end_rounded,
                      color: const Color(0xFFFF1744),
                      onTap: () {
                        NotificationActionService.invoke(
                            n.key, n.declineAction?.index ?? 1);
                        overlay.dismiss();
                      },
                    ),
                  ),
                ],
              ),
            ),
          ],
          // Reply
          if (n.isMessage && n.replyAction != null) ...[
            Padding(
              padding: const EdgeInsets.fromLTRB(12, 0, 12, 12),
              child: Row(
                children: [
                  Expanded(
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 12, vertical: 8),
                      decoration: BoxDecoration(
                        color: const Color(0xFF00E5FF).withValues(alpha: 0.08),
                        borderRadius: BorderRadius.circular(8),
                        border: Border.all(
                            color: const Color(0xFF00E5FF).withValues(alpha: 0.25)),
                      ),
                      child: Text(
                        '"${overlay.currentReply}"',
                        style: const TextStyle(
                            fontSize: 12, color: Color(0xFF00E5FF)),
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  GestureDetector(
                    onTap: () {
                      NotificationActionService.invoke(
                        n.key,
                        n.replyAction!.index,
                        replyText: overlay.currentReply,
                      );
                      overlay.dismiss();
                    },
                    child: Container(
                      width: 38,
                      height: 38,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: const Color(0xFF00E5FF).withValues(alpha: 0.12),
                        border: Border.all(
                            color: const Color(0xFF00E5FF).withValues(alpha: 0.3)),
                      ),
                      child: const Icon(Icons.send_rounded,
                          size: 16, color: Color(0xFF00E5FF)),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ],
      ),
    );
  }
}

// ── Recent notification row ─────────────────────────────────────────────────

class _NotifRow extends StatelessWidget {
  final NotificationEvent event;
  final bool isLast;
  const _NotifRow({required this.event, required this.isLast});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    final accentColor = event.isCall
        ? const Color(0xFF00E676)
        : event.isMessage
            ? const Color(0xFF00E5FF)
            : cs.onSurfaceVariant.withValues(alpha: 0.5);

    final icon = event.isCall
        ? Icons.call_rounded
        : event.isMessage
            ? Icons.chat_bubble_rounded
            : Icons.notifications_rounded;

    return Container(
      decoration: BoxDecoration(
        color: cs.surfaceContainerLow,
        border: isLast
            ? null
            : Border(
                bottom: BorderSide(
                    color: cs.outlineVariant.withValues(alpha: 0.15), width: 1)),
      ),
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _AppIcon(icon: icon, color: accentColor, size: 13),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Text(
                        event.appName,
                        style: TextStyle(
                          fontSize: 10,
                          fontWeight: FontWeight.w700,
                          color: accentColor,
                          letterSpacing: 0.3,
                        ),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    Text(
                      _timeLabel(event.timestamp),
                      style: TextStyle(
                        fontSize: 10,
                        color: cs.onSurfaceVariant.withValues(alpha: 0.4),
                      ),
                    ),
                  ],
                ),
                if (event.title.isNotEmpty) ...[
                  const SizedBox(height: 1),
                  Text(
                    event.title,
                    style: TextStyle(
                      fontSize: 13,
                      fontWeight: FontWeight.w600,
                      color: cs.onSurface,
                    ),
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
                if (event.text.isNotEmpty) ...[
                  const SizedBox(height: 1),
                  Text(
                    event.text,
                    style: TextStyle(
                      fontSize: 12,
                      color: cs.onSurfaceVariant.withValues(alpha: 0.6),
                    ),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }

  String _timeLabel(DateTime t) {
    final diff = DateTime.now().difference(t);
    if (diff.inSeconds < 60) return 'now';
    if (diff.inMinutes < 60) return '${diff.inMinutes}m';
    if (diff.inHours < 24) return '${diff.inHours}h';
    return '${diff.inDays}d';
  }
}

// ── Small helpers ────────────────────────────────────────────────────────────

class _AppIcon extends StatelessWidget {
  final IconData icon;
  final Color color;
  final double size;
  const _AppIcon({required this.icon, required this.color, required this.size});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size * 2.2,
      height: size * 2.2,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: color.withValues(alpha: 0.12),
      ),
      child: Icon(icon, size: size, color: color),
    );
  }
}

class _ActionButton extends StatelessWidget {
  final String label;
  final IconData icon;
  final Color color;
  final VoidCallback onTap;
  const _ActionButton(
      {required this.label,
      required this.icon,
      required this.color,
      required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 9),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(10),
          color: color.withValues(alpha: 0.12),
          border: Border.all(color: color.withValues(alpha: 0.35)),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 14, color: color),
            const SizedBox(width: 5),
            Text(label,
                style: TextStyle(
                    fontSize: 12, fontWeight: FontWeight.w700, color: color)),
          ],
        ),
      ),
    );
  }
}

class _LiveDot extends StatelessWidget {
  final Color color;
  const _LiveDot({required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 6,
      height: 6,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: color,
        boxShadow: [BoxShadow(color: color.withValues(alpha: 0.6), blurRadius: 5)],
      ),
    );
  }
}
