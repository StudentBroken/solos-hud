package com.kopin.solos.share;

/* JADX INFO: loaded from: classes4.dex */
public class Config extends com.kopin.solos.common.config.Config {
    public static final long CLOUD_RIDE_DATA_LOAD_PERIOD_MILLIS = 7776000000L;
    public static final boolean CLOUD_UPLOAD_ALL_ON_EDIT = true;
    private static final long DAY_MILLIS = 86400000;
    private static final long MINUTE_MILLIS = 60000;
    public static Platforms SYNC_PROVIDER = Platforms.Peloton;
    public static long CLOUD_SYNC_UPLOAD_PERIOD = 60000;
    public static long CLOUD_SYNC_FULL_PERIOD = 3600000;
    public static boolean CLOUD_LIVE = true;
    public static boolean CLOUD_HTTPS = true;
}
