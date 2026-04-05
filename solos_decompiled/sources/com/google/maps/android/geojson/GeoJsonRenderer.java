package com.google.maps.android.geojson;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.kml.KmlLineString;
import com.google.maps.android.kml.KmlPoint;
import com.google.maps.android.kml.KmlPolygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/* JADX INFO: loaded from: classes69.dex */
class GeoJsonRenderer implements Observer {
    private static final Object FEATURE_NOT_ON_MAP = null;
    private static final int POLYGON_INNER_COORDINATE_INDEX = 1;
    private static final int POLYGON_OUTER_COORDINATE_INDEX = 0;
    private final GeoJsonLineStringStyle mDefaultLineStringStyle;
    private final GeoJsonPointStyle mDefaultPointStyle;
    private final GeoJsonPolygonStyle mDefaultPolygonStyle;
    private final BiMultiMap<GeoJsonFeature> mFeatures = new BiMultiMap<>();
    private boolean mLayerOnMap;
    private GoogleMap mMap;

    GeoJsonRenderer(GoogleMap map, HashMap<GeoJsonFeature, Object> features) {
        this.mMap = map;
        this.mFeatures.putAll(features);
        this.mLayerOnMap = false;
        this.mDefaultPointStyle = new GeoJsonPointStyle();
        this.mDefaultLineStringStyle = new GeoJsonLineStringStyle();
        this.mDefaultPolygonStyle = new GeoJsonPolygonStyle();
        for (GeoJsonFeature feature : getFeatures()) {
            setFeatureDefaultStyles(feature);
        }
    }

    private static void removeFromMap(Object mapObject) {
        if (mapObject instanceof Marker) {
            ((Marker) mapObject).remove();
            return;
        }
        if (mapObject instanceof Polyline) {
            ((Polyline) mapObject).remove();
            return;
        }
        if (mapObject instanceof Polygon) {
            ((Polygon) mapObject).remove();
        } else if (mapObject instanceof ArrayList) {
            for (Object mapObjectElement : (ArrayList) mapObject) {
                removeFromMap(mapObjectElement);
            }
        }
    }

    boolean isLayerOnMap() {
        return this.mLayerOnMap;
    }

    GoogleMap getMap() {
        return this.mMap;
    }

    void setMap(GoogleMap map) {
        for (GeoJsonFeature feature : getFeatures()) {
            redrawFeatureToMap(feature, map);
        }
    }

    void addLayerToMap() {
        if (!this.mLayerOnMap) {
            this.mLayerOnMap = true;
            for (GeoJsonFeature feature : getFeatures()) {
                addFeature(feature);
            }
        }
    }

    Set<GeoJsonFeature> getFeatures() {
        return this.mFeatures.keySet();
    }

    GeoJsonFeature getFeature(Object mapObject) {
        return this.mFeatures.getKey(mapObject);
    }

    private void setFeatureDefaultStyles(GeoJsonFeature feature) {
        if (feature.getPointStyle() == null) {
            feature.setPointStyle(this.mDefaultPointStyle);
        }
        if (feature.getLineStringStyle() == null) {
            feature.setLineStringStyle(this.mDefaultLineStringStyle);
        }
        if (feature.getPolygonStyle() == null) {
            feature.setPolygonStyle(this.mDefaultPolygonStyle);
        }
    }

    void addFeature(GeoJsonFeature feature) {
        Object mapObject = FEATURE_NOT_ON_MAP;
        setFeatureDefaultStyles(feature);
        if (this.mLayerOnMap) {
            feature.addObserver(this);
            if (this.mFeatures.containsKey(feature)) {
                removeFromMap(this.mFeatures.get(feature));
            }
            if (feature.hasGeometry()) {
                mapObject = addFeatureToMap(feature, feature.getGeometry());
            }
        }
        this.mFeatures.put(feature, mapObject);
    }

    void removeLayerFromMap() {
        if (this.mLayerOnMap) {
            for (GeoJsonFeature feature : this.mFeatures.keySet()) {
                removeFromMap(this.mFeatures.get(feature));
                feature.deleteObserver(this);
            }
            this.mLayerOnMap = false;
        }
    }

    void removeFeature(GeoJsonFeature feature) {
        if (this.mFeatures.containsKey(feature)) {
            removeFromMap(this.mFeatures.remove(feature));
            feature.deleteObserver(this);
        }
    }

    GeoJsonPointStyle getDefaultPointStyle() {
        return this.mDefaultPointStyle;
    }

    GeoJsonLineStringStyle getDefaultLineStringStyle() {
        return this.mDefaultLineStringStyle;
    }

    GeoJsonPolygonStyle getDefaultPolygonStyle() {
        return this.mDefaultPolygonStyle;
    }

    private Object addFeatureToMap(GeoJsonFeature feature, GeoJsonGeometry geometry) {
        String geometryType = geometry.getType();
        if (geometryType.equals(KmlPoint.GEOMETRY_TYPE)) {
            return addPointToMap(feature.getPointStyle(), (GeoJsonPoint) geometry);
        }
        if (geometryType.equals(KmlLineString.GEOMETRY_TYPE)) {
            return addLineStringToMap(feature.getLineStringStyle(), (GeoJsonLineString) geometry);
        }
        if (geometryType.equals(KmlPolygon.GEOMETRY_TYPE)) {
            return addPolygonToMap(feature.getPolygonStyle(), (GeoJsonPolygon) geometry);
        }
        if (geometryType.equals("MultiPoint")) {
            return addMultiPointToMap(feature.getPointStyle(), (GeoJsonMultiPoint) geometry);
        }
        if (geometryType.equals("MultiLineString")) {
            return addMultiLineStringToMap(feature.getLineStringStyle(), (GeoJsonMultiLineString) geometry);
        }
        if (geometryType.equals("MultiPolygon")) {
            return addMultiPolygonToMap(feature.getPolygonStyle(), (GeoJsonMultiPolygon) geometry);
        }
        if (geometryType.equals("GeometryCollection")) {
            return addGeometryCollectionToMap(feature, ((GeoJsonGeometryCollection) geometry).getGeometries());
        }
        return null;
    }

    private Marker addPointToMap(GeoJsonPointStyle pointStyle, GeoJsonPoint point) {
        MarkerOptions markerOptions = pointStyle.toMarkerOptions();
        markerOptions.position(point.getCoordinates());
        return this.mMap.addMarker(markerOptions);
    }

    private ArrayList<Marker> addMultiPointToMap(GeoJsonPointStyle pointStyle, GeoJsonMultiPoint multiPoint) {
        ArrayList<Marker> markers = new ArrayList<>();
        for (GeoJsonPoint geoJsonPoint : multiPoint.getPoints()) {
            markers.add(addPointToMap(pointStyle, geoJsonPoint));
        }
        return markers;
    }

    private Polyline addLineStringToMap(GeoJsonLineStringStyle lineStringStyle, GeoJsonLineString lineString) {
        PolylineOptions polylineOptions = lineStringStyle.toPolylineOptions();
        polylineOptions.addAll(lineString.getCoordinates());
        Polyline addedPolyline = this.mMap.addPolyline(polylineOptions);
        addedPolyline.setClickable(true);
        return addedPolyline;
    }

    private ArrayList<Polyline> addMultiLineStringToMap(GeoJsonLineStringStyle lineStringStyle, GeoJsonMultiLineString multiLineString) {
        ArrayList<Polyline> polylines = new ArrayList<>();
        for (GeoJsonLineString geoJsonLineString : multiLineString.getLineStrings()) {
            polylines.add(addLineStringToMap(lineStringStyle, geoJsonLineString));
        }
        return polylines;
    }

    private Polygon addPolygonToMap(GeoJsonPolygonStyle polygonStyle, GeoJsonPolygon polygon) {
        PolygonOptions polygonOptions = polygonStyle.toPolygonOptions();
        polygonOptions.addAll(polygon.getCoordinates().get(0));
        for (int i = 1; i < polygon.getCoordinates().size(); i++) {
            polygonOptions.addHole(polygon.getCoordinates().get(i));
        }
        Polygon addedPolygon = this.mMap.addPolygon(polygonOptions);
        addedPolygon.setClickable(true);
        return addedPolygon;
    }

    private ArrayList<Polygon> addMultiPolygonToMap(GeoJsonPolygonStyle polygonStyle, GeoJsonMultiPolygon multiPolygon) {
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (GeoJsonPolygon geoJsonPolygon : multiPolygon.getPolygons()) {
            polygons.add(addPolygonToMap(polygonStyle, geoJsonPolygon));
        }
        return polygons;
    }

    private ArrayList<Object> addGeometryCollectionToMap(GeoJsonFeature feature, List<GeoJsonGeometry> geoJsonGeometries) {
        ArrayList<Object> geometries = new ArrayList<>();
        for (GeoJsonGeometry geometry : geoJsonGeometries) {
            geometries.add(addFeatureToMap(feature, geometry));
        }
        return geometries;
    }

    private void redrawFeatureToMap(GeoJsonFeature feature) {
        redrawFeatureToMap(feature, this.mMap);
    }

    private void redrawFeatureToMap(GeoJsonFeature feature, GoogleMap map) {
        removeFromMap(this.mFeatures.get(feature));
        this.mFeatures.put(feature, FEATURE_NOT_ON_MAP);
        this.mMap = map;
        if (map != null && feature.hasGeometry()) {
            this.mFeatures.put(feature, addFeatureToMap(feature, feature.getGeometry()));
        }
    }

    @Override // java.util.Observer
    public void update(Observable observable, Object data) {
        if (observable instanceof GeoJsonFeature) {
            GeoJsonFeature feature = (GeoJsonFeature) observable;
            boolean featureIsOnMap = this.mFeatures.get(feature) != FEATURE_NOT_ON_MAP;
            if (featureIsOnMap && feature.hasGeometry()) {
                redrawFeatureToMap(feature);
                return;
            }
            if (featureIsOnMap && !feature.hasGeometry()) {
                removeFromMap(this.mFeatures.get(feature));
                this.mFeatures.put(feature, FEATURE_NOT_ON_MAP);
            } else if (!featureIsOnMap && feature.hasGeometry()) {
                addFeature(feature);
            }
        }
    }
}
