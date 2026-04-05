package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class Media {

    @SerializedName("image")
    public final Image image;

    @SerializedName("media_id")
    public final long mediaId;

    @SerializedName("media_id_string")
    public final String mediaIdString;

    @SerializedName("size")
    public final long size;

    public Media(long mediaID, String mediaIdString, long size, Image image) {
        this.mediaId = mediaID;
        this.mediaIdString = mediaIdString;
        this.size = size;
        this.image = image;
    }
}
