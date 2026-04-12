import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../core/vesc/vesc_service.dart';
import '../../core/vesc/vesc_packet.dart';
import 'vesc_app.dart';

class VescPhoneWidget extends StatefulWidget {
  final VescService vesc;
  final VescApp app;
  const VescPhoneWidget({super.key, required this.vesc, required this.app});

  @override
  State<VescPhoneWidget> createState() => _VescPhoneWidgetState();
}

class _VescPhoneWidgetState extends State<VescPhoneWidget> {
  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: Listenable.merge([widget.vesc, widget.vesc.settings]),
      builder: (context, _) {
        final connected = widget.vesc.isConnected && widget.vesc.values != null;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            _ConnectionCard(vesc: widget.vesc),
            if (connected) ...[
              const SizedBox(height: 10),
              // ── Dashboard button ──────────────────────────────────────────
              FilledButton.icon(
                onPressed: () => Navigator.of(context).push(
                  MaterialPageRoute(
                    fullscreenDialog: true,
                    builder: (_) => VescDashboardScreen(
                      vesc: widget.vesc,
                      app: widget.app,
                    ),
                  ),
                ),
                icon: const Icon(Icons.dashboard_rounded, size: 18),
                label: const Text('Phone Dashboard',
                    style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold)),
                style: FilledButton.styleFrom(
                  backgroundColor: Colors.cyanAccent.withValues(alpha: 0.15),
                  foregroundColor: Colors.cyanAccent,
                  side: BorderSide(
                      color: Colors.cyanAccent.withValues(alpha: 0.4)),
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10)),
                ),
              ),
              const SizedBox(height: 10),
              _FocusSelector(app: widget.app),
              const SizedBox(height: 10),
              _TelemetryGrid(vesc: widget.vesc),
              const SizedBox(height: 10),
              _TemperatureBar(vesc: widget.vesc),
              const SizedBox(height: 10),
            ] else
              const SizedBox(height: 10),
            _BoardSettings(vesc: widget.vesc),
          ],
        );
      },
    );
  }
}

// ── Phone Dashboard (full-screen) ─────────────────────────────────────────────

class VescDashboardScreen extends StatefulWidget {
  final VescService vesc;
  final VescApp app;
  const VescDashboardScreen({super.key, required this.vesc, required this.app});

  @override
  State<VescDashboardScreen> createState() => _VescDashboardScreenState();
}

class _VescDashboardScreenState extends State<VescDashboardScreen> {
  @override
  void initState() {
    super.initState();
    // Keep screen on while dashboard is visible
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.immersiveSticky);
  }

  @override
  void dispose() {
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.edgeToEdge);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF060A0F),
      body: ListenableBuilder(
        listenable: Listenable.merge([widget.vesc, widget.vesc.settings]),
        builder: (context, _) {
          final v = widget.vesc.values;
          if (!widget.vesc.isConnected || v == null) {
            return _buildDisconnected(context);
          }
          return _buildDashboard(context, v);
        },
      ),
    );
  }

  Widget _buildDisconnected(BuildContext context) {
    return SafeArea(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.electric_bolt_outlined,
              size: 64, color: Colors.grey),
          const SizedBox(height: 16),
          Text(
            widget.vesc.scanning ? 'Scanning…' : 'Not connected',
            style: const TextStyle(
                color: Colors.grey, fontSize: 20, fontWeight: FontWeight.w500),
          ),
          const SizedBox(height: 32),
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Back',
                style: TextStyle(color: Colors.cyanAccent, fontSize: 16)),
          ),
        ],
      ),
    );
  }

  Widget _buildDashboard(BuildContext context, VescValues v) {
    final s = widget.vesc.settings;
    final speed = v.speedKmh(
      motorPoles: widget.vesc.motorPoles,
      wheelDiameterMm: widget.vesc.wheelDiameterMm,
    );

    final adjustedV = v.voltageV + s.vescVoltageOffset;
    final cellV = adjustedV / s.vescCellCount;
    final isLiFe = s.vescBatteryChem == 'lifepo4';
    final emptyV = isLiFe ? 2.8 : 3.2;
    final fullV = isLiFe ? 3.6 : 4.2;
    final batPct = ((cellV - emptyV) / (fullV - emptyV)).clamp(0.0, 1.0);
    final batPctInt = (batPct * 100).round();

    final Color batColor = batPct > 0.5
        ? Colors.greenAccent
        : batPct > 0.25
            ? Colors.orange
            : Colors.redAccent;

    final Color voltColor = cellV > emptyV + 0.5
        ? Colors.greenAccent
        : cellV > emptyV + 0.2
            ? Colors.orange
            : Colors.redAccent;

    final watts = v.watts;
    final Color powerColor = watts > 1000
        ? Colors.redAccent
        : watts > 400
            ? Colors.orange
            : Colors.cyanAccent;

    return SafeArea(
      child: Column(
        children: [
          // ── Top bar ──────────────────────────────────────────────────────
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Row(
              children: [
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: Colors.greenAccent.withValues(alpha: 0.1),
                    borderRadius: BorderRadius.circular(6),
                    border: Border.all(
                        color: Colors.greenAccent.withValues(alpha: 0.4)),
                  ),
                  child: const Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.electric_bolt,
                          size: 12, color: Colors.greenAccent),
                      SizedBox(width: 4),
                      Text('LIVE',
                          style: TextStyle(
                              color: Colors.greenAccent,
                              fontSize: 11,
                              fontWeight: FontWeight.bold,
                              letterSpacing: 1.2)),
                    ],
                  ),
                ),
                const Spacer(),
                // Fault indicator
                if (v.faultCode != 0)
                  Container(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 8, vertical: 4),
                    decoration: BoxDecoration(
                      color: Colors.redAccent.withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(6),
                    ),
                    child: Text(
                      v.faultLabel,
                      style: const TextStyle(
                          color: Colors.redAccent,
                          fontSize: 12,
                          fontWeight: FontWeight.bold),
                    ),
                  ),
                const SizedBox(width: 8),
                IconButton(
                  onPressed: () => Navigator.of(context).pop(),
                  icon: const Icon(Icons.close, color: Colors.grey, size: 22),
                  padding: EdgeInsets.zero,
                  constraints: const BoxConstraints(),
                ),
              ],
            ),
          ),

          Expanded(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  // ── Speed ─────────────────────────────────────────────────
                  _DashMetric(
                    label: 'SPEED',
                    value: speed.isFinite
                        ? speed.toStringAsFixed(1)
                        : '0.0',
                    unit: 'km/h',
                    color: Colors.cyanAccent,
                    valueFontSize: 110,
                    unitFontSize: 22,
                    icon: Icons.speed,
                  ),

                  // ── Battery bar ───────────────────────────────────────────
                  _BatteryRow(
                    pct: batPct,
                    pctInt: batPctInt,
                    voltageStr: adjustedV.isFinite
                        ? '${adjustedV.toStringAsFixed(1)} V'
                        : '-- V',
                    cellVStr: cellV.isFinite
                        ? '${cellV.toStringAsFixed(2)} V/cell'
                        : '-- V/cell',
                    batColor: batColor,
                    voltColor: voltColor,
                  ),

                  // ── Power + Current ────────────────────────────────────────
                  Row(
                    children: [
                      Expanded(
                        child: _DashMetric(
                          label: 'POWER',
                          value: watts.isFinite
                              ? watts.round().toString()
                              : '--',
                          unit: 'W',
                          color: powerColor,
                          valueFontSize: 64,
                          unitFontSize: 18,
                          icon: Icons.bolt,
                        ),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: _DashMetric(
                          label: 'PHASE A',
                          value: v.motorCurrentA.isFinite
                              ? v.motorCurrentA.abs().toStringAsFixed(1)
                              : '--',
                          unit: 'A',
                          color: Colors.orangeAccent,
                          valueFontSize: 64,
                          unitFontSize: 18,
                          icon: Icons.electric_meter_outlined,
                        ),
                      ),
                    ],
                  ),

                  // ── Duty + Wh ─────────────────────────────────────────────
                  Row(
                    children: [
                      Expanded(
                        child: _DashMetric(
                          label: 'DUTY',
                          value: v.dutyCycle.isFinite
                              ? '${(v.dutyCycle * 100).round()}'
                              : '--',
                          unit: '%',
                          color: Colors.white70,
                          valueFontSize: 48,
                          unitFontSize: 16,
                          icon: Icons.tune,
                        ),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: _DashMetric(
                          label: 'CONSUMED',
                          value: v.wattHours.isFinite
                              ? v.wattHours.toStringAsFixed(1)
                              : '--',
                          unit: 'Wh',
                          color: Colors.white38,
                          valueFontSize: 48,
                          unitFontSize: 16,
                          icon: Icons.battery_charging_full,
                        ),
                      ),
                    ],
                  ),

                  // ── Temperatures ──────────────────────────────────────────
                  _TempStrip(motor: v.tempMotor, fet: v.tempFet),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _DashMetric extends StatelessWidget {
  final String label, value, unit;
  final Color color;
  final double valueFontSize, unitFontSize;
  final IconData icon;

  const _DashMetric({
    required this.label,
    required this.value,
    required this.unit,
    required this.color,
    required this.valueFontSize,
    required this.unitFontSize,
    required this.icon,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.07),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: color.withValues(alpha: 0.25)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icon, size: 12, color: color.withValues(alpha: 0.7)),
              const SizedBox(width: 4),
              Text(label,
                  style: TextStyle(
                      fontSize: 11,
                      color: color.withValues(alpha: 0.7),
                      fontWeight: FontWeight.w600,
                      letterSpacing: 1.0)),
            ],
          ),
          const SizedBox(height: 2),
          Row(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Flexible(
                child: FittedBox(
                  fit: BoxFit.scaleDown,
                  alignment: Alignment.bottomLeft,
                  child: Text(
                    value,
                    style: TextStyle(
                      fontSize: valueFontSize,
                      color: color,
                      fontWeight: FontWeight.w900,
                      fontFamily: 'monospace',
                      height: 1.0,
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 4),
              Padding(
                padding: const EdgeInsets.only(bottom: 6),
                child: Text(unit,
                    style: TextStyle(
                        fontSize: unitFontSize,
                        color: color.withValues(alpha: 0.6),
                        fontWeight: FontWeight.w500)),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _BatteryRow extends StatelessWidget {
  final double pct;
  final int pctInt;
  final String voltageStr, cellVStr;
  final Color batColor, voltColor;

  const _BatteryRow({
    required this.pct,
    required this.pctInt,
    required this.voltageStr,
    required this.cellVStr,
    required this.batColor,
    required this.voltColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      decoration: BoxDecoration(
        color: batColor.withValues(alpha: 0.07),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: batColor.withValues(alpha: 0.25)),
      ),
      child: Column(
        children: [
          Row(
            children: [
              Icon(
                pct > 0.75
                    ? Icons.battery_full
                    : pct > 0.5
                        ? Icons.battery_5_bar
                        : pct > 0.25
                            ? Icons.battery_3_bar
                            : Icons.battery_1_bar,
                color: batColor,
                size: 16,
              ),
              const SizedBox(width: 6),
              const Text('BATTERY',
                  style: TextStyle(
                      fontSize: 11,
                      color: Colors.grey,
                      fontWeight: FontWeight.w600,
                      letterSpacing: 1.0)),
              const Spacer(),
              Text(voltageStr,
                  style: TextStyle(
                      fontSize: 16,
                      color: voltColor,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'monospace')),
              const SizedBox(width: 8),
              Text(cellVStr,
                  style: TextStyle(
                      fontSize: 12,
                      color: Colors.grey.shade600,
                      fontFamily: 'monospace')),
            ],
          ),
          const SizedBox(height: 10),
          Row(
            children: [
              Expanded(
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(6),
                  child: LinearProgressIndicator(
                    value: pct,
                    backgroundColor: Colors.grey.shade800,
                    valueColor: AlwaysStoppedAnimation(batColor),
                    minHeight: 20,
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Text(
                '$pctInt%',
                style: TextStyle(
                  fontSize: 32,
                  fontWeight: FontWeight.w900,
                  color: batColor,
                  fontFamily: 'monospace',
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _TempStrip extends StatelessWidget {
  final double motor, fet;
  const _TempStrip({required this.motor, required this.fet});

  @override
  Widget build(BuildContext context) {
    Color tempColor(double t, double max) {
      final p = t / max;
      return p > 0.85 ? Colors.redAccent : p > 0.65 ? Colors.orange : Colors.grey;
    }

    return Row(
      children: [
        Expanded(
          child: _TempCell(
              label: 'MOTOR',
              temp: motor,
              max: 100,
              color: tempColor(motor, 100)),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _TempCell(
              label: 'FET', temp: fet, max: 80, color: tempColor(fet, 80)),
        ),
      ],
    );
  }
}

class _TempCell extends StatelessWidget {
  final String label;
  final double temp, max;
  final Color color;
  const _TempCell(
      {required this.label,
      required this.temp,
      required this.max,
      required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
      decoration: BoxDecoration(
        color: const Color(0xFF0D1318),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.white12),
      ),
      child: Row(
        children: [
          Icon(Icons.thermostat, size: 14, color: color),
          const SizedBox(width: 6),
          Text(label,
              style: const TextStyle(fontSize: 11, color: Colors.grey)),
          const Spacer(),
          Text(
            temp.isFinite ? '${temp.round()}°C' : '--°C',
            style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: color,
                fontFamily: 'monospace'),
          ),
        ],
      ),
    );
  }
}

// ── Focus selector ────────────────────────────────────────────────────────────

class _FocusSelector extends StatefulWidget {
  final VescApp app;
  const _FocusSelector({required this.app});

  @override
  State<_FocusSelector> createState() => _FocusSelectorState();
}

class _FocusSelectorState extends State<_FocusSelector> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 10),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.2)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.visibility_outlined,
                  size: 13, color: Colors.cyanAccent),
              const SizedBox(width: 5),
              const Text(
                'Glasses Focus',
                style: TextStyle(
                    fontSize: 12,
                    color: Colors.cyanAccent,
                    fontWeight: FontWeight.w600),
              ),
              const Spacer(),
              Text(
                'FRONT / BACK buttons',
                style: TextStyle(fontSize: 10, color: Colors.grey.shade600),
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              _NavBtn(
                icon: Icons.arrow_upward,
                onTap: () => setState(() => widget.app.cycleFocusPrev()),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Row(
                  children: VescFocus.values.map((f) {
                    final selected = f == widget.app.focus;
                    return Expanded(
                      child: GestureDetector(
                        onTap: () {
                          setState(() {
                            while (widget.app.focus != f) {
                              widget.app.cycleFocusNext();
                            }
                          });
                        },
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 180),
                          margin: const EdgeInsets.symmetric(horizontal: 2),
                          padding: const EdgeInsets.symmetric(vertical: 7),
                          decoration: BoxDecoration(
                            color: selected
                                ? Colors.cyanAccent.withValues(alpha: 0.18)
                                : Colors.transparent,
                            borderRadius: BorderRadius.circular(6),
                            border: Border.all(
                              color: selected
                                  ? Colors.cyanAccent.withValues(alpha: 0.6)
                                  : Colors.transparent,
                            ),
                          ),
                          child: Text(
                            f.label,
                            textAlign: TextAlign.center,
                            overflow: TextOverflow.ellipsis,
                            style: TextStyle(
                              fontSize: 11,
                              fontWeight: selected
                                  ? FontWeight.bold
                                  : FontWeight.normal,
                              color: selected
                                  ? Colors.cyanAccent
                                  : Colors.grey.shade500,
                            ),
                          ),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
              const SizedBox(width: 6),
              _NavBtn(
                icon: Icons.arrow_downward,
                onTap: () => setState(() => widget.app.cycleFocusNext()),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _NavBtn extends StatelessWidget {
  final IconData icon;
  final VoidCallback onTap;
  const _NavBtn({required this.icon, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(20),
      child: Container(
        width: 34,
        height: 34,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.cyanAccent.withValues(alpha: 0.1),
          border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.3)),
        ),
        child: Icon(icon, size: 15, color: Colors.cyanAccent),
      ),
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
      VescState.reconnecting =>
        (Colors.orange, Icons.sync, 'Reconnecting…'),
      VescState.disconnected => (
        Colors.grey.shade600,
        Icons.electric_bolt_outlined,
        'Not connected',
      ),
    };

    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Row(
        children: [
          Icon(icon, color: color, size: 18),
          const SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: TextStyle(
                    color: color,
                    fontWeight: FontWeight.bold,
                    fontSize: 13,
                  ),
                ),
                if (vesc.deviceName != null)
                  Text(
                    vesc.deviceName!,
                    style:
                        TextStyle(fontSize: 11, color: Colors.grey.shade400),
                    overflow: TextOverflow.ellipsis,
                  ),
                if (vesc.error != null)
                  Text(
                    vesc.error!,
                    style: const TextStyle(
                        fontSize: 10, color: Colors.redAccent),
                    overflow: TextOverflow.ellipsis,
                    maxLines: 2,
                  ),
              ],
            ),
          ),
          if (!vesc.isConnected &&
              vesc.state != VescState.scanning &&
              vesc.state != VescState.connecting &&
              vesc.state != VescState.reconnecting)
            FilledButton.icon(
              onPressed: vesc.scan,
              icon: const Icon(Icons.search, size: 13),
              label: const Text('Scan', style: TextStyle(fontSize: 11)),
              style: FilledButton.styleFrom(
                backgroundColor: Colors.cyanAccent.withValues(alpha: 0.2),
                foregroundColor: Colors.cyanAccent,
                padding:
                    const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
              ),
            ),
          if (vesc.state == VescState.scanning ||
              vesc.state == VescState.connecting ||
              vesc.state == VescState.reconnecting)
            const SizedBox(
              width: 18,
              height: 18,
              child: CircularProgressIndicator(
                strokeWidth: 2,
                color: Colors.cyanAccent,
              ),
            ),
          if (vesc.isConnected) ...[
            const SizedBox(width: 4),
            TextButton(
              onPressed: vesc.disconnect,
              style:
                  TextButton.styleFrom(foregroundColor: Colors.redAccent),
              child: const Text('Disconnect',
                  style: TextStyle(fontSize: 11)),
            ),
          ],
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

    final items = [
      _Stat('Speed',
          speed.isFinite ? '${speed.toStringAsFixed(1)} km/h' : '-- km/h',
          Colors.cyanAccent,
          isLarge: true),
      _Stat('Voltage',
          v.voltageV.isFinite
              ? '${v.voltageV.toStringAsFixed(1)} V'
              : '-- V',
          Colors.white),
      _Stat('Power',
          v.watts.isFinite ? '${v.watts.round()} W' : '-- W', Colors.white),
      _Stat('Current',
          v.inputCurrentA.isFinite
              ? '${v.inputCurrentA.abs().toStringAsFixed(1)} A'
              : '-- A',
          Colors.white),
      _Stat('Duty',
          v.dutyCycle.isFinite
              ? '${(v.dutyCycle * 100).round()}%'
              : '--%',
          Colors.white),
      _Stat('Wh used',
          v.wattHours.isFinite
              ? '${v.wattHours.toStringAsFixed(1)} Wh'
              : '-- Wh',
          Colors.grey.shade400),
    ];

    return GridView.count(
      crossAxisCount: 3,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      mainAxisSpacing: 6,
      crossAxisSpacing: 6,
      childAspectRatio: 2.2,
      children: items.map(_buildStatCard).toList(),
    );
  }

  Widget _buildStatCard(_Stat s) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 6),
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
          Text(s.label,
              style: TextStyle(fontSize: 9, color: Colors.grey.shade500)),
          const SizedBox(height: 2),
          Flexible(
            child: FittedBox(
              fit: BoxFit.scaleDown,
              alignment: Alignment.centerLeft,
              child: Text(
                s.value,
                style: TextStyle(
                  fontSize: s.isLarge ? 16 : 14,
                  color: s.color,
                  fontWeight: FontWeight.bold,
                  fontFamily: 'monospace',
                ),
              ),
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
          const SizedBox(height: 6),
          _TempRow(label: 'FET', tempC: v.tempFet, maxC: 80),
          const SizedBox(height: 8),
          Row(
            children: [
              const Icon(Icons.warning_amber_outlined,
                  size: 13, color: Colors.grey),
              const SizedBox(width: 5),
              const Text('Fault: ',
                  style: TextStyle(fontSize: 11, color: Colors.grey)),
              Flexible(
                child: Text(
                  v.faultLabel,
                  style: TextStyle(
                    fontSize: 11,
                    fontWeight: FontWeight.bold,
                    color:
                        v.faultCode == 0 ? Colors.green : Colors.redAccent,
                  ),
                  overflow: TextOverflow.ellipsis,
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
  const _TempRow(
      {required this.label, required this.tempC, required this.maxC});

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
          width: 44,
          child: Text(label,
              style:
                  const TextStyle(fontSize: 11, color: Colors.grey)),
        ),
        Expanded(
          child: ClipRRect(
            borderRadius: BorderRadius.circular(3),
            child: LinearProgressIndicator(
              value: pct,
              backgroundColor: Colors.grey.shade800,
              valueColor: AlwaysStoppedAnimation(color),
              minHeight: 7,
            ),
          ),
        ),
        const SizedBox(width: 8),
        Text(
          tempC.isFinite ? '${tempC.round()}°C' : '--°C',
          style: TextStyle(
              fontSize: 11, color: color, fontFamily: 'monospace'),
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
        leading: const Icon(Icons.settings_outlined,
            size: 15, color: Colors.grey),
        title: const Text('Board Settings',
            style: TextStyle(fontSize: 13, color: Colors.grey)),
        tilePadding: EdgeInsets.zero,
        childrenPadding: const EdgeInsets.only(bottom: 8),
        children: [
          _TextSettingRow(
            label: 'Motor Poles',
            value: '${vesc.motorPoles}',
            hint: 'e.g. 14',
            suffix: '',
            keyboardType: TextInputType.number,
            onSubmit: (s) {
              final v = int.tryParse(s.trim());
              if (v != null && v >= 2) vesc.settings.setVescMotorPoles(v);
            },
          ),
          _TextSettingRow(
            label: 'Wheel Diameter',
            value: '${vesc.wheelDiameterMm.round()}',
            hint: 'e.g. 97 or 622',
            suffix: 'mm',
            keyboardType:
                const TextInputType.numberWithOptions(decimal: true),
            onSubmit: (s) {
              final v = double.tryParse(s.trim());
              if (v != null && v > 0) vesc.settings.setVescWheelDiameter(v);
            },
          ),
          const Divider(height: 12, color: Colors.white12),
          _SettingRow(
            label: 'Voltage Offset',
            value:
                '${vesc.settings.vescVoltageOffset.toStringAsFixed(1)} V',
            onDec: () => vesc.settings.setVescVoltageOffset(
                vesc.settings.vescVoltageOffset - 0.1),
            onInc: () => vesc.settings.setVescVoltageOffset(
                vesc.settings.vescVoltageOffset + 0.1),
          ),
          _SettingRow(
            label: 'Cell Count',
            value: '${vesc.settings.vescCellCount}S',
            onDec: () => vesc.settings
                .setVescCellCount(vesc.settings.vescCellCount - 1),
            onInc: () => vesc.settings
                .setVescCellCount(vesc.settings.vescCellCount + 1),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 4),
            child: Row(
              children: [
                const Expanded(
                  child: Text('Chemistry',
                      style: TextStyle(fontSize: 13)),
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
                    selectedBackgroundColor:
                        Colors.cyanAccent.withValues(alpha: 0.2),
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
      padding: const EdgeInsets.symmetric(vertical: 3),
      child: Row(
        children: [
          Expanded(
              child: Text(label, style: const TextStyle(fontSize: 13))),
          IconButton(
            onPressed: onDec,
            icon: const Icon(Icons.remove, size: 15),
            constraints: const BoxConstraints(),
            padding: EdgeInsets.zero,
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10),
            child: Text(value,
                style: const TextStyle(
                    fontSize: 13,
                    fontFamily: 'monospace',
                    fontWeight: FontWeight.bold)),
          ),
          IconButton(
            onPressed: onInc,
            icon: const Icon(Icons.add, size: 15),
            constraints: const BoxConstraints(),
            padding: EdgeInsets.zero,
          ),
        ],
      ),
    );
  }
}

// ── Text-input setting row ────────────────────────────────────────────────────

class _TextSettingRow extends StatefulWidget {
  final String label;
  final String value;
  final String hint;
  final String suffix;
  final TextInputType keyboardType;
  final ValueChanged<String> onSubmit;

  const _TextSettingRow({
    required this.label,
    required this.value,
    required this.hint,
    required this.suffix,
    required this.keyboardType,
    required this.onSubmit,
  });

  @override
  State<_TextSettingRow> createState() => _TextSettingRowState();
}

class _TextSettingRowState extends State<_TextSettingRow> {
  late final TextEditingController _ctrl;
  final _focus = FocusNode();
  bool _editing = false;

  @override
  void initState() {
    super.initState();
    _ctrl = TextEditingController(text: widget.value);
  }

  @override
  void didUpdateWidget(_TextSettingRow old) {
    super.didUpdateWidget(old);
    if (!_editing && old.value != widget.value) {
      _ctrl.text = widget.value;
    }
  }

  @override
  void dispose() {
    _ctrl.dispose();
    _focus.dispose();
    super.dispose();
  }

  void _submit() {
    setState(() => _editing = false);
    _focus.unfocus();
    widget.onSubmit(_ctrl.text);
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 5),
      child: Row(
        children: [
          Expanded(
            child: Text(widget.label,
                style: const TextStyle(fontSize: 13)),
          ),
          SizedBox(
            width: 110,
            height: 36,
            child: TextField(
              controller: _ctrl,
              focusNode: _focus,
              keyboardType: widget.keyboardType,
              textAlign: TextAlign.right,
              style: const TextStyle(
                fontSize: 14,
                fontFamily: 'monospace',
                fontWeight: FontWeight.bold,
                color: Colors.cyanAccent,
              ),
              decoration: InputDecoration(
                suffixText:
                    widget.suffix.isEmpty ? null : widget.suffix,
                suffixStyle: TextStyle(
                    fontSize: 12, color: Colors.grey.shade500),
                hintText: widget.hint,
                hintStyle: TextStyle(
                    fontSize: 12, color: Colors.grey.shade700),
                isDense: true,
                contentPadding: const EdgeInsets.symmetric(
                    horizontal: 8, vertical: 8),
                filled: true,
                fillColor: Colors.grey.shade900,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: BorderSide(
                      color: Colors.cyanAccent.withValues(alpha: 0.3)),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide:
                      const BorderSide(color: Colors.cyanAccent),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide:
                      BorderSide(color: Colors.grey.shade700),
                ),
              ),
              onTap: () => setState(() => _editing = true),
              onSubmitted: (_) => _submit(),
              onEditingComplete: _submit,
            ),
          ),
        ],
      ),
    );
  }
}
