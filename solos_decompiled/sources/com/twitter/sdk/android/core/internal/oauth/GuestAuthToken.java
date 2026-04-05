package com.twitter.sdk.android.core.internal.oauth;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class GuestAuthToken extends OAuth2Token {
    private static final long EXPIRES_IN_MS = 10800000;
    public static final String HEADER_GUEST_TOKEN = "x-guest-token";

    @SerializedName("guest_token")
    private final String guestToken;

    public GuestAuthToken(String tokenType, String accessToken, String guestToken) {
        super(tokenType, accessToken);
        this.guestToken = guestToken;
    }

    public GuestAuthToken(String tokenType, String accessToken, String guestToken, long createdAt) {
        super(tokenType, accessToken, createdAt);
        this.guestToken = guestToken;
    }

    public String getGuestToken() {
        return this.guestToken;
    }

    @Override // com.twitter.sdk.android.core.internal.oauth.OAuth2Token, com.twitter.sdk.android.core.AuthToken
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.createdAt + EXPIRES_IN_MS;
    }

    @Override // com.twitter.sdk.android.core.internal.oauth.OAuth2Token, com.twitter.sdk.android.core.internal.oauth.AuthHeaders
    public Map<String, String> getAuthHeaders(TwitterAuthConfig authConfig, String method, String url, Map<String, String> postParams) {
        Map<String, String> headers = super.getAuthHeaders(authConfig, method, url, postParams);
        headers.put(HEADER_GUEST_TOKEN, getGuestToken());
        return headers;
    }

    @Override // com.twitter.sdk.android.core.internal.oauth.OAuth2Token
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GuestAuthToken that = (GuestAuthToken) o;
        if (this.guestToken != null) {
            if (this.guestToken.equals(that.guestToken)) {
                return true;
            }
        } else if (that.guestToken == null) {
            return true;
        }
        return false;
    }

    @Override // com.twitter.sdk.android.core.internal.oauth.OAuth2Token
    public int hashCode() {
        int result = super.hashCode();
        return (result * 31) + (this.guestToken != null ? this.guestToken.hashCode() : 0);
    }
}
