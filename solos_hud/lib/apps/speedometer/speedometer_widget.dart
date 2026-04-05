import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/gps/gps_service.dart';
import '../../core/settings/app_settings.dart';
import 'speedometer_app.dart';

class SpeedometerWidget extends StatelessWidget {
  final SpeedometerApp app;
  const SpeedometerWidget({super.key, required this.app});

  @override
  Widget build(BuildContext context) {
    final gps = context.watch<GpsService>();
    final settings = context.watch<AppSettings>();

    return switch (gps.status) {
      GpsStatus.uninitialized => _buildAction(
        context,
        icon: Icons.location_searching,
        message: 'GPS not started',
        buttonLabel: 'Start GPS',
        onTap: () => gps.init(),
      ),
      GpsStatus.requesting => const Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            CircularProgressIndicator(),
            SizedBox(height: 12),
            Text('Requesting GPS permission…'),
          ],
        ),
      ),
      GpsStatus.denied => _buildAction(
        context,
        icon: Icons.location_off,
        message: gps.error ?? 'Location permission denied',
        buttonLabel: 'Retry',
        onTap: () => gps.retry(),
      ),
      GpsStatus.disabled => _buildAction(
        context,
        icon: Icons.location_disabled,
        message: 'Location services are disabled',
        buttonLabel: 'Retry',
        onTap: () => gps.retry(),
      ),
      GpsStatus.available => _buildData(context, gps, settings),
    };
  }

  Widget _buildAction(
    BuildContext context, {
    required IconData icon,
    required String message,
    required String buttonLabel,
    required VoidCallback onTap,
  }) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 48, color: Colors.grey.shade400),
          const SizedBox(height: 8),
          Text(
            message,
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.grey.shade400),
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            onPressed: onTap,
            icon: const Icon(Icons.refresh),
            label: Text(buttonLabel),
          ),
        ],
      ),
    );
  }

  Widget _buildData(
    BuildContext context,
    GpsService gps,
    AppSettings settings,
  ) {
    final data = gps.data;
    final unit = settings.speedUnit;
    final speed = data.speedIn(unit);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // Big speed readout
        Center(
          child: Column(
            children: [
              Text(
                speed.toStringAsFixed(1),
                style: Theme.of(context).textTheme.displayLarge?.copyWith(
                  fontWeight: FontWeight.w900,
                  fontSize: 80,
                  fontFamily: 'monospace',
                ),
              ),
              Text(
                unit,
                style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  color: Theme.of(context).colorScheme.primary,
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 16),
        const Divider(),
        const SizedBox(height: 8),
        _GpsInfoGrid(data: data),
        const SizedBox(height: 12),
        _HudPreview(payload: app.buildGlassesPayload() ?? '—'),
      ],
    );
  }
}

class _GpsInfoGrid extends StatelessWidget {
  final GpsData data;
  const _GpsInfoGrid({required this.data});

  @override
  Widget build(BuildContext context) {
    return GridView.count(
      crossAxisCount: 2,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      childAspectRatio: 3,
      children: [
        _InfoTile(label: 'Lat', value: data.latitude.toStringAsFixed(6)),
        _InfoTile(label: 'Lon', value: data.longitude.toStringAsFixed(6)),
        _InfoTile(
          label: 'Altitude',
          value: data.altitude != null
              ? '${data.altitude!.toStringAsFixed(1)} m'
              : '--',
        ),
        _InfoTile(
          label: 'Heading',
          value: data.heading != null
              ? '${data.heading!.toStringAsFixed(1)}°'
              : '--',
        ),
        _InfoTile(
          label: 'Accuracy',
          value: data.accuracy != null
              ? '±${data.accuracy!.toStringAsFixed(1)} m'
              : '--',
        ),
        _InfoTile(label: 'Speed (m/s)', value: data.speedMs.toStringAsFixed(2)),
      ],
    );
  }
}

class _InfoTile extends StatelessWidget {
  final String label;
  final String value;
  const _InfoTile({required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
      child: Row(
        children: [
          Text(
            '$label:',
            style: TextStyle(color: Colors.grey.shade400, fontSize: 12),
          ),
          const SizedBox(width: 4),
          Expanded(
            child: Text(
              value,
              style: const TextStyle(
                fontFamily: 'monospace',
                fontWeight: FontWeight.bold,
                fontSize: 12,
              ),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}

class _HudPreview extends StatelessWidget {
  final String payload;
  const _HudPreview({required this.payload});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.black,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.grey.shade700),
      ),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'GLASSES HUD PREVIEW',
            style: TextStyle(
              color: Colors.grey.shade400,
              fontSize: 10,
              letterSpacing: 1.5,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            payload.replaceAll('\n', '  '),
            style: const TextStyle(
              color: Colors.greenAccent,
              fontSize: 22,
              fontFamily: 'monospace',
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }
}
