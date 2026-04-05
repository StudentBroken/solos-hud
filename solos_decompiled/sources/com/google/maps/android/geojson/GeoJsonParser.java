package com.google.maps.android.geojson;

import android.util.Log;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes69.dex */
class GeoJsonParser {
    private static final String BOUNDING_BOX = "bbox";
    private static final String FEATURE = "Feature";
    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURE_COLLECTION_ARRAY = "features";
    private static final String FEATURE_GEOMETRY = "geometry";
    private static final String FEATURE_ID = "id";
    private static final String GEOMETRY_COLLECTION = "GeometryCollection";
    private static final String GEOMETRY_COLLECTION_ARRAY = "geometries";
    private static final String GEOMETRY_COORDINATES_ARRAY = "coordinates";
    private static final String LINESTRING = "LineString";
    private static final String LOG_TAG = "GeoJsonParser";
    private static final String MULTILINESTRING = "MultiLineString";
    private static final String MULTIPOINT = "MultiPoint";
    private static final String MULTIPOLYGON = "MultiPolygon";
    private static final String POINT = "Point";
    private static final String POLYGON = "Polygon";
    private static final String PROPERTIES = "properties";
    private final JSONObject mGeoJsonFile;
    private final ArrayList<GeoJsonFeature> mGeoJsonFeatures = new ArrayList<>();
    private LatLngBounds mBoundingBox = null;

    GeoJsonParser(JSONObject geoJsonFile) {
        this.mGeoJsonFile = geoJsonFile;
        parseGeoJson();
    }

    private static boolean isGeometry(String type) {
        return type.matches("Point|MultiPoint|LineString|MultiLineString|Polygon|MultiPolygon|GeometryCollection");
    }

    private static GeoJsonFeature parseFeature(JSONObject geoJsonFeature) {
        String id = null;
        LatLngBounds boundingBox = null;
        GeoJsonGeometry geometry = null;
        HashMap<String, String> properties = new HashMap<>();
        try {
            if (geoJsonFeature.has("id")) {
                id = geoJsonFeature.getString("id");
            }
            if (geoJsonFeature.has(BOUNDING_BOX)) {
                boundingBox = parseBoundingBox(geoJsonFeature.getJSONArray(BOUNDING_BOX));
            }
            if (geoJsonFeature.has(FEATURE_GEOMETRY) && !geoJsonFeature.isNull(FEATURE_GEOMETRY)) {
                geometry = parseGeometry(geoJsonFeature.getJSONObject(FEATURE_GEOMETRY));
            }
            if (geoJsonFeature.has(PROPERTIES) && !geoJsonFeature.isNull(PROPERTIES)) {
                properties = parseProperties(geoJsonFeature.getJSONObject(PROPERTIES));
            }
            return new GeoJsonFeature(geometry, id, properties, boundingBox);
        } catch (JSONException e) {
            Log.w(LOG_TAG, "Feature could not be successfully parsed " + geoJsonFeature.toString());
            return null;
        }
    }

    private static LatLngBounds parseBoundingBox(JSONArray coordinates) throws JSONException {
        LatLng southWestCorner = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
        LatLng northEastCorner = new LatLng(coordinates.getDouble(3), coordinates.getDouble(2));
        return new LatLngBounds(southWestCorner, northEastCorner);
    }

    private static GeoJsonGeometry parseGeometry(JSONObject geoJsonGeometry) {
        JSONArray geometryArray;
        GeoJsonGeometry geoJsonGeometryCreateGeometry = null;
        try {
            String geometryType = geoJsonGeometry.getString(ShareConstants.MEDIA_TYPE);
            if (geometryType.equals(GEOMETRY_COLLECTION)) {
                geometryArray = geoJsonGeometry.getJSONArray(GEOMETRY_COLLECTION_ARRAY);
            } else {
                if (!isGeometry(geometryType)) {
                    return null;
                }
                geometryArray = geoJsonGeometry.getJSONArray(GEOMETRY_COORDINATES_ARRAY);
            }
            geoJsonGeometryCreateGeometry = createGeometry(geometryType, geometryArray);
            return geoJsonGeometryCreateGeometry;
        } catch (JSONException e) {
            return geoJsonGeometryCreateGeometry;
        }
    }

    private static GeoJsonFeature parseGeometryToFeature(JSONObject geoJsonGeometry) {
        GeoJsonGeometry geometry = parseGeometry(geoJsonGeometry);
        if (geometry != null) {
            return new GeoJsonFeature(geometry, null, new HashMap(), null);
        }
        Log.w(LOG_TAG, "Geometry could not be parsed");
        return null;
    }

    private static HashMap<String, String> parseProperties(JSONObject properties) throws JSONException {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Iterator<String> itKeys = properties.keys();
        while (itKeys.hasNext()) {
            String key = itKeys.next();
            propertiesMap.put(key, properties.isNull(key) ? null : properties.getString(key));
        }
        return propertiesMap;
    }

    private static GeoJsonGeometry createGeometry(String geometryType, JSONArray geometryArray) throws JSONException {
        if (geometryType.equals("Point")) {
            return createPoint(geometryArray);
        }
        if (geometryType.equals(MULTIPOINT)) {
            return createMultiPoint(geometryArray);
        }
        if (geometryType.equals("LineString")) {
            return createLineString(geometryArray);
        }
        if (geometryType.equals(MULTILINESTRING)) {
            return createMultiLineString(geometryArray);
        }
        if (geometryType.equals("Polygon")) {
            return createPolygon(geometryArray);
        }
        if (geometryType.equals(MULTIPOLYGON)) {
            return createMultiPolygon(geometryArray);
        }
        if (geometryType.equals(GEOMETRY_COLLECTION)) {
            return createGeometryCollection(geometryArray);
        }
        return null;
    }

    private static GeoJsonPoint createPoint(JSONArray coordinates) throws JSONException {
        return new GeoJsonPoint(parseCoordinate(coordinates));
    }

    private static GeoJsonMultiPoint createMultiPoint(JSONArray coordinates) throws JSONException {
        ArrayList<GeoJsonPoint> geoJsonPoints = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            geoJsonPoints.add(createPoint(coordinates.getJSONArray(i)));
        }
        return new GeoJsonMultiPoint(geoJsonPoints);
    }

    private static GeoJsonLineString createLineString(JSONArray coordinates) throws JSONException {
        return new GeoJsonLineString(parseCoordinatesArray(coordinates));
    }

    private static GeoJsonMultiLineString createMultiLineString(JSONArray coordinates) throws JSONException {
        ArrayList<GeoJsonLineString> geoJsonLineStrings = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            geoJsonLineStrings.add(createLineString(coordinates.getJSONArray(i)));
        }
        return new GeoJsonMultiLineString(geoJsonLineStrings);
    }

    private static GeoJsonPolygon createPolygon(JSONArray coordinates) throws JSONException {
        return new GeoJsonPolygon(parseCoordinatesArrays(coordinates));
    }

    private static GeoJsonMultiPolygon createMultiPolygon(JSONArray coordinates) throws JSONException {
        ArrayList<GeoJsonPolygon> geoJsonPolygons = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            geoJsonPolygons.add(createPolygon(coordinates.getJSONArray(i)));
        }
        return new GeoJsonMultiPolygon(geoJsonPolygons);
    }

    private static GeoJsonGeometryCollection createGeometryCollection(JSONArray geometries) throws JSONException {
        ArrayList<GeoJsonGeometry> geometryCollectionElements = new ArrayList<>();
        for (int i = 0; i < geometries.length(); i++) {
            JSONObject geometryElement = geometries.getJSONObject(i);
            GeoJsonGeometry geometry = parseGeometry(geometryElement);
            if (geometry != null) {
                geometryCollectionElements.add(geometry);
            }
        }
        return new GeoJsonGeometryCollection(geometryCollectionElements);
    }

    private static LatLng parseCoordinate(JSONArray coordinates) throws JSONException {
        return new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
    }

    private static ArrayList<LatLng> parseCoordinatesArray(JSONArray coordinates) throws JSONException {
        ArrayList<LatLng> coordinatesArray = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            coordinatesArray.add(parseCoordinate(coordinates.getJSONArray(i)));
        }
        return coordinatesArray;
    }

    private static ArrayList<ArrayList<LatLng>> parseCoordinatesArrays(JSONArray coordinates) throws JSONException {
        ArrayList<ArrayList<LatLng>> coordinatesArray = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            coordinatesArray.add(parseCoordinatesArray(coordinates.getJSONArray(i)));
        }
        return coordinatesArray;
    }

    private void parseGeoJson() {
        try {
            String type = this.mGeoJsonFile.getString(ShareConstants.MEDIA_TYPE);
            if (type.equals(FEATURE)) {
                GeoJsonFeature feature = parseFeature(this.mGeoJsonFile);
                if (feature != null) {
                    this.mGeoJsonFeatures.add(feature);
                }
            } else if (type.equals(FEATURE_COLLECTION)) {
                this.mGeoJsonFeatures.addAll(parseFeatureCollection(this.mGeoJsonFile));
            } else if (isGeometry(type)) {
                GeoJsonFeature feature2 = parseGeometryToFeature(this.mGeoJsonFile);
                if (feature2 != null) {
                    this.mGeoJsonFeatures.add(feature2);
                }
            } else {
                Log.w(LOG_TAG, "GeoJSON file could not be parsed.");
            }
        } catch (JSONException e) {
            Log.w(LOG_TAG, "GeoJSON file could not be parsed.");
        }
    }

    private ArrayList<GeoJsonFeature> parseFeatureCollection(JSONObject geoJsonFeatureCollection) {
        ArrayList<GeoJsonFeature> features = new ArrayList<>();
        try {
            JSONArray geoJsonFeatures = geoJsonFeatureCollection.getJSONArray("features");
            if (geoJsonFeatureCollection.has(BOUNDING_BOX)) {
                this.mBoundingBox = parseBoundingBox(geoJsonFeatureCollection.getJSONArray(BOUNDING_BOX));
            }
            for (int i = 0; i < geoJsonFeatures.length(); i++) {
                try {
                    JSONObject feature = geoJsonFeatures.getJSONObject(i);
                    if (feature.getString(ShareConstants.MEDIA_TYPE).equals(FEATURE)) {
                        GeoJsonFeature parsedFeature = parseFeature(feature);
                        if (parsedFeature != null) {
                            features.add(parsedFeature);
                        } else {
                            Log.w(LOG_TAG, "Index of Feature in Feature Collection that could not be created: " + i);
                        }
                    }
                } catch (JSONException e) {
                    Log.w(LOG_TAG, "Index of Feature in Feature Collection that could not be created: " + i);
                }
            }
        } catch (JSONException e2) {
            Log.w(LOG_TAG, "Feature Collection could not be created.");
        }
        return features;
    }

    ArrayList<GeoJsonFeature> getFeatures() {
        return this.mGeoJsonFeatures;
    }

    LatLngBounds getBoundingBox() {
        return this.mBoundingBox;
    }
}
