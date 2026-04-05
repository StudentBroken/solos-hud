package com.ua.sdk.authentication;

import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class OAuth2CredentialsTO {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName(AccessToken.EXPIRES_IN_KEY)
    Long expiresIn;

    @SerializedName("refresh_token")
    String refreshToken;

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static OAuth2Credentials toImpl(OAuth2CredentialsTO from) {
        OAuth2Credentials answer = new OAuth2CredentialsImpl();
        answer.setAccessToken(from.getAccessToken());
        answer.setRefreshToken(from.getRefreshToken());
        answer.setExpiresAt(Long.valueOf(System.currentTimeMillis() + (from.getExpiresIn().longValue() * 1000)));
        return answer;
    }
}
