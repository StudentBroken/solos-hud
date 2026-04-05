import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

/// Controls the Android foreground service that keeps the app alive
/// while the screen is off or the app is backgrounded.
///
/// The service must be running whenever the glasses are connected so that:
///   • The BT RFCOMM socket stays open
///   • The HUD render Timer keeps firing
///   • GPS, compass, and notification listeners keep receiving data
class ForegroundServiceHelper {
  static const _channel = MethodChannel('solos_hud_service');

  bool _running = false;
  bool get isRunning => _running;

  /// Start (or keep) the foreground service.
  /// [title] / [text] set the persistent notification text.
  Future<void> start({
    String title = 'Solos HUD',
    String text  = 'Running in background',
  }) async {
    if (!_isAndroid) return;
    try {
      await _channel.invokeMethod('startForeground', {
        'title': title,
        'text':  text,
      });
      _running = true;
    } on PlatformException catch (e) {
      debugPrint('ForegroundService.start failed: ${e.message}');
    }
  }

  /// Update the notification text without restarting the service.
  Future<void> update({
    required String title,
    required String text,
  }) async {
    if (!_isAndroid || !_running) return;
    try {
      await _channel.invokeMethod('updateForeground', {
        'title': title,
        'text':  text,
      });
    } on PlatformException catch (_) {}
  }

  /// Stop the foreground service.
  Future<void> stop() async {
    if (!_isAndroid) return;
    try {
      await _channel.invokeMethod('stopForeground');
      _running = false;
    } on PlatformException catch (e) {
      debugPrint('ForegroundService.stop failed: ${e.message}');
    }
  }

  bool get _isAndroid => defaultTargetPlatform == TargetPlatform.android;
}
