import 'package:flutter/material.dart';
import '../../core/glasses_app.dart';
import 'custom_command_widget.dart';

enum TestColor { red, green, blue, white, black }

extension TestColorExtension on TestColor {
  String get label {
    switch (this) {
      case TestColor.red:
        return 'RED';
      case TestColor.green:
        return 'GREEN';
      case TestColor.blue:
        return 'BLUE';
      case TestColor.white:
        return 'WHITE';
      case TestColor.black:
        return 'BLACK';
    }
  }

  Color get displayColor {
    switch (this) {
      case TestColor.red:
        return Colors.red;
      case TestColor.green:
        return Colors.green;
      case TestColor.blue:
        return Colors.blue;
      case TestColor.white:
        return Colors.white;
      case TestColor.black:
        return Colors.black;
    }
  }

  /// Payload sent to HudController — prefix COLOR: triggers solid fill.
  String get payload => 'COLOR:$label';
}

class CustomCommandApp extends GlassesApp {
  TestColor selectedColor = TestColor.black;
  String lastCommand = '';
  final List<String> commandLog = [];

  @override
  String get id => 'custom_command';

  @override
  String get name => 'Custom Command';

  @override
  IconData get icon => Icons.terminal;

  @override
  String get description => 'Send color tests and raw commands to the glasses';

  @override
  void onActivate() {}

  @override
  void onDeactivate() {}

  @override
  String? buildGlassesPayload() =>
      lastCommand.isNotEmpty ? lastCommand : selectedColor.payload;

  @override
  Widget buildPhoneWidget(BuildContext context) {
    return CustomCommandWidget(app: this);
  }

  @override
  Widget buildPreviewWidget(BuildContext context) {
    return Row(
      children: [
        Container(
          width: 14,
          height: 14,
          decoration: BoxDecoration(
            color: selectedColor.displayColor,
            border: Border.all(color: Colors.grey),
            borderRadius: BorderRadius.circular(3),
          ),
        ),
        const SizedBox(width: 6),
        Text(
          selectedColor.label,
          style: Theme.of(context).textTheme.bodySmall,
        ),
      ],
    );
  }
}
