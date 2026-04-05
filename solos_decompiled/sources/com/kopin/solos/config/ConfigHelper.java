package com.kopin.solos.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.kopin.peloton.Peloton;
import com.kopin.solos.metrics.MetricResource;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class ConfigHelper {
    private static final String PREF_FILE = "config_prefs_main";
    private static WeakReference<Context> mContext;
    private static SharedPreferences preferences;

    private enum Key {
        CONFIG_DAY_OFFSET,
        CONFIG_SHORT_FTP,
        CONFIG_DEBUG,
        CONFIG_FAKE_DATA,
        CONFIG_SHOW_GLASSES_MIRROR,
        CONFIG_CLOUD,
        CONFIG_CLOUD_LIVE,
        CONFIG_CLOUD_HTTPS,
        CONFIG_CLOUD_SYNC_UPLOAD_PERIOD,
        CONFIG_CLOUD_SYNC_FULL_PERIOD,
        CONFIG_VOCON,
        CONFIG_ALLOW_RUN,
        CONFIG_FIRMWARE_SERVER_LIVE
    }

    public static void init(Context context) {
        mContext = new WeakReference<>(context.getApplicationContext());
        preferences = context.getSharedPreferences(PREF_FILE, 0);
        load();
    }

    private static void load() {
        if (preferences != null) {
            Config.START_DAY_OFFSET = preferences.getLong(Key.CONFIG_DAY_OFFSET.name(), 0L);
            Config.SHORT_FTP = preferences.getBoolean(Key.CONFIG_SHORT_FTP.name(), Config.SHORT_FTP);
            Config.SHOW_GLASSES_MIRROR = preferences.getBoolean(Key.CONFIG_SHOW_GLASSES_MIRROR.name(), Config.SHOW_GLASSES_MIRROR);
            Config.FAKE_DATA = preferences.getBoolean(Key.CONFIG_FAKE_DATA.name(), Config.FAKE_DATA);
            Config.DEBUG = preferences.getBoolean(Key.CONFIG_DEBUG.name(), Config.DEBUG);
            Config.VOCON_ENABLED = preferences.getBoolean(Key.CONFIG_VOCON.name(), Config.VOCON_ENABLED);
            Config.SYNC_PROVIDER = preferences.getBoolean(Key.CONFIG_CLOUD.name(), Config.SYNC_PROVIDER == Platforms.Peloton) ? Platforms.Peloton : Platforms.None;
            Config.CLOUD_LIVE = preferences.getBoolean(Key.CONFIG_CLOUD_LIVE.name(), Config.CLOUD_LIVE);
            Config.CLOUD_HTTPS = preferences.getBoolean(Key.CONFIG_CLOUD_HTTPS.name(), Config.CLOUD_HTTPS);
            if (Config.SYNC_PROVIDER != Platforms.None) {
                Peloton.configure(Config.CLOUD_LIVE, Config.CLOUD_HTTPS);
            }
            Config.CLOUD_SYNC_UPLOAD_PERIOD = preferences.getLong(Key.CONFIG_CLOUD_SYNC_UPLOAD_PERIOD.name(), Config.CLOUD_SYNC_UPLOAD_PERIOD);
            Config.CLOUD_SYNC_FULL_PERIOD = preferences.getLong(Key.CONFIG_CLOUD_SYNC_FULL_PERIOD.name(), Config.CLOUD_SYNC_FULL_PERIOD);
            Config.MULTI_SPORT_ENABLED = preferences.getBoolean(Key.CONFIG_ALLOW_RUN.name(), Features.IS_SPORT_CHANGE_ENABLED);
            Config.IS_RELEASE_FIRMWARE_SERVER = preferences.getBoolean(Key.CONFIG_FIRMWARE_SERVER_LIVE.name(), true);
        }
    }

    public static Map<MetricResource, String> loadMetrics() {
        Map<MetricResource, String> data = new HashMap<>();
        for (MetricResource metricResource : ConfigActivity.metricResources) {
            MetricItem metricItem = new MetricItem(mContext.get(), metricResource);
            data.put(metricResource, metricItem.deserialize());
        }
        return data;
    }

    public static void save(long dayOffset, boolean shortFtp, boolean debug, boolean fake, boolean cloud, boolean cloudLive, boolean cloudHttps, long syncUpload, long syncFull, boolean mirror, boolean allowRun, boolean liveFirmwareServer) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(Key.CONFIG_DAY_OFFSET.name(), dayOffset);
            editor.putBoolean(Key.CONFIG_SHORT_FTP.name(), shortFtp);
            editor.putBoolean(Key.CONFIG_DEBUG.name(), debug);
            editor.putBoolean(Key.CONFIG_FAKE_DATA.name(), fake);
            editor.putBoolean(Key.CONFIG_SHOW_GLASSES_MIRROR.name(), mirror);
            editor.putBoolean(Key.CONFIG_VOCON.name(), Config.VOCON_ENABLED);
            editor.putBoolean(Key.CONFIG_CLOUD.name(), cloud);
            editor.putBoolean(Key.CONFIG_CLOUD_LIVE.name(), cloudLive);
            editor.putBoolean(Key.CONFIG_CLOUD_HTTPS.name(), cloudHttps);
            editor.putLong(Key.CONFIG_CLOUD_SYNC_UPLOAD_PERIOD.name(), syncUpload);
            editor.putLong(Key.CONFIG_CLOUD_SYNC_FULL_PERIOD.name(), syncFull);
            editor.putBoolean(Key.CONFIG_ALLOW_RUN.name(), allowRun);
            editor.putBoolean(Key.CONFIG_FIRMWARE_SERVER_LIVE.name(), liveFirmwareServer);
            editor.apply();
            load();
        }
    }

    static void apply() {
        if (preferences != null) {
            save(Config.START_DAY_OFFSET, Config.SHORT_FTP, Config.DEBUG, Config.FAKE_DATA, Config.SYNC_PROVIDER != Platforms.None, Config.CLOUD_LIVE, Config.CLOUD_HTTPS, Config.CLOUD_SYNC_UPLOAD_PERIOD, Config.CLOUD_SYNC_FULL_PERIOD, Config.SHOW_GLASSES_MIRROR, Config.MULTI_SPORT_ENABLED, Config.IS_RELEASE_FIRMWARE_SERVER);
        }
    }
}
