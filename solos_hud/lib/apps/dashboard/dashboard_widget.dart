import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/gps/gps_service.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/hud_controller.dart';
import 'dashboard_app.dart';

class DashboardPhoneWidget extends StatelessWidget {
  final DashboardApp app;
  const DashboardPhoneWidget({super.key, required this.app});

  @override
  Widget build(BuildContext context) {
    final gps = context.watch<GpsService>();
    final hr = context.watch<HeartRateService>();
    final events = context.watch<GlassesEventService>();
    final settings = context.watch<AppSettings>();
    final hud = context.watch<HudController>();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // ── HUD Preview ───────────────────────────────────────────
        _HudPreviewCard(gps: gps, hr: hr, events: events, settings: settings),
        const SizedBox(height: 16),

        // ── Live Metrics Grid ─────────────────────────────────────
        _MetricsGrid(gps: gps, hr: hr, events: events, settings: settings),
        const SizedBox(height: 16),

        // ── Primary Metric Selector ───────────────────────────────
        _PrimarySelector(settings: settings),
        const SizedBox(height: 12),

        // ── Metric Toggles ────────────────────────────────────────
        _ToggleRow(
          label: 'Time',
          settingKey: 'dash_show_time',
          settings: settings,
        ),
        _ToggleRow(
          label: 'Battery',
          settingKey: 'dash_show_battery',
          settings: settings,
        ),
        _ToggleRow(
          label: 'Speed',
          settingKey: 'dash_show_speed',
          settings: settings,
        ),
        _ToggleRow(
          label: 'Heart Rate',
          settingKey: 'dash_show_hr',
          settings: settings,
        ),
        _ToggleRow(
          label: 'Compass',
          settingKey: 'dash_show_compass',
          settings: settings,
        ),
        const SizedBox(height: 12),

        // ── HR Device ─────────────────────────────────────────────
        _HrDeviceRow(hr: hr),
        const SizedBox(height: 12),

        // ── Force frame ───────────────────────────────────────────
        OutlinedButton.icon(
          onPressed: hud.rfcomm.isConnected ? () => hud.sendNow() : null,
          icon: const Icon(Icons.refresh, size: 16),
          label: const Text('Send Frame Now'),
        ),
      ],
    );
  }
}

// ── Preview card ──────────────────────────────────────────────────────────────

class _HudPreviewCard extends StatelessWidget {
  final GpsService gps;
  final HeartRateService hr;
  final GlassesEventService events;
  final AppSettings settings;

  const _HudPreviewCard({
    required this.gps,
    required this.hr,
    required this.events,
    required this.settings,
  });

  @override
  Widget build(BuildContext context) {
    final now = DateTime.now();
    final speed = gps.available
        ? _fmtSpeed(gps.data.speedKmh, settings.speedUnit)
        : '--';
    final hrStr = hr.heartRate != null ? '♥ ${hr.heartRate} bpm' : '♥ -- bpm';
    final bat = events.batteryLevel != null
        ? '${events.batteryLevel}%${events.isCharging == true ? " ⚡" : ""}'
        : 'Bat: ?';
    final heading = gps.available && gps.data.heading != null
        ? '↑ ${_compass(gps.data.heading!)} ${gps.data.heading!.toStringAsFixed(0)}°'
        : '↑ --';
    final timeStr =
        '${now.hour.toString().padLeft(2, '0')}:${now.minute.toString().padLeft(2, '0')}:${now.second.toString().padLeft(2, '0')}';

    return Container(
      decoration: BoxDecoration(
        color: Colors.black,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: const Color(0xFF0066FF), width: 2),
      ),
      padding: const EdgeInsets.all(12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // Top bar
          Row(
            children: [
              Text(
                timeStr,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 16,
                  fontFamily: 'monospace',
                ),
              ),
              const Spacer(),
              Text(
                bat,
                style: TextStyle(
                  color: events.isCharging == true
                      ? Colors.greenAccent
                      : Colors.white,
                  fontSize: 14,
                ),
              ),
            ],
          ),
          Container(
            height: 1,
            color: const Color(0xFF0066FF),
            margin: const EdgeInsets.symmetric(vertical: 6),
          ),
          // Big speed
          Center(
            child: Text(
              speed,
              style: const TextStyle(
                color: Color(0xFF00CCFF),
                fontSize: 48,
                fontWeight: FontWeight.w900,
                fontFamily: 'monospace',
              ),
            ),
          ),
          Center(
            child: Text(
              settings.speedUnit,
              style: const TextStyle(color: Color(0xFF4477AA), fontSize: 14),
            ),
          ),
          Container(
            height: 1,
            color: const Color(0xFF0066FF),
            margin: const EdgeInsets.symmetric(vertical: 6),
          ),
          // Bottom row
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Text(
                hrStr,
                style: const TextStyle(
                  color: Colors.greenAccent,
                  fontSize: 14,
                  fontFamily: 'monospace',
                ),
              ),
              Text(
                heading,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 14,
                  fontFamily: 'monospace',
                ),
              ),
            ],
          ),
          const SizedBox(height: 4),
          Center(
            child: Text(
              'GLASSES HUD PREVIEW',
              style: TextStyle(
                color: Colors.grey.shade700,
                fontSize: 9,
                letterSpacing: 1.5,
              ),
            ),
          ),
        ],
      ),
    );
  }

  String _fmtSpeed(double kmh, String unit) {
    switch (unit) {
      case 'mph':
        return (kmh * 0.621371).toStringAsFixed(1);
      case 'm/s':
        return (kmh / 3.6).toStringAsFixed(1);
      default:
        return kmh.toStringAsFixed(1);
    }
  }

  String _compass(double deg) {
    const d = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
    return d[((deg + 22.5) / 45).floor() % 8];
  }
}

// ── Metrics grid ──────────────────────────────────────────────────────────────

class _MetricsGrid extends StatelessWidget {
  final GpsService gps;
  final HeartRateService hr;
  final GlassesEventService events;
  final AppSettings settings;
  const _MetricsGrid({
    required this.gps,
    required this.hr,
    required this.events,
    required this.settings,
  });

  @override
  Widget build(BuildContext context) {
    final speed = gps.available ? gps.data.speedKmh.toStringAsFixed(1) : '--';
    final bat = events.batteryLevel != null ? '${events.batteryLevel}%' : '--';
    final heading = gps.available && gps.data.heading != null
        ? '${gps.data.heading!.toStringAsFixed(0)}°'
        : '--';

    return GridView.count(
      crossAxisCount: 3,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      childAspectRatio: 1.6,
      children: [
        _MetricTile(
          label: 'Speed',
          value: speed,
          unit: settings.speedUnit,
          icon: Icons.speed,
        ),
        _MetricTile(
          label: 'Heart Rate',
          value: hr.heartRate?.toString() ?? '--',
          unit: 'bpm',
          icon: Icons.favorite,
          valueColor: hr.heartRate != null
              ? (hr.heartRate! > 160 ? Colors.red : Colors.greenAccent)
              : null,
        ),
        _MetricTile(
          label: 'Battery',
          value: bat,
          unit: events.isCharging == true ? '⚡' : '',
          icon: Icons.battery_full,
        ),
        _MetricTile(
          label: 'Heading',
          value: heading,
          unit: '',
          icon: Icons.explore,
        ),
        _MetricTile(
          label: 'Accuracy',
          value: gps.available && gps.data.accuracy != null
              ? '±${gps.data.accuracy!.toStringAsFixed(0)}'
              : '--',
          unit: 'm',
          icon: Icons.gps_fixed,
        ),
        _MetricTile(
          label: 'HR Contact',
          value: hr.sensorContact == null
              ? '--'
              : hr.sensorContact!
              ? 'Yes'
              : 'No',
          unit: '',
          icon: Icons.sensors,
        ),
      ],
    );
  }
}

class _MetricTile extends StatelessWidget {
  final String label;
  final String value;
  final String unit;
  final IconData icon;
  final Color? valueColor;
  const _MetricTile({
    required this.label,
    required this.value,
    required this.unit,
    required this.icon,
    this.valueColor,
  });

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
            Row(
              children: [
                Icon(icon, size: 12, color: Colors.grey),
                const SizedBox(width: 4),
                Text(
                  label,
                  style: const TextStyle(fontSize: 10, color: Colors.grey),
                ),
              ],
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Flexible(
                  child: Text(
                    value,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontSize: 17,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'monospace',
                      color: valueColor,
                    ),
                  ),
                ),
                if (unit.isNotEmpty) ...[
                  const SizedBox(width: 2),
                  Text(
                    unit,
                    style: const TextStyle(fontSize: 10, color: Colors.grey),
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

// ── Controls ──────────────────────────────────────────────────────────────────

class _PrimarySelector extends StatelessWidget {
  final AppSettings settings;
  const _PrimarySelector({required this.settings});

  @override
  Widget build(BuildContext context) {
    final current = settings.getAppValue('dash_primary', 'speed') as String;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 4),
          child: Text(
            'Primary Metric',
            style: TextStyle(fontSize: 12, color: Colors.grey),
          ),
        ),
        const SizedBox(height: 6),
        SegmentedButton<String>(
          expandedInsets: EdgeInsets.zero,
          segments: const [
            ButtonSegment(
              value: 'speed',
              label: Text('Speed'),
              icon: Icon(Icons.speed, size: 14),
            ),
            ButtonSegment(
              value: 'hr',
              label: Text('HR'),
              icon: Icon(Icons.favorite, size: 14),
            ),
            ButtonSegment(
              value: 'time',
              label: Text('Time'),
              icon: Icon(Icons.access_time, size: 14),
            ),
          ],
          selected: {current},
          onSelectionChanged: (s) =>
              settings.setAppValue('dash_primary', s.first),
        ),
      ],
    );
  }
}

class _ToggleRow extends StatelessWidget {
  final String label;
  final String settingKey;
  final AppSettings settings;
  const _ToggleRow({
    required this.label,
    required this.settingKey,
    required this.settings,
  });

  @override
  Widget build(BuildContext context) {
    final v = settings.getAppValue(settingKey, true) as bool;
    return SwitchListTile.adaptive(
      dense: true,
      title: Text(label, style: const TextStyle(fontSize: 13)),
      value: v,
      onChanged: (val) => settings.setAppValue(settingKey, val),
    );
  }
}

class _HrDeviceRow extends StatelessWidget {
  final HeartRateService hr;
  const _HrDeviceRow({required this.hr});

  @override
  Widget build(BuildContext context) {
    final isConnecting = hr.state == HrConnectionState.connecting;
    return ListTile(
      dense: true,
      leading: isConnecting
          ? const SizedBox(
              width: 20,
              height: 20,
              child: CircularProgressIndicator(strokeWidth: 2),
            )
          : Icon(
              Icons.favorite,
              color: hr.isConnected ? Colors.redAccent : Colors.grey,
            ),
      title: Text(
        hr.isConnected
            ? hr.deviceName ?? 'Heart Rate Monitor'
            : isConnecting
            ? 'Connecting…'
            : 'Heart Rate Monitor',
      ),
      subtitle: Text(
        hr.isConnected
            ? '${hr.heartRate ?? "--"} bpm  •  tap to disconnect'
            : isConnecting
            ? 'Please wait'
            : hr.error != null
            ? hr.error!
            : 'Not connected',
      ),
      trailing: hr.isConnected
          ? IconButton(
              icon: const Icon(Icons.link_off, size: 20),
              tooltip: 'Disconnect',
              onPressed: () => hr.disconnect(),
            )
          : isConnecting
          ? null
          : FilledButton.icon(
              onPressed: () => _showHrScanner(context),
              icon: const Icon(Icons.bluetooth_searching, size: 16),
              label: const Text('Connect'),
            ),
    );
  }

  void _showHrScanner(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (_) => _HrScanSheet(hr: hr),
    );
  }
}

class _HrScanSheet extends StatefulWidget {
  final HeartRateService hr;
  const _HrScanSheet({required this.hr});

  @override
  State<_HrScanSheet> createState() => _HrScanSheetState();
}

class _HrScanSheetState extends State<_HrScanSheet> {
  @override
  void initState() {
    super.initState();
    widget.hr.addListener(_onChanged);
    widget.hr.startScan();
  }

  @override
  void dispose() {
    widget.hr.removeListener(_onChanged);
    widget.hr.stopScan();
    super.dispose();
  }

  void _onChanged() {
    if (mounted) setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    final hr = widget.hr;
    final connected = hr.connectedSystemDevices;
    final scanning = hr.state == HrConnectionState.scanning;

    return DraggableScrollableSheet(
      expand: false,
      initialChildSize: 0.65,
      builder: (context, ctrl) => Column(
        children: [
          // ── Header ──────────────────────────────────────────────
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 8, 0),
            child: Row(
              children: [
                const Expanded(
                  child: Text(
                    'Connect Heart Rate Monitor',
                    style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
                  ),
                ),
                if (scanning)
                  const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 12),
                    child: SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    ),
                  )
                else
                  IconButton(
                    icon: const Icon(Icons.refresh),
                    tooltip: 'Scan again',
                    onPressed: hr.startScan,
                  ),
              ],
            ),
          ),
          const Padding(
            padding: EdgeInsets.fromLTRB(16, 4, 16, 8),
            child: Text(
              'Amazefit: Settings → Heart Rate → Enable HR push, or start a workout.',
              style: TextStyle(fontSize: 11, color: Colors.grey),
            ),
          ),
          const Divider(height: 1),

          // ── Scrollable list ──────────────────────────────────────
          Expanded(
            child: ListView(
              controller: ctrl,
              children: [
                // Already-connected devices (paired at OS level)
                if (connected.isNotEmpty) ...[
                  const _SectionHeader(label: 'Already Connected'),
                  for (final dev in connected)
                    _DeviceTile(
                      name: dev.platformName.isNotEmpty
                          ? dev.platformName
                          : 'Device (${dev.remoteId})',
                      subtitle: 'Connected — tap to use',
                      icon: Icons.bluetooth_connected,
                      iconColor: Colors.blueAccent,
                      rssi: null,
                      onTap: () {
                        Navigator.pop(context);
                        hr.connect(dev);
                      },
                    ),
                  const Divider(),
                ],

                // Scan results
                const _SectionHeader(label: 'Nearby Devices'),
                if (hr.scanResults.isEmpty)
                  Padding(
                    padding: const EdgeInsets.symmetric(vertical: 32),
                    child: Center(
                      child: scanning
                          ? const Column(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                CircularProgressIndicator(),
                                SizedBox(height: 12),
                                Text('Scanning for HR devices…'),
                              ],
                            )
                          : const Text(
                              'No HR devices found.\nMake sure your device is in HR push mode.',
                              textAlign: TextAlign.center,
                              style: TextStyle(color: Colors.grey),
                            ),
                    ),
                  )
                else
                  for (final r in hr.scanResults)
                    _DeviceTile(
                      name: r.device.platformName.isNotEmpty
                          ? r.device.platformName
                          : 'Unknown (${r.device.remoteId})',
                      subtitle: '${r.rssi} dBm',
                      icon: Icons.favorite,
                      iconColor: Colors.redAccent,
                      rssi: r.rssi,
                      onTap: () {
                        Navigator.pop(context);
                        hr.connect(r.device);
                      },
                    ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _SectionHeader extends StatelessWidget {
  final String label;
  const _SectionHeader({required this.label});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
      child: Text(
        label.toUpperCase(),
        style: const TextStyle(
          fontSize: 11,
          color: Colors.grey,
          letterSpacing: 0.8,
        ),
      ),
    );
  }
}

class _DeviceTile extends StatelessWidget {
  final String name;
  final String subtitle;
  final IconData icon;
  final Color iconColor;
  final int? rssi;
  final VoidCallback onTap;

  const _DeviceTile({
    required this.name,
    required this.subtitle,
    required this.icon,
    required this.iconColor,
    required this.rssi,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon, color: iconColor),
      title: Text(name),
      subtitle: Text(subtitle),
      trailing: rssi != null
          ? _SignalBars(rssi: rssi!)
          : const Icon(Icons.chevron_right),
      onTap: onTap,
    );
  }
}

class _SignalBars extends StatelessWidget {
  final int rssi;
  const _SignalBars({required this.rssi});

  @override
  Widget build(BuildContext context) {
    // rssi typically -30 (excellent) to -100 (very weak)
    final strength = rssi > -60
        ? 3
        : rssi > -75
        ? 2
        : 1;
    return Row(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        for (int i = 1; i <= 3; i++)
          Container(
            width: 5,
            height: 6.0 * i,
            margin: const EdgeInsets.only(left: 2),
            decoration: BoxDecoration(
              color: i <= strength ? Colors.greenAccent : Colors.grey.shade700,
              borderRadius: BorderRadius.circular(2),
            ),
          ),
      ],
    );
  }
}
