import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/rfcomm/rfcomm_service.dart';
import '../core/rfcomm/glasses_event_service.dart';
import '../core/hud_ui/glasses_menu_controller.dart';

class BleStatusBar extends StatelessWidget {
  const BleStatusBar({super.key});

  @override
  Widget build(BuildContext context) {
    final rfcomm = context.watch<RfcommService>();
    final events = context.watch<GlassesEventService>();
    final menu   = context.watch<GlassesMenuController>();

    final (color, label, icon) = switch (rfcomm.state) {
      RfcommState.connected => (Colors.green, 'Connected', Icons.bluetooth_connected),
      RfcommState.connecting => (Colors.orange, 'Connecting…', Icons.bluetooth_searching),
      RfcommState.disconnected => (Colors.red, 'Disconnected', Icons.bluetooth_disabled),
    };

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      color: color.withValues(alpha: 0.12),
      child: Row(
        children: [
          Icon(icon, color: color, size: 18),
          const SizedBox(width: 8),
          Text(label,
              style: TextStyle(color: color, fontWeight: FontWeight.w600, fontSize: 13)),
          if (rfcomm.isConnected && rfcomm.device != null) ...[
            Text(
              ' — ${rfcomm.device!.name}',
              style: const TextStyle(fontSize: 13),
              overflow: TextOverflow.ellipsis,
            ),
          ],
          const Spacer(),
          // Menu open indicator
          if (menu.isOpen) ...[
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
              decoration: BoxDecoration(
                color: Colors.cyanAccent.withValues(alpha: 0.15),
                borderRadius: BorderRadius.circular(4),
                border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.5)),
              ),
              child: const Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.grid_view, size: 12, color: Colors.cyanAccent),
                  SizedBox(width: 4),
                  Text('MENU OPEN', style: TextStyle(fontSize: 10, color: Colors.cyanAccent, letterSpacing: 0.5)),
                ],
              ),
            ),
            const SizedBox(width: 8),
          ],
          // Battery indicator
          if (rfcomm.isConnected && events.batteryLevel != null) ...[
            _BatteryIcon(
              level: events.batteryLevel!,
              isCharging: events.isCharging ?? false,
            ),
            const SizedBox(width: 4),
            Text(
              events.batteryLabel,
              style: const TextStyle(fontSize: 12, fontFamily: 'monospace'),
            ),
            const SizedBox(width: 8),
          ],
        ],
      ),
    );
  }
}

class _BatteryIcon extends StatelessWidget {
  final int level;
  final bool isCharging;
  const _BatteryIcon({required this.level, required this.isCharging});

  @override
  Widget build(BuildContext context) {
    final color = isCharging
        ? Colors.greenAccent
        : level > 20
            ? Colors.green
            : level > 10
                ? Colors.orange
                : Colors.red;

    final icon = isCharging
        ? Icons.battery_charging_full
        : level > 90
            ? Icons.battery_full
            : level > 60
                ? Icons.battery_5_bar
                : level > 40
                    ? Icons.battery_4_bar
                    : level > 20
                        ? Icons.battery_2_bar
                        : Icons.battery_1_bar;

    return Icon(icon, color: color, size: 18);
  }
}
