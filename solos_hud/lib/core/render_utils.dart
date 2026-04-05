import 'dart:typed_data';
import 'package:image/image.dart' as img;
import 'solos_protocol.dart';

/// Encode [rgba] bytes as RLE565.
///
/// Runs synchronously on the calling isolate. RLE565 takes 5–15 ms for a
/// 428×240 frame — fast enough to inline without isolate overhead.
/// (compute() would copy ~400 KB across isolate boundaries, costing more
/// than the encoding itself and delaying event processing on slow devices.)
Future<Uint8List> encodeRLE565Async(Uint8List rgba, int w, int h) async =>
    SolosProtocol.encodeRLE565(rgba, w, h);

/// Encode [rgba] bytes as JPEG at [quality] (1–100).
///
/// JPEG compresses anti-aliased Canvas text far better than RLE565:
/// a typical 428×240 HUD frame with coloured text produces 20–50 KB in
/// RLE565 (poor run-length due to many unique anti-alias edge colours) but
/// only 5–12 KB as JPEG q=80 — well within the RFCOMM packet size budget.
Future<Uint8List> encodeJpegAsync(
  Uint8List rgba,
  int w,
  int h, {
  int quality = 80,
}) async {
  final image = img.Image.fromBytes(
    width: w,
    height: h,
    bytes: rgba.buffer,
    numChannels: 4,
    order: img.ChannelOrder.rgba,
  );
  return Uint8List.fromList(img.encodeJpg(image, quality: quality));
}
