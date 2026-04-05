import 'dart:math' as math;
import 'dart:typed_data';
import 'package:http/http.dart' as http;

/// Fetches Mapbox Static Images for the navigation map mode.
///
/// The service caches the last fetched tile and only refetches when the
/// device has moved > [minDistMeters] or heading has changed > [minBearingDeg].
/// This keeps API usage low while still showing a current map.
class MapTileService {
  final String apiKey;

  MapTileService({required this.apiKey});

  // ── Cache state ───────────────────────────────────────────────────────────
  Uint8List? _cachedBytes;
  double?    _cachedLat;
  double?    _cachedLng;
  double?    _cachedBearing;
  int?       _cachedZoom;

  static const double _minDistMeters = 15.0;
  static const double _minBearingDeg = 12.0;

  bool get hasCachedTile => _cachedBytes != null;

  /// Returns the cached tile bytes, or null if no cache exists.
  Uint8List? get cachedTile => _cachedBytes;

  void clearCache() {
    _cachedBytes   = null;
    _cachedLat     = null;
    _cachedLng     = null;
    _cachedBearing = null;
    _cachedZoom    = null;
  }

  // ── Public fetch ──────────────────────────────────────────────────────────

  /// Fetch a dark map tile centred on [lat],[lng] at the given [zoom] and
  /// optional [bearing] (degrees, 0 = north-up, matches heading-up if set).
  ///
  /// [destLat] / [destLng]: if provided, a red destination pin is added.
  /// Returns the JPEG bytes (Mapbox-rendered), or null on error.
  Future<Uint8List?> fetchTile({
    required double lat,
    required double lng,
    required int    width,
    required int    height,
    double bearing      = 0,
    int    zoom         = 16,
    double? destLat,
    double? destLng,
    bool   headingUp    = true,
  }) async {
    if (apiKey.isEmpty) return null;

    final effectiveBearing = headingUp ? bearing : 0.0;

    // Return cache if position / heading / zoom haven't changed enough
    if (_isCacheValid(lat, lng, effectiveBearing, zoom)) return _cachedBytes;

    final url = _buildUrl(
      lat: lat, lng: lng,
      width: width, height: height,
      bearing: effectiveBearing,
      zoom: zoom,
      destLat: destLat,
      destLng: destLng,
    );

    try {
      final response = await http.get(Uri.parse(url))
          .timeout(const Duration(seconds: 5));

      if (response.statusCode == 200) {
        _cachedBytes   = response.bodyBytes;
        _cachedLat     = lat;
        _cachedLng     = lng;
        _cachedBearing = effectiveBearing;
        _cachedZoom    = zoom;
        return _cachedBytes;
      }
      // On error return stale cache so the HUD doesn't go blank
      return _cachedBytes;
    } catch (_) {
      return _cachedBytes;
    }
  }

  // ── URL builder ───────────────────────────────────────────────────────────

  String _buildUrl({
    required double lat,
    required double lng,
    required int    width,
    required int    height,
    required double bearing,
    required int    zoom,
    double? destLat,
    double? destLng,
  }) {
    // Overlays: destination pin (if set) + current-position indicator
    final overlays = <String>[];

    if (destLat != null && destLng != null) {
      // Red flag at destination
      overlays.add(
        'pin-l-flag+ff3300'
        '(${_fmt(destLng)},${_fmt(destLat)})',
      );
    }

    // Cyan pulsing dot at current position
    overlays.add(
      'pin-l-circle+00ccff'
      '(${_fmt(lng)},${_fmt(lat)})',
    );

    final overlayStr = overlays.join(',');
    final bear = bearing.round().clamp(0, 359);

    // Mapbox Static Images endpoint:
    // /styles/v1/{username}/{style_id}/static/{overlays}/{lon},{lat},{zoom},{bearing},{pitch}/{width}x{height}
    return 'https://api.mapbox.com/styles/v1/mapbox/dark-v11/static'
        '/$overlayStr'
        '/${_fmt(lng)},${_fmt(lat)},$zoom,$bear,0'
        '/${width}x$height'
        '?access_token=$apiKey';
  }

  // ── Cache validity ────────────────────────────────────────────────────────

  bool _isCacheValid(double lat, double lng, double bearing, int zoom) {
    if (_cachedBytes == null) return false;
    if (_cachedZoom != zoom) return false;

    if (_cachedLat != null && _cachedLng != null) {
      if (_haversine(lat, lng, _cachedLat!, _cachedLng!) > _minDistMeters) {
        return false;
      }
    }

    if (_cachedBearing != null) {
      final diff = (bearing - _cachedBearing!).abs() % 360;
      final norm = diff > 180 ? 360 - diff : diff;
      if (norm > _minBearingDeg) return false;
    }

    return true;
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  static String _fmt(double v) => v.toStringAsFixed(6);

  /// Haversine distance in metres between two lat/lng points.
  static double _haversine(double lat1, double lng1, double lat2, double lng2) {
    const R = 6371000.0;
    final dLat = (lat2 - lat1) * math.pi / 180;
    final dLng = (lng2 - lng1) * math.pi / 180;
    final a = math.sin(dLat / 2) * math.sin(dLat / 2) +
        math.cos(lat1 * math.pi / 180) *
            math.cos(lat2 * math.pi / 180) *
            math.sin(dLng / 2) *
            math.sin(dLng / 2);
    return R * 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a));
  }
}
