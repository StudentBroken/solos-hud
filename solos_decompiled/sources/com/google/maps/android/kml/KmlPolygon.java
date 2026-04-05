package com.google.maps.android.kml;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes69.dex */
public class KmlPolygon implements KmlGeometry<ArrayList<ArrayList<LatLng>>> {
    public static final String GEOMETRY_TYPE = "Polygon";
    private final ArrayList<ArrayList<LatLng>> mInnerBoundaryCoordinates;
    private final ArrayList<LatLng> mOuterBoundaryCoordinates;

    public KmlPolygon(ArrayList<LatLng> outerBoundaryCoordinates, ArrayList<ArrayList<LatLng>> innerBoundaryCoordinates) {
        if (outerBoundaryCoordinates == null) {
            throw new IllegalArgumentException("Outer boundary coordinates cannot be null");
        }
        this.mOuterBoundaryCoordinates = outerBoundaryCoordinates;
        this.mInnerBoundaryCoordinates = innerBoundaryCoordinates;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public String getGeometryType() {
        return GEOMETRY_TYPE;
    }

    public ArrayList<LatLng> getOuterBoundaryCoordinates() {
        return this.mOuterBoundaryCoordinates;
    }

    public ArrayList<ArrayList<LatLng>> getInnerBoundaryCoordinates() {
        return this.mInnerBoundaryCoordinates;
    }

    @Override // com.google.maps.android.kml.KmlGeometry
    public ArrayList<ArrayList<LatLng>> getGeometryObject() {
        ArrayList<ArrayList<LatLng>> coordinates = new ArrayList<>();
        coordinates.add(this.mOuterBoundaryCoordinates);
        if (this.mInnerBoundaryCoordinates != null) {
            coordinates.addAll(this.mInnerBoundaryCoordinates);
        }
        return coordinates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n outer coordinates=").append(this.mOuterBoundaryCoordinates);
        sb.append(",\n inner coordinates=").append(this.mInnerBoundaryCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}
