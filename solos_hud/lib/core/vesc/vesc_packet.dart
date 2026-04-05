import 'dart:typed_data';

/// VESC binary packet encoder / decoder.
///
/// Frame format: 0x02 | length(1B) | payload... | CRC16_hi | CRC16_lo | 0x03
/// For payloads > 255 bytes: 0x03 | len_hi | len_lo | payload... | CRC16 | 0x03
///
/// COMM_GET_VALUES = packet ID 4.
/// Response fields (big-endian, in order):
///   temp_fet(f32), temp_motor(f32), avg_motor_current(f32),
///   avg_input_current(f32), avg_id(f32), avg_iq(f32),
///   duty_cycle(f32), erpm(f32), voltage(f32),
///   amp_hours(f32), amp_hours_charged(f32),
///   watt_hours(f32), watt_hours_charged(f32),
///   tachometer(i32), tachometer_abs(i32), fault_code(u8)
class VescPacket {
  static const int _cmdGetValues = 4;

  // ── Encode ──────────────────────────────────────────────────────────────

  static Uint8List buildGetValues() =>
      _build(Uint8List.fromList([_cmdGetValues]));

  static Uint8List _build(Uint8List payload) {
    final len = payload.length;
    final buf = BytesBuilder();
    if (len <= 255) {
      buf.addByte(0x02);
      buf.addByte(len);
    } else {
      buf.addByte(0x03);
      buf.addByte((len >> 8) & 0xFF);
      buf.addByte(len & 0xFF);
    }
    buf.add(payload);
    final crc = _crc16(payload);
    buf.addByte((crc >> 8) & 0xFF);
    buf.addByte(crc & 0xFF);
    buf.addByte(0x03);
    return buf.toBytes();
  }

  // ── Decode ──────────────────────────────────────────────────────────────

  /// Parse a raw VESC response byte stream.
  static VescValues? parse(Uint8List raw) {
    if (raw.length < 5) return null;

    int payloadLen;
    int payloadStart;

    if (raw[0] == 0x02) {
      payloadLen = raw[1];
      payloadStart = 2;
    } else if (raw[0] == 0x03) {
      if (raw.length < 6) return null;
      payloadLen = (raw[1] << 8) | raw[2];
      payloadStart = 3;
    } else {
      return null;
    }

    if (raw.length < payloadStart + payloadLen + 3) return null;

    final payload = raw.sublist(payloadStart, payloadStart + payloadLen);
    final crcHi = raw[payloadStart + payloadLen];
    final crcLo = raw[payloadStart + payloadLen + 1];
    final crc = (crcHi << 8) | crcLo;

    if (_crc16(payload) != crc) return null;
    if (payload.isEmpty || payload[0] != _cmdGetValues) return null;

    return VescValues._parse(payload.sublist(1));
  }

  // ── CRC-16/CCITT (XModem) ───────────────────────────────────────────────

  static int _crc16(Uint8List data) {
    int crc = 0;
    for (final b in data) {
      crc ^= b << 8;
      for (int i = 0; i < 8; i++) {
        if (crc & 0x8000 != 0) {
          crc = ((crc << 1) ^ 0x1021) & 0xFFFF;
        } else {
          crc = (crc << 1) & 0xFFFF;
        }
      }
    }
    return crc;
  }
}

// ── Parsed telemetry ──────────────────────────────────────────────────────────

class VescValues {
  final double tempFet; // °C
  final double tempMotor; // °C
  final double motorCurrentA;
  final double inputCurrentA;
  final double dutyCycle; // 0.0–1.0
  final double erpm;
  final double voltageV;
  final double ampHours;
  final double wattHours;
  final int faultCode;

  const VescValues({
    required this.tempFet,
    required this.tempMotor,
    required this.motorCurrentA,
    required this.inputCurrentA,
    required this.dutyCycle,
    required this.erpm,
    required this.voltageV,
    required this.ampHours,
    required this.wattHours,
    required this.faultCode,
  });

  static VescValues? _parse(Uint8List p) {
    // VESC standard COMM_GET_VALUES uses mostly big-endian scaled integers.
    // Offsets based on standard 2.x/3.x/5.x firmware:
    if (p.length < 55) return null;
    final d = ByteData.sublistView(p);
    return VescValues(
      tempFet: d.getInt16(0, Endian.big) / 10.0,
      tempMotor: d.getInt16(2, Endian.big) / 10.0,
      motorCurrentA: d.getInt32(4, Endian.big) / 100.0,
      inputCurrentA: d.getInt32(8, Endian.big) / 100.0,
      dutyCycle: d.getInt16(20, Endian.big) / 1000.0,
      erpm: d.getInt32(22, Endian.big).toDouble(),
      voltageV: d.getInt16(26, Endian.big) / 10.0,
      ampHours: d.getInt32(28, Endian.big) / 10000.0,
      wattHours: d.getInt32(36, Endian.big) / 10000.0,
      faultCode: p.length >= 56 ? p[55] : 0,
    );
  }

  /// Speed in km/h from eRPM.
  double speedKmh({int motorPoles = 14, double wheelDiameterMm = 97}) {
    final mechanicalRpm = erpm / (motorPoles / 2);
    final circumferenceM = wheelDiameterMm / 1000 * 3.14159;
    return mechanicalRpm * circumferenceM * 60 / 1000;
  }

  double get watts => voltageV * inputCurrentA;

  String get faultLabel => faultCode == 0 ? 'NONE' : 'FAULT $faultCode';
}
