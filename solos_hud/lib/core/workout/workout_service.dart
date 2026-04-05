import 'dart:async';
import 'dart:math';
import 'package:flutter/foundation.dart';
import '../gps/gps_service.dart';

enum WorkoutState { idle, active, paused, finished }

enum WorkoutType { running, cycling, walking, other }

extension WorkoutTypeLabel on WorkoutType {
  String get label => switch (this) {
        WorkoutType.running => 'Running',
        WorkoutType.cycling => 'Cycling',
        WorkoutType.walking => 'Walking',
        WorkoutType.other => 'Workout',
      };
  String get paceUnit => switch (this) {
        WorkoutType.cycling => 'km/h',
        _ => 'min/km',
      };
}

class WorkoutService extends ChangeNotifier {
  final GpsService gps;
  WorkoutService({required this.gps});

  WorkoutState _state = WorkoutState.idle;
  WorkoutType _type = WorkoutType.running;

  DateTime? _startTime;
  DateTime? _pauseStart;
  Duration _pausedTotal = Duration.zero;
  Timer? _ticker;

  double _distanceM = 0;
  GpsData? _lastGpsData;
  double _maxSpeedMs = 0;

  final List<int> _hrReadings = [];
  int _maxHr = 0;

  WorkoutState get state => _state;
  WorkoutType get type => _type;
  bool get isActive => _state == WorkoutState.active;
  bool get isPaused => _state == WorkoutState.paused;
  bool get isRunning => _state == WorkoutState.active || _state == WorkoutState.paused;

  // ── Metrics ───────────────────────────────────────────────────────────────

  Duration get elapsed {
    if (_startTime == null) return Duration.zero;
    final now = _state == WorkoutState.paused ? (_pauseStart ?? DateTime.now()) : DateTime.now();
    final raw = now.difference(_startTime!);
    return raw - _pausedTotal;
  }

  double get distanceKm => _distanceM / 1000;
  double get distanceM => _distanceM;

  double get currentSpeedKmh => gps.data.speedKmh;
  double get currentSpeedMs => gps.data.speedMs;

  double get avgSpeedKmh {
    final secs = elapsed.inSeconds;
    if (secs == 0) return 0;
    return (_distanceM / secs) * 3.6;
  }

  /// Pace in seconds per km (for running/walking)
  double get paceSecPerKm {
    if (_distanceM < 10) return 0;
    return elapsed.inSeconds / (_distanceM / 1000);
  }

  String get paceLabel {
    if (_type == WorkoutType.cycling) {
      return '${avgSpeedKmh.toStringAsFixed(1)} km/h';
    }
    final secs = paceSecPerKm;
    if (secs <= 0) return '--:--';
    final m = (secs ~/ 60).toString().padLeft(2, '0');
    final s = (secs % 60).round().toString().padLeft(2, '0');
    return '$m:$s /km';
  }

  double get maxSpeedKmh => _maxSpeedMs * 3.6;

  // ── Goal pace ─────────────────────────────────────────────────────────────

  double _goalPaceSecPerKm = 0; // 0 = disabled

  double get goalPaceSecPerKm => _goalPaceSecPerKm;

  void setGoalPace(double secPerKm) {
    _goalPaceSecPerKm = secPerKm;
    notifyListeners();
  }

  /// Instantaneous pace from current GPS speed (not avg).
  double get currentLivePaceSecPerKm {
    final spd = gps.data.speedMs;
    if (spd < 0.2) return 0;
    return 1000.0 / spd;
  }

  String get currentLivePaceLabel {
    if (_type == WorkoutType.cycling) {
      return '${gps.data.speedKmh.toStringAsFixed(1)} km/h';
    }
    final secs = currentLivePaceSecPerKm;
    if (secs <= 0) return '--:--';
    final m = (secs ~/ 60).toString().padLeft(2, '0');
    final s = (secs % 60).round().toString().padLeft(2, '0');
    return '$m:$s /km';
  }

  int get avgHr =>
      _hrReadings.isEmpty ? 0 : _hrReadings.reduce((a, b) => a + b) ~/ _hrReadings.length;
  int get maxHr => _maxHr;

  /// Rough calorie estimate (Keytel formula using HR, assumes 70kg 30yo male by default)
  double get estimatedCalories {
    if (_hrReadings.isEmpty || elapsed.inMinutes == 0) return 0;
    final hr = avgHr.toDouble();
    final mins = elapsed.inSeconds / 60.0;
    // Simplified gender-neutral estimate
    return (((-55.0969 + (0.6309 * hr) + (0.1988 * 70) + (0.2017 * 30)) / 4.184) * mins)
        .clamp(0, double.infinity);
  }

  String get elapsedLabel {
    final e = elapsed;
    final h = e.inHours.toString().padLeft(2, '0');
    final m = e.inMinutes.remainder(60).toString().padLeft(2, '0');
    final s = e.inSeconds.remainder(60).toString().padLeft(2, '0');
    return e.inHours > 0 ? '$h:$m:$s' : '$m:$s';
  }

  // ── HR zone (based on 220 - age, using 30 as default) ────────────────────
  int get hrZone {
    final hr = _hrReadings.isEmpty ? 0 : _hrReadings.last;
    if (hr == 0) return 0;
    const maxHrEst = 190;
    final pct = hr / maxHrEst;
    if (pct < 0.60) return 1;
    if (pct < 0.70) return 2;
    if (pct < 0.80) return 3;
    if (pct < 0.90) return 4;
    return 5;
  }

  // ── Controls ──────────────────────────────────────────────────────────────

  void setType(WorkoutType type) {
    _type = type;
    notifyListeners();
  }

  void start() {
    if (_state != WorkoutState.idle) return;
    _startTime = DateTime.now();
    _pausedTotal = Duration.zero;
    _distanceM = 0;
    _lastGpsData = null;
    _hrReadings.clear();
    _maxHr = 0;
    _maxSpeedMs = 0;
    _state = WorkoutState.active;
    _ticker = Timer.periodic(const Duration(seconds: 1), _onTick);
    notifyListeners();
  }

  void pause() {
    if (_state != WorkoutState.active) return;
    _pauseStart = DateTime.now();
    _state = WorkoutState.paused;
    notifyListeners();
  }

  void resume() {
    if (_state != WorkoutState.paused) return;
    if (_pauseStart != null) {
      _pausedTotal += DateTime.now().difference(_pauseStart!);
      _pauseStart = null;
    }
    _state = WorkoutState.active;
    notifyListeners();
  }

  void stop() {
    if (_state == WorkoutState.idle) return;
    _ticker?.cancel();
    if (_state == WorkoutState.paused && _pauseStart != null) {
      _pausedTotal += DateTime.now().difference(_pauseStart!);
    }
    _state = WorkoutState.finished;
    notifyListeners();
  }

  void reset() {
    _ticker?.cancel();
    _state = WorkoutState.idle;
    _startTime = null;
    _pauseStart = null;
    _pausedTotal = Duration.zero;
    _distanceM = 0;
    _lastGpsData = null;
    _hrReadings.clear();
    _maxHr = 0;
    _maxSpeedMs = 0;
    notifyListeners();
  }

  void recordHr(int hr) {
    if (_state != WorkoutState.active || hr <= 0) return;
    _hrReadings.add(hr);
    if (hr > _maxHr) _maxHr = hr;
    notifyListeners();
  }

  void _onTick(Timer _) {
    if (_state != WorkoutState.active) return;
    final current = gps.data;
    if (_lastGpsData != null && current.speedMs > 0.5) {
      // Haversine distance
      _distanceM += _haversine(_lastGpsData!, current);
    }
    if (current.speedMs > _maxSpeedMs) _maxSpeedMs = current.speedMs;
    _lastGpsData = current;
    notifyListeners();
  }

  double _haversine(GpsData a, GpsData b) {
    const r = 6371000.0;
    final lat1 = a.latitude * pi / 180;
    final lat2 = b.latitude * pi / 180;
    final dLat = (b.latitude - a.latitude) * pi / 180;
    final dLon = (b.longitude - a.longitude) * pi / 180;
    final x = sin(dLat / 2) * sin(dLat / 2) +
        cos(lat1) * cos(lat2) * sin(dLon / 2) * sin(dLon / 2);
    return r * 2 * atan2(sqrt(x), sqrt(1 - x));
  }

  @override
  void dispose() {
    _ticker?.cancel();
    super.dispose();
  }
}
