package com.google.maps.android.kml;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class KmlLineString implements KmlGeometry<List<LatLng>> {
    public static final String GEOMETRY_TYPE = "LineString";
    final ArrayList<LatLng> mCoordinates;

    public KmlLineString(ArrayList<LatLng> coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.mCoordinates = coordinates;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public String getGeometryType() {
        return GEOMETRY_TYPE;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public List<LatLng> getGeometryObject() {
        return this.mCoordinates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n coordinates=").append(this.mCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}
