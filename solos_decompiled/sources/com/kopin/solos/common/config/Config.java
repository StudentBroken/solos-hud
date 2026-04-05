package com.kopin.solos.common.config;

/* JADX INFO: loaded from: classes52.dex */
public class Config {
    public static final long FIRMWARE_UPGRADE_REMINDER_TIME = 21600000;
    public static final boolean SHOW_ALL_ROUTES = false;
    public static final boolean SHOW_RIDE_DELETE_DIALOG = false;
    public static final boolean SHOW_WELCOME_PAGE = false;
    public static final boolean USE_CORRECTED_ELEVATION = true;
    static final BuildFlavour DEFAULT = BuildFlavour.DEV;
    public static boolean DEBUG = false;
    public static boolean FAKE_DATA = false;
    public static boolean DEBUG_ALLOW_CONFIG_OVERRIDE = false;
    public static boolean VOCON_ENABLED = false;
    public static boolean SHORT_FTP = false;
    public static long START_DAY_OFFSET = 0;
    public static boolean SHOW_GLASSES_MIRROR = true;
    public static boolean SHOW_FIRMWARE_UPDATE_PROMPT_DIALOG = true;
    public static boolean IS_RELEASE_FIRMWARE_SERVER = true;
    public static boolean FORCE_MULTI_TIME_ELAPSED = true;
    public static boolean REMOTE_CRASH_LOG_ENABLED = true;
    public static boolean REMOTE_EVENT_LOG_ENABLED = true;
    public static boolean MULTI_SPORT_ENABLED = DEBUG;
    public static boolean STATS_CAPPED_TO_MIDNIGHT = false;
    public static final MetricDataType[] METRICS_MASTER = {MetricDataType.CADENCE, MetricDataType.AVG_CADENCE, MetricDataType.CALORIES, MetricDataType.DISTANCE, MetricDataType.ELEVATION, MetricDataType.ELEVATION_CHANGE, MetricDataType.FUNCTIONAL_THRESHOLD_POWER, MetricDataType.HEART_RATE, MetricDataType.HEART_RATE_ZONE, MetricDataType.OVERALL_CLIMB, MetricDataType.OXYGEN, MetricDataType.POWER, MetricDataType.POWER_BAR, MetricDataType.AVG_POWER, MetricDataType.SPEED, MetricDataType.AVG_SPEED, MetricDataType.STRIDE, MetricDataType.PACE, MetricDataType.AVG_PACE, MetricDataType.STEP, MetricDataType.AVG_STEP, MetricDataType.KICK, MetricDataType.AVG_KICK, MetricDataType.KICK_BAR, MetricDataType.TRAINING_STRESS_SCORE, MetricDataType.TIME, MetricDataType.TARGET_AVERAGE_CADENCE, MetricDataType.TARGET_AVERAGE_HEART_RATE, MetricDataType.TARGET_AVERAGE_POWER, MetricDataType.TARGET_AVERAGE_SPEED, MetricDataType.TARGET_AVERAGE_PACE, MetricDataType.TARGET_AVERAGE_STEP, MetricDataType.TARGET_AVERAGE_KICK, MetricDataType.TARGET_DISTANCE, MetricDataType.TARGET_TIME, MetricDataType.INTENSITY_FACTOR, MetricDataType.NORMALISED_POWER, MetricDataType.QUAD_METRIC_OVERVIEW};
    public static final MetricDataType[] METRICS_HEADSET = {MetricDataType.CADENCE, MetricDataType.AVG_CADENCE, MetricDataType.CALORIES, MetricDataType.DISTANCE, MetricDataType.ELEVATION, MetricDataType.ELEVATION_CHANGE, MetricDataType.FUNCTIONAL_THRESHOLD_POWER, MetricDataType.HEART_RATE, MetricDataType.HEART_RATE_ZONE, MetricDataType.OXYGEN, MetricDataType.POWER, MetricDataType.POWER_BAR, MetricDataType.AVG_POWER, MetricDataType.SPEED, MetricDataType.AVG_SPEED, MetricDataType.STRIDE, MetricDataType.PACE, MetricDataType.AVG_PACE, MetricDataType.STEP, MetricDataType.AVG_STEP, MetricDataType.KICK, MetricDataType.AVG_KICK, MetricDataType.KICK_BAR, MetricDataType.TRAINING_STRESS_SCORE, MetricDataType.TIME, MetricDataType.TARGET_AVERAGE_CADENCE, MetricDataType.TARGET_AVERAGE_HEART_RATE, MetricDataType.TARGET_AVERAGE_POWER, MetricDataType.TARGET_AVERAGE_SPEED, MetricDataType.TARGET_AVERAGE_PACE, MetricDataType.TARGET_AVERAGE_STEP, MetricDataType.TARGET_AVERAGE_KICK, MetricDataType.TARGET_DISTANCE, MetricDataType.TARGET_TIME, MetricDataType.INTENSITY_FACTOR, MetricDataType.NORMALISED_POWER, MetricDataType.QUAD_METRIC_OVERVIEW};
    public static final MetricDataType[] METRICS_RIDE_SCREEN = {MetricDataType.CADENCE, MetricDataType.AVG_CADENCE, MetricDataType.CALORIES, MetricDataType.DISTANCE, MetricDataType.ELEVATION, MetricDataType.ELEVATION_CHANGE, MetricDataType.FUNCTIONAL_THRESHOLD_POWER, MetricDataType.HEART_RATE, MetricDataType.HEART_RATE_ZONE, MetricDataType.OXYGEN, MetricDataType.POWER, MetricDataType.POWER_BAR, MetricDataType.AVG_POWER, MetricDataType.SPEED, MetricDataType.AVG_SPEED, MetricDataType.STRIDE, MetricDataType.PACE, MetricDataType.AVG_PACE, MetricDataType.STEP, MetricDataType.AVG_STEP, MetricDataType.KICK, MetricDataType.AVG_KICK, MetricDataType.KICK_BAR, MetricDataType.TRAINING_STRESS_SCORE, MetricDataType.TIME, MetricDataType.TARGET_AVERAGE_CADENCE, MetricDataType.TARGET_AVERAGE_HEART_RATE, MetricDataType.TARGET_AVERAGE_POWER, MetricDataType.TARGET_AVERAGE_SPEED, MetricDataType.TARGET_AVERAGE_PACE, MetricDataType.TARGET_AVERAGE_STEP, MetricDataType.TARGET_AVERAGE_KICK, MetricDataType.TARGET_DISTANCE, MetricDataType.TARGET_TIME, MetricDataType.INTENSITY_FACTOR, MetricDataType.NORMALISED_POWER};

    public enum BuildFlavour {
        RELEASE(false, false, false),
        DEBUG(true, false, true),
        DEMO(false, true, true),
        DEV(true, true, true);

        private final boolean allowConfig;
        private final boolean hasFakeData;
        private final boolean isDebug;

        BuildFlavour(boolean debug, boolean fakeData, boolean config) {
            this.isDebug = debug;
            this.hasFakeData = fakeData;
            this.allowConfig = config;
        }
    }

    public static void init(BuildFlavour flavour) {
        DEBUG = flavour.isDebug;
        DEBUG_ALLOW_CONFIG_OVERRIDE = flavour.allowConfig;
        FAKE_DATA = flavour.hasFakeData;
        VOCON_ENABLED = flavour.isDebug;
        SHOW_FIRMWARE_UPDATE_PROMPT_DIALOG = !flavour.isDebug;
        MULTI_SPORT_ENABLED = flavour.isDebug;
    }
}
