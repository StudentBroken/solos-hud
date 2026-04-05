package com.twitter.sdk.android.core.internal.scribe;

/* JADX INFO: loaded from: classes62.dex */
public class ScribeConfig {
    public static final String BASE_URL = "https://api.twitter.com";
    public static final int DEFAULT_MAX_FILES_TO_KEEP = 100;
    public static final int DEFAULT_SEND_INTERVAL_SECONDS = 600;
    public final String baseUrl;
    public final boolean isEnabled;
    public final int maxFilesToKeep;
    public final String pathType;
    public final String pathVersion;
    public final int sendIntervalSeconds;
    public final String sequence;
    public final String userAgent;

    public ScribeConfig(boolean isEnabled, String baseUrl, String pathVersion, String pathType, String sequence, String userAgent, int maxFilesToKeep, int sendIntervalSeconds) {
        this.isEnabled = isEnabled;
        this.baseUrl = baseUrl;
        this.pathVersion = pathVersion;
        this.pathType = pathType;
        this.sequence = sequence;
        this.userAgent = userAgent;
        this.maxFilesToKeep = maxFilesToKeep;
        this.sendIntervalSeconds = sendIntervalSeconds;
    }
}
