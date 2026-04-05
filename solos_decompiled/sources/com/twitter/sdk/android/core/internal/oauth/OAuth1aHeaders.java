package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class OAuth1aHeaders {
    public static final String HEADER_AUTH_CREDENTIALS = "X-Verify-Credentials-Authorization";
    public static final String HEADER_AUTH_SERVICE_PROVIDER = "X-Auth-Service-Provider";

    public String getAuthorizationHeader(TwitterAuthConfig authConfig, TwitterAuthToken authToken, String callback, String method, String url, Map<String, String> postParams) {
        OAuth1aParameters oAuth1aParameters = getOAuth1aParameters(authConfig, authToken, callback, method, url, postParams);
        return oAuth1aParameters.getAuthorizationHeader();
    }

    public Map<String, String> getOAuthEchoHeaders(TwitterAuthConfig authConfig, TwitterAuthToken authToken, String callback, String method, String url, Map<String, String> postParams) {
        Map<String, String> headers = new HashMap<>(2);
        String authorizationHeader = getAuthorizationHeader(authConfig, authToken, callback, method, url, postParams);
        headers.put(HEADER_AUTH_CREDENTIALS, authorizationHeader);
        headers.put(HEADER_AUTH_SERVICE_PROVIDER, url);
        return headers;
    }

    OAuth1aParameters getOAuth1aParameters(TwitterAuthConfig authConfig, TwitterAuthToken authToken, String callback, String method, String url, Map<String, String> postParams) {
        return new OAuth1aParameters(authConfig, authToken, callback, method, url, postParams);
    }
}
