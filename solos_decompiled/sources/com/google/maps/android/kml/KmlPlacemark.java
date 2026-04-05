package com.google.maps.android.kml;

import java.util.HashMap;

/* JADX INFO: loaded from: classes69.dex */
public class KmlPlacemark {
    private final KmlGeometry mGeometry;
    private final KmlStyle mInlineStyle;
    private HashMap<String, String> mProperties;
    private final String mStyle;

    public KmlPlacemark(KmlGeometry geometry, String style, KmlStyle inlineStyle, HashMap<String, String> properties) {
        this.mProperties = new HashMap<>();
        this.mGeometry = geometry;
        this.mStyle = style;
        this.mInlineStyle = inlineStyle;
        this.mProperties = properties;
    }

    public String getStyleId() {
        return this.mStyle;
    }

    public KmlStyle getInlineStyle() {
        return this.mInlineStyle;
    }

    public Iterable getProperties() {
        return this.mProperties.entrySet();
    }

    public String getProperty(String keyValue) {
        return this.mProperties.get(keyValue);
    }

    public KmlGeometry getGeometry() {
        return this.mGeometry;
    }

    public boolean hasProperty(String keyValue) {
        return this.mProperties.containsKey(keyValue);
    }

    public boolean hasProperties() {
        return this.mProperties.size() > 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Placemark").append("{");
        sb.append("\n style id=").append(this.mStyle);
        sb.append(",\n inline style=").append(this.mInlineStyle);
        sb.append(",\n properties=").append(this.mProperties);
        sb.append(",\n geometry=").append(this.mGeometry);
        sb.append("\n}\n");
        return sb.toString();
    }
}
