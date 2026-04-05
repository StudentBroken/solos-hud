package com.google.maps.android.geojson;

import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonMultiPolygon implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "MultiPolygon";
    private final List<GeoJsonPolygon> mGeoJsonPolygons;

    public GeoJsonMultiPolygon(List<GeoJsonPolygon> geoJsonPolygons) {
        if (geoJsonPolygons == null) {
            throw new IllegalArgumentException("GeoJsonPolygons cannot be null");
        }
        this.mGeoJsonPolygons = geoJsonPolygons;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return GEOMETRY_TYPE;
    }

    public List<GeoJsonPolygon> getPolygons() {
        return this.mGeoJsonPolygons;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n Polygons=").append(this.mGeoJsonPolygons);
        sb.append("\n}\n");
        return sb.toString();
    }
}
