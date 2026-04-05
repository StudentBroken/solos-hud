import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter_compass/flutter_compass.dart';

/// Provides a fused heading value suitable for map rotation.
///
/// Strategy:
///   • When GPS speed > [_gpsThresholdKmh], use GPS course (more stable at speed)
///   • Otherwise use the magnetometer compass (works stationary)
///   • Applies a simple low-pass filter to smooth jitter
class CompassService extends ChangeNotifier {
  static const double _gpsThresholdKmh = 5.0;
  static const double _alpha            = 0.15; // low-pass weight (0 = frozen, 1 = raw)

  StreamSubscription<CompassEvent>? _sub;

  double _compassRaw    = 0; // latest raw compass reading
  double _heading       = 0; // smoothed fused heading (0–360°, 0 = north)
  double _gpsHeading    = 0;
  double _gpsSpeedKmh   = 0;
  bool   _hasCompass    = false;
  bool   _disposed      = false;

  /// Smoothed heading in degrees (0 = north, 90 = east, clockwise).
  double get heading => _heading;

  /// Whether the compass sensor is available on this device.
  bool get hasCompass => _hasCompass;

  void start() {
    _sub?.cancel();
    _sub = FlutterCompass.events?.listen(_onCompassEvent);
    _hasCompass = FlutterCompass.events != null;
  }

  void stop() {
    _sub?.cancel();
    _sub = null;
  }

  /// Call this every time a new GPS position arrives.
  void updateGps({required double speedKmh, required double? gpsHeadingDeg}) {
    _gpsSpeedKmh = speedKmh;
    if (gpsHeadingDeg != null && gpsHeadingDeg >= 0) {
      _gpsHeading = gpsHeadingDeg;
    }
    _fuse();
  }

  void _onCompassEvent(CompassEvent event) {
    if (_disposed) return;
    final raw = event.heading;
    if (raw == null || raw.isNaN) return;
    _compassRaw = ((raw % 360) + 360) % 360;
    _fuse();
  }

  void _fuse() {
    // Choose source: GPS course when moving fast, compass otherwise
    final source = _gpsSpeedKmh >= _gpsThresholdKmh ? _gpsHeading : _compassRaw;

    // Low-pass filter with shortest-angle wrapping
    final diff = _angleDiff(source, _heading);
    _heading = ((_heading + _alpha * diff) % 360 + 360) % 360;

    if (!_disposed) notifyListeners();
  }

  /// Signed angular difference from [current] to [target] in [-180, 180].
  static double _angleDiff(double target, double current) {
    double d = (target - current) % 360;
    if (d > 180) d -= 360;
    if (d < -180) d += 360;
    return d;
  }

  @override
  void dispose() {
    _disposed = true;
    _sub?.cancel();
    super.dispose();
  }
}
