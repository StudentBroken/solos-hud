package com.google.maps.android.geojson;

import com.google.android.gms.maps.model.LatLngBounds;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonFeature extends Observable implements Observer {
    private final LatLngBounds mBoundingBox;
    private GeoJsonGeometry mGeometry;
    private final String mId;
    private GeoJsonLineStringStyle mLineStringStyle;
    private GeoJsonPointStyle mPointStyle;
    private GeoJsonPolygonStyle mPolygonStyle;
    private final HashMap<String, String> mProperties;

    public GeoJsonFeature(GeoJsonGeometry geometry, String id, HashMap<String, String> properties, LatLngBounds boundingBox) {
        this.mGeometry = geometry;
        this.mId = id;
        this.mBoundingBox = boundingBox;
        if (properties == null) {
            this.mProperties = new HashMap<>();
        } else {
            this.mProperties = properties;
        }
    }

    public Iterable<String> getPropertyKeys() {
        return this.mProperties.keySet();
    }

    public String getProperty(String property) {
        return this.mProperties.get(property);
    }

    public String setProperty(String property, String propertyValue) {
        return this.mProperties.put(property, propertyValue);
    }

    public boolean hasProperty(String property) {
        return this.mProperties.containsKey(property);
    }

    public String removeProperty(String property) {
        return this.mProperties.remove(property);
    }

    public GeoJsonPointStyle getPointStyle() {
        return this.mPointStyle;
    }

    public void setPointStyle(GeoJsonPointStyle pointStyle) {
        if (pointStyle == null) {
            throw new IllegalArgumentException("Point style cannot be null");
        }
        if (this.mPointStyle != null) {
            this.mPointStyle.deleteObserver(this);
        }
        this.mPointStyle = pointStyle;
        this.mPointStyle.addObserver(this);
        checkRedrawFeature(this.mPointStyle);
    }

    public GeoJsonLineStringStyle getLineStringStyle() {
        return this.mLineStringStyle;
    }

    public void setLineStringStyle(GeoJsonLineStringStyle lineStringStyle) {
        if (lineStringStyle == null) {
            throw new IllegalArgumentException("Line string style cannot be null");
        }
        if (this.mLineStringStyle != null) {
            this.mLineStringStyle.deleteObserver(this);
        }
        this.mLineStringStyle = lineStringStyle;
        this.mLineStringStyle.addObserver(this);
        checkRedrawFeature(this.mLineStringStyle);
    }

    public GeoJsonPolygonStyle getPolygonStyle() {
        return this.mPolygonStyle;
    }

    public void setPolygonStyle(GeoJsonPolygonStyle polygonStyle) {
        if (polygonStyle == null) {
            throw new IllegalArgumentException("Polygon style cannot be null");
        }
        if (this.mPolygonStyle != null) {
            this.mPolygonStyle.deleteObserver(this);
        }
        this.mPolygonStyle = polygonStyle;
        this.mPolygonStyle.addObserver(this);
        checkRedrawFeature(this.mPolygonStyle);
    }

    private void checkRedrawFeature(GeoJsonStyle style) {
        if (this.mGeometry != null && Arrays.asList(style.getGeometryType()).contains(this.mGeometry.getType())) {
            setChanged();
            notifyObservers();
        }
    }

    public GeoJsonGeometry getGeometry() {
        return this.mGeometry;
    }

    public void setGeometry(GeoJsonGeometry geometry) {
        this.mGeometry = geometry;
        setChanged();
        notifyObservers();
    }

    public String getId() {
        return this.mId;
    }

    public boolean hasGeometry() {
        return this.mGeometry != null;
    }

    public LatLngBounds getBoundingBox() {
        return this.mBoundingBox;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Feature{");
        sb.append("\n bounding box=").append(this.mBoundingBox);
        sb.append(",\n geometry=").append(this.mGeometry);
        sb.append(",\n point style=").append(this.mPointStyle);
        sb.append(",\n line string style=").append(this.mLineStringStyle);
        sb.append(",\n polygon style=").append(this.mPolygonStyle);
        sb.append(",\n id=").append(this.mId);
        sb.append(",\n properties=").append(this.mProperties);
        sb.append("\n}\n");
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Observer
    public void update(Observable observable, Object data) {
        if (observable instanceof GeoJsonStyle) {
            checkRedrawFeature((GeoJsonStyle) observable);
        }
    }
}
