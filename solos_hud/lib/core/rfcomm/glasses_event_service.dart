import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import '../solos_protocol.dart';

// ── Action / Button types (from ActionType.java + ButtonType.java) ────────────

enum GlassesActionType {
  none(0),
  buttonShort(1),
  buttonLong(2),
  alSEvent(3),
  powerCharging(4),
  powerDischarging(5),
  playChirp(6),
  playListeningChime(7),
  playProcessingChime(8),
  audioBufferFull(9),
  audioBufferEmpty(10),
  buttonMake(11),
  buttonBreak(12),
  buttonRepeat(13),
  proximityEvent(14),
  tapEvent(15),
  vadSilence(18),
  vadVoice(19),
  unknown(-1);

  final int value;
  const GlassesActionType(this.value);

  static GlassesActionType fromInt(int v) => GlassesActionType.values
      .firstWhere((e) => e.value == v, orElse: () => GlassesActionType.unknown);

  bool get isButtonEvent =>
      this == buttonShort ||
      this == buttonLong ||
      this == buttonMake ||
      this == buttonBreak ||
      this == buttonRepeat;
}

enum GlassesButton {
  main(0),
  front(1),
  back(2),
  unknown(-1);

  final int code;
  const GlassesButton(this.code);

  static GlassesButton fromByte(int b) => GlassesButton.values.firstWhere(
    (e) => e.code == b,
    orElse: () => GlassesButton.unknown,
  );
}

// ── Typed event ───────────────────────────────────────────────────────────────

class GlassesEvent {
  final int packetType;
  final GlassesActionType? action;
  final GlassesButton? button;
  final int? holdTimeMs;
  final DateTime timestamp;

  GlassesEvent._({
    required this.packetType,
    this.action,
    this.button,
    this.holdTimeMs,
  }) : timestamp = DateTime.now();

  factory GlassesEvent.action(
    GlassesActionType action, {
    GlassesButton? button,
    int? holdTimeMs,
  }) {
    return GlassesEvent._(
      packetType: 2,
      action: action,
      button: button,
      holdTimeMs: holdTimeMs,
    );
  }

  factory GlassesEvent.pong() => GlassesEvent._(packetType: 34);

  factory GlassesEvent.raw(int type) => GlassesEvent._(packetType: type);

  bool get isButtonPress =>
      action?.isButtonEvent == true || action == GlassesActionType.tapEvent;

  String get description {
    if (action == null) {
      return 'Packet type=$packetType';
    }
    if (action!.isButtonEvent && button != null) {
      return '${action!.name} • ${button!.name}'
          '${holdTimeMs != null && holdTimeMs! > 0 ? ' (${holdTimeMs}ms)' : ''}';
    }
    return action!.name;
  }

  @override
  String toString() =>
      '[${timestamp.hour.toString().padLeft(2, '0')}:'
      '${timestamp.minute.toString().padLeft(2, '0')}:'
      '${timestamp.second.toString().padLeft(2, '0')}] $description';
}

// ── Service ───────────────────────────────────────────────────────────────────

/// Subscribes to the native EventChannel and exposes typed [GlassesEvent]s.
class GlassesEventService extends ChangeNotifier {
  static const _channel = EventChannel('solos_rfcomm_events');

  StreamSubscription? _sub;
  final List<GlassesEvent> _log = [];
  GlassesEvent? _lastEvent;

  // ── Battery state ─────────────────────────────────────────────────────────
  int? _batteryLevel; // 0–100, null = unknown
  bool? _isCharging; // null = unknown

  int? get batteryLevel => _batteryLevel;
  bool? get isCharging => _isCharging;

  // ── Orientation (raw signed shorts from glasses IMU) ─────────────────────
  int? _pitchRaw; // positive = tilt up
  int? _rollRaw;
  int? _yawRaw;

  // ── Display state (from STATUS_RESPONSE) ─────────────────────────────────
  int? _brightness; // 0-255, null = unknown

  int? get brightness => _brightness;
  int? get pitchRaw => _pitchRaw;
  int? get rollRaw => _rollRaw;
  int? get yawRaw => _yawRaw;

  String get batteryLabel {
    if (_batteryLevel == null) return '?';
    final pct = '$_batteryLevel%';
    if (_isCharging == true) return '$pct ⚡';
    return pct;
  }

  List<GlassesEvent> get log => List.unmodifiable(_log);
  GlassesEvent? get lastEvent => _lastEvent;

  // Fires whenever a button-type event arrives — useful for wiring up actions
  final StreamController<GlassesEvent> _buttonController =
      StreamController.broadcast();
  Stream<GlassesEvent> get buttonEvents => _buttonController.stream;

  void startListening() {
    _sub?.cancel();
    _sub = _channel.receiveBroadcastStream().listen(
      _onRawEvent,
      onError: (_) {},
      onDone: () {},
    );
  }

  void stopListening() {
    _sub?.cancel();
    _sub = null;
  }

  void _onRawEvent(dynamic raw) {
    if (raw is! Map) return;
    final type = raw['type'] as int? ?? -1;
    final payload = raw['payload'] as Uint8List? ?? Uint8List(0);

    final event = _parsePacket(type, payload);
    _log.add(event);
    if (_log.length > 200) _log.removeRange(0, _log.length - 200);
    _lastEvent = event;

    if (event.isButtonPress) {
      _buttonController.add(event);
    }
    notifyListeners();
  }

  GlassesEvent _parsePacket(int type, Uint8List payload) {
    switch (type) {
      case 2: // ACTION
        return _parseAction(payload);
      case 19: // STATUS_RESPONSE
        _parseStatusResponse(payload);
        return GlassesEvent.raw(type);
      case 34: // PONG
        return GlassesEvent.pong();
      default:
        return GlassesEvent.raw(type);
    }
  }

  GlassesEvent _parseAction(Uint8List payload) {
    if (payload.length < 4) {
      return GlassesEvent.action(GlassesActionType.unknown);
    }

    final data = ByteData.sublistView(payload);
    final actionInt = data.getInt32(0, Endian.little);
    final action = GlassesActionType.fromInt(actionInt);

    // Update charging state from proactive power events
    if (action == GlassesActionType.powerCharging) {
      _isCharging = true;
      // Don't notify yet — fall through to add to log + notify once
    } else if (action == GlassesActionType.powerDischarging) {
      _isCharging = false;
    }

    if (action.isButtonEvent && payload.length >= 7) {
      final holdTime = data.getInt16(4, Endian.little);
      final buttonByte = payload[6];
      final button = GlassesButton.fromByte(buttonByte);
      return GlassesEvent.action(
        action,
        button: button,
        holdTimeMs: holdTime.toInt(),
      );
    }

    return GlassesEvent.action(action);
  }

  void _parseStatusResponse(Uint8List payload) {
    final fields = SolosProtocol.parseStatusResponse(payload);
    bool changed = false;
    if (fields.containsKey('batteryLevel')) {
      _batteryLevel = fields['batteryLevel']!.clamp(0, 100);
      changed = true;
    }
    if (fields.containsKey('powerAvailable')) {
      _isCharging = fields['powerAvailable'] == 1;
      changed = true;
    }
    if (fields.containsKey('pitchRaw')) {
      _pitchRaw = fields['pitchRaw'];
      _rollRaw = fields['rollRaw'];
      _yawRaw = fields['yawRaw'];
      changed = true;
    }
    if (fields.containsKey('displayBrightness')) {
      _brightness = fields['displayBrightness'];
      changed = true;
    }
    // Note: statusFlagAutoBrightness (16) is not yet in scalarDescriptors
    // but we could add it there if the hardware reports it.
    if (changed) notifyListeners();
  }

  /// Inject a synthetic button event as if it came from the physical glasses.
  /// Used by the on-screen simulation buttons in the preview widget.
  void simulateButton(GlassesButton button, GlassesActionType action) {
    final event = GlassesEvent.action(action, button: button);
    _log.add(event);
    if (_log.length > 200) _log.removeRange(0, _log.length - 200);
    _lastEvent = event;
    if (event.isButtonPress) _buttonController.add(event);
    notifyListeners();
  }

  void clearLog() {
    _log.clear();
    notifyListeners();
  }

  @override
  void dispose() {
    _sub?.cancel();
    _buttonController.close();
    super.dispose();
  }
}
