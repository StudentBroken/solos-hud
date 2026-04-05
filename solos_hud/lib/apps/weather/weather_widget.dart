import 'package:flutter/material.dart';
import '../../core/weather/weather_service.dart';

class WeatherPhoneWidget extends StatelessWidget {
  final WeatherService weather;
  const WeatherPhoneWidget({super.key, required this.weather});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: weather,
      builder: (context, _) {
        if (weather.loading && !weather.hasData) {
          return const Padding(
            padding: EdgeInsets.all(32),
            child: Center(child: CircularProgressIndicator()),
          );
        }
        if (weather.error != null && !weather.hasData) {
          return _ErrorCard(error: weather.error!, onRetry: weather.refresh);
        }
        final d = weather.data!;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            _CurrentCard(data: d),
            const SizedBox(height: 10),
            _HourlyStrip(hourly: d.hourly),
            const SizedBox(height: 10),
            _DetailsGrid(data: d),
            const SizedBox(height: 10),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text(
                  'Updated ${_timeAgo(d.fetchedAt)}',
                  style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                ),
                const SizedBox(width: 8),
                TextButton.icon(
                  onPressed: weather.loading ? null : weather.refresh,
                  icon: weather.loading
                      ? const SizedBox(
                          width: 12,
                          height: 12,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Icon(Icons.refresh, size: 14),
                  label: const Text('Refresh', style: TextStyle(fontSize: 12)),
                  style: TextButton.styleFrom(
                    foregroundColor: Colors.grey,
                    padding: EdgeInsets.zero,
                    minimumSize: Size.zero,
                  ),
                ),
              ],
            ),
          ],
        );
      },
    );
  }

  static String _timeAgo(DateTime t) {
    final diff = DateTime.now().difference(t);
    if (diff.inMinutes < 1) return 'just now';
    if (diff.inMinutes < 60) return '${diff.inMinutes}m ago';
    return '${diff.inHours}h ago';
  }
}

// ── Current conditions hero ───────────────────────────────────────────────────

class _CurrentCard extends StatelessWidget {
  final WeatherData data;
  const _CurrentCard({required this.data});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Colors.cyan.shade900.withValues(alpha: 0.4),
            Colors.blue.shade900.withValues(alpha: 0.3),
          ],
        ),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.2)),
      ),
      child: Row(
        children: [
          // Condition symbol + label
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(data.condition.symbol, style: const TextStyle(fontSize: 52)),
              const SizedBox(height: 4),
              Text(
                data.condition.label,
                style: TextStyle(fontSize: 13, color: Colors.grey.shade400),
              ),
            ],
          ),
          const SizedBox(width: 20),
          // Temperature
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  data.tempLabel,
                  style: const TextStyle(
                    fontSize: 52,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                Text(
                  data.feelsLabel,
                  style: TextStyle(fontSize: 13, color: Colors.grey.shade400),
                ),
              ],
            ),
          ),
          // City
          Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Text(
                data.city,
                style: const TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w600,
                  color: Colors.white70,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                TimeOfDay.now().format(context),
                style: TextStyle(fontSize: 12, color: Colors.grey.shade500),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

// ── Hourly strip ──────────────────────────────────────────────────────────────

class _HourlyStrip extends StatelessWidget {
  final List<HourlyWeather> hourly;
  const _HourlyStrip({required this.hourly});

  @override
  Widget build(BuildContext context) {
    if (hourly.isEmpty) return const SizedBox.shrink();
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 10),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: hourly.map((h) => _HourlyCell(h: h)).toList(),
      ),
    );
  }
}

class _HourlyCell extends StatelessWidget {
  final HourlyWeather h;
  const _HourlyCell({required this.h});

  @override
  Widget build(BuildContext context) {
    final hh = h.time.hour.toString().padLeft(2, '0');
    return Column(
      children: [
        Text(
          '$hh:00',
          style: TextStyle(fontSize: 11, color: Colors.grey.shade500),
        ),
        const SizedBox(height: 4),
        Text(h.condition.symbol, style: const TextStyle(fontSize: 20)),
        const SizedBox(height: 4),
        Text(
          '${h.tempC.round()}°',
          style: const TextStyle(
            fontSize: 14,
            color: Colors.white,
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }
}

// ── Details grid ─────────────────────────────────────────────────────────────

class _DetailsGrid extends StatelessWidget {
  final WeatherData data;
  const _DetailsGrid({required this.data});

  @override
  Widget build(BuildContext context) {
    final items = [
      (
        Icons.air,
        'Wind',
        '${data.windSpeedKmh.round()} km/h ${data.windDirLabel}',
      ),
      (Icons.water_drop, 'Humidity', '${data.humidity}%'),
      (Icons.wb_sunny_outlined, 'UV Index', data.uvIndex.round().toString()),
      (Icons.umbrella, 'Rain', '${data.precipitationPct}%'),
    ];
    return GridView.count(
      crossAxisCount: 2,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      mainAxisSpacing: 8,
      crossAxisSpacing: 8,
      childAspectRatio: 3.2,
      children: items
          .map(
            (item) =>
                _DetailTile(icon: item.$1, label: item.$2, value: item.$3),
          )
          .toList(),
    );
  }
}

class _DetailTile extends StatelessWidget {
  final IconData icon;
  final String label;
  final String value;
  const _DetailTile({
    required this.icon,
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          Icon(icon, size: 16, color: Colors.cyanAccent),
          const SizedBox(width: 8),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                label,
                style: TextStyle(fontSize: 10, color: Colors.grey.shade500),
              ),
              Text(
                value,
                style: const TextStyle(
                  fontSize: 14,
                  color: Colors.white,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

// ── Error card ────────────────────────────────────────────────────────────────

class _ErrorCard extends StatelessWidget {
  final String error;
  final VoidCallback onRetry;
  const _ErrorCard({required this.error, required this.onRetry});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.red.shade900.withValues(alpha: 0.2),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Column(
        children: [
          Text(
            error,
            style: const TextStyle(color: Colors.redAccent, fontSize: 13),
          ),
          const SizedBox(height: 8),
          ElevatedButton.icon(
            onPressed: onRetry,
            icon: const Icon(Icons.refresh, size: 14),
            label: const Text('Retry'),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.red.shade800,
            ),
          ),
        ],
      ),
    );
  }
}
