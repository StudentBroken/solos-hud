import 'dart:typed_data';
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import '../../core/solos_protocol.dart';
import '../../core/notifications/notification_service.dart';
import '../../core/intent/intent_service.dart';
import '../../core/gps/gps_service.dart';
import '../../core/settings/app_settings.dart';
import '../../core/sensors/compass_service.dart';
import '../../core/routing/tile_map_renderer.dart';
import '../../core/routing/route_service.dart';
import 'navigation_renderer.dart';
import 'navigation_widget.dart';

export '../../core/routing/route_service.dart'
    show LatLng, RouteResult, RouteStatus;

enum NavDisplayMode { arrow, map }

class NavigationApp extends GlassesApp {
  final NotificationService notifications;
  final IntentService intents;
  final GpsService gps;
  final AppSettings settings;
  final CompassService compass;
  final RouteService routeService;

  NavigationApp({
    required this.notifications,
    required this.intents,
    required this.gps,
    required this.settings,
    required this.compass,
    required this.routeService,
  });

  @override
  String get id => 'navigation';
  @override
  String get name => 'Navigation';
  @override
  IconData get icon => Icons.navigation;
  @override
  String get description => 'Arrow or live map · Google Maps turn-by-turn';

  // ── Settings helpers ──────────────────────────────────────────────────────

  NavDisplayMode get displayMode {
    final v = settings.getAppValue('nav_mode', 'arrow') as String;
    return v == 'map' ? NavDisplayMode.map : NavDisplayMode.arrow;
  }

  bool get headingUp => settings.getAppValue('nav_heading_up', true) as bool;
  int get mapZoom => (settings.getAppValue('nav_zoom', 16) as num).toInt();
  String get mapboxKey => settings.getAppValue('nav_mapbox_key', '') as String;

  // Tell HudController which codec wraps our frames
  @override
  int get preferredCodec => displayMode == NavDisplayMode.map
      ? SolosProtocol.codecJPEG
      : SolosProtocol.codecRLE565;

  @override
  void onActivate() {
    compass.start();
  }

  @override
  void onDeactivate() {
    compass.stop();
    routeService.clearCache();
  }

  // ── Glasses button zoom ───────────────────────────────────────────────────
  // Only active in map mode; FRONT = zoom in (+), BACK = zoom out (−).
  @override
  bool onButtonEvent(GlassesEvent event) {
    if (displayMode != NavDisplayMode.map) return false;
    final action = event.action;
    final btn = event.button;
    if (btn == null) return false;
    if (action != GlassesActionType.buttonShort &&
        action != GlassesActionType.buttonMake) {
      return false;
    }

    final current = mapZoom;
    if (btn == GlassesButton.front && current < 18) {
      settings.setAppValue('nav_zoom', current + 1);
      return true;
    }
    if (btn == GlassesButton.back && current > 12) {
      settings.setAppValue('nav_zoom', current - 1);
      return true;
    }
    return false;
  }

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() async {
    try {
      final nav = notifications.currentNav;
      final dest = intents.destination;
      final arrived = nav?.isArrival ?? false;

      // Keep compass fused with latest GPS
      if (gps.available) {
        compass.updateGps(
          speedKmh: gps.data.speedKmh,
          gpsHeadingDeg: gps.data.heading,
        );
      }

      if (displayMode == NavDisplayMode.map) {
        return await _buildMapFrame(nav, dest, arrived);
      }

      // Arrow mode: rate-limit the same way as map mode.
      // Arrow RLE565 frames are 5-12 KB — sending them every tick creates
      // the same BT congestion that breaks A2DP music playback.
      return await _buildArrowFrame(nav, dest, arrived);
    } catch (e) {
      debugPrint('[NavigationApp] frame error: $e');
      return null; // HudController falls back to text renderer
    }
  }

  // ── Arrow frame rate limiter ──────────────────────────────────────────────
  DateTime?   _lastArrowRender;
  Uint8List?  _lastArrowFrame;
  static const _arrowMinInterval = Duration(milliseconds: 800);

  Future<Uint8List?> _buildArrowFrame(
    dynamic nav,
    SharedDestination? dest,
    bool arrived,
  ) async {
    final now       = DateTime.now();
    final canRender = _lastArrowRender == null ||
        now.difference(_lastArrowRender!) >= _arrowMinInterval;

    if (!canRender && _lastArrowFrame != null) return _lastArrowFrame!;

    final frame = await NavigationRenderer.render(
      instruction:      nav,
      destinationLabel: dest?.shortLabel,
      arrived:          arrived,
    );
    _lastArrowRender = now;
    _lastArrowFrame  = frame;
    return frame;
  }

  // ── Map frame rate limiter ────────────────────────────────────────────────
  // The map only needs 1 fps — tiles and GPS don't change faster than that.
  // Skipping renders keeps the BT write queue clear and prevents the memory
  // spikes that crash the BT connection when a destination is shared.
  DateTime?   _lastMapRender;
  Uint8List?  _lastMapFrame;
  static const _mapMinInterval = Duration(milliseconds: 1500);

  Future<Uint8List> _buildMapFrame(
    dynamic nav,
    SharedDestination? dest,
    bool arrived,
  ) async {
    final now = DateTime.now();
    final canRender = _lastMapRender == null ||
        now.difference(_lastMapRender!) >= _mapMinInterval;

    // Return the cached frame until enough time has passed
    if (!canRender && _lastMapFrame != null) return _lastMapFrame!;

    final hasGps  = gps.available;
    final gpsData = hasGps ? gps.data : null;
    final heading = compass.heading;

    List<LatLng>? route;
    if (hasGps && dest?.lat != null && dest?.lng != null) {
      final from = LatLng(gpsData!.latitude, gpsData.longitude);
      final to   = LatLng(dest!.lat!, dest.lng!);
      route = routeService.getRouteAsync(from, to)?.points;
    }

    final frame = await TileMapRenderer.render(
      lat: gpsData?.latitude ?? 38.7169,
      lng: gpsData?.longitude ?? -9.1395,
      heading: heading,
      zoom: mapZoom,
      route: route,
      destination: dest?.lat != null ? LatLng(dest!.lat!, dest.lng!) : null,
      instruction: nav,
      destLabel: dest?.shortLabel,
      headingUp: headingUp,
      arrived:     arrived,
      mapboxKey:   mapboxKey.isEmpty ? null : mapboxKey,
      useJpeg:     true,
    );
    _lastMapRender = now;
    _lastMapFrame  = frame;
    return frame;
  }

  // ── Settings entries ──────────────────────────────────────────────────────

  @override
  List<AppSettingEntry> get settingEntries => [
    const AppSettingEntry(
      key: 'nav_mapbox_key',
      label: 'Mapbox API Key (optional)',
      description: 'Premium dark tiles. Leave blank for free Carto tiles.',
      type: AppSettingType.text,
      defaultValue: '',
    ),
    const AppSettingEntry(
      key: 'nav_heading_up',
      label: 'Heading-Up Map',
      description: 'Rotate map so direction of travel is always up',
      type: AppSettingType.toggle,
      defaultValue: true,
    ),
    const AppSettingEntry(
      key: 'nav_zoom',
      label: 'Map Zoom Level',
      description: '14 = area · 16 = street · 18 = close-up',
      type: AppSettingType.slider,
      defaultValue: 16,
    ),
  ];

  // ── Preview ───────────────────────────────────────────────────────────────

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      NavigationPhoneWidget(app: this);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    final nav = notifications.currentNav;
    final dest = intents.destination;
    final mode = displayMode == NavDisplayMode.map ? '🗺' : '↱';

    if (nav != null) {
      return Text(
        '$mode  ${nav.maneuverLabel}  ${nav.distanceText}',
        style: Theme.of(context).textTheme.bodySmall?.copyWith(
          fontFamily: 'monospace',
          color: Colors.cyanAccent,
        ),
      );
    }
    if (dest != null) {
      return Text(
        '$mode  ▶ ${dest.shortLabel}',
        style: Theme.of(
          context,
        ).textTheme.bodySmall?.copyWith(color: Colors.white70),
        overflow: TextOverflow.ellipsis,
      );
    }
    return Text(
      '$mode  No active navigation',
      style: Theme.of(
        context,
      ).textTheme.bodySmall?.copyWith(color: Colors.grey),
    );
  }
}
