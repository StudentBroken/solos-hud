package com.twitter.sdk.android.tweetcomposer.internal;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes29.dex */
public class CardCreate {

    @SerializedName("card_uri")
    public final String cardUri;

    @SerializedName("status")
    public final String status;

    public CardCreate(String cardUri, String status) {
        this.cardUri = cardUri;
        this.status = status;
    }
}
