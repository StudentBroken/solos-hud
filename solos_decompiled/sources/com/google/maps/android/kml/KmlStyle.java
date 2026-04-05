package com.google.maps.android.kml;

import android.graphics.Color;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.pupil.ui.PageHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/* JADX INFO: loaded from: classes69.dex */
class KmlStyle {
    private static final int HSV_VALUES = 3;
    private static final int HUE_VALUE = 0;
    private static final int INITIAL_SCALE = 1;
    private String mIconUrl;
    private boolean mFill = true;
    private boolean mOutline = true;
    private String mStyleId = null;
    private final MarkerOptions mMarkerOptions = new MarkerOptions();
    private final PolylineOptions mPolylineOptions = new PolylineOptions();
    private final PolygonOptions mPolygonOptions = new PolygonOptions();
    private final HashMap<String, String> mBalloonOptions = new HashMap<>();
    private final HashSet<String> mStylesSet = new HashSet<>();
    private double mScale = 1.0d;
    private float mMarkerColor = 0.0f;
    private boolean mIconRandomColorMode = false;
    private boolean mLineRandomColorMode = false;
    private boolean mPolyRandomColorMode = false;

    KmlStyle() {
    }

    void setInfoWindowText(String text) {
        this.mBalloonOptions.put(PageHelper.TEXT_PART_TAG, text);
    }

    String getStyleId() {
        return this.mStyleId;
    }

    void setStyleId(String styleId) {
        this.mStyleId = styleId;
    }

    boolean isStyleSet(String style) {
        return this.mStylesSet.contains(style);
    }

    boolean hasFill() {
        return this.mFill;
    }

    void setFill(boolean fill) {
        this.mFill = fill;
    }

    double getIconScale() {
        return this.mScale;
    }

    void setIconScale(double scale) {
        this.mScale = scale;
        this.mStylesSet.add("iconScale");
    }

    boolean hasOutline() {
        return this.mOutline;
    }

    boolean hasBalloonStyle() {
        return this.mBalloonOptions.size() > 0;
    }

    void setOutline(boolean outline) {
        this.mOutline = outline;
        this.mStylesSet.add("outline");
    }

    String getIconUrl() {
        return this.mIconUrl;
    }

    void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
        this.mStylesSet.add("iconUrl");
    }

    void setFillColor(String color) {
        this.mPolygonOptions.fillColor(Color.parseColor("#" + convertColor(color)));
        this.mStylesSet.add("fillColor");
    }

    void setMarkerColor(String color) {
        int integerColor = Color.parseColor("#" + convertColor(color));
        this.mMarkerColor = getHueValue(integerColor);
        this.mMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(this.mMarkerColor));
        this.mStylesSet.add("markerColor");
    }

    private static float getHueValue(int integerColor) {
        float[] hsvValues = new float[3];
        Color.colorToHSV(integerColor, hsvValues);
        return hsvValues[0];
    }

    private static String convertColor(String color) {
        String newColor;
        if (color.length() > 6) {
            newColor = color.substring(0, 2) + color.substring(6, 8) + color.substring(4, 6) + color.substring(2, 4);
        } else {
            newColor = color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2);
        }
        if (newColor.substring(0, 1).equals(" ")) {
            return "0" + newColor.substring(1, newColor.length());
        }
        return newColor;
    }

    void setHeading(float heading) {
        this.mMarkerOptions.rotation(heading);
        this.mStylesSet.add("heading");
    }

    void setHotSpot(float x, float y, String xUnits, String yUnits) {
        float xAnchor = 0.5f;
        float yAnchor = 1.0f;
        if (xUnits.equals("fraction")) {
            xAnchor = x;
        }
        if (yUnits.equals("fraction")) {
            yAnchor = y;
        }
        this.mMarkerOptions.anchor(xAnchor, yAnchor);
        this.mStylesSet.add("hotSpot");
    }

    void setIconColorMode(String colorMode) {
        this.mIconRandomColorMode = colorMode.equals("random");
        this.mStylesSet.add("iconColorMode");
    }

    boolean isIconRandomColorMode() {
        return this.mIconRandomColorMode;
    }

    void setLineColorMode(String colorMode) {
        this.mLineRandomColorMode = colorMode.equals("random");
        this.mStylesSet.add("lineColorMode");
    }

    boolean isLineRandomColorMode() {
        return this.mLineRandomColorMode;
    }

    void setPolyColorMode(String colorMode) {
        this.mPolyRandomColorMode = colorMode.equals("random");
        this.mStylesSet.add("polyColorMode");
    }

    boolean isPolyRandomColorMode() {
        return this.mPolyRandomColorMode;
    }

    void setOutlineColor(String color) {
        this.mPolylineOptions.color(Color.parseColor("#" + convertColor(color)));
        this.mPolygonOptions.strokeColor(Color.parseColor("#" + color));
        this.mStylesSet.add("outlineColor");
    }

    void setWidth(Float width) {
        this.mPolylineOptions.width(width.floatValue());
        this.mPolygonOptions.strokeWidth(width.floatValue());
        this.mStylesSet.add("width");
    }

    HashMap<String, String> getBalloonOptions() {
        return this.mBalloonOptions;
    }

    private static MarkerOptions createMarkerOptions(MarkerOptions originalMarkerOption, boolean iconRandomColorMode, float markerColor) {
        MarkerOptions newMarkerOption = new MarkerOptions();
        newMarkerOption.rotation(originalMarkerOption.getRotation());
        newMarkerOption.anchor(originalMarkerOption.getAnchorU(), originalMarkerOption.getAnchorV());
        if (iconRandomColorMode) {
            float hue = getHueValue(computeRandomColor((int) markerColor));
            originalMarkerOption.icon(BitmapDescriptorFactory.defaultMarker(hue));
        }
        newMarkerOption.icon(originalMarkerOption.getIcon());
        return newMarkerOption;
    }

    private static PolylineOptions createPolylineOptions(PolylineOptions originalPolylineOption) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(originalPolylineOption.getColor());
        polylineOptions.width(originalPolylineOption.getWidth());
        return polylineOptions;
    }

    private static PolygonOptions createPolygonOptions(PolygonOptions originalPolygonOption, boolean isFill, boolean isOutline) {
        PolygonOptions polygonOptions = new PolygonOptions();
        if (isFill) {
            polygonOptions.fillColor(originalPolygonOption.getFillColor());
        }
        if (isOutline) {
            polygonOptions.strokeColor(originalPolygonOption.getStrokeColor());
            polygonOptions.strokeWidth(originalPolygonOption.getStrokeWidth());
        }
        return polygonOptions;
    }

    MarkerOptions getMarkerOptions() {
        return createMarkerOptions(this.mMarkerOptions, isIconRandomColorMode(), this.mMarkerColor);
    }

    PolylineOptions getPolylineOptions() {
        return createPolylineOptions(this.mPolylineOptions);
    }

    PolygonOptions getPolygonOptions() {
        return createPolygonOptions(this.mPolygonOptions, this.mFill, this.mOutline);
    }

    static int computeRandomColor(int color) {
        Random random = new Random();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red != 0) {
            red = random.nextInt(red);
        }
        if (blue != 0) {
            blue = random.nextInt(blue);
        }
        if (green != 0) {
            green = random.nextInt(green);
        }
        return Color.rgb(red, green, blue);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Style").append("{");
        sb.append("\n balloon options=").append(this.mBalloonOptions);
        sb.append(",\n fill=").append(this.mFill);
        sb.append(",\n outline=").append(this.mOutline);
        sb.append(",\n icon url=").append(this.mIconUrl);
        sb.append(",\n scale=").append(this.mScale);
        sb.append(",\n style id=").append(this.mStyleId);
        sb.append("\n}\n");
        return sb.toString();
    }
}
