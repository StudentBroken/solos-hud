package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;

/* JADX INFO: loaded from: classes18.dex */
class DigitsSessionResponse {

    @SerializedName("screen_name")
    public String screenName;

    @SerializedName(OAuthConstants.PARAM_TOKEN_SECRET)
    public String secret;

    @SerializedName(OAuthConstants.PARAM_TOKEN)
    public String token;

    @SerializedName("user_id")
    public long userId;

    DigitsSessionResponse() {
    }

    public boolean isEmpty() {
        return this.token == null && this.secret == null && this.screenName == null && this.userId == 0;
    }
}
