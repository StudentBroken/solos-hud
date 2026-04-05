import 'dart:async';
import 'dart:math';
import 'package:flutter/foundation.dart';
import 'package:sensors_plus/sensors_plus.dart';
import '../gps/gps_service.dart';

/// Fuses GPS Doppler speed with IMU (linear accelerometer) via a 1D Kalman
/// filter to produce smooth, low-latency speed estimates for pace display.
///
/// GPS gives accurate but low-rate (~2 Hz) speed measurements.
/// The IMU (100 Hz) drives:
///   • Process noise adaptation — high motion → higher Q → faster GPS tracking.
///   • Zero-velocity detection — snap to 0 when IMU confirms standstill.
///   • Deceleration detection — when IMU activity drops sharply relative to
///     the running-average, the filter lets GPS pull the estimate down faster.
class SpeedFusionService extends ChangeNotifier {
  // ── Kalman filter state ────────────────────────────────────────────────────
  double _v = 0.0;   // estimated speed (m/s)
  double _cov = 1.0;   // Kalman error covariance

  // ── Tuning ─────────────────────────────────────────────────────────────────
  /// Base process noise (m/s)² per IMU tick — constant-velocity model drift.
  static const _qBase = 0.006;

  /// GPS measurement noise at 1 m accuracy.  R scales with accuracy².
  static const _rBase = 0.05;
  static const _rFloor = 0.02;
  static const _rCeiling = 6.0;

  /// Running footstrikes produce 3–8 m/s² spikes in a pocket.
  /// Use the slow-LP envelope (_smoothAccelMag) rather than raw magnitude
  /// for all decisions — raw spikes are gait noise, not speed information.

  /// Consecutive IMU ticks of the LP envelope below threshold → zero-velocity.
  /// Long window because pocket vibrations can dip briefly even while running.
  static const _stillFrames = 40; // ~800 ms at ~50 Hz

  /// LP envelope below this → candidate for "still".
  /// Set well above pocket-at-rest (~0.3) but below slow-walk (~4) pocket level.
  static const _imuStillThreshold = 2.0; // m/s² envelope

  /// GPS speed below this reinforces zero-velocity detection.
  static const _gpsStillThreshold = 0.4; // m/s

  /// LP time constant for the running energy envelope.
  /// Very slow — smooths over many strides to build a stable baseline.
  static const _alphaEnergy = 0.005;

  /// When LP envelope drops below this fraction of running baseline,
  /// deceleration is sustained — boost Q to let GPS track down faster.
  /// Lower threshold = only fires on clear, sustained slowdowns.
  static const _decelerationRatio = 0.25;

  /// Q multiplier when deceleration is detected.
  static const _decelerationQBoost = 3.0;

  // ── Internal state ─────────────────────────────────────────────────────────
  double _smoothAccelMag = 0.0; // very-slow LP of accel magnitude
  double _runningEnergy  = 0.0; // baseline "running" energy reference
  int    _stillCount     = 0;
  bool   _initialized    = false;
  StreamSubscription<UserAccelerometerEvent>? _imuSub;

  // ── Public API ─────────────────────────────────────────────────────────────
  double get speedMs  => _v;
  double get speedKmh => _v * 3.6;

  /// Wire up the service.  Call once after [GpsService.init] is done.
  void start(GpsService gps) {
    if (_initialized) return;
    _initialized = true;

    gps.addListener(() => _onGps(gps.data));

    _imuSub = userAccelerometerEventStream(
      samplingPeriod: SensorInterval.normalInterval, // ~20 ms
    ).listen(_onImu, onError: (_) {});
  }

  // ── IMU step ──────────────────────────────────────────────────────────────
  void _onImu(UserAccelerometerEvent e) {
    final mag = sqrt(e.x * e.x + e.y * e.y + e.z * e.z);

    // LP envelope — smooths over many strides; footstrike spikes average out.
    // This is the only IMU signal we trust for pocket/running use.
    _smoothAccelMag = (1 - _alphaEnergy) * _smoothAccelMag + _alphaEnergy * mag;

    // Running energy baseline: high-water mark tracked only while moving.
    if (_v > 1.5) _runningEnergy = max(_runningEnergy * 0.9998, _smoothAccelMag);

    // ── Adaptive process noise (envelope-based, not raw) ─────────────────
    // Constant base Q; only boost on confirmed sustained deceleration.
    double Q = _qBase;
    if (_runningEnergy > 0.5 && _smoothAccelMag < _runningEnergy * _decelerationRatio) {
      Q *= _decelerationQBoost;
    }

    _cov = (_cov + Q).clamp(0.0, 8.0);

    // ── Zero-velocity detection (envelope-based) ──────────────────────────
    // Running pocket vibrations keep envelope high; standstill is clearly lower.
    if (_smoothAccelMag < _imuStillThreshold && _v < _gpsStillThreshold) {
      _stillCount++;
      if (_stillCount >= _stillFrames) {
        _v = 0.0;
        _cov = 0.4;
        _runningEnergy = 0.0;
        _stillCount = 0;
        notifyListeners();
      }
    } else {
      _stillCount = 0;
    }
  }

  // ── GPS correction (Kalman update) ────────────────────────────────────────
  void _onGps(GpsData pos) {
    final gpsSpeed = pos.speedMs.clamp(0.0, 20.0);

    // Measurement noise: trust GPS more when accuracy is good.
    final accuracy = (pos.accuracy ?? 8.0).clamp(1.0, 40.0);
    final R = (_rBase * accuracy * accuracy).clamp(_rFloor, _rCeiling);

    // Kalman update
    final K = _cov / (_cov + R);
    _v = (_v + K * (gpsSpeed - _v)).clamp(0.0, 20.0);
    _cov = (1.0 - K) * _cov;

    notifyListeners();
  }

  @override
  void dispose() {
    _imuSub?.cancel();
    super.dispose();
  }
}
