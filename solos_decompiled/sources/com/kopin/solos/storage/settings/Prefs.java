package com.kopin.solos.storage.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.preference.TwoStatePreference;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes54.dex */
public class Prefs {
    public static final String AUTO_PAUSE_KEY = "auto_pause_control";
    public static final String AUTO_PAUSE_SPEED_KEY = "pause_control_speed";
    private static final String AUTO_PAUSE_SPEED_KEY_MPS = "pause_control_speed_actual";
    public static final String DEBUG_NAVIGATE_LONG_REMINDER = "navigate_long_reminder";
    public static final String DEBUG_NAVIGATE_SHORT_REMINDER = "navigate_short_reminder";
    private static final String DEFAULT_RIDE_TYPE_INDEX = "4";
    private static final String GR_SCREENS_KEY = "gr_screens_setting";
    private static final String GR_SCREENS_RUN_KEY = "gr_screens_setting_run";
    public static final String HEADSET_BRIGHTNESS = "headset_brightness_setting";
    private static final String HEADSET_PAIRED_CACHE_KEY = "headset_paired_cache";
    private static final String HEADSET_PAIRED_KEY = "headset_paired";
    private static final String HEADSET_PAIRED_MODEL_KEY = "headset_model";
    public static final String HEADSET_SLEEP = "headset_sleep_setting";
    public static final String HEADSET_VOLUME = "headset_volume_setting";
    public static final String LAP_MODE_SETTING = "lap_mode_setting";
    public static final String LAP_TIME = "lap_time";
    public static final int MAX_NUM_MULTI_METRICS = 3;
    private static final String MODE_AUTO = "a";
    public static final String MODE_DISTANCE = "d";
    public static final String MODE_KEY = "mode_setting";
    public static final String MODE_MANUAL = "m";
    public static final String MODE_TIME = "t";
    public static final String MULTI_KEY = "multi_display";
    public static final String NAVIGATE_DISPLAY_COMPASS = "navigate_display_compass";
    public static final String NAVIGATE_FULL_SCREEN = "navigate_full_screen";
    public static final String PAGE_DISPLAY_MODE_KEY = "mode_single_multi";
    private static final String PREF_DEFAULT_BIKE = "pref_default_bike";
    private static final String PREF_DEFAULT_RIDE = "pref_default_ride";
    public static final String PREF_NAVIGATE = "pref_navigation_settings";
    private static final String PREF_SETUP_KEY = "setup";
    private static final String PREF_SETUP_KEY_LOGGED_IN = "setup_logged_in";
    public static final String ROLLING_AVERAGE_OFF = "0";
    private static final String ROLLING_AVERAGE_ON = "3";
    private static final int SCREEN_TIMER_DEFAULT_SECONDS = 7;
    public static final String SPLIT_UNIT = "lap_unit_setting";
    private static final String TAG = "Prefs";
    public static final String TARGET_AVERAGE_CADENCE_KEY = "target_average_cadence";
    public static final String TARGET_AVERAGE_HEARTRATE_KEY = "target_average_heartrate";
    public static final String TARGET_AVERAGE_KICK_KEY = "target_average_kick";
    public static final String TARGET_AVERAGE_PACE_KEY = "target_average_pace";
    public static final String TARGET_AVERAGE_POWER_KEY = "target_average_power";
    public static final String TARGET_AVERAGE_SPEED_KEY = "target_average_speed";
    public static final String TARGET_AVERAGE_STEP_KEY = "target_average_step";
    public static final String TARGET_DISTANCE = "target_distance";
    private static final String TARGET_RIDE_TIME_KEY = "target_ride_time1";
    public static final String TIMER_KEY = "timer_setting";
    public static final String TTS_KEY = "tts_switch";
    public static final String TTS_TIMER_KEY = "tts_timer";
    public static final String UNIT_ELEVATION = "e";
    public static final String UNIT_SPEED = "s";
    private static final String WHEEL_SIZE_KEY = "size_setting";
    private static Context mContext;
    private static SharedPreferences preferences;
    private static String MULTI_SCREEN_FORMAT = "multi_display_screen_%d";
    private static String MULTI_SCREEN_FORMAT_RUN = "multi_display_screen_%d_run";
    private static String SCREEN_FORMAT = "screen_%d_option_%d";
    private static String SCREEN_FORMAT_RUN = "screen_%d_option_%d_run";
    private static final List<MetricType> mSingleMetricChoices = new ArrayList();
    private static final List<MetricType> mSingleMetricGhostChoices = new ArrayList();
    private static final List<String> prefKeyList = new ArrayList();
    private static final String[] metricSwitchKeys = {"switch_average_pace", "switch_average_speed", "switch_calories", "switch_distance", "switch_elevation_change", "switch_heart_rate_zones", "switch_intensity_factor", "switch_normalised_power", "switch_overall_climb", "switch_power_bar", "page_target_distance", "switch_stride", "switch_kick_bar"};
    private static final MetricType[] metricSwitchValues = {MetricType.AVERAGE_PACE_GRAPH, MetricType.AVERAGE_SPEED_GRAPH, MetricType.CALORIES, MetricType.DISTANCE, MetricType.ELEVATION_CHANGE, MetricType.HEARTRATE_ZONES, MetricType.INTENSITY_FACTOR, MetricType.POWER_NORMALISED, MetricType.OVERALL_CLIMB, MetricType.POWER_BAR, MetricType.TARGET_DISTANCE, MetricType.STRIDE, MetricType.KICK_BAR};

    private enum SCREEN_STATE {
        AUTOMATIC,
        AUTO_MANUAL,
        MANUAL
    }

    public enum UnitSystem {
        METRIC,
        IMPERIAL,
        NONE;

        public static UnitSystem getUnit(String name) {
            if (METRIC.name().equalsIgnoreCase(name)) {
                return METRIC;
            }
            if (IMPERIAL.name().equalsIgnoreCase(name)) {
                return IMPERIAL;
            }
            return NONE;
        }
    }

    public enum TargetAveNotificationVerbosity {
        OFF(0),
        LOW(300),
        LOWER_MEDIUM(180),
        MEDIUM(TwitterApiErrorConstants.EMAIL_ALREADY_REGISTERED),
        UPPER_MEDIUM(60),
        HIGH(30);

        int verbosityInSeconds;

        TargetAveNotificationVerbosity(int seconds) {
            this.verbosityInSeconds = seconds;
        }

        public int getVerbosityInSeconds() {
            return this.verbosityInSeconds;
        }

        public boolean isVerbosityOff() {
            return this.verbosityInSeconds == 0;
        }
    }

    public static void init(Context context) {
        Log.d(TAG, "init");
        mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        TargetPreference.init(context);
        ConfigPrefs.init(context);
        double val = PrefItem.TARGET_AVERAGE_SPEED_MPS_VALUE.getDouble();
        if (val == -2.147483648E9d || val <= 0.0d) {
            setTargetAverageSpeedValue(Conversion.speedFromLocale(getTargetAverageSpeed()));
        }
        double val2 = PrefItem.TARGET_AVERAGE_PACE_VALUE.getDouble();
        if (val2 == -2.147483648E9d || val2 <= 0.0d) {
            setTargetAveragePaceValue(Conversion.millisPerKmToSecondsPerMetre(getTargetAveragePaceKm()));
        }
        PrefItem.TARGET_AVERAGE_PACE.apply(Conversion.secondsPerMetreToMillisPerKm(getTargetAveragePaceValue()));
    }

    public static void initUnitSystem() {
        UnitSystem unitSystem = UnitSystem.METRIC;
        if (Locale.getDefault().getCountry().equalsIgnoreCase(Locale.US.getCountry())) {
            unitSystem = UnitSystem.IMPERIAL;
        }
        PrefItem.UNIT_SYSTEM.commit(unitSystem.name());
        Utility.setUnitSystem(unitSystem);
        setTargetAverageSpeedValue(Conversion.speedFromLocale(getTargetAverageSpeed()));
        setTargetAveragePaceValue(Conversion.millisPerKmToSecondsPerMetre(getTargetAveragePaceKm()));
        PrefItem.TARGET_AVERAGE_PACE.apply(Conversion.secondsPerMetreToMillisPerKm(getTargetAveragePaceValue()));
    }

    public static boolean getSetupComplete() {
        SportType sportType = getSportType();
        return getSetupCompleteSet().contains(sportType.name());
    }

    public static boolean getSetupComplete(SportType sportType) {
        return getSetupCompleteSet().contains(sportType.name());
    }

    public static Set<String> getSetupCompleteSet() {
        Set<String> sports = PrefItem.SETUP_COMPLETE_SPORTSET.getStrings();
        if (sports == null || sports.size() == 0) {
            sports = new HashSet<>();
            if (PrefItem.SETUP_COMPLETE.get()) {
                sports.add(SportType.RIDE.name());
                Log.d(TAG, "setup not completed for sports types yet, so add Ride for legacy setup pref");
                PrefItem.SETUP_COMPLETE_SPORTSET.apply(sports);
            }
        }
        Log.d(TAG, "getSetupCompleteSet, sports set size " + sports.size());
        return sports;
    }

    public static void setSetupComplete(boolean complete) {
        SportType sportType = getSportType();
        HashSet<String> sports = new HashSet<>();
        sports.addAll(getSetupCompleteSet());
        if (complete) {
            sports.add(sportType.name());
        } else {
            sports.remove(sportType.name());
        }
        Log.d(TAG, "setSetupComplete, sports set size " + sports.size());
        PrefItem.SETUP_COMPLETE_SPORTSET.apply(sports);
    }

    public static SportType getSportType() {
        Log.d(TAG, "getSportType()");
        return SportType.get(preferences.getString(SportType.KEY, SportType.DEFAULT_TYPE.name()));
    }

    public static void setSportType(SportType sportType) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SportType.KEY, sportType.name()).apply();
    }

    static String getDOB(boolean monthBase1) {
        Calendar cal = Calendar.getInstance();
        cal.add(1, -25);
        String dobDef = "" + cal.get(1) + mContext.getString(R.string.dob_default_short);
        String dob = preferences.getString(mContext.getString(R.string.pref_key_user_dob), dobDef);
        if (dob.trim().isEmpty()) {
            dob = dobDef;
        }
        if (monthBase1) {
            String[] parts = dob.split(",");
            int month = Integer.parseInt(parts[1]);
            return parts[0] + "," + (month + 1) + "," + parts[2];
        }
        return dob;
    }

    static void setDOB(int year, int month, int day, boolean monthBase1) {
        if (year >= 1880) {
            SharedPreferences.Editor editorEdit = preferences.edit();
            String string = mContext.getString(R.string.pref_key_user_dob);
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            objArr[0] = Integer.valueOf(year);
            if (monthBase1) {
                month--;
            }
            objArr[1] = Integer.valueOf(month);
            objArr[2] = Integer.valueOf(day);
            editorEdit.putString(string, String.format(locale, "%d,%d,%d", objArr)).apply();
        }
    }

    static void setGender(UserProfile.Gender gender) {
        PrefItem.GENDER.apply(gender.name());
    }

    static UserProfile.Gender getGender() {
        return UserProfile.Gender.getGender(PrefItem.GENDER.getString());
    }

    static void setName(String name) {
        if (name != null && name.trim().length() > 0) {
            preferences.edit().putString(mContext.getString(R.string.pref_key_user_name), name).apply();
        }
    }

    static String getName(int nameResDefault) {
        return preferences.getString(mContext.getString(R.string.pref_key_user_name), nameResDefault > 0 ? mContext.getString(nameResDefault) : "");
    }

    static double getWeightKG() {
        String sizeString = PrefItem.USER_WEIGHT_KG.getString();
        if (sizeString.trim().isEmpty()) {
            sizeString = "" + mContext.getResources().getInteger(R.integer.weight_default);
        }
        return Utility.doubleFromString(sizeString, mContext.getResources().getInteger(R.integer.weight_default));
    }

    static double getWeightPounds() {
        return Utility.kilogramsToPounds(getWeightKG());
    }

    static double getWeight() {
        return isMetric() ? getWeightKG() : getWeightPounds();
    }

    static void setWeight(String weight) {
        setWeight(weight, isMetric());
    }

    static void setWeight(String weight, boolean kg) {
        if (weight != null && !weight.isEmpty() && UserProfile.isValidWeight(weight)) {
            if (!kg) {
                Double pounds = Double.valueOf(Double.parseDouble(weight));
                weight = "" + Utility.poundsToKilograms(pounds.doubleValue());
            }
            PrefItem.USER_WEIGHT_KG.apply(weight);
        }
    }

    static void setWeightKG(double weightKG) {
        if (UserProfile.isValidWeight(weightKG)) {
            PrefItem.USER_WEIGHT_KG.apply(String.valueOf(weightKG));
        }
    }

    public static UnitSystem getUnitSystem() {
        return UnitSystem.getUnit(PrefItem.UNIT_SYSTEM.getString());
    }

    public static boolean isMetric() {
        return getUnitSystem() == UnitSystem.METRIC;
    }

    public static long getTargetTime() {
        return preferences.getLong(mContext.getString(R.string.pref_key_target_time), 3600000L);
    }

    public static void setTargetTime(long target) {
        if (target >= 0) {
            preferences.edit().putLong(mContext.getString(R.string.pref_key_target_time), target).commit();
        }
    }

    public static List<String> getSingleMetricPrefKeys() {
        if (prefKeyList.size() == 0) {
            prefKeyList.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.switch_pref_single_metrics_all)));
        }
        return prefKeyList;
    }

    private static TwoStatePreference getPref(List<TwoStatePreference> prefs, String key) {
        for (TwoStatePreference tsPref : prefs) {
            if (key.equals(tsPref.getKey())) {
                return tsPref;
            }
        }
        return null;
    }

    public static List<MetricType> getSingleMetricChoices() {
        synchronized (mSingleMetricChoices) {
            mSingleMetricChoices.clear();
            List<String> keys = getSingleMetricPrefKeys();
            List<String> chosenKeys = new ArrayList<>();
            for (String key : keys) {
                if (preferences.getBoolean(key, false)) {
                    chosenKeys.add(key);
                }
            }
            for (String key2 : chosenKeys) {
                int index = Arrays.asList(metricSwitchKeys).indexOf(key2);
                if (index >= 0) {
                    mSingleMetricChoices.add(metricSwitchValues[index]);
                } else {
                    switch (key2) {
                        case "switch_cadence":
                            addSingleMetric(chosenKeys.contains("pref_dual_cadence") ? MetricType.CADENCE : MetricType.CADENCE_GRAPH);
                            break;
                        case "switch_elevation":
                            addSingleMetric(chosenKeys.contains("pref_dual_elevation") ? MetricType.ELEVATION : MetricType.ELEVATION_GRAPH);
                            break;
                        case "switch_heart_rate":
                            addSingleMetric(chosenKeys.contains("pref_dual_heart_rate") ? MetricType.HEARTRATE : MetricType.HEARTRATE_GRAPH);
                            break;
                        case "switch_power":
                            addSingleMetric(chosenKeys.contains("pref_dual_power") ? MetricType.POWER : MetricType.POWER_GRAPH);
                            break;
                        case "switch_speed":
                            addSingleMetric(chosenKeys.contains("pref_dual_speed") ? MetricType.SPEED : MetricType.SPEED_GRAPH);
                            break;
                        case "switch_oxygen":
                            addSingleMetric(chosenKeys.contains("pref_dual_oxygen") ? MetricType.OXYGENATION : MetricType.OXYGENATION_GRAPH);
                            break;
                        case "switch_pace":
                            addSingleMetric(chosenKeys.contains("pref_dual_pace") ? MetricType.PACE : MetricType.PACE_GRAPH);
                            break;
                        case "switch_step":
                            addSingleMetric(chosenKeys.contains("pref_dual_step") ? MetricType.STEP : MetricType.STEP_GRAPH);
                            break;
                        case "switch_kick":
                            addSingleMetric(chosenKeys.contains("pref_dual_kick") ? MetricType.KICK : MetricType.KICK_GRAPH);
                            break;
                        case "switch_metric_overview":
                            addSingleMetric(MetricType.METRIC_OVERVIEW);
                            break;
                        default:
                            MetricType metricType = MetricType.getMetricType(key2);
                            if (metricType != MetricType.NONE) {
                                addSingleMetric(metricType);
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                }
            }
            List<MetricType> list = ConfigMetrics.getMetricTypeList(ConfigMetrics.getHeadsetList());
            mSingleMetricChoices.retainAll(list);
        }
        return mSingleMetricChoices;
    }

    public static void setSingleMetrics(List<MetricType> metrics) {
    }

    private static void addSingleMetric(MetricType metricType) {
        if (!mSingleMetricChoices.contains(metricType)) {
            mSingleMetricChoices.add(metricType);
        }
    }

    public static void initSingleMetrics() {
        if (isFirstRun()) {
            String[] defaultKeys = mContext.getResources().getStringArray(R.array.metric_keys_switch_on_defaults);
            for (String key : defaultKeys) {
                preferences.edit().putBoolean(key, true).commit();
            }
        }
    }

    public static void initMultiMetrics() {
        if (Config.FORCE_MULTI_TIME_ELAPSED) {
            for (SportType sportType : SportType.values()) {
                preferences.edit().putBoolean(getMultiScreenKey(1, sportType), true).putString(getMultiScreenOptionKey(1, 1, sportType), "time").apply();
            }
        }
    }

    public static String getMultiScreenKey(int index) {
        return getMultiScreenKey(index, LiveRide.getCurrentSport());
    }

    public static String getMultiScreenKey(int index, SportType sportType) {
        switch (sportType) {
            case RUN:
                return String.format(Locale.US, MULTI_SCREEN_FORMAT_RUN, Integer.valueOf(index));
            case RIDE:
                return String.format(Locale.US, MULTI_SCREEN_FORMAT, Integer.valueOf(index));
            default:
                return String.format(Locale.US, MULTI_SCREEN_FORMAT, Integer.valueOf(index));
        }
    }

    public static String getMultiScreenOptionKey(int pageIndex, int metricIndex) {
        return getMultiScreenOptionKey(pageIndex, metricIndex, LiveRide.getCurrentSport());
    }

    public static String getMultiScreenOptionKey(int pageIndex, int metricIndex, SportType sportType) {
        String format;
        switch (sportType) {
            case RUN:
                format = SCREEN_FORMAT_RUN;
                break;
            case RIDE:
                format = SCREEN_FORMAT;
                break;
            default:
                format = SCREEN_FORMAT;
                break;
        }
        return String.format(Locale.US, format, Integer.valueOf(pageIndex), Integer.valueOf(metricIndex));
    }

    private static boolean isFirstRun() {
        boolean firstRun = PrefItem.FIRST_RUN.get();
        if (firstRun) {
            PrefItem.FIRST_RUN.commit(false);
        }
        return firstRun;
    }

    public static List<MetricType> getSingleMetricGhostChoices() {
        setSingleMetricGhostChoices();
        return mSingleMetricGhostChoices;
    }

    public static String getGhostMetricKey() {
        switch (getSportType()) {
            case RUN:
                return GR_SCREENS_RUN_KEY;
            default:
                return GR_SCREENS_KEY;
        }
    }

    public static void setSingleMetricChoices(List<TwoStatePreference> prefs) {
        synchronized (mSingleMetricChoices) {
            mSingleMetricChoices.clear();
            for (TwoStatePreference tsPref : prefs) {
                if (tsPref.isChecked()) {
                    int index = Arrays.asList(metricSwitchKeys).indexOf(tsPref.getKey());
                    if (index >= 0) {
                        mSingleMetricChoices.add(metricSwitchValues[index]);
                    } else {
                        switch (tsPref.getKey()) {
                            case "switch_cadence":
                                TwoStatePreference tsp = getPref(prefs, "pref_dual_cadence");
                                mSingleMetricChoices.add((tsp == null || !tsp.isChecked()) ? MetricType.CADENCE_GRAPH : MetricType.CADENCE);
                                break;
                            case "switch_elevation":
                                TwoStatePreference tsp2 = getPref(prefs, "pref_dual_elevation");
                                mSingleMetricChoices.add((tsp2 == null || !tsp2.isChecked()) ? MetricType.ELEVATION_GRAPH : MetricType.ELEVATION);
                                break;
                            case "switch_heart_rate":
                                TwoStatePreference tsp3 = getPref(prefs, "pref_dual_heart_rate");
                                mSingleMetricChoices.add((tsp3 == null || !tsp3.isChecked()) ? MetricType.HEARTRATE_GRAPH : MetricType.HEARTRATE);
                                break;
                            case "switch_power":
                                TwoStatePreference tsp4 = getPref(prefs, "pref_dual_power");
                                mSingleMetricChoices.add((tsp4 == null || !tsp4.isChecked()) ? MetricType.POWER_GRAPH : MetricType.POWER);
                                break;
                            case "switch_speed":
                                TwoStatePreference tsp5 = getPref(prefs, "pref_dual_speed");
                                mSingleMetricChoices.add((tsp5 == null || !tsp5.isChecked()) ? MetricType.SPEED_GRAPH : MetricType.SPEED);
                                break;
                            case "switch_oxygen":
                                TwoStatePreference tsp6 = getPref(prefs, "pref_dual_oxygen");
                                mSingleMetricChoices.add((tsp6 == null || !tsp6.isChecked()) ? MetricType.OXYGENATION_GRAPH : MetricType.OXYGENATION);
                                break;
                            case "switch_pace":
                                TwoStatePreference tsp7 = getPref(prefs, "pref_dual_pace");
                                mSingleMetricChoices.add((tsp7 == null || !tsp7.isChecked()) ? MetricType.PACE_GRAPH : MetricType.PACE);
                                break;
                            case "switch_step":
                                TwoStatePreference tsp8 = getPref(prefs, "pref_dual_step");
                                mSingleMetricChoices.add((tsp8 == null || !tsp8.isChecked()) ? MetricType.STEP_GRAPH : MetricType.STEP);
                                break;
                            case "switch_kick":
                                TwoStatePreference tsp9 = getPref(prefs, "pref_dual_kick");
                                mSingleMetricChoices.add((tsp9 == null || !tsp9.isChecked()) ? MetricType.KICK_GRAPH : MetricType.KICK);
                                break;
                            default:
                                MetricType metricType = MetricType.getMetricType(tsPref.getKey());
                                if (metricType != MetricType.NONE) {
                                    mSingleMetricChoices.add(metricType);
                                    break;
                                } else {
                                    break;
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    public static void setSingleMetricGhostChoices() {
        mSingleMetricGhostChoices.clear();
        Set<String> pages = preferences.getStringSet(getGhostMetricKey(), null);
        if (pages == null) {
            pages = getDefaultGhostPages();
        }
        for (String page : pages) {
            MetricType metricType = MetricType.getMetricType(page);
            if (metricType != MetricType.NONE) {
                mSingleMetricGhostChoices.add(metricType);
            }
        }
    }

    private static Set<String> getDefaultGhostPages() {
        HashSet<String> ghostPages = new HashSet<>();
        String[] strings = mContext.getResources().getStringArray(R.array.gr_setting_screens_values);
        for (String metricName : strings) {
            MetricType metricType = MetricType.getMetricType(metricName);
            if (metricType != null && (metricType.getSensorType() == Sensor.DataType.UNKOWN || SensorsConnector.isAllowedType(metricType.getSensorType()))) {
                ghostPages.add(metricName);
            }
        }
        return ghostPages;
    }

    public static String getPageOption(String optionId) {
        return preferences.getString(optionId, "");
    }

    public static boolean isMultiMetricPageEnabled(String optionId) {
        return preferences.getBoolean(optionId, false);
    }

    public static List<String> getMultiMetricPages() {
        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            if (isMultiMetricPageEnabled(getMultiScreenKey(i))) {
                pages.add(getMultiScreenOptionKey(i, 1));
                pages.add(getMultiScreenOptionKey(i, 2));
            }
        }
        return pages;
    }

    public static boolean isSingleMetrics() {
        return PrefItem.MODE_SINGLE_METRICS.get();
    }

    public static void setSingleMetricsMode(boolean singleMetrics) {
        PrefItem.MODE_SINGLE_METRICS.apply(singleMetrics);
    }

    public static boolean navigationIsFullScreen() {
        return PrefItem.NAVIGATE_FULL_SCREEN.get();
    }

    public static boolean navigationShowCompass() {
        return PrefItem.NAVIGATE_DISPLAY_COMPASS.get();
    }

    public static boolean navigationDirectionsOrMap() {
        return PrefItem.NAVIGATE_DIRECTIONS.get();
    }

    public static void setPairedHeadset(String mac) {
        PrefItem.HEADSET_PAIRED.apply(mac);
    }

    public static void removedPairedHeadset() {
        PrefItem.HEADSET_PAIRED.remove();
    }

    public static void setModel(String model) {
        PrefItem.HEADSET_PAIRED_MODEL.apply(model);
    }

    public static String getLastModel() {
        return PrefItem.HEADSET_PAIRED_MODEL.getString();
    }

    public static boolean isWatchMode() {
        Log.d(TAG, "isWatchMode " + PrefItem.WATCH_MODE.get());
        return PrefItem.WATCH_MODE.get();
    }

    public static void setWatchMode(boolean watchMode) {
        Log.d(TAG, "setWatchMode " + watchMode);
        PrefItem.WATCH_MODE.apply(watchMode);
    }

    public static List<MetricType> getDoubleChoices() {
        List<MetricType> metricTypes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            if (isMultiMetricPageEnabled(getMultiScreenKey(i))) {
                String met1 = preferences.getString(getMultiScreenOptionKey(i, 1), "");
                String met2 = preferences.getString(getMultiScreenOptionKey(i, 2), "");
                MetricType metricType = MetricType.getMetricType(met1);
                if (metricType != MetricType.NONE) {
                    metricTypes.add(metricType);
                }
                MetricType metricType2 = MetricType.getMetricType(met2);
                if (metricType2 != MetricType.NONE) {
                    metricTypes.add(metricType2);
                }
            }
        }
        return metricTypes;
    }

    public static void convertTargets(boolean toMetric) {
        if (isMetric() != toMetric) {
            convertStoredPrefValue(TargetPreference.DISTANCE.getKey(), getTarget(TargetPreference.DISTANCE), toMetric, mContext.getResources().getInteger(R.integer.target_distance_max), mContext.getResources().getInteger(R.integer.target_distance_max_miles));
            String key = mContext.getString(R.string.pref_key_lap_distance);
            convertStoredPrefValue(key, getTarget(key), toMetric);
        }
    }

    private static void convertStoredPrefValue(String key, double value, boolean toMetric) {
        convertStoredPrefValue(key, value, toMetric, 0.0d, 0.0d);
    }

    private static void convertStoredPrefValue(String key, double value, boolean toMetric, double maxMetric, double maxImperial) {
        if (isMetric() != toMetric) {
            double value2 = toMetric ? Conversion.milesToKm(value) : Conversion.kmToMiles(value);
            if (maxMetric > 0.0d && maxImperial > 0.0d && value2 > 0.0d) {
                if (!toMetric) {
                    maxMetric = maxImperial;
                }
                value2 = Math.min(value2, maxMetric);
            }
            preferences.edit().putString(key, String.format("%.1f", Double.valueOf(value2))).commit();
        }
    }

    private static void convertStoredPaceValue(String key, double value, boolean toMetric, double maxMetric, double maxImperial) {
        if (isMetric() != toMetric) {
            double value2 = toMetric ? Conversion.minutesPerMileToMinutesPerKm(value) : Conversion.minutesPerKmToMinutesPerMile(value);
            if (maxMetric > 0.0d && maxImperial > 0.0d && value2 > 0.0d) {
                if (!toMetric) {
                    maxMetric = maxImperial;
                }
                value2 = Math.min(value2, maxMetric);
            }
            preferences.edit().putString(key, String.format("%.1f", Double.valueOf(value2))).commit();
        }
    }

    public static double getTarget(TargetPreference targetPreference) {
        if (targetPreference == TargetPreference.NONE) {
            return 0.0d;
        }
        return getTarget(targetPreference.getKey());
    }

    public static double getTarget(String key) {
        if (key == null || key.isEmpty()) {
            return 0.0d;
        }
        try {
            return Double.valueOf(preferences.getString(key, "0")).doubleValue();
        } catch (NumberFormatException e) {
            return 0.0d;
        }
    }

    public static int getTargetAverageCadence() {
        return (int) Float.parseFloat(PrefItem.TARGET_AVERAGE_CADENCE.getString());
    }

    public static int getTargetAverageHeartrate() {
        return (int) Float.parseFloat(PrefItem.TARGET_AVERAGE_HEARTRATE.getString());
    }

    public static int getTargetAveragePower() {
        return (int) Float.parseFloat(PrefItem.TARGET_AVERAGE_POWER.getString());
    }

    private static float getTargetAverageSpeed() {
        return Float.parseFloat(PrefItem.TARGET_AVERAGE_SPEED.getString());
    }

    private static long getTargetAveragePaceKm() {
        return PrefItem.TARGET_AVERAGE_PACE.getLong();
    }

    public static boolean hasTargetAveragePaceValue() {
        return getTargetAveragePaceValue() != -2.147483648E9d;
    }

    public static double getTargetAveragePaceValue() {
        return PrefItem.TARGET_AVERAGE_PACE_VALUE.getDouble(PrefItem.TARGET_AVERAGE_PACE_VALUE.defaultValueString);
    }

    public static void setTargetAveragePaceValue(double spm) {
        PrefItem.TARGET_AVERAGE_PACE_VALUE.apply(spm);
    }

    public static int getTargetAverageStep() {
        return (int) Float.parseFloat(PrefItem.TARGET_AVERAGE_STEP.getString());
    }

    public static double getTargetAverageSpeedValue() {
        return PrefItem.TARGET_AVERAGE_SPEED_MPS_VALUE.getDouble(PrefItem.TARGET_AVERAGE_SPEED_MPS_VALUE.defaultValueString);
    }

    public static void setTargetAverageSpeedValue(double mps) {
        PrefItem.TARGET_AVERAGE_SPEED_MPS_VALUE.apply(mps);
    }

    public static int getTargetAverageKick() {
        return (int) Float.parseFloat(PrefItem.TARGET_AVERAGE_KICK.getString());
    }

    public static boolean isTTSNotificationsEnabled() {
        return PrefItem.TTS_NOTIFICATIONS_ENABLED.get();
    }

    public static boolean isGhostLapsEnabled() {
        return PrefItem.GHOST_LAPS.get();
    }

    public static double getTargetDistance() {
        return Double.valueOf(PrefItem.TARGET_DISTANCE_VALUE.getString()).doubleValue();
    }

    public static boolean hasTargetDistance() {
        return getTargetDistance() > 0.0d && (getSingleMetricChoices().contains(MetricType.TARGET_DISTANCE) || getDoubleChoices().contains(MetricType.TARGET_DISTANCE));
    }

    public static boolean isReady() {
        return PrefItem.READY.get();
    }

    public static void setReady(boolean ready) {
        PrefItem.READY.commit(ready);
    }

    public static int getScreenTimer() {
        return Utility.intFromString(PrefItem.SCREEN_TIMER.getString(), 7) * 1000;
    }

    public static int getHeadsetScreenRefreshTime() {
        return Utility.intFromString(preferences.getString(TIMER_KEY, "10"), 10);
    }

    public static int getHeadsetSleepTime() {
        return Integer.valueOf(PrefItem.HEADSET_SLEEP_TIME.getString()).intValue();
    }

    public static int getHeadsetVolume() {
        return Integer.valueOf(PrefItem.HEADSET_VOLUME_LEVEL.getString()).intValue();
    }

    public static void setHeadsetVolume(int vol) {
        PrefItem.HEADSET_VOLUME_LEVEL.commit(Integer.toString(vol));
    }

    public static int getHeadsetBrightness() {
        return Integer.valueOf(PrefItem.HEADSET_BRIGHTNESS_LEVEL.getString()).intValue();
    }

    public static void setHeadsetBrightness(int vol) {
        PrefItem.HEADSET_BRIGHTNESS_LEVEL.commit(Integer.toString(vol));
    }

    public static boolean hasHeadsetAutoBrightness() {
        return PrefItem.HEADSET_AUTO_BRIGHTNESS.get();
    }

    public static void setHeadsetAutoBrightness(boolean check) {
        PrefItem.HEADSET_AUTO_BRIGHTNESS.commit(check);
    }

    private static SCREEN_STATE getScreenMode() {
        String mode = preferences.getString(TIMER_KEY, MODE_AUTO);
        return mode.equals(MODE_MANUAL) ? SCREEN_STATE.MANUAL : SCREEN_STATE.AUTO_MANUAL;
    }

    public static double getWheelSize() {
        return preferences.getFloat(WHEEL_SIZE_KEY, 2.06f);
    }

    public static void setWheelSize(float wheelSize) {
        preferences.edit().putFloat(WHEEL_SIZE_KEY, wheelSize).commit();
    }

    public static int getDefaultRideType() {
        return Integer.parseInt(PrefItem.DEFAULT_RIDE_TYPE.getString());
    }

    public static void setDefaultRideType(int rideType) {
        if (rideType >= 0) {
            PrefItem.DEFAULT_RIDE_TYPE.commit(String.valueOf(rideType));
        }
    }

    public static long getChosenBikeId() {
        return Long.parseLong(PrefItem.DEFAULT_BIKE_TYPE.getString());
    }

    public static void setChosenBikeId(long localBikeId) {
        if (localBikeId >= 0) {
            PrefItem.DEFAULT_BIKE_TYPE.commit(String.valueOf(localBikeId));
        }
    }

    public static String getChosenBikeExternalId() {
        return PrefItem.BIKE_EXTERNAL_ID.getString();
    }

    public static void setChosenBikeExternalId(String id) {
        PrefItem.BIKE_EXTERNAL_ID.commit(id);
    }

    public static boolean hasTargetAverageSpeed() {
        if (getTargetAverageSpeed() > 0.0f) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_SPEED) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_SPEED);
        }
        return false;
    }

    public static boolean hasTargetAveragePace() {
        if (getTargetAveragePaceKm() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_PACE) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_PACE);
        }
        return false;
    }

    public static boolean hasTargetAverageHeartRate() {
        if (getTargetAverageHeartrate() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_HEARTRATE) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_HEARTRATE);
        }
        return false;
    }

    public static boolean hasTargetAveragePower() {
        if (getTargetAveragePower() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_POWER) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_POWER);
        }
        return false;
    }

    public static boolean hasTargetAverageCadence() {
        if (getTargetAverageCadence() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_CADENCE) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_CADENCE);
        }
        return false;
    }

    public static boolean hasTargetAverageStep() {
        if (getTargetAverageCadence() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_STEP) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_STEP);
        }
        return false;
    }

    public static boolean hasTargetAverageKick() {
        if (getTargetAverageKick() > 0) {
            return isSingleMetrics() ? getSingleMetricChoices().contains(MetricType.AVERAGE_TARGET_KICK) : getDoubleChoices().contains(MetricType.AVERAGE_TARGET_KICK);
        }
        return false;
    }

    public static boolean hasAutoScreenChange() {
        SCREEN_STATE state = getScreenMode();
        return state == SCREEN_STATE.AUTOMATIC || state == SCREEN_STATE.AUTO_MANUAL;
    }

    public static boolean hasManualScreenChange() {
        SCREEN_STATE state = getScreenMode();
        return state == SCREEN_STATE.AUTO_MANUAL || state == SCREEN_STATE.MANUAL;
    }

    public static boolean hasTTS() {
        return PrefItem.TTS.get();
    }

    public static void setTTS(boolean on) {
        PrefItem.TTS.apply(on);
    }

    public static boolean isAuto() {
        String mode = preferences.getString(MODE_KEY, mContext.getString(R.string.setting_mode_default));
        return MODE_AUTO.equals(mode);
    }

    public static void setAutoStart(boolean autoStart) {
        preferences.edit().putString(MODE_KEY, autoStart ? MODE_AUTO : MODE_MANUAL).commit();
    }

    public static boolean isAutoStartPossible() {
        if (!isAuto()) {
            return false;
        }
        if (Config.FAKE_DATA) {
            return true;
        }
        return SensorList.isSensorConnected(Sensor.DataType.CADENCE, true) || SensorList.isSensorConnected(Sensor.DataType.STEP, true) || SensorList.isSensorConnected(Sensor.DataType.SPEED, true);
    }

    public static boolean isCountdownActive() {
        return PrefItem.COUNTDOWN_TIMER_ENABLE.get();
    }

    public static void setCountdownActive(boolean on) {
        PrefItem.COUNTDOWN_TIMER_ENABLE.apply(on);
    }

    public static boolean isAutoLockEnabled() {
        return PrefItem.AUTO_LOCK_ENABLED.get();
    }

    public static boolean isGPSEnabled() {
        return PrefItem.GPS_ENABLED.get();
    }

    public static void setGPSEnabled(boolean on) {
        PrefItem.GPS_ENABLED.apply(on);
        if (!on) {
            setGPSAllowed(false);
        }
    }

    public static boolean isGPSAllowed() {
        return PrefItem.GPS_CONSENT.get();
    }

    public static void setGPSAllowed(boolean on) {
        PrefItem.GPS_CONSENT.apply(on);
    }

    public static void changeAutoPause(boolean enable) {
        PrefItem.AUTO_PAUSE_ENABLED.commit(enable);
    }

    public static boolean isAutoPauseEnabled() {
        return PrefItem.AUTO_PAUSE_ENABLED.get();
    }

    public static double getAutoPauseSpeedMPS() {
        switch (getSportType()) {
            case RUN:
                return 0.1d;
            case RIDE:
                String speedInString = preferences.getString(AUTO_PAUSE_SPEED_KEY_MPS, "0.27778");
                return Utility.doubleFromString(speedInString, 0.0d);
            default:
                return 0.0d;
        }
    }

    public static void setAutoPauseSpeedMPS(double speedMPS) {
        preferences.edit().putString(AUTO_PAUSE_SPEED_KEY_MPS, Double.toString(speedMPS)).commit();
    }

    public static boolean hasTargetTime() {
        return getTargetRideTimeInMillis() > 0 && (getSingleMetricChoices().contains(MetricType.TARGET_TIME) || getDoubleChoices().contains(MetricType.TARGET_TIME) || LiveRide.isFunctionalThresholdPowerMode());
    }

    public static long getTargetRideTimeInMillis() {
        long targetTime = preferences.getLong(TARGET_RIDE_TIME_KEY, 3600000L);
        return targetTime;
    }

    public static List<String> getNavigationTTSSettings() {
        List<String> integerList = new ArrayList<>();
        integerList.add(preferences.getString(DEBUG_NAVIGATE_LONG_REMINDER, "60"));
        integerList.add(preferences.getString(DEBUG_NAVIGATE_SHORT_REMINDER, "20"));
        return integerList;
    }

    public static int getTargetAveTolerance() {
        return preferences.getInt(mContext.getString(R.string.pref_key_target_ave_tolerance), mContext.getResources().getInteger(R.integer.tolerance_default));
    }

    public static TargetAveNotificationVerbosity getTargetAveVerbosity() {
        int choice = preferences.getInt(mContext.getString(R.string.pref_key_target_ave_verbosity), mContext.getResources().getInteger(R.integer.verbosity_default));
        int i = 0;
        for (TargetAveNotificationVerbosity verbosity : TargetAveNotificationVerbosity.values()) {
            if (i != choice) {
                i++;
            } else {
                return verbosity;
            }
        }
        TargetAveNotificationVerbosity verbosity2 = TargetAveNotificationVerbosity.MEDIUM;
        return verbosity2;
    }

    public static String getTargetVerbosityKey() {
        return mContext.getString(R.string.pref_key_target_ave_verbosity);
    }

    public static boolean getNotifyPrimaryTargetsOnly() {
        return preferences.getBoolean("pref_notify_primary_metric_only", false);
    }

    public static double getLapDistanceMeters() {
        double value = getLapDistance();
        return isMetric() ? 1000.0d * value : 1609.344d * value;
    }

    public static double getLapDistance() {
        Integer lapDefault = Integer.valueOf(mContext.getResources().getInteger(isMetric() ? R.integer.lap_default : R.integer.lap_default_miles));
        String sizeString = preferences.getString(mContext.getString(R.string.pref_key_lap_distance), lapDefault.toString());
        if (sizeString.trim().isEmpty()) {
            sizeString = lapDefault.toString();
        }
        return Utility.doubleFromString(sizeString, lapDefault.intValue());
    }

    public static void setAutoShare(String platformAutoShareKey, boolean on) {
        preferences.edit().putBoolean(platformAutoShareKey, on).commit();
    }

    public static long getCloudLastUploadTime() {
        return preferences.getLong(mContext.getString(R.string.pref_key_cloud_last_upload_time), 0L);
    }

    public static void setCloudLastUploadTime(long time) {
        if (time > 0) {
            preferences.edit().putLong(mContext.getString(R.string.pref_key_cloud_last_upload_time), time).commit();
        }
    }

    public static boolean updateCloudLastUploadTime(long waitPeriod) {
        long last = getCloudLastUploadTime();
        if (System.currentTimeMillis() < last + waitPeriod) {
            return false;
        }
        setCloudLastUploadTime(System.currentTimeMillis());
        return true;
    }

    private static long getCloudLastSyncTime() {
        return preferences.getLong(mContext.getString(R.string.pref_key_cloud_last_sync_time), 0L);
    }

    private static void setCloudLastSyncTime(long time) {
        if (time > 0) {
            preferences.edit().putLong(mContext.getString(R.string.pref_key_cloud_last_sync_time), time).commit();
        }
    }

    public static boolean updateCloudLastSyncTime(long waitPeriod, boolean forceSync) {
        long last = getCloudLastSyncTime();
        if (!forceSync && System.currentTimeMillis() < last + waitPeriod) {
            return false;
        }
        setCloudLastSyncTime(System.currentTimeMillis());
        return true;
    }

    public static boolean isCloudLoggedInUser() {
        return PrefItem.CLOUD_USER_NOT_SETUP.get();
    }

    public static void setCloudLoggedInUser() {
        setCloudLoggedInUser(true);
    }

    public static void setCloudLoggedInUser(boolean loggedIn) {
        PrefItem.CLOUD_USER_NOT_SETUP.commit(loggedIn);
    }

    public static boolean isForceSync() {
        return PrefItem.FORCE_SYNC.get();
    }

    public static void setForceSync(boolean sync) {
        PrefItem.FORCE_SYNC.apply(sync);
    }

    public static void setMultiSportSupported(boolean supported) {
        PrefItem.MULTI_SPORT_SUPPORTED.apply(supported);
    }

    public static boolean isMultiSportSupported() {
        return PrefItem.MULTI_SPORT_SUPPORTED.get();
    }

    public static String getRollingAverageModeFor(MetricDataType dataType) {
        switch (dataType) {
            case SPEED:
                return PrefItem.ROLLING_AVG_SPEED.getString();
            case PACE:
                return PrefItem.ROLLING_AVG_PACE.getString();
            case CADENCE:
                return PrefItem.ROLLING_AVG_CADENCE.getString();
            case STEP:
                return PrefItem.ROLLING_AVG_STEP.getString();
            case POWER:
                return PrefItem.ROLLING_AVG_POWER.getString();
            case HEART_RATE:
                return PrefItem.ROLLING_AVG_HEARTRATE.getString();
            case STRIDE:
                return PrefItem.ROLLING_AVG_STRIDE.getString();
            case KICK:
                return PrefItem.ROLLING_AVG_KICK.getString();
            default:
                return "0";
        }
    }

    public static void clear() {
        Log.i(TAG, "clear");
        preferences.edit().clear().commit();
    }

    private enum PrefItem {
        CLOUD_USER_NOT_SETUP(false, Prefs.PREF_SETUP_KEY_LOGGED_IN),
        SETUP_COMPLETE(false, Prefs.PREF_SETUP_KEY),
        FIRST_RUN(true),
        MODE_SINGLE_METRICS(true, Prefs.PAGE_DISPLAY_MODE_KEY),
        NAVIGATE_FULL_SCREEN(true),
        NAVIGATE_DISPLAY_COMPASS(false),
        NAVIGATE_DIRECTIONS(false),
        TTS_NOTIFICATIONS_ENABLED(false, R.string.pref_key_tts_notifications),
        GHOST_LAPS(false, R.string.pref_key_ghost_laps),
        READY,
        SPEED_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_speed_on),
        PACE_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_pace_on),
        HEARTRATE_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_heartrate_on),
        POWER_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_power_on),
        CADENCE_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_cadence_on),
        STEP_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_step_on),
        KICK_AVERAGE_TARGET_ON(false, R.string.pref_key_target_average_kick_on),
        TTS(false, Prefs.TTS_KEY),
        COUNTDOWN_TIMER_ENABLE,
        AUTO_LOCK_ENABLED(true),
        GPS_ENABLED(true, R.string.pref_key_gps),
        GPS_CONSENT(false, R.string.pref_key_gps_consent),
        AUTO_PAUSE_ENABLED(false, Prefs.AUTO_PAUSE_KEY),
        FORCE_SYNC(false),
        WATCH_MODE(false),
        TARGET_AVERAGE_SPEED_MPS_VALUE(Prefs.mContext.getString(R.string.pref_target_average_speed_default)),
        TARGET_AVERAGE_PACE_VALUE(Prefs.mContext.getString(R.string.pref_target_average_pace_default)),
        GENDER("", R.string.pref_key_user_gender),
        UNIT_SYSTEM(UnitSystem.METRIC.name(), R.string.pref_key_unit),
        USER_WEIGHT_KG("" + Prefs.mContext.getResources().getInteger(R.integer.weight_default), R.string.pref_key_user_weight),
        RIDE_TYPE(Prefs.DEFAULT_RIDE_TYPE_INDEX, R.string.pref_key_ride_type),
        TARGET_AVERAGE_CADENCE(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_cadence_default)), R.string.pref_key_target_average_cadence),
        TARGET_AVERAGE_HEARTRATE(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_heartrate_default)), R.string.pref_key_target_average_heartrate),
        TARGET_AVERAGE_POWER(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_power_default)), R.string.pref_key_target_average_power),
        TARGET_AVERAGE_SPEED(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_speed_default)), R.string.pref_key_target_average_speed),
        TARGET_AVERAGE_PACE(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_pace_default)), R.string.pref_key_target_average_pace),
        TARGET_AVERAGE_STEP(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_cadence_default)), R.string.pref_key_target_average_step),
        TARGET_AVERAGE_KICK(String.valueOf(Prefs.mContext.getResources().getInteger(R.integer.target_power_default)), R.string.pref_key_target_average_kick),
        TARGET_DISTANCE_VALUE("0", Prefs.TARGET_DISTANCE),
        SCREEN_TIMER(Prefs.mContext.getString(R.string.setting_timer_default), Prefs.TIMER_KEY),
        HEADSET_SLEEP_TIME("300", Prefs.HEADSET_SLEEP),
        HEADSET_VOLUME_LEVEL("7", Prefs.HEADSET_VOLUME),
        HEADSET_BRIGHTNESS_LEVEL("200", Prefs.HEADSET_BRIGHTNESS),
        HEADSET_AUTO_BRIGHTNESS(false),
        DEFAULT_RIDE_TYPE(Prefs.DEFAULT_RIDE_TYPE_INDEX, Prefs.PREF_DEFAULT_RIDE),
        DEFAULT_BIKE_TYPE(AppEventsConstants.EVENT_PARAM_VALUE_YES, Prefs.PREF_DEFAULT_BIKE),
        BIKE_EXTERNAL_ID,
        HEADSET_PAIRED((String) null, "headset_paired"),
        HEADSET_PAIRED_MODEL((String) null, Prefs.HEADSET_PAIRED_MODEL_KEY),
        MULTI_SPORT_SUPPORTED(false),
        SETUP_COMPLETE_SPORTSET,
        ROLLING_AVG_SPEED(Prefs.ROLLING_AVERAGE_ON, "rolling_avg_speed"),
        ROLLING_AVG_PACE(Prefs.ROLLING_AVERAGE_ON, "rolling_avg_pace"),
        ROLLING_AVG_CADENCE("0", "rolling_avg_cadence"),
        ROLLING_AVG_STEP("0", "rolling_avg_step"),
        ROLLING_AVG_POWER(Prefs.ROLLING_AVERAGE_ON, "rolling_avg_power"),
        ROLLING_AVG_HEARTRATE("0", "rolling_avg_heartrate"),
        ROLLING_AVG_STRIDE("0", "rolling_avg_stride"),
        ROLLING_AVG_KICK(Prefs.ROLLING_AVERAGE_ON, "rolling_avg_kick"),
        ACTIVITY_SELECTION_FILTER("0", "activity_selection_filter");

        private boolean defaultValue;
        private String defaultValueString;
        private final String key;

        PrefItem() {
            this(false);
        }

        PrefItem(boolean initValue) {
            this.defaultValue = false;
            this.defaultValueString = "";
            this.defaultValue = initValue;
            this.key = name().toLowerCase();
        }

        PrefItem(boolean initValue, int key) {
            this(initValue, Prefs.mContext.getString(key));
        }

        PrefItem(boolean initValue, String key) {
            this.defaultValue = false;
            this.defaultValueString = "";
            this.defaultValue = initValue;
            this.key = key;
        }

        PrefItem(String initValue) {
            this.defaultValue = false;
            this.defaultValueString = "";
            this.defaultValueString = initValue;
            this.key = name().toLowerCase();
        }

        PrefItem(String initValue, int key) {
            this(initValue, Prefs.mContext.getString(key));
        }

        PrefItem(String initValue, String key) {
            this.defaultValue = false;
            this.defaultValueString = "";
            this.defaultValueString = initValue;
            this.key = key;
        }

        boolean get() {
            return Prefs.preferences.getBoolean(this.key, this.defaultValue);
        }

        int getInt() {
            return Prefs.preferences.getInt(this.key, 0);
        }

        long getLong() {
            return Prefs.preferences.getLong(this.key, Long.valueOf(this.defaultValueString).longValue());
        }

        double getDouble() {
            return Prefs.preferences.getFloat(this.key, -2.14748365E9f);
        }

        double getDouble(String defaultValue) {
            return Prefs.preferences.getFloat(this.key, Float.valueOf(defaultValue).floatValue());
        }

        String getString() {
            return Prefs.preferences.getString(this.key, this.defaultValueString);
        }

        Set<String> getStrings() {
            return Prefs.preferences.getStringSet(this.key, null);
        }

        void apply(boolean b) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putBoolean(this.key, b).apply();
            }
        }

        void commit(boolean b) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putBoolean(this.key, b).commit();
            }
        }

        void apply(int i) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putInt(this.key, i).apply();
            }
        }

        void apply(long l) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putLong(this.key, l).apply();
            }
        }

        void apply(double d) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putFloat(this.key, (float) d).apply();
            }
        }

        void apply(String s) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putString(this.key, s).apply();
            }
        }

        void commit(String s) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putString(this.key, s).commit();
            }
        }

        void apply(Set<String> s) {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().putStringSet(this.key, s).apply();
            }
        }

        void remove() {
            if (Prefs.preferences != null) {
                Prefs.preferences.edit().remove(this.key).apply();
            }
        }
    }

    public static void setData(Map<String, Object> map) {
        if (preferences != null && map != null) {
            Log.d(TAG, "setData, map size " + map.size());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    Log.d("map values", key + " : " + value);
                    if (value instanceof String) {
                        preferences.edit().putString(key, (String) value).apply();
                    } else if (value instanceof Boolean) {
                        preferences.edit().putBoolean(key, ((Boolean) value).booleanValue()).apply();
                    } else if (value instanceof Float) {
                        preferences.edit().putFloat(key, ((Float) value).floatValue()).apply();
                    } else if (value instanceof Long) {
                        preferences.edit().putLong(key, ((Long) value).longValue()).apply();
                    } else if (value instanceof Integer) {
                        preferences.edit().putInt(key, ((Integer) value).intValue()).apply();
                    } else if (value instanceof Set) {
                    }
                }
            }
        }
    }

    public static boolean isAnyTargetEnabled() {
        return hasTargetAverageCadence() || hasTargetAverageStep() || hasTargetAverageHeartRate() || hasTargetAveragePower() || hasTargetAveragePace() || hasTargetAverageSpeed() || hasTargetAverageKick();
    }

    public static int getActivitySelectionFilter() {
        String filter = PrefItem.ACTIVITY_SELECTION_FILTER.getString();
        if (filter.trim().isEmpty()) {
            filter = "0";
        }
        return Integer.parseInt(filter);
    }

    public static void setActivitySelectionFilter(int filter) {
        if (filter >= 0) {
            PrefItem.ACTIVITY_SELECTION_FILTER.commit(String.valueOf(filter));
        }
    }
}
