import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import '../solos_protocol.dart';
import '../render_utils.dart';

// ── Palette ───────────────────────────────────────────────────────────────────
const _bg = Color(0xFF080C14);
const _headerBg = Color(0xFF0D1420);
const _selBg = Color(0xFF001A2E);
const _selBorder = Color(0xFF00CCFF);
const _selText = Color(0xFFFFFFFF);
const _selIcon = Color(0xFF00CCFF);
const _unselText = Color(0xFF6688AA);
const _unselIcon = Color(0xFF3A5570);
const _activeDot = Color(0xFF00FF88);
const _divider = Color(0xFF111C28);
const _footerText = Color(0xFF334455);
const _scrollHint = Color(0xFF224466);
const _titleColor = Color(0xFF00CCFF);

// ── Data model ────────────────────────────────────────────────────────────────

class MenuAppItem {
  final String name;
  final int iconCodePoint;
  final bool isActive;
  final String? subtitle; // optional one-line status

  const MenuAppItem({
    required this.name,
    required this.iconCodePoint,
    this.isActive = false,
    this.subtitle,
  });
}

// ── Renderer ──────────────────────────────────────────────────────────────────

class GlassesMenuRenderer {
  static const int _dw = SolosProtocol.displayWidth; // 428
  static const int _dh = SolosProtocol.displayHeight; // 240

  // Layout constants
  static const double _headerH = 30;
  static const double _prevH = 54;
  static const double _selH = 80;
  static const double _nextH = 54;
  static const double _footerH = 22;

  static const double _prevY = _headerH + 1; // 31
  static const double _selY = _prevY + _prevH + 2; // 87
  static const double _nextY = _selY + _selH + 2; // 169
  static const double _footY = _dh - _footerH; // 218

  /// Render → RLE565 bytes
  static Future<Uint8List> render({
    required List<MenuAppItem> items,
    required int selectedIndex,
    String? batteryLabel, // e.g. "82%" or "82% ⚡"
    int? batteryLevel, // 0-100 for color coding
  }) async {
    final rec = ui.PictureRecorder();
    final canvas = ui.Canvas(
      rec,
      Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()),
    );

    _drawBackground(canvas);
    _drawHeader(
      canvas,
      selectedIndex,
      items.length,
      batteryLabel: batteryLabel,
      batteryLevel: batteryLevel,
    );
    _drawItems(canvas, items, selectedIndex);
    _drawFooter(canvas, selectedIndex, items.length);

    final picture = rec.endRecording();
    final image = await picture.toImage(_dw, _dh);
    final bd = await image.toByteData(format: ui.ImageByteFormat.rawRgba);
    return encodeRLE565Async(bd!.buffer.asUint8List(), _dw, _dh);
  }

  // ── Sections ─────────────────────────────────────────────────────────────

  static void _drawBackground(ui.Canvas c) {
    c.drawRect(Rect.fromLTWH(0, 0, _dw.toDouble(), _dh.toDouble()), _fill(_bg));
  }

  static void _drawHeader(
    ui.Canvas c,
    int sel,
    int total, {
    String? batteryLabel,
    int? batteryLevel,
  }) {
    // Header background
    c.drawRect(Rect.fromLTWH(0, 0, _dw.toDouble(), _headerH), _fill(_headerBg));
    c.drawRect(Rect.fromLTWH(0, _headerH, _dw.toDouble(), 1), _fill(_divider));

    // Title
    _text(
      c,
      'APPS',
      x: 14,
      y: 6,
      size: 16,
      color: _titleColor,
      bold: true,
      align: ui.TextAlign.left,
      maxW: 120,
    );

    // Position counter  e.g. "3 / 5"
    _text(
      c,
      '${sel + 1} / $total',
      x: _dw / 2 - 30,
      y: 7,
      size: 14,
      color: _unselText,
      align: ui.TextAlign.left,
      maxW: 60,
    );

    // ── Battery (top-right) ──────────────────────────────────────────────────
    if (batteryLabel != null) {
      final Color batColor = batteryLevel == null
          ? _unselText
          : batteryLevel > 50
          ? const Color(0xFF00CC66) // green
          : batteryLevel > 20
          ? const Color(0xFFFFAA00) // amber
          : const Color(0xFFFF3333); // red
      // Battery icon
      _icon(
        c,
        0xe1a4 /* battery_full codepoint */,
        x: _dw - 92,
        y: 4,
        size: 20,
        color: batColor,
      );
      _text(
        c,
        batteryLabel,
        x: _dw - 68.0,
        y: 7,
        size: 14,
        color: batColor,
        align: ui.TextAlign.left,
        maxW: 62,
      );
    }

    // Scroll hint chevrons — only drawn when no battery, to avoid overlap
    if (batteryLabel == null) {
      if (sel > 0) {
        _text(
          c,
          '▲ front',
          x: _dw - 90.0,
          y: 3,
          size: 11,
          color: _scrollHint,
          align: ui.TextAlign.left,
          maxW: 80,
        );
      }
      if (sel < total - 1) {
        _text(
          c,
          '▼ back',
          x: _dw - 90.0,
          y: 16,
          size: 11,
          color: _scrollHint,
          align: ui.TextAlign.left,
          maxW: 80,
        );
      }
    }
  }

  static void _drawItems(ui.Canvas c, List<MenuAppItem> items, int sel) {
    final hasPrev = sel > 0;
    final hasNext = sel < items.length - 1;

    // Previous item (above selected)
    if (hasPrev) {
      _drawUnselectedItem(c, items[sel - 1], _prevY, _prevH, dimmer: true);
    } else {
      // Faint empty slot to maintain layout stability
      c.drawRect(
        Rect.fromLTWH(8, _prevY, _dw - 16.0, _prevH),
        _fill(const Color(0xFF050910)),
      );
    }

    // Selected item
    _drawSelectedItem(c, items[sel], _selY, _selH);

    // Next item (below selected)
    if (hasNext) {
      _drawUnselectedItem(c, items[sel + 1], _nextY, _nextH, dimmer: false);
    } else {
      c.drawRect(
        Rect.fromLTWH(8, _nextY, _dw - 16.0, _nextH),
        _fill(const Color(0xFF050910)),
      );
    }

    // More-items indicators (off-screen arrows, blurred)
    if (sel > 1) {
      _text(
        c,
        '···',
        x: _dw / 2 - 15,
        y: _prevY - 14,
        size: 12,
        color: _scrollHint,
        align: ui.TextAlign.left,
        maxW: 30,
      );
    }
    if (sel < items.length - 2) {
      _text(
        c,
        '···',
        x: _dw / 2 - 15,
        y: _nextY + _nextH + 2,
        size: 12,
        color: _scrollHint,
        align: ui.TextAlign.left,
        maxW: 30,
      );
    }
  }

  static void _drawSelectedItem(
    ui.Canvas c,
    MenuAppItem item,
    double y,
    double h,
  ) {
    const pad = 10.0;

    // Background fill
    c.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromLTWH(pad, y, _dw - pad * 2, h),
        const Radius.circular(8),
      ),
      _fill(_selBg),
    );
    // Glowing cyan border
    c.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromLTWH(pad, y, _dw - pad * 2, h),
        const Radius.circular(8),
      ),
      ui.Paint()
        ..color = _selBorder
        ..style = ui.PaintingStyle.stroke
        ..strokeWidth = 2,
    );
    // Left accent bar
    c.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromLTWH(pad, y + 10, 3, h - 20),
        const Radius.circular(2),
      ),
      _fill(_selBorder),
    );

    // Icon
    _icon(
      c,
      item.iconCodePoint,
      x: pad + 12,
      y: y + h / 2 - 16,
      size: 30,
      color: _selIcon,
    );

    // App name
    _text(
      c,
      item.name,
      x: pad + 52,
      y: y + 12,
      size: 26,
      color: _selText,
      bold: true,
      align: ui.TextAlign.left,
      maxW: _dw - pad * 2 - 52 - 90,
    );

    // Subtitle / status
    if (item.subtitle != null) {
      _text(
        c,
        item.subtitle!,
        x: pad + 52,
        y: y + 44,
        size: 14,
        color: const Color(0xFF5588AA),
        align: ui.TextAlign.left,
        maxW: _dw - pad * 2 - 52 - 90,
      );
    }

    // Active indicator (right side)
    if (item.isActive) {
      // Green dot
      c.drawCircle(
        Offset(_dw - pad - 16, y + h / 2 - 10),
        5,
        ui.Paint()
          ..color = _activeDot
          ..style = ui.PaintingStyle.fill,
      );
      _text(
        c,
        'ACTIVE',
        x: _dw - pad - 68.0,
        y: y + h / 2 - 9,
        size: 12,
        color: _activeDot,
        align: ui.TextAlign.left,
        maxW: 54,
      );
    } else {
      // "LAUNCH" hint
      _text(
        c,
        '● LAUNCH',
        x: _dw - pad - 80.0,
        y: y + h / 2 - 9,
        size: 12,
        color: const Color(0xFF224455),
        align: ui.TextAlign.left,
        maxW: 72,
      );
    }
  }

  static void _drawUnselectedItem(
    ui.Canvas c,
    MenuAppItem item,
    double y,
    double h, {
    required bool dimmer,
  }) {
    const pad = 14.0;
    final textColor = dimmer ? const Color(0xFF3A5060) : _unselText;
    final iconColor = dimmer ? const Color(0xFF2A3A48) : _unselIcon;

    _icon(
      c,
      item.iconCodePoint,
      x: pad + 4,
      y: y + h / 2 - 13,
      size: 24,
      color: iconColor,
    );

    _text(
      c,
      item.name,
      x: pad + 36,
      y: y + h / 2 - 11,
      size: 20,
      color: textColor,
      align: ui.TextAlign.left,
      maxW: _dw - pad - 120,
    );

    if (item.isActive) {
      c.drawCircle(
        Offset(_dw - pad - 10, y + h / 2),
        4,
        ui.Paint()
          ..color = _activeDot.withAlpha(180)
          ..style = ui.PaintingStyle.fill,
      );
    }
  }

  static void _drawFooter(ui.Canvas c, int sel, int total) {
    c.drawRect(Rect.fromLTWH(0, _footY, _dw.toDouble(), 1), _fill(_divider));
    c.drawRect(
      Rect.fromLTWH(0, _footY, _dw.toDouble(), _footerH),
      _fill(_headerBg),
    );
    _text(
      c,
      '▲ prev   ▼ next   ● select   hold main = close',
      x: 12,
      y: _footY + 4,
      size: 12,
      color: _footerText,
      align: ui.TextAlign.left,
      maxW: _dw - 16.0,
    );
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  static void _icon(
    ui.Canvas c,
    int codePoint, {
    required double x,
    required double y,
    required double size,
    required Color color,
  }) {
    final builder =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              fontSize: size,
              fontFamily: 'MaterialIcons',
              maxLines: 1,
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontFamily: 'MaterialIcons',
            ),
          )
          ..addText(String.fromCharCode(codePoint));
    final para = builder.build()
      ..layout(ui.ParagraphConstraints(width: size + 4));
    c.drawParagraph(para, Offset(x, y));
  }

  static void _text(
    ui.Canvas c,
    String text, {
    required double x,
    required double y,
    required double size,
    required Color color,
    bool bold = false,
    required ui.TextAlign align,
    required double maxW,
  }) {
    final builder =
        ui.ParagraphBuilder(
            ui.ParagraphStyle(
              textAlign: align,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
              maxLines: 1,
              ellipsis: '…',
            ),
          )
          ..pushStyle(
            ui.TextStyle(
              color: color,
              fontSize: size,
              fontWeight: bold ? ui.FontWeight.w700 : ui.FontWeight.w400,
            ),
          )
          ..addText(text);
    final para = builder.build()..layout(ui.ParagraphConstraints(width: maxW));
    c.drawParagraph(para, Offset(x, y));
  }

  static ui.Paint _fill(Color color) => ui.Paint()
    ..color = color
    ..style = ui.PaintingStyle.fill;
}
