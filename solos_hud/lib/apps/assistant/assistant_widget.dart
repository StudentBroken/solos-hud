import 'package:flutter/material.dart';
import '../../core/voice/voice_service.dart';

class AssistantPhoneWidget extends StatefulWidget {
  final VoiceService voice;
  const AssistantPhoneWidget({super.key, required this.voice});

  @override
  State<AssistantPhoneWidget> createState() => _AssistantPhoneWidgetState();
}

class _AssistantPhoneWidgetState extends State<AssistantPhoneWidget> {
  final _keyCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _keyCtrl.text = widget.voice.apiKey;
  }

  @override
  void dispose() {
    _keyCtrl.dispose();
    super.dispose();
  }

  VoiceService get v => widget.voice;

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: v,
      builder: (context, _) => Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // ── Mode toggle ──────────────────────────────────────────────────
          _ModeToggle(voice: v),
          const SizedBox(height: 12),

          if (v.phoneAssistantMode) ...[
            // ── Phone assistant card ───────────────────────────────────────
            _PhoneAssistantCard(voice: v),
          ] else ...[
            // ── Status card ────────────────────────────────────────────────
            _StatusCard(voice: v),
            const SizedBox(height: 12),

            // ── Mic button ─────────────────────────────────────────────────
            _MicButton(voice: v),
            const SizedBox(height: 12),

            // ── Last response ──────────────────────────────────────────────
            if (v.response.isNotEmpty) ...[
              _ResponseCard(response: v.response),
              const SizedBox(height: 12),
            ],

            // ── Settings ───────────────────────────────────────────────────
            _SettingsSection(voice: v, keyCtrl: _keyCtrl),
            const SizedBox(height: 12),

            // ── Wake-word calibration ──────────────────────────────────────
            _CalibrationSection(voice: v),
            const SizedBox(height: 12),

            // ── Conversation history ───────────────────────────────────────
            if (v.history.isNotEmpty) _HistorySection(voice: v),
          ],
        ],
      ),
    );
  }
}

// ── Mode toggle ───────────────────────────────────────────────────────────────

class _ModeToggle extends StatelessWidget {
  final VoiceService voice;
  const _ModeToggle({required this.voice});

  @override
  Widget build(BuildContext context) {
    final isPhone = voice.phoneAssistantMode;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Padding(
          padding: const EdgeInsets.only(bottom: 8),
          child: Text(
            'ASSISTANT MODE',
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.w700,
              color: Colors.grey.shade500,
              letterSpacing: 1.2,
            ),
          ),
        ),
        Row(
          children: [
            Expanded(
              child: _ModeCard(
                label: 'Gemini AI',
                subtitle: 'On-device Gemini',
                icon: Icons.auto_awesome,
                selected: !isPhone,
                selectedColor: Colors.cyanAccent,
                onTap: () => voice.phoneAssistantMode = false,
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
              child: _ModeCard(
                label: 'Phone',
                subtitle: 'Google / Bixby / Siri',
                icon: Icons.assistant,
                selected: isPhone,
                selectedColor: Colors.purpleAccent,
                onTap: () => voice.phoneAssistantMode = true,
              ),
            ),
          ],
        ),
        const SizedBox(height: 6),
        // Active mode banner
        AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
          decoration: BoxDecoration(
            color: isPhone
                ? Colors.purpleAccent.withValues(alpha: 0.1)
                : Colors.cyanAccent.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(
              color: isPhone
                  ? Colors.purpleAccent.withValues(alpha: 0.4)
                  : Colors.cyanAccent.withValues(alpha: 0.4),
            ),
          ),
          child: Row(
            children: [
              Icon(
                isPhone ? Icons.assistant : Icons.auto_awesome,
                size: 16,
                color: isPhone ? Colors.purpleAccent : Colors.cyanAccent,
              ),
              const SizedBox(width: 8),
              Text(
                'BACK button triggers: ${isPhone ? "Phone Assistant" : "Gemini AI"}',
                style: TextStyle(
                  fontSize: 13,
                  fontWeight: FontWeight.w600,
                  color: isPhone ? Colors.purpleAccent : Colors.cyanAccent,
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class _ModeCard extends StatelessWidget {
  final String label;
  final String subtitle;
  final IconData icon;
  final bool selected;
  final Color selectedColor;
  final VoidCallback onTap;

  const _ModeCard({
    required this.label,
    required this.subtitle,
    required this.icon,
    required this.selected,
    required this.selectedColor,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 180),
        padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 14),
        decoration: BoxDecoration(
          color: selected
              ? selectedColor.withValues(alpha: 0.15)
              : Colors.grey.shade900,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: selected
                ? selectedColor.withValues(alpha: 0.7)
                : Colors.grey.shade800,
            width: selected ? 2 : 1,
          ),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Icon(
              icon,
              size: 26,
              color: selected ? selectedColor : Colors.grey.shade600,
            ),
            const SizedBox(height: 8),
            Text(
              label,
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: selected ? selectedColor : Colors.grey.shade400,
              ),
            ),
            const SizedBox(height: 2),
            Text(
              subtitle,
              style: TextStyle(
                fontSize: 11,
                color: selected
                    ? selectedColor.withValues(alpha: 0.7)
                    : Colors.grey.shade600,
              ),
            ),
            if (selected) ...[
              const SizedBox(height: 8),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: selectedColor.withValues(alpha: 0.2),
                  borderRadius: BorderRadius.circular(4),
                ),
                child: Text(
                  'ACTIVE',
                  style: TextStyle(
                    fontSize: 10,
                    fontWeight: FontWeight.w800,
                    color: selectedColor,
                    letterSpacing: 1,
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

// ── (legacy _ModeTab kept for reference — not used) ───────────────────────────
class _ModeTab extends StatelessWidget {
  final String label;
  final IconData icon;
  final bool selected;
  final VoidCallback onTap;
  const _ModeTab({
    required this.label,
    required this.icon,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final color = selected ? Colors.cyanAccent : Colors.grey.shade600;
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 150),
          padding: const EdgeInsets.symmetric(vertical: 10),
          decoration: BoxDecoration(
            color: selected
                ? Colors.cyanAccent.withValues(alpha: 0.1)
                : Colors.transparent,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(
              color: selected
                  ? Colors.cyanAccent.withValues(alpha: 0.4)
                  : Colors.transparent,
            ),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, size: 14, color: color),
              const SizedBox(width: 6),
              Text(
                label,
                style: TextStyle(
                  fontSize: 12,
                  color: color,
                  fontWeight:
                      selected ? FontWeight.w600 : FontWeight.normal,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ── Phone assistant card ───────────────────────────────────────────────────────

class _PhoneAssistantCard extends StatelessWidget {
  final VoiceService voice;
  const _PhoneAssistantCard({required this.voice});

  static const _targets = [
    ('voice_assist',      'Voice Assist',       Icons.record_voice_over),
    ('google_assistant',  'Google Assistant',   Icons.assistant),
    ('bixby',             'Samsung Bixby',      Icons.stars_outlined),
  ];

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // ── Assistant selector ──────────────────────────────────────────
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
          decoration: BoxDecoration(
            color: Colors.grey.shade900,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.grey.shade800),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Assistant', style: TextStyle(fontSize: 11, color: Colors.grey.shade500)),
              const SizedBox(height: 8),
              ..._targets.map(((String id, String label, IconData icon) rec) {
                final selected = voice.phoneAssistantType == rec.$1;
                return GestureDetector(
                  onTap: () {
                    voice.phoneAssistantType = rec.$1;
                    // trigger save via listener
                    voice.phoneAssistantMode = true;
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 4),
                    child: Row(children: [
                      Icon(
                        selected ? Icons.radio_button_checked : Icons.radio_button_unchecked,
                        size: 16,
                        color: selected ? Colors.greenAccent : Colors.grey.shade600,
                      ),
                      const SizedBox(width: 10),
                      Icon(rec.$3, size: 16,
                          color: selected ? Colors.greenAccent : Colors.grey.shade600),
                      const SizedBox(width: 8),
                      Text(rec.$2,
                          style: TextStyle(
                            fontSize: 13,
                            color: selected ? Colors.white : Colors.grey.shade500,
                            fontWeight: selected ? FontWeight.w600 : FontWeight.normal,
                          )),
                    ]),
                  ),
                );
              }),
            ],
          ),
        ),
        const SizedBox(height: 12),

        // ── Info + launch button ────────────────────────────────────────
        Row(
          children: [
            const Icon(Icons.info_outline, size: 14, color: Colors.grey),
            const SizedBox(width: 6),
            const Expanded(
              child: Text(
                'Press BACK on glasses to open',
                style: TextStyle(color: Colors.grey, fontSize: 12),
              ),
            ),
          ],
        ),
        const SizedBox(height: 12),
        Center(
          child: GestureDetector(
            onTap: voice.launchPhoneAssistant,
            child: Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: Colors.greenAccent.withValues(alpha: 0.1),
                border: Border.all(
                  color: Colors.greenAccent.withValues(alpha: 0.5),
                  width: 1.5,
                ),
              ),
              child: const Icon(Icons.assistant, color: Colors.greenAccent, size: 36),
            ),
          ),
        ),
      ],
    );
  }
}

// ── Status card ───────────────────────────────────────────────────────────────

class _StatusCard extends StatelessWidget {
  final VoiceService voice;
  const _StatusCard({required this.voice});

  @override
  Widget build(BuildContext context) {
    final (color, label, icon) = switch (voice.state) {
      VoiceState.idle        => voice.wakeActive
          ? (Colors.green,  'Listening for "${voice.wakeWord}"', Icons.hearing)
          : (Colors.grey,   'Ready — press BACK button to speak', Icons.mic_none),
      VoiceState.listening   => (Colors.cyanAccent, 'Listening…',  Icons.mic),
      VoiceState.processing  => (Colors.orange,     'Thinking…',   Icons.auto_awesome),
      VoiceState.responding  => (Colors.green,      'Speaking…',   Icons.volume_up),
      VoiceState.calibrating => (Colors.orange,     voice.calFeedback, Icons.tune),
    };

    return AnimatedContainer(
      duration: const Duration(milliseconds: 200),
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Row(
        children: [
          Icon(icon, color: color, size: 20),
          const SizedBox(width: 10),
          Expanded(child: Text(label,
              style: TextStyle(color: color, fontWeight: FontWeight.w600,
                  fontSize: 13))),
          if (voice.state == VoiceState.responding)
            TextButton(
              onPressed: voice.stopSpeaking,
              style: TextButton.styleFrom(foregroundColor: Colors.orange,
                  padding: EdgeInsets.zero, minimumSize: Size.zero),
              child: const Text('Stop', style: TextStyle(fontSize: 12)),
            ),
        ],
      ),
    );
  }
}

// ── Big mic button ────────────────────────────────────────────────────────────

class _MicButton extends StatelessWidget {
  final VoiceService voice;
  const _MicButton({required this.voice});

  @override
  Widget build(BuildContext context) {
    final isListening = voice.state == VoiceState.listening;
    return Center(
      child: GestureDetector(
        onTapDown: (_) => voice.startListening(),
        onTapUp:   (_) {
          if (voice.state == VoiceState.listening) voice.stopListening();
        },
        onTapCancel: () {
          if (voice.state == VoiceState.listening) voice.stopListening();
        },
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 150),
          width: 80, height: 80,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: isListening
                ? Colors.cyanAccent.withValues(alpha: 0.2)
                : Colors.grey.shade900,
            border: Border.all(
              color: isListening ? Colors.cyanAccent : Colors.grey.shade700,
              width: isListening ? 2.5 : 1.5,
            ),
            boxShadow: isListening
                ? [BoxShadow(color: Colors.cyanAccent.withValues(alpha: 0.3),
                    blurRadius: 20)]
                : [],
          ),
          child: Icon(
            isListening ? Icons.mic : Icons.mic_none,
            color: isListening ? Colors.cyanAccent : Colors.grey,
            size: 34,
          ),
        ),
      ),
    );
  }
}

// ── Response card ─────────────────────────────────────────────────────────────

class _ResponseCard extends StatelessWidget {
  final String response;
  const _ResponseCard({required this.response});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.cyan.shade900.withValues(alpha: 0.15),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.cyanAccent.withValues(alpha: 0.2)),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Icon(Icons.auto_awesome, size: 16, color: Colors.cyanAccent),
          const SizedBox(width: 10),
          Expanded(child: Text(response,
              style: const TextStyle(fontSize: 14, color: Colors.white,
                  height: 1.4))),
        ],
      ),
    );
  }
}

// ── Settings section ──────────────────────────────────────────────────────────

class _SettingsSection extends StatelessWidget {
  final VoiceService voice;
  final TextEditingController keyCtrl;
  const _SettingsSection({required this.voice, required this.keyCtrl});

  @override
  Widget build(BuildContext context) {
    return Theme(
      data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
      child: ExpansionTile(
        leading: const Icon(Icons.settings_outlined, size: 16, color: Colors.grey),
        title: const Text('Settings',
            style: TextStyle(fontSize: 13, color: Colors.grey)),
        tilePadding: EdgeInsets.zero,
        children: [
          // API Key
          TextField(
            controller: keyCtrl,
            obscureText: true,
            decoration: const InputDecoration(
              labelText: 'Gemini API Key',
              hintText: 'AIza…',
              isDense: true,
              border: OutlineInputBorder(),
              prefixIcon: Icon(Icons.vpn_key_outlined, size: 16),
            ),
            style: const TextStyle(fontFamily: 'monospace', fontSize: 12),
            onChanged: (v) => voice.apiKey = v.trim(),
          ),
          const SizedBox(height: 10),

          // Model
          TextField(
            controller: TextEditingController(text: voice.model),
            decoration: const InputDecoration(
              labelText: 'Model',
              hintText: 'gemini-2.0-flash',
              isDense: true,
              border: OutlineInputBorder(),
            ),
            style: const TextStyle(fontFamily: 'monospace', fontSize: 12),
            onChanged: (v) => voice.model = v.trim().isEmpty ? 'gemini-2.0-flash' : v.trim(),
          ),
          const SizedBox(height: 10),

          // Wake word
          TextField(
            controller: TextEditingController(text: voice.wakeWord),
            decoration: const InputDecoration(
              labelText: 'Wake Word',
              hintText: 'hey jarvis',
              isDense: true,
              border: OutlineInputBorder(),
              helperText: 'Say this phrase to activate hands-free',
            ),
            onChanged: (v) => voice.wakeWord = v.trim().isEmpty ? 'hey jarvis' : v.trim().toLowerCase(),
          ),
          const SizedBox(height: 10),

          // Wake word toggle
          SwitchListTile.adaptive(
            value: voice.wakeActive,
            onChanged: (on) => on
                ? voice.startWakeWordDetection()
                : voice.stopWakeWordDetection(),
            title: const Text('Always-on Wake Word',
                style: TextStyle(fontSize: 13)),
            subtitle: const Text('Continuously listens. Uses more battery.',
                style: TextStyle(fontSize: 11)),
            contentPadding: EdgeInsets.zero,
          ),
        ],
      ),
    );
  }
}

// ── Calibration section ───────────────────────────────────────────────────────

class _CalibrationSection extends StatelessWidget {
  final VoiceService voice;
  const _CalibrationSection({required this.voice});

  @override
  Widget build(BuildContext context) {
    final isRunning = voice.state == VoiceState.calibrating;
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.grey.shade900,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.tune, size: 14, color: Colors.grey),
              const SizedBox(width: 6),
              const Expanded(child: Text('Wake Word Calibration',
                  style: TextStyle(fontSize: 13, fontWeight: FontWeight.w600))),
              if (isRunning)
                Text('${voice.calSuccess}/3',
                    style: const TextStyle(color: Colors.green, fontSize: 12)),
            ],
          ),
          const SizedBox(height: 4),
          Text(
            isRunning
                ? voice.calFeedback
                : 'Test that your voice is recognised correctly (say it 3×)',
            style: TextStyle(
              fontSize: 12,
              color: isRunning ? Colors.white : Colors.grey.shade500,
            ),
          ),
          const SizedBox(height: 8),
          ElevatedButton.icon(
            onPressed: isRunning ? null : voice.startCalibration,
            icon: isRunning
                ? const SizedBox(width: 12, height: 12,
                    child: CircularProgressIndicator(strokeWidth: 2))
                : const Icon(Icons.record_voice_over, size: 14),
            label: Text(isRunning ? 'Calibrating…' : 'Start Calibration',
                style: const TextStyle(fontSize: 12)),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.cyan.shade900,
              foregroundColor: Colors.cyanAccent,
            ),
          ),
        ],
      ),
    );
  }
}

// ── Conversation history ──────────────────────────────────────────────────────

class _HistorySection extends StatelessWidget {
  final VoiceService voice;
  const _HistorySection({required this.voice});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            Text('HISTORY',
                style: Theme.of(context).textTheme.labelSmall
                    ?.copyWith(color: Colors.grey, letterSpacing: 1.3)),
            const Spacer(),
            TextButton.icon(
              onPressed: voice.clearConversation,
              icon: const Icon(Icons.delete_outline, size: 14),
              label: const Text('Clear', style: TextStyle(fontSize: 11)),
              style: TextButton.styleFrom(
                  foregroundColor: Colors.grey, padding: EdgeInsets.zero),
            ),
          ],
        ),
        ...voice.history.reversed.take(5).map((e) => _TurnTile(entry: e)),
      ],
    );
  }
}

class _TurnTile extends StatelessWidget {
  final ConversationEntry entry;
  const _TurnTile({required this.entry});

  @override
  Widget build(BuildContext context) {
    final h  = entry.timestamp.hour.toString().padLeft(2, '0');
    final m  = entry.timestamp.minute.toString().padLeft(2, '0');
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(children: [
            Text('$h:$m  ', style: TextStyle(fontSize: 10,
                color: Colors.grey.shade600, fontFamily: 'monospace')),
            const Icon(Icons.person_outline, size: 12, color: Colors.grey),
            const SizedBox(width: 4),
            Expanded(child: Text(entry.userText,
                style: const TextStyle(fontSize: 12, color: Colors.white70))),
          ]),
          const SizedBox(height: 2),
          Row(crossAxisAlignment: CrossAxisAlignment.start, children: [
            const SizedBox(width: 42),
            const Icon(Icons.auto_awesome, size: 12, color: Colors.cyanAccent),
            const SizedBox(width: 4),
            Expanded(child: Text(entry.assistantText,
                style: const TextStyle(fontSize: 12, color: Colors.cyanAccent))),
          ]),
        ],
      ),
    );
  }
}
