package com.google.maps.android.geojson;

import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonMultiPoint implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "MultiPoint";
    private final List<GeoJsonPoint> mGeoJsonPoints;

    public GeoJsonMultiPoint(List<GeoJsonPoint> geoJsonPoints) {
        if (geoJsonPoints == null) {
            throw new IllegalArgumentException("GeoJsonPoints cannot be null");
        }
        this.mGeoJsonPoints = geoJsonPoints;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return GEOMETRY_TYPE;
    }

    public List<GeoJsonPoint> getPoints() {
        return this.mGeoJsonPoints;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n points=").append(this.mGeoJsonPoints);
        sb.append("\n}\n");
        return sb.toString();
    }
}
