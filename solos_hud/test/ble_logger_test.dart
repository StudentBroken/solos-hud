import 'package:flutter_test/flutter_test.dart';
import 'package:solos_hud/core/bluetooth/ble_logger.dart';

void main() {
  test('BleLogger.exportLogs formats entries correctly', () {
    final logger = BleLogger();
    logger.log(BleLogDirection.sent, 'Hello World', rawBytes: [0x01, 0x02]);
    logger.log(BleLogDirection.received, 'Response', rawBytes: [0x03]);
    logger.log(BleLogDirection.info, 'Status OK');

    final export = logger.exportLogs();

    expect(export, contains('Solos HUD Bluetooth Log'));
    expect(export, contains('TX: Hello World'));
    expect(export, contains('HEX: 01 02'));
    expect(export, contains('RX: Response'));
    expect(export, contains('HEX: 03'));
    expect(export, contains('INFO: Status OK'));
  });
}
