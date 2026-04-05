package com.google.maps.android.kml;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes69.dex */
public class KmlMultiGeometry implements KmlGeometry<ArrayList<KmlGeometry>> {
    private static final String GEOMETRY_TYPE = "MultiGeometry";
    private ArrayList<KmlGeometry> mGeometries;

    public KmlMultiGeometry(ArrayList<KmlGeometry> geometries) {
        this.mGeometries = new ArrayList<>();
        if (geometries == null) {
            throw new IllegalArgumentException("Geometries cannot be null");
        }
        this.mGeometries = geometries;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public String getGeometryType() {
        return GEOMETRY_TYPE;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public ArrayList<KmlGeometry> getGeometryObject() {
        return this.mGeometries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n geometries=").append(this.mGeometries);
        sb.append("\n}\n");
        return sb.toString();
    }
}
