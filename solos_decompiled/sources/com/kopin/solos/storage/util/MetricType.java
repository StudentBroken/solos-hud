package com.kopin.solos.storage.util;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.goldeni.audio.GIAudioNative;
import com.kopin.solos.AppService;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.view.graphics.Bar;

/* JADX INFO: loaded from: classes54.dex */
public enum MetricType {
    NONE,
    HOME,
    HEARTRATE(R.string.vc_title_heartrate),
    SPEED(R.string.speed),
    AVERAGE_SPEED("average_speed", false, "average_speed", R.string.vc_title_average_speed_abbrev),
    CADENCE(R.string.vc_title_cadence),
    TIME,
    TIME_GHOST,
    POWER(R.string.power),
    AVERAGE_POWER("average_power", false, "average_power", R.string.vc_title_average_power),
    ELEVATION,
    ELEVATION_CHANGE,
    DISTANCE(R.string.distance),
    OXYGENATION,
    OXYGENATION_GRAPH,
    SPEED_GRAPH,
    HEARTRATE_GRAPH,
    POWER_BAR,
    POWER_GRAPH,
    LAP_GRAPH,
    ELEVATION_GRAPH,
    CADENCE_GRAPH,
    ELEVATION_LAP_GRAPH,
    CALORIES,
    TARGET_DISTANCE(Prefs.TARGET_DISTANCE, false, Prefs.TARGET_DISTANCE, R.string.vc_title_target_distance),
    AVERAGE_SPEED_GRAPH,
    PAUSED_PAGE(AppService.PAUSED_PAGE),
    TARGET_TIME("countdown_time", false, "target_ride_time1", R.string.vc_title_target_time),
    OVERALL_CLIMB(R.string.vc_title_overall_climb),
    INTENSITY_FACTOR(R.string.vc_title_intensity_factor),
    POWER_NORMALISED(R.string.vc_title_normalised_power),
    HEARTRATE_ZONES("heart_rate_zones"),
    GHOST_AVERAGE_CADENCE(R.string.vc_title_average_cadence),
    GHOST_AVERAGE_HEARTRATE(R.string.vc_title_average_heartrate),
    GHOST_AVERAGE_SPEED(R.string.vc_title_average_speed_abbrev),
    GHOST_AVERAGE_POWER(R.string.vc_title_average_power),
    GHOST_AVERAGE_OXYGENATION,
    AVERAGE_TARGET_SPEED("speed_average_target", true, Prefs.TARGET_AVERAGE_SPEED_KEY, R.string.vc_title_average_speed_abbrev),
    AVERAGE_TARGET_HEARTRATE("heartrate_average_target", true, Prefs.TARGET_AVERAGE_HEARTRATE_KEY, R.string.vc_title_average_heartrate),
    AVERAGE_TARGET_POWER("power_average_target", true, Prefs.TARGET_AVERAGE_POWER_KEY, R.string.vc_title_average_power),
    AVERAGE_TARGET_CADENCE("cadence_average_target", true, Prefs.TARGET_AVERAGE_CADENCE_KEY, R.string.vc_title_average_cadence),
    COUNTDOWN,
    STRIDE(R.string.stride),
    GHOST_AVERAGE_STRIDE,
    AVERAGE_PACE_GRAPH,
    PACE(R.string.pace),
    PACE_GRAPH,
    GHOST_AVERAGE_PACE(R.string.vc_title_average_pace),
    AVERAGE_PACE("average_pace", false, "average_pace", R.string.vc_title_average_pace),
    AVERAGE_TARGET_PACE("pace_average_target", true, Prefs.TARGET_AVERAGE_PACE_KEY, R.string.vc_title_average_pace),
    STEP(R.string.vc_title_cadence),
    STEP_GRAPH,
    GHOST_AVERAGE_STEP(R.string.vc_title_average_cadence),
    AVERAGE_STEP("average_step", false, "average_step", R.string.vc_title_average_cadence),
    AVERAGE_TARGET_STEP("step_average_target", true, Prefs.TARGET_AVERAGE_STEP_KEY, R.string.vc_title_average_cadence),
    KICK(R.string.power),
    AVERAGE_KICK("average_kick", false, "average_kick", R.string.vc_title_average_power),
    KICK_BAR,
    KICK_GRAPH,
    GHOST_AVERAGE_KICK(R.string.vc_title_average_power),
    AVERAGE_TARGET_KICK("kick_average_target", true, Prefs.TARGET_AVERAGE_KICK_KEY, R.string.vc_title_average_power),
    NAVIGATION,
    NAVIGATION_NO_COMPASS,
    COMPASS,
    NAVIGATION_DESTINATION_REACHED,
    NAVIGATION_WITH_MAP("navigation_map"),
    RIDE_STATS,
    GHOST_STATS,
    FWFLASH,
    PERCEIVED_EXERTION_RATING,
    TRAINING_STEP,
    TRAINING_STEP_2,
    TRAINING_MANUAL_STEP,
    TRAINING_MANUAL_STEP_2,
    TRAINING_NEXT_STEP,
    METRIC_OVERVIEW("quad_metric", false, "switch_metric_overview", R.string.vc_title_metric_overview);

    private String key;
    private String resource;
    private int stringResourceName;
    private boolean target;

    MetricType() {
        this.target = false;
        this.key = null;
        this.stringResourceName = 0;
        this.resource = toString().toLowerCase();
    }

    MetricType(String res) {
        this.target = false;
        this.key = null;
        this.stringResourceName = 0;
        this.resource = res;
    }

    MetricType(int stringRes) {
        this.target = false;
        this.key = null;
        this.stringResourceName = 0;
        this.resource = toString().toLowerCase();
        this.stringResourceName = stringRes;
    }

    MetricType(String res, boolean target, String prefKey, int stringRes) {
        this.target = false;
        this.key = null;
        this.stringResourceName = 0;
        this.resource = res;
        this.target = target;
        this.key = prefKey;
        this.stringResourceName = stringRes;
    }

    public String getResource() {
        return this.resource;
    }

    public boolean isTarget() {
        return this.target;
    }

    public String getKey() {
        return this.key;
    }

    public int getStringResourceName() {
        return this.stringResourceName;
    }

    public String getStringResourceName(Context context) {
        return this.stringResourceName > 0 ? context.getString(this.stringResourceName) : name();
    }

    public String getMetricName(Context context) {
        int resId = context.getResources().getIdentifier(this.resource, "string", context.getPackageName());
        if (resId > 0) {
            return context.getString(resId);
        }
        Log.d("MetricType", "Resource " + this.resource + "NOT Found");
        return null;
    }

    public static MetricType getMetricType(String name) {
        MetricType[] metricTypeArrValues = values();
        int length = metricTypeArrValues.length;
        for (int i = 0; i < length; i++) {
            MetricType dt = metricTypeArrValues[i];
            if (name.equalsIgnoreCase(dt.name()) || name.equalsIgnoreCase(dt.getResource())) {
                return dt;
            }
        }
        return NONE;
    }

    public MetricType getBaseMetricType() {
        switch (this) {
            case AVERAGE_TARGET_HEARTRATE:
            case GHOST_AVERAGE_HEARTRATE:
                return HEARTRATE;
            case AVERAGE_TARGET_CADENCE:
            case GHOST_AVERAGE_CADENCE:
                return CADENCE;
            case AVERAGE_TARGET_POWER:
            case AVERAGE_POWER:
            case POWER_NORMALISED:
            case GHOST_AVERAGE_POWER:
                return POWER;
            case AVERAGE_TARGET_SPEED:
            case AVERAGE_SPEED:
            case GHOST_AVERAGE_SPEED:
                return SPEED;
            case TARGET_TIME:
                return TIME;
            case OVERALL_CLIMB:
                return ELEVATION;
            case ELEVATION_CHANGE:
                return ELEVATION;
            case AVERAGE_PACE:
            case AVERAGE_TARGET_PACE:
            case GHOST_AVERAGE_PACE:
                return PACE;
            case AVERAGE_STEP:
            case AVERAGE_TARGET_STEP:
            case GHOST_AVERAGE_STEP:
                return STEP;
            case AVERAGE_KICK:
            case AVERAGE_TARGET_KICK:
            case GHOST_AVERAGE_KICK:
                return KICK;
            case GHOST_AVERAGE_STRIDE:
                return STRIDE;
            default:
                return this;
        }
    }

    public MetricType getTargetAverageType() {
        switch (AnonymousClass1.$SwitchMap$com$kopin$solos$storage$util$MetricType[ordinal()]) {
            case 6:
            case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
                return AVERAGE_TARGET_POWER;
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            default:
                return this;
            case 10:
            case 25:
                return AVERAGE_TARGET_SPEED;
            case 15:
            case 26:
                return AVERAGE_TARGET_PACE;
            case 21:
            case 30:
                return AVERAGE_TARGET_KICK;
            case 27:
                return AVERAGE_TARGET_CADENCE;
            case 28:
                return AVERAGE_TARGET_STEP;
            case 31:
                return AVERAGE_TARGET_HEARTRATE;
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.storage.util.MetricType$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$common$config$MetricDataType = new int[MetricDataType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.DISTANCE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.ELEVATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.ELEVATION_CHANGE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.OVERALL_CLIMB.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.CALORIES.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.HEART_RATE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.HEART_RATE_ZONE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.CADENCE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_CADENCE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.POWER.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_POWER.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.SPEED.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_SPEED.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.OXYGEN.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TIME.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.POWER_BAR.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.INTENSITY_FACTOR.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.NORMALISED_POWER.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_CADENCE.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_HEART_RATE.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_PACE.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_POWER.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_SPEED.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_DISTANCE.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_TIME.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.STRIDE.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.PACE.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_PACE.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.STEP.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_STEP.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_STEP.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.KICK.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.AVG_KICK.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.TARGET_AVERAGE_KICK.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.KICK_BAR.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$config$MetricDataType[MetricDataType.QUAD_METRIC_OVERVIEW.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            $SwitchMap$com$kopin$solos$storage$util$MetricType = new int[MetricType.values().length];
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_HEARTRATE.ordinal()] = 1;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_HEARTRATE.ordinal()] = 2;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_CADENCE.ordinal()] = 3;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_CADENCE.ordinal()] = 4;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_POWER.ordinal()] = 5;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_POWER.ordinal()] = 6;
            } catch (NoSuchFieldError e42) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_NORMALISED.ordinal()] = 7;
            } catch (NoSuchFieldError e43) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_POWER.ordinal()] = 8;
            } catch (NoSuchFieldError e44) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_SPEED.ordinal()] = 9;
            } catch (NoSuchFieldError e45) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED.ordinal()] = 10;
            } catch (NoSuchFieldError e46) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_SPEED.ordinal()] = 11;
            } catch (NoSuchFieldError e47) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.TARGET_TIME.ordinal()] = 12;
            } catch (NoSuchFieldError e48) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.OVERALL_CLIMB.ordinal()] = 13;
            } catch (NoSuchFieldError e49) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_CHANGE.ordinal()] = 14;
            } catch (NoSuchFieldError e50) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_PACE.ordinal()] = 15;
            } catch (NoSuchFieldError e51) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_PACE.ordinal()] = 16;
            } catch (NoSuchFieldError e52) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_PACE.ordinal()] = 17;
            } catch (NoSuchFieldError e53) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_STEP.ordinal()] = 18;
            } catch (NoSuchFieldError e54) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_STEP.ordinal()] = 19;
            } catch (NoSuchFieldError e55) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_STEP.ordinal()] = 20;
            } catch (NoSuchFieldError e56) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_KICK.ordinal()] = 21;
            } catch (NoSuchFieldError e57) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_KICK.ordinal()] = 22;
            } catch (NoSuchFieldError e58) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_KICK.ordinal()] = 23;
            } catch (NoSuchFieldError e59) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_STRIDE.ordinal()] = 24;
            } catch (NoSuchFieldError e60) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED.ordinal()] = 25;
            } catch (NoSuchFieldError e61) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE.ordinal()] = 26;
            } catch (NoSuchFieldError e62) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE.ordinal()] = 27;
            } catch (NoSuchFieldError e63) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP.ordinal()] = 28;
            } catch (NoSuchFieldError e64) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER.ordinal()] = 29;
            } catch (NoSuchFieldError e65) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK.ordinal()] = 30;
            } catch (NoSuchFieldError e66) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE.ordinal()] = 31;
            } catch (NoSuchFieldError e67) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE_GRAPH.ordinal()] = 32;
            } catch (NoSuchFieldError e68) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION.ordinal()] = 33;
            } catch (NoSuchFieldError e69) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_LAP_GRAPH.ordinal()] = 34;
            } catch (NoSuchFieldError e70) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_GRAPH.ordinal()] = 35;
            } catch (NoSuchFieldError e71) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_GRAPH.ordinal()] = 36;
            } catch (NoSuchFieldError e72) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_ZONES.ordinal()] = 37;
            } catch (NoSuchFieldError e73) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.OXYGENATION.ordinal()] = 38;
            } catch (NoSuchFieldError e74) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.OXYGENATION_GRAPH.ordinal()] = 39;
            } catch (NoSuchFieldError e75) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_OXYGENATION.ordinal()] = 40;
            } catch (NoSuchFieldError e76) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE_GRAPH.ordinal()] = 41;
            } catch (NoSuchFieldError e77) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_PACE_GRAPH.ordinal()] = 42;
            } catch (NoSuchFieldError e78) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_BAR.ordinal()] = 43;
            } catch (NoSuchFieldError e79) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_GRAPH.ordinal()] = 44;
            } catch (NoSuchFieldError e80) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED_GRAPH.ordinal()] = 45;
            } catch (NoSuchFieldError e81) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED_GRAPH.ordinal()] = 46;
            } catch (NoSuchFieldError e82) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP_GRAPH.ordinal()] = 47;
            } catch (NoSuchFieldError e83) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STRIDE.ordinal()] = 48;
            } catch (NoSuchFieldError e84) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_BAR.ordinal()] = 49;
            } catch (NoSuchFieldError e85) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_GRAPH.ordinal()] = 50;
            } catch (NoSuchFieldError e86) {
            }
        }
    }

    public static MetricType getFromMetricDataType(MetricDataType metricDataType) {
        switch (AnonymousClass1.$SwitchMap$com$kopin$solos$common$config$MetricDataType[metricDataType.ordinal()]) {
            case 1:
                return DISTANCE;
            case 2:
                return ELEVATION;
            case 3:
                return ELEVATION_CHANGE;
            case 4:
                return OVERALL_CLIMB;
            case 5:
                return CALORIES;
            case 6:
                return HEARTRATE;
            case 7:
                return HEARTRATE_ZONES;
            case 8:
            case 9:
                return CADENCE;
            case 10:
                return POWER;
            case 11:
                return AVERAGE_POWER;
            case 12:
                return SPEED;
            case 13:
                return AVERAGE_SPEED;
            case 14:
                return OXYGENATION;
            case 15:
                return TIME;
            case 16:
                return POWER_BAR;
            case 17:
                return INTENSITY_FACTOR;
            case 18:
                return POWER_NORMALISED;
            case 19:
                return AVERAGE_TARGET_CADENCE;
            case 20:
                return AVERAGE_TARGET_HEARTRATE;
            case 21:
                return AVERAGE_TARGET_PACE;
            case 22:
                return AVERAGE_TARGET_POWER;
            case 23:
                return AVERAGE_TARGET_SPEED;
            case 24:
                return TARGET_DISTANCE;
            case 25:
                return TARGET_TIME;
            case 26:
                return STRIDE;
            case 27:
                return PACE;
            case 28:
                return AVERAGE_PACE;
            case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
                return STEP;
            case 30:
                return AVERAGE_STEP;
            case 31:
                return AVERAGE_TARGET_STEP;
            case 32:
                return KICK;
            case MotionEventCompat.AXIS_GENERIC_2 /* 33 */:
                return AVERAGE_KICK;
            case 34:
                return AVERAGE_TARGET_KICK;
            case MotionEventCompat.AXIS_GENERIC_4 /* 35 */:
                return KICK_BAR;
            case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
                return METRIC_OVERVIEW;
            default:
                return null;
        }
    }

    public Sensor.DataType getSensorType() {
        switch (AnonymousClass1.$SwitchMap$com$kopin$solos$storage$util$MetricType[ordinal()]) {
            case 1:
            case 2:
            case 31:
            case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
            case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                return Sensor.DataType.HEARTRATE;
            case 3:
            case 4:
            case 27:
            case 32:
                return Sensor.DataType.CADENCE;
            case 5:
            case 6:
            case 8:
            case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
            case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
            case 44:
                return Sensor.DataType.POWER;
            case 7:
            case 12:
            case 13:
            case 24:
            default:
                return Sensor.DataType.UNKOWN;
            case 9:
            case 10:
            case 11:
            case 25:
            case MotionEventCompat.AXIS_GENERIC_14 /* 45 */:
            case MotionEventCompat.AXIS_GENERIC_15 /* 46 */:
                return Sensor.DataType.SPEED;
            case 14:
            case MotionEventCompat.AXIS_GENERIC_2 /* 33 */:
            case 34:
            case MotionEventCompat.AXIS_GENERIC_4 /* 35 */:
                return Sensor.DataType.ALTITUDE;
            case 15:
            case 16:
            case 17:
            case 26:
            case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                return Sensor.DataType.PACE;
            case 18:
            case 19:
            case 20:
            case 28:
            case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                return Sensor.DataType.STEP;
            case 21:
            case 22:
            case 23:
            case 30:
            case 49:
            case 50:
                return Sensor.DataType.KICK;
            case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
            case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
            case 40:
                return Sensor.DataType.OXYGEN;
            case Bar.DEFAULT_HEIGHT /* 48 */:
                return Sensor.DataType.STRIDE;
        }
    }
}
