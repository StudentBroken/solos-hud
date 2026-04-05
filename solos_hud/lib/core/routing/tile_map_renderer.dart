import 'dart:math' as math;
import 'dart:ui' as ui;
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:image/image.dart' as img;
import '../notifications/notification_event.dart';
import '../solos_protocol.dart';
import 'route_service.dart';

// ── Isolate workers ───────────────────────────────────────────────────────────

Uint8List _rle565MapWorker(List<dynamic> args) => SolosProtocol.encodeRLE565(
  args[0] as Uint8List,
  args[1] as int,
  args[2] as int,
);

Uint8List _jpegWorker(List<dynamic> args) {
  final rgba = args[0] as Uint8List;
  final w = args[1] as int;
  final h = args[2] as int;
  final quality = args[3] as int;
  final image = img.Image.fromBytes(
    width: w,
    height: h,
    bytes: rgba.buffer,
    format: img.Format.uint8,
    numChannels: 4,
    order: img.ChannelOrder.rgba,
  );
  return Uint8List.fromList(img.encodeJpg(image, quality: quality));
}

// ── Palette ───────────────────────────────────────────────────────────────────
const _bg = Color(0xFF0A0E17);
const _cyan = Color(0xFF00CCFF);
const _white = Color(0xFFFFFFFF);
const _muted = Color(0xFF4A6080);
const _green = Color(0xFF00FF88);
const _destColor = Color(0xFFFF5533);
const _overlayBg = Color(0xDD000A14);

/// Renders a live tile-based map to a 428×240 bitmap ready for the glasses.
///
/// Primary tiles: Carto Dark Matter (free, no key).
/// Optional premium: Mapbox dark-v11 tiles (requires API key).
/// Fallback: OSM standard tiles (light, but always available).
class TileMapRenderer {
  static const int _tw = 256;
  static const int _dw = SolosProtocol.displayWidth; // 428
  static const int _dh = SolosProtocol.displayHeight; // 240

  // ── Tile cache ────────────────────────────────────────────────────────────
  // Stores successfully decoded images only. Failed fetches are tracked
  // separately so they can be retried after [_retryAfter].
  static final Map<String, Uint8List> _ok = {}; // key → PNG bytes
  static final Map<String, DateTime> _failed = {}; // key → time of failure
  static final List<String> _order = [];
  static const int _maxCache = 100;
  static const Duration _retryAfter = Duration(seconds: 20);

  // Round-robin Carto CDN subdomains for load spreading
  static const _cartoCdns = ['a', 'b', 'c', 'd'];
  static int _cdnIdx = 0;

  // ── Public render ─────────────────────────────────────────────────────────

  static Future<Uint8List> render({
    required double lat,
    required double lng,
    double heading = 0,
    int zoom = 16,
    List<LatLng>? route,
    LatLng? destination,
    NavInstruction? instruction,
    String? destLabel,
    bool headingUp = true,
    bool arrived = false,
    String? mapboxKey,
    bool useJpeg = true,
    // Quality 45: ~6–8 KB — small enough for the glasses' BT buffer
    // while preserving the route line's coloured edges at 428×240.
    int jpegQuality = 45,
  }) async {
    // ── Fractional tile position of current location ───────────────────────
    final cfx = _lon2t(lng, zoom);
    final cfy = _lat2t(lat, zoom);

    // Tile radius capped at 2 — covers the visible viewport with a small buffer.
    // Fetching 5×5 = 25 tiles max instead of 7×7 = 49 significantly reduces
    // memory pressure and HTTP concurrency, which was crashing the BT connection.
    final tileRadius = headingUp ? 2 : 1;

    final cx = cfx.floor();
    final cy = cfy.floor();
    final maxTile = (1 << zoom) - 1;

    // Collect tile keys
    final needed = <String, (int tx, int ty)>{};
    for (int dy = -tileRadius; dy <= tileRadius; dy++) {
      for (int dx = -tileRadius; dx <= tileRadius; dx++) {
        final tx = (cx + dx).clamp(0, maxTile);
        final ty = (cy + dy).clamp(0, maxTile);
        needed['$zoom/$tx/$ty'] = (tx, ty);
      }
    }

    // Fetch missing / retryable tiles in parallel
    await Future.wait([
      for (final e in needed.entries)
        if (_shouldFetch(e.key))
          _fetch(e.key, e.value.$1, e.value.$2, zoom, mapboxKey),
    ]);

    // Decode all available tiles
    final images = <String, ui.Image?>{};
    await Future.wait([
      for (final key in needed.keys)
        if (_ok.containsKey(key))
          _decode(_ok[key]!).then((img) {
            images[key] = img;
          }),
    ]);

    final loadedCount = images.values.where((v) => v != null).length;

    // ── Canvas ─────────────────────────────────────────────────────────────
    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(
      rec,
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
    );

    canvas.drawRect(
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
      _fill(_bg),
    );

    // ── Heading-up rotation ────────────────────────────────────────────────
    canvas.save();
    canvas.translate(_dw / 2, _dh / 2);
    if (headingUp) canvas.rotate(-heading * math.pi / 180);
    canvas.translate(-_dw / 2.0, -_dh / 2.0);

    // Draw tiles
    for (final e in needed.entries) {
      final img = images[e.key];
      if (img == null) continue;
      final tx = e.value.$1;
      final ty = e.value.$2;
      final px = (tx - cfx) * _tw + _dw / 2;
      final py = (ty - cfy) * _tw + _dh / 2;
      canvas.drawImage(img, Offset(px, py), ui.Paint());
      img.dispose();
    }

    // Route polyline (with glow)
    if (route != null && route.length > 1) {
      _drawRoute(canvas, route, cfx, cfy, zoom);
    }

    // Destination pin
    if (destination != null) {
      final px = (_lon2t(destination.lng, zoom) - cfx) * _tw + _dw / 2;
      final py = (_lat2t(destination.lat, zoom) - cfy) * _tw + _dh / 2;
      _drawDestPin(canvas, Offset(px, py));
    }

    canvas.restore(); // pop heading rotation

    // If no tiles loaded yet, show a helpful loading screen instead of just bg
    if (loadedCount == 0) {
      _drawLoadingOverlay(canvas);
    }

    // Position arrow (always at center, always upright = forward direction)
    _drawPositionArrow(canvas, heading, headingUp);

    // Compass rose (heading-up mode only)
    if (headingUp && loadedCount > 0) _drawCompass(canvas, heading);

    // Bottom instruction / destination overlay
    if (arrived) {
      _drawArrivedBar(canvas);
    } else {
      _drawInstructionBar(canvas, instruction, destLabel);
    }

    // ── Encode ─────────────────────────────────────────────────────────────
    final picture = rec.endRecording();
    final image = await picture.toImage(_dw, _dh);
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    final rgba = bd!.buffer.asUint8List();

    // JPEG is 5–10× smaller than RLE565 for photo-like map tiles,
    // which means much faster RFCOMM transmission.
    if (useJpeg) {
      return await compute(_jpegWorker, [rgba, _dw, _dh, jpegQuality]);
    }
    return await compute(_rle565MapWorker, [rgba, _dw, _dh]);
  }

  // ── Tile fetch & cache ────────────────────────────────────────────────────

  static bool _shouldFetch(String key) {
    if (_ok.containsKey(key)) return false; // already cached successfully
    final f = _failed[key];
    if (f == null) return true; // never attempted
    return DateTime.now().difference(f) > _retryAfter; // retry after cooldown
  }

  static Future<void> _fetch(
    String key,
    int tx,
    int ty,
    int zoom,
    String? mapboxKey,
  ) async {
    // Build URL — try preferred source first, then fallback
    final urls = _buildUrls(tx, ty, zoom, mapboxKey);
    for (final url in urls) {
      try {
        final resp = await http
            .get(
              Uri.parse(url),
              headers: {'User-Agent': 'SolosHUD/1.0 (github.com/solos-hud)'},
            )
            .timeout(const Duration(seconds: 6));

        if (resp.statusCode == 200 && resp.bodyBytes.isNotEmpty) {
          // Verify it's actually an image (basic check)
          final bytes = resp.bodyBytes;
          if (_looksLikeImage(bytes)) {
            _putCache(key, bytes);
            return; // success
          }
        }
      } catch (e) {
        debugPrint('[Tiles] fetch error $key: $e');
      }
    }
    // All sources failed — record failure time for retry
    _failed[key] = DateTime.now();
  }

  static List<String> _buildUrls(int tx, int ty, int zoom, String? mapboxKey) {
    final urls = <String>[];

    // 1. Mapbox dark tiles (premium, best quality)
    if (mapboxKey != null && mapboxKey.isNotEmpty) {
      urls.add(
        'https://api.mapbox.com/styles/v1/mapbox/dark-v11'
        '/tiles/256/$zoom/$tx/$ty?access_token=$mapboxKey',
      );
    }

    // 2. Carto Dark Matter — round-robin across CDN subdomains
    final cdn = _cartoCdns[_cdnIdx % _cartoCdns.length];
    _cdnIdx++;
    urls.add('https://$cdn.basemaps.cartocdn.com/dark_all/$zoom/$tx/$ty.png');

    // 3. Carto via alternate CDN path (sometimes different subdomain works)
    final cdn2 = _cartoCdns[(_cdnIdx + 1) % _cartoCdns.length];
    urls.add('https://$cdn2.basemaps.cartocdn.com/dark_all/$zoom/$tx/$ty.png');

    // 4. OpenStreetMap standard tiles (light, but always available as last resort)
    final osmCdn = ['a', 'b', 'c'][tx % 3];
    urls.add('https://$osmCdn.tile.openstreetmap.org/$zoom/$tx/$ty.png');

    return urls;
  }

  static bool _looksLikeImage(Uint8List bytes) {
    if (bytes.length < 8) return false;
    // PNG magic: 89 50 4E 47
    if (bytes[0] == 0x89 && bytes[1] == 0x50) return true;
    // JPEG magic: FF D8
    if (bytes[0] == 0xFF && bytes[1] == 0xD8) return true;
    // WebP: 52 49 46 46 ... 57 45 42 50
    if (bytes[0] == 0x52 && bytes[1] == 0x49) return true;
    return false;
  }

  static void _putCache(String key, Uint8List bytes) {
    if (_order.length >= _maxCache) {
      final oldest = _order.removeAt(0);
      _ok.remove(oldest);
    }
    _ok[key] = bytes;
    _failed.remove(key); // clear any prior failure
    _order.add(key);
  }

  static Future<ui.Image?> _decode(Uint8List bytes) async {
    try {
      final codec = await ui.instantiateImageCodec(
        bytes,
        targetWidth: _tw,
        targetHeight: _tw,
      );
      final frame = await codec.getNextFrame();
      return frame.image;
    } catch (_) {
      return null;
    }
  }

  // ── Drawing ───────────────────────────────────────────────────────────────

  static void _drawLoadingOverlay(ui.Canvas c) {
    // Grid pattern so it doesn't just look black
    final gridPaint = ui.Paint()
      ..color = const Color(0xFF111B28)
      ..strokeWidth = 1
      ..style = ui.PaintingStyle.stroke;
    const step = 32.0;
    for (double x = 0; x < _dw; x += step) {
      c.drawLine(Offset(x, 0), Offset(x, _dh.toDouble()), gridPaint);
    }
    for (double y = 0; y < _dh; y += step) {
      c.drawLine(Offset(0, y), Offset(_dw.toDouble(), y), gridPaint);
    }
    _text(
      c,
      'Loading map tiles…',
      x: 0,
      y: _dh / 2 - 12,
      size: 18,
      color: _muted,
      align: ui.TextAlign.center,
      maxW: _dw.toDouble(),
    );
    _text(
      c,
      'Check network connection if this persists',
      x: 0,
      y: _dh / 2 + 12,
      size: 12,
      color: const Color(0xFF2A3A4A),
      align: ui.TextAlign.center,
      maxW: _dw.toDouble(),
    );
  }

  static void _drawRoute(
    ui.Canvas c,
    List<LatLng> pts,
    double cfx,
    double cfy,
    int zoom,
  ) {
    if (pts.length < 2) return;

    final path = ui.Path();
    bool first = true;
    for (final pt in pts) {
      final px = (_lon2t(pt.lng, zoom) - cfx) * _tw + _dw / 2;
      final py = (_lat2t(pt.lat, zoom) - cfy) * _tw + _dh / 2;
      if (first) {
        path.moveTo(px, py);
        first = false;
      } else {
        path.lineTo(px, py);
      }
    }

    // Layer 1 — dark drop shadow (gives depth, like Google Maps)
    c.drawPath(
      path,
      ui.Paint()
        ..color = const Color(0x66000000)
        ..strokeWidth = 14
        ..style = ui.PaintingStyle.stroke
        ..strokeCap = ui.StrokeCap.round
        ..strokeJoin = ui.StrokeJoin.round,
    );

    // Layer 2 — white border (Google Maps style outline)
    c.drawPath(
      path,
      ui.Paint()
        ..color = const Color(0xFFFFFFFF)
        ..strokeWidth = 10
        ..style = ui.PaintingStyle.stroke
        ..strokeCap = ui.StrokeCap.round
        ..strokeJoin = ui.StrokeJoin.round,
    );

    // Layer 3 — bright Google-blue fill line
    c.drawPath(
      path,
      ui.Paint()
        ..color =
            const Color(0xFF4285F4) // Google Maps blue
        ..strokeWidth = 7
        ..style = ui.PaintingStyle.stroke
        ..strokeCap = ui.StrokeCap.round
        ..strokeJoin = ui.StrokeJoin.round,
    );
  }

  static void _drawDestPin(ui.Canvas c, Offset pos) {
    final fill = ui.Paint()
      ..color = _destColor
      ..style = ui.PaintingStyle.fill;
    c.drawCircle(pos.translate(0, -14), 10, fill);
    final spike = ui.Path()
      ..moveTo(pos.dx - 7, pos.dy - 14)
      ..lineTo(pos.dx + 7, pos.dy - 14)
      ..lineTo(pos.dx, pos.dy)
      ..close();
    c.drawPath(spike, fill);
    c.drawCircle(
      pos.translate(0, -14),
      4,
      ui.Paint()
        ..color = _white
        ..style = ui.PaintingStyle.fill,
    );
  }

  static void _drawPositionArrow(ui.Canvas c, double heading, bool headingUp) {
    const cx = _dw / 2.0;
    const cy = _dh / 2.0;

    // Outer glow ring
    c.drawCircle(
      const Offset(cx, cy),
      15,
      ui.Paint()
        ..color = const Color(0x6600CCFF)
        ..style = ui.PaintingStyle.fill,
    );

    c.save();
    c.translate(cx, cy);
    // In heading-up: map rotates, arrow always points forward (up).
    // In north-up: map fixed, arrow shows heading direction.
    if (!headingUp) c.rotate(heading * math.pi / 180);

    final arrow = ui.Path()
      ..moveTo(0, -26) // Slightly longer tip
      ..lineTo(16, 11)
      ..lineTo(0, 5)
      ..lineTo(-16, 11)
      ..close();

    c.drawPath(
      arrow,
      ui.Paint()
        ..color = _cyan
        ..style = ui.PaintingStyle.fill,
    );
    c.drawPath(
      arrow,
      ui.Paint()
        ..color = _white
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 3.5,
    );
    c.restore();
  }

  static void _drawCompass(ui.Canvas c, double heading) {
    final cx = _dw - 24.0;
    const cy = 24.0;
    const r = 18.0;

    c.drawCircle(
      Offset(cx, cy),
      r,
      ui.Paint()
        ..color = const Color(0xCC050A10)
        ..style = ui.PaintingStyle.fill,
    );
    c.drawCircle(
      Offset(cx, cy),
      r,
      ui.Paint()
        ..color = const Color(0x5500CCFF)
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 1,
    );

    c.save();
    c.translate(cx, cy);
    c.rotate(heading * math.pi / 180);

    final north = ui.Path()
      ..moveTo(0, -14)
      ..lineTo(4, 2)
      ..lineTo(-4, 2)
      ..close();
    c.drawPath(
      north,
      ui.Paint()
        ..color = const Color(0xFFFF4422)
        ..style = ui.PaintingStyle.fill,
    );

    final south = ui.Path()
      ..moveTo(0, 14)
      ..lineTo(4, -2)
      ..lineTo(-4, -2)
      ..close();
    c.drawPath(
      south,
      ui.Paint()
        ..color = _muted
        ..style = ui.PaintingStyle.fill,
    );
    c.restore();
  }

  static void _drawInstructionBar(
    ui.Canvas c,
    NavInstruction? instruction,
    String? destLabel,
  ) {
    if (instruction == null && destLabel == null) return;

    const h = 68.0;
    final y = _dh - h;

    c.drawRect(Rect.fromLTWH(0, y, _dw.toDouble(), h), _fill(_overlayBg));
    c.drawRect(
      Rect.fromLTWH(0, y, _dw.toDouble(), 1),
      _fill(const Color(0x3300CCFF)),
    );

    if (instruction != null) {
      _drawGeometricManeuver(c, instruction.maneuver, 160, y + h / 2);

      final textStartX = (_dw / 2.0) - 25;

      if (instruction.distanceText.isNotEmpty) {
        _text(
          c,
          instruction.distanceText,
          x: textStartX,
          y: y + 8,
          size: 36,
          color: _white,
          bold: true,
          align: ui.TextAlign.left,
          maxW: 210,
        );
      }
      if (instruction.streetName.isNotEmpty) {
        _text(
          c,
          instruction.streetName,
          x: textStartX,
          y: y + 44,
          size: 20,
          color: const Color(0xFFAABBCC),
          align: ui.TextAlign.left,
          maxW: 210,
        );
      }
    } else if (destLabel != null) {
      _text(
        c,
        '▶  $destLabel',
        x: 10,
        y: y + 14,
        size: 17,
        color: _muted,
        align: ui.TextAlign.left,
        maxW: _dw - 14.0,
      );
    }
  }

  static void _drawArrivedBar(ui.Canvas c) {
    const h = 52.0;
    final y = _dh - h;
    c.drawRect(
      Rect.fromLTWH(0, y, _dw.toDouble(), h),
      _fill(const Color(0xDD001A08)),
    );
    c.drawRect(
      Rect.fromLTWH(0, y, _dw.toDouble(), 1),
      _fill(const Color(0x3300FF88)),
    );
    _text(
      c,
      '✓  ARRIVED',
      x: 10,
      y: y + 8,
      size: 22,
      color: _green,
      bold: true,
      align: ui.TextAlign.left,
      maxW: _dw - 14.0,
    );
    _text(
      c,
      'You have reached your destination',
      x: 10,
      y: y + 32,
      size: 13,
      color: const Color(0xFF448844),
      align: ui.TextAlign.left,
      maxW: _dw - 14.0,
    );
  }

  // ── Tile math ─────────────────────────────────────────────────────────────

  static double _lon2t(double lng, int zoom) => (lng + 180) / 360 * (1 << zoom);

  static double _lat2t(double lat, int zoom) {
    final r = lat.clamp(-85.05, 85.05) * math.pi / 180;
    return (1 - math.log(math.tan(r) + 1 / math.cos(r)) / math.pi) /
        2 *
        (1 << zoom);
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  static void _text(
    ui.Canvas c,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    bool bold = false,
    required ui.TextAlign align,
    required double maxW,
  }) {
    final builder =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: align,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
              maxLines: 1,
              ellipsis: '…',
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            ),
          )
          ..addText(text);
    final para = builder.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  static ui.Paint _fill(Color color) => ui.Paint()
    ..color = color
    ..style = ui.PaintingStyle.fill;

  // Draw a scaled/simplified version of the maneuver arrow into the bottom bar
  static void _drawGeometricManeuver(
    ui.Canvas c,
    Maneuver m,
    double centerX,
    double centerY,
  ) {
    // Utility for arrowhead
    void arrowhead(ui.Canvas c, Offset tip, double angle, {double size = 16}) {
      final p = ui.Paint()
        ..color = _cyan
        ..style = ui.PaintingStyle.fill;
      final path = ui.Path()
        ..moveTo(tip.dx, tip.dy)
        ..lineTo(
          tip.dx + size * math.cos(angle + math.pi * 0.75),
          tip.dy + size * math.sin(angle + math.pi * 0.75),
        )
        ..lineTo(
          tip.dx + size * math.cos(angle - math.pi * 0.75),
          tip.dy + size * math.sin(angle - math.pi * 0.75),
        )
        ..close();
      c.drawPath(path, p);
    }

    final pLine = ui.Paint()
      ..color = _cyan
      ..style = ui.PaintingStyle.stroke
      ..strokeWidth = 10
      ..strokeCap = ui.StrokeCap.round
      ..strokeJoin = ui.StrokeJoin.round;

    final size = 32.0;
    final top = centerY - size / 2;
    final bot = centerY + size / 2;

    switch (m) {
      case Maneuver.straight:
      case Maneuver.unknown:
      case Maneuver.merge:
        c.drawLine(Offset(centerX, bot), Offset(centerX, top + 8), pLine);
        arrowhead(c, Offset(centerX, top), -math.pi / 2);
      case Maneuver.turnRight:
      case Maneuver.slightRight:
      case Maneuver.sharpRight:
        c.drawLine(
          Offset(centerX - 10, bot),
          Offset(centerX - 10, centerY - 6),
          pLine,
        );
        final path = ui.Path()
          ..moveTo(centerX - 10, centerY - 6)
          ..quadraticBezierTo(
            centerX - 10,
            centerY - 14,
            centerX + 10,
            centerY - 14,
          );
        c.drawPath(path, pLine);
        arrowhead(c, Offset(centerX + 18, centerY - 14), 0);
      case Maneuver.turnLeft:
      case Maneuver.slightLeft:
      case Maneuver.sharpLeft:
        c.drawLine(
          Offset(centerX + 10, bot),
          Offset(centerX + 10, centerY - 6),
          pLine,
        );
        final path = ui.Path()
          ..moveTo(centerX + 10, centerY - 6)
          ..quadraticBezierTo(
            centerX + 10,
            centerY - 14,
            centerX - 10,
            centerY - 14,
          );
        c.drawPath(path, pLine);
        arrowhead(c, Offset(centerX - 18, centerY - 14), math.pi);
      case Maneuver.uTurn:
        c.drawLine(
          Offset(centerX + 8, bot),
          Offset(centerX + 8, centerY - 6),
          pLine,
        );
        final rect = Rect.fromLTRB(
          centerX - 8,
          centerY - 16,
          centerX + 12,
          centerY,
        );
        c.drawArc(rect, 0, -math.pi, false, pLine);
        c.drawLine(
          Offset(centerX - 8, centerY - 6),
          Offset(centerX - 8, bot - 4),
          pLine,
        );
        arrowhead(c, Offset(centerX - 8, bot), math.pi / 2, size: 14);
      case Maneuver.roundabout:
        c.drawCircle(Offset(centerX, centerY), 12, pLine);
        arrowhead(c, Offset(centerX + 12, centerY), math.pi / 2, size: 12);
      case Maneuver.arrive:
        final path = ui.Path()
          ..moveTo(centerX - 12, centerY)
          ..lineTo(centerX - 4, centerY + 8)
          ..lineTo(centerX + 16, centerY - 10);
        c.drawPath(
          path,
          ui.Paint()
            ..color = _green
            ..style = ui.PaintingStyle.stroke
            ..strokeWidth = 8
            ..strokeCap = ui.StrokeCap.round,
        );
    }
  }
}
