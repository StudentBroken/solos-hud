import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../core/settings/app_settings.dart';
import '../core/bluetooth/ble_logger.dart';
import '../core/rfcomm/rfcomm_service.dart';
import '../core/rfcomm/glasses_event_service.dart';
import '../core/glasses_app.dart';
import '../core/hud_controller.dart';
import '../core/solos_protocol.dart';
import '../core/tilt_wake_service.dart';
import '../core/notifications/notification_overlay_controller.dart';
import '../widgets/log_viewer.dart';
import 'scan_sheet.dart';
import 'package:share_plus/share_plus.dart';
import 'package:path_provider/path_provider.dart';
import 'dart:io';

class SettingsScreen extends StatelessWidget {
  final List<GlassesApp> apps;
  const SettingsScreen({super.key, required this.apps});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<AppSettings>();
    final hud      = context.watch<HudController>();
    final rfcomm   = context.watch<RfcommService>();
    final notif    = context.watch<NotificationOverlayController>();
    final appsWithSettings =
        apps.where((a) => a.settingEntries.isNotEmpty).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
        centerTitle: false,
      ),
      body: ListView(
        padding: const EdgeInsets.fromLTRB(0, 6, 0, 48),
        children: [
          // ── Connection ─────────────────────────────────────────────
          _SettingsCard(
            title: 'Connection',
            icon: Icons.bluetooth_rounded,
            children: [
              _ConnectionSection(rfcomm: rfcomm, settings: settings),
            ],
          ),

          // ── Display ────────────────────────────────────────────────
          _SettingsCard(
            title: 'Display',
            icon: Icons.tune_rounded,
            children: [
              const _BrightnessSection(),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _SliderTile(
                label: 'Refresh Rate',
                value: settings.refreshRateMs.toDouble().clamp(250, 2000),
                min: 250,
                max: 2000,
                divisions: 35,
                valueLabel: (v) =>
                    '${v.round()} ms · ${(1000 / v).toStringAsFixed(1)} Hz',
                onChanged: (v) => settings.setRefreshRateMs(v.round()),
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _PickerTile(
                label: 'Speed Unit',
                value: settings.speedUnit,
                options: const ['km/h', 'mph', 'm/s'],
                onChanged: settings.setSpeedUnit,
              ),
            ],
          ),

          // ── HUD ────────────────────────────────────────────────────
          _SettingsCard(
            title: 'HUD',
            icon: Icons.remove_red_eye_outlined,
            children: [
              _InfoTile(
                label: 'Active App',
                value: hud.activeApp?.name ?? 'None',
                valueColor: hud.activeApp != null
                    ? Theme.of(context).colorScheme.primary
                    : null,
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              SwitchListTile.adaptive(
                dense: true,
                contentPadding: const EdgeInsets.symmetric(horizontal: 14),
                title: const Text('Display Loop'),
                subtitle: Text(
                  hud.running ? 'Transmitting to glasses' : 'Paused',
                  style: const TextStyle(fontSize: 12),
                ),
                value: hud.running,
                onChanged: (v) => v ? hud.startLoop() : hud.stopLoop(),
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              ListTile(
                dense: true,
                contentPadding: const EdgeInsets.symmetric(horizontal: 14),
                leading: Icon(
                  Icons.wb_sunny_outlined,
                  size: 18,
                  color: rfcomm.isConnected
                      ? Theme.of(context).colorScheme.primary
                      : Theme.of(context).colorScheme.onSurfaceVariant,
                ),
                title: const Text('Wake Display'),
                subtitle: const Text('Send wake signal to glasses',
                    style: TextStyle(fontSize: 12)),
                enabled: rfcomm.isConnected,
                onTap: rfcomm.isConnected ? () => hud.sendWakeUp() : null,
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _InfoTile(
                label: 'Frames Sent',
                value: '${hud.tickCount}',
                mono: true,
              ),
            ],
          ),

          // ── Tilt Wake ──────────────────────────────────────────────
          _SettingsCard(
            title: 'Tilt Wake',
            icon: Icons.screen_rotation_outlined,
            children: [const _TiltWakeSection()],
          ),

          // ── Notifications ──────────────────────────────────────────
          _SettingsCard(
            title: 'Notifications',
            icon: Icons.notifications_outlined,
            children: [
              SwitchListTile.adaptive(
                dense: true,
                contentPadding: const EdgeInsets.symmetric(horizontal: 14),
                title: const Text('Enable Notifications'),
                subtitle: const Text(
                  'Show incoming notifications on glasses',
                  style: TextStyle(fontSize: 12),
                ),
                value: settings.notifEnabled,
                onChanged: settings.setNotifEnabled,
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _SliderTile(
                label: 'Call duration',
                value: settings.notifDurationCall.toDouble(),
                min: 10,
                max: 120,
                divisions: 22,
                valueLabel: (v) => '${v.round()} s',
                onChanged: (v) => settings.setNotifDurationCall(v.round()),
                enabled: settings.notifEnabled,
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _SliderTile(
                label: 'Message duration',
                value: settings.notifDurationMsg.toDouble(),
                min: 3,
                max: 60,
                divisions: 19,
                valueLabel: (v) => '${v.round()} s',
                onChanged: (v) => settings.setNotifDurationMsg(v.round()),
                enabled: settings.notifEnabled,
              ),
              const Divider(height: 1, indent: 14, endIndent: 14),
              _SliderTile(
                label: 'General duration',
                value: settings.notifDurationGen.toDouble(),
                min: 2,
                max: 30,
                divisions: 14,
                valueLabel: (v) => '${v.round()} s',
                onChanged: (v) => settings.setNotifDurationGen(v.round()),
                enabled: settings.notifEnabled,
              ),
              if (notif.isActive) ...[
                const Divider(height: 1, indent: 14, endIndent: 14),
                ListTile(
                  dense: true,
                  contentPadding: const EdgeInsets.symmetric(horizontal: 14),
                  leading: const Icon(Icons.close, size: 16, color: Colors.redAccent),
                  title: Text(
                    'Dismiss: ${notif.current?.appName ?? ""}  ${notif.current?.title ?? ""}',
                    style: const TextStyle(fontSize: 13),
                    overflow: TextOverflow.ellipsis,
                  ),
                  onTap: notif.dismiss,
                ),
              ],
            ],
          ),

          // ── Per-app settings ───────────────────────────────────────
          for (final app in appsWithSettings)
            _SettingsCard(
              title: app.name,
              icon: app.icon,
              children: app.settingEntries
                  .map((e) => _AppSettingTile(entry: e))
                  .toList(),
            ),

          // ── Developer (collapsed by default) ───────────────────────
          _DeveloperSection(apps: apps),
        ],
      ),
    );
  }
}

// ── Card wrapper ──────────────────────────────────────────────────────────────

class _SettingsCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final List<Widget> children;

  const _SettingsCard({
    required this.title,
    required this.icon,
    required this.children,
  });

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 0, 12, 10),
      child: Card(
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(14),
          side: BorderSide(color: cs.outlineVariant.withValues(alpha: 0.4)),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Card header
            Padding(
              padding: const EdgeInsets.fromLTRB(14, 11, 14, 9),
              child: Row(
                children: [
                  Icon(icon, size: 14, color: cs.primary),
                  const SizedBox(width: 6),
                  Text(
                    title.toUpperCase(),
                    style: TextStyle(
                      fontSize: 10,
                      fontWeight: FontWeight.w800,
                      color: cs.primary,
                      letterSpacing: 1.3,
                    ),
                  ),
                ],
              ),
            ),
            Divider(
                height: 1,
                color: cs.outlineVariant.withValues(alpha: 0.35)),
            ...children,
            const SizedBox(height: 4),
          ],
        ),
      ),
    );
  }
}

// ── Connection section ────────────────────────────────────────────────────────

class _ConnectionSection extends StatelessWidget {
  final RfcommService rfcomm;
  final AppSettings settings;

  const _ConnectionSection({required this.rfcomm, required this.settings});

  @override
  Widget build(BuildContext context) {
    final events = context.watch<GlassesEventService>();
    final cs = Theme.of(context).colorScheme;
    final isConnected = rfcomm.isConnected;
    final isConnecting = rfcomm.state == RfcommState.connecting;

    final statusColor = isConnected
        ? const Color(0xFF4CAF50)
        : isConnecting
            ? const Color(0xFFFF9800)
            : cs.onSurfaceVariant.withValues(alpha: 0.5);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // Status row
        Padding(
          padding: const EdgeInsets.fromLTRB(14, 12, 14, 10),
          child: Row(
            children: [
              AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                width: 9,
                height: 9,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: statusColor,
                  boxShadow: isConnected
                      ? [
                          BoxShadow(
                            color: statusColor.withValues(alpha: 0.45),
                            blurRadius: 6,
                          )
                        ]
                      : null,
                ),
              ),
              const SizedBox(width: 9),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      isConnected
                          ? (rfcomm.device?.name ?? 'Connected')
                          : isConnecting
                              ? 'Connecting…'
                              : 'Disconnected',
                      style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w600,
                        color: isConnected ? statusColor : cs.onSurface,
                      ),
                    ),
                    if (isConnected && rfcomm.device != null)
                      Text(
                        '${rfcomm.device!.address}  ·  '
                        '${SolosProtocol.displayWidth}×${SolosProtocol.displayHeight}',
                        style: TextStyle(
                          fontFamily: 'monospace',
                          fontSize: 10,
                          color: cs.onSurfaceVariant,
                        ),
                      ),
                  ],
                ),
              ),
              // Battery
              if (isConnected && events.batteryLevel != null) ...[
                _BatteryBadge(
                  level: events.batteryLevel!,
                  charging: events.isCharging ?? false,
                ),
                IconButton(
                  icon: const Icon(Icons.refresh_rounded, size: 14),
                  visualDensity: VisualDensity.compact,
                  color: cs.onSurfaceVariant,
                  onPressed: () => rfcomm.requestBattery(),
                ),
              ],
              // Copy address
              if (isConnected && rfcomm.device != null)
                IconButton(
                  icon: const Icon(Icons.copy_rounded, size: 14),
                  visualDensity: VisualDensity.compact,
                  color: cs.onSurfaceVariant,
                  tooltip: 'Copy address',
                  onPressed: () => Clipboard.setData(
                    ClipboardData(text: rfcomm.device!.address),
                  ),
                ),
            ],
          ),
        ),
        // Connect / Disconnect button
        Padding(
          padding: const EdgeInsets.fromLTRB(14, 0, 14, 10),
          child: isConnected
              ? OutlinedButton.icon(
                  icon: const Icon(Icons.bluetooth_disabled_rounded, size: 16),
                  label: const Text('Disconnect'),
                  style: OutlinedButton.styleFrom(
                    foregroundColor: Colors.redAccent,
                    side: const BorderSide(color: Colors.redAccent),
                    visualDensity: VisualDensity.compact,
                  ),
                  onPressed: () => rfcomm.disconnect(),
                )
              : FilledButton.icon(
                  icon: Icon(
                    isConnecting
                        ? Icons.bluetooth_searching
                        : Icons.bluetooth_searching,
                    size: 16,
                  ),
                  label: Text(isConnecting ? 'Connecting…' : 'Connect to Glasses'),
                  onPressed: isConnecting
                      ? null
                      : () => showModalBottomSheet(
                            context: context,
                            isScrollControlled: true,
                            useSafeArea: true,
                            builder: (_) => ScanSheet(rfcomm: rfcomm),
                          ),
                ),
        ),
        const Divider(height: 1, indent: 14, endIndent: 14),
        SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: const Text('Auto-connect'),
          subtitle: const Text('Connect to last device on launch',
              style: TextStyle(fontSize: 12)),
          value: settings.autoConnect,
          onChanged: settings.setAutoConnect,
        ),
        SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: const Text('Auto-start HUD'),
          subtitle: const Text('Restore last app on connect',
              style: TextStyle(fontSize: 12)),
          value: settings.autoStartHud,
          onChanged: settings.setAutoStartHud,
        ),
      ],
    );
  }
}

// ── Battery badge ─────────────────────────────────────────────────────────────

class _BatteryBadge extends StatelessWidget {
  final int level;
  final bool charging;
  const _BatteryBadge({required this.level, required this.charging});

  @override
  Widget build(BuildContext context) {
    final color = charging
        ? Colors.greenAccent.shade400
        : level > 20
            ? Colors.green
            : level > 10
                ? Colors.orange
                : Colors.red;
    return Text(
      '${charging ? '⚡ ' : ''}$level%',
      style: TextStyle(
        fontSize: 12,
        fontWeight: FontWeight.w600,
        color: color,
        fontFamily: 'monospace',
      ),
    );
  }
}

// ── Brightness section ────────────────────────────────────────────────────────

class _BrightnessSection extends StatelessWidget {
  const _BrightnessSection();

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<AppSettings>();
    final rfcomm = context.watch<RfcommService>();
    final pct = (settings.brightness / 255 * 100).round();
    final cs = Theme.of(context).colorScheme;

    return Column(
      children: [
        SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: const Text('Auto Brightness'),
          subtitle: const Text('Glasses adjust to ambient light',
              style: TextStyle(fontSize: 12)),
          value: settings.autoBrightness,
          onChanged: (v) {
            settings.setAutoBrightness(v);
            if (!v) rfcomm.setBrightness(settings.brightness);
          },
        ),
        AnimatedOpacity(
          opacity: settings.autoBrightness ? 0.35 : 1.0,
          duration: const Duration(milliseconds: 200),
          child: _SliderTile(
            label: 'Manual Brightness',
            value: settings.brightness.toDouble(),
            min: 0,
            max: 255,
            divisions: 51,
            valueLabel: (_) => '$pct%',
            enabled: !settings.autoBrightness,
            leading: Icon(Icons.brightness_low_rounded,
                size: 14, color: cs.onSurfaceVariant),
            trailing:
                Icon(Icons.brightness_high_rounded, size: 14, color: cs.onSurfaceVariant),
            onChanged: settings.autoBrightness
                ? null
                : (v) {
                    final val = v.round();
                    settings.setBrightness(val);
                    rfcomm.setBrightness(val);
                  },
          ),
        ),
      ],
    );
  }
}

// ── Tilt Wake section ─────────────────────────────────────────────────────────

class _TiltWakeSection extends StatelessWidget {
  const _TiltWakeSection();

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<AppSettings>();
    final tilt = context.watch<TiltWakeService>();
    final rfcomm = context.watch<RfcommService>();
    final cs = Theme.of(context).colorScheme;

    return Column(
      children: [
        SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: const Text('Enable Tilt Wake'),
          subtitle: const Text(
            'Screen stays off until you tilt your head up',
            style: TextStyle(fontSize: 12),
          ),
          value: settings.tiltWakeEnabled,
          onChanged: rfcomm.isConnected
              ? (v) async {
                  await settings.setTiltWakeEnabled(v);
                  if (v) {
                    tilt.start();
                  } else {
                    tilt.stop();
                  }
                }
              : null,
        ),
        const Divider(height: 1, indent: 14, endIndent: 14),
        _SliderTile(
          label: 'Wake Threshold',
          value: settings.tiltThresholdDeg.toDouble(),
          min: 5,
          max: 60,
          divisions: 55,
          valueLabel: (v) => '${v.round()}°',
          onChanged: (v) => settings.setTiltThresholdDeg(v.round()),
        ),
        if (settings.tiltWakeEnabled) ...[
          const Divider(height: 1, indent: 14, endIndent: 14),
          Padding(
            padding: const EdgeInsets.fromLTRB(14, 8, 14, 8),
            child: Row(
              children: [
                AnimatedContainer(
                  duration: const Duration(milliseconds: 300),
                  width: 8,
                  height: 8,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: tilt.screenOn ? Colors.greenAccent : cs.onSurfaceVariant.withValues(alpha: 0.4),
                    boxShadow: tilt.screenOn
                        ? [BoxShadow(color: Colors.greenAccent.withValues(alpha: 0.5), blurRadius: 6)]
                        : null,
                  ),
                ),
                const SizedBox(width: 8),
                Text(
                  tilt.screenOn ? 'Display ON' : 'Display OFF',
                  style: TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w500,
                    color: tilt.screenOn ? Colors.greenAccent : cs.onSurfaceVariant,
                  ),
                ),
                const Spacer(),
                Text(
                  tilt.pitchDeg != null
                      ? 'Pitch ${tilt.pitchDeg!.toStringAsFixed(1)}°'
                      : 'Pitch —',
                  style: TextStyle(
                    fontFamily: 'monospace',
                    fontSize: 11,
                    color: cs.onSurfaceVariant,
                  ),
                ),
              ],
            ),
          ),
        ],
      ],
    );
  }
}

// ── Developer section (collapsible) ──────────────────────────────────────────

class _DeveloperSection extends StatelessWidget {
  final List<GlassesApp> apps;
  const _DeveloperSection({required this.apps});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 0, 12, 10),
      child: Card(
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(14),
          side: BorderSide(color: cs.outlineVariant.withValues(alpha: 0.35)),
        ),
        child: Theme(
          data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
          child: ExpansionTile(
            tilePadding: const EdgeInsets.fromLTRB(14, 2, 14, 2),
            shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(14)),
            ),
            collapsedShape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(14)),
            ),
            leading: Icon(Icons.code_rounded,
                size: 16, color: cs.onSurfaceVariant),
            title: Text(
              'DEVELOPER',
              style: TextStyle(
                fontSize: 10,
                fontWeight: FontWeight.w800,
                letterSpacing: 1.3,
                color: cs.onSurfaceVariant,
              ),
            ),
            children: [
              Divider(height: 1, color: cs.outlineVariant.withValues(alpha: 0.3)),
              // Glasses events
              Padding(
                padding: const EdgeInsets.fromLTRB(14, 10, 14, 4),
                child: _MiniHeader(label: 'Glasses Events'),
              ),
              const Padding(
                padding: EdgeInsets.symmetric(horizontal: 14),
                child: _GlassesEventViewer(),
              ),
              const SizedBox(height: 12),
              // BLE log
              Padding(
                padding: const EdgeInsets.fromLTRB(14, 0, 14, 4),
                child: _MiniHeader(label: 'Bluetooth Log'),
              ),
              const _BleLogSection(),
              const SizedBox(height: 10),
            ],
          ),
        ),
      ),
    );
  }
}

// ── BLE log section ───────────────────────────────────────────────────────────

class _BleLogSection extends StatelessWidget {
  const _BleLogSection();

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<AppSettings>();

    return Column(
      children: [
        SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: const Text('Enable Logging'),
          value: settings.logBle,
          onChanged: (v) {
            settings.setLogBle(v);
            context.read<BleLogger>().enabled = v;
          },
        ),
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 14),
          child: LogViewer(),
        ),
        Padding(
          padding: const EdgeInsets.fromLTRB(14, 6, 14, 0),
          child: Align(
            alignment: Alignment.centerRight,
            child: TextButton.icon(
              onPressed: () async {
                final logger = context.read<BleLogger>();
                if (logger.entries.isEmpty) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Log is empty')),
                  );
                  return;
                }
                final logData = logger.exportLogs();
                final tempDir = await getTemporaryDirectory();
                final file = File('${tempDir.path}/ble_log.txt');
                await file.writeAsString(logData);
                await SharePlus.instance.share(
                  ShareParams(
                    files: [XFile(file.path)],
                    subject: 'Solos HUD Bluetooth Log',
                  ),
                );
              },
              icon: const Icon(Icons.upload_rounded, size: 15),
              label: const Text('Export Log'),
              style: TextButton.styleFrom(
                visualDensity: VisualDensity.compact,
              ),
            ),
          ),
        ),
      ],
    );
  }
}

// ── Glasses event viewer ──────────────────────────────────────────────────────

class _GlassesEventViewer extends StatefulWidget {
  const _GlassesEventViewer();

  @override
  State<_GlassesEventViewer> createState() => _GlassesEventViewerState();
}

class _GlassesEventViewerState extends State<_GlassesEventViewer> {
  final _scroll = ScrollController();

  @override
  void dispose() {
    _scroll.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final events = context.watch<GlassesEventService>();
    final log = events.log;
    final cs = Theme.of(context).colorScheme;

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_scroll.hasClients && log.isNotEmpty) {
        _scroll.animateTo(
          _scroll.position.maxScrollExtent,
          duration: const Duration(milliseconds: 150),
          curve: Curves.easeOut,
        );
      }
    });

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        if (events.lastEvent != null && events.lastEvent!.isButtonPress)
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 7),
            margin: const EdgeInsets.only(bottom: 8),
            decoration: BoxDecoration(
              color: cs.primary.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(8),
              border: Border.all(color: cs.primary.withValues(alpha: 0.3)),
            ),
            child: Row(
              children: [
                Icon(Icons.touch_app_rounded, size: 16, color: cs.primary),
                const SizedBox(width: 8),
                Text(
                  events.lastEvent!.description,
                  style: TextStyle(
                    fontWeight: FontWeight.w600,
                    fontSize: 13,
                    color: cs.primary,
                  ),
                ),
              ],
            ),
          ),
        Row(
          children: [
            Text(
              '${log.length} events',
              style: TextStyle(fontSize: 11, color: cs.onSurfaceVariant),
            ),
            const Spacer(),
            TextButton.icon(
              onPressed: () => context.read<GlassesEventService>().clearLog(),
              icon: const Icon(Icons.delete_outline_rounded, size: 14),
              label: const Text('Clear'),
              style: TextButton.styleFrom(
                visualDensity: VisualDensity.compact,
                textStyle: const TextStyle(fontSize: 12),
              ),
            ),
          ],
        ),
        Container(
          height: 160,
          decoration: BoxDecoration(
            color: const Color(0xFF080810),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: cs.outlineVariant.withValues(alpha: 0.3)),
          ),
          child: log.isEmpty
              ? Center(
                  child: Text(
                    'No events yet — press a button on the glasses',
                    style: TextStyle(color: cs.onSurfaceVariant, fontSize: 11),
                    textAlign: TextAlign.center,
                  ),
                )
              : ListView.builder(
                  controller: _scroll,
                  padding: const EdgeInsets.all(8),
                  itemCount: log.length,
                  itemBuilder: (_, i) {
                    final e = log[i];
                    final color = e.isButtonPress
                        ? Colors.cyanAccent
                        : e.packetType == 34
                            ? cs.onSurfaceVariant.withValues(alpha: 0.5)
                            : Colors.greenAccent;
                    return Text(
                      e.toString(),
                      style: TextStyle(
                        color: color,
                        fontSize: 10,
                        fontFamily: 'monospace',
                      ),
                    );
                  },
                ),
        ),
      ],
    );
  }
}

// ── Per-app setting tile ──────────────────────────────────────────────────────

class _AppSettingTile extends StatelessWidget {
  final AppSettingEntry entry;
  const _AppSettingTile({required this.entry});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<AppSettings>();
    final value = settings.getAppValue(entry.key, entry.defaultValue);

    switch (entry.type) {
      case AppSettingType.toggle:
        return SwitchListTile.adaptive(
          dense: true,
          contentPadding: const EdgeInsets.symmetric(horizontal: 14),
          title: Text(entry.label),
          subtitle: entry.description != null
              ? Text(entry.description!, style: const TextStyle(fontSize: 12))
              : null,
          value: value as bool,
          onChanged: (v) => settings.setAppValue(entry.key, v),
        );
      case AppSettingType.slider:
        return _SliderTile(
          label: entry.label,
          value: (value as num).toDouble(),
          min: 0,
          max: 100,
          divisions: 20,
          valueLabel: (v) => v.round().toString(),
          onChanged: (v) => settings.setAppValue(entry.key, v),
        );
      case AppSettingType.text:
        return _InfoTile(
          label: entry.label,
          value: value.toString(),
          mono: true,
        );
      case AppSettingType.dropdown:
        return _InfoTile(
          label: entry.label,
          value: value.toString(),
        );
    }
  }
}

// ── Reusable compact widgets ──────────────────────────────────────────────────

class _SliderTile extends StatelessWidget {
  final String label;
  final double value;
  final double min;
  final double max;
  final int divisions;
  final String Function(double) valueLabel;
  final ValueChanged<double>? onChanged;
  final bool enabled;
  final Widget? leading;
  final Widget? trailing;

  const _SliderTile({
    required this.label,
    required this.value,
    required this.min,
    required this.max,
    required this.divisions,
    required this.valueLabel,
    required this.onChanged,
    this.enabled = true,
    this.leading,
    this.trailing,
  });

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.fromLTRB(14, 8, 14, 2),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Text(label,
                  style: TextStyle(
                    fontSize: 14,
                    color: enabled ? cs.onSurface : cs.onSurface.withValues(alpha: 0.38),
                  )),
              const Spacer(),
              Text(
                valueLabel(value),
                style: TextStyle(
                  fontSize: 12,
                  color: cs.onSurfaceVariant,
                  fontFamily: 'monospace',
                ),
              ),
            ],
          ),
          Row(
            children: [
              if (leading != null) ...[leading!, const SizedBox(width: 4)],
              Expanded(
                child: Slider(
                  value: value.clamp(min, max),
                  min: min,
                  max: max,
                  divisions: divisions,
                  onChanged: enabled ? onChanged : null,
                ),
              ),
              if (trailing != null) ...[const SizedBox(width: 4), trailing!],
            ],
          ),
        ],
      ),
    );
  }
}

class _PickerTile extends StatelessWidget {
  final String label;
  final String value;
  final List<String> options;
  final ValueChanged<String> onChanged;

  const _PickerTile({
    required this.label,
    required this.value,
    required this.options,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 2),
      child: Row(
        children: [
          Expanded(
            child: Text(label,
                style: TextStyle(fontSize: 14, color: cs.onSurface)),
          ),
          DropdownButton<String>(
            value: value,
            isDense: true,
            underline: const SizedBox.shrink(),
            items: options
                .map((o) => DropdownMenuItem(value: o, child: Text(o)))
                .toList(),
            onChanged: (v) {
              if (v != null) onChanged(v);
            },
          ),
        ],
      ),
    );
  }
}

class _InfoTile extends StatelessWidget {
  final String label;
  final String value;
  final Color? valueColor;
  final bool mono;

  const _InfoTile({
    required this.label,
    required this.value,
    this.valueColor,
    this.mono = false,
  });

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
      child: Row(
        children: [
          Expanded(
            child: Text(label,
                style: TextStyle(fontSize: 14, color: cs.onSurface)),
          ),
          const SizedBox(width: 8),
          Flexible(
            child: Text(
              value,
              style: TextStyle(
                fontSize: 13,
                color: valueColor ?? cs.onSurfaceVariant,
                fontFamily: mono ? 'monospace' : null,
                fontWeight: FontWeight.w500,
              ),
              textAlign: TextAlign.end,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}

class _MiniHeader extends StatelessWidget {
  final String label;
  const _MiniHeader({required this.label});

  @override
  Widget build(BuildContext context) {
    return Text(
      label.toUpperCase(),
      style: TextStyle(
        fontSize: 10,
        fontWeight: FontWeight.w700,
        letterSpacing: 1.2,
        color: Theme.of(context).colorScheme.onSurfaceVariant,
      ),
    );
  }
}
