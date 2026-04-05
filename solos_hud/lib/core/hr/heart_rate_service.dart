import 'dart:async';
import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart' as fbp;
import 'package:permission_handler/permission_handler.dart';

enum HrConnectionState { disconnected, scanning, connecting, connected }

/// Connects to any BLE device exposing the standard Heart Rate Profile
/// (service 0x180D, characteristic 0x2A37) — e.g. Amazefit Balance,
/// Polar chest straps, Wahoo, etc.
///
/// To use Amazefit Balance in HR push mode:
///   1. Open Zepp app → Profile → Amazefit Balance → Health monitoring
///   2. Enable "Heart Rate Push" (or use workout mode which auto-advertises)
///   3. The watch will advertise as a BLE HR peripheral
class HeartRateService extends ChangeNotifier {
  static const String hrServiceUuid = '0000180d-0000-1000-8000-00805f9b34fb';
  static const String hrMeasurementUuid =
      '00002a37-0000-1000-8000-00805f9b34fb';

  HrConnectionState _state = HrConnectionState.disconnected;
  fbp.BluetoothDevice? _device;
  int? _heartRate;
  bool? _sensorContact;
  String? _error;

  final List<fbp.ScanResult> _scanResults = [];
  StreamSubscription? _scanSub;
  StreamSubscription? _notifySub;
  StreamSubscription? _connectionSub;

  HrConnectionState get state => _state;
  bool get isConnected => _state == HrConnectionState.connected;
  int? get heartRate => _heartRate;
  bool? get sensorContact => _sensorContact;
  String? get error => _error;
  String? get deviceName => _device?.platformName;
  List<fbp.ScanResult> get scanResults => List.unmodifiable(_scanResults);

  /// BLE devices currently connected at OS level (may include already-paired HR monitors).
  List<fbp.BluetoothDevice> get connectedSystemDevices =>
      fbp.FlutterBluePlus.connectedDevices;

  // ── Permissions ───────────────────────────────────────────────────────────

  Future<bool> _requestPermissions() async {
    if (!Platform.isAndroid) return true;
    final statuses = await [
      Permission.bluetoothScan,
      Permission.bluetoothConnect,
      Permission.locationWhenInUse,
    ].request();
    return statuses.values.every(
      (s) => s == PermissionStatus.granted || s == PermissionStatus.limited,
    );
  }

  // ── Scanning ──────────────────────────────────────────────────────────────

  Future<void> startScan({
    Duration timeout = const Duration(seconds: 12),
  }) async {
    if (_state == HrConnectionState.scanning) return;
    if (!await _requestPermissions()) return;

    final adapterState = await fbp.FlutterBluePlus.adapterState.first;
    if (adapterState != fbp.BluetoothAdapterState.on) {
      _error = 'Bluetooth is off';
      notifyListeners();
      return;
    }

    _scanResults.clear();
    _setState(HrConnectionState.scanning);

    _scanSub?.cancel();
    _scanSub = fbp.FlutterBluePlus.scanResults.listen((results) {
      _scanResults
        ..clear()
        ..addAll(results);
      notifyListeners();
    });

    // Scan for HR service UUID specifically
    await fbp.FlutterBluePlus.startScan(
      withServices: [fbp.Guid(hrServiceUuid)],
      timeout: timeout,
    );
    _scanSub?.cancel();

    if (_state == HrConnectionState.scanning) {
      _setState(HrConnectionState.disconnected);
    }
  }

  Future<void> stopScan() async {
    await fbp.FlutterBluePlus.stopScan();
    _scanSub?.cancel();
    if (_state == HrConnectionState.scanning) {
      _setState(HrConnectionState.disconnected);
    }
  }

  // ── Connection ────────────────────────────────────────────────────────────

  Future<void> connect(fbp.BluetoothDevice device) async {
    // Must stop scanning before connecting
    if (_state == HrConnectionState.scanning) await stopScan();
    if (isConnected) await disconnect();
    _error = null;
    _setState(HrConnectionState.connecting);

    try {
      // Device may already be connected at OS level (paired) — skip connect() in that case.
      if (!device.isConnected) {
        await device.connect(
          timeout: const Duration(seconds: 15),
          autoConnect: false,
        );
      }
      _device = device;

      _connectionSub = device.connectionState.listen((s) {
        if (s == fbp.BluetoothConnectionState.disconnected) {
          _heartRate = null;
          _device = null;
          _notifySub?.cancel();
          _setState(HrConnectionState.disconnected);
        }
      });

      await _subscribeToHR(device);
      _setState(HrConnectionState.connected);
    } catch (e) {
      _error = e.toString();
      _setState(HrConnectionState.disconnected);
    }
  }

  Future<void> _subscribeToHR(fbp.BluetoothDevice device) async {
    final services = await device.discoverServices();
    for (final svc in services) {
      if (_uuidMatches(svc.uuid.toString(), hrServiceUuid)) {
        for (final char in svc.characteristics) {
          if (_uuidMatches(char.uuid.toString(), hrMeasurementUuid)) {
            await char.setNotifyValue(true);
            _notifySub = char.lastValueStream.listen(_parseHRMeasurement);
            return;
          }
        }
      }
    }
    _error = 'HR characteristic not found on device';
  }

  /// Compares BLE UUIDs, handling both short (e.g. "180d") and full
  /// 128-bit (e.g. "0000180d-0000-1000-8000-00805f9b34fb") forms.
  static bool _uuidMatches(String a, String b) {
    String clean(String s) => s.toLowerCase().replaceAll('-', '');
    final ca = clean(a);
    final cb = clean(b);
    if (ca == cb) return true;
    // Short UUID is 4 hex chars; expand with standard BT base UUID prefix/suffix
    if (ca.length == 4) return cb == '0000${ca}00001000800000805f9b34fb';
    if (cb.length == 4) return ca == '0000${cb}00001000800000805f9b34fb';
    return false;
  }

  /// Parse the Heart Rate Measurement characteristic value.
  /// Spec: https://www.bluetooth.com/xml-viewer/?src=https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.heart_rate_measurement.xml
  void _parseHRMeasurement(List<int> data) {
    if (data.isEmpty) return;
    final flags = data[0];
    final hrFormat16bit = (flags & 0x01) != 0;
    final contactSupported = (flags & 0x04) != 0;
    final contactDetected = (flags & 0x02) != 0;

    _sensorContact = contactSupported ? contactDetected : null;

    if (hrFormat16bit && data.length >= 3) {
      _heartRate = data[1] | (data[2] << 8);
    } else if (!hrFormat16bit && data.length >= 2) {
      _heartRate = data[1];
    }
    notifyListeners();
  }

  Future<void> disconnect() async {
    _notifySub?.cancel();
    _connectionSub?.cancel();
    await _device?.disconnect();
    _device = null;
    _heartRate = null;
    _sensorContact = null;
    _setState(HrConnectionState.disconnected);
  }

  void _setState(HrConnectionState s) {
    _state = s;
    notifyListeners();
  }

  @override
  void dispose() {
    disconnect();
    _scanSub?.cancel();
    super.dispose();
  }
}
