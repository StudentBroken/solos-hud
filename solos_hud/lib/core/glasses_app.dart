import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'rfcomm/glasses_event_service.dart';

/// Base class for every "app" that runs on the glasses.
/// Implement this to add a new mini-app to the HUD.
abstract class GlassesApp {
  /// Unique identifier used for settings persistence.
  String get id;

  /// Display name shown in the app list.
  String get name;

  /// Icon shown in the app card.
  IconData get icon;

  /// Short description shown in the app card subtitle.
  String get description;

  /// Called once when the app is activated. Use for starting streams, timers, etc.
  void onActivate();

  /// Called when the app is deactivated or the user switches away.
  void onDeactivate();

  /// The payload string to send to the glasses on each refresh tick.
  /// Return null to skip sending (e.g. nothing has changed).
  String? buildGlassesPayload();

  /// Override to return a fully-rendered frame (428×240).
  /// When non-null this is used directly, bypassing the text renderer.
  /// Encode using the codec matching [preferredCodec].
  Future<Uint8List?> buildCustomFrame() async => null;

  /// The image codec the frame returned by [buildCustomFrame] is encoded with.
  /// Default: RLE565. Map-heavy apps should return [SolosProtocol.codecJPEG].
  int get preferredCodec => 2; // SolosProtocol.codecRLE565

  /// Override to request a specific HUD refresh interval in milliseconds.
  /// Returns null to use the global settings refresh rate.
  int? get preferredRefreshMs => null;

  /// Full-featured UI shown inside the phone app card.
  Widget buildPhoneWidget(BuildContext context);

  /// Compact preview shown in the app list when the app is not expanded.
  Widget buildPreviewWidget(BuildContext context);

  /// List of settings entries specific to this app.
  /// Each entry is shown in the Settings screen under the app's section.
  List<AppSettingEntry> get settingEntries => [];

  /// Called when a glasses button fires while this app is active and the
  /// launcher menu is NOT open. Return true to consume the event.
  bool onButtonEvent(GlassesEvent event) => false;
}

/// Descriptor for a single tunable parameter within an app.
class AppSettingEntry {
  final String key;
  final String label;
  final String? description;
  final AppSettingType type;
  final dynamic defaultValue;

  const AppSettingEntry({
    required this.key,
    required this.label,
    this.description,
    required this.type,
    required this.defaultValue,
  });
}

enum AppSettingType { toggle, slider, dropdown, text }
