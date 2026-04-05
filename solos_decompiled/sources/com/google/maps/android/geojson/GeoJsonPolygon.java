package com.google.maps.android.geojson;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonPolygon implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "Polygon";
    private final List<? extends List<LatLng>> mCoordinates;

    public GeoJsonPolygon(List<? extends List<LatLng>> coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.mCoordinates = coordinates;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return "Polygon";
    }

    public List<? extends List<LatLng>> getCoordinates() {
        return this.mCoordinates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Polygon").append("{");
        sb.append("\n coordinates=").append(this.mCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}
