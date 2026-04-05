# Solos HUD — Feature Roadmap

> Each feature includes: approach rationale, implementation steps, phone UI design,
> and HUD frame design. Build order is listed at the end.

---

## On Rendering a Real Map

**Short answer: yes, and it looks great.**

The existing HUD pipeline already does exactly this — a Flutter widget is rendered
to a bitmap (`RenderRepaintBoundary.toImage()`), JPEG-encoded, and sent as an IMAGE
packet. The map is just another widget fed into that same pipeline.

Two solid options:

### Option A — Mapbox / Google Static Maps API (recommended to start)
Send a single HTTPS request with lat/lng, zoom, size `480x128`, encoded route polyline,
and a position marker → API returns a JPEG → send to glasses. Fast, one call, looks
professional. Mapbox free tier: 50,000 requests/month.

```
GET https://api.mapbox.com/styles/v1/mapbox/dark-v11/static/
    path-4+00aaff-0.8({encoded_polyline}),
    pin-s-arrow+ff0000({lng},{lat})
    /{lng},{lat},{zoom}/{480x128}
    ?access_token={TOKEN}
```

### Option B — flutter_map (fully offline-capable)
Render a `flutter_map` widget off-screen at 480×128, wait for tiles to paint, then
capture. Supports OSM + custom tile servers. More work but works offline.
The trick: use a `Completer` that resolves once the `TileLayer` fires `onTileLoaded`
for all visible tiles, then call `repaintBoundary.toImage()`.

**For the HUD we use Option A** (instant, predictable latency) and fall back to
Option B if the API quota runs out or the user is offline.

---

## 1. Notifications

**Difficulty:** Medium
**Unlocks:** Maps Navigation (for free), Call Screen, Quick Replies

### Why first
Every other "reactive" feature (nav, calls, messages) can be built on top of the
notification stream at zero extra cost. Android already delivers rich structured data
inside every notification.

### Approach
`NotificationListenerService` runs natively in Kotlin and forwards events to Dart
through the existing `EventChannel` pattern (same as RFCOMM events). The user grants
access once in **Settings → Notification Access**.

### Native side (`NotificationService.kt`)
```kotlin
class NotificationService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title  = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text   = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        // send over EventChannel → Dart
    }
}
```

### Steps
1. `NotificationService.kt` + `AndroidManifest.xml` entry (BIND_NOTIFICATION_LISTENER_SERVICE).
2. `lib/core/notifications/notification_service.dart` — subscribe to EventChannel,
   expose `Stream<NotificationEvent>`, built-in blocklist for system noise
   (battery, media playback, download progress, self-app).
3. `HudController` interrupt layer — when a notification arrives, pause the current
   app tick, push the overlay frame for a configurable duration (default 4 s), resume.
   Priority queue: calls > messages > other.
4. `lib/apps/notifications/notifications_app.dart` — the "inbox" app.

### Phone UI
- Card with a live notification feed (last 20 items, grouped by app).
- Per-app toggle rows: show icon, app name, on/off switch, display-duration slider.
- "Grant Access" banner with a direct deep-link button when permission is not granted.
- Notification preview chip at the top of the card showing the last received item.

### HUD Frame (480×128)
```
┌──────────────────────────────────────────────────────────────────┐
│  ● WhatsApp                                         10:42 AM     │
│  João  ·  "Hey are you coming tonight?"                          │
└──────────────────────────────────────────────────────────────────┘
```
- Left: filled circle in app brand colour (WhatsApp green, Gmail red, etc.) + app name.
- Body: bold sender name · truncated message text (max 2 lines).
- Top-right: time.
- Fades out after duration, sliding up off-screen.

---

## 2. Weather

**Difficulty:** Low
**API cost:** Free (Open-Meteo, no key required)

### Approach
`geolocator` (already present) → coordinates → Open-Meteo current + hourly JSON →
`WeatherService` ChangeNotifier → WeatherApp renders phone widget + glasses frame.
Cache with a 10-minute TTL; refresh on app foreground or location change > 5 km.

### Steps
1. `lib/core/weather/weather_service.dart`:
   - Fetches `current=temperature_2m,apparent_temperature,weathercode,windspeed_10m,winddirection_10m,precipitation,uv_index`.
   - `hourly=temperature_2m,weathercode` for the next 6 hours.
   - Maps WMO weather codes → `WeatherCondition` enum (Sunny, Cloudy, Rain, Snow, …).
   - Each condition has a label string and a Nerd Font / Unicode icon char.
2. `lib/apps/weather/weather_app.dart`.
3. Add UV index + air quality to display when available.

### Phone UI
- Large current-conditions hero card: big temperature, condition icon, feels-like, wind arrow.
- Horizontal scroll strip for next 6 hours: time, icon, temp.
- Expandable details row: humidity, UV index, sunrise/sunset, precipitation chance.
- Subtle animated background tint matching condition (blue tint = clear, grey = overcast, etc.)

### HUD Frame (480×128)
```
┌──────────────────────────────────────────────────────────────────┐
│   ⛅  18°C / feels 15°C          Lisbon                         │
│   Partly Cloudy  ·  Wind 14 km/h NW  ·  UV 5  ·  Rain 10%      │
└──────────────────────────────────────────────────────────────────┘
```
- Condition icon left, large temp, city name right.
- Second line: full details strip.
- Alternate view (every 8 s): hourly forecast as a mini bar chart.

---

## 3. VESC Telemetry

**Difficulty:** Medium
**API cost:** None — direct BLE

### Approach
VESC firmware ≥ 5.x exposes a **Nordic UART Service** (NUS) BLE bridge:
- Service UUID: `6e400001-b5a3-f393-e0a9-e50e24dcca9e`
- RX char: `6e400002…` (write here to send commands)
- TX char: `6e400003…` (subscribe here to receive responses)

`flutter_blue_plus` (already in project) handles this. Packets are:
`0x02 | length_byte | payload... | CRC16_hi | CRC16_lo | 0x03`

`COMM_GET_VALUES` = packet ID 4. Response contains ~60 bytes of telemetry in big-endian
floats/ints.

### Steps
1. `lib/core/vesc/vesc_crc.dart` — CRC16/CCITT implementation (30 lines).
2. `lib/core/vesc/vesc_packet.dart` — frame builder + parser.
3. `lib/core/vesc/vesc_service.dart`:
   - Scan for NUS UUID.
   - Connect, subscribe to TX, poll `COMM_GET_VALUES` every 200 ms.
   - Parse: `erpm → speed (km/h) = erpm / (motor_poles / 2) / gear_ratio * wheel_circumference / 60`.
   - Expose: speed, voltage, input_current, motor_current, duty_cycle, watt_hours, temp_fet, temp_motor, fault_code.
4. Settings: motor poles, wheel diameter (mm), max battery voltage (for % bar).
5. `lib/apps/vesc/vesc_app.dart`.

### Phone UI
- Top: VESC connection status badge (scan → connecting → connected) + device name.
- Speed: giant number center-screen with unit label, animated gauge arc below it.
- 3-column stat grid: Voltage · Input Current · Motor Current.
- 2-column row: Duty Cycle progress bar · Watt-Hours consumed.
- Temperatures: FET and motor, colour-coded (green → orange → red).
- Fault code banner (hidden when NONE, red prominent when active).
- Settings: motor poles input, wheel diameter slider, battery cell count.

### HUD Frame (480×128)
```
┌──────────────────────────────────────────────────────────────────┐
│  47.3V  ████████░░  78%          FAULT: NONE                    │
│              38.4 km/h              1 380 W   Motor 41°C        │
└──────────────────────────────────────────────────────────────────┘
```
- Voltage + battery bar top-left. Speed large-center. Power and temp right.
- Fault code replaces voltage bar when non-zero (red, flashing border).

---

## 4. Voice Assistant

**Difficulty:** High
**API cost:** Claude Haiku ~$0.0008 / query

### Approach
Long-press glasses **main** button → `SpeechService` starts Android `SpeechRecognizer`
(no internet needed for recognition if Offline model installed, otherwise Google STT) →
transcript sent to Claude Haiku with a compact system prompt → response streamed back
token by token → each chunk updates the glasses frame → full response spoken by TTS.

The AI service is context-aware: it is told the currently active app, current GPS city,
time of day, and recent glasses events so it can give relevant answers.

### Steps
1. **Platform channel** `lib/core/voice/speech_channel.dart`:
   - Kotlin: `SpeechRecognizer` → EventChannel streams partials + final result.
   - Shows animated "waveform" frame on glasses while listening.
2. `lib/core/voice/claude_service.dart`:
   - `http` POST to `https://api.anthropic.com/v1/messages` (streaming).
   - System prompt includes: current time, city, active app name, last 3 events.
   - Two response modes: **HUD mode** (≤ 100 chars, one punchy line) and **detail mode**
     (up to 400 chars, scrolled on glasses).
   - API key stored in `shared_preferences`, never compiled in.
3. `lib/core/voice/tts_service.dart` — `flutter_tts`, speaks while glasses display.
4. `lib/apps/assistant/assistant_app.dart` — conversation history.
5. Button wiring in `HudController`: long-press main → activate, short press during
   assistant → dismiss and return to previous app.

### Phone UI
- Chat-style conversation view (bubbles, user = right, assistant = left).
- At the bottom: a large mic button with press-and-hold animation, waveform visualiser.
- When listening: animated sound bars. When processing: pulsing logo.
- Conversation is persistent (saved in SharedPreferences as JSON).
- Settings: API key field (obscured), response length toggle, TTS speed, wake-word
  toggle (always-on listening, optional).

### HUD Frame sequence
```
[   ··· Listening ···  ]   waveform animation

[  ✦ Thinking…         ]   spinning dot

[  18°C cloudy · rain later today    ]   streamed in token by token
```

---

## 5. Maps Navigation (Rendered)

**Difficulty:** Medium (after Notifications exist)
**API cost:** Mapbox free tier: 50k req/month

### How rendering works
The existing HUD already renders a Flutter widget to a JPEG and sends it as an IMAGE
packet. Navigation is the same pipeline — we just point it at a map image instead of
a dashboard widget.

**Route A (Static API — use this):**
Every time position changes by ≥ 15 m, fire a Mapbox Static Images request:
- Map style: `mapbox/dark-v11` (matches glasses dark background).
- Size: `480x128`.
- Encoded route polyline as a path layer.
- Current position as a custom marker.
- Returned JPEG → straight into `SolosProtocol.imagePacket()`.

One request per position update. At 15 m intervals on a bicycle that is ~1 req / 2 s
at 27 km/h — well within free tier.

**Route B (flutter_map — offline / no quota):**
Off-screen `InteractiveViewer`-less `flutter_map` widget in a `RepaintBoundary`,
auto-centered on current position, with `PolylineLayer` for route and `MarkerLayer`
for position arrow. Capture once tiles finish loading. Use OSM Cycle Map tiles for
cycling, regular OSM for driving.

### Steps
1. `lib/core/routing/routing_service.dart`:
   - Input: destination (address or lat/lng).
   - Uses OpenRouteService (free, 2000 req/day) or OSRM (self-host option).
   - Returns a `List<LatLng>` polyline + `List<RouteStep>` (instruction, distance, turn type).
   - Tracks current step by matching GPS position against waypoints.
2. `lib/core/routing/map_renderer.dart`:
   - `renderFrame(LatLng position, double heading, RouteStep currentStep)` → `Uint8List` JPEG.
   - Calls Mapbox Static API, gets response, optionally composites text overlay
     (street name + distance) using Flutter's Canvas on top of the returned image.
3. `lib/apps/navigation/navigation_app.dart`:
   - Active → overrides HUD tick with map frame.
   - Notification listener intercepts Google Maps notifications as a free data source
     (no routing needed if user is already navigating in Maps).
4. Search / address input with autocomplete (Nominatim OSM geocoding, free).

### Phone UI
- Search bar at top with autocomplete dropdown (Nominatim API).
- Preview map (`flutter_map`, interactive) with the calculated route drawn.
- Step-by-step list expandable below the map.
- Active navigation bottom sheet: current instruction card, next instruction preview,
  ETA, distance remaining. Big "Stop Navigation" button.
- Turn indicator arrow large on the right side of the bottom sheet.

### HUD Frame (480×128) — two zones
```
┌──────────────────────────────────────────────────────────────────┐
│  [MAP TILE RENDER — dark OSM/Mapbox, cyan route line, white arrow│
│   pointing in travel direction, next turn highlighted in yellow] │
│                                                          0.3 km  │
│  ↱  Turn right — Oak Avenue                                      │
└──────────────────────────────────────────────────────────────────┘
```
Top 80 px: rendered map tile.
Bottom 48 px: black bar with turn arrow icon + street name + distance.
When approaching turn (< 50 m): bottom bar flashes, arrow grows.

---

## 6. Search

**Difficulty:** Low-Medium
**API cost:** Free (DuckDuckGo Instant Answers) or Claude Haiku fallback

### Approach
Three-tier resolver in `lib/core/search/search_service.dart`:
1. **Local** — regex match for math expressions, unit conversions (`50 mph to km/h`),
   time zone queries (`time in Tokyo`), currency using cached daily FX rates.
2. **DuckDuckGo Instant Answers API** — returns structured answers for facts, Wikipedia
   summaries, definitions. Free, no key.
3. **Claude Haiku** — if DDG returns nothing, ask Claude for a one-line factual answer.

Surface via: voice assistant (Feature 4), or a dedicated typed search bar in the app.

### Phone UI
- Prominent search bar below the app header.
- Query history chips below the bar (scrollable horizontal).
- Result card: icon (source badge), bold answer text, source attribution link.
- When Claude answers: "AI" badge, subtle shimmer while loading.
- Math result: monospace large display like a calculator.
- Unit conversion: shows both values with unit labels, swap button.

### HUD Frame
```
┌──────────────────────────────────────────────────────────────────┐
│  50 mph  =  80.5 km/h                                            │
│  ──  or  ──                                                      │
│  Tokyo  ·  16:42  Thu  (UTC+9)                                   │
└──────────────────────────────────────────────────────────────────┘
```
Displayed for 6 s then fades. Pressing front button extends display by 3 s.

---

## 7. Translator

**Difficulty:** Low (with Claude already integrated)
**API cost:** Covered by Claude Haiku budget or MyMemory free tier (5k words/day)

### Approach
If the Voice Assistant is built first, the Translator is basically a different system
prompt + a dedicated UI. Speak or type the phrase, translator returns the result, TTS
speaks it in the target language using `flutter_tts` language switching.

For text-only (no Claude key): MyMemory API is free for 5000 words/day and supports
auto-detect source language.

### Steps
1. `lib/core/translate/translate_service.dart`:
   - `translate(text, from: 'auto', to: 'en')` → `Future<String>`.
   - Tries MyMemory first (free); falls back to Claude for context-aware translation.
2. `lib/apps/translator/translator_app.dart`.
3. TTS speaks result in target language (set `flutter_tts` locale before speaking).
4. Quick language pair presets: saved in settings (EN↔PT, EN↔ES, etc.).

### Phone UI
- Two large text areas: source (top, editable / speech input) and result (bottom, read-only).
- Language picker pills between the two areas, with a swap button.
- Mic button on source field, speak button (megaphone) on result field.
- History list (last 20 translations) below, tap to restore.
- Saved phrases star button to bookmark a translation.

### HUD Frame (480×128)
```
┌──────────────────────────────────────────────────────────────────┐
│  PT → EN                                                         │
│  "Onde fica a farmácia mais próxima?"                            │
│  "Where is the nearest pharmacy?"                                │
└──────────────────────────────────────────────────────────────────┘
```
Source phrase in grey, translated phrase in white bold below.
Language pair badge top-left.

---

## Build Order

| Step | Feature | Why now |
|------|---------|---------|
| 1 | **Notifications** | Foundation for Maps, Calls, Messages |
| 2 | **Weather** | Quick win, zero dependencies, high daily value |
| 3 | **VESC** | Self-contained, unique, BLE already set up |
| 4 | **Voice Assistant** | Unlocks Search + Translator as thin wrappers |
| 5 | **Search** | 1 service file on top of Assistant |
| 6 | **Translator** | 1 service file + system prompt tweak |
| 7 | **Maps Navigation** | Needs Notifications + ideally Routing service |

---

## General UI Principles (apply everywhere)

### Phone UI
- Dark theme throughout (M3 dark, current surface colour `#1a1a2e`-ish).
- Every app card has: status badge (active/inactive), a preview thumbnail of the
  current glasses frame, and an expand button for settings.
- Consistent section headers (already in codebase — `_SectionHeader` pattern).
- Loading states: skeleton shimmer, not spinners.
- Empty states: icon + short message + action button (e.g. "No route — tap to search").
- Settings: always accessible from within the app card, not only the global settings screen.

### HUD Frames
- Black background always (glasses are see-through AR — black = transparent).
- Two visual zones: **data zone** (content, max 80% width) and **status zone**
  (top-right corner: time, battery, BT icon — 1-char each).
- Typography: monospace for numbers, sans-serif for labels.
  Numbers in white, labels in `Colors.grey.shade400`, highlights in `Colors.cyanAccent`.
- Avoid hairline text < 14 px equivalent at 480 px width — it becomes unreadable.
- Critical alerts (fault codes, low battery, sharp turns) use a full-width flashing
  border rather than colour changes alone (colour-blind friendly).
- Animations: entrance slide-in from bottom, exit fade-out. Never abrupt cuts.

---

## More Feature Ideas

### Transport
- **Speed Limit Overlay** — match GPS position against OpenStreetMap `maxspeed` tags;
  show your speed vs limit, highlight red when over.
- **Public Transit Live** — nearest departures from your stop using a transit API
  (Transitland covers most cities, free).
- **Flight Tracker** — if a FlightAware/AviationStack notification arrives, show flight
  status, gate, and delay on glasses.
- **Bike / Scooter Share** — nearby available bikes (Citymapper / GBFS feed).
- **Parking Timer** — set duration, countdown on glasses, alert when expiring.

### Communication
- **Call Screen** — show caller name/number when a call notification arrives;
  front = answer, back = decline (via Android intent).
- **Quick Reply Templates** — hold front button to speak a canned reply to the last
  message ("On my way", "5 minutes", "Can't talk"); sent via Android share intent.
- **Calendar Widget** — pull next 3 events from `CalendarContract`; show time-to-next
  event as a subtle HUD element alongside the main app.
- **Reminders** — user-set time + message; push to glasses at trigger time.

### Fitness
- **Cadence Sensor** — BLE Cycling Speed and Cadence profile (CSC, 0x1816); pair and
  display RPM on the VESC frame or a dedicated cycling frame.
- **Running Pace Zones** — when workout app is active, colour-code pace relative to
  target zone (Z1–Z5) in real time.
- **Hydration Nudge** — configurable reminder every N minutes during activity.
- **VO2 Estimation** — from HR + speed + weight, estimate effort as % VO2max; display.

### Productivity
- **Teleprompter** — paste a script; scrolls at adjustable WPM; front/back buttons
  speed up / slow down; main button pauses.
- **Flashcards** — import CSV (Anki export format); glasses display question for 3 s,
  front button flips to answer, back button marks wrong (shows again), main = correct.
- **Timer / Lap Stopwatch** — glasses-native: main = start/stop, front = lap, back = reset.
- **Shopping List** — simple list in the app; cycle items with glasses buttons; main = check off.

### AI / Vision
- **Context-Aware Mode Switcher** — background ML model watches time + motion + location
  and auto-selects the best HUD app (stationary at home = weather + calendar;
  cycling = VESC or speed; walking = notifications; meeting room = do-not-disturb).
- **Live Transcription** — continuous STT → rolling 2-line subtitle on glasses;
  great for lectures, meetings, or foreign-language conversations.
- **OCR Reader** — take a phone camera shot of text (menu, sign, document); show the
  most relevant line on glasses (translated if needed).
- **Scene Describer** — phone camera → Claude Vision API → 1-line description spoken
  + shown on glasses; accessibility use case.
- **Language Learning** — periodic vocabulary flash: target word on glasses, voice
  pronounces it; button to reveal translation; daily word count tracker.

### System & Polish
- **Theme Switcher** — HUD colour profiles (cyan/dark = current, amber/dark = night,
  green/dark = matrix, white/dark = high-contrast).
- **Haptic Feedback Sync** — when a HUD notification appears, the phone vibrates a
  distinct pattern so you know to look at the glasses.
- **Battery Saver Auto-Mode** — when phone battery < 20%, auto-drop glasses refresh
  rate to 1 Hz and suppress non-critical notifications.
- **Wear OS Companion** — mirror the active HUD app on a watch tile; glasses buttons
  forwarded through watch when not wearing the Solos.
- **Widgets / Lock Screen** — Android lock-screen widget showing the same data as
  whatever is on the glasses (useful if glasses are off but phone is in hand).
