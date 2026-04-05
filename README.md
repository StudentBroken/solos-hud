# Solos HUD

An open-source, extensible alternative client for Solos Smart Glasses, built with Flutter.

The goal of this project is to create a dynamic, reactive Heads-Up Display (HUD) experience that surpasses the capabilities of the stock application. By communicating directly with the glasses over Classic Bluetooth RFCOMM using a reverse-engineered protocol, this app renders custom widgets and pushes them as image frames (RLE565 encoded) directly to the smart glasses display.

## Overview

Solos HUD acts as a real-time smart assistant right in your field of vision. It skips basic text alerts in favor of a fully rendered graphics pipeline, allowing any Flutter widget to be rasterized and mapped to the 428×240 waveguide display.

### Modules & Roadmap
* **Real-time Notifications:** Intercept incoming calls and messages via Android's `NotificationListenerService`.
* **Weather Integration:** Free tier weather polling and forecasting via Open-Meteo. 
* **VESC Telemetry:** Live speed, voltage, current, and temperature display for PEVs (Personal Electric Vehicles) over BLE NUS.
* **AI Voice Assistant:** Voice-activated assistant using Claude Haiku with context awareness.
* **Maps & Navigation:** Off-screen rendering of Mapbox static maps or offline `flutter_map` tiles for turn-by-turn directions.
* **Translation & Search:** Inline text and language translator tools using DDG Instant Answers and Claude.

## Architecture

- **UI & Graphics:** Flutter (Dart) builds both the mobile companion app interface and the virtual layouts for the HUD.
- **Native Services:** Android (Kotlin) handles low-level system accesses like Bluetooth RFCOMM connections and notification listeners.
- **Communication:** Frames are generated off-screen with `RepaintBoundary`, converted into RGB565, RLE-compressed natively, and chunked into binary packets according to the `SolosProtocol`.

## Documentation

- **`ROADMAP.md`:** Detailed architectural designs, implementation steps, and UI layouts for all planned features.
- **`solos.md`:** Comprehensive protocol documentation based on packet captures and APK decompilation. Detailed breakdowns of the binary headers, codecs, and RFCOMM structures.

## Setup

*Instructions on how to build and run the Flutter application will be added soon as the core BT architecture stabilizes.*
