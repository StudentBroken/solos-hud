import 'package:flutter/foundation.dart';

enum BleLogDirection { sent, received, info, error }

class BleLogEntry {
  final DateTime timestamp;
  final BleLogDirection direction;
  final String message;
  final List<int>? rawBytes;

  BleLogEntry({required this.direction, required this.message, this.rawBytes})
    : timestamp = DateTime.now();

  String get directionLabel {
    switch (direction) {
      case BleLogDirection.sent:
        return 'TX';
      case BleLogDirection.received:
        return 'RX';
      case BleLogDirection.info:
        return 'INFO';
      case BleLogDirection.error:
        return 'ERR';
    }
  }

  String get hexBytes {
    if (rawBytes == null) return '';
    return rawBytes!.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ');
  }
}

/// In-memory BLE traffic log. Exposed as a ChangeNotifier so the UI can react.
class BleLogger extends ChangeNotifier {
  final List<BleLogEntry> _entries = [];
  static const int _maxEntries = 500;
  bool enabled = true;

  List<BleLogEntry> get entries => List.unmodifiable(_entries);

  void log(BleLogDirection direction, String message, {List<int>? rawBytes}) {
    if (!enabled) return;
    _entries.add(
      BleLogEntry(direction: direction, message: message, rawBytes: rawBytes),
    );
    if (_entries.length > _maxEntries) {
      _entries.removeRange(0, _entries.length - _maxEntries);
    }
    notifyListeners();
  }

  void clear() {
    _entries.clear();
    notifyListeners();
  }

  /// Returns the entire log as a formatted string.
  String exportLogs() {
    final buffer = StringBuffer();
    buffer.writeln('Solos HUD Bluetooth Log - ${DateTime.now()}');
    buffer.writeln('----------------------------------------');
    for (final entry in _entries) {
      final time = entry.timestamp
          .toIso8601String()
          .split('T')
          .last
          .substring(0, 12);
      buffer.writeln('[$time] ${entry.directionLabel}: ${entry.message}');
      if (entry.rawBytes != null && entry.rawBytes!.isNotEmpty) {
        buffer.writeln('        HEX: ${entry.hexBytes}');
      }
    }
    return buffer.toString();
  }
}
