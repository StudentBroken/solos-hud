package com.twitter.sdk.android.core.models;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class Place {

    @SerializedName("attributes")
    public final Map<String, String> attributes;

    @SerializedName("bounding_box")
    public final BoundingBox boundingBox;

    @SerializedName("country")
    public final String country;

    @SerializedName("country_code")
    public final String countryCode;

    @SerializedName("full_name")
    public final String fullName;

    @SerializedName("id")
    public final String id;

    @SerializedName("name")
    public final String name;

    @SerializedName("place_type")
    public final String placeType;

    @SerializedName("url")
    public final String url;

    public Place(Map<String, String> attributes, BoundingBox boundingBox, String country, String countryCode, String fullName, String id, String name, String placeType, String url) {
        this.attributes = attributes;
        this.boundingBox = boundingBox;
        this.country = country;
        this.countryCode = countryCode;
        this.fullName = fullName;
        this.id = id;
        this.name = name;
        this.placeType = placeType;
        this.url = url;
    }

    public static class BoundingBox {

        @SerializedName("coordinates")
        public final List<List<List<Double>>> coordinates;

        @SerializedName(ShareConstants.MEDIA_TYPE)
        public final String type;

        public BoundingBox(List<List<List<Double>>> coordinates, String type) {
            this.coordinates = coordinates;
            this.type = type;
        }
    }
}
