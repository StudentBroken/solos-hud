package com.google.maps.android.kml;

import com.facebook.share.internal.ShareConstants;
import com.kopin.pupil.ui.PageHelper;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes69.dex */
class KmlStyleParser {
    private static final String COLOR_STYLE_COLOR = "color";
    private static final String COLOR_STYLE_MODE = "colorMode";
    private static final String ICON_STYLE_HEADING = "heading";
    private static final String ICON_STYLE_HOTSPOT = "hotSpot";
    private static final String ICON_STYLE_SCALE = "scale";
    private static final String ICON_STYLE_URL = "Icon";
    private static final String LINE_STYLE_WIDTH = "width";
    private static final String POLY_STYLE_FILL = "fill";
    private static final String POLY_STYLE_OUTLINE = "outline";
    private static final String STYLE_MAP_KEY = "key";
    private static final String STYLE_MAP_NORMAL_STYLE = "normal";
    private static final String STYLE_TAG = "styleUrl";

    KmlStyleParser() {
    }

    static KmlStyle createStyle(XmlPullParser parser) throws XmlPullParserException, IOException {
        KmlStyle styleProperties = new KmlStyle();
        setStyleId(parser.getAttributeValue(null, "id"), styleProperties);
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("Style")) {
                if (eventType == 2) {
                    if (parser.getName().equals("IconStyle")) {
                        createIconStyle(parser, styleProperties);
                    } else if (parser.getName().equals("LineStyle")) {
                        createLineStyle(parser, styleProperties);
                    } else if (parser.getName().equals("PolyStyle")) {
                        createPolyStyle(parser, styleProperties);
                    } else if (parser.getName().equals("BalloonStyle")) {
                        createBalloonStyle(parser, styleProperties);
                    }
                }
                eventType = parser.next();
            } else {
                return styleProperties;
            }
        }
    }

    private static void setStyleId(String id, KmlStyle styleProperties) {
        if (id != null) {
            String styleId = "#" + id;
            styleProperties.setStyleId(styleId);
        }
    }

    private static void createIconStyle(XmlPullParser parser, KmlStyle style) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("IconStyle")) {
                if (eventType == 2) {
                    if (parser.getName().equals(ICON_STYLE_HEADING)) {
                        style.setHeading(Float.parseFloat(parser.nextText()));
                    } else if (parser.getName().equals(ICON_STYLE_URL)) {
                        setIconUrl(parser, style);
                    } else if (parser.getName().equals(ICON_STYLE_HOTSPOT)) {
                        setIconHotSpot(parser, style);
                    } else if (parser.getName().equals(ICON_STYLE_SCALE)) {
                        style.setIconScale(Double.parseDouble(parser.nextText()));
                    } else if (parser.getName().equals("color")) {
                        style.setMarkerColor(parser.nextText());
                    } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                        style.setIconColorMode(parser.nextText());
                    }
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }

    static HashMap<String, String> createStyleMap(XmlPullParser parser) throws XmlPullParserException, IOException {
        HashMap<String, String> styleMaps = new HashMap<>();
        Boolean isNormalStyleMapValue = false;
        String styleId = "#" + parser.getAttributeValue(null, "id");
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("StyleMap")) {
                if (eventType == 2) {
                    if (parser.getName().equals(STYLE_MAP_KEY) && parser.nextText().equals(STYLE_MAP_NORMAL_STYLE)) {
                        isNormalStyleMapValue = true;
                    } else if (parser.getName().equals(STYLE_TAG) && isNormalStyleMapValue.booleanValue()) {
                        styleMaps.put(styleId, parser.nextText());
                        isNormalStyleMapValue = false;
                    }
                }
                eventType = parser.next();
            } else {
                return styleMaps;
            }
        }
    }

    private static void createBalloonStyle(XmlPullParser parser, KmlStyle style) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("BalloonStyle")) {
                if (eventType == 2 && parser.getName().equals(PageHelper.TEXT_PART_TAG)) {
                    style.setInfoWindowText(parser.nextText());
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }

    private static void setIconUrl(XmlPullParser parser, KmlStyle style) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(ICON_STYLE_URL)) {
                if (eventType == 2 && parser.getName().equals(ShareConstants.WEB_DIALOG_PARAM_HREF)) {
                    style.setIconUrl(parser.nextText());
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }

    private static void setIconHotSpot(XmlPullParser parser, KmlStyle style) {
        Float xValue = Float.valueOf(Float.parseFloat(parser.getAttributeValue(null, "x")));
        Float yValue = Float.valueOf(Float.parseFloat(parser.getAttributeValue(null, "y")));
        String xUnits = parser.getAttributeValue(null, "xunits");
        String yUnits = parser.getAttributeValue(null, "yunits");
        style.setHotSpot(xValue.floatValue(), yValue.floatValue(), xUnits, yUnits);
    }

    private static void createLineStyle(XmlPullParser parser, KmlStyle style) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("LineStyle")) {
                if (eventType == 2) {
                    if (parser.getName().equals("color")) {
                        style.setOutlineColor(parser.nextText());
                    } else if (parser.getName().equals("width")) {
                        style.setWidth(Float.valueOf(parser.nextText()));
                    } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                        style.setLineColorMode(parser.nextText());
                    }
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }

    private static void createPolyStyle(XmlPullParser parser, KmlStyle style) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("PolyStyle")) {
                if (eventType == 2) {
                    if (parser.getName().equals("color")) {
                        style.setFillColor(parser.nextText());
                    } else if (parser.getName().equals(POLY_STYLE_OUTLINE)) {
                        style.setOutline(KmlBoolean.parseBoolean(parser.nextText()));
                    } else if (parser.getName().equals(POLY_STYLE_FILL)) {
                        style.setFill(KmlBoolean.parseBoolean(parser.nextText()));
                    } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                        style.setPolyColorMode(parser.nextText());
                    }
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }
}
