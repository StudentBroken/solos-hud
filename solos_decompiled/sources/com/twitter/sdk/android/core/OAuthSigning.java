package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import io.fabric.sdk.android.services.network.HttpMethod;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class OAuthSigning {
    static final String VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    protected final TwitterAuthConfig authConfig;
    protected final TwitterAuthToken authToken;
    protected final OAuth1aHeaders oAuth1aHeaders;

    public OAuthSigning(TwitterAuthConfig authConfig, TwitterAuthToken authToken) {
        this(authConfig, authToken, new OAuth1aHeaders());
    }

    public OAuthSigning(TwitterAuthConfig authConfig, TwitterAuthToken authToken, OAuth1aHeaders oAuth1aHeaders) {
        if (authConfig == null) {
            throw new IllegalArgumentException("authConfig must not be null");
        }
        if (authToken == null) {
            throw new IllegalArgumentException("authToken must not be null");
        }
        this.authConfig = authConfig;
        this.authToken = authToken;
        this.oAuth1aHeaders = oAuth1aHeaders;
    }

    public Map<String, String> getOAuthEchoHeaders(String method, String url, Map<String, String> postParams) {
        return this.oAuth1aHeaders.getOAuthEchoHeaders(this.authConfig, this.authToken, null, method, url, postParams);
    }

    public Map<String, String> getOAuthEchoHeadersForVerifyCredentials() {
        return this.oAuth1aHeaders.getOAuthEchoHeaders(this.authConfig, this.authToken, null, HttpMethod.GET.name(), VERIFY_CREDENTIALS_URL, null);
    }
}
