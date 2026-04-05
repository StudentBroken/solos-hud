import 'dart:async';
import 'dart:typed_data';
import 'package:flutter/foundation.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'vesc_packet.dart';
import '../settings/app_settings.dart';

// Nordic UART Service (NUS) — used by VESC BLE bridge
const _nusService = '6e400001-b5a3-f393-e0a9-e50e24dcca9e';
const _nusRx = '6e400002-b5a3-f393-e0a9-e50e24dcca9e'; // phone → VESC
const _nusTx = '6e400003-b5a3-f393-e0a9-e50e24dcca9e'; // VESC  → phone

enum VescState { disconnected, scanning, connecting, connected }

class VescService extends ChangeNotifier {
  final AppSettings settings;
  VescService({required this.settings});

  VescState _state = VescState.disconnected;
  VescValues? _values;
  String? _error;
  String? _deviceName;

  // BLE internals
  BluetoothDevice? _device;
  BluetoothCharacteristic? _rx;
  StreamSubscription? _notifySub;
  StreamSubscription? _stateSub;
  Timer? _pollTimer;

  VescState get state => _state;
  VescValues? get values => _values;
  String? get error => _error;
  String? get deviceName => _deviceName;
  bool get isConnected => _state == VescState.connected;
  bool get scanning => _state == VescState.scanning;

  // Delegate settings to AppSettings
  int get motorPoles => settings.vescMotorPoles;
  set motorPoles(int v) => settings.setVescMotorPoles(v);

  double get wheelDiameterMm => settings.vescWheelDiameter;
  set wheelDiameterMm(double v) => settings.setVescWheelDiameter(v);

  void notifyChange() => notifyListeners();

  // ── Scan & connect ────────────────────────────────────────────────────────

  Future<void> scan() async {
    if (_state != VescState.disconnected) return;
    _setState(VescState.scanning);
    _error = null;
    notifyListeners();

    try {
      await FlutterBluePlus.startScan(
        withServices: [Guid(_nusService)],
        timeout: const Duration(seconds: 8),
      );

      await for (final results in FlutterBluePlus.scanResults) {
        if (results.isNotEmpty) {
          await FlutterBluePlus.stopScan();
          await _connectTo(results.first.device);
          return;
        }
      }
      _error = 'No VESC found — make sure BLE UART is enabled on the VESC';
    } catch (e) {
      _error = e.toString().split('\n').first;
    }
    _setState(VescState.disconnected);
  }

  Future<void> connectToDevice(BluetoothDevice device) async {
    await FlutterBluePlus.stopScan();
    await _connectTo(device);
  }

  Future<void> _connectTo(BluetoothDevice device) async {
    _setState(VescState.connecting);
    try {
      await device.connect(timeout: const Duration(seconds: 8));
      _device = device;
      _deviceName = device.platformName.isNotEmpty
          ? device.platformName
          : device.remoteId.str;

      // Request larger MTU for better performance with VESC data
      try {
        await device.requestMtu(247);
      } catch (_) {}

      // Discover NUS characteristics
      await Future.delayed(const Duration(milliseconds: 200));
      final services = await device.discoverServices();
      BluetoothCharacteristic? tx;
      for (final s in services) {
        if (s.serviceUuid == Guid(_nusService)) {
          for (final c in s.characteristics) {
            if (c.characteristicUuid == Guid(_nusTx)) tx = c;
            if (c.characteristicUuid == Guid(_nusRx)) _rx = c;
          }
        }
      }

      if (tx == null || _rx == null) {
        throw Exception('NUS characteristics not found');
      }

      // Subscribe to TX notifications
      await tx.setNotifyValue(true);
      _notifySub = tx.onValueReceived.listen(_onData);

      // Watch for unexpected disconnection
      _stateSub = device.connectionState.listen((s) {
        if (s == BluetoothConnectionState.disconnected && isConnected) {
          _onUnexpectedDisconnect();
        }
      });

      _setState(VescState.connected);

      // Poll every 200 ms
      _pollTimer = Timer.periodic(
        const Duration(milliseconds: 200),
        (_) => _poll(),
      );
    } catch (e) {
      _error = e.toString().split('\n').first;
      await _cleanup();
      _setState(VescState.disconnected);
    }
  }

  // ── Communication ─────────────────────────────────────────────────────────

  void _poll() {
    if (!isConnected || _rx == null) return;
    _rx!
        .write(VescPacket.buildGetValues(), withoutResponse: true)
        .catchError((_) {});
  }

  final _rxBuffer = BytesBuilder();

  void _onData(List<int> data) {
    _rxBuffer.add(data);
    _processBuffer();
  }

  void _processBuffer() {
    while (_rxBuffer.length >= 5) {
      final bytes = _rxBuffer.toBytes();
      int? expectedLen;
      int payloadStart;

      if (bytes[0] == 0x02) {
        expectedLen = bytes[1];
        payloadStart = 2;
      } else if (bytes[0] == 0x03) {
        if (bytes.length < 6) return; // Wait for full header
        expectedLen = (bytes[1] << 8) | bytes[2];
        payloadStart = 3;
      } else {
        // Invalid start byte, skip until we find 0x02 or 0x03
        final buf = _rxBuffer.takeBytes();
        int? nextStart;
        for (int i = 1; i < buf.length; i++) {
          if (buf[i] == 0x02 || buf[i] == 0x03) {
            nextStart = i;
            break;
          }
        }
        if (nextStart != null) {
          _rxBuffer.add(buf.sublist(nextStart));
          continue;
        }
        return;
      }

      final totalFrameLen =
          payloadStart +
          expectedLen +
          3; // head + len + payload + crc(2) + tail(1)
      if (bytes.length < totalFrameLen) return; // Wait for more data

      final frame = bytes.sublist(0, totalFrameLen);
      // Consume frame from buffer
      final allBytes = _rxBuffer.takeBytes();
      if (allBytes.length > totalFrameLen) {
        _rxBuffer.add(allBytes.sublist(totalFrameLen));
      }

      final parsed = VescPacket.parse(Uint8List.fromList(frame));
      if (parsed != null) {
        _values = parsed;
        notifyListeners();
      }
    }
  }

  // ── Disconnect ────────────────────────────────────────────────────────────

  Future<void> disconnect() async {
    await _cleanup();
    _setState(VescState.disconnected);
  }

  void _onUnexpectedDisconnect() {
    _cleanup();
    _error = 'Connection lost';
    _setState(VescState.disconnected);
  }

  Future<void> _cleanup() async {
    _pollTimer?.cancel();
    _pollTimer = null;
    _notifySub?.cancel();
    _notifySub = null;
    _stateSub?.cancel();
    _stateSub = null;
    try {
      await _device?.disconnect();
    } catch (_) {}
    _device = null;
    _rx = null;
    _rxBuffer.clear();
  }

  void _setState(VescState s) {
    _state = s;
    notifyListeners();
  }

  @override
  void dispose() {
    _cleanup();
    super.dispose();
  }
}
