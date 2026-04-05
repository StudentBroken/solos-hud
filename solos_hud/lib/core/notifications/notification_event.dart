import 'dart:typed_data';

// ── Maneuver type ─────────────────────────────────────────────────────────────

enum Maneuver {
  straight,
  turnLeft,
  turnRight,
  slightLeft,
  slightRight,
  sharpLeft,
  sharpRight,
  uTurn,
  roundabout,
  merge,
  arrive,
  unknown;

  static Maneuver fromText(String text) {
    final t = text.toLowerCase();
    if (t.contains('arrived') || t.contains('destination') || t.contains('you have arrived')) {
      return Maneuver.arrive;
    }
    if (t.contains('u-turn') || t.contains('u turn') || t.contains('uturn')) {
      return Maneuver.uTurn;
    }
    if (t.contains('roundabout') || t.contains('traffic circle') || t.contains('rotary')) {
      return Maneuver.roundabout;
    }
    if (t.contains('slight left') || t.contains('keep left') || t.contains('bear left')) {
      return Maneuver.slightLeft;
    }
    if (t.contains('slight right') || t.contains('keep right') || t.contains('bear right')) {
      return Maneuver.slightRight;
    }
    if (t.contains('sharp left') || t.contains('hard left')) return Maneuver.sharpLeft;
    if (t.contains('sharp right') || t.contains('hard right')) return Maneuver.sharpRight;
    if (t.contains('turn left') || (t.contains('left') && !t.contains('right'))) {
      return Maneuver.turnLeft;
    }
    if (t.contains('turn right') || (t.contains('right') && !t.contains('left'))) {
      return Maneuver.turnRight;
    }
    if (t.contains('merge') || t.contains('ramp') || t.contains('take the')) {
      return Maneuver.merge;
    }
    if (t.contains('continue') || t.contains('head') || t.contains('go straight')) {
      return Maneuver.straight;
    }
    return Maneuver.unknown;
  }
}

// ── Parsed navigation instruction ────────────────────────────────────────────

class NavInstruction {
  final Maneuver maneuver;
  final String distanceText; // e.g. "200 m" or "1.2 km"
  final String streetName;   // e.g. "Oak Avenue"
  final String raw;          // full original string

  const NavInstruction({
    required this.maneuver,
    required this.distanceText,
    required this.streetName,
    required this.raw,
  });

  static final _distRe = RegExp(
    r'(\d+(?:[.,]\d+)?)\s*(m|km|mi|ft|yard|yd)',
    caseSensitive: false,
  );

  // Instruction text patterns:
  // "In 200 m, turn right onto Oak Avenue"
  // "Turn right onto Oak Avenue in 200 m"
  // "Continue for 2.1 km on Main Road"
  // "You have arrived at your destination"
  factory NavInstruction.parse(String text) {
    final maneuver = Maneuver.fromText(text);

    // Distance
    final distMatch = _distRe.firstMatch(text);
    final distanceText = distMatch != null
        ? '${distMatch.group(1)!.replaceAll(',', '.')} ${distMatch.group(2)!}'
        : '';

    // Street name: strip known prefixes + distance token
    var street = text
        .replaceAll(_distRe, '')
        .replaceAll(RegExp(r'^in\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'turn\s+(left|right)\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'slight\s+(left|right)\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'sharp\s+(left|right)\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'u-?turn\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'continue\s+(for\s+)?', caseSensitive: false), '')
        .replaceAll(RegExp(r'head\s+\w+\s+on\s+', caseSensitive: false), '')
        .replaceAll(RegExp(r'\bon\b\s*', caseSensitive: false), '')
        .replaceAll(RegExp(r'\bonto\b\s*', caseSensitive: false), '')
        .replaceAll(RegExp(r'[,·•\-]+', caseSensitive: false), '')
        .replaceAll(RegExp(r'\s{2,}'), ' ')
        .trim();

    // Capitalise first letter only
    if (street.isNotEmpty) {
      street = street[0].toUpperCase() + street.substring(1);
    }

    return NavInstruction(
      maneuver: maneuver,
      distanceText: distanceText,
      streetName: street,
      raw: text,
    );
  }

  bool get isArrival => maneuver == Maneuver.arrive;

  /// Short label for the maneuver, shown on the HUD.
  String get maneuverLabel => switch (maneuver) {
        Maneuver.straight    => 'CONTINUE',
        Maneuver.turnLeft    => 'TURN LEFT',
        Maneuver.turnRight   => 'TURN RIGHT',
        Maneuver.slightLeft  => 'SLIGHT LEFT',
        Maneuver.slightRight => 'SLIGHT RIGHT',
        Maneuver.sharpLeft   => 'SHARP LEFT',
        Maneuver.sharpRight  => 'SHARP RIGHT',
        Maneuver.uTurn       => 'U-TURN',
        Maneuver.roundabout  => 'ROUNDABOUT',
        Maneuver.merge       => 'MERGE',
        Maneuver.arrive      => 'ARRIVED',
        Maneuver.unknown     => 'FOLLOW ROUTE',
      };
}

// ── Notification action info ──────────────────────────────────────────────────

class NotifActionInfo {
  final int index;
  final String label;
  final bool isReply;
  const NotifActionInfo({
    required this.index,
    required this.label,
    required this.isReply,
  });
}

// ── Notification event ────────────────────────────────────────────────────────

class NotificationEvent {
  final String     packageName;
  final String     appName;
  final String     title;
  final String     text;
  final String     bigText;
  final String?    artist;      // music: artist / sub-text
  final Uint8List? albumArtRaw; // music: 128×128 JPEG bytes
  final DateTime   timestamp;
  final String     key;
  final bool       isOngoing;
  final bool       isRemoval;
  final bool       isMedia;
  final bool       isCall;
  final bool       isMessage;
  final List<NotifActionInfo> actions;

  NotificationEvent({
    required this.packageName,
    required this.appName,
    required this.title,
    required this.text,
    required this.bigText,
    this.artist,
    this.albumArtRaw,
    required this.timestamp,
    required this.key,
    this.isOngoing  = false,
    this.isRemoval  = false,
    this.isMedia    = false,
    this.isCall     = false,
    this.isMessage  = false,
    this.actions    = const [],
  });

  factory NotificationEvent.fromMap(Map<dynamic, dynamic> m) {
    final removed = m['removed'] as bool? ?? false;

    // Album art arrives as List<int> from Kotlin ByteArray
    Uint8List? art;
    final rawArt = m['albumArt'];
    if (rawArt is List) {
      art = Uint8List.fromList(rawArt.cast<int>());
    } else if (rawArt is Uint8List) {
      art = rawArt;
    }

    final actions = (m['actions'] as List?)?.map((a) {
      final map = a as Map;
      return NotifActionInfo(
        index:   map['index']    as int?    ?? 0,
        label:   map['label']    as String? ?? '',
        isReply: map['hasReply'] as bool?   ?? false,
      );
    }).toList() ?? const <NotifActionInfo>[];

    return NotificationEvent(
      packageName: m['packageName'] as String? ?? '',
      appName:     m['appName']     as String? ?? '',
      title:       m['title']       as String? ?? '',
      text:        m['text']        as String? ?? '',
      bigText:     m['bigText']     as String? ?? '',
      artist:      m['artist']      as String?,
      albumArtRaw: art,
      timestamp:   DateTime.fromMillisecondsSinceEpoch(m['timestamp'] as int? ?? 0),
      key:         m['key']         as String? ?? '',
      isOngoing:   m['isOngoing']   as bool?   ?? false,
      isRemoval:   removed,
      isMedia:     m['isMedia']     as bool?   ?? false,
      isCall:      m['isCall']      as bool?   ?? false,
      isMessage:   m['isMessage']   as bool?   ?? false,
      actions:     actions,
    );
  }

  NotifActionInfo? get answerAction {
    for (final a in actions) {
      final l = a.label.toLowerCase();
      if (l.contains('answer') || l.contains('accept')) return a;
    }
    return null;
  }

  NotifActionInfo? get declineAction {
    for (final a in actions) {
      final l = a.label.toLowerCase();
      if (l.contains('decline') || l.contains('reject') || l.contains('dismiss')) return a;
    }
    return actions.isNotEmpty ? actions.last : null;
  }

  NotifActionInfo? get replyAction {
    for (final a in actions) {
      if (a.isReply) return a;
    }
    return null;
  }

  static const _navPackages = {'com.google.android.apps.maps', 'com.waze'};

  bool get isNavigation  => _navPackages.contains(packageName);
  bool get isPhoneCall   => packageName == 'com.android.phone' ||
                            packageName == 'com.android.server.telecom';

  /// The best instruction string from the notification.
  String get instructionText {
    // Google Maps puts the full instruction in bigText when different from text
    if (bigText.isNotEmpty && bigText != text) return bigText;
    // Some versions put it in title + text
    if (title.isNotEmpty && text.isNotEmpty) return '$title $text';
    return text.isNotEmpty ? text : title;
  }

  /// Parsed turn instruction (only meaningful when isNavigation == true).
  NavInstruction? get navInstruction {
    final raw = instructionText.trim();
    if (raw.isEmpty || isRemoval) return null;
    return NavInstruction.parse(raw);
  }

  @override
  String toString() => '[$appName] $title: $text';
}
