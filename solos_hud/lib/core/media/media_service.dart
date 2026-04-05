import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import '../notifications/notification_service.dart';

class MediaTrack {
  final String title;
  final String artist;
  final String appName;
  final String packageName;
  final Uint8List? albumArtJpeg; // 128×128 JPEG from notification
  final bool isPlaying;
  final DateTime updatedAt;

  MediaTrack({
    required this.title,
    required this.artist,
    required this.appName,
    required this.packageName,
    this.isPlaying = false,
    this.albumArtJpeg,
  }) : updatedAt = DateTime.now();

  String get displayTitle => title.isNotEmpty ? title : 'Unknown title';
  String get displayArtist => artist.isNotEmpty ? artist : '';
}

class MediaService extends ChangeNotifier {
  static const _mediaChannel = MethodChannel('solos_intent');

  final NotificationService notifications;
  MediaService({required this.notifications}) {
    notifications.addListener(_onNotificationChanged);
  }

  MediaTrack? _current;
  MediaTrack? get current => _current;
  bool get hasTrack => _current != null;

  // Stable art reference — preserved across notifications for the same track
  // so Image.memory doesn't restart decoding on every notification update.
  String _prevTitle = '';
  String _prevArtist = '';
  Uint8List? _prevArt;

  void _onNotificationChanged() {
    // Find the latest media notification
    final mediaEvents = notifications.log
        .where((e) => e.isMedia && !e.isRemoval && e.title.isNotEmpty)
        .toList();

    if (mediaEvents.isEmpty) return;
    final latest = mediaEvents.last;

    // Preserve the same Uint8List reference when the track hasn't changed.
    // Image.memory restarts decoding every time the bytes reference changes,
    // so keeping the same object prevents the image from never finishing loading
    // when rapid notifications arrive for the same song.
    final artist = latest.artist ?? latest.text;
    var art = latest.albumArtRaw;
    if (art != null
        && latest.title == _prevTitle
        && artist == _prevArtist
        && _prevArt != null) {
      art = _prevArt; // reuse stable reference
    } else {
      _prevTitle = latest.title;
      _prevArtist = artist;
      _prevArt = art;
    }

    _current = MediaTrack(
      title: latest.title,
      artist: artist,
      appName: latest.appName,
      packageName: latest.packageName,
      isPlaying: latest.isOngoing,
      albumArtJpeg: art,
    );
    notifyListeners();
  }

  // ── Media controls ────────────────────────────────────────────────────────

  Future<void> playPause() => _invoke('mediaPlayPause');
  Future<void> next() => _invoke('mediaNext');
  Future<void> previous() => _invoke('mediaPrev');
  Future<void> volumeUp() => _invoke('volumeUp');
  Future<void> volumeDown() => _invoke('volumeDown');

  Future<void> _invoke(String method) async {
    try {
      await _mediaChannel.invokeMethod(method);
    } catch (e) {
      debugPrint('[Media] $method failed: $e');
    }
  }

  @override
  void dispose() {
    notifications.removeListener(_onNotificationChanged);
    super.dispose();
  }
}
