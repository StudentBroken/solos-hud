package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class Image {

    @SerializedName("h")
    public final int h;

    @SerializedName("image_type")
    public final String imageType;

    @SerializedName("w")
    public final int w;

    public Image(int w, int h, String imageType) {
        this.w = w;
        this.h = h;
        this.imageType = imageType;
    }
}
