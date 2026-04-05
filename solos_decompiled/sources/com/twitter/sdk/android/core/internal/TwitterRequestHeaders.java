package com.twitter.sdk.android.core.internal;

import android.text.TextUtils;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterRequestHeaders {
    public static final String HEADER_USER_AGENT = "User-Agent";
    private final TwitterAuthConfig authConfig;
    private final String method;
    private final Map<String, String> postParams;
    private final Session session;
    private final String url;
    private final String userAgent;

    public TwitterRequestHeaders(String method, String url, TwitterAuthConfig authConfig, Session session, String userAgent, Map<String, String> postParams) {
        this.method = method;
        this.url = url;
        this.authConfig = authConfig;
        this.session = session;
        this.userAgent = userAgent;
        this.postParams = postParams;
    }

    public final Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.putAll(getExtraHeaders());
        if (!TextUtils.isEmpty(this.userAgent)) {
            headers.put("User-Agent", this.userAgent);
        }
        headers.putAll(getAuthHeaders());
        return headers;
    }

    protected Map<String, String> getExtraHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> getAuthHeaders() {
        return (this.session == null || this.session.getAuthToken() == null) ? Collections.emptyMap() : this.session.getAuthToken().getAuthHeaders(this.authConfig, getMethod(), this.url, getPostParams());
    }

    protected String getMethod() {
        return this.method;
    }

    protected Map<String, String> getPostParams() {
        return this.postParams;
    }
}
