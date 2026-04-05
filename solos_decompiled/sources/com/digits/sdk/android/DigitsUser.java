package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes18.dex */
public class DigitsUser {

    @SerializedName("id")
    public final long id;

    @SerializedName("id_str")
    public final String idStr;

    public DigitsUser(long id, String idStr) {
        this.id = id;
        this.idStr = idStr;
    }
}
