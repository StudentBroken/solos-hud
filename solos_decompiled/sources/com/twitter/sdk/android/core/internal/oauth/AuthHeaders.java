package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public interface AuthHeaders {
    public static final String HEADER_AUTHORIZATION = "Authorization";

    Map<String, String> getAuthHeaders(TwitterAuthConfig twitterAuthConfig, String str, String str2, Map<String, String> map);
}
