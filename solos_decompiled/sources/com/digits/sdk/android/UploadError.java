package com.digits.sdk.android;

import com.google.analytics.tracking.android.HitTypes;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes18.dex */
class UploadError {

    @SerializedName("code")
    final int code;

    @SerializedName(HitTypes.ITEM)
    final int item;

    @SerializedName("message")
    final String message;

    UploadError(int code, String message, int item) {
        this.code = code;
        this.message = message;
        this.item = item;
    }
}
