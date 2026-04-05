import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import '../bluetooth/ble_logger.dart';
import '../solos_protocol.dart';

enum RfcommState { disconnected, connecting, connected }

class PairedDevice {
  final String name;
  final String address;
  const PairedDevice({required this.name, required this.address});

  bool get isSolos => name.toLowerCase().contains('solos');
}

/// Manages a Classic Bluetooth RFCOMM connection to the Solos glasses.
class RfcommService extends ChangeNotifier {
  static const _channel = MethodChannel('solos_rfcomm');

  final BleLogger logger;
  RfcommService({required this.logger});

  RfcommState _state = RfcommState.disconnected;
  PairedDevice? _device;
  List<PairedDevice> _pairedDevices = [];
  Timer? _batteryPollTimer;

  /// Whether the last disconnect was user-initiated (vs unexpected drop).
  bool _userDisconnected = false;
  bool get wasUserDisconnected => _userDisconnected;

  RfcommState get state => _state;
  PairedDevice? get device => _device;
  bool get isConnected => _state == RfcommState.connected;
  List<PairedDevice> get pairedDevices => List.unmodifiable(_pairedDevices);

  // ── Discovery ─────────────────────────────────────────────────────────────

  Future<void> loadPairedDevices() async {
    try {
      final raw = await _channel.invokeMethod<List>('getPairedDevices');
      _pairedDevices = (raw ?? [])
          .cast<Map>()
          .map(
            (m) => PairedDevice(
              name: m['name'] as String? ?? '',
              address: m['address'] as String? ?? '',
            ),
          )
          .toList();
      logger.log(
        BleLogDirection.info,
        'Paired devices: ${_pairedDevices.map((d) => d.name).join(", ")}',
      );
      notifyListeners();
    } catch (e) {
      logger.log(BleLogDirection.error, 'getPairedDevices failed: $e');
    }
  }

  /// Connect to the last known device by address first (fast path),
  /// then fall back to scanning for any paired "Solos" device.
  Future<bool> autoConnectSolos({String lastAddress = ''}) async {
    // Don't start a new attempt while one is already in progress.
    if (_state == RfcommState.connecting) {
      logger.log(BleLogDirection.info, 'Auto-connect: already connecting — skipped');
      return false;
    }
    await loadPairedDevices();

    // Fast path — reconnect to exact last known device
    if (lastAddress.isNotEmpty) {
      final last = _pairedDevices.firstWhere(
        (d) => d.address.toUpperCase() == lastAddress.toUpperCase(),
        orElse: () => const PairedDevice(name: '', address: ''),
      );
      if (last.address.isNotEmpty) {
        logger.log(BleLogDirection.info,
            'Auto-connect: trying last device ${last.name} ($lastAddress)');
        await connect(last);
        if (isConnected) return true;
        logger.log(BleLogDirection.info, 'Last device failed — trying scan');
      }
    }

    // Fallback — first paired device whose name contains "solos"
    final solosList = _pairedDevices.where((d) => d.isSolos).toList();
    if (solosList.isEmpty) {
      logger.log(BleLogDirection.info, 'Auto-connect: no Solos device found');
      return false;
    }
    logger.log(BleLogDirection.info,
        'Auto-connect: found ${solosList.first.name} (${solosList.first.address})');
    await connect(solosList.first);
    return isConnected;
  }

  // ── Connection ────────────────────────────────────────────────────────────

  Future<void> connect(PairedDevice device) async {
    if (isConnected) await disconnect();
    _setState(RfcommState.connecting);
    logger.log(
      BleLogDirection.info,
      'Connecting to ${device.name} (${device.address})…',
    );

    try {
      _userDisconnected = false;
      await _channel.invokeMethod('connect', {'address': device.address});
      _device = device;
      _setState(RfcommState.connected);
      logger.log(BleLogDirection.info, 'Connected to ${device.name}');

      // Request battery immediately, then poll every 30 s
      await requestBattery();
      _batteryPollTimer = Timer.periodic(
        const Duration(seconds: 30),
        (_) => requestBattery(),
      );
    } on PlatformException catch (e) {
      logger.log(BleLogDirection.error, 'Connect failed: ${e.message}');
      // "Superseded" means the Kotlin side already has a newer connection in
      // flight — do NOT set disconnected, that would trigger a third reconnect
      // attempt and start the infinite cycle. Just go back to disconnected
      // state quietly and let the existing attempt complete.
      if (e.message?.contains('Superseded') == true ||
          e.message?.contains('Already connecting') == true) {
        logger.log(BleLogDirection.info, 'Connect superseded — waiting for in-flight attempt');
        _setState(RfcommState.disconnected);
        return; // don't propagate — caller won't schedule another reconnect
      }
      _setState(RfcommState.disconnected);
    }
  }

  Future<void> disconnect() async {
    _userDisconnected = true;
    _batteryPollTimer?.cancel();
    _batteryPollTimer = null;
    try {
      await _channel.invokeMethod('disconnect');
    } catch (_) {}
    _device = null;
    _setState(RfcommState.disconnected);
    logger.log(BleLogDirection.info, 'Disconnected');
  }

  // ── Sending ───────────────────────────────────────────────────────────────

  Future<bool> sendBytes(Uint8List bytes) async {
    if (!isConnected) {
      logger.log(BleLogDirection.error, 'Cannot send — not connected');
      return false;
    }
    try {
      await _channel.invokeMethod('send', {'bytes': bytes});
      logger.log(BleLogDirection.sent, 'TX ${bytes.length} bytes');
      return true;
    } on PlatformException catch (e) {
      logger.log(BleLogDirection.error, 'Send failed: ${e.message}');
      _device = null;
      _batteryPollTimer?.cancel();
      _batteryPollTimer = null;
      _userDisconnected = false; // unexpected drop → eligible for auto-reconnect
      _setState(RfcommState.disconnected);
      return false;
    }
  }

  /// Ask the glasses for battery level + charging state.
  Future<void> requestBattery() async {
    if (!isConnected) return;
    logger.log(BleLogDirection.info, 'Requesting battery status…');
    await sendBytes(SolosProtocol.buildStatusGet());
  }

  /// Set display brightness (0-255).
  Future<void> setBrightness(int value) async {
    if (!isConnected) return;
    logger.log(BleLogDirection.info, 'Setting brightness to $value…');
    await sendBytes(
      SolosProtocol.buildStatusSet(
        SolosProtocol.statusFlagDisplayBrightness,
        value,
      ),
    );
  }

  void _setState(RfcommState s) {
    _state = s;
    notifyListeners();
  }
}
