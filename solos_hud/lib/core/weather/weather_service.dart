import 'dart:async';
import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import '../gps/gps_service.dart';

// ── Weather condition ─────────────────────────────────────────────────────────

enum WeatherCondition {
  clear,
  mainlyClear,
  partlyCloudy,
  overcast,
  fog,
  drizzle,
  rain,
  heavyRain,
  snow,
  heavySnow,
  rainShowers,
  snowShowers,
  thunderstorm;

  /// Unicode symbol safe to render in dart:ui with the default font.
  String get symbol => switch (this) {
    WeatherCondition.clear => '☀',
    WeatherCondition.mainlyClear => '☀',
    WeatherCondition.partlyCloudy => '⛅',
    WeatherCondition.overcast => '☁',
    WeatherCondition.fog => '≡',
    WeatherCondition.drizzle => '·rain',
    WeatherCondition.rain => '☔',
    WeatherCondition.heavyRain => '☔☔',
    WeatherCondition.snow => '❄',
    WeatherCondition.heavySnow => '❄❄',
    WeatherCondition.rainShowers => '☔',
    WeatherCondition.snowShowers => '❄',
    WeatherCondition.thunderstorm => '⛈',
  };

  String get label => switch (this) {
    WeatherCondition.clear => 'Clear',
    WeatherCondition.mainlyClear => 'Mostly Clear',
    WeatherCondition.partlyCloudy => 'Partly Cloudy',
    WeatherCondition.overcast => 'Overcast',
    WeatherCondition.fog => 'Foggy',
    WeatherCondition.drizzle => 'Drizzle',
    WeatherCondition.rain => 'Rain',
    WeatherCondition.heavyRain => 'Heavy Rain',
    WeatherCondition.snow => 'Snow',
    WeatherCondition.heavySnow => 'Heavy Snow',
    WeatherCondition.rainShowers => 'Showers',
    WeatherCondition.snowShowers => 'Snow Showers',
    WeatherCondition.thunderstorm => 'Thunderstorm',
  };

  static WeatherCondition fromWmoCode(int code) => switch (code) {
    0 => WeatherCondition.clear,
    1 => WeatherCondition.mainlyClear,
    2 => WeatherCondition.partlyCloudy,
    3 => WeatherCondition.overcast,
    45 || 48 => WeatherCondition.fog,
    51 || 53 || 55 => WeatherCondition.drizzle,
    61 || 63 => WeatherCondition.rain,
    65 => WeatherCondition.heavyRain,
    71 || 73 => WeatherCondition.snow,
    75 || 77 => WeatherCondition.heavySnow,
    80 || 81 || 82 => WeatherCondition.rainShowers,
    85 || 86 => WeatherCondition.snowShowers,
    95 || 96 || 99 => WeatherCondition.thunderstorm,
    _ => WeatherCondition.partlyCloudy,
  };
}

// ── Models ────────────────────────────────────────────────────────────────────

class HourlyWeather {
  final DateTime time;
  final double tempC;
  final WeatherCondition condition;
  const HourlyWeather({
    required this.time,
    required this.tempC,
    required this.condition,
  });
}

class WeatherData {
  final double tempC;
  final double feelsLikeC;
  final WeatherCondition condition;
  final double windSpeedKmh;
  final int windDirectionDeg;
  final int humidity;
  final double uvIndex;
  final int precipitationPct;
  final String city;
  final DateTime fetchedAt;
  final List<HourlyWeather> hourly; // next 6 hours

  const WeatherData({
    required this.tempC,
    required this.feelsLikeC,
    required this.condition,
    required this.windSpeedKmh,
    required this.windDirectionDeg,
    required this.humidity,
    required this.uvIndex,
    required this.precipitationPct,
    required this.city,
    required this.fetchedAt,
    required this.hourly,
  });

  String get windDirLabel {
    const dirs = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
    return dirs[((windDirectionDeg + 22) / 45).floor() % 8];
  }

  String get tempLabel => '${tempC.round()}°C';
  String get feelsLabel => 'Feels ${feelsLikeC.round()}°C';
}

// ── Service ───────────────────────────────────────────────────────────────────

class WeatherService extends ChangeNotifier {
  static const _ttl = Duration(minutes: 10);

  final GpsService gps;
  WeatherService({required this.gps});

  WeatherData? _data;
  bool _loading = false;
  String? _error;
  Timer? _refreshTimer;

  WeatherData? get data => _data;
  bool get loading => _loading;
  String? get error => _error;
  bool get hasData => _data != null;

  void startAutoRefresh() {
    _refreshTimer?.cancel();
    _fetch();
    _refreshTimer = Timer.periodic(_ttl, (_) => _fetch());
  }

  void stopAutoRefresh() {
    _refreshTimer?.cancel();
    _refreshTimer = null;
  }

  Future<void> refresh() => _fetch();

  Future<void> _fetch() async {
    if (_loading) return;
    if (!gps.available) {
      _error = 'GPS not available';
      notifyListeners();
      return;
    }

    _loading = true;
    _error = null;
    notifyListeners();

    try {
      final lat = gps.data.latitude;
      final lng = gps.data.longitude;

      // Open-Meteo — free, no key
      final uri = Uri.parse(
        'https://api.open-meteo.com/v1/forecast'
        '?latitude=${lat.toStringAsFixed(4)}'
        '&longitude=${lng.toStringAsFixed(4)}'
        '&current=temperature_2m,apparent_temperature,weathercode'
        ',windspeed_10m,winddirection_10m,precipitation_probability'
        ',relativehumidity_2m,uv_index'
        '&hourly=temperature_2m,weathercode'
        '&forecast_days=1&timezone=auto',
      );

      final res = await http
          .get(uri, headers: {'User-Agent': 'SolosHUD/1.0'})
          .timeout(const Duration(seconds: 8));

      if (res.statusCode != 200) throw Exception('HTTP ${res.statusCode}');

      final j = jsonDecode(res.body) as Map<String, dynamic>;
      final current = j['current'] as Map<String, dynamic>;
      final hourlyJ = j['hourly'] as Map<String, dynamic>;

      // Reverse-geocode city name (Nominatim, free)
      final city = await _cityName(lat, lng);

      // Parse current
      final data = WeatherData(
        tempC: (current['temperature_2m'] as num).toDouble(),
        feelsLikeC: (current['apparent_temperature'] as num).toDouble(),
        condition: WeatherCondition.fromWmoCode(
          (current['weathercode'] as num).toInt(),
        ),
        windSpeedKmh: (current['windspeed_10m'] as num).toDouble(),
        windDirectionDeg: (current['winddirection_10m'] as num).toInt(),
        humidity: (current['relativehumidity_2m'] as num).toInt(),
        uvIndex: (current['uv_index'] as num).toDouble(),
        precipitationPct: (current['precipitation_probability'] as num? ?? 0)
            .toInt(),
        city: city,
        fetchedAt: DateTime.now(),
        hourly: _parseHourly(hourlyJ),
      );

      _data = data;
      _error = null;
    } catch (e) {
      _error = e.toString().split('\n').first;
    } finally {
      _loading = false;
      notifyListeners();
    }
  }

  List<HourlyWeather> _parseHourly(Map<String, dynamic> hourly) {
    final times = (hourly['time'] as List).cast<String>();
    final temps = (hourly['temperature_2m'] as List).cast<num>();
    final codes = (hourly['weathercode'] as List).cast<num>();
    final now = DateTime.now();
    final result = <HourlyWeather>[];

    for (int i = 0; i < times.length && result.length < 6; i++) {
      final t = DateTime.parse(times[i]);
      if (t.isAfter(now)) {
        result.add(
          HourlyWeather(
            time: t,
            tempC: temps[i].toDouble(),
            condition: WeatherCondition.fromWmoCode(codes[i].toInt()),
          ),
        );
      }
    }
    return result;
  }

  Future<String> _cityName(double lat, double lng) async {
    try {
      final uri = Uri.parse(
        'https://nominatim.openstreetmap.org/reverse'
        '?lat=${lat.toStringAsFixed(4)}&lon=${lng.toStringAsFixed(4)}&format=json',
      );
      final res = await http
          .get(uri, headers: {'User-Agent': 'SolosHUD/1.0'})
          .timeout(const Duration(seconds: 4));
      if (res.statusCode == 200) {
        final j = jsonDecode(res.body) as Map<String, dynamic>;
        final addr = j['address'] as Map<String, dynamic>? ?? {};
        return (addr['city'] as String?) ??
            (addr['town'] as String?) ??
            (addr['village'] as String?) ??
            (addr['county'] as String?) ??
            '${lat.toStringAsFixed(2)}, ${lng.toStringAsFixed(2)}';
      }
    } catch (_) {}
    return '${lat.toStringAsFixed(2)}, ${lng.toStringAsFixed(2)}';
  }

  @override
  void dispose() {
    _refreshTimer?.cancel();
    super.dispose();
  }
}
