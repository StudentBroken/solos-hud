package com.twitter.sdk.android.core.models;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class Coordinates {
    public static final int INDEX_LATITUDE = 1;
    public static final int INDEX_LONGITUDE = 0;

    @SerializedName("coordinates")
    public final List<Double> coordinates;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    public final String type;

    public Coordinates(Double longitude, Double latitude, String type) {
        List<Double> coords = new ArrayList<>(2);
        coords.add(0, longitude);
        coords.add(1, latitude);
        this.coordinates = coords;
        this.type = type;
    }

    public Double getLongitude() {
        return this.coordinates.get(0);
    }

    public Double getLatitude() {
        return this.coordinates.get(1);
    }
}
