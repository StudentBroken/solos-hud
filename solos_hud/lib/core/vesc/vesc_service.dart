import 'dart:async';
import 'dart:typed_data';
import 'package:flutter/foundation.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'vesc_packet.dart';
import '../settings/app_settings.dart';

// Nordic UART Service (NUS) — used by VESC BLE bridge
const _nusService = '6e400001-b5a3-f393-e0a9-e50e24dcca9e';
const _nusRx      = '6e400002-b5a3-f393-e0a9-e50e24dcca9e'; // phone → VESC
const _nusTx      = '6e400003-b5a3-f393-e0a9-e50e24dcca9e'; // VESC  → phone

enum VescState { disconnected, scanning, connecting, reconnecting, connected }

class VescService extends ChangeNotifier {
  final AppSettings settings;
  VescService({required this.settings});

  VescState  _state       = VescState.disconnected;
  VescValues? _values;
  String?     _error;
  String?     _deviceName;

  // BLE handles
  BluetoothDevice?           _device;
  BluetoothCharacteristic?   _rx;
  StreamSubscription<List<int>>? _notifySub;
  StreamSubscription<BluetoothConnectionState>? _stateSub;

  // Request-response loop
  Timer? _watchdog;       // fires if VESC goes quiet for too long
  bool   _waitingForReply = false;
  static const _watchdogMs = 350;   // resend request if no reply in 350 ms
  static const _minIntervalMs = 60; // don't spam faster than 60 ms

  // Auto-reconnect
  bool    _userDisconnected  = false;
  int     _reconnectAttempts = 0;
  static const _maxReconnectAttempts = 20;
  static const List<int> _backoffMs = [500, 1000, 2000, 4000, 8000];

  // ── Public getters ────────────────────────────────────────────────────────
  VescState   get state       => _state;
  VescValues? get values      => _values;
  String?     get error       => _error;
  String?     get deviceName  => _deviceName;
  bool        get isConnected => _state == VescState.connected;
  bool        get scanning    => _state == VescState.scanning;

  int    get motorPoles      => settings.vescMotorPoles;
  set    motorPoles(int v)   => settings.setVescMotorPoles(v);
  double get wheelDiameterMm => settings.vescWheelDiameter;
  set    wheelDiameterMm(double v) => settings.setVescWheelDiameter(v);

  void notifyChange() => notifyListeners();

  // ── Scan & connect ────────────────────────────────────────────────────────

  Future<void> scan() async {
    if (_state != VescState.disconnected) return;
    _userDisconnected  = false;
    _reconnectAttempts = 0;
    _setState(VescState.scanning);
    _error = null;
    notifyListeners();

    try {
      await FlutterBluePlus.startScan(
        withServices: [Guid(_nusService)],
        timeout: const Duration(seconds: 10),
      );

      await for (final results in FlutterBluePlus.scanResults) {
        if (results.isNotEmpty) {
          await FlutterBluePlus.stopScan();
          await _connectTo(results.first.device);
          return;
        }
      }
      _error = 'No VESC found — enable BLE UART on the VESC';
    } catch (e) {
      _error = e.toString().split('\n').first;
    }
    _setState(VescState.disconnected);
  }

  Future<void> connectToDevice(BluetoothDevice device) async {
    _userDisconnected  = false;
    _reconnectAttempts = 0;
    await FlutterBluePlus.stopScan();
    await _connectTo(device);
  }

  Future<void> _connectTo(BluetoothDevice device) async {
    _setState(VescState.connecting);
    try {
      await device.connect(
        timeout: const Duration(seconds: 10),
        autoConnect: false,
      );
      _device     = device;
      _deviceName = device.platformName.isNotEmpty
          ? device.platformName
          : device.remoteId.str;

      // Larger MTU = fewer packets needed per GET_VALUES response
      try { await device.requestMtu(247); } catch (_) {}

      // Small settle delay after connect before service discovery
      await Future.delayed(const Duration(milliseconds: 300));
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
      if (tx == null || _rx == null) throw Exception('NUS characteristics not found');

      // Subscribe to notifications from VESC
      await tx.setNotifyValue(true);
      _notifySub = tx.onValueReceived.listen(_onData);

      // Watch for drops
      _stateSub = device.connectionState.listen((s) {
        if (s == BluetoothConnectionState.disconnected && isConnected) {
          _onUnexpectedDisconnect();
        }
      });

      _reconnectAttempts = 0;
      _error = null;
      _setState(VescState.connected);

      // Kick off the request→response polling loop
      _sendRequest();

    } catch (e) {
      _error = e.toString().split('\n').first;
      await _teardown();
      // If this was a reconnect attempt, schedule next try
      if (!_userDisconnected && _reconnectAttempts > 0) {
        _scheduleReconnect();
      } else {
        _setState(VescState.disconnected);
      }
    }
  }

  // ── Request → response loop ───────────────────────────────────────────────
  //
  // Instead of a fixed timer, we send a new GET_VALUES the moment we
  // receive and parse the previous one (capped at _minIntervalMs).
  // A watchdog fires if the VESC goes silent for _watchdogMs — it
  // re-sends the request so we never get stuck waiting.

  void _sendRequest() {
    if (!isConnected || _rx == null) return;
    _waitingForReply = true;
    _rx!.write(VescPacket.buildGetValues(), withoutResponse: true)
        .catchError((_) {});
    // Watchdog: if no reply arrives within _watchdogMs, re-send
    _watchdog?.cancel();
    _watchdog = Timer(
      const Duration(milliseconds: _watchdogMs),
      () {
        if (isConnected && _waitingForReply) {
          _waitingForReply = false;
          _sendRequest();
        }
      },
    );
  }

  final _rxBuffer = BytesBuilder();

  void _onData(List<int> data) {
    _rxBuffer.add(data);
    _processBuffer();
  }

  void _processBuffer() {
    while (_rxBuffer.length >= 5) {
      final bytes = _rxBuffer.toBytes();
      int expectedLen;
      int payloadStart;

      if (bytes[0] == 0x02) {
        expectedLen  = bytes[1];
        payloadStart = 2;
      } else if (bytes[0] == 0x03) {
        if (bytes.length < 6) return;
        expectedLen  = (bytes[1] << 8) | bytes[2];
        payloadStart = 3;
      } else {
        // Corrupt byte — scan forward for next frame header
        final buf = _rxBuffer.takeBytes();
        final next = buf.indexWhere(
            (b) => b == 0x02 || b == 0x03, 1);
        if (next > 0) _rxBuffer.add(buf.sublist(next));
        return;
      }

      // 3 = CRC-hi + CRC-lo + end-byte (0x03)
      final frameLen = payloadStart + expectedLen + 3;
      if (bytes.length < frameLen) return; // Wait for more BLE packets

      final frame   = bytes.sublist(0, frameLen);
      final allBytes = _rxBuffer.takeBytes();
      if (allBytes.length > frameLen) {
        _rxBuffer.add(allBytes.sublist(frameLen));
      }

      final parsed = VescPacket.parse(Uint8List.fromList(frame));
      if (parsed != null) {
        _values = parsed;
        notifyListeners();
      }

      // Schedule next request — tiny gap so we don't flood
      if (isConnected) {
        _watchdog?.cancel();
        _waitingForReply = false;
        Future.delayed(const Duration(milliseconds: _minIntervalMs), _sendRequest);
      }
    }
  }

  // ── Reconnection ──────────────────────────────────────────────────────────

  void _onUnexpectedDisconnect() {
    debugPrint('[VESC] Unexpected disconnect — will reconnect');
    _teardown();
    _error = 'Connection lost — reconnecting…';
    if (!_userDisconnected) {
      _reconnectAttempts = 0;
      _setState(VescState.reconnecting);
      _scheduleReconnect();
    } else {
      _setState(VescState.disconnected);
    }
  }

  void _scheduleReconnect() {
    if (_userDisconnected) return;
    if (_reconnectAttempts >= _maxReconnectAttempts) {
      _error = 'Could not reconnect after $_maxReconnectAttempts attempts';
      _setState(VescState.disconnected);
      return;
    }

    final delayMs = _backoffMs[
        _reconnectAttempts.clamp(0, _backoffMs.length - 1)];
    _reconnectAttempts++;

    debugPrint('[VESC] Reconnect attempt $_reconnectAttempts in ${delayMs}ms');

    Timer(Duration(milliseconds: delayMs), () async {
      if (_userDisconnected || isConnected) return;

      final dev = _device;
      if (dev == null) {
        // No remembered device — fall back to a fresh scan
        _setState(VescState.disconnected);
        return;
      }

      _setState(VescState.reconnecting);
      _error = 'Reconnecting… (attempt $_reconnectAttempts)';
      notifyListeners();
      await _connectTo(dev);
    });
  }

  // ── Explicit disconnect ───────────────────────────────────────────────────

  Future<void> disconnect() async {
    _userDisconnected = true;
    await _teardown();
    _setState(VescState.disconnected);
  }

  // ── Cleanup ───────────────────────────────────────────────────────────────

  Future<void> _teardown() async {
    _watchdog?.cancel();
    _watchdog = null;
    _waitingForReply = false;
    _notifySub?.cancel();
    _notifySub = null;
    _stateSub?.cancel();
    _stateSub = null;
    _rx = null;
    _rxBuffer.clear();
    try { await _device?.disconnect(); } catch (_) {}
    // Keep _device reference so we can reconnect to it
  }

  void _setState(VescState s) {
    _state = s;
    notifyListeners();
  }

  @override
  void dispose() {
    _userDisconnected = true;
    _teardown();
    super.dispose();
  }
}
