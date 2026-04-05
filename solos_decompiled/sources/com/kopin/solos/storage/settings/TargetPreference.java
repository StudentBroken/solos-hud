package com.kopin.solos.storage.settings;

import android.content.Context;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes54.dex */
public class TargetPreference {
    public static final TargetPreference AVERAGE_KICK;
    public static final TargetPreference AVERAGE_PACE;
    public static final TargetPreference AVERAGE_SPEED;
    public static final TargetPreference AVERAGE_STEP;
    public static final TargetPreference DISTANCE;
    public static String KEY_AVG_KICK;
    public static String KEY_AVG_PACE;
    public static String KEY_AVG_SPEED;
    public static String KEY_AVG_STEP;
    public static String KEY_DISTANCE;
    public static String KEY_TIME;
    private static final ArrayList<TargetPreference> LOCALISABLE_TARGETS;
    public static final TargetPreference TIME;
    private boolean mAllowDecimals;
    private String mKey;
    private int mResDefault;
    private int mResLen;
    private int mResMax;
    private int mResMin;
    private int mResUnits;
    public static final TargetPreference NONE = new TargetPreference("");
    public static String KEY_AVG_CADENCE;
    public static final TargetPreference AVERAGE_CADENCE = new TargetPreference(KEY_AVG_CADENCE, R.integer.target_cadence_default, R.integer.target_cadence_min, R.integer.target_cadence_max, R.integer.target_cadence_len, R.string.caps_rpm, false);
    public static String KEY_AVG_HR;
    public static final TargetPreference AVERAGE_HEARTRATE = new TargetPreference(KEY_AVG_HR, R.integer.target_heartrate_default, R.integer.target_heartrate_min, R.integer.target_heartrate_max, R.integer.target_heartrate_len, R.string.caps_bpm, false);
    public static String KEY_AVG_POWER;
    public static final TargetPreference AVERAGE_POWER = new TargetPreference(KEY_AVG_POWER, R.integer.target_power_default, R.integer.target_power_min, R.integer.target_power_max, R.integer.target_power_len, R.string.lower_watts, false);

    static {
        AVERAGE_SPEED = new TargetPreference(KEY_AVG_SPEED, R.integer.target_speed_default, R.integer.target_speed_min, Prefs.isMetric() ? R.integer.target_speed_max : R.integer.target_speed_max_mph, R.integer.target_speed_len, 0, false);
        AVERAGE_PACE = new TargetPreference(KEY_AVG_PACE);
        AVERAGE_STEP = new TargetPreference(KEY_AVG_STEP, R.integer.target_pace_default, R.integer.target_pace_min, Prefs.isMetric() ? R.integer.target_pace_max : R.integer.target_pace_max_minpmi, R.integer.target_pace_len, R.string.spm, true);
        AVERAGE_KICK = new TargetPreference(KEY_AVG_KICK, R.integer.target_running_power_default, R.integer.target_running_power_min, R.integer.target_running_power_max, R.integer.target_running_power_len, R.string.lower_watts, false);
        DISTANCE = new TargetPreference(KEY_DISTANCE, R.integer.target_distance_default, R.integer.target_distance_min, R.integer.target_distance_max, R.integer.target_distance_len, 0, true);
        TIME = new TargetPreference(KEY_TIME);
        LOCALISABLE_TARGETS = new ArrayList<>();
    }

    public static void init(Context context) {
        TargetPreference targetPreference = AVERAGE_CADENCE;
        String string = context.getString(R.string.pref_key_target_average_cadence);
        KEY_AVG_CADENCE = string;
        targetPreference.mKey = string;
        TargetPreference targetPreference2 = AVERAGE_HEARTRATE;
        String string2 = context.getString(R.string.pref_key_target_average_heartrate);
        KEY_AVG_HR = string2;
        targetPreference2.mKey = string2;
        TargetPreference targetPreference3 = AVERAGE_POWER;
        String string3 = context.getString(R.string.pref_key_target_average_power);
        KEY_AVG_POWER = string3;
        targetPreference3.mKey = string3;
        TargetPreference targetPreference4 = AVERAGE_SPEED;
        String string4 = context.getString(R.string.pref_key_target_average_speed);
        KEY_AVG_SPEED = string4;
        targetPreference4.mKey = string4;
        TargetPreference targetPreference5 = AVERAGE_PACE;
        String string5 = context.getString(R.string.pref_key_target_average_pace);
        KEY_AVG_PACE = string5;
        targetPreference5.mKey = string5;
        TargetPreference targetPreference6 = AVERAGE_STEP;
        String string6 = context.getString(R.string.pref_key_target_average_step);
        KEY_AVG_STEP = string6;
        targetPreference6.mKey = string6;
        TargetPreference targetPreference7 = AVERAGE_KICK;
        String string7 = context.getString(R.string.pref_key_target_average_kick);
        KEY_AVG_KICK = string7;
        targetPreference7.mKey = string7;
        TargetPreference targetPreference8 = DISTANCE;
        String string8 = context.getString(R.string.pref_key_target_distance);
        KEY_DISTANCE = string8;
        targetPreference8.mKey = string8;
        TargetPreference targetPreference9 = TIME;
        String string9 = context.getString(R.string.pref_key_target_time);
        KEY_TIME = string9;
        targetPreference9.mKey = string9;
    }

    public static ArrayList<TargetPreference> LOCALISABLE_TARGETS() {
        if (LOCALISABLE_TARGETS.isEmpty()) {
            LOCALISABLE_TARGETS.add(DISTANCE);
            LOCALISABLE_TARGETS.add(AVERAGE_SPEED);
        }
        return LOCALISABLE_TARGETS;
    }

    private TargetPreference(String key) {
        this.mResMin = 0;
        this.mResMax = 0;
        this.mResLen = 0;
        this.mResDefault = 0;
        this.mAllowDecimals = false;
        this.mResUnits = 0;
        this.mKey = key;
    }

    private TargetPreference(String key, int resDefault, int resMin, int resMax, int resLen, int resUnits, boolean allowDecimals) {
        this.mResMin = 0;
        this.mResMax = 0;
        this.mResLen = 0;
        this.mResDefault = 0;
        this.mAllowDecimals = false;
        this.mResUnits = 0;
        this.mKey = key;
        this.mResMin = resMin;
        this.mResMax = resMax;
        this.mResDefault = resDefault;
        this.mResLen = resLen;
        this.mAllowDecimals = allowDecimals;
        this.mResUnits = resUnits;
    }

    public String getKey() {
        return this.mKey;
    }

    public int getResMin() {
        return this.mResMin;
    }

    public int getResMax() {
        return this.mResMax;
    }

    public int getResLen() {
        return this.mResLen;
    }

    public int getResDefault() {
        return this.mResDefault;
    }

    public String getUnitSuffix(Context context) {
        if (this.mKey.matches(KEY_AVG_SPEED)) {
            return Conversion.getUnitOfSpeed(context);
        }
        if (this.mKey.matches(KEY_AVG_PACE)) {
            return Conversion.getUnitOfPace(context);
        }
        if (this.mKey.matches(KEY_DISTANCE)) {
            return Conversion.getUnitOfDistance(context);
        }
        return this.mResUnits == 0 ? "" : context.getString(this.mResUnits).toLowerCase();
    }

    public boolean getAllowDecimals() {
        return this.mAllowDecimals;
    }

    public static TargetPreference getTarget(String label) {
        String targetKey = MetricType.getMetricType(label).getKey();
        if (targetKey == null) {
            return NONE;
        }
        if (AVERAGE_CADENCE.getKey().matches(targetKey)) {
            return AVERAGE_CADENCE;
        }
        if (AVERAGE_HEARTRATE.getKey().matches(targetKey)) {
            return AVERAGE_HEARTRATE;
        }
        if (AVERAGE_POWER.getKey().matches(targetKey)) {
            return AVERAGE_POWER;
        }
        if (AVERAGE_SPEED.getKey().matches(targetKey)) {
            AVERAGE_SPEED.mResMax = Prefs.isMetric() ? R.integer.target_speed_max : R.integer.target_speed_max_mph;
            return AVERAGE_SPEED;
        }
        if (AVERAGE_PACE.getKey().matches(targetKey)) {
            AVERAGE_PACE.mResMax = Prefs.isMetric() ? R.integer.target_pace_max : R.integer.target_pace_max_minpmi;
            return AVERAGE_PACE;
        }
        if (DISTANCE.getKey().matches(targetKey)) {
            DISTANCE.mResMax = Prefs.isMetric() ? R.integer.target_distance_max : R.integer.target_distance_max_miles;
            return DISTANCE;
        }
        if (TIME.getKey().matches(targetKey)) {
            return TIME;
        }
        if (AVERAGE_STEP.getKey().matches(targetKey)) {
            return AVERAGE_STEP;
        }
        if (AVERAGE_KICK.getKey().matches(targetKey)) {
            return AVERAGE_KICK;
        }
        return NONE;
    }
}
