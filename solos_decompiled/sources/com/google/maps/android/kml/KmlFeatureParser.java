package com.google.maps.android.kml;

import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes69.dex */
class KmlFeatureParser {
    private static final String BOUNDARY_REGEX = "outerBoundaryIs|innerBoundaryIs";
    private static final String COMPASS_REGEX = "north|south|east|west";
    private static final String EXTENDED_DATA = "ExtendedData";
    private static final String GEOMETRY_REGEX = "Point|LineString|Polygon|MultiGeometry";
    private static final int LATITUDE_INDEX = 1;
    private static final int LONGITUDE_INDEX = 0;
    private static final String PROPERTY_REGEX = "name|description|drawOrder|visibility|open|address|phoneNumber";
    private static final String STYLE_TAG = "Style";
    private static final String STYLE_URL_TAG = "styleUrl";

    KmlFeatureParser() {
    }

    static KmlPlacemark createPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {
        String styleId = null;
        KmlStyle inlineStyle = null;
        HashMap<String, String> properties = new HashMap<>();
        KmlGeometry geometry = null;
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("Placemark")) {
                if (eventType == 2) {
                    if (parser.getName().equals(STYLE_URL_TAG)) {
                        styleId = parser.nextText();
                    } else if (parser.getName().matches(GEOMETRY_REGEX)) {
                        geometry = createGeometry(parser, parser.getName());
                    } else if (parser.getName().matches(PROPERTY_REGEX)) {
                        properties.put(parser.getName(), parser.nextText());
                    } else if (parser.getName().equals(EXTENDED_DATA)) {
                        properties.putAll(setExtendedDataProperties(parser));
                    } else if (parser.getName().equals(STYLE_TAG)) {
                        inlineStyle = KmlStyleParser.createStyle(parser);
                    }
                }
                eventType = parser.next();
            } else {
                return new KmlPlacemark(geometry, styleId, inlineStyle, properties);
            }
        }
    }

    static KmlGroundOverlay createGroundOverlay(XmlPullParser parser) throws XmlPullParserException, IOException {
        float drawOrder = 0.0f;
        float rotation = 0.0f;
        int visibility = 1;
        String imageUrl = null;
        HashMap<String, String> properties = new HashMap<>();
        HashMap<String, Double> compassPoints = new HashMap<>();
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("GroundOverlay")) {
                if (eventType == 2) {
                    if (parser.getName().equals("Icon")) {
                        imageUrl = getImageUrl(parser);
                    } else if (parser.getName().equals("drawOrder")) {
                        drawOrder = Float.parseFloat(parser.nextText());
                    } else if (parser.getName().equals("visibility")) {
                        visibility = Integer.parseInt(parser.nextText());
                    } else if (parser.getName().equals(EXTENDED_DATA)) {
                        properties.putAll(setExtendedDataProperties(parser));
                    } else if (parser.getName().equals("rotation")) {
                        rotation = getRotation(parser);
                    } else if (parser.getName().matches(PROPERTY_REGEX) || parser.getName().equals("color")) {
                        properties.put(parser.getName(), parser.nextText());
                    } else if (parser.getName().matches(COMPASS_REGEX)) {
                        compassPoints.put(parser.getName(), Double.valueOf(Double.parseDouble(parser.nextText())));
                    }
                }
                eventType = parser.next();
            } else {
                LatLngBounds latLonBox = createLatLngBounds(compassPoints.get("north"), compassPoints.get("south"), compassPoints.get("east"), compassPoints.get("west"));
                return new KmlGroundOverlay(imageUrl, latLonBox, drawOrder, visibility, properties, rotation);
            }
        }
    }

    private static float getRotation(XmlPullParser parser) throws XmlPullParserException, IOException {
        return -Float.parseFloat(parser.nextText());
    }

    private static String getImageUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("Icon")) {
                if (eventType == 2 && parser.getName().equals(ShareConstants.WEB_DIALOG_PARAM_HREF)) {
                    return parser.nextText();
                }
                eventType = parser.next();
            } else {
                return null;
            }
        }
    }

    private static KmlGeometry createGeometry(XmlPullParser parser, String geometryType) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(geometryType)) {
                if (eventType == 2) {
                    if (parser.getName().equals(KmlPoint.GEOMETRY_TYPE)) {
                        return createPoint(parser);
                    }
                    if (parser.getName().equals(KmlLineString.GEOMETRY_TYPE)) {
                        return createLineString(parser);
                    }
                    if (parser.getName().equals(KmlPolygon.GEOMETRY_TYPE)) {
                        return createPolygon(parser);
                    }
                    if (parser.getName().equals("MultiGeometry")) {
                        return createMultiGeometry(parser);
                    }
                }
                eventType = parser.next();
            } else {
                return null;
            }
        }
    }

    private static HashMap<String, String> setExtendedDataProperties(XmlPullParser parser) throws XmlPullParserException, IOException {
        HashMap<String, String> properties = new HashMap<>();
        String propertyKey = null;
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(EXTENDED_DATA)) {
                if (eventType == 2) {
                    if (parser.getName().equals("Data")) {
                        propertyKey = parser.getAttributeValue(null, "name");
                    } else if (parser.getName().equals(FirebaseAnalytics.Param.VALUE) && propertyKey != null) {
                        properties.put(propertyKey, parser.nextText());
                        propertyKey = null;
                    }
                }
                eventType = parser.next();
            } else {
                return properties;
            }
        }
    }

    private static KmlPoint createPoint(XmlPullParser parser) throws XmlPullParserException, IOException {
        LatLng coordinate = null;
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(KmlPoint.GEOMETRY_TYPE)) {
                if (eventType == 2 && parser.getName().equals("coordinates")) {
                    coordinate = convertToLatLng(parser.nextText());
                }
                eventType = parser.next();
            } else {
                return new KmlPoint(coordinate);
            }
        }
    }

    private static KmlLineString createLineString(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<LatLng> coordinates = new ArrayList<>();
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(KmlLineString.GEOMETRY_TYPE)) {
                if (eventType == 2 && parser.getName().equals("coordinates")) {
                    coordinates = convertToLatLngArray(parser.nextText());
                }
                eventType = parser.next();
            } else {
                return new KmlLineString(coordinates);
            }
        }
    }

    private static KmlPolygon createPolygon(XmlPullParser parser) throws XmlPullParserException, IOException {
        Boolean isOuterBoundary = false;
        ArrayList<LatLng> outerBoundary = new ArrayList<>();
        ArrayList<ArrayList<LatLng>> innerBoundaries = new ArrayList<>();
        int eventType = parser.getEventType();
        while (true) {
            if (eventType != 3 || !parser.getName().equals(KmlPolygon.GEOMETRY_TYPE)) {
                if (eventType == 2) {
                    if (parser.getName().matches(BOUNDARY_REGEX)) {
                        isOuterBoundary = Boolean.valueOf(parser.getName().equals("outerBoundaryIs"));
                    } else if (parser.getName().equals("coordinates")) {
                        if (isOuterBoundary.booleanValue()) {
                            outerBoundary = convertToLatLngArray(parser.nextText());
                        } else {
                            innerBoundaries.add(convertToLatLngArray(parser.nextText()));
                        }
                    }
                }
                eventType = parser.next();
            } else {
                return new KmlPolygon(outerBoundary, innerBoundaries);
            }
        }
    }

    private static KmlMultiGeometry createMultiGeometry(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<KmlGeometry> geometries = new ArrayList<>();
        int eventType = parser.next();
        while (true) {
            if (eventType != 3 || !parser.getName().equals("MultiGeometry")) {
                if (eventType == 2 && parser.getName().matches(GEOMETRY_REGEX)) {
                    geometries.add(createGeometry(parser, parser.getName()));
                }
                eventType = parser.next();
            } else {
                return new KmlMultiGeometry(geometries);
            }
        }
    }

    private static ArrayList<LatLng> convertToLatLngArray(String coordinatesString) {
        ArrayList<LatLng> coordinatesArray = new ArrayList<>();
        String[] coordinates = coordinatesString.trim().split("(\\s+)");
        for (String coordinate : coordinates) {
            coordinatesArray.add(convertToLatLng(coordinate));
        }
        return coordinatesArray;
    }

    private static LatLng convertToLatLng(String coordinateString) {
        String[] coordinate = coordinateString.split(",");
        Double lat = Double.valueOf(Double.parseDouble(coordinate[1]));
        Double lon = Double.valueOf(Double.parseDouble(coordinate[0]));
        return new LatLng(lat.doubleValue(), lon.doubleValue());
    }

    private static LatLngBounds createLatLngBounds(Double north, Double south, Double east, Double west) {
        LatLng southWest = new LatLng(south.doubleValue(), west.doubleValue());
        LatLng northEast = new LatLng(north.doubleValue(), east.doubleValue());
        return new LatLngBounds(southWest, northEast);
    }
}
