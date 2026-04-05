import 'package:flutter/material.dart';
import '../../core/vesc/vesc_service.dart';

class VescPhoneWidget extends StatelessWidget {
  final VescService vesc;
  const VescPhoneWidget({super.key, required this.vesc});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: Listenable.merge([vesc, vesc.settings]),
      builder: (context, _) {
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            _ConnectionCard(vesc: vesc),
            const SizedBox(height: 12),
            if (vesc.isConnected && vesc.values != null) ...[
              _TelemetryGrid(vesc: vesc),
              const SizedBox(height: 12),
              _TemperatureBar(vesc: vesc),
              const SizedBox(height: 12),
            ],
            _BoardSettings(vesc: vesc),
          ],
        );
      },
    );
  }
}

// ── Connection card ───────────────────────────────────────────────────────────

class _ConnectionCard extends StatelessWidget {
  final VescService vesc;
  const _ConnectionCard({required this.vesc});

  @override
  Widget build(BuildContext context) {
    final (color, icon, label) = switch (vesc.state) {
      VescState.connected => (Colors.green, Icons.electric_bolt, 'Connected'),
      VescState.connecting => (Colors.orange, Icons.sync, 'Connecting…'),
      VescState.scanning => (Colors.cyanAccent, Icons.search, 'Scanning…'),
      VescState.disconnected => (
        Colors.grey.shade600,
        Icons.electric_bolt_outlined,
        'Not connected',
      ),
    };

    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Row(
        children: [
          Icon(icon, color: color, size: 20),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: TextStyle(
                    color: color,
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                  ),
                ),
                if (vesc.deviceName != null)
                  Text(
                    vesc.deviceName!,
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade400),
                  ),
                if (vesc.error != null)
                  Text(
                    vesc.error!,
                    style: const TextStyle(
                      fontSize: 11,
                      color: Colors.redAccent,
                    ),
                  ),
              ],
            ),
          ),
          if (!vesc.isConnected && !vesc.scanning)
            FilledButton.icon(
              onPressed: vesc.scan,
              icon: const Icon(Icons.search, size: 14),
              label: const Text('Scan', style: TextStyle(fontSize: 12)),
              style: FilledButton.styleFrom(
                backgroundColor: Colors.cyanAccent.withValues(alpha: 0.2),
                foregroundColor: Colors.cyanAccent,
              ),
            ),
          if (vesc.scanning)
            const SizedBox(
              width: 20,
              height: 20,
              child: CircularProgressIndicator(
                strokeWidth: 2,
                color: Colors.cyanAccent,
              ),
            ),
          if (vesc.isConnected)
            TextButton(
              onPressed: vesc.disconnect,
              style: TextButton.styleFrom(foregroundColor: Colors.redAccent),
              child: const Text('Disconnect', style: TextStyle(fontSize: 12)),
            ),
        ],
      ),
    );
  }
}

// ── Live telemetry grid ───────────────────────────────────────────────────────

class _TelemetryGrid extends StatelessWidget {
  final VescService vesc;
  const _TelemetryGrid({required this.vesc});

  @override
  Widget build(BuildContext context) {
    final v = vesc.values!;
    final speed = v.speedKmh(
      motorPoles: vesc.motorPoles,
      wheelDiameterMm: vesc.wheelDiameterMm,
    );

    final speedStr = speed.isFinite
        ? '${speed.toStringAsFixed(1)} km/h'
        : '-- km/h';
    final voltStr = v.voltageV.isFinite
        ? '${v.voltageV.toStringAsFixed(1)} V'
        : '-- V';
    final wattStr = v.watts.isFinite ? '${v.watts.round()} W' : '-- W';
    final ampStr = v.inputCurrentA.isFinite
        ? '${v.inputCurrentA.abs().toStringAsFixed(1)} A'
        : '-- A';
    final dutyStr = v.dutyCycle.isFinite
        ? '${(v.dutyCycle * 100).round()}%'
        : '--%';
    final whStr = v.wattHours.isFinite
        ? '${v.wattHours.toStringAsFixed(1)} Wh'
        : '-- Wh';

    final items = [
      _Stat('Speed', speedStr, Colors.cyanAccent, isLarge: true),
      _Stat('Voltage', voltStr, Colors.white),
      _Stat('Power', wattStr, Colors.white),
      _Stat('Current', ampStr, Colors.white),
      _Stat('Duty', dutyStr, Colors.white),
      _Stat('Wh used', whStr, Colors.grey.shade400),
    ];

    return GridView.count(
      crossAxisCount: 3,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      mainAxisSpacing: 8,
      crossAxisSpacing: 8,
      childAspectRatio: 2.0,
      children: items.map(_buildStatCard).toList(),
    );
  }

  Widget _buildStatCard(_Stat s) {
    return Container(
      padding: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: s.isLarge
            ? Colors.cyanAccent.withValues(alpha: 0.08)
            : Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
        border: s.isLarge
            ? Border.all(color: Colors.cyanAccent.withValues(alpha: 0.3))
            : null,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            s.label,
            style: TextStyle(fontSize: 10, color: Colors.grey.shade500),
          ),
          Text(
            s.value,
            style: TextStyle(
              fontSize: s.isLarge ? 18 : 15,
              color: s.color,
              fontWeight: FontWeight.bold,
              fontFamily: 'monospace',
            ),
          ),
        ],
      ),
    );
  }
}

class _Stat {
  final String label, value;
  final Color color;
  final bool isLarge;
  const _Stat(this.label, this.value, this.color, {this.isLarge = false});
}

// ── Temperature bar ───────────────────────────────────────────────────────────

class _TemperatureBar extends StatelessWidget {
  final VescService vesc;
  const _TemperatureBar({required this.vesc});

  @override
  Widget build(BuildContext context) {
    final v = vesc.values!;
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        children: [
          _TempRow(label: 'Motor', tempC: v.tempMotor, maxC: 100),
          const SizedBox(height: 8),
          _TempRow(label: 'FET', tempC: v.tempFet, maxC: 80),
          const SizedBox(height: 8),
          // Fault
          Row(
            children: [
              const Icon(
                Icons.warning_amber_outlined,
                size: 14,
                color: Colors.grey,
              ),
              const SizedBox(width: 6),
              const Text(
                'Fault: ',
                style: TextStyle(fontSize: 12, color: Colors.grey),
              ),
              Text(
                v.faultLabel,
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.bold,
                  color: v.faultCode == 0 ? Colors.green : Colors.redAccent,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _TempRow extends StatelessWidget {
  final String label;
  final double tempC;
  final double maxC;
  const _TempRow({
    required this.label,
    required this.tempC,
    required this.maxC,
  });

  @override
  Widget build(BuildContext context) {
    final pct = (tempC / maxC).clamp(0.0, 1.0);
    final color = pct > 0.85
        ? Colors.redAccent
        : pct > 0.65
        ? Colors.orange
        : Colors.green;
    return Row(
      children: [
        SizedBox(
          width: 50,
          child: Text(
            label,
            style: const TextStyle(fontSize: 12, color: Colors.grey),
          ),
        ),
        Expanded(
          child: ClipRRect(
            borderRadius: BorderRadius.circular(3),
            child: LinearProgressIndicator(
              value: pct,
              backgroundColor: Colors.grey.shade800,
              valueColor: AlwaysStoppedAnimation(color),
              minHeight: 8,
            ),
          ),
        ),
        const SizedBox(width: 8),
        Text(
          tempC.isFinite ? '${tempC.round()}°C' : '--°C',
          style: TextStyle(fontSize: 12, color: color, fontFamily: 'monospace'),
        ),
      ],
    );
  }
}

// ── Board settings ────────────────────────────────────────────────────────────

class _BoardSettings extends StatelessWidget {
  final VescService vesc;
  const _BoardSettings({required this.vesc});

  @override
  Widget build(BuildContext context) {
    return Theme(
      data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
      child: ExpansionTile(
        leading: const Icon(
          Icons.settings_outlined,
          size: 16,
          color: Colors.grey,
        ),
        title: const Text(
          'Board Settings',
          style: TextStyle(fontSize: 13, color: Colors.grey),
        ),
        tilePadding: EdgeInsets.zero,
        childrenPadding: const EdgeInsets.only(bottom: 8),
        children: [
          _SettingRow(
            label: 'Motor Poles',
            value: '${vesc.motorPoles}',
            onDec: () => vesc.motorPoles -= 2,
            onInc: () => vesc.motorPoles += 2,
          ),
          _SettingRow(
            label: 'Wheel Diameter',
            value: '${vesc.wheelDiameterMm.round()} mm',
            onDec: () => vesc.wheelDiameterMm -= 5,
            onInc: () => vesc.wheelDiameterMm += 5,
          ),
          const Divider(height: 16, color: Colors.white12),
          _SettingRow(
            label: 'Voltage Offset',
            value: '${vesc.settings.vescVoltageOffset.toStringAsFixed(1)} V',
            onDec: () => vesc.settings.setVescVoltageOffset(
              vesc.settings.vescVoltageOffset - 0.1,
            ),
            onInc: () => vesc.settings.setVescVoltageOffset(
              vesc.settings.vescVoltageOffset + 0.1,
            ),
          ),
          _SettingRow(
            label: 'Cell Count',
            value: '${vesc.settings.vescCellCount}S',
            onDec: () =>
                vesc.settings.setVescCellCount(vesc.settings.vescCellCount - 1),
            onInc: () =>
                vesc.settings.setVescCellCount(vesc.settings.vescCellCount + 1),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 4),
            child: Row(
              children: [
                const Expanded(
                  child: Text('Chemistry', style: TextStyle(fontSize: 13)),
                ),
                SegmentedButton<String>(
                  segments: const [
                    ButtonSegment(value: 'li-ion', label: Text('Li-ion')),
                    ButtonSegment(value: 'lifepo4', label: Text('LiFe')),
                  ],
                  selected: {vesc.settings.vescBatteryChem},
                  onSelectionChanged: (val) =>
                      vesc.settings.setVescBatteryChem(val.first),
                  style: SegmentedButton.styleFrom(
                    visualDensity: VisualDensity.compact,
                    selectedBackgroundColor: Colors.cyanAccent.withValues(
                      alpha: 0.2,
                    ),
                    selectedForegroundColor: Colors.cyanAccent,
                    textStyle: const TextStyle(fontSize: 11),
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

class _SettingRow extends StatelessWidget {
  final String label, value;
  final VoidCallback onDec, onInc;
  const _SettingRow({
    required this.label,
    required this.value,
    required this.onDec,
    required this.onInc,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        children: [
          Expanded(child: Text(label, style: const TextStyle(fontSize: 13))),
          IconButton(
            onPressed: onDec,
            icon: const Icon(Icons.remove, size: 16),
            constraints: const BoxConstraints(),
            padding: EdgeInsets.zero,
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: Text(
              value,
              style: const TextStyle(
                fontSize: 13,
                fontFamily: 'monospace',
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          IconButton(
            onPressed: onInc,
            icon: const Icon(Icons.add, size: 16),
            constraints: const BoxConstraints(),
            padding: EdgeInsets.zero,
          ),
        ],
      ),
    );
  }
}
