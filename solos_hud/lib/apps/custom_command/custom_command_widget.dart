import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../core/hud_controller.dart';
import '../../core/rfcomm/rfcomm_service.dart';
import '../../core/solos_protocol.dart';
import 'custom_command_app.dart';

class CustomCommandWidget extends StatefulWidget {
  final CustomCommandApp app;
  const CustomCommandWidget({super.key, required this.app});

  @override
  State<CustomCommandWidget> createState() => _CustomCommandWidgetState();
}

class _CustomCommandWidgetState extends State<CustomCommandWidget> {
  final _cmdController = TextEditingController();
  final _scrollController = ScrollController();

  @override
  void dispose() {
    _cmdController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  void _log(String msg) {
    setState(() => widget.app.commandLog.add(msg));
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_scrollController.hasClients) {
        _scrollController.animateTo(
          _scrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 150),
          curve: Curves.easeOut,
        );
      }
    });
  }

  // Send the selected colour as a solid-fill IMAGE packet immediately
  Future<void> _sendColor(RfcommService rfcomm) async {
    final color = widget.app.selectedColor;
    final (r, g, b) = switch (color) {
      TestColor.red => (255, 0, 0),
      TestColor.green => (0, 255, 0),
      TestColor.blue => (0, 0, 255),
      TestColor.white => (255, 255, 255),
      TestColor.black => (0, 0, 0),
    };
    final packet = SolosProtocol.buildSolidColorPacket(r, g, b);
    final ok = await rfcomm.sendBytes(packet);
    _log('COLOR ${color.label}  [${ok ? 'OK' : 'FAIL'}]');
  }

  // Render the typed text as an IMAGE packet and send
  Future<void> _sendText(HudController hud) async {
    final text = _cmdController.text.trim();
    if (text.isEmpty) return;
    _cmdController.clear();
    widget.app.lastCommand = text;
    // Force HUD to render this text as the payload
    widget.app.commandLog; // touch
    _log('> $text  [sending…]');
    await hud.sendNow();
    _log('  [OK]');
  }

  @override
  Widget build(BuildContext context) {
    final rfcomm = context.watch<RfcommService>();
    final hud = context.watch<HudController>();
    final app = widget.app;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // ── Color tester ──────────────────────────────────────────
        _SectionHeader(title: 'Screen Color Test'),
        const SizedBox(height: 8),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: TestColor.values.map((color) {
            final selected = app.selectedColor == color;
            return GestureDetector(
              onTap: () => setState(() => app.selectedColor = color),
              child: AnimatedContainer(
                duration: const Duration(milliseconds: 150),
                width: 52,
                height: 52,
                decoration: BoxDecoration(
                  color: color.displayColor,
                  border: Border.all(
                    color: selected
                        ? Theme.of(context).colorScheme.primary
                        : Colors.grey.shade600,
                    width: selected ? 3 : 1,
                  ),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: selected
                    ? Icon(
                        Icons.check,
                        color: (color == TestColor.black || color == TestColor.blue)
                            ? Colors.white
                            : Colors.black,
                      )
                    : null,
              ),
            );
          }).toList(),
        ),
        const SizedBox(height: 10),
        ElevatedButton.icon(
          onPressed: rfcomm.isConnected ? () => _sendColor(rfcomm) : null,
          icon: const Icon(Icons.send),
          label: Text('Send ${app.selectedColor.label} to glasses'),
          style: ElevatedButton.styleFrom(
            backgroundColor: app.selectedColor.displayColor,
            foregroundColor:
                (app.selectedColor == TestColor.black || app.selectedColor == TestColor.blue)
                    ? Colors.white
                    : Colors.black,
          ),
        ),

        const SizedBox(height: 20),

        // ── Terminal ──────────────────────────────────────────────
        _SectionHeader(title: 'Text Terminal'),
        const SizedBox(height: 6),
        Container(
          height: 140,
          decoration: BoxDecoration(
            color: Colors.black,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.grey.shade800),
          ),
          padding: const EdgeInsets.all(8),
          child: app.commandLog.isEmpty
              ? const Text('No commands sent yet…',
                  style: TextStyle(color: Colors.grey, fontFamily: 'monospace', fontSize: 12))
              : ListView.builder(
                  controller: _scrollController,
                  itemCount: app.commandLog.length,
                  itemBuilder: (_, i) => Text(
                    app.commandLog[i],
                    style: const TextStyle(
                        color: Colors.greenAccent, fontFamily: 'monospace', fontSize: 12),
                  ),
                ),
        ),
        const SizedBox(height: 6),
        Row(
          children: [
            Expanded(
              child: TextField(
                controller: _cmdController,
                style: const TextStyle(fontFamily: 'monospace'),
                decoration: InputDecoration(
                  hintText: 'Text to display on glasses…',
                  prefixText: '> ',
                  isDense: true,
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
                ),
                onSubmitted: (_) => _sendText(hud),
              ),
            ),
            const SizedBox(width: 6),
            IconButton.filled(
              onPressed: rfcomm.isConnected ? () => _sendText(hud) : null,
              icon: const Icon(Icons.send),
              tooltip: 'Render & send',
            ),
            const SizedBox(width: 4),
            IconButton(
              onPressed: () => setState(() => app.commandLog.clear()),
              icon: const Icon(Icons.delete_outline),
              tooltip: 'Clear log',
            ),
          ],
        ),

        const SizedBox(height: 16),

        // ── Quick actions ─────────────────────────────────────────
        _SectionHeader(title: 'Quick Actions'),
        const SizedBox(height: 6),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton.icon(
              onPressed: rfcomm.isConnected ? () => hud.sendWakeUp() : null,
              icon: const Icon(Icons.wb_sunny_outlined, size: 16),
              label: const Text('Wake Up'),
            ),
            OutlinedButton.icon(
              onPressed: rfcomm.isConnected ? () => hud.sendNow() : null,
              icon: const Icon(Icons.refresh, size: 16),
              label: const Text('Force Frame'),
            ),
          ],
        ),
      ],
    );
  }
}

class _SectionHeader extends StatelessWidget {
  final String title;
  const _SectionHeader({required this.title});

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: Theme.of(context).textTheme.titleSmall?.copyWith(
            color: Theme.of(context).colorScheme.primary,
            fontWeight: FontWeight.bold,
          ),
    );
  }
}
