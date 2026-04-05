import 'package:flutter/material.dart';
import '../core/rfcomm/rfcomm_service.dart';

/// Bottom sheet listing paired Classic BT devices.
/// [rfcomm] is passed directly because modal routes don't inherit the
/// parent's provider scope.
class ScanSheet extends StatefulWidget {
  final RfcommService rfcomm;
  const ScanSheet({super.key, required this.rfcomm});

  @override
  State<ScanSheet> createState() => _ScanSheetState();
}

class _ScanSheetState extends State<ScanSheet> {
  RfcommService get rfcomm => widget.rfcomm;
  bool _loading = false;

  @override
  void initState() {
    super.initState();
    rfcomm.addListener(_onChanged);
    _refresh();
  }

  @override
  void dispose() {
    rfcomm.removeListener(_onChanged);
    super.dispose();
  }

  void _onChanged() {
    if (mounted) setState(() {});
  }

  Future<void> _refresh() async {
    setState(() => _loading = true);
    await rfcomm.loadPairedDevices();
    if (mounted) setState(() => _loading = false);
  }

  @override
  Widget build(BuildContext context) {
    final devices = rfcomm.pairedDevices;
    final solosList = devices.where((d) => d.isSolos).toList();
    final otherList = devices.where((d) => !d.isSolos).toList();
    final cs = Theme.of(context).colorScheme;

    return DraggableScrollableSheet(
      expand: false,
      initialChildSize: 0.52,
      maxChildSize: 0.88,
      builder: (context, controller) => Column(
        children: [
          // Drag handle
          Padding(
            padding: const EdgeInsets.only(top: 10, bottom: 4),
            child: Container(
              width: 36,
              height: 4,
              decoration: BoxDecoration(
                color: cs.onSurfaceVariant.withValues(alpha: 0.3),
                borderRadius: BorderRadius.circular(2),
              ),
            ),
          ),
          // Header
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 8, 16, 8),
            child: Row(
              children: [
                Container(
                  width: 36,
                  height: 36,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: cs.primaryContainer,
                  ),
                  child: Icon(Icons.bluetooth_searching,
                      size: 18, color: cs.onPrimaryContainer),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Connect Glasses',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w700,
                          color: cs.onSurface,
                        ),
                      ),
                      Text(
                        'Pair in Android Settings first',
                        style: TextStyle(fontSize: 11, color: cs.onSurfaceVariant),
                      ),
                    ],
                  ),
                ),
                _loading
                    ? SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: cs.primary,
                        ),
                      )
                    : IconButton(
                        icon: const Icon(Icons.refresh_rounded, size: 18),
                        onPressed: _refresh,
                        visualDensity: VisualDensity.compact,
                      ),
              ],
            ),
          ),
          Divider(height: 1, color: cs.outlineVariant.withValues(alpha: 0.35)),
          // Device list
          Expanded(
            child: devices.isEmpty
                ? Center(
                    child: _loading
                        ? CircularProgressIndicator(color: cs.primary)
                        : Padding(
                            padding: const EdgeInsets.all(32),
                            child: Column(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Icon(Icons.bluetooth_disabled_rounded,
                                    size: 40,
                                    color: cs.onSurfaceVariant.withValues(alpha: 0.4)),
                                const SizedBox(height: 12),
                                Text(
                                  'No paired devices',
                                  style: TextStyle(
                                    fontSize: 15,
                                    fontWeight: FontWeight.w600,
                                    color: cs.onSurface,
                                  ),
                                ),
                                const SizedBox(height: 6),
                                Text(
                                  'Pair your Solos glasses in Android\nBluetooth settings, then refresh.',
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: cs.onSurfaceVariant,
                                  ),
                                ),
                              ],
                            ),
                          ),
                  )
                : ListView(
                    controller: controller,
                    padding: const EdgeInsets.only(bottom: 24),
                    children: [
                      if (solosList.isNotEmpty) ...[
                        _ListHeader(
                          label: 'Solos Glasses',
                          trailing: const Icon(Icons.star_rounded,
                              color: Colors.amber, size: 14),
                        ),
                        ...solosList.map((d) => _DeviceTile(
                              device: d,
                              highlight: true,
                              onTap: () async {
                                Navigator.pop(context);
                                await rfcomm.connect(d);
                              },
                            )),
                      ],
                      if (otherList.isNotEmpty) ...[
                        _ListHeader(label: 'Other Paired Devices'),
                        ...otherList.map((d) => _DeviceTile(
                              device: d,
                              highlight: false,
                              onTap: () async {
                                Navigator.pop(context);
                                await rfcomm.connect(d);
                              },
                            )),
                      ],
                    ],
                  ),
          ),
        ],
      ),
    );
  }
}

class _ListHeader extends StatelessWidget {
  final String label;
  final Widget? trailing;
  const _ListHeader({required this.label, this.trailing});

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 14, 16, 4),
      child: Row(
        children: [
          Text(
            label.toUpperCase(),
            style: TextStyle(
              fontSize: 10,
              fontWeight: FontWeight.w700,
              color: cs.primary,
              letterSpacing: 1.3,
            ),
          ),
          if (trailing != null) ...[const SizedBox(width: 5), trailing!],
        ],
      ),
    );
  }
}

class _DeviceTile extends StatelessWidget {
  final PairedDevice device;
  final bool highlight;
  final VoidCallback onTap;
  const _DeviceTile({
    required this.device,
    required this.highlight,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 3),
      child: Card(
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
          side: BorderSide(
            color: highlight
                ? cs.primary.withValues(alpha: 0.4)
                : cs.outlineVariant.withValues(alpha: 0.3),
          ),
        ),
        color: highlight
            ? cs.primaryContainer.withValues(alpha: 0.15)
            : cs.surfaceContainerLow,
        child: ListTile(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          leading: Container(
            width: 36,
            height: 36,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: highlight ? cs.primaryContainer : cs.surfaceContainerHighest,
            ),
            child: Icon(
              highlight ? Icons.bluetooth_connected_rounded : Icons.bluetooth_rounded,
              size: 17,
              color: highlight ? cs.onPrimaryContainer : cs.onSurfaceVariant,
            ),
          ),
          title: Text(
            device.name.isEmpty ? 'Unknown device' : device.name,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w600,
              color: highlight ? cs.primary : cs.onSurface,
            ),
          ),
          subtitle: Text(
            device.address,
            style: TextStyle(
              fontFamily: 'monospace',
              fontSize: 10,
              color: cs.onSurfaceVariant,
            ),
          ),
          trailing: Icon(Icons.chevron_right_rounded,
              color: cs.onSurfaceVariant, size: 18),
          onTap: onTap,
        ),
      ),
    );
  }
}
