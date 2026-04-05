import 'dart:convert';
import 'dart:math' as math;
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

// ── Simple LatLng ─────────────────────────────────────────────────────────────

class LatLng {
  final double lat;
  final double lng;
  const LatLng(this.lat, this.lng);

  // Round to ~11 m grid so minor GPS jitter doesn't re-trigger a route fetch
  LatLng get rounded => LatLng(
        (lat * 10000).round() / 10000,
        (lng * 10000).round() / 10000,
      );

  @override
  bool operator ==(Object other) =>
      other is LatLng && other.lat == lat && other.lng == lng;

  @override
  int get hashCode => Object.hash(lat, lng);
}

// ── Route result ──────────────────────────────────────────────────────────────

class RouteResult {
  final List<LatLng> points;
  final double distanceMeters;
  final double durationSeconds;

  const RouteResult({
    required this.points,
    required this.distanceMeters,
    required this.durationSeconds,
  });

  String get distanceLabel {
    if (distanceMeters >= 1000) {
      return '${(distanceMeters / 1000).toStringAsFixed(1)} km';
    }
    return '${distanceMeters.round()} m';
  }

  String get durationLabel {
    final m = (durationSeconds / 60).round();
    if (m >= 60) return '${m ~/ 60}h ${m % 60}min';
    return '${m}min';
  }
}

// ── Status ────────────────────────────────────────────────────────────────────

enum RouteStatus { idle, loading, ready, error }

// ── Service ───────────────────────────────────────────────────────────────────

/// Fetches a driving route between two points using the free OSRM public API.
/// No API key required. Notifies listeners when state changes.
class RouteService extends ChangeNotifier {
  // Multiple routing endpoints tried in order.
  // OSRM public demo servers are often rate-limited; GraphHopper is the
  // most reliable free fallback (2500 req/day, no key needed for basic use).
  static const _endpoints = [
    'https://router.project-osrm.org/route/v1/driving',
    'https://routing.openstreetmap.de/routed-car/route/v1/driving',
    // GraphHopper — format is different, handled below
  ];
  static const _graphhopperUrl =
      'https://graphhopper.com/api/1/route';
  static const _timeout = Duration(seconds: 8);

  RouteResult? _route;
  RouteStatus  _status    = RouteStatus.idle;
  String?      _error;
  LatLng?      _cachedDest; // rounded destination key
  bool         _disposed  = false;

  RouteResult? get route   => _route;
  RouteStatus  get status  => _status;
  String?      get error   => _error;
  bool get isLoading       => _status == RouteStatus.loading;
  bool get hasRoute        => _route != null;

  // ── Public API ─────────────────────────────────────────────────────────────

  /// Returns the cached route if destination unchanged, else kicks off a fetch.
  /// Callers should listen to this service for state changes.
  RouteResult? getRouteAsync(LatLng from, LatLng to) {
    final roundedTo = to.rounded;
    if (_cachedDest == roundedTo && _route != null) return _route;
    if (_status != RouteStatus.loading) _fetchRoute(from, roundedTo);
    return _route; // return stale while fetching
  }

  /// Force a re-fetch (e.g. from a Retry button).
  void retry(LatLng from, LatLng to) {
    _status    = RouteStatus.idle;
    _cachedDest = null;
    _notify();
    _fetchRoute(from, to.rounded);
  }

  void clearCache() {
    _route      = null;
    _status     = RouteStatus.idle;
    _error      = null;
    _cachedDest = null;
    _notify();
  }

  // ── Internal ───────────────────────────────────────────────────────────────

  Future<void> _fetchRoute(LatLng from, LatLng to) async {
    _status = RouteStatus.loading;
    _error  = null;
    _notify();

    // Try each endpoint in order — use the first that succeeds
    for (final base in _endpoints) {
      if (_disposed) return;
      try {
        final uri = Uri.parse(
          '$base/${from.lng},${from.lat};${to.lng},${to.lat}'
          '?overview=full&geometries=polyline',
        );

        final response = await http
            .get(uri, headers: {'User-Agent': 'SolosHUD/1.0'})
            .timeout(_timeout);

        if (_disposed) return;
        if (response.statusCode != 200) continue; // try next endpoint

        final body   = jsonDecode(response.body) as Map<String, dynamic>;
        final code   = body['code'] as String?;
        if (code != 'Ok') continue;

        final routes = body['routes'] as List?;
        if (routes == null || routes.isEmpty) continue;

        final r = routes[0] as Map<String, dynamic>;

        _route = RouteResult(
          points:          _decodePolyline(r['geometry'] as String),
          distanceMeters:  (r['distance'] as num).toDouble(),
          durationSeconds: (r['duration'] as num).toDouble(),
        );
        _cachedDest = to;
        _status     = RouteStatus.ready;
        _notify();
        return; // success — done
      } catch (_) {
        continue; // timeout or parse error — try next endpoint
      }
    }

    // OSRM endpoints all failed — try GraphHopper (different URL format)
    if (!_disposed) {
      final ghResult = await _tryGraphHopper(from, to);
      if (ghResult != null && !_disposed) {
        _route     = ghResult;
        _cachedDest = to;
        _status    = RouteStatus.ready;
        _notify();
        return;
      }
    }

    if (!_disposed) {
      _status = RouteStatus.error;
      _error  = 'Could not calculate route. Check connection.';
      _notify();
    }
  }

  Future<RouteResult?> _tryGraphHopper(LatLng from, LatLng to) async {
    try {
      // GraphHopper public API — no key for basic usage
      final uri = Uri.parse(
        '$_graphhopperUrl'
        '?point=${from.lat},${from.lng}'
        '&point=${to.lat},${to.lng}'
        '&type=json&points_encoded=true&vehicle=car&locale=en',
      );
      final resp = await http
          .get(uri, headers: {'User-Agent': 'SolosHUD/1.0'})
          .timeout(_timeout);
      if (resp.statusCode != 200) return null;
      final body  = jsonDecode(resp.body) as Map<String, dynamic>;
      final paths = body['paths'] as List?;
      if (paths == null || paths.isEmpty) return null;
      final path     = paths[0] as Map<String, dynamic>;
      final encoded  = path['points'] as String;
      final dist     = (path['distance'] as num).toDouble();
      final dur      = (path['time']     as num).toDouble() / 1000;
      return RouteResult(
        points:          _decodePolyline(encoded),
        distanceMeters:  dist,
        durationSeconds: dur,
      );
    } catch (_) {
      return null;
    }
  }

  void _notify() {
    if (!_disposed) notifyListeners();
  }

  @override
  void dispose() {
    _disposed = true;
    super.dispose();
  }

  // ── Google Encoded Polyline decoder ────────────────────────────────────────

  static List<LatLng> _decodePolyline(String encoded) {
    final result = <LatLng>[];
    int index = 0, lat = 0, lng = 0;

    while (index < encoded.length) {
      int b, shift = 0, result_ = 0;
      do {
        b        = encoded.codeUnitAt(index++) - 63;
        result_ |= (b & 0x1f) << shift;
        shift   += 5;
      } while (b >= 0x20);
      lat += (result_ & 1) != 0 ? ~(result_ >> 1) : (result_ >> 1);

      shift = 0; result_ = 0;
      do {
        b        = encoded.codeUnitAt(index++) - 63;
        result_ |= (b & 0x1f) << shift;
        shift   += 5;
      } while (b >= 0x20);
      lng += (result_ & 1) != 0 ? ~(result_ >> 1) : (result_ >> 1);

      result.add(LatLng(lat / 1e5, lng / 1e5));
    }

    return result;
  }

  // ── Distance helper ───────────────────────────────────────────────────────

  static double haversineMeters(LatLng a, LatLng b) {
    const R   = 6371000.0;
    final dLat = (b.lat - a.lat) * math.pi / 180;
    final dLng = (b.lng - a.lng) * math.pi / 180;
    final x = math.sin(dLat / 2) * math.sin(dLat / 2) +
        math.cos(a.lat * math.pi / 180) *
            math.cos(b.lat * math.pi / 180) *
            math.sin(dLng / 2) *
            math.sin(dLng / 2);
    return R * 2 * math.atan2(math.sqrt(x), math.sqrt(1 - x));
  }
}
