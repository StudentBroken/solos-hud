package com.google.maps.android.kml;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes69.dex */
class KmlContainerParser {
    private static final String CONTAINER_REGEX = "Folder|Document";
    private static final String EXTENDED_DATA = "ExtendedData";
    private static final String GROUND_OVERLAY = "GroundOverlay";
    private static final String PLACEMARK = "Placemark";
    private static final String PROPERTY_REGEX = "name|description|visibility|open|address|phoneNumber";
    private static final String STYLE = "Style";
    private static final String STYLE_MAP = "StyleMap";
    private static final String UNSUPPORTED_REGEX = "altitude|altitudeModeGroup|altitudeMode|begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|maxWidth|near|overlayXY|range|refreshMode|refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|viewRefreshTime|when";

    KmlContainerParser() {
    }

    static KmlContainer createContainer(XmlPullParser parser) throws XmlPullParserException, IOException {
        return assignPropertiesToContainer(parser);
    }

    private static KmlContainer assignPropertiesToContainer(XmlPullParser parser) throws XmlPullParserException, IOException {
        String startTag = parser.getName();
        String containerId = null;
        HashMap<String, String> containerProperties = new HashMap<>();
        HashMap<String, KmlStyle> containerStyles = new HashMap<>();
        HashMap<KmlPlacemark, Object> containerPlacemarks = new HashMap<>();
        ArrayList<KmlContainer> nestedContainers = new ArrayList<>();
        HashMap<String, String> containerStyleMaps = new HashMap<>();
        HashMap<KmlGroundOverlay, GroundOverlay> containerGroundOverlays = new HashMap<>();
        if (parser.getAttributeValue(null, "id") != null) {
            containerId = parser.getAttributeValue(null, "id");
        }
        parser.next();
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(startTag)) {
                if (eventType == 2) {
                    if (parser.getName().matches(UNSUPPORTED_REGEX)) {
                        KmlParser.skip(parser);
                    } else if (parser.getName().matches(CONTAINER_REGEX)) {
                        nestedContainers.add(assignPropertiesToContainer(parser));
                    } else if (parser.getName().matches(PROPERTY_REGEX)) {
                        containerProperties.put(parser.getName(), parser.nextText());
                    } else if (parser.getName().equals(STYLE_MAP)) {
                        setContainerStyleMap(parser, containerStyleMaps);
                    } else if (parser.getName().equals(STYLE)) {
                        setContainerStyle(parser, containerStyles);
                    } else if (parser.getName().equals(PLACEMARK)) {
                        setContainerPlacemark(parser, containerPlacemarks);
                    } else if (parser.getName().equals(EXTENDED_DATA)) {
                        setExtendedDataProperties(parser, containerProperties);
                    } else if (parser.getName().equals(GROUND_OVERLAY)) {
                        containerGroundOverlays.put(KmlFeatureParser.createGroundOverlay(parser), null);
                    }
                }
                eventType = parser.next();
            } else {
                return new KmlContainer(containerProperties, containerStyles, containerPlacemarks, containerStyleMaps, nestedContainers, containerGroundOverlays, containerId);
            }
        }
    }

    private static void setContainerStyleMap(XmlPullParser parser, HashMap<String, String> containerStyleMap) throws XmlPullParserException, IOException {
        containerStyleMap.putAll(KmlStyleParser.createStyleMap(parser));
    }

    private static void setExtendedDataProperties(XmlPullParser parser, HashMap<String, String> mContainerProperties) throws XmlPullParserException, IOException {
        String propertyKey = null;
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(EXTENDED_DATA)) {
                if (eventType == 2) {
                    if (parser.getName().equals("Data")) {
                        propertyKey = parser.getAttributeValue(null, "name");
                    } else if (parser.getName().equals(FirebaseAnalytics.Param.VALUE) && propertyKey != null) {
                        mContainerProperties.put(propertyKey, parser.nextText());
                        propertyKey = null;
                    }
                }
                eventType = parser.next();
            } else {
                return;
            }
        }
    }

    private static void setContainerStyle(XmlPullParser parser, HashMap<String, KmlStyle> containerStyles) throws XmlPullParserException, IOException {
        if (parser.getAttributeValue(null, "id") != null) {
            KmlStyle style = KmlStyleParser.createStyle(parser);
            String styleId = style.getStyleId();
            containerStyles.put(styleId, style);
        }
    }

    private static void setContainerPlacemark(XmlPullParser parser, HashMap<KmlPlacemark, Object> containerPlacemarks) throws XmlPullParserException, IOException {
        containerPlacemarks.put(KmlFeatureParser.createPlacemark(parser), null);
    }
}
