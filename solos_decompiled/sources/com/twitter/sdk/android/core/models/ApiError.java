package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class ApiError {

    @SerializedName("code")
    private final int code;

    @SerializedName("message")
    private final String message;

    public ApiError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}
