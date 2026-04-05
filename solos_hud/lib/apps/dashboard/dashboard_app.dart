import 'dart:typed_data';
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/gps/gps_service.dart';
import '../../core/hr/heart_rate_service.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/solos_protocol.dart';
import 'dashboard_renderer.dart';
import 'dashboard_widget.dart';

class DashboardApp extends GlassesApp {
  final GpsService gps;
  final HeartRateService hr;
  final GlassesEventService glassesEvents;
  final AppSettings settings;

  DashboardApp({
    required this.gps,
    required this.hr,
    required this.glassesEvents,
    required this.settings,
  });

  @override String get id => 'dashboard';
  @override String get name => 'Workout';
  @override IconData get icon => Icons.directions_run;
  @override String get description => 'HUD: speed, HR, battery, compass, time';

  @override
  int get preferredCodec => SolosProtocol.codecJPEG;

  @override void onActivate() { gps.init(); }
  @override void onDeactivate() {}

  // Dashboard always uses buildCustomFrame — buildGlassesPayload is fallback only.
  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() async {
    final cfg = _config;
    final data = DashboardData(
      time: DateTime.now(),
      speedKmh: gps.available ? gps.data.speedKmh : null,
      speedUnit: settings.speedUnit,
      heartRate: hr.heartRate,
      batteryPct: glassesEvents.batteryLevel,
      batteryCharging: glassesEvents.isCharging ?? false,
      headingDeg: gps.available ? gps.data.heading : null,
    );
    return DashboardRenderer.render(data, cfg);
  }

  DashboardConfig get _config => DashboardConfig(
        showTime: settings.getAppValue('dash_show_time', true) as bool,
        showBattery: settings.getAppValue('dash_show_battery', true) as bool,
        showSpeed: settings.getAppValue('dash_show_speed', true) as bool,
        showHr: settings.getAppValue('dash_show_hr', true) as bool,
        showCompass: settings.getAppValue('dash_show_compass', true) as bool,
        primaryMetric: settings.getAppValue('dash_primary', 'speed') as String,
      );

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      DashboardPhoneWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    final speed = gps.available ? gps.data.speedKmh.toStringAsFixed(1) : '--';
    final hrStr = hr.heartRate != null ? ' • ♥${hr.heartRate}' : '';
    return Text('$speed ${settings.speedUnit}$hrStr',
        style: Theme.of(context).textTheme.bodySmall?.copyWith(
            fontFamily: 'monospace', fontWeight: FontWeight.bold));
  }

  @override
  List<AppSettingEntry> get settingEntries => [
        const AppSettingEntry(
          key: 'dash_primary',
          label: 'Primary Metric',
          type: AppSettingType.dropdown,
          defaultValue: 'speed',
        ),
        const AppSettingEntry(
          key: 'dash_show_time',
          label: 'Show Time',
          type: AppSettingType.toggle,
          defaultValue: true,
        ),
        const AppSettingEntry(
          key: 'dash_show_battery',
          label: 'Show Battery',
          type: AppSettingType.toggle,
          defaultValue: true,
        ),
        const AppSettingEntry(
          key: 'dash_show_speed',
          label: 'Show Speed',
          type: AppSettingType.toggle,
          defaultValue: true,
        ),
        const AppSettingEntry(
          key: 'dash_show_hr',
          label: 'Show Heart Rate',
          type: AppSettingType.toggle,
          defaultValue: true,
        ),
        const AppSettingEntry(
          key: 'dash_show_compass',
          label: 'Show Compass',
          type: AppSettingType.toggle,
          defaultValue: true,
        ),
      ];
}
