import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/bluetooth/ble_logger.dart';

class LogViewer extends StatefulWidget {
  const LogViewer({super.key});

  @override
  State<LogViewer> createState() => _LogViewerState();
}

class _LogViewerState extends State<LogViewer> {
  final _scrollController = ScrollController();
  bool _autoScroll = true;

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  Color _colorForDirection(BleLogDirection dir) {
    switch (dir) {
      case BleLogDirection.sent:
        return Colors.cyanAccent;
      case BleLogDirection.received:
        return Colors.greenAccent;
      case BleLogDirection.info:
        return Colors.grey.shade400;
      case BleLogDirection.error:
        return Colors.redAccent;
    }
  }

  @override
  Widget build(BuildContext context) {
    final logger = context.watch<BleLogger>();
    final entries = logger.entries;

    if (_autoScroll) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        if (_scrollController.hasClients) {
          _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
        }
      });
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Row(
          children: [
            const Text('BLE Log', style: TextStyle(fontWeight: FontWeight.bold)),
            const Spacer(),
            Row(
              children: [
                const Text('Auto-scroll', style: TextStyle(fontSize: 12)),
                Switch.adaptive(
                  value: _autoScroll,
                  onChanged: (v) => setState(() => _autoScroll = v),
                ),
              ],
            ),
            IconButton(
              icon: const Icon(Icons.delete_outline),
              tooltip: 'Clear',
              onPressed: () => context.read<BleLogger>().clear(),
            ),
          ],
        ),
        Container(
          height: 240,
          decoration: BoxDecoration(
            color: Colors.black,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.grey.shade800),
          ),
          child: entries.isEmpty
              ? const Center(
                  child: Text(
                    'No BLE traffic yet',
                    style: TextStyle(color: Colors.grey, fontFamily: 'monospace'),
                  ),
                )
              : ListView.builder(
                  controller: _scrollController,
                  padding: const EdgeInsets.all(8),
                  itemCount: entries.length,
                  itemBuilder: (context, i) {
                    final e = entries[i];
                    return Padding(
                      padding: const EdgeInsets.symmetric(vertical: 1),
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            '${e.timestamp.hour.toString().padLeft(2, '0')}:'
                            '${e.timestamp.minute.toString().padLeft(2, '0')}:'
                            '${e.timestamp.second.toString().padLeft(2, '0')} ',
                            style: const TextStyle(
                              color: Colors.grey,
                              fontSize: 11,
                              fontFamily: 'monospace',
                            ),
                          ),
                          SizedBox(
                            width: 36,
                            child: Text(
                              '[${e.directionLabel}]',
                              style: TextStyle(
                                color: _colorForDirection(e.direction),
                                fontSize: 11,
                                fontFamily: 'monospace',
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          const SizedBox(width: 4),
                          Expanded(
                            child: Text(
                              e.message,
                              style: TextStyle(
                                color: _colorForDirection(e.direction),
                                fontSize: 11,
                                fontFamily: 'monospace',
                              ),
                            ),
                          ),
                        ],
                      ),
                    );
                  },
                ),
        ),
      ],
    );
  }
}
