import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Central settings store. All values are persisted via SharedPreferences.
class AppSettings extends ChangeNotifier {
  static const _keyRefreshRateMs = 'global_refresh_rate_ms';
  static const _keyAutoConnect = 'auto_connect';
  static const _keyDeviceName = 'device_name';
  static const _keyLogBle = 'log_ble';
  static const _keySpeedUnit = 'speed_unit';
  static const _keyTxCharUuid = 'tx_char_uuid';
  static const _keyTxServiceUuid = 'tx_service_uuid';
  static const _keyTiltWakeEnabled = 'tilt_wake_enabled';
  static const _keyTiltThresholdDeg = 'tilt_threshold_deg';
  static const _keyBrightness = 'display_brightness';
  static const _keyAutoBrightness = 'auto_brightness';
  static const _keyLastDeviceAddress = 'last_device_address';
  static const _keyLastAppId = 'last_app_id';
  static const _keyAutoStartHud = 'auto_start_hud';
  static const _keyGeminiKey = 'gemini_api_key';
  static const _keyGeminiModel = 'gemini_model';
  static const _keyWakeWord = 'voice_wake_word';
  static const _keyVescMotorPoles = 'vesc_motor_poles';
  static const _keyVescWheelDiameter = 'vesc_wheel_diameter';
  static const _keyVescVoltageOffset = 'vesc_voltage_offset';
  static const _keyVescCellCount = 'vesc_cell_count';
  static const _keyVescBatteryChem = 'vesc_battery_chem';
  static const _keyPhoneAssistantMode = 'phone_assistant_mode';
  static const _keyPhoneAssistantType = 'phone_assistant_type';

  late SharedPreferences _prefs;

  // Global defaults
  int refreshRateMs = 300;
  bool autoConnect = true;
  bool autoStartHud = true; // Start HUD loop automatically on connect
  String deviceName = '';
  String lastDeviceAddress =
      ''; // MAC address of last successfully connected glasses
  String lastAppId = '';
  String geminiKey = '';
  String geminiModel = 'gemini-2.5-flash-preview';
  String wakeWord = 'hey jarvis';
  bool logBle = true;
  String speedUnit = 'km/h'; // 'km/h' | 'mph' | 'm/s'
  bool tiltWakeEnabled = false;
  int tiltThresholdDeg = 20;
  int brightness = 128; // 0-255
  bool autoBrightness = true;

  // Assistant settings
  bool phoneAssistantMode = false;
  /// 'voice_assist' | 'google_assistant' | 'bixby'
  String phoneAssistantType = 'voice_assist';

  // VESC Settings
  int vescMotorPoles = 14;
  double vescWheelDiameter = 97.0;
  double vescVoltageOffset = 0.0;
  int vescCellCount = 12;
  String vescBatteryChem = 'li-ion'; // 'li-ion' or 'lifepo4'

  /// UUID of the BLE characteristic to write payloads to.
  /// Empty string = auto-detect (use first writable characteristic found).
  String txCharUuid = '';

  /// UUID of the BLE service that contains the TX characteristic.
  /// Empty string = search all services.
  String txServiceUuid = '';

  // Per-app arbitrary key-value store
  final Map<String, dynamic> _appValues = {};

  Future<void> load() async {
    _prefs = await SharedPreferences.getInstance();
    refreshRateMs = _prefs.getInt(_keyRefreshRateMs) ?? 300;
    autoConnect = _prefs.getBool(_keyAutoConnect) ?? true;
    deviceName = _prefs.getString(_keyDeviceName) ?? '';
    logBle = _prefs.getBool(_keyLogBle) ?? true;
    speedUnit = _prefs.getString(_keySpeedUnit) ?? 'km/h';
    txCharUuid = _prefs.getString(_keyTxCharUuid) ?? '';
    txServiceUuid = _prefs.getString(_keyTxServiceUuid) ?? '';
    tiltWakeEnabled = _prefs.getBool(_keyTiltWakeEnabled) ?? false;
    tiltThresholdDeg = _prefs.getInt(_keyTiltThresholdDeg) ?? 20;
    brightness = _prefs.getInt(_keyBrightness) ?? 128;
    autoBrightness = _prefs.getBool(_keyAutoBrightness) ?? true;
    lastDeviceAddress = _prefs.getString(_keyLastDeviceAddress) ?? '';
    lastAppId = _prefs.getString(_keyLastAppId) ?? '';
    autoStartHud = _prefs.getBool(_keyAutoStartHud) ?? true;
    geminiKey = _prefs.getString(_keyGeminiKey) ?? '';
    geminiModel =
        _prefs.getString(_keyGeminiModel) ?? 'gemini-2.5-flash-preview';
    wakeWord = _prefs.getString(_keyWakeWord) ?? 'hey jarvis';
    phoneAssistantMode = _prefs.getBool(_keyPhoneAssistantMode) ?? false;
    phoneAssistantType = _prefs.getString(_keyPhoneAssistantType) ?? 'voice_assist';
    vescMotorPoles = _prefs.getInt(_keyVescMotorPoles) ?? 14;
    vescWheelDiameter = _prefs.getDouble(_keyVescWheelDiameter) ?? 97.0;
    vescVoltageOffset = _prefs.getDouble(_keyVescVoltageOffset) ?? 0.0;
    vescCellCount = _prefs.getInt(_keyVescCellCount) ?? 12;
    vescBatteryChem = _prefs.getString(_keyVescBatteryChem) ?? 'li-ion';
  }

  Future<void> setRefreshRateMs(int ms) async {
    refreshRateMs = ms.clamp(250, 2000);
    await _prefs.setInt(_keyRefreshRateMs, refreshRateMs);
    notifyListeners();
  }

  Future<void> setAutoConnect(bool value) async {
    autoConnect = value;
    await _prefs.setBool(_keyAutoConnect, value);
    notifyListeners();
  }

  Future<void> setDeviceName(String value) async {
    deviceName = value;
    await _prefs.setString(_keyDeviceName, value);
    notifyListeners();
  }

  Future<void> setLogBle(bool value) async {
    logBle = value;
    await _prefs.setBool(_keyLogBle, value);
    notifyListeners();
  }

  Future<void> setSpeedUnit(String value) async {
    speedUnit = value;
    await _prefs.setString(_keySpeedUnit, value);
    notifyListeners();
  }

  Future<void> setTxCharUuid(String value) async {
    txCharUuid = value.trim().toLowerCase();
    await _prefs.setString(_keyTxCharUuid, txCharUuid);
    notifyListeners();
  }

  Future<void> setTxServiceUuid(String value) async {
    txServiceUuid = value.trim().toLowerCase();
    await _prefs.setString(_keyTxServiceUuid, txServiceUuid);
    notifyListeners();
  }

  Future<void> setTiltWakeEnabled(bool value) async {
    tiltWakeEnabled = value;
    await _prefs.setBool(_keyTiltWakeEnabled, value);
    notifyListeners();
  }

  Future<void> setTiltThresholdDeg(int value) async {
    tiltThresholdDeg = value.clamp(5, 60);
    await _prefs.setInt(_keyTiltThresholdDeg, tiltThresholdDeg);
    notifyListeners();
  }

  Future<void> setBrightness(int value) async {
    brightness = value.clamp(0, 255);
    await _prefs.setInt(_keyBrightness, brightness);
    notifyListeners();
  }

  Future<void> setGeminiKey(String key) async {
    geminiKey = key;
    await _prefs.setString(_keyGeminiKey, key);
    notifyListeners();
  }

  Future<void> setGeminiModel(String model) async {
    geminiModel = model.isEmpty ? 'gemini-2.5-flash-preview' : model;
    await _prefs.setString(_keyGeminiModel, geminiModel);
    notifyListeners();
  }

  Future<void> setWakeWord(String word) async {
    wakeWord = word.isEmpty ? 'hey jarvis' : word.toLowerCase();
    await _prefs.setString(_keyWakeWord, wakeWord);
    notifyListeners();
  }

  Future<void> setLastDeviceAddress(String address) async {
    lastDeviceAddress = address;
    await _prefs.setString(_keyLastDeviceAddress, address);
    // No notifyListeners — this is background state, UI doesn't need to rebuild
  }

  Future<void> setLastAppId(String id) async {
    lastAppId = id;
    await _prefs.setString(_keyLastAppId, id);
  }

  Future<void> setAutoStartHud(bool value) async {
    autoStartHud = value;
    await _prefs.setBool(_keyAutoStartHud, value);
    notifyListeners();
  }

  Future<void> setAutoBrightness(bool enabled) async {
    autoBrightness = enabled;
    await _prefs.setBool(_keyAutoBrightness, enabled);
    notifyListeners();
  }

  Future<void> setVescMotorPoles(int value) async {
    vescMotorPoles = value.clamp(2, 64);
    await _prefs.setInt(_keyVescMotorPoles, vescMotorPoles);
    notifyListeners();
  }

  Future<void> setVescWheelDiameter(double value) async {
    vescWheelDiameter = value.clamp(50, 200);
    await _prefs.setDouble(_keyVescWheelDiameter, vescWheelDiameter);
    notifyListeners();
  }

  Future<void> setVescVoltageOffset(double value) async {
    vescVoltageOffset = value.clamp(-5.0, 5.0);
    await _prefs.setDouble(_keyVescVoltageOffset, vescVoltageOffset);
    notifyListeners();
  }

  Future<void> setVescCellCount(int value) async {
    vescCellCount = value.clamp(1, 24);
    await _prefs.setInt(_keyVescCellCount, vescCellCount);
    notifyListeners();
  }

  Future<void> setVescBatteryChem(String chem) async {
    vescBatteryChem = chem;
    await _prefs.setString(_keyVescBatteryChem, vescBatteryChem);
    notifyListeners();
  }

  Future<void> setPhoneAssistantMode(bool value) async {
    phoneAssistantMode = value;
    await _prefs.setBool(_keyPhoneAssistantMode, value);
    notifyListeners();
  }

  Future<void> setPhoneAssistantType(String type) async {
    phoneAssistantType = type;
    await _prefs.setString(_keyPhoneAssistantType, type);
    notifyListeners();
  }

  // Generic per-app setting access
  dynamic getAppValue(String key, dynamic defaultValue) {
    return _appValues[key] ?? _prefs.get(key) ?? defaultValue;
  }

  Future<void> setAppValue(String key, dynamic value) async {
    _appValues[key] = value;
    if (value is bool) await _prefs.setBool(key, value);
    if (value is int) await _prefs.setInt(key, value);
    if (value is double) await _prefs.setDouble(key, value);
    if (value is String) await _prefs.setString(key, value);
    notifyListeners();
  }
}
