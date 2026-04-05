import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/rfcomm/rfcomm_service.dart';
import '../core/rfcomm/glasses_event_service.dart';
import '../core/hud_ui/glasses_menu_controller.dart';
import '../core/glasses_app.dart';
import '../core/hud_controller.dart';
import '../widgets/glasses_preview_widget.dart';
import '../apps/notifications/notifications_widget.dart';
import 'scan_sheet.dart';

// ══════════════════════════════════════════════════════════════════════════════
// HomeScreen — state machine:
//   1. Disconnected  → full-screen connect prompt
//   2. Connected, no active app → app launcher grid
//   3. Connected, app active   → app's full-screen dashboard
// ══════════════════════════════════════════════════════════════════════════════

class HomeScreen extends StatelessWidget {
  final List<GlassesApp> apps;
  const HomeScreen({super.key, required this.apps});

  @override
  Widget build(BuildContext context) {
    final rfcomm = context.watch<RfcommService>();
    final hud    = context.watch<HudController>();

    final isConnected  = rfcomm.isConnected;
    final isConnecting = rfcomm.state == RfcommState.connecting;

    if (!isConnected && !isConnecting) {
      return _DisconnectedView(rfcomm: rfcomm);
    }
    if (isConnecting && !isConnected) {
      return _ConnectingView(rfcomm: rfcomm);
    }

    final activeApp = hud.activeApp;

    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surface,
      body: Column(
        children: [
          // ── Persistent top status bar ──────────────────────────────
          _TopStatusBar(apps: apps),
          // ── Glasses live preview strip (only on launcher) ──────────
          if (activeApp == null) const _PreviewStrip(),
          // ── Main content: launcher or active app dashboard ─────────
          Expanded(
            child: activeApp != null
                ? _ActiveDashboard(app: activeApp, apps: apps)
                : _LauncherGrid(apps: apps),
          ),
        ],
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Top status bar — always visible
// ══════════════════════════════════════════════════════════════════════════════

class _TopStatusBar extends StatelessWidget {
  final List<GlassesApp> apps;
  const _TopStatusBar({required this.apps});

  @override
  Widget build(BuildContext context) {
    final rfcomm = context.watch<RfcommService>();
    final events = context.watch<GlassesEventService>();
    final menu   = context.watch<GlassesMenuController>();
    final hud    = context.watch<HudController>();
    final cs     = Theme.of(context).colorScheme;

    final isConnected = rfcomm.isConnected;
    final statusColor = isConnected
        ? const Color(0xFF00E676)
        : const Color(0xFFFF9800);

    return Container(
      color: cs.surface,
      padding: EdgeInsets.only(
        top: MediaQuery.of(context).padding.top + 4,
        left: 16,
        right: 8,
        bottom: 6,
      ),
      child: Row(
        children: [
          // App name + connection indicator
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'SOLOS HUD',
                style: TextStyle(
                  fontSize: 11,
                  fontWeight: FontWeight.w800,
                  letterSpacing: 2,
                  color: cs.onSurface,
                ),
              ),
              const SizedBox(height: 1),
              Row(
                children: [
                  AnimatedContainer(
                    duration: const Duration(milliseconds: 400),
                    width: 5,
                    height: 5,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: statusColor,
                      boxShadow: isConnected
                          ? [BoxShadow(color: statusColor.withValues(alpha: 0.6), blurRadius: 5)]
                          : null,
                    ),
                  ),
                  const SizedBox(width: 5),
                  Text(
                    isConnected
                        ? (rfcomm.device?.name ?? 'Connected')
                        : 'Connecting…',
                    style: TextStyle(
                      fontSize: 10,
                      color: statusColor,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
            ],
          ),
          const Spacer(),
          // Active app pill
          if (hud.activeApp != null) ...[
            _ActiveAppPill(app: hud.activeApp!),
            const SizedBox(width: 6),
          ],
          // Menu chip
          if (menu.isOpen) ...[
            _StatusChip(
              icon: Icons.grid_view_rounded,
              label: 'MENU',
              color: Colors.cyanAccent,
            ),
            const SizedBox(width: 4),
          ],
          // Battery
          if (isConnected && events.batteryLevel != null) ...[
            _BatteryChip(
              level: events.batteryLevel!,
              charging: events.isCharging ?? false,
            ),
            const SizedBox(width: 2),
          ],
          // Bluetooth icon
          IconButton(
            icon: Icon(
              isConnected ? Icons.bluetooth_connected : Icons.bluetooth_searching,
              size: 20,
            ),
            color: isConnected ? cs.primary : null,
            visualDensity: VisualDensity.compact,
            tooltip: 'Disconnect',
            onPressed: () => rfcomm.disconnect(),
          ),
        ],
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Glasses preview strip — compact live frame
// ══════════════════════════════════════════════════════════════════════════════

class _PreviewStrip extends StatelessWidget {
  const _PreviewStrip();

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Container(
      color: cs.surface,
      padding: const EdgeInsets.fromLTRB(12, 0, 12, 8),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(10),
        child: const GlassesPreviewWidget(),
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Active dashboard — full-screen app controls
// ══════════════════════════════════════════════════════════════════════════════

class _ActiveDashboard extends StatelessWidget {
  final GlassesApp app;
  final List<GlassesApp> apps;
  const _ActiveDashboard({required this.app, required this.apps});

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        // App phone widget — scrollable
        SingleChildScrollView(
          padding: const EdgeInsets.fromLTRB(12, 4, 12, 90),
          child: app.buildPhoneWidget(context),
        ),
        // Floating switch-app button at bottom
        Positioned(
          bottom: 16,
          left: 16,
          right: 16,
          child: _SwitchAppBar(apps: apps, activeApp: app),
        ),
      ],
    );
  }
}

class _SwitchAppBar extends StatelessWidget {
  final List<GlassesApp> apps;
  final GlassesApp activeApp;
  const _SwitchAppBar({required this.apps, required this.activeApp});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: cs.surfaceContainerHighest.withValues(alpha: 0.95),
        border: Border.all(color: cs.outlineVariant.withValues(alpha: 0.4)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.3),
            blurRadius: 16,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(16),
          onTap: () => _showLauncher(context),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            child: Row(
              children: [
                Icon(activeApp.icon, size: 16, color: cs.primary),
                const SizedBox(width: 8),
                Text(
                  activeApp.name,
                  style: TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w700,
                    color: cs.onSurface,
                  ),
                ),
                const SizedBox(width: 6),
                Container(
                  width: 6,
                  height: 6,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: cs.primary,
                    boxShadow: [
                      BoxShadow(color: cs.primary.withValues(alpha: 0.6), blurRadius: 4)
                    ],
                  ),
                ),
                const Spacer(),
                Text(
                  'SWITCH APP',
                  style: TextStyle(
                    fontSize: 10,
                    fontWeight: FontWeight.w700,
                    letterSpacing: 1,
                    color: cs.onSurfaceVariant,
                  ),
                ),
                const SizedBox(width: 4),
                Icon(Icons.grid_view_rounded, size: 14, color: cs.onSurfaceVariant),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _showLauncher(BuildContext context) {
    final hud = context.read<HudController>();
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      useSafeArea: true,
      backgroundColor: Colors.transparent,
      builder: (_) => _LauncherSheet(apps: apps, activeAppId: activeApp.id, hud: hud),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Launcher grid — no active app
// ══════════════════════════════════════════════════════════════════════════════

class _LauncherGrid extends StatelessWidget {
  final List<GlassesApp> apps;
  const _LauncherGrid({required this.apps});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // Notifications
        const Padding(
          padding: EdgeInsets.fromLTRB(12, 4, 12, 0),
          child: NotificationsWidget(),
        ),
        // Header
        Padding(
          padding: const EdgeInsets.fromLTRB(16, 14, 16, 8),
          child: Text(
            'APPS',
            style: TextStyle(
              fontSize: 10,
              fontWeight: FontWeight.w800,
              letterSpacing: 2,
              color: cs.onSurfaceVariant.withValues(alpha: 0.6),
            ),
          ),
        ),
        // 2-column grid
        Expanded(
          child: GridView.builder(
            padding: const EdgeInsets.fromLTRB(12, 0, 12, 24),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              crossAxisSpacing: 8,
              mainAxisSpacing: 8,
              childAspectRatio: 1.35,
            ),
            itemCount: apps.length,
            itemBuilder: (ctx, i) => _AppTile(app: apps[i]),
          ),
        ),
      ],
    );
  }
}

class _AppTile extends StatelessWidget {
  final GlassesApp app;
  const _AppTile({required this.app});

  @override
  Widget build(BuildContext context) {
    final hud = context.watch<HudController>();
    final isActive = hud.activeApp?.id == app.id;
    final cs = Theme.of(context).colorScheme;

    return AnimatedContainer(
      duration: const Duration(milliseconds: 200),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        color: isActive
            ? cs.primaryContainer.withValues(alpha: 0.25)
            : cs.surfaceContainerLow,
        border: Border.all(
          color: isActive
              ? cs.primary.withValues(alpha: 0.6)
              : cs.outlineVariant.withValues(alpha: 0.3),
          width: isActive ? 1.5 : 1,
        ),
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(15),
          onTap: () {
            if (isActive) {
              hud.setActiveApp(null);
              hud.stopLoop();
            } else {
              hud.setActiveApp(app);
              if (!hud.running) hud.startLoop();
            }
          },
          child: Padding(
            padding: const EdgeInsets.fromLTRB(14, 14, 10, 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Container(
                      width: 36,
                      height: 36,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: isActive
                            ? cs.primaryContainer
                            : cs.surfaceContainerHighest,
                      ),
                      child: Icon(
                        app.icon,
                        size: 18,
                        color: isActive ? cs.onPrimaryContainer : cs.onSurfaceVariant,
                      ),
                    ),
                    const Spacer(),
                    if (isActive)
                      Container(
                        width: 8,
                        height: 8,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: cs.primary,
                          boxShadow: [
                            BoxShadow(
                              color: cs.primary.withValues(alpha: 0.6),
                              blurRadius: 6,
                            )
                          ],
                        ),
                      ),
                  ],
                ),
                const Spacer(),
                Text(
                  app.name,
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w700,
                    color: isActive ? cs.primary : cs.onSurface,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 2),
                DefaultTextStyle(
                  style: TextStyle(
                    fontSize: 10,
                    color: cs.onSurfaceVariant.withValues(alpha: 0.6),
                  ),
                  child: app.buildPreviewWidget(context),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Launcher modal bottom sheet
// ══════════════════════════════════════════════════════════════════════════════

class _LauncherSheet extends StatelessWidget {
  final List<GlassesApp> apps;
  final String activeAppId;
  final HudController hud;
  const _LauncherSheet({required this.apps, required this.activeAppId, required this.hud});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    return Container(
      decoration: BoxDecoration(
        color: cs.surface,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Drag handle
          Padding(
            padding: const EdgeInsets.only(top: 10, bottom: 6),
            child: Container(
              width: 36, height: 4,
              decoration: BoxDecoration(
                color: cs.onSurfaceVariant.withValues(alpha: 0.3),
                borderRadius: BorderRadius.circular(2),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 6, 16, 12),
            child: Row(
              children: [
                Text('SWITCH APP',
                    style: TextStyle(
                      fontSize: 11, fontWeight: FontWeight.w800,
                      letterSpacing: 2,
                      color: cs.onSurfaceVariant.withValues(alpha: 0.7),
                    )),
              ],
            ),
          ),
          Flexible(
            child: GridView.builder(
              shrinkWrap: true,
              padding: const EdgeInsets.fromLTRB(12, 0, 12, 24),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                crossAxisSpacing: 8,
                mainAxisSpacing: 8,
                childAspectRatio: 1.0,
              ),
              itemCount: apps.length,
              itemBuilder: (ctx, i) {
                final app = apps[i];
                final isActive = app.id == activeAppId;
                return _SheetAppTile(app: app, isActive: isActive, hud: hud);
              },
            ),
          ),
        ],
      ),
    );
  }
}

class _SheetAppTile extends StatelessWidget {
  final GlassesApp app;
  final bool isActive;
  final HudController hud;
  const _SheetAppTile({required this.app, required this.isActive, required this.hud});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    return GestureDetector(
      onTap: () {
        Navigator.pop(context);
        if (!isActive) {
          hud.setActiveApp(app);
          if (!hud.running) hud.startLoop();
        }
      },
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(14),
          color: isActive
              ? cs.primaryContainer.withValues(alpha: 0.2)
              : cs.surfaceContainerLow,
          border: Border.all(
            color: isActive
                ? cs.primary.withValues(alpha: 0.5)
                : cs.outlineVariant.withValues(alpha: 0.3),
            width: isActive ? 1.5 : 1,
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 40, height: 40,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: isActive
                    ? cs.primaryContainer
                    : cs.surfaceContainerHighest,
              ),
              child: Icon(app.icon, size: 20,
                  color: isActive ? cs.onPrimaryContainer : cs.onSurfaceVariant),
            ),
            const SizedBox(height: 7),
            Text(
              app.name,
              style: TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.w700,
                color: isActive ? cs.primary : cs.onSurface,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ),
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Disconnected / Connecting full-screen views
// ══════════════════════════════════════════════════════════════════════════════

class _DisconnectedView extends StatelessWidget {
  final RfcommService rfcomm;
  const _DisconnectedView({required this.rfcomm});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    return Scaffold(
      backgroundColor: cs.surface,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            children: [
              // App name top
              Align(
                alignment: Alignment.centerLeft,
                child: Text('SOLOS HUD',
                    style: TextStyle(
                      fontSize: 11, fontWeight: FontWeight.w800,
                      letterSpacing: 2, color: cs.onSurfaceVariant,
                    )),
              ),
              const Spacer(),
              // Big icon
              Container(
                width: 96, height: 96,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: cs.primaryContainer.withValues(alpha: 0.3),
                  border: Border.all(
                    color: cs.primary.withValues(alpha: 0.3), width: 2),
                ),
                child: Icon(Icons.bluetooth_rounded,
                    size: 42, color: cs.primary.withValues(alpha: 0.7)),
              ),
              const SizedBox(height: 24),
              Text('No Glasses Connected',
                  style: TextStyle(
                    fontSize: 22, fontWeight: FontWeight.w800,
                    color: cs.onSurface,
                  )),
              const SizedBox(height: 8),
              Text(
                'Pair your Solos glasses in Android\nBluetooth settings, then tap connect.',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 14, color: cs.onSurfaceVariant, height: 1.5,
                ),
              ),
              const SizedBox(height: 32),
              SizedBox(
                width: double.infinity,
                height: 52,
                child: FilledButton.icon(
                  icon: const Icon(Icons.bluetooth_searching_rounded, size: 20),
                  label: const Text('Connect to Glasses',
                      style: TextStyle(fontSize: 15, fontWeight: FontWeight.w700)),
                  style: FilledButton.styleFrom(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(14)),
                  ),
                  onPressed: () => showModalBottomSheet(
                    context: context,
                    isScrollControlled: true,
                    useSafeArea: true,
                    builder: (_) => ScanSheet(rfcomm: rfcomm),
                  ),
                ),
              ),
              const SizedBox(height: 12),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: OutlinedButton.icon(
                  icon: const Icon(Icons.replay_rounded, size: 18),
                  label: const Text('Try Auto-Connect'),
                  style: OutlinedButton.styleFrom(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(14)),
                  ),
                  onPressed: () => rfcomm.autoConnectSolos(),
                ),
              ),
              const Spacer(),
            ],
          ),
        ),
      ),
    );
  }
}

class _ConnectingView extends StatelessWidget {
  final RfcommService rfcomm;
  const _ConnectingView({required this.rfcomm});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;

    return Scaffold(
      backgroundColor: cs.surface,
      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              SizedBox(
                width: 64, height: 64,
                child: CircularProgressIndicator(
                  strokeWidth: 3, color: cs.primary,
                ),
              ),
              const SizedBox(height: 20),
              Text('Connecting…',
                  style: TextStyle(
                    fontSize: 18, fontWeight: FontWeight.w700,
                    color: cs.onSurface,
                  )),
              const SizedBox(height: 8),
              Text('Reaching out to your glasses',
                  style: TextStyle(fontSize: 13, color: cs.onSurfaceVariant)),
              const SizedBox(height: 32),
              TextButton(
                onPressed: () => rfcomm.disconnect(),
                child: const Text('Cancel'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ══════════════════════════════════════════════════════════════════════════════
// Small reusable chips / pills
// ══════════════════════════════════════════════════════════════════════════════

class _ActiveAppPill extends StatelessWidget {
  final GlassesApp app;
  const _ActiveAppPill({required this.app});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        color: cs.primary.withValues(alpha: 0.12),
        border: Border.all(color: cs.primary.withValues(alpha: 0.3)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            width: 5, height: 5,
            decoration: BoxDecoration(
              shape: BoxShape.circle, color: cs.primary,
              boxShadow: [BoxShadow(color: cs.primary.withValues(alpha: 0.6), blurRadius: 4)],
            ),
          ),
          const SizedBox(width: 5),
          Text(app.name,
              style: TextStyle(
                fontSize: 10, fontWeight: FontWeight.w700,
                color: cs.primary, letterSpacing: 0.3,
              )),
        ],
      ),
    );
  }
}

class _StatusChip extends StatelessWidget {
  final IconData icon;
  final String label;
  final Color color;
  const _StatusChip({required this.icon, required this.label, required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        color: color.withValues(alpha: 0.08),
        border: Border.all(color: color.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 10, color: color),
          const SizedBox(width: 3),
          Text(label,
              style: TextStyle(
                fontSize: 9, fontWeight: FontWeight.w800,
                color: color, letterSpacing: 0.5,
              )),
        ],
      ),
    );
  }
}

class _BatteryChip extends StatelessWidget {
  final int level;
  final bool charging;
  const _BatteryChip({required this.level, required this.charging});

  @override
  Widget build(BuildContext context) {
    final color = charging
        ? Colors.greenAccent.shade400
        : level > 20 ? Colors.green
        : level > 10 ? Colors.orange
        : Colors.red;
    final icon = charging ? Icons.battery_charging_full
        : level > 60 ? Icons.battery_full
        : level > 20 ? Icons.battery_4_bar
        : Icons.battery_1_bar;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        color: color.withValues(alpha: 0.08),
        border: Border.all(color: color.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 11, color: color),
          const SizedBox(width: 2),
          Text('$level%',
              style: TextStyle(
                fontSize: 10, fontWeight: FontWeight.w700,
                color: color, fontFamily: 'monospace',
              )),
        ],
      ),
    );
  }
}
