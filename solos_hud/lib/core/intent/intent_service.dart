import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;

// ── Shared destination ────────────────────────────────────────────────────────

class SharedDestination {
  final String    label;
  final String?   rawUrl;
  final double?   lat;
  final double?   lng;
  final DateTime  receivedAt;

  SharedDestination({
    required this.label,
    this.rawUrl,
    this.lat,
    this.lng,
  }) : receivedAt = DateTime.now();

  String get googleMapsUrl {
    if (rawUrl != null) return rawUrl!;
    if (lat != null && lng != null) return 'geo:$lat,$lng';
    return 'https://maps.google.com/?q=${Uri.encodeComponent(label)}';
  }

  String get shortLabel {
    final l = label.startsWith('http') ? 'Destination' : label;
    if (l.length <= 40) return l;
    return '${l.substring(0, 37)}…';
  }

  @override
  String toString() => label;
}

// ── Service ───────────────────────────────────────────────────────────────────

class IntentService extends ChangeNotifier {
  static const _channel = MethodChannel('solos_intent');

  SharedDestination? _destination;
  SharedDestination? get destination => _destination;

  bool _initialized = false;

  Future<void> init() async {
    if (_initialized) return;
    _initialized = true;

    _channel.setMethodCallHandler((call) async {
      if (call.method == 'onSharedIntent') {
        final map = Map<String, dynamic>.from(call.arguments as Map);
        _handleData(map);
      }
    });

    try {
      final raw = await _channel.invokeMethod<Map>('getInitialIntent');
      if (raw != null) {
        _handleData(Map<String, dynamic>.from(raw));
      }
    } catch (_) {}
  }

  void _handleData(Map<String, dynamic> data) {
    final type = data['type'] as String?;
    SharedDestination? dest;

    switch (type) {
      case 'geo_uri':
        dest = _parseGeoUri(data['uri'] as String);
      case 'maps_url':
        dest = _parseMapsUrl(data['url'] as String);
      case 'share_text':
        dest = _parseShareText(data['text'] as String);
    }

    if (dest != null) {
      _destination = dest;
      notifyListeners();

      // Resolve coordinates in background if not already present.
      // We try two parallel strategies and use whichever wins:
      //   1. Geocode the place label with Nominatim (fast, reliable)
      //   2. Follow URL redirects to extract @lat,lng from the final URL
      if (dest.lat == null) {
        _resolveCoordinates(dest);
      }
    }
  }

  Future<void> _resolveCoordinates(SharedDestination partial) async {
    // Run both strategies in parallel — whichever gives coords first wins.
    final results = await Future.wait([
      _geocodeLabel(partial.label),
      if (partial.rawUrl != null) _resolveFromUrl(partial.rawUrl!)
          else Future.value(null),
    ]);

    // Bail if destination changed while we were fetching
    if (_destination?.rawUrl != partial.rawUrl &&
        _destination?.label   != partial.label) return;

    // Pick the first non-null result
    SharedDestination? winner;
    for (final r in results) {
      if (r != null && r.lat != null) { winner = r; break; }
    }
    if (winner == null) return;

    _destination = SharedDestination(
      label:  (winner.label.isNotEmpty && !winner.label.startsWith('http'))
                  ? winner.label
                  : partial.label,
      rawUrl: partial.rawUrl,
      lat:    winner.lat,
      lng:    winner.lng,
    );
    notifyListeners();
  }

  // ── Strategy 1: geocode the place name via Nominatim ─────────────────────
  // Free, no API key, works from any place name string.

  static Future<SharedDestination?> _geocodeLabel(String label) async {
    if (label.isEmpty || label.startsWith('http')) return null;
    try {
      final uri = Uri.parse(
        'https://nominatim.openstreetmap.org/search'
        '?q=${Uri.encodeComponent(label)}'
        '&format=json&limit=1&addressdetails=0',
      );
      final resp = await http
          .get(uri, headers: {
            'User-Agent':  'SolosHUD/1.0',
            'Accept-Language': 'en',
          })
          .timeout(const Duration(seconds: 6));

      if (resp.statusCode != 200) return null;
      final list = jsonDecode(resp.body) as List?;
      if (list == null || list.isEmpty) return null;

      final item = list[0] as Map<String, dynamic>;
      final lat  = double.tryParse(item['lat']?.toString() ?? '');
      final lng  = double.tryParse(item['lon']?.toString() ?? '');
      if (lat == null || lng == null) return null;

      return SharedDestination(
        label: (item['display_name'] as String?)
                   ?.split(',').first.trim() ?? label,
        lat:   lat,
        lng:   lng,
      );
    } catch (_) {
      return null;
    }
  }

  // ── Strategy 2: follow the redirect and parse @lat,lng from the final URL ─

  static Future<SharedDestination?> _resolveFromUrl(String url) async {
    try {
      // Use a browser User-Agent so Google serves a proper HTTP redirect
      // rather than a JavaScript-redirect HTML page.
      final resp = await http
          .get(Uri.parse(url), headers: {
            'User-Agent': 'Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36',
            'Accept':     'text/html',
          })
          .timeout(const Duration(seconds: 8));

      // Extract the final URL from the response headers OR from the body.
      final bodyMatch = RegExp(r'https://www\.google\.com/maps/place[^"<>\s]+')
          .firstMatch(resp.body)?.group(0) ?? '';
      final sources = <String>[
        resp.request?.url.toString() ?? '',
        resp.headers['location'] ?? '',
        resp.headers['x-final-url'] ?? '',
        bodyMatch,
      ];

      for (final candidate in sources) {
        if (candidate.isEmpty) continue;
        final result = _parseMapsUrl(candidate);
        if (result?.lat != null) return result;
      }
      return null;
    } catch (_) {
      return null;
    }
  }

  // ── Parsers ───────────────────────────────────────────────────────────────

  SharedDestination? _parseGeoUri(String uri) {
    try {
      final parsed = Uri.parse(uri);
      final query  = parsed.queryParameters['q'] ?? '';
      final coords = parsed.path.split(',');
      double? lat = double.tryParse(coords.isNotEmpty ? coords[0] : '');
      double? lng = double.tryParse(coords.length > 1  ? coords[1] : '');
      final label = query.isNotEmpty
          ? Uri.decodeComponent(query)
          : (lat != null && lng != null)
              ? '${lat.toStringAsFixed(5)}, ${lng.toStringAsFixed(5)}'
              : uri;
      return SharedDestination(label: label, rawUrl: uri, lat: lat, lng: lng);
    } catch (_) { return null; }
  }

  static SharedDestination? _parseMapsUrl(String url) {
    try {
      final parsed = Uri.parse(url);
      final q      = parsed.queryParameters['q'] ??
                     parsed.queryParameters['query'] ?? '';

      final placeMatch = RegExp(r'/place/([^/@]+)').firstMatch(parsed.path);
      final placeName  = placeMatch?.group(1) != null
          ? Uri.decodeComponent(placeMatch!.group(1)!.replaceAll('+', ' '))
          : null;

      // @lat,lng or ll=lat,lng or query=lat,lng
      double? lat, lng;
      final atMatch = RegExp(r'@(-?\d+\.\d+),(-?\d+\.\d+)').firstMatch(url);
      if (atMatch != null) {
        lat = double.tryParse(atMatch.group(1)!);
        lng = double.tryParse(atMatch.group(2)!);
      }
      if (lat == null) {
        final llMatch = RegExp(r'll=(-?\d+\.\d+),(-?\d+\.\d+)').firstMatch(url);
        if (llMatch != null) {
          lat = double.tryParse(llMatch.group(1)!);
          lng = double.tryParse(llMatch.group(2)!);
        }
      }

      final label = placeName ?? q.replaceAll('+', ' ');
      return SharedDestination(
        label:  label.isNotEmpty ? label : url,
        rawUrl: url,
        lat:    lat,
        lng:    lng,
      );
    } catch (_) { return null; }
  }

  SharedDestination? _parseShareText(String text) {
    final lines = text.split('\n').map((l) => l.trim())
        .where((l) => l.isNotEmpty).toList();

    String? url;
    String  label = '';

    for (final line in lines) {
      if (line.startsWith('http') &&
          (line.contains('maps') || line.contains('goo.gl'))) {
        url = line;
        final parsed = _parseMapsUrl(line);
        if (parsed != null && parsed.label.isNotEmpty &&
            !parsed.label.startsWith('http') && label.isEmpty) {
          label = parsed.label;
        }
      } else if (label.isEmpty) {
        label = line;
      }
    }

    if (label.isEmpty && url == null) return null;
    return SharedDestination(label: label.isNotEmpty ? label : url!, rawUrl: url);
  }

  void clearDestination() {
    _destination = null;
    notifyListeners();
  }
}
