package com.twitter.sdk.android.core.internal.oauth;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
class GuestTokenResponse {

    @SerializedName("guest_token")
    public final String guestToken;

    public GuestTokenResponse(String guestToken) {
        this.guestToken = guestToken;
    }
}
