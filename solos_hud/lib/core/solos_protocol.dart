import 'dart:typed_data';

/// Builds binary packets for the Solos smart glasses protocol.
///
/// Packet format (all little-endian):
///   [0-1]  Magic: 0x1D 0x60
///   [2-3]  PacketType (short)
///   [4-5]  Reserved: 0x00 0x00
///   [6-9]  PayloadLength (int)
///   [10+]  Payload
///
/// IMAGE payload = ImageHeader (14 bytes) + image data
/// Source: APK decompile + John Floren's Wireshark analysis
class SolosProtocol {
  static const int _magic = 0x601D;

  // Packet types
  static const int typePing = 1;
  static const int typeImage = 5;
  static const int typeWakeUp = 4;
  static const int typeSleep = 22;
  static const int typeStatusSet =
      20; // confirmed from PacketType.java decompile
  static const int typeStatusGet = 18;
  static const int typeStatusResponse = 19;
  static const int typeCapabilitiesGet = 7;

  // STATUS flags (bitmask, from StatusType.java)
  static const int statusFlagOrientation =
      2; // 0x002 — 3 × short (pitch, roll, yaw)
  static const int statusFlagBattery = 256; // 0x100 — 1 byte (0-100)
  static const int statusFlagPower =
      512; // 0x200 — 1 byte (0=discharging, 1=charging)
  static const int statusFlagBatteryAndPower =
      statusFlagBattery | statusFlagPower; // 0x300
  // STATUS flags confirmed from StatusType.java:
  //   1=AMBIENT_LIGHT(int), 2=DEVICE_ORIENTATION(3xshort), 4=DISPLAY_BRIGHTNESS(byte),
  //   8=SPEAKER_VOLUME(byte), 16=AMBIENT_NOISE(16bytes), 32=LEFT_MIC(byte),
  //   64=RIGHT_MIC(byte), 256=BATTERY_LEVEL(byte), 512=POWER_AVAILABLE(byte)
  static const int statusFlagDisplayBrightness = 4; // 0x004 — 1 byte (0-255)
  static const int statusFlagTiltPoll =
      statusFlagOrientation | statusFlagBattery | statusFlagPower; // 0x302

  // Image codecs
  static const int codecRGB565 = 1;
  static const int codecRLE565 = 2; // used by the Solos app
  static const int codecJPEG = 8;

  // Display dimensions confirmed by Wireshark capture
  static const int displayWidth = 428;
  static const int displayHeight = 240;

  // ── Packet builders ────────────────────────────────────────────────────────

  static Uint8List _header(int type, int payloadLength) {
    final buf = ByteData(10);
    buf.setUint16(0, _magic, Endian.little);
    buf.setUint16(2, type, Endian.little);
    buf.setUint16(4, 0, Endian.little); // reserved
    buf.setUint32(6, payloadLength, Endian.little);
    return buf.buffer.asUint8List();
  }

  static Uint8List buildWakeUp() => _header(typeWakeUp, 0);

  /// Request battery level (and optionally charging state) from the glasses.
  /// [flags] is a bitmask of statusFlag* constants.
  static Uint8List buildStatusGet([int flags = statusFlagBatteryAndPower]) {
    final payload = ByteData(4)..setInt32(0, flags, Endian.little);
    final h = _header(typeStatusGet, 4);
    final out = Uint8List(14);
    out.setRange(0, 10, h);
    out.setRange(10, 14, payload.buffer.asUint8List());
    return out;
  }

  /// Update hardware settings (brightness, auto-brightness, volume, etc).
  /// [flags] indicates which byte(s) are provided in the payload.
  /// [value] is the data to set.
  static Uint8List buildStatusSet(int flags, int value) {
    // STATUS_SET payload: 4-byte flags + 1-byte value
    final payload = ByteData(5);
    payload.setInt32(0, flags, Endian.little);
    payload.setUint8(4, value);

    final h = _header(typeStatusSet, 5);
    final out = Uint8List(15);
    out.setRange(0, 10, h);
    out.setRange(10, 15, payload.buffer.asUint8List());
    return out;
  }

  /// Parse a STATUS_RESPONSE payload.
  /// Returns a map of present field names → their values.
  /// Fields decoded in descriptor order: 1,2,4,8,16,32,64,256,512
  static Map<String, int> parseStatusResponse(Uint8List payload) {
    if (payload.length < 4) return {};
    final data = ByteData.sublistView(payload);
    final flags = data.getInt32(0, Endian.little);
    int offset = 4;
    final result = <String, int>{};

    // Descriptors in order (flag, bytesPerEntry, count, name)
    // For orientation (flag=2, 3 shorts) we split into pitchRaw/rollRaw/yawRaw.
    const scalarDescriptors = [
      (1, 4, 1, 'ambientLight'),
      (4, 1, 1, 'displayBrightness'),
      (8, 1, 1, 'speakerVolume'),
      (32, 1, 1, 'leftMicLevel'),
      (64, 1, 1, 'rightMicLevel'),
      (256, 1, 1, 'batteryLevel'),
      (512, 1, 1, 'powerAvailable'),
    ];

    // ── ambientLight (flag 1) ────────────────────────────────────────────
    if ((flags & 1) == 1) {
      if (offset + 4 <= payload.length) {
        result['ambientLight'] = data.getInt32(offset, Endian.little);
        offset += 4;
      }
    }

    // ── deviceOrientation (flag 2): 3 × signed short ────────────────────
    if ((flags & statusFlagOrientation) == statusFlagOrientation) {
      if (offset + 6 <= payload.length) {
        result['pitchRaw'] = data.getInt16(offset, Endian.little);
        result['rollRaw'] = data.getInt16(offset + 2, Endian.little);
        result['yawRaw'] = data.getInt16(offset + 4, Endian.little);
        offset += 6;
      }
    }

    // ── remaining scalar fields ──────────────────────────────────────────
    for (final (flag, bytesEach, _, name) in scalarDescriptors) {
      if ((flags & flag) == flag) {
        if (offset + bytesEach > payload.length) break;
        result[name] = bytesEach == 1
            ? payload[offset] & 0xFF
            : bytesEach == 2
            ? data.getInt16(offset, Endian.little)
            : data.getInt32(offset, Endian.little);
        offset += bytesEach;
      }
    }
    return result;
  }

  static Uint8List buildSleep() => _header(typeSleep, 0);

  static Uint8List buildCapabilitiesGet() {
    // IntPacketContent: 4-byte int payload, value 0
    final payload = ByteData(4)..setInt32(0, 0, Endian.little);
    final h = _header(typeCapabilitiesGet, 4);
    return Uint8List(14)
      ..setRange(0, 10, h)
      ..setRange(10, 14, payload.buffer.asUint8List());
  }

  /// Wrap [imageData] in an IMAGE packet.
  /// [x], [y], [width], [height] define the region being updated.
  static Uint8List buildImagePacket({
    required Uint8List imageData,
    int codec = codecRLE565,
    int window = 0,
    int x = 0,
    int y = 0,
    int width = displayWidth,
    int height = displayHeight,
  }) {
    final imageHeader = ByteData(14);
    imageHeader.setUint8(0, codec);
    imageHeader.setUint8(1, window);
    imageHeader.setUint16(2, 0, Endian.little); // chunk index = 0
    imageHeader.setUint16(4, 1, Endian.little); // total chunks = 1
    imageHeader.setUint16(6, x, Endian.little);
    imageHeader.setUint16(8, y, Endian.little);
    imageHeader.setUint16(10, width, Endian.little);
    imageHeader.setUint16(12, height, Endian.little);

    final payloadLen = 14 + imageData.length;
    final result = Uint8List(10 + payloadLen);
    result.setRange(0, 10, _header(typeImage, payloadLen));
    result.setRange(10, 24, imageHeader.buffer.asUint8List());
    result.setRange(24, result.length, imageData);
    return result;
  }

  // ── RLE565 encoder ─────────────────────────────────────────────────────────

  /// Encode raw RGBA pixel bytes as RLE565.
  ///
  /// [rgba] must be width × height × 4 bytes (R, G, B, A per pixel).
  /// Output format: [count_byte][rgb565_lo][rgb565_hi] per run, max 255 pixels/run.
  static Uint8List encodeRLE565(Uint8List rgba, int width, int height) {
    final out = BytesBuilder(copy: false);
    int count = 0;
    int lastColor = -1;

    final total = width * height;
    for (int i = 0; i < total; i++) {
      final r = rgba[i * 4];
      final g = rgba[i * 4 + 1];
      final b = rgba[i * 4 + 2];
      final rgb565 = ((r >> 3) << 11) | ((g >> 2) << 5) | (b >> 3);

      if (lastColor < 0) {
        lastColor = rgb565;
        count = 1;
      } else if (rgb565 == lastColor && count < 255) {
        count++;
      } else {
        out.addByte(count);
        out.addByte(lastColor & 0xFF);
        out.addByte((lastColor >> 8) & 0xFF);
        lastColor = rgb565;
        count = 1;
      }
    }

    if (count > 0) {
      out.addByte(count);
      out.addByte(lastColor & 0xFF);
      out.addByte((lastColor >> 8) & 0xFF);
    }

    return out.toBytes();
  }

  /// Build a solid-color IMAGE packet (single-color fill, very compact).
  static Uint8List buildSolidColorPacket(int r, int g, int b) {
    final rgb565 = ((r >> 3) << 11) | ((g >> 2) << 5) | (b >> 3);
    final colorLo = rgb565 & 0xFF;
    final colorHi = (rgb565 >> 8) & 0xFF;

    // Fill 428×240 = 102720 pixels.
    // 255 pixels per run = 403 full runs + 1 partial run (102720 % 255 = 45)
    final fullRuns = (displayWidth * displayHeight) ~/ 255;
    final remainder = (displayWidth * displayHeight) % 255;

    final rleData = Uint8List((fullRuns + (remainder > 0 ? 1 : 0)) * 3);
    int pos = 0;
    for (int i = 0; i < fullRuns; i++) {
      rleData[pos++] = 255;
      rleData[pos++] = colorLo;
      rleData[pos++] = colorHi;
    }
    if (remainder > 0) {
      rleData[pos++] = remainder;
      rleData[pos++] = colorLo;
      rleData[pos] = colorHi;
    }

    return buildImagePacket(imageData: rleData);
  }
}
