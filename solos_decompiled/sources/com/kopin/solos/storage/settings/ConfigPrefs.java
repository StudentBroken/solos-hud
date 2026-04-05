package com.kopin.solos.storage.settings;

import android.content.Context;
import android.content.SharedPreferences;
import com.kopin.solos.common.config.MetricDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes54.dex */
public class ConfigPrefs {
    private static String PREF_FILE = "config_prefs";
    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE, 0);
    }

    private static Set<String> getNames(List<MetricDataType> metrics) {
        Set<String> names = new TreeSet<>();
        for (MetricDataType metricDataType : metrics) {
            names.add(metricDataType.name());
        }
        return names;
    }

    private static List<MetricDataType> getMetrics(Set<String> names) {
        if (names == null) {
            return null;
        }
        List<MetricDataType> metrics = new ArrayList<>();
        for (String name : names) {
            metrics.add(MetricDataType.getMetricDataType(name));
        }
        return metrics;
    }

    static List<MetricDataType> getMetricConfigMaster() {
        return getMetrics(PrefItem.METRICS_ON_MASTER.getStringSet());
    }

    static List<MetricDataType> getMetricConfigHeadset() {
        return getMetrics(PrefItem.METRICS_ON_HEADSET.getStringSet());
    }

    static List<MetricDataType> getMetricConfigRideScreen() {
        return getMetrics(PrefItem.METRICS_ON_RIDE_SCREEN.getStringSet());
    }

    public static boolean isMetricConfigMaster(MetricDataType metric) {
        return getMetricConfigMaster() == null || getMetricConfigMaster().contains(metric);
    }

    public static boolean isMetricConfigHeadset(MetricDataType metric) {
        return getMetricConfigHeadset() == null || getMetricConfigHeadset().contains(metric);
    }

    public static boolean isMetricConfigRideScreen(MetricDataType metric) {
        return getMetricConfigRideScreen() == null || getMetricConfigRideScreen().contains(metric);
    }

    static void setMetricConfigMaster(List<MetricDataType> metrics) {
        PrefItem.METRICS_ON_MASTER.apply(getNames(metrics));
    }

    static void setMetricConfigHeadset(List<MetricDataType> metrics) {
        PrefItem.METRICS_ON_HEADSET.apply(getNames(metrics));
    }

    static void setMetricConfigRideScreen(List<MetricDataType> metrics) {
        PrefItem.METRICS_ON_RIDE_SCREEN.apply(getNames(metrics));
    }

    public static void setMetricConfigMaster(MetricDataType metric, boolean active) {
        setMetricConfig(metric, active, PrefItem.METRICS_ON_MASTER);
    }

    public static void setMetricConfigHeadset(MetricDataType metric, boolean active) {
        setMetricConfig(metric, active, PrefItem.METRICS_ON_HEADSET);
    }

    public static void setMetricConfigRide(MetricDataType metric, boolean active) {
        setMetricConfig(metric, active, PrefItem.METRICS_ON_RIDE_SCREEN);
    }

    private static void setMetricConfig(MetricDataType metric, boolean active, PrefItem prefItem) {
        List<MetricDataType> list = getMetrics(prefItem.getStringSet());
        if (list != null) {
            if (active && !list.contains(metric)) {
                list.add(metric);
                prefItem.apply(getNames(list));
                return;
            } else {
                if (!active && list.remove(metric)) {
                    prefItem.apply(getNames(list));
                    return;
                }
                return;
            }
        }
        if (active) {
            List<MetricDataType> list2 = new ArrayList<>();
            list2.add(metric);
            prefItem.apply(getNames(list2));
        }
    }

    private enum PrefItem {
        METRICS_ON_MASTER,
        METRICS_ON_HEADSET,
        METRICS_ON_RIDE_SCREEN;

        Set<String> getStringSet() {
            return ConfigPrefs.preferences.getStringSet(name().toLowerCase(), null);
        }

        void apply(Set<String> set) {
            if (ConfigPrefs.preferences != null) {
                ConfigPrefs.preferences.edit().putStringSet(name().toLowerCase(), set).apply();
            }
        }
    }
}
