import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'core/bluetooth/ble_logger.dart';
import 'core/rfcomm/rfcomm_service.dart';
import 'core/rfcomm/glasses_event_service.dart';
import 'core/hr/heart_rate_service.dart';
import 'core/gps/gps_service.dart';
import 'core/hud_controller.dart';
import 'core/settings/app_settings.dart';
import 'core/glasses_app.dart';
import 'core/tilt_wake_service.dart';
import 'core/foreground_service_helper.dart';
import 'core/notifications/notification_service.dart';
import 'core/intent/intent_service.dart';
import 'core/sensors/compass_service.dart';
import 'core/routing/route_service.dart';
import 'core/hud_ui/glasses_menu_controller.dart';
import 'core/hud_ui/boot_screen_renderer.dart';
import 'core/weather/weather_service.dart';
import 'core/vesc/vesc_service.dart';
import 'core/voice/voice_service.dart';
import 'core/media/media_service.dart';
import 'core/notifications/notification_overlay_controller.dart';
import 'apps/weather/weather_app.dart';
import 'apps/vesc/vesc_app.dart';
import 'apps/assistant/assistant_app.dart';
import 'apps/music/music_app.dart';
import 'apps/custom_command/custom_command_app.dart';
import 'apps/speedometer/speedometer_app.dart';
import 'apps/dashboard/dashboard_app.dart';
import 'apps/navigation/navigation_app.dart';
import 'apps/running/running_app.dart';
import 'core/workout/workout_service.dart';
import 'core/sensors/speed_fusion_service.dart';
import 'apps/pacer/pacer_app.dart';
import 'screens/home_screen.dart';
import 'screens/settings_screen.dart';

class SolosHudApp extends StatelessWidget {
  const SolosHudApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Solos HUD',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF00BCD4), // cyan-600
          brightness: Brightness.dark,
          surface: const Color(0xFF0E0E14),
          surfaceContainerLow: const Color(0xFF131318),
          surfaceContainerHighest: const Color(0xFF1E1E26),
        ),
        useMaterial3: true,
        cardTheme: const CardThemeData(
          elevation: 0,
          margin: EdgeInsets.zero,
        ),
        listTileTheme: const ListTileThemeData(
          minVerticalPadding: 4,
        ),
        appBarTheme: const AppBarTheme(
          elevation: 0,
          scrolledUnderElevation: 1,
          surfaceTintColor: Colors.transparent,
        ),
        navigationBarTheme: NavigationBarThemeData(
          elevation: 0,
          surfaceTintColor: Colors.transparent,
          indicatorShape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10),
          ),
        ),
      ),
      home: const _Root(),
    );
  }
}

class _Root extends StatefulWidget {
  const _Root();

  @override
  State<_Root> createState() => _RootState();
}

class _RootState extends State<_Root> {
  late final AppSettings _settings;
  late final BleLogger _logger;
  late final RfcommService _rfcomm;
  late final GlassesEventService _glassesEvents;
  late final HeartRateService _hr;
  late final GpsService _gps;
  late final HudController _hud;
  late final TiltWakeService _tiltWake;
  late final NotificationService _notifications;
  late final IntentService _intents;
  late final CompassService _compass;
  late final WeatherService _weather;
  late final VescService _vesc;
  late final VoiceService _voice;
  late final MediaService _media;
  late final RouteService _routeService;
  late final WorkoutService _workout;
  late final SpeedFusionService _speedFusion;
  late final GlassesMenuController _glassesMenu;
  late final NotificationOverlayController _notifOverlay;
  late final List<GlassesApp> _apps;
  late final MusicApp _musicApp;

  final _fgService = ForegroundServiceHelper();
  bool _loaded = false;

  // Auto-reconnect state
  Timer? _reconnectTimer;
  int _reconnectAttempts = 0;
  static const _maxReconnectAttempts = 5;
  static const _reconnectDelays = [3, 5, 10, 20, 30]; // seconds

  @override
  void initState() {
    super.initState();
    _settings = AppSettings();
    _logger = BleLogger();
    _rfcomm = RfcommService(logger: _logger);
    _glassesEvents = GlassesEventService()..startListening();
    _hr = HeartRateService();
    _gps = GpsService();
    _hud = HudController(rfcomm: _rfcomm, settings: _settings);
    _notifications = NotificationService()..startListening();
    _intents = IntentService();
    _compass = CompassService();
    _weather = WeatherService(gps: _gps);
    _vesc = VescService(settings: _settings);
    _voice = VoiceService();
    _media = MediaService(notifications: _notifications);
    _workout = WorkoutService(gps: _gps);
    _speedFusion = SpeedFusionService();
    _voice.media = _media;
    _routeService = RouteService();
    _glassesMenu = GlassesMenuController(
      events: _glassesEvents,
      voice: _voice,
      onLaunchApp: _onMenuLaunch,
      onStartLoop: () {
        if (!_hud.running) _hud.startLoop();
      },
      onActiveAppButton: (event) => _hud.activeApp?.onButtonEvent(event),
    )..startListening();

    _notifOverlay = NotificationOverlayController(
      notifications: _notifications,
      hud: _hud,
      settings: _settings,
    );
    _glassesMenu.notifButtonHandler = _notifOverlay.handleButton;

    _tiltWake = TiltWakeService(
      rfcomm: _rfcomm,
      glassesEvents: _glassesEvents,
      settings: _settings,
      hud: _hud,
    );
    _hud.tiltWake = _tiltWake;
    _hud.menu = _glassesMenu;
    _hud.voice          = _voice;
    _hud.isMusicPlaying = () => _media.current?.isPlaying == true;
    _glassesMenu.media  = _media;

    // No media listener needed — voice handles pause/resume internally

    // HUD active-app changes → persist last app id + update notification
    _hud.addListener(_onHudChanged);

    // Listen to connection state → auto-start HUD + auto-reconnect
    _rfcomm.addListener(_onRfcommChanged);

    // Tilt-wake also ensures the service stays alive while active
    _tiltWake.addListener(() {
      if (_tiltWake.active) _ensureForegroundRunning();
    });

    _settings.load().then((_) async {
      _logger.enabled = _settings.logBle;
      await _intents.init();

      // When a destination is set (or updated with coordinates after URL resolution),
      // auto-switch to Navigation + map mode so the user immediately sees the route.
      _intents.addListener(_onDestinationChanged);
      // Sync persisted voice settings into the VoiceService
      _voice.apiKey = _settings.geminiKey;
      _voice.model = _settings.geminiModel;
      _voice.wakeWord = _settings.wakeWord;
      _voice.phoneAssistantMode = _settings.phoneAssistantMode;
      _voice.phoneAssistantType = _settings.phoneAssistantType;
      // Do NOT eagerly init STT/TTS here — flutter_tts and speech_to_text
      // both interact with Android's AudioManager during initialisation and
      // can hold audio focus or register audio sessions that cause other apps
      // (music, navigation audio) to duck/pause. Init lazily on first trigger.
      // Save voice settings back whenever they change
      _voice.addListener(_onVoiceSettingsChanged);
      _musicApp = MusicApp(media: _media);
      _hud.musicFrameBuilder = () => _musicApp.buildOverlayFrame();
      _apps = _buildAppsList();
      _glassesMenu.apps = _apps;
      // Wire voice assistant app-switching to the HUD
      _voice.onSwitchApp = (id) {
        final target = _apps.firstWhere(
          (a) => a.id == id || a.name.toLowerCase() == id.toLowerCase(),
          orElse: () => _apps.first,
        );
        _hud.setActiveApp(target);
        _glassesMenu.activeApp = target;
        if (!_hud.running) _hud.startLoop();
      };
      setState(() => _loaded = true);
      _gps.init();

      // Auto-connect on startup using last known address for speed
      if (_settings.autoConnect) {
        _rfcomm.autoConnectSolos(lastAddress: _settings.lastDeviceAddress);
      }
    });
  }

  // ── App list ──────────────────────────────────────────────────────────────

  List<GlassesApp> _buildAppsList() => [
    DashboardApp(
      gps: _gps,
      hr: _hr,
      glassesEvents: _glassesEvents,
      settings: _settings,
    ),
    SpeedometerApp(gps: _gps, settings: _settings),
    NavigationApp(
      notifications: _notifications,
      intents: _intents,
      gps: _gps,
      settings: _settings,
      compass: _compass,
      routeService: _routeService,
    ),
    RunningApp(workout: _workout, gps: _gps, hr: _hr, settings: _settings),
    PacerApp(gps: _gps, hr: _hr, fusion: _speedFusion, settings: _settings),
    WeatherApp(weather: _weather),
    VescApp(vesc: _vesc),
    AssistantApp(voice: _voice),
    _musicApp,
    CustomCommandApp(),
  ];

  // ── Event handlers ────────────────────────────────────────────────────────

  void _onDestinationChanged() {
    final dest = _intents.destination;
    if (dest == null || !_loaded) return;

    // Only switch to Navigation once — do NOT re-run if already active.
    // _resolveShortUrl updates _destination a second time when coords arrive,
    // which would re-fire this handler and call setActiveApp again (triggering
    // onDeactivate → routeService.clearCache(), killing the in-flight OSRM request).
    if (_hud.activeApp?.id == 'navigation') return;

    final navApp = _apps.firstWhere(
      (a) => a.id == 'navigation',
      orElse: () => _apps.first,
    );

    // Switch to Navigation in ARROW mode — lightweight, no tile fetching.
    // Arrow mode shows the turn-by-turn instructions from Google Maps notifications.
    // The user can manually switch to Map mode in the Navigation card if they want
    // the tile map (which requires more BT bandwidth).
    _hud.setActiveApp(navApp);
    _glassesMenu.activeApp = navApp;
    if (!_hud.running) _hud.startLoop();
    // Leave nav_mode at whatever the user last set — don't force map mode.
  }

  void _onVoiceSettingsChanged() {
    // Persist voice settings whenever they change via the UI
    _settings.setGeminiKey(_voice.apiKey);
    _settings.setGeminiModel(_voice.model);
    _settings.setWakeWord(_voice.wakeWord);
    _settings.setPhoneAssistantMode(_voice.phoneAssistantMode);
    _settings.setPhoneAssistantType(_voice.phoneAssistantType);
  }

  void _onMenuLaunch(GlassesApp app) {
    _hud.setActiveApp(app);
    _glassesMenu.activeApp = app;
  }

  void _onHudChanged() {
    final app = _hud.activeApp;
    if (app != null) {
      _settings.setLastAppId(app.id);
      _glassesMenu.activeApp = app;

      // Keep the notification text current so the user can see
      // what's running from the lock screen / notification shade.
      if (_fgService.isRunning && _rfcomm.isConnected) {
        _fgService.update(
          title: 'Solos HUD · ${_rfcomm.device?.name ?? "Glasses"}',
          text:  '${app.name} active',
        );
      }
    }
  }

  void _onRfcommChanged() {
    if (_rfcomm.isConnected) {
      _onConnected();
    } else if (_rfcomm.state == RfcommState.disconnected) {
      _onDisconnected();
    }
  }

  void _ensureForegroundRunning() {
    final device = _rfcomm.device?.name ?? 'Solos';
    final app    = _hud.activeApp?.name ?? 'HUD';
    _fgService.start(
      title: 'Solos HUD · $device',
      text:  '$app running',
    );
  }

  Future<void> _onConnected() async {
    _reconnectTimer?.cancel();
    _reconnectAttempts = 0;

    final deviceName = _rfcomm.device?.name ?? 'Solos';

    // 1 — Persist this device as the new "last known"
    if (_rfcomm.device != null) {
      await _settings.setLastDeviceAddress(_rfcomm.device!.address);
    }

    if (!_settings.autoStartHud) return;

    // 2 — Show boot screen immediately on glasses
    try {
      final bootFrame = await BootScreenRenderer.render(deviceName);
      _hud.showNotificationFrame(
        bootFrame,
        duration: const Duration(milliseconds: 1800),
      );
    } catch (_) {}

    // 3 — Restore last active app (or default to Dashboard) as the
    //     background app — the launcher will display on top of it.
    if (_loaded && _apps.isNotEmpty) {
      final lastId = _settings.lastAppId;
      final lastApp = lastId.isNotEmpty
          ? _apps.firstWhere((a) => a.id == lastId, orElse: () => _apps.first)
          : _apps.first;
      _hud.setActiveApp(lastApp);
      _glassesMenu.activeApp = lastApp;
    }

    // 4 — Start foreground service so background execution works
    _ensureForegroundRunning();

    // 5 — Brief stabilisation delay before we start sending packets.
    //     The BT socket reports "connected" before the radio link is fully
    //     negotiated; sending immediately risks dropping the first frames
    //     and triggering a disconnect.
    await Future.delayed(const Duration(milliseconds: 800));
    if (!_rfcomm.isConnected) return; // aborted if disconnected during delay

    // 6 — Start the HUD loop automatically
    if (!_hud.running) _hud.startLoop();

    // 6 — Open the app launcher so the user can pick what to do
    //     (opens after the 1.8 s boot screen finishes)
    Future.delayed(const Duration(milliseconds: 1900), () {
      if (_rfcomm.isConnected && !_glassesMenu.isOpen) {
        _glassesMenu.openLauncher();
      }
    });
  }

  void _onDisconnected() {
    _hud.stopLoop();

    // If we're already mid-connect (state = connecting), this disconnect event
    // came from a "Superseded" failure in Kotlin.  Don't schedule ANOTHER
    // reconnect — the in-flight attempt will complete on its own.
    if (_rfcomm.state == RfcommState.connecting) return;

    // Update notification to show disconnected state
    if (_fgService.isRunning) {
      _fgService.update(
        title: 'Solos HUD',
        text:  'Disconnected — reconnecting…',
      );
    }

    if (_rfcomm.wasUserDisconnected) {
      _fgService.stop();
      return;
    }
    if (!_settings.autoConnect) return;
    if (_reconnectAttempts >= _maxReconnectAttempts) {
      _logger.log(
        BleLogDirection.info,
        'Auto-reconnect: max attempts reached, giving up',
      );
      return;
    }

    final delaySeconds = _reconnectAttempts < _reconnectDelays.length
        ? _reconnectDelays[_reconnectAttempts]
        : _reconnectDelays.last;

    _logger.log(
      BleLogDirection.info,
      'Auto-reconnect in ${delaySeconds}s (attempt ${_reconnectAttempts + 1})',
    );

    _reconnectTimer = Timer(Duration(seconds: delaySeconds), () {
      if (!_rfcomm.isConnected) {
        _reconnectAttempts++;
        _rfcomm.autoConnectSolos(lastAddress: _settings.lastDeviceAddress);
      }
    });
  }

  // ── Build ─────────────────────────────────────────────────────────────────

  @override
  Widget build(BuildContext context) {
    if (!_loaded) {
      return const MaterialApp(
        home: Scaffold(
          backgroundColor: Colors.black,
          body: Center(child: CircularProgressIndicator()),
        ),
      );
    }

    return MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: _settings),
        ChangeNotifierProvider.value(value: _logger),
        ChangeNotifierProvider.value(value: _rfcomm),
        ChangeNotifierProvider.value(value: _glassesEvents),
        ChangeNotifierProvider.value(value: _hr),
        ChangeNotifierProvider.value(value: _gps),
        ChangeNotifierProvider.value(value: _hud),
        ChangeNotifierProvider.value(value: _tiltWake),
        ChangeNotifierProvider.value(value: _notifications),
        ChangeNotifierProvider.value(value: _intents),
        ChangeNotifierProvider.value(value: _compass),
        ChangeNotifierProvider.value(value: _weather),
        ChangeNotifierProvider.value(value: _vesc),
        ChangeNotifierProvider.value(value: _voice),
        ChangeNotifierProvider.value(value: _media),
        ChangeNotifierProvider.value(value: _routeService),
        ChangeNotifierProvider.value(value: _workout),
        ChangeNotifierProvider.value(value: _speedFusion),
        ChangeNotifierProvider.value(value: _glassesMenu),
        ChangeNotifierProvider.value(value: _notifOverlay),
      ],
      child: _AppShell(apps: _apps),
    );
  }

  @override
  void dispose() {
    _reconnectTimer?.cancel();
    _rfcomm.removeListener(_onRfcommChanged);
    _hud.removeListener(_onHudChanged);
    _workout.dispose();
    _speedFusion.dispose();
    _rfcomm.disconnect();
    _glassesEvents.dispose();
    _tiltWake.dispose();
    _hr.dispose();
    _gps.dispose();
    _hud.dispose();
    _notifications.dispose();
    _intents.removeListener(_onDestinationChanged);
    _intents.dispose();
    _compass.dispose();
    _weather.dispose();
    _vesc.dispose();
    _voice.dispose();
    _media.dispose();
    _routeService.dispose();
    _glassesMenu.stopListening();
    _notifOverlay.dispose();
    super.dispose();
  }
}

// ── App shell ─────────────────────────────────────────────────────────────────

class _AppShell extends StatefulWidget {
  final List<GlassesApp> apps;
  const _AppShell({required this.apps});

  @override
  State<_AppShell> createState() => _AppShellState();
}

class _AppShellState extends State<_AppShell> {
  int _tab = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: IndexedStack(
        index: _tab,
        children: [
          HomeScreen(apps: widget.apps),
          SettingsScreen(apps: widget.apps),
        ],
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: _tab,
        height: 60,
        labelBehavior: NavigationDestinationLabelBehavior.alwaysShow,
        onDestinationSelected: (i) => setState(() => _tab = i),
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.view_list_outlined),
            selectedIcon: Icon(Icons.view_list),
            label: 'Apps',
          ),
          NavigationDestination(
            icon: Icon(Icons.settings_outlined),
            selectedIcon: Icon(Icons.settings_rounded),
            label: 'Settings',
          ),
        ],
      ),
    );
  }
}
