import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:speech_to_text/speech_to_text.dart';
import 'package:flutter_tts/flutter_tts.dart';
import '../media/media_service.dart';
import 'gemini_client.dart';

// ── State ─────────────────────────────────────────────────────────────────────

enum VoiceState { idle, listening, processing, responding, calibrating }

// ── Parsed action ─────────────────────────────────────────────────────────────

class GeminiAction {
  final String type;
  final String param1;
  final String param2;
  const GeminiAction(this.type, this.param1, [this.param2 = '']);
}

// ── Conversation turn ─────────────────────────────────────────────────────────

class ConversationEntry {
  final String userText;
  final String assistantText;
  final DateTime timestamp;
  ConversationEntry({required this.userText, required this.assistantText})
    : timestamp = DateTime.now();
}

// ── Service ───────────────────────────────────────────────────────────────────

class VoiceService extends ChangeNotifier {
  static const _mediaChannel = MethodChannel('solos_intent');

  // ── Config (synced from AppSettings) ─────────────────────────────────────
  String apiKey = '';
  String model = 'gemini-2.5-flash-preview';
  String wakeWord = 'hey jarvis';

  /// When true, BACK button / mic button launch the phone's built-in assistant
  /// (Google Assistant, etc.) instead of the Gemini AI pipeline.
  bool _phoneAssistantMode = false;
  bool get phoneAssistantMode => _phoneAssistantMode;
  set phoneAssistantMode(bool value) {
    if (_phoneAssistantMode == value) return;
    _phoneAssistantMode = value;
    _notify();
  }

  /// Which phone assistant to launch: 'voice_assist' | 'google_assistant' | 'bixby'
  String phoneAssistantType = 'voice_assist';

  // ── Dependencies injected by app.dart ─────────────────────────────────────
  MediaService? media;
  void Function(String appId)? onSwitchApp;
  void Function(GeminiAction)? onAction;

  // ── STT / TTS ─────────────────────────────────────────────────────────────
  final _stt = SpeechToText();
  final _tts = FlutterTts();
  bool _sttReady = false;
  bool _disposed = false;
  bool _wakeActive = false;

  // ── State ──────────────────────────────────────────────────────────────────
  VoiceState _state = VoiceState.idle;
  String _partial = ''; // latest partial transcript
  String _response = ''; // last assistant response
  String? _error;

  // Whether we paused music to listen — so we can resume afterwards
  bool _musicWasPaused = false;

  Timer? _listenGuard;
  Timer? _wakeRestartTimer;

  // Calibration
  int _calAttempt = 0;
  int _calSuccess = 0;
  String _calFeedback = '';

  final List<ConversationEntry> _history = [];

  VoiceState get state => _state;
  String get partial => _partial;
  String get response => _response;
  String? get error => _error;
  bool get isListening => _state == VoiceState.listening;
  bool get wakeActive => _wakeActive;
  List<ConversationEntry> get history => List.unmodifiable(_history);
  int get calAttempt => _calAttempt;
  int get calSuccess => _calSuccess;
  String get calFeedback => _calFeedback;

  // ── Init ──────────────────────────────────────────────────────────────────

  Future<bool> init() async {
    if (_sttReady) return true;

    _sttReady = await _stt.initialize(
      onError: _onSttError,
      onStatus: _onSttStatus,
      debugLogging: false,
    );

    await _initTts();
    return _sttReady;
  }

  // Preferred Google neural voice names, best quality first.
  // These are Wavenet/neural voices bundled in Google TTS on Android.
  static const _preferredVoices = [
    'en-us-x-tpf-network', // Google US English female (most natural)
    'en-us-x-sfg-network', // Google US English female (variant)
    'en-us-x-tpm-network', // Google US English male
    'en-us-x-tpd-network', // Google US English deep female
    'en-gb-x-rjs-network', // Google UK English
  ];

  Future<void> _initTts() async {
    try {
      // Use Google TTS engine which includes Wavenet/neural voices
      final engines = await _tts.getEngines;
      if ((engines as List?)?.contains('com.google.android.tts') == true) {
        await _tts.setEngine('com.google.android.tts');
      }

      // Pick the best available voice — try preferred names first
      final voices = (await _tts.getVoices as List?)?.cast<Map>() ?? [];
      Map<dynamic, dynamic> chosen = {};
      for (final preferred in _preferredVoices) {
        final found = voices.firstWhere(
          (v) => (v['name'] as String? ?? '').toLowerCase() == preferred,
          orElse: () => <String, dynamic>{},
        );
        if (found.isNotEmpty) {
          chosen = found;
          break;
        }
      }
      // Fallback: any English network (neural) voice
      if (chosen.isEmpty) {
        chosen = voices.firstWhere((v) {
          final name = (v['name'] as String? ?? '').toLowerCase();
          final locale = (v['locale'] as String? ?? '').toLowerCase();
          return locale.startsWith('en') && name.contains('network');
        }, orElse: () => <String, dynamic>{});
      }
      if (chosen.isNotEmpty) {
        await _tts.setVoice(Map<String, String>.from(chosen));
      }
    } catch (_) {}

    await _tts.setLanguage('en-US');
    await _tts.setSpeechRate(
      0.45,
    ); // slightly slower = more natural, easier to understand
    await _tts.setPitch(0.92); // very slightly lower = warmer, less robotic
    await _tts.setVolume(1.0);

    _tts.setCompletionHandler(() {
      if (_state == VoiceState.responding && !_disposed) {
        _goIdle();
      }
    });
    _tts.setErrorHandler((_) {
      if (!_disposed) _goIdle();
    });
  }

  // ── STT event handlers ────────────────────────────────────────────────────

  void _onSttError(dynamic e) {
    if (_disposed) return;
    debugPrint('[Voice] STT error: $e');
    _listenGuard?.cancel();
    // If we have a partial, try to use it rather than losing the input
    final words = _partial.trim();
    if (_state == VoiceState.listening && words.isNotEmpty) {
      _processCommand(words);
    } else if (_state == VoiceState.listening) {
      _goIdle();
    }
  }

  /// ── KEY FIX ──────────────────────────────────────────────────────────────
  /// Android fires status='done' when the recogniser finishes — sometimes
  /// BEFORE the finalResult callback, sometimes INSTEAD of it (e.g. when the
  /// user stops mid-sentence and Android's VAD cuts in).
  ///
  /// Old code set state → idle on 'done', dropping anything in _partial.
  /// New code: if we have partial words → use them.  If nothing → go idle.
  void _onSttStatus(String status) {
    debugPrint('[Voice] STT status: $status');
    if (_disposed || _state != VoiceState.listening) return;

    if (status == 'done' || status == 'notListening') {
      _listenGuard?.cancel();
      final words = _partial.trim();
      if (words.isNotEmpty) {
        // We heard something — process it even if finalResult never fired
        _processCommand(words);
      } else {
        _goIdle();
      }
    }
  }

  // ── Primary trigger (called by double-tap) ────────────────────────────────

  /// Start listening. Pauses music if playing. Works from any app.
  /// If assistant is already active, cancels it instead.
  /// Ask Android if audio is currently being played by any app.
  Future<bool> _isMusicPlaying() async {
    try {
      return await _mediaChannel.invokeMethod<bool>('isMusicPlaying') ?? false;
    } catch (_) {
      return false;
    }
  }

  Future<void> trigger() async {
    if (_disposed) return;

    // Already active — cancel and resume music
    if (_state != VoiceState.idle) {
      await stopSpeaking();
      await stopListening();
      // _goIdle() is called inside those, which calls _resumeMusicIfNeeded
      return;
    }

    // Only pause if music is actually playing right now.
    // Using isMusicActive (AudioManager native check) so we never
    // accidentally start music that was already stopped.
    final playing = await _isMusicPlaying();
    if (playing) {
      _musicWasPaused = true;
      try {
        await _mediaChannel.invokeMethod('mediaPlayPause');
      } catch (_) {
        _musicWasPaused = false;
      }
      // Let the audio focus hand over to the mic cleanly
      await Future.delayed(const Duration(milliseconds: 300));
    }

    await startListening();
  }

  // ── Listening ─────────────────────────────────────────────────────────────

  Future<void> startListening() async {
    if (_disposed) return;
    await init();
    if (!_sttReady) {
      _error = 'Microphone not available';
      _notify();
      return;
    }
    if (_state == VoiceState.listening) return;

    _stt.cancel();
    _listenGuard?.cancel();

    _setState(VoiceState.listening);
    _partial = '';
    _error = null;

    // Hard guard — forces resolution after 20 s no matter what Android does
    _listenGuard = Timer(const Duration(seconds: 20), () {
      if (_state == VoiceState.listening && !_disposed) {
        debugPrint('[Voice] 20-s guard fired');
        final words = _partial.trim();
        if (words.isNotEmpty) {
          _processCommand(words);
        } else {
          _goIdle();
        }
      }
    });

    try {
      await _stt.listen(
        onResult: (result) {
          if (_disposed || _state != VoiceState.listening) return;
          _partial = result.recognizedWords;
          _notify();

          if (result.finalResult && result.recognizedWords.isNotEmpty) {
            _listenGuard?.cancel();
            _processCommand(result.recognizedWords);
          }
        },
        // Long window so the user isn't cut off mid-sentence
        listenFor: const Duration(seconds: 30),
        pauseFor: const Duration(seconds: 4),
        partialResults: true,
        cancelOnError: false,
        listenMode: ListenMode.dictation,
        localeId: 'en_US',
      );
    } catch (e) {
      _listenGuard?.cancel();
      _error = e.toString().split('\n').first;
      _goIdle();
    }
  }

  /// Stop listening without processing (cancel).
  Future<void> stopListening() async {
    _listenGuard?.cancel();
    _stt.cancel();
    if (_state == VoiceState.listening) _goIdle();
  }

  /// Stop listening and process whatever was heard so far.
  Future<void> stopListeningAndProcess() async {
    if (_state != VoiceState.listening) return;
    _listenGuard?.cancel();
    await _stt.stop();
    final words = _partial.trim();
    if (words.isNotEmpty) {
      _processCommand(words);
    } else {
      _goIdle();
    }
  }

  void clearConversation() {
    _history.clear();
    _response = '';
    _notify();
  }

  // ── Command processing ────────────────────────────────────────────────────

  Future<void> _processCommand(String text) async {
    if (_disposed) return;
    _stt.cancel();
    _listenGuard?.cancel();

    _setState(VoiceState.processing);
    _partial = text;

    // Fast built-ins
    final builtin = _handleBuiltin(text);
    if (builtin != null) {
      if (builtin.action != null) _executeAction(builtin.action!);
      _response = builtin.reply;
      _history.add(
        ConversationEntry(userText: text, assistantText: builtin.reply),
      );
      await _speak(builtin.reply);
      return;
    }

    // Gemini
    final systemPrompt = _buildSystemPrompt();
    final historyMaps = _history
        .expand(
          (e) => [
            {'role': 'user', 'text': e.userText},
            {'role': 'model', 'text': e.assistantText},
          ],
        )
        .toList();

    final raw = await GeminiClient.generate(
      apiKey: apiKey,
      model: model,
      systemPrompt: systemPrompt,
      userMessage: text,
      history: historyMaps,
      maxTokens: 350,
    );

    final (action, cleanReply) = _parseAction(raw);
    _response = cleanReply;
    _history.add(ConversationEntry(userText: text, assistantText: cleanReply));
    if (_history.length > 24) _history.removeAt(0);

    if (action != null) _executeAction(action);
    await _speak(cleanReply);
  }

  // ── System prompt ─────────────────────────────────────────────────────────

  String _buildSystemPrompt() =>
      '''You are a smart assistant for AR smart glasses.
Keep responses SHORT — ideally under 100 characters so they fit on the display.
Be direct. No markdown, no bullet points, no asterisks.
Current time: ${_now()}.

AVAILABLE ACTIONS — append ONE action tag at the end of your response when relevant:
  [ACTION:switch_app:weather]      weather / forecast / temperature
  [ACTION:switch_app:navigation]   directions / navigate / maps
  [ACTION:switch_app:vesc]         board / motor / speed stats
  [ACTION:switch_app:speedometer]  GPS speed
  [ACTION:switch_app:music]        now playing / current song
  [ACTION:switch_app:assistant]    open assistant view
  [ACTION:music:play_pause]        play or pause
  [ACTION:music:next]              next track
  [ACTION:music:prev]              previous track

Only add an action tag when the user clearly wants one of these. Otherwise answer normally.''';

  static (GeminiAction?, String) _parseAction(String raw) {
    final match = RegExp(r'\[ACTION:([^\]]+)\]').firstMatch(raw);
    if (match == null) return (null, raw.trim());
    final clean = raw.replaceFirst(match.group(0)!, '').trim();
    final parts = match.group(1)!.split(':');
    return (
      GeminiAction(
        parts[0],
        parts.length > 1 ? parts[1] : '',
        parts.length > 2 ? parts.sublist(2).join(':') : '',
      ),
      clean,
    );
  }

  ({GeminiAction? action, String reply})? _handleBuiltin(String text) {
    final t = text.toLowerCase();
    if (RegExp(r'\b(stop|cancel|nevermind|abort)\b').hasMatch(t)) {
      return (action: null, reply: 'Cancelled.');
    }
    if (RegExp(r'\b(what time|current time|whats the time)\b').hasMatch(t)) {
      return (action: null, reply: "It's ${_now()}.");
    }
    return null;
  }

  void _executeAction(GeminiAction action) {
    switch (action.type) {
      case 'switch_app':
        if (action.param1.isNotEmpty) onSwitchApp?.call(action.param1);
      case 'music':
        _executeMusicAction(action.param1);
      default:
        onAction?.call(action);
    }
  }

  Future<void> _executeMusicAction(String cmd) async {
    try {
      final method = switch (cmd) {
        'play_pause' => 'mediaPlayPause',
        'next' => 'mediaNext',
        'prev' => 'mediaPrev',
        _ => null,
      };
      if (method != null) await _mediaChannel.invokeMethod(method);
    } catch (_) {}
  }

  // ── TTS ───────────────────────────────────────────────────────────────────

  Future<void> _speak(String text) async {
    if (_disposed) {
      _goIdle();
      return;
    }
    _setState(VoiceState.responding);
    if (text.isEmpty) {
      _goIdle();
      return;
    }
    try {
      await _tts.speak(text);
    } catch (_) {
      _goIdle();
    }
  }

  Future<void> stopSpeaking() async {
    await _tts.stop();
    if (_state == VoiceState.responding) _goIdle();
  }

  // ── Idle transition ───────────────────────────────────────────────────────

  void _goIdle() {
    _setState(VoiceState.idle);
    _resumeMusicIfNeeded();
    if (_wakeActive) _scheduleWakeRestart();
  }

  void _resumeMusicIfNeeded() {
    if (!_musicWasPaused) return;
    _musicWasPaused = false;
    // 600 ms delay: lets TTS audio fully fade before music restarts,
    // preventing the two audio streams clashing on the Bluetooth speaker.
    Future.delayed(const Duration(milliseconds: 600), () {
      if (!_disposed) {
        _mediaChannel.invokeMethod('mediaPlayPause').catchError((_) {});
      }
    });
  }

  // ── Wake-word loop (disabled by default, user-toggled in settings) ────────

  Future<void> startWakeWordDetection() async {
    if (_wakeActive) return;
    _wakeActive = true;
    _notify();
    await init();
    _runWakeLoop();
  }

  void stopWakeWordDetection() {
    _wakeActive = false;
    _wakeRestartTimer?.cancel();
    _stt.stop();
    _notify();
  }

  Future<void> _runWakeLoop() async {
    while (_wakeActive && !_disposed) {
      if (_state != VoiceState.idle) {
        await Future.delayed(const Duration(milliseconds: 500));
        continue;
      }
      try {
        await _stt.listen(
          onResult: (result) {
            if (!_wakeActive ||
                _state != VoiceState.idle ||
                !result.finalResult)
              return;
            final text = result.recognizedWords.toLowerCase();
            if (text.contains(wakeWord.toLowerCase())) {
              final after = text
                  .replaceFirst(
                    RegExp(RegExp.escape(wakeWord.toLowerCase())),
                    '',
                  )
                  .trim();
              if (after.isNotEmpty) {
                _processCommand(after);
              } else {
                startListening();
              }
            }
          },
          listenFor: const Duration(seconds: 6),
          pauseFor: const Duration(seconds: 2),
          partialResults: false,
          cancelOnError: true,
          listenMode: ListenMode.search,
          localeId: 'en_US',
        );
      } catch (_) {}
      await Future.delayed(const Duration(milliseconds: 600));
    }
  }

  void _scheduleWakeRestart() {
    _wakeRestartTimer?.cancel();
    _wakeRestartTimer = Timer(const Duration(milliseconds: 800), () {
      if (_wakeActive && _state == VoiceState.idle && !_disposed)
        _runWakeLoop();
    });
  }

  void notifyPlaybackState(bool isPlaying) {} // kept for API compat

  /// Launch the phone's built-in voice assistant.
  Future<void> launchPhoneAssistant() async {
    try {
      await _mediaChannel.invokeMethod('launchAssistant', {'target': phoneAssistantType});
    } catch (e) {
      debugPrint('[Voice] launchAssistant failed: $e');
    }
  }

  // ── Calibration ───────────────────────────────────────────────────────────

  Future<void> startCalibration() async {
    if (!_sttReady) await init();
    _calAttempt = 0;
    _calSuccess = 0;
    _calFeedback = 'Say "$wakeWord" clearly';
    _setState(VoiceState.calibrating);
    await _calibrationRound();
  }

  Future<void> _calibrationRound() async {
    if (_calAttempt >= 3 || _disposed) {
      _calFeedback = _calSuccess >= 2
          ? '✓ Good! ($_calSuccess/3 recognised)'
          : '⚠ Only $_calSuccess/3 — try a simpler wake word';
      _goIdle();
      return;
    }
    _calFeedback = 'Attempt ${_calAttempt + 1}/3 — say "$wakeWord"';
    _notify();

    await _stt.listen(
      onResult: (result) {
        if (!result.finalResult) return;
        _calAttempt++;
        final heard = result.recognizedWords.toLowerCase();
        _calSuccess += heard.contains(wakeWord.toLowerCase()) ? 1 : 0;
        _calFeedback = heard.contains(wakeWord.toLowerCase())
            ? '✓ Heard: "${result.recognizedWords}"'
            : '✗ Heard: "${result.recognizedWords}"';
        _notify();
        Future.delayed(const Duration(milliseconds: 1200), _calibrationRound);
      },
      listenFor: const Duration(seconds: 6),
      pauseFor: const Duration(seconds: 3),
      cancelOnError: true,
      partialResults: false,
    );
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  static String _now() {
    final t = DateTime.now();
    return '${t.hour.toString().padLeft(2, '0')}:${t.minute.toString().padLeft(2, '0')}';
  }

  void _setState(VoiceState s) {
    _state = s;
    _notify();
  }

  void _notify() {
    if (!_disposed) notifyListeners();
  }

  @override
  void dispose() {
    _disposed = true;
    _wakeActive = false;
    _listenGuard?.cancel();
    _wakeRestartTimer?.cancel();
    _stt.cancel();
    _tts.stop();
    super.dispose();
  }
}
