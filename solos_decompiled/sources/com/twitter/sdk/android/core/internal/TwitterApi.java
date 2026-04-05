package com.twitter.sdk.android.core.internal;

import android.net.Uri;
import android.os.Build;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterApi {
    public static final String BASE_HOST = "api.twitter.com";
    public static final String BASE_HOST_URL = "https://api.twitter.com";
    private final String baseHostUrl;

    public TwitterApi() {
        this("https://api.twitter.com");
    }

    public TwitterApi(String baseHostUrl) {
        this.baseHostUrl = baseHostUrl;
    }

    public String getBaseHostUrl() {
        return this.baseHostUrl;
    }

    public Uri.Builder buildUponBaseHostUrl(String... paths) {
        Uri.Builder builder = Uri.parse(getBaseHostUrl()).buildUpon();
        if (paths != null) {
            for (String p : paths) {
                builder.appendPath(p);
            }
        }
        return builder;
    }

    public static String buildUserAgent(String clientName, String version) {
        StringBuilder ua = new StringBuilder(clientName).append('/').append(version).append(' ').append(Build.MODEL).append('/').append(Build.VERSION.RELEASE).append(" (").append(Build.MANUFACTURER).append(';').append(Build.MODEL).append(';').append(Build.BRAND).append(';').append(Build.PRODUCT).append(')');
        return ua.toString();
    }
}
