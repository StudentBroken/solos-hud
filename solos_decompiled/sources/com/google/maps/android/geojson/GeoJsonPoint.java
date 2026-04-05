package com.google.maps.android.geojson;

import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonPoint implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "Point";
    private final LatLng mCoordinates;

    public GeoJsonPoint(LatLng coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        this.mCoordinates = coordinate;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return "Point";
    }

    public LatLng getCoordinates() {
        return this.mCoordinates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Point").append("{");
        sb.append("\n coordinates=").append(this.mCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}
