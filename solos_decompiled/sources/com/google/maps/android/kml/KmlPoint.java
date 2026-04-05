package com.google.maps.android.kml;

import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes69.dex */
public class KmlPoint implements KmlGeometry<LatLng> {
    public static final String GEOMETRY_TYPE = "Point";
    private final LatLng mCoordinate;

    public KmlPoint(LatLng coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.mCoordinate = coordinate;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public String getGeometryType() {
        return GEOMETRY_TYPE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.maps.android.kml.KmlGeometry
    public LatLng getGeometryObject() {
        return this.mCoordinate;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n coordinates=").append(this.mCoordinate);
        sb.append("\n}\n");
        return sb.toString();
    }
}
