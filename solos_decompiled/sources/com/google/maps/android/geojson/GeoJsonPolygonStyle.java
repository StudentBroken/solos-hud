package com.google.maps.android.geojson;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.kml.KmlPolygon;
import java.util.Arrays;
import java.util.Observable;

/* JADX INFO: loaded from: classes69.dex */
public class GeoJsonPolygonStyle extends Observable implements GeoJsonStyle {
    private static final String[] GEOMETRY_TYPE = {KmlPolygon.GEOMETRY_TYPE, "MultiPolygon", "GeometryCollection"};
    private final PolygonOptions mPolygonOptions = new PolygonOptions();

    @Override // com.google.maps.android.geojson.GeoJsonStyle
    public String[] getGeometryType() {
        return GEOMETRY_TYPE;
    }

    public int getFillColor() {
        return this.mPolygonOptions.getFillColor();
    }

    public void setFillColor(int fillColor) {
        this.mPolygonOptions.fillColor(fillColor);
        styleChanged();
    }

    public boolean isGeodesic() {
        return this.mPolygonOptions.isGeodesic();
    }

    public void setGeodesic(boolean geodesic) {
        this.mPolygonOptions.geodesic(geodesic);
        styleChanged();
    }

    public int getStrokeColor() {
        return this.mPolygonOptions.getStrokeColor();
    }

    public void setStrokeColor(int strokeColor) {
        this.mPolygonOptions.strokeColor(strokeColor);
        styleChanged();
    }

    public float getStrokeWidth() {
        return this.mPolygonOptions.getStrokeWidth();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mPolygonOptions.strokeWidth(strokeWidth);
        styleChanged();
    }

    public float getZIndex() {
        return this.mPolygonOptions.getZIndex();
    }

    public void setZIndex(float zIndex) {
        this.mPolygonOptions.zIndex(zIndex);
        styleChanged();
    }

    @Override // com.google.maps.android.geojson.GeoJsonStyle
    public boolean isVisible() {
        return this.mPolygonOptions.isVisible();
    }

    @Override // com.google.maps.android.geojson.GeoJsonStyle
    public void setVisible(boolean visible) {
        this.mPolygonOptions.visible(visible);
        styleChanged();
    }

    private void styleChanged() {
        setChanged();
        notifyObservers();
    }

    public PolygonOptions toPolygonOptions() {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(this.mPolygonOptions.getFillColor());
        polygonOptions.geodesic(this.mPolygonOptions.isGeodesic());
        polygonOptions.strokeColor(this.mPolygonOptions.getStrokeColor());
        polygonOptions.strokeWidth(this.mPolygonOptions.getStrokeWidth());
        polygonOptions.visible(this.mPolygonOptions.isVisible());
        polygonOptions.zIndex(this.mPolygonOptions.getZIndex());
        return polygonOptions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PolygonStyle{");
        sb.append("\n geometry type=").append(Arrays.toString(GEOMETRY_TYPE));
        sb.append(",\n fill color=").append(getFillColor());
        sb.append(",\n geodesic=").append(isGeodesic());
        sb.append(",\n stroke color=").append(getStrokeColor());
        sb.append(",\n stroke width=").append(getStrokeWidth());
        sb.append(",\n visible=").append(isVisible());
        sb.append(",\n z index=").append(getZIndex());
        sb.append("\n}\n");
        return sb.toString();
    }
}
