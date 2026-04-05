package com.twitter.sdk.android.core.internal.oauth;

/* JADX INFO: loaded from: classes62.dex */
public class AppAuthToken extends OAuth2Token {
    public AppAuthToken(String tokenType, String accessToken) {
        super(tokenType, accessToken);
    }

    public AppAuthToken(String tokenType, String accessToken, long createdAt) {
        super(tokenType, accessToken, createdAt);
    }
}
