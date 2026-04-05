package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.TwitterAuthToken;

/* JADX INFO: loaded from: classes18.dex */
public class VerifyAccountResponse {

    @SerializedName("email_address")
    public Email email;

    @SerializedName(DigitsClient.EXTRA_PHONE)
    public String phoneNumber;

    @SerializedName("access_token")
    TwitterAuthToken token;

    @SerializedName("id_str")
    public long userId;
}
