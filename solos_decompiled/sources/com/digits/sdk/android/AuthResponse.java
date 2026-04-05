package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes18.dex */
class AuthResponse {

    @SerializedName("config")
    public AuthConfig authConfig;

    @SerializedName(DigitsClient.EXTRA_PHONE)
    public String normalizedPhoneNumber;

    @SerializedName("login_verification_request_id")
    public String requestId;

    @SerializedName("login_verification_user_id")
    public long userId;

    AuthResponse() {
    }
}
