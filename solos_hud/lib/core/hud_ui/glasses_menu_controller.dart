import 'dart:async';
import 'package:flutter/foundation.dart';
import '../glasses_app.dart';
import '../media/media_service.dart';
import '../rfcomm/glasses_event_service.dart';
import '../voice/voice_service.dart';
import 'glasses_menu_renderer.dart';

/// Controls the in-glasses app launcher / HUD menu and global gestures.
///
/// ── Global gestures (menu closed) ───────────────────────────────────────────
///   FRONT double-tap  → trigger voice assistant (works in ANY app)
///   FRONT single-tap  → forwarded to the active app (e.g. prev track)
///   BACK  single-tap  → forwarded to the active app (e.g. next track)
///   MAIN  long-press  → open app launcher
///
/// ── Launcher gestures (menu open) ───────────────────────────────────────────
///   FRONT short/make  → scroll up
///   BACK  short/make  → scroll down
///   MAIN  short       → launch selected app
///   MAIN  long        → close without launching
///   Auto-closes after 10 s of no input.
///
/// ── Double-tap detection ─────────────────────────────────────────────────────
///   Uses buttonShort (fires on clean release) for reliability.
///   Two buttonShort events from FRONT within 600 ms = double-tap.
///   First press is forwarded to the active app immediately — the second press
///   triggers the assistant.  This means the app sees one press and then the
///   assistant opens; acceptable trade-off.
class GlassesMenuController extends ChangeNotifier {
  static const _autoCloseDuration = Duration(seconds: 10);
  static const _debounce = Duration(milliseconds: 250);
  static const _doubleTapWindow = Duration(milliseconds: 600);

  final GlassesEventService events;
  final VoiceService voice;
  void Function(GlassesApp) onLaunchApp;
  void Function() onStartLoop;
  void Function(GlassesEvent)? onActiveAppButton;

  /// Inject MediaService to enable the music quick-overlay.
  MediaService? media;

  /// When set and returns true, the button event is consumed by the
  /// notification overlay before normal menu/app handling.
  bool Function(GlassesEvent)? notifButtonHandler;

  List<GlassesApp> apps = [];
  GlassesApp? activeApp;

  GlassesMenuController({
    required this.events,
    required this.voice,
    required this.onLaunchApp,
    required this.onStartLoop,
    this.onActiveAppButton,
  });

  // ── State ─────────────────────────────────────────────────────────────────
  bool _open = false;
  int _selectedIndex = 0;
  bool _disposed = false;

  bool get isOpen => _open;
  int get selectedIndex => _selectedIndex;

  StreamSubscription<GlassesEvent>? _sub;
  Timer? _autoCloseTimer;
  final Map<int, DateTime> _lastFired = {};

  // Double-tap tracking
  DateTime? _lastFrontShort;
  DateTime? _lastBackShort;

  // Music quick-overlay — shown when user double-taps BACK
  bool _musicOverlayActive = false;
  bool get musicOverlayActive => _musicOverlayActive;

  /// Programmatically open the launcher (e.g. on glasses connect).
  void openLauncher() => _openMenu();

  void closeMusicOverlay() {
    if (_musicOverlayActive) {
      _musicOverlayActive = false;
      _lastBackShort = null;
      _notify();
    }
  }

  // ── Init / dispose ────────────────────────────────────────────────────────

  void startListening() {
    _sub?.cancel();
    _sub = events.buttonEvents.listen(_onButton);
  }

  void stopListening() {
    _sub?.cancel();
    _sub = null;
    _autoCloseTimer?.cancel();
  }

  // ── Button handling ───────────────────────────────────────────────────────

  void _onButton(GlassesEvent event) {
    if (_disposed) return;

    final action = event.action;
    final btn = event.button;
    if (btn == null || btn == GlassesButton.unknown) return;

    // buttonRepeat is never useful — always discard
    if (action == GlassesActionType.buttonRepeat) return;

    // ── Notification overlay takes highest priority ───────────────────────────
    if (notifButtonHandler?.call(event) == true) return;

    // ── Menu open ─────────────────────────────────────────────────────────
    if (_open) {
      // Only gesture events matter inside the menu; discard raw hardware events
      if (action == GlassesActionType.buttonBreak) return;
      _resetAutoClose();

      switch (btn) {
        case GlassesButton.front:
          if ((action == GlassesActionType.buttonMake ||
                  action == GlassesActionType.buttonShort) &&
              _debounced(btn)) {
            _scrollUp();
          }
        case GlassesButton.back:
          if ((action == GlassesActionType.buttonMake ||
                  action == GlassesActionType.buttonShort) &&
              _debounced(btn)) {
            _scrollDown();
          }
        case GlassesButton.main:
          if (action == GlassesActionType.buttonShort && _debounced(btn)) {
            _launch();
          } else if (action == GlassesActionType.buttonLong &&
              _debounced(btn)) {
            _closeMenu();
          }
        default:
          break;
      }
      return;
    }

    // ── Music overlay active — intercept buttons for media control ───────────
    if (_musicOverlayActive) {
      // MAIN long-press is the universal escape hatch — ALWAYS works
      if (btn == GlassesButton.main && action == GlassesActionType.buttonLong) {
        closeMusicOverlay();
        _openMenu();
        return;
      }

      // Discard raw hardware events inside overlay
      if (action == GlassesActionType.buttonBreak ||
          action == GlassesActionType.buttonMake) {
        return;
      }

      switch (btn) {
        case GlassesButton.front:
          if (action == GlassesActionType.buttonShort) {
            media?.volumeUp();
          } else if (action == GlassesActionType.buttonLong) {
            media?.next();
          }
        case GlassesButton.main:
          if (action == GlassesActionType.buttonShort) {
            media?.playPause();
          }
        case GlassesButton.back:
          if (action == GlassesActionType.buttonShort) {
            final now = DateTime.now();
            final isDouble =
                _lastBackShort != null &&
                now.difference(_lastBackShort!) <= _doubleTapWindow;
            _lastBackShort = now;
            if (isDouble) {
              _lastBackShort = null;
              closeMusicOverlay();
            } else {
              media?.volumeDown();
            }
          } else if (action == GlassesActionType.buttonLong) {
            media?.previous();
          }
        default:
          break;
      }
      return;
    }

    // ── Menu closed ───────────────────────────────────────────────────────

    // MAIN long-press → open launcher (always, regardless of voice state)
    if (btn == GlassesButton.main &&
        action == GlassesActionType.buttonLong &&
        _debounced(btn)) {
      _openMenu();
      return;
    }

    // Discard raw hardware events for non-menu navigation
    if (action == GlassesActionType.buttonBreak ||
        action == GlassesActionType.buttonMake) {
      // Exception: cancel voice speaking on any Make to FRONT/MAIN
      if (action == GlassesActionType.buttonMake &&
          voice.state != VoiceState.idle) {
        if (btn == GlassesButton.front || btn == GlassesButton.main) {
          voice.stopSpeaking();
          return;
        }
      }
      return;
    }

    // From here: only buttonShort and buttonLong

    // FRONT short → double-tap = assistant, single = forward to app
    if (btn == GlassesButton.front && action == GlassesActionType.buttonShort) {
      final now = DateTime.now();
      final isDouble =
          _lastFrontShort != null &&
          now.difference(_lastFrontShort!) <= _doubleTapWindow;
      _lastFrontShort = now;

      if (isDouble) {
        _lastFrontShort = null;
        _triggerAssistant();
        return;
      }

      onActiveAppButton?.call(event);
      return;
    }

    // BACK short → double-tap = open music overlay, single = forward to app
    if (btn == GlassesButton.back && action == GlassesActionType.buttonShort) {
      final now = DateTime.now();
      final isDouble =
          _lastBackShort != null &&
          now.difference(_lastBackShort!) <= _doubleTapWindow;
      _lastBackShort = now;

      if (isDouble) {
        _lastBackShort = null;
        _openMusicOverlay();
        return;
      }

      // Single BACK tap — forward to active app (e.g. scroll down)
      onActiveAppButton?.call(event);
      return;
    }

    // All other events go to the active app
    onActiveAppButton?.call(event);
  }

  void _triggerAssistant() {
    if (!_disposed) voice.trigger();
  }

  void _openMusicOverlay() {
    if (_disposed) return;
    _musicOverlayActive = true;
    _lastBackShort = null;
    _notify();
  }

  // ── Per-button debounce ───────────────────────────────────────────────────

  bool _debounced(GlassesButton button) {
    final now = DateTime.now();
    final last = _lastFired[button.code];
    if (last != null && now.difference(last) < _debounce) return false;
    _lastFired[button.code] = now;
    return true;
  }

  // ── Menu actions ──────────────────────────────────────────────────────────

  void _openMenu() {
    if (apps.isEmpty) return;
    // Close music overlay if it's showing
    _musicOverlayActive = false;
    _lastBackShort = null;
    final idx = apps.indexWhere((a) => a == activeApp);
    _selectedIndex = idx >= 0 ? idx : 0;
    _open = true;
    _lastFired.clear();
    _lastFrontShort = null;
    _resetAutoClose();
    _notify();
  }

  void _closeMenu() {
    _open = false;
    _autoCloseTimer?.cancel();
    _lastFired.clear();
    _notify();
  }

  void _scrollUp() {
    if (_selectedIndex > 0) {
      _selectedIndex--;
      _notify();
    }
  }

  void _scrollDown() {
    if (_selectedIndex < apps.length - 1) {
      _selectedIndex++;
      _notify();
    }
  }

  void _launch() {
    if (apps.isEmpty) return;
    final app = apps[_selectedIndex];
    _open = false;
    _autoCloseTimer?.cancel();
    _lastFired.clear();
    onLaunchApp(app);
    onStartLoop();
    _notify();
  }

  void _resetAutoClose() {
    _autoCloseTimer?.cancel();
    _autoCloseTimer = Timer(_autoCloseDuration, () {
      if (_open) _closeMenu();
    });
  }

  // ── Render ────────────────────────────────────────────────────────────────

  Future<Uint8List> renderFrame() {
    final items = apps
        .map(
          (app) => MenuAppItem(
            name: app.name,
            iconCodePoint: app.icon.codePoint,
            isActive: app == activeApp,
          ),
        )
        .toList();
    return GlassesMenuRenderer.render(
      items: items,
      selectedIndex: _selectedIndex,
      batteryLabel: events.batteryLabel,
      batteryLevel: events.batteryLevel,
    );
  }

  void _notify() {
    if (!_disposed) notifyListeners();
  }

  @override
  void dispose() {
    _disposed = true;
    _sub?.cancel();
    _autoCloseTimer?.cancel();
    super.dispose();
  }
}
