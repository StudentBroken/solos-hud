package com.google.maps.android.geojson;

import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonMultiLineString implements GeoJsonGeometry {
    private static final String GEOMETRY_TYPE = "MultiLineString";
    private final List<GeoJsonLineString> mGeoJsonLineStrings;

    public GeoJsonMultiLineString(List<GeoJsonLineString> geoJsonLineStrings) {
        if (geoJsonLineStrings == null) {
            throw new IllegalArgumentException("GeoJsonLineStrings cannot be null");
        }
        this.mGeoJsonLineStrings = geoJsonLineStrings;
    }

    @Override // com.google.maps.android.geojson.GeoJsonGeometry
    public String getType() {
        return GEOMETRY_TYPE;
    }

    public List<GeoJsonLineString> getLineStrings() {
        return this.mGeoJsonLineStrings;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n LineStrings=").append(this.mGeoJsonLineStrings);
        sb.append("\n}\n");
        return sb.toString();
    }
}
