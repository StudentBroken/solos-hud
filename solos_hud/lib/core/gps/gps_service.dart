import 'dart:async';
import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';

class GpsData {
  final double speedMs;
  final double latitude;
  final double longitude;
  final double? altitude;
  final double? heading;
  final double? accuracy;
  final DateTime timestamp;

  GpsData({
    required this.speedMs,
    required this.latitude,
    required this.longitude,
    this.altitude,
    this.heading,
    this.accuracy,
    required this.timestamp,
  });

  double get speedKmh => speedMs * 3.6;
  double get speedMph => speedMs * 2.23694;

  double speedIn(String unit) {
    switch (unit) {
      case 'mph':
        return speedMph;
      case 'm/s':
        return speedMs;
      default:
        return speedKmh;
    }
  }

  static GpsData zero() => GpsData(
        speedMs: 0,
        latitude: 0,
        longitude: 0,
        timestamp: DateTime.now(),
      );
}

enum GpsStatus { uninitialized, requesting, available, denied, disabled }

class GpsService extends ChangeNotifier {
  GpsData _data = GpsData.zero();
  GpsStatus _status = GpsStatus.uninitialized;
  String? _error;
  StreamSubscription<Position>? _sub;

  GpsData get data => _data;
  GpsStatus get status => _status;
  bool get available => _status == GpsStatus.available;
  String? get error => _error;

  Future<void> init() async {
    if (_status == GpsStatus.available) return; // already running

    _status = GpsStatus.requesting;
    notifyListeners();

    // Check location service
    if (!await Geolocator.isLocationServiceEnabled()) {
      _status = GpsStatus.disabled;
      _error = 'Location services are disabled on this device';
      notifyListeners();
      return;
    }

    // Check / request permission
    var permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
    }
    if (permission == LocationPermission.denied ||
        permission == LocationPermission.deniedForever) {
      _status = GpsStatus.denied;
      _error = permission == LocationPermission.deniedForever
          ? 'Location permission permanently denied — enable in app settings'
          : 'Location permission denied';
      notifyListeners();
      return;
    }

    _status = GpsStatus.available;
    _error = null;
    notifyListeners();
    _startStream();
  }

  void _startStream() {
    _sub?.cancel();

    final LocationSettings locationSettings = Platform.isAndroid
        ? AndroidSettings(
            accuracy: LocationAccuracy.bestForNavigation,
            distanceFilter: 0,
            intervalDuration: const Duration(milliseconds: 500),
            forceLocationManager: false,
          )
        : const LocationSettings(
            accuracy: LocationAccuracy.bestForNavigation,
            distanceFilter: 0,
          );

    _sub = Geolocator.getPositionStream(locationSettings: locationSettings).listen(
      (pos) {
        _data = GpsData(
          speedMs: pos.speed < 0 ? 0 : pos.speed,
          latitude: pos.latitude,
          longitude: pos.longitude,
          altitude: pos.altitude,
          heading: pos.heading,
          accuracy: pos.accuracy,
          timestamp: pos.timestamp,
        );
        notifyListeners();
      },
      onError: (e) {
        _error = e.toString();
        notifyListeners();
      },
    );
  }

  /// Retry initialisation (e.g. after user grants permission manually).
  Future<void> retry() async {
    _status = GpsStatus.uninitialized;
    await init();
  }

  @override
  void dispose() {
    _sub?.cancel();
    super.dispose();
  }
}
