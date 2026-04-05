import 'dart:typed_data';
import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import '../../core/voice/voice_service.dart';
import '../../core/voice/voice_overlay_renderer.dart';
import '../../core/rfcomm/glasses_event_service.dart';
import 'assistant_widget.dart';

class AssistantApp extends GlassesApp {
  final VoiceService voice;
  AssistantApp({required this.voice});

  @override
  String get id => 'assistant';
  @override
  String get name => 'Assistant';
  @override
  IconData get icon => Icons.mic_outlined;
  @override
  String get description =>
      'Gemini AI or Phone Assistant · voice commands';

  @override
  void onActivate() {
    // History is persistent globally
  }

  @override
  void onDeactivate() {
    // Voice service continues in background
  }

  // ── Button navigation ─────────────────────────────────────────────────────
  @override
  bool onButtonEvent(GlassesEvent event) {
    final action = event.action;
    final btn = event.button;
    if (btn == null) return false;

    if (btn == GlassesButton.front) {
      if (action == GlassesActionType.buttonMake ||
          action == GlassesActionType.buttonShort) {
        voice.stopSpeaking();
        voice.stopListening();
        return true;
      }
    }

    // BACK button: trigger assistant (phone or Gemini)
    if (btn == GlassesButton.back) {
      if (action == GlassesActionType.buttonShort) {
        if (voice.phoneAssistantMode) {
          voice.launchPhoneAssistant();
        } else {
          voice.trigger();
        }
        return true;
      }
    }

    return false;
  }

  @override
  String? buildGlassesPayload() => null;

  @override
  Future<Uint8List?> buildCustomFrame() async {
    try {
      return await VoiceOverlayRenderer.render(voice);
    } catch (_) {
      return null;
    }
  }

  @override
  Widget buildPhoneWidget(BuildContext context) =>
      AssistantPhoneWidget(voice: voice);

  @override
  Widget buildPreviewWidget(BuildContext context) {
    return ListenableBuilder(
      listenable: voice,
      builder: (_, __) {
        if (voice.phoneAssistantMode) {
          return Text(
            'Phone Assistant — press BACK',
            style: Theme.of(context).textTheme.bodySmall
                ?.copyWith(color: Colors.greenAccent),
            overflow: TextOverflow.ellipsis,
          );
        }
        final label = switch (voice.state) {
          VoiceState.listening => '● Listening…',
          VoiceState.processing => '◌ Thinking…',
          VoiceState.responding => '▶ ${voice.response}',
          _ => voice.wakeActive
              ? 'Wake: "${voice.wakeWord}"'
              : 'Press BACK to speak',
        };
        return Text(
          label,
          style: Theme.of(context).textTheme.bodySmall
              ?.copyWith(color: Colors.cyanAccent),
          overflow: TextOverflow.ellipsis,
        );
      },
    );
  }
}
