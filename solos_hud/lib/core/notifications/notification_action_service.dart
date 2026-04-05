import 'package:flutter/services.dart';

class NotificationActionService {
  static const _channel = MethodChannel('solos_actions');

  static Future<bool> invoke(
    String key,
    int actionIndex, {
    String? replyText,
  }) async {
    try {
      final args = <String, dynamic>{'key': key, 'index': actionIndex};
      if (replyText != null) args['reply'] = replyText;
      await _channel.invokeMethod('invoke', args);
      return true;
    } catch (_) {
      return false;
    }
  }
}
