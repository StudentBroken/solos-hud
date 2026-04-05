import 'dart:typed_data';
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/weather/weather_service.dart';
import 'weather_renderer.dart';
import 'weather_widget.dart';

class WeatherApp extends GlassesApp {
  final WeatherService weather;
  WeatherApp({required this.weather});

  @override String   get id          => 'weather';
  @override String   get name        => 'Weather';
  @override IconData get icon        => Icons.wb_cloudy_outlined;
  @override String   get description => 'Current conditions · 6-hour forecast · No API key';

  @override
  void onActivate()   => weather.startAutoRefresh();
  @override
  void onDeactivate() => weather.stopAutoRefresh();

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() async {
    if (!weather.hasData) return null;
    try {
      return await WeatherRenderer.render(weather.data!);
    } catch (_) {
      return null;
    }
  }

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      WeatherPhoneWidget(weather: weather);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (!weather.hasData) {
      return Text('No data',
          style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey));
    }
    final d = weather.data!;
    return Text(
      '${d.condition.symbol}  ${d.tempLabel}  ${d.condition.label}',
      style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.white70),
      overflow: TextOverflow.ellipsis,
    );
  }
}
