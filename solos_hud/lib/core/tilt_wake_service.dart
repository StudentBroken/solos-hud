import 'dart:async';
import 'package:flutter/foundation.dart';
import 'rfcomm/rfcomm_service.dart';
import 'rfcomm/glasses_event_service.dart';
import 'settings/app_settings.dart';
import 'hud_controller.dart';
import 'solos_protocol.dart';

/// Polls the glasses for orientation every 200 ms and wakes/sleeps the HUD
/// display based on pitch.
///
/// Architecture fix: the STATUS_GET send and the result read are DECOUPLED.
/// - A periodic Timer sends STATUS_GET requests at 200 ms intervals.
/// - A listener on GlassesEventService fires whenever a STATUS_RESPONSE
///   arrives (which populates pitchRaw). That listener does the wake/sleep
///   decision — it does NOT rely on timing alignment with the sender.
///
/// Raw scale: firmware encodes orientation as signed 16-bit integers.
/// Best-guess is 100 raw units ≈ 1°. Adjust [rawPerDegree] after calibrating
/// on real hardware.
class TiltWakeService extends ChangeNotifier {
  static const _pollIntervalMs = 200;
  static const _rawPerDegree = 100;

  final RfcommService rfcomm;
  final GlassesEventService glassesEvents;
  final AppSettings settings;
  final HudController hud;

  Timer? _poller;
  bool _screenOn = false;
  int? _lastPitchRaw;
  bool _active = false;

  TiltWakeService({
    required this.rfcomm,
    required this.glassesEvents,
    required this.settings,
    required this.hud,
  }) {
    settings.addListener(_onSettingsChanged);
  }

  // ── Public state ───────────────────────────────────────────────────────────

  bool get active => _active;
  bool get screenOn => _screenOn;
  int? get lastPitchRaw => _lastPitchRaw;

  double? get pitchDeg {
    if (_lastPitchRaw == null) return null;
    return _lastPitchRaw! / _rawPerDegree;
  }

  int get _thresholdRaw => settings.tiltThresholdDeg * _rawPerDegree;

  // ── Control ────────────────────────────────────────────────────────────────

  void start() {
    if (_active) return;
    _active = true;
    _screenOn = false;
    _lastPitchRaw = null;

    // Listen for every STATUS_RESPONSE that GlassesEventService processes.
    // This fires whenever pitchRaw changes — decoupled from the send timer.
    glassesEvents.addListener(_onOrientationUpdate);

    // Blank the display immediately
    _sendBlank();

    // Kick off the periodic STATUS_GET sender
    _poller = Timer.periodic(
      const Duration(milliseconds: _pollIntervalMs),
      (_) => _sendStatusRequest(),
    );

    notifyListeners();
  }

  void stop() {
    _poller?.cancel();
    _poller = null;
    glassesEvents.removeListener(_onOrientationUpdate);
    _active = false;
    _screenOn = false;
    notifyListeners();
  }

  // ── Internal ───────────────────────────────────────────────────────────────

  void _onSettingsChanged() {
    if (settings.tiltWakeEnabled && !_active && rfcomm.isConnected) {
      start();
    } else if (!settings.tiltWakeEnabled && _active) {
      stop();
    }
  }

  /// Sends a STATUS_GET to the glasses. The response arrives asynchronously
  /// via the EventChannel and is handled in [_onOrientationUpdate].
  Future<void> _sendStatusRequest() async {
    if (!rfcomm.isConnected) return;
    await rfcomm.sendBytes(
      SolosProtocol.buildStatusGet(SolosProtocol.statusFlagTiltPoll),
    );
  }

  /// Called by GlassesEventService (via ChangeNotifier) every time a new
  /// STATUS_RESPONSE is parsed — i.e. right after pitchRaw is updated.
  void _onOrientationUpdate() {
    if (!_active) return;

    final pitch = glassesEvents.pitchRaw;
    if (pitch == null) return;

    _lastPitchRaw = pitch;

    final shouldBeOn = pitch >= _thresholdRaw;

    if (shouldBeOn && !_screenOn) {
      _wakeScreen();
    } else if (!shouldBeOn && _screenOn) {
      _sendBlank();
    } else {
      // No state change, just update pitch display in UI
      notifyListeners();
    }
  }

  Future<void> _wakeScreen() async {
    if (!rfcomm.isConnected) return;
    await rfcomm.sendBytes(SolosProtocol.buildWakeUp());
    _screenOn = true;
    notifyListeners();
  }

  Future<void> _sendBlank() async {
    if (!rfcomm.isConnected) return;
    await rfcomm.sendBytes(SolosProtocol.buildSolidColorPacket(0, 0, 0));
    _screenOn = false;
    notifyListeners();
  }

  @override
  void dispose() {
    settings.removeListener(_onSettingsChanged);
    glassesEvents.removeListener(_onOrientationUpdate);
    _poller?.cancel();
    super.dispose();
  }
}
