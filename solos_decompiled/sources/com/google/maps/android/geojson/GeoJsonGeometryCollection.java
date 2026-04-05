package com.google.maps.android.geojson;

import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonGeometryCollection implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "GeometryCollection";
    private final List<GeoJsonGeometry> mGeometries;

    public GeoJsonGeometryCollection(List<GeoJsonGeometry> geometries) {
        if (geometries == null) {
            throw new IllegalArgumentException("Geometries cannot be null");
        }
        this.mGeometries = geometries;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return GEOMETRY_TYPE;
    }

    public List<GeoJsonGeometry> getGeometries() {
        return this.mGeometries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n Geometries=").append(this.mGeometries);
        sb.append("\n}\n");
        return sb.toString();
    }
}
