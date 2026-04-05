import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../core/notifications/notification_event.dart';
import '../../core/intent/intent_service.dart';
import '../../core/settings/app_settings.dart';
import 'navigation_app.dart';

class NavigationPhoneWidget extends StatelessWidget {
  final NavigationApp app;
  const NavigationPhoneWidget({super.key, required this.app});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: Listenable.merge([
        app.notifications,
        app.intents,
        app.settings,
        app.routeService,
      ]),
      builder: (context, _) {
        final nav = app.notifications.currentNav;
        final dest = app.intents.destination;
        final route = app.routeService.route;
        final log = app.notifications.log
            .where((e) => e.isNavigation && !e.isRemoval)
            .toList()
            .reversed
            .take(8)
            .toList();

        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // ── Display mode toggle ──────────────────────────────────────────
            _ModeToggle(app: app),
            const SizedBox(height: 12),

            // ── Route status (map mode only) ─────────────────────────────────
            if (app.displayMode == NavDisplayMode.map) ...[
              _RouteStatusCard(app: app, dest: dest, route: route),
              const SizedBox(height: 12),
            ],

            // ── How to share ────────────────────────────────────────────────
            _ShareInstructions(),
            const SizedBox(height: 12),

            // ── Destination card ─────────────────────────────────────────────
            _DestinationCard(dest: dest, intents: app.intents),
            const SizedBox(height: 12),

            // ── Current instruction ──────────────────────────────────────────
            if (nav != null) ...[
              _InstructionCard(instruction: nav),
              const SizedBox(height: 12),
            ],

            // ── Notification access status ────────────────────────────────────
            _NotificationAccessBanner(),
            const SizedBox(height: 12),

            // ── Recent nav events ────────────────────────────────────────────
            if (log.isNotEmpty) ...[
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 4),
                child: Row(
                  children: [
                    Text(
                      'RECENT TURNS',
                      style: Theme.of(context).textTheme.labelSmall?.copyWith(
                        color: Colors.grey,
                        letterSpacing: 1.4,
                      ),
                    ),
                    const Spacer(),
                    TextButton.icon(
                      onPressed: () => app.notifications.clearNav(),
                      icon: const Icon(Icons.clear, size: 14),
                      label: const Text('Clear'),
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.grey,
                        padding: EdgeInsets.zero,
                      ),
                    ),
                  ],
                ),
              ),
              ...log.map((e) => _NavLogTile(event: e)),
            ],
          ],
        );
      },
    );
  }
}

// ── Mode toggle ───────────────────────────────────────────────────────────────

class _ModeToggle extends StatelessWidget {
  final NavigationApp app;
  const _ModeToggle({required this.app});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: app.settings,
      builder: (context, _) {
        final mode = app.displayMode;
        final isMap = mode == NavDisplayMode.map;
        final apiKey = app.settings.getAppValue('nav_mapbox_key', '') as String;
        final hasKey = apiKey.isNotEmpty;
        final headingUp = app.headingUp;
        final zoom = app.mapZoom;

        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Mode selector pills
            Container(
              decoration: BoxDecoration(
                color: Colors.grey.shade900,
                borderRadius: BorderRadius.circular(10),
              ),
              child: Row(
                children: [
                  _ModePill(
                    label: 'Arrow',
                    icon: Icons.turn_right,
                    selected: !isMap,
                    onTap: () => app.settings.setAppValue('nav_mode', 'arrow'),
                  ),
                  _ModePill(
                    label: 'Live Map',
                    icon: Icons.map_outlined,
                    selected: isMap,
                    onTap: () => app.settings.setAppValue('nav_mode', 'map'),
                  ),
                ],
              ),
            ),

            // Map-mode extras (only visible when map mode is selected)
            if (isMap) ...[
              const SizedBox(height: 10),
              // Map options row (always shown — free tiles work without a key)
              Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 8,
                ),
                decoration: BoxDecoration(
                  color: Colors.grey.shade900,
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Column(
                  children: [
                    Row(
                      children: [
                        const Icon(
                          Icons.explore_outlined,
                          size: 16,
                          color: Colors.grey,
                        ),
                        const SizedBox(width: 8),
                        const Expanded(
                          child: Text(
                            'Heading-Up',
                            style: TextStyle(fontSize: 13),
                          ),
                        ),
                        Switch.adaptive(
                          value: headingUp,
                          onChanged: (v) =>
                              app.settings.setAppValue('nav_heading_up', v),
                        ),
                      ],
                    ),
                    Row(
                      children: [
                        const Icon(Icons.zoom_in, size: 16, color: Colors.grey),
                        const SizedBox(width: 8),
                        Text(
                          'Zoom  $zoom',
                          style: const TextStyle(
                            fontSize: 13,
                            fontFamily: 'monospace',
                          ),
                        ),
                        const Spacer(),
                        // − button
                        _ZoomBtn(
                          icon: Icons.remove,
                          enabled: zoom > 12,
                          onTap: () => app.settings.setAppValue(
                            'nav_zoom',
                            (zoom - 1).clamp(12, 18),
                          ),
                        ),
                        const SizedBox(width: 6),
                        // + button
                        _ZoomBtn(
                          icon: Icons.add,
                          enabled: zoom < 18,
                          onTap: () => app.settings.setAppValue(
                            'nav_zoom',
                            (zoom + 1).clamp(12, 18),
                          ),
                        ),
                      ],
                    ),
                    // Tile source indicator + optional Mapbox key
                    const Divider(height: 12),
                    Row(
                      children: [
                        const Icon(
                          Icons.layers_outlined,
                          size: 14,
                          color: Colors.grey,
                        ),
                        const SizedBox(width: 6),
                        Expanded(
                          child: Text(
                            hasKey
                                ? 'Tiles: Mapbox Dark'
                                : 'Tiles: Carto Dark (free)',
                            style: TextStyle(
                              fontSize: 11,
                              color: hasKey ? Colors.cyanAccent : Colors.grey,
                            ),
                          ),
                        ),
                        TextButton.icon(
                          onPressed: () => _showKeyDialog(context),
                          icon: const Icon(Icons.vpn_key_outlined, size: 12),
                          label: Text(
                            hasKey ? 'Change Key' : 'Add Mapbox Key',
                            style: const TextStyle(fontSize: 11),
                          ),
                          style: TextButton.styleFrom(
                            foregroundColor: Colors.grey,
                            padding: EdgeInsets.zero,
                            minimumSize: Size.zero,
                            tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ],
        );
      },
    );
  }

  void _showKeyDialog(BuildContext context) {
    showDialog<void>(
      context: context,
      builder: (_) => _MapboxKeyDialog(settings: app.settings),
    );
  }
}

class _ModePill extends StatelessWidget {
  final String label;
  final IconData icon;
  final bool selected;
  final VoidCallback onTap;
  const _ModePill({
    required this.label,
    required this.icon,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 180),
          margin: const EdgeInsets.all(4),
          padding: const EdgeInsets.symmetric(vertical: 10),
          decoration: BoxDecoration(
            color: selected
                ? Colors.cyanAccent.withValues(alpha: 0.15)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(7),
            border: Border.all(
              color: selected
                  ? Colors.cyanAccent.withValues(alpha: 0.6)
                  : Colors.transparent,
            ),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                icon,
                size: 16,
                color: selected ? Colors.cyanAccent : Colors.grey,
              ),
              const SizedBox(width: 6),
              Text(
                label,
                style: TextStyle(
                  fontSize: 13,
                  fontWeight: selected ? FontWeight.bold : FontWeight.normal,
                  color: selected ? Colors.cyanAccent : Colors.grey,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _MapboxKeyDialog extends StatefulWidget {
  final AppSettings settings;
  const _MapboxKeyDialog({required this.settings});

  @override
  State<_MapboxKeyDialog> createState() => _MapboxKeyDialogState();
}

class _MapboxKeyDialogState extends State<_MapboxKeyDialog> {
  late final TextEditingController _ctrl;

  @override
  void initState() {
    super.initState();
    final existing =
        widget.settings.getAppValue('nav_mapbox_key', '') as String;
    _ctrl = TextEditingController(text: existing);
  }

  @override
  void dispose() {
    _ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Mapbox API Key'),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Get a free key at mapbox.com.\nPaste your public access token below:',
            style: TextStyle(fontSize: 13, color: Colors.grey),
          ),
          const SizedBox(height: 12),
          TextField(
            controller: _ctrl,
            decoration: const InputDecoration(
              hintText: 'pk.eyJ1Ij…',
              border: OutlineInputBorder(),
              isDense: true,
            ),
            style: const TextStyle(fontFamily: 'monospace', fontSize: 12),
            maxLines: 2,
          ),
        ],
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Cancel'),
        ),
        FilledButton(
          onPressed: () {
            widget.settings.setAppValue('nav_mapbox_key', _ctrl.text.trim());
            Navigator.pop(context);
          },
          child: const Text('Save'),
        ),
      ],
    );
  }
}

// ── Route status card ─────────────────────────────────────────────────────────

class _RouteStatusCard extends StatelessWidget {
  final NavigationApp app;
  final SharedDestination? dest;
  final RouteResult? route;
  const _RouteStatusCard({
    required this.app,
    required this.dest,
    required this.route,
  });

  @override
  Widget build(BuildContext context) {
    final rs = app.routeService;

    if (dest == null) {
      return _statusTile(
        icon: Icons.route,
        color: Colors.grey,
        label: 'No route — share a destination first',
      );
    }

    switch (rs.status) {
      case RouteStatus.idle:
        return _statusTile(
          icon: Icons.route,
          color: Colors.grey,
          label: 'Route not started',
        );

      case RouteStatus.loading:
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
          decoration: BoxDecoration(
            color: Colors.cyan.shade900.withValues(alpha: 0.15),
            borderRadius: BorderRadius.circular(8),
          ),
          child: const Row(
            children: [
              SizedBox(
                width: 14,
                height: 14,
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                  color: Colors.cyanAccent,
                ),
              ),
              SizedBox(width: 10),
              Text(
                'Calculating route…',
                style: TextStyle(fontSize: 12, color: Colors.cyanAccent),
              ),
            ],
          ),
        );

      case RouteStatus.error:
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          decoration: BoxDecoration(
            color: Colors.red.shade900.withValues(alpha: 0.25),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.redAccent.withValues(alpha: 0.4)),
          ),
          child: Row(
            children: [
              const Icon(
                Icons.error_outline,
                size: 16,
                color: Colors.redAccent,
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  rs.error ?? 'Route failed',
                  style: const TextStyle(fontSize: 12, color: Colors.redAccent),
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              const SizedBox(width: 6),
              TextButton(
                onPressed: () {
                  if (app.gps.available && dest?.lat != null) {
                    rs.retry(
                      LatLng(app.gps.data.latitude, app.gps.data.longitude),
                      LatLng(dest!.lat!, dest!.lng!),
                    );
                  }
                },
                style: TextButton.styleFrom(
                  foregroundColor: Colors.orangeAccent,
                  padding: const EdgeInsets.symmetric(horizontal: 8),
                  minimumSize: Size.zero,
                  tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                ),
                child: const Text('Retry', style: TextStyle(fontSize: 12)),
              ),
            ],
          ),
        );

      case RouteStatus.ready:
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
          decoration: BoxDecoration(
            color: Colors.cyan.shade900.withValues(alpha: 0.2),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(
              color: Colors.cyanAccent.withValues(alpha: 0.25),
            ),
          ),
          child: Row(
            children: [
              const Icon(Icons.route, size: 16, color: Colors.cyanAccent),
              const SizedBox(width: 8),
              Text(
                'Route ready · ${route!.distanceLabel} · ${route!.durationLabel}'
                ' · ${route!.points.length} pts',
                style: const TextStyle(fontSize: 12, color: Colors.cyanAccent),
              ),
            ],
          ),
        );
    }
  }

  Widget _statusTile({
    required IconData icon,
    required Color color,
    required String label,
  }) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          Icon(icon, size: 16, color: color),
          const SizedBox(width: 8),
          Text(label, style: TextStyle(fontSize: 12, color: color)),
        ],
      ),
    );
  }
}

// ── Share instructions banner ─────────────────────────────────────────────────

// ── Zoom button ───────────────────────────────────────────────────────────────

class _ZoomBtn extends StatelessWidget {
  final IconData icon;
  final bool enabled;
  final VoidCallback onTap;
  const _ZoomBtn({
    required this.icon,
    required this.enabled,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: enabled ? onTap : null,
      child: Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: enabled
              ? Colors.cyanAccent.withValues(alpha: 0.12)
              : Colors.grey.shade900,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(
            color: enabled
                ? Colors.cyanAccent.withValues(alpha: 0.4)
                : Colors.grey.shade800,
          ),
        ),
        child: Icon(
          icon,
          size: 18,
          color: enabled ? Colors.cyanAccent : Colors.grey.shade700,
        ),
      ),
    );
  }
}

class _ShareInstructions extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.blue.shade900.withValues(alpha: 0.35),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.blue.shade800),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Icon(Icons.info_outline, color: Colors.blueAccent, size: 20),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'How to send a destination',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                    fontSize: 13,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  '1. Open Google Maps and find your destination.\n'
                  '2. Tap  Share  →  Solos HUD.\n'
                  '3. Start navigation in Google Maps — turn instructions '
                  'will appear on your glasses automatically.',
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.grey.shade300,
                    height: 1.5,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Destination card ──────────────────────────────────────────────────────────

class _DestinationCard extends StatelessWidget {
  final dynamic dest; // SharedDestination?
  final IntentService intents;
  const _DestinationCard({required this.dest, required this.intents});

  @override
  Widget build(BuildContext context) {
    if (dest == null) {
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 18),
        decoration: BoxDecoration(
          color: Colors.grey.shade900,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Row(
          children: [
            Icon(Icons.place_outlined, color: Colors.grey.shade600),
            const SizedBox(width: 10),
            Text(
              'No destination set',
              style: TextStyle(color: Colors.grey.shade600, fontSize: 13),
            ),
          ],
        ),
      );
    }

    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.teal.shade900.withValues(alpha: 0.4),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.tealAccent.withValues(alpha: 0.3)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.place, color: Colors.tealAccent, size: 18),
              const SizedBox(width: 8),
              const Text(
                'DESTINATION',
                style: TextStyle(
                  fontSize: 11,
                  color: Colors.tealAccent,
                  letterSpacing: 1.2,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const Spacer(),
              IconButton(
                icon: const Icon(Icons.close, size: 16, color: Colors.grey),
                padding: EdgeInsets.zero,
                constraints: const BoxConstraints(),
                tooltip: 'Clear destination',
                onPressed: () => intents.clearDestination(),
              ),
            ],
          ),
          const SizedBox(height: 6),
          Text(
            dest.label as String,
            style: const TextStyle(
              fontSize: 16,
              color: Colors.white,
              fontWeight: FontWeight.w600,
            ),
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          if (dest.rawUrl != null) ...[
            const SizedBox(height: 4),
            Text(
              dest.rawUrl as String,
              style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ],
          const SizedBox(height: 10),
          Row(
            children: [
              _ActionButton(
                icon: Icons.navigation,
                label: 'Open in Maps',
                onTap: () => _openMaps(context, dest),
              ),
              const SizedBox(width: 8),
              _ActionButton(
                icon: Icons.copy,
                label: 'Copy',
                onTap: () => _copy(context, dest),
              ),
            ],
          ),
        ],
      ),
    );
  }

  void _openMaps(BuildContext context, dynamic d) {
    // Use Android intent to open Google Maps with the URL
    const ch = MethodChannel('solos_intent');
    ch.invokeMethod('openUrl', {'url': d.googleMapsUrl}).catchError((_) {});
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('Opening Google Maps…'),
        duration: Duration(seconds: 1),
      ),
    );
  }

  void _copy(BuildContext context, dynamic d) {
    Clipboard.setData(ClipboardData(text: d.googleMapsUrl as String));
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('Copied to clipboard'),
        duration: Duration(seconds: 1),
      ),
    );
  }
}

class _ActionButton extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;
  const _ActionButton({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return TextButton.icon(
      onPressed: onTap,
      icon: Icon(icon, size: 15),
      label: Text(label, style: const TextStyle(fontSize: 12)),
      style: TextButton.styleFrom(
        foregroundColor: Colors.tealAccent,
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
        side: BorderSide(color: Colors.tealAccent.withValues(alpha: 0.3)),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(6)),
      ),
    );
  }
}

// ── Current instruction card ──────────────────────────────────────────────────

class _InstructionCard extends StatelessWidget {
  final NavInstruction instruction;
  const _InstructionCard({required this.instruction});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.cyan.shade900.withValues(alpha: 0.3),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.5)),
      ),
      child: Row(
        children: [
          // Arrow icon
          Container(
            width: 80,
            height: 80,
            decoration: BoxDecoration(
              color: Colors.cyanAccent.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Icon(
              _maneuverIcon(instruction.maneuver),
              color: Colors.cyanAccent,
              size: 72,
            ),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  instruction.maneuverLabel,
                  style: const TextStyle(
                    color: Colors.cyanAccent,
                    fontSize: 14,
                    fontWeight: FontWeight.bold,
                    letterSpacing: 1,
                  ),
                ),
                if (instruction.distanceText.isNotEmpty) ...[
                  const SizedBox(height: 2),
                  Text(
                    instruction.distanceText,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 34,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'monospace',
                    ),
                  ),
                ],
                if (instruction.streetName.isNotEmpty) ...[
                  const SizedBox(height: 2),
                  Text(
                    instruction.streetName,
                    style: TextStyle(color: Colors.grey.shade300, fontSize: 18),
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

  IconData _maneuverIcon(Maneuver m) => switch (m) {
    Maneuver.straight => Icons.straight,
    Maneuver.turnLeft => Icons.turn_left,
    Maneuver.turnRight => Icons.turn_right,
    Maneuver.slightLeft => Icons.turn_slight_left,
    Maneuver.slightRight => Icons.turn_slight_right,
    Maneuver.sharpLeft => Icons.turn_sharp_left,
    Maneuver.sharpRight => Icons.turn_sharp_right,
    Maneuver.uTurn => Icons.u_turn_left,
    Maneuver.roundabout => Icons.roundabout_left,
    Maneuver.merge => Icons.merge,
    Maneuver.arrive => Icons.flag,
    Maneuver.unknown => Icons.navigation,
  };
}

// ── Notification access banner ────────────────────────────────────────────────

class _NotificationAccessBanner extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // This banner guides the user to grant notification listener access.
    // We can't check from Dart if the permission was granted without a helper method,
    // so we show it as a collapsible tip.
    return Theme(
      data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
      child: ExpansionTile(
        leading: const Icon(
          Icons.notifications_active_outlined,
          color: Colors.orangeAccent,
          size: 20,
        ),
        title: const Text(
          'Notification access required',
          style: TextStyle(fontSize: 13, color: Colors.orangeAccent),
        ),
        subtitle: const Text(
          'Needed for live turn instructions',
          style: TextStyle(fontSize: 11, color: Colors.grey),
        ),
        tilePadding: EdgeInsets.zero,
        childrenPadding: const EdgeInsets.only(bottom: 8),
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: Colors.orange.shade900.withValues(alpha: 0.2),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const Text(
                  'Grant Solos HUD access to read notifications so '
                  'turn-by-turn instructions from Google Maps appear '
                  'on your glasses in real time.',
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.white70,
                    height: 1.4,
                  ),
                ),
                const SizedBox(height: 10),
                ElevatedButton.icon(
                  onPressed: () => _openNotificationSettings(context),
                  icon: const Icon(Icons.settings, size: 16),
                  label: const Text('Open Notification Access Settings'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.orange.shade800,
                    foregroundColor: Colors.white,
                    textStyle: const TextStyle(fontSize: 12),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _openNotificationSettings(BuildContext context) {
    const ch = MethodChannel('solos_intent');
    ch.invokeMethod('openNotificationSettings').catchError((_) {});
  }
}

// ── Nav log tile ──────────────────────────────────────────────────────────────

class _NavLogTile extends StatelessWidget {
  final NotificationEvent event;
  const _NavLogTile({required this.event});

  @override
  Widget build(BuildContext context) {
    final instr = event.navInstruction;
    final time = event.timestamp;
    final hh = time.hour.toString().padLeft(2, '0');
    final mm = time.minute.toString().padLeft(2, '0');
    final ss = time.second.toString().padLeft(2, '0');

    return Container(
      margin: const EdgeInsets.only(bottom: 4),
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(6),
      ),
      child: Row(
        children: [
          Text(
            '$hh:$mm:$ss',
            style: const TextStyle(
              fontSize: 11,
              fontFamily: 'monospace',
              color: Colors.grey,
            ),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Text(
              instr != null
                  ? '${instr.maneuverLabel}'
                        '${instr.distanceText.isNotEmpty ? " · ${instr.distanceText}" : ""}'
                        '${instr.streetName.isNotEmpty ? " · ${instr.streetName}" : ""}'
                  : event.instructionText,
              style: const TextStyle(fontSize: 12, color: Colors.white70),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}
