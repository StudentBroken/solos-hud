import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../../core/media/media_service.dart';

class MusicPhoneWidget extends StatelessWidget {
  final MediaService media;
  const MusicPhoneWidget({super.key, required this.media});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: media,
      builder: (context, _) {
        final track = media.current;

        // Nothing playing — minimal message, no controls
        if (track == null) {
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 28),
            child: Column(
              children: [
                Icon(
                  Icons.music_off_outlined,
                  size: 36,
                  color: Colors.grey.shade700,
                ),
                const SizedBox(height: 10),
                Text(
                  'Nothing playing',
                  style: TextStyle(color: Colors.grey.shade500, fontSize: 13),
                ),
              ],
            ),
          );
        }

        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // ── Track card ──────────────────────────────────────────────────
            _TrackCard(track: track),
            const SizedBox(height: 16),

            // ── Single play/pause button — centred ──────────────────────────
            Center(
              child: GestureDetector(
                onTap: media.playPause,
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 150),
                  width: 68,
                  height: 68,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: Colors.cyanAccent.withValues(alpha: 0.12),
                    border: Border.all(
                      color: Colors.cyanAccent.withValues(alpha: 0.5),
                      width: 1.5,
                    ),
                  ),
                  child: Icon(
                    track.isPlaying
                        ? Icons.pause_rounded
                        : Icons.play_arrow_rounded,
                    color: Colors.cyanAccent,
                    size: 38,
                  ),
                ),
              ),
            ),
          ],
        );
      },
    );
  }
}

// ── Track card ────────────────────────────────────────────────────────────────

class _TrackCard extends StatelessWidget {
  final MediaTrack track;
  const _TrackCard({required this.track});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Colors.purple.shade900.withValues(alpha: 0.35),
            Colors.blue.shade900.withValues(alpha: 0.25),
          ],
        ),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.purpleAccent.withValues(alpha: 0.15)),
      ),
      child: Row(
        children: [
          // Album art
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: _AlbumArt(bytes: track.albumArtJpeg),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  track.displayTitle,
                  style: const TextStyle(
                    fontSize: 15,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
                if (track.displayArtist.isNotEmpty) ...[
                  const SizedBox(height: 4),
                  Text(
                    track.displayArtist,
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade400),
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
                const SizedBox(height: 6),
                Row(
                  children: [
                    Icon(
                      Icons.circle,
                      size: 6,
                      color: track.isPlaying
                          ? Colors.greenAccent
                          : Colors.grey.shade600,
                    ),
                    const SizedBox(width: 5),
                    Text(
                      track.isPlaying ? 'Playing' : 'Paused',
                      style: TextStyle(
                        fontSize: 11,
                        color: track.isPlaying
                            ? Colors.greenAccent
                            : Colors.grey.shade600,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Flexible(
                      child: Text(
                        track.appName,
                        style: TextStyle(
                          fontSize: 11,
                          color: Colors.grey.shade600,
                        ),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Album art widget ──────────────────────────────────────────────────────────
// Uses the same dart:ui decode path as the glasses renderer to avoid the
// MemoryImage / repeated-notification restart bug.

class _AlbumArt extends StatefulWidget {
  final Uint8List? bytes;
  const _AlbumArt({required this.bytes});

  @override
  State<_AlbumArt> createState() => _AlbumArtState();
}

class _AlbumArtState extends State<_AlbumArt> {
  ui.Image? _image;
  Uint8List? _lastBytes;

  @override
  void initState() {
    super.initState();
    _decode(widget.bytes);
  }

  @override
  void didUpdateWidget(_AlbumArt old) {
    super.didUpdateWidget(old);
    // Only re-decode when the bytes reference actually changes (stable reference
    // from MediaService means same song = same object = no unnecessary work).
    if (widget.bytes != _lastBytes) _decode(widget.bytes);
  }

  Future<void> _decode(Uint8List? bytes) async {
    _lastBytes = bytes;
    if (bytes == null) {
      if (mounted) setState(() => _image = null);
      return;
    }
    try {
      final codec = await ui.instantiateImageCodec(bytes, targetWidth: 72, targetHeight: 72);
      final frame = await codec.getNextFrame();
      if (mounted && _lastBytes == bytes) {
        setState(() => _image = frame.image);
      }
    } catch (_) {
      if (mounted) setState(() => _image = null);
    }
  }

  @override
  void dispose() {
    _image?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final img = _image;
    if (img != null) {
      return RawImage(image: img, width: 72, height: 72, fit: BoxFit.cover);
    }
    return Container(
      width: 72,
      height: 72,
      color: Colors.grey.shade900,
      child: Icon(Icons.music_note, color: Colors.grey.shade600, size: 32),
    );
  }
}
