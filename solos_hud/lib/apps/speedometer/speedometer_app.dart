import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/gps/gps_service.dart';
import '../../core/settings/app_settings.dart';
import 'speedometer_widget.dart';

class SpeedometerApp extends GlassesApp {
  final GpsService gps;
  final AppSettings settings;

  SpeedometerApp({required this.gps, required this.settings});

  @override
  String get id => 'speedometer';

  @override
  String get name => 'Speedometer';

  @override
  IconData get icon => Icons.speed;

  @override
  String get description => 'GPS speed displayed on glasses HUD';

  @override
  void onActivate() {
    // init() is idempotent — safe to call multiple times
    gps.init();
  }

  @override
  void onDeactivate() {}

  @override
  String? buildGlassesPayload() {
    switch (gps.status) {
      case GpsStatus.uninitialized:
      case GpsStatus.requesting:
        return 'GPS\nstarting...';
      case GpsStatus.denied:
        return 'GPS\ndenied';
      case GpsStatus.disabled:
        return 'GPS\noff';
      case GpsStatus.available:
        final speed = gps.data.speedIn(settings.speedUnit);
        final unit = settings.speedUnit;
        return '${speed.toStringAsFixed(1)}\n$unit';
    }
  }

  @override
  Widget buildPhoneWidget(BuildContext context) => SpeedometerWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    if (!gps.available) {
      return Text(
        gps.status.name,
        style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey),
      );
    }
    final speed = gps.data.speedIn(settings.speedUnit);
    return Text(
      '${speed.toStringAsFixed(1)} ${settings.speedUnit}',
      style: Theme.of(context)
          .textTheme
          .bodySmall
          ?.copyWith(fontFamily: 'monospace', fontWeight: FontWeight.bold),
    );
  }

  @override
  List<AppSettingEntry> get settingEntries => [
        const AppSettingEntry(
          key: 'speedometer_show_heading',
          label: 'Show Heading on Glasses',
          type: AppSettingType.toggle,
          defaultValue: false,
        ),
        const AppSettingEntry(
          key: 'speedometer_show_altitude',
          label: 'Show Altitude on Glasses',
          type: AppSettingType.toggle,
          defaultValue: false,
        ),
      ];
}
