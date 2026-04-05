import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart' show EventChannel;
import 'notification_event.dart';

class NotificationService extends ChangeNotifier {
  static const _channel = EventChannel('solos_notifications');

  StreamSubscription? _sub;
  final List<NotificationEvent> _log = [];
  NotificationEvent? _latest;
  NavInstruction?   _currentNav;

  final _allController = StreamController<NotificationEvent>.broadcast();
  final _navController = StreamController<NotificationEvent>.broadcast();

  /// All notification events.
  Stream<NotificationEvent> get stream => _allController.stream;

  /// Only Google Maps / Waze navigation events.
  Stream<NotificationEvent> get navigationStream => _navController.stream;

  List<NotificationEvent> get log       => List.unmodifiable(_log);
  NotificationEvent?       get latest   => _latest;
  NavInstruction?          get currentNav => _currentNav;

  bool _isListening = false;
  bool get isListening => _isListening;

  void startListening() {
    if (_isListening) return;
    _isListening = true;
    _sub = _channel.receiveBroadcastStream().listen(
      _onRaw,
      onError: (_) {},
      onDone: () { _isListening = false; },
    );
  }

  void stopListening() {
    _sub?.cancel();
    _sub = null;
    _isListening = false;
  }

  void _onRaw(dynamic raw) {
    if (raw is! Map) return;
    final event = NotificationEvent.fromMap(raw);

    // Handle removal: clear current nav if it was from Maps
    if (event.isRemoval) {
      if (event.isNavigation && _currentNav != null) {
        _log.removeWhere((e) => e.key == event.key);
        // Don't clear _currentNav immediately — the user may still be navigating
      }
      notifyListeners();
      return;
    }

    _log.add(event);
    if (_log.length > 100) _log.removeRange(0, _log.length - 100);
    _latest = event;

    _allController.add(event);

    if (event.isNavigation) {
      final instr = event.navInstruction;
      if (instr != null) {
        _currentNav = instr;
        _navController.add(event);
      }
    }

    notifyListeners();
  }

  /// Clear the stored navigation instruction (e.g. when user ends navigation).
  void clearNav() {
    _currentNav = null;
    notifyListeners();
  }

  void clearLog() {
    _log.clear();
    notifyListeners();
  }

  @override
  void dispose() {
    _sub?.cancel();
    _allController.close();
    _navController.close();
    super.dispose();
  }
}
