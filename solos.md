# Solos Smart Glasses — Reverse Engineering Notes

Sources:
- Blog: John Floren (2025/8/28) — Wireshark capture of actual BT traffic
- APK: Solos_2.0.844_APKPure.apk — jadx decompile

Both sources agree completely. The blog fills in the display dimensions and encoding details;
the APK fills in the full packet type table and the image sub-header structure.

---

## 1. Connection

**Protocol:** Classic Bluetooth RFCOMM (NOT BLE GATT)

| Item | Value |
|------|-------|
| RFCOMM Service UUID | `000011A0-0000-1000-8000-00805F9B34FB` |
| RFCOMM Port | 1 |
| Connection call | `BluetoothSocket.connect(mac, port=1)` |

> The app also does BLE scanning for discovery (finding nearby paired devices), but all
> data to the display goes over Classic BT RFCOMM.
> On iOS, Classic BT is MFi-restricted — the Solos app likely has an MFi cert.

---

## 2. Binary Packet Format

All values **little-endian**. Every packet starts with magic `0x601D`.

### Outer Header (10 bytes)

```
Offset  Size  Description
------  ----  -----------
0       2     Magic: 0x1D 0x60  (= 0x601D = 24605 decimal)
2       2     PacketType (short)
4       2     Reserved: 0x00 0x00
6       4     PayloadLength (int) — byte count of everything after this header
```

### Known Packet Types (from PacketType.java)

| ID | Name | Direction | Notes |
|----|------|-----------|-------|
| 1  | PING | → glasses | keep-alive; payload is a UTF-8 string |
| 4  | WAKE_UP | → glasses | empty payload |
| 5  | IMAGE | → glasses | see Image Packet below |
| 6  | IMAGE_REQUEST | ← glasses | |
| 7  | CAPABILITIES_GET | → glasses | |
| 8  | CAPABILITIES_RESPONSE | ← glasses | |
| 18 | STATUS_GET | → glasses | |
| 19 | STATUS_RESPONSE | ← glasses | |
| 22 | SLEEP | → glasses | empty payload |
| 33 | SET_DISPLAY_MODE | → glasses | |
| 34 | PONG | ← glasses | response to PING |
| 36 | ANT_COMMAND | → glasses | ANT+ sensor bridge |

---

## 3. Image Packet (type = 5)

Payload = **ImageHeader (14 bytes)** + **image data bytes**

### ImageHeader (14 bytes)

```
Offset  Size  Description
------  ----  -----------
0       1     Codec (see table below)
1       1     Window (use 0)
2       2     Chunk index (short, 0-based; use 0 for single-chunk)
4       2     Total chunks (short; use 1 for single-chunk)
6       2     X position (short)
8       2     Y position (short)
10      2     Width (short)
12      2     Height (short)
```

### Image Codecs (from ImageCodec.java)

| Byte | Name | Notes |
|------|------|-------|
| 0x01 | RGB565 | Raw 16-bit pixels, no compression |
| 0x02 | RLE565 | Run-length encoded RGB565 — **what the app actually uses** |
| 0x08 | JPEG | |
| 0x20 | PNG | |
| 0x40 | COLOUR8 / COLOR8 | |

### Example IMAGE packet (from Wireshark capture)

```
1d 60          — magic 0x601D
05 00          — PacketType = 5 (IMAGE)
00 00          — reserved
1c 4c 00 00    — payloadLength = 19484 bytes
--- ImageHeader ---
02             — codec = 2 (RLE565)
00             — window = 0
00 00          — chunk index = 0
01 00          — total chunks = 1
00 00          — x = 0
00 00          — y = 0
ac 01          — width = 428
f0 00          — height = 240
--- RLE data follows ---
ff 00 00 ad 00 00 ff 00 00 ...
```

---

## 4. Display

| Property | Value |
|----------|-------|
| Width | **428 pixels** (confirmed by Wireshark: `0x01AC`) |
| Height | **240 pixels** (confirmed by Wireshark: `0x00F0`) |
| Color format | RGB565 (16-bit, little-endian) |
| Physical | Waveguide HUD, monocular right eye |

The app renders to a `Bitmap.Config.RGB_565` canvas at this resolution.
Partial updates are supported by setting x/y/width/height to a sub-region.

---

## 5. RLE565 Encoding

The simplest and most bandwidth-efficient codec for this hardware.

### Format

Each run = **3 bytes**:
```
[count_byte] [rgb565_lo] [rgb565_hi]
```
- `count_byte`: 1–255, how many consecutive pixels share this color
- `rgb565`: 16-bit RGB565 color in little-endian order

### RGB888 → RGB565 conversion

```python
red5   = red   >> 3          # 8-bit R → 5-bit
green6 = green >> 2          # 8-bit G → 6-bit
blue5  = blue  >> 3          # 8-bit B → 5-bit
rgb16  = (red5 << 11) | (green6 << 5) | blue5
bytes  = rgb16.to_bytes(2, 'little')
```

### Example

`ff 00 00` = 255 pixels of 0x0000 = black
`ad 00 00` = 173 pixels of 0x0000 = black
`ff 55 55` = 255 pixels of 0x5555 = teal/grey

### Pixel scan order

Left-to-right, top-to-bottom (standard raster order).

---

## 6. Python Reference Implementation (from blog)

```python
import bluetooth, binascii, time
from PIL import Image

def get_rle(inputfile):
    img = Image.open(inputfile).convert(mode="RGB", dither=Image.Dither.NONE)
    count = 0
    last = ""
    s = ""
    for y in range(240):
        for x in range(428):
            r, g, b = img.getpixel((x, y))
            rgb16 = ((r >> 3) << 11) | ((g >> 2) << 5) | (b >> 3)
            color = (0xffff & rgb16).to_bytes(2, 'little').hex()
            if last == "":
                last = color
            if (last != color) or count == 0xff:
                s += f'{count:02x}{last}'
                count = 0
            last = color
            count += 1
    return s

bd_addr = "cc:78:ab:59:6a:2b"  # replace with your glasses MAC
sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
sock.connect((bd_addr, 1))

img = get_rle("image.png")
chunk_size = 900
chunks = [img[i:i+chunk_size] for i in range(0, len(img), chunk_size)]
for i, chunk in enumerate(chunks):
    if i == 0:
        length = (len(img) // 2).to_bytes(4, 'little').hex()
        # header: magic + type(5) + reserved + length | ImageHeader: codec(2)+window+idx+total+x+y+w+h
        chunk = f'1d600500 0000{length}020000000100000000 00ac01f000' + chunk
    sock.send(binascii.unhexlify(chunk))
```

---

## 7. Flutter Implementation Plan

### Packages needed
- `flutter_bluetooth_serial` — Classic BT RFCOMM on Android
- `image` — Dart JPEG / pixel manipulation (for RLE565 encoding)

### New files to create
- `lib/core/solos_protocol.dart` — builds binary packets (magic, type, ImageHeader)
- `lib/core/rfcomm/rfcomm_service.dart` — Classic BT connection + send

### What to change
- `HudController` — on each tick: render text to pixels → RLE565 → IMAGE packet → RFCOMM send
- `GlassesApp.buildGlassesPayload()` stays as `String?` — the controller renders it to bitmap
- Replace BLE scan sheet with a Classic BT device picker (or use the MAC saved in settings)
- `AppSettings` — add `glassesMacAddress` field

### Rendering pipeline per tick
```
GlassesApp.buildGlassesPayload()  →  String (e.g. "34.2 km/h")
    ↓
HudController renders text onto 428×240 canvas (dart:ui PictureRecorder)
    ↓
Extract raw RGBA pixels → convert to RGB565 → RLE565 encode
    ↓
Build IMAGE packet (10-byte outer header + 14-byte ImageHeader + RLE bytes)
    ↓
Send over RFCOMM socket
```

### Connection flow
1. User opens scan sheet → lists paired Classic BT devices (not BLE scan)
2. User taps glasses → save MAC to settings
3. App opens RFCOMM socket to saved MAC, port 1
4. Send WAKE_UP packet (type=4, empty payload)
5. Start refresh loop

### Open questions
- Does the device need a CAPABILITIES handshake before accepting IMAGE packets?
  (blog's Python sends images directly with no handshake and it works)
- Partial screen updates: does setting x/y/width/height to a sub-region work?
  (likely yes, blog shows values like `45001200` = offset 69,18)
- iOS support: requires MFi accessory protocol or separate BLE channel investigation

---

## 8. What Doesn't Work / Open

- The `payloadLength` "divide by 2" mystery in the blog: this was a Python artifact
  (he was measuring a hex string's length, which is 2× the byte count). No actual divide-by-2.
- The original app re-sends the packet sequence twice — unclear why, possibly a keep-alive quirk.
  The blog's Python doesn't do this and still works.
- Audio: microphone and speakers are present but audio commands are not yet mapped.
  PacketTypes AUDIO_ENABLE (11), AUDIO (3), AUDIO_END (21) exist in the protocol.
- Navigation / other modes: `SET_DISPLAY_MODE (33)` likely controls full/partial refresh modes.
