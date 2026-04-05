package com.kopin.solos.storage.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.sensors.utils.Utilities;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.settings.Prefs;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes54.dex */
public class Utility {
    private static final double POUND_KG_CONSTANT = 2.2046d;
    private static final String TAG = "Utility";
    public static final int UNIT_TYPE_DISTANCE = 1;
    public static final int UNIT_TYPE_DISTANCE_SMALL = 4;
    public static final int UNIT_TYPE_LENGTH = 0;
    public static final int UNIT_TYPE_LENGTH_LONG = 3;
    public static final int UNIT_TYPE_SPEED = 2;
    private static final SimpleDateFormat UPLOAD_SDF_URL;
    private static volatile Prefs.UnitSystem sUnitSystem;
    private static final SimpleDateFormat DISPLAY_SDF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);
    private static final SimpleDateFormat COMPACT_SDF = new SimpleDateFormat("yyyyMMddHHmm", Locale.US);
    private static final SimpleDateFormat PAGE_TIME_SDF = new SimpleDateFormat("HH:mm", Locale.US);
    private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final SimpleDateFormat UPLOAD_SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    static {
        UPLOAD_SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
        UPLOAD_SDF_URL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        UPLOAD_SDF_URL.setTimeZone(TimeZone.getTimeZone("UTC"));
        sUnitSystem = Prefs.UnitSystem.METRIC;
    }

    public static long getTimeMilliseconds() {
        return SystemClock.elapsedRealtime();
    }

    public static long getActualDateTimeMilliseconds() {
        long offset = Config.FAKE_DATA ? Config.START_DAY_OFFSET * 86400000 : 0L;
        return System.currentTimeMillis() + offset;
    }

    public static int intFromString(String string, int defValue) {
        try {
            Double d = Double.valueOf(string);
            int defValue2 = d.intValue();
            return defValue2;
        } catch (NumberFormatException e) {
            Log.e(TAG, "", e);
            return defValue;
        }
    }

    public static boolean areAllTrue(boolean... array) {
        for (boolean b : array) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static boolean areAllTrue(boolean[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!array[i]) {
                return false;
            }
        }
        return true;
    }

    public static double doubleFromString(String string, double defValue) {
        if (string != null) {
            try {
                return Double.valueOf(string).doubleValue();
            } catch (NumberFormatException e) {
                Log.e(TAG, "", e);
                return defValue;
            }
        }
        return defValue;
    }

    public static void markReady(Context context, boolean ready, Class<?> cls) {
        Prefs.setReady(ready);
    }

    public static String getPageTitle(Context context, String optionId) {
        String[] entries = context.getResources().getStringArray(R.array.setting_screens_entries);
        String[] values = context.getResources().getStringArray(R.array.setting_screens_values);
        String option = Prefs.getPageOption(optionId);
        if (option.isEmpty()) {
            Log.e(TAG, "getPageTitle null " + optionId);
            return null;
        }
        int pos = search(values, option);
        if (pos < 0 || pos >= entries.length) {
            Log.d(TAG, "getPageTitle null " + optionId);
            return null;
        }
        Log.d(TAG, "getPageTitle " + optionId + " : " + entries[pos]);
        return entries[pos];
    }

    public static int search(Object[] array, Object obj) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(obj)) {
                return i;
            }
        }
        return -1;
    }

    public static String removeDecimalZeros(String s) {
        if (s.endsWith(".0") || s.endsWith(".00") || s.endsWith(".000")) {
            return s.substring(0, s.indexOf("."));
        }
        return s;
    }

    public static String trimDecimalPlaces(double d, int numDecimalPlaces) {
        return trimDecimalPlaces(d, numDecimalPlaces, false);
    }

    public static String trimDecimalPlaces(double d, int numDecimalPlaces, boolean hideZeros) {
        if (hideZeros) {
            String orig = "" + d;
            String s = removeDecimalZeros(orig);
            if (orig.length() != s.length()) {
                return s;
            }
        }
        return String.format("%." + numDecimalPlaces + "f", Double.valueOf(d));
    }

    public static String roundToTwoDecimalPlaces(String number) {
        double value = Double.parseDouble(number);
        return String.valueOf(Math.round(value * 100.0d) / 100.0d);
    }

    public static String roundToTwoDecimalPlaces(double number) {
        double value = Math.round(number * 100.0d) / 100.0d;
        return String.valueOf(value);
    }

    public static String roundToOneDecimalPlacesString(String number) {
        double value = Double.parseDouble(number);
        return roundToOneDecimalPlacesString(value);
    }

    public static String roundToOneDecimalPlacesString(double number) {
        return String.valueOf(roundToOneDecimalPlaces(number));
    }

    public static double roundToOneDecimalPlaces(double number) {
        return Math.round(number * 10.0d) / 10.0d;
    }

    public static String roundToLong(String number) {
        return String.valueOf(Math.rint(Double.parseDouble(number)));
    }

    public static long roundToLong(double number) {
        return (long) Math.rint(number);
    }

    public static long round(double value, int rounding) {
        return rounding < 1 ? (long) value : Math.round(value / ((double) rounding)) * ((long) rounding);
    }

    public static double calculateSpeedMetresPerSecond(double distanceMeters, long timeMillisecond) {
        return averageSpeedMph(distanceMeters, timeMillisecond) * 0.44704d;
    }

    public static double averageSpeedMph(double distanceMeters, long timeMillisecond) {
        if (distanceMeters == -2.147483648E9d || timeMillisecond == -2147483648L) {
            return 0.0d;
        }
        double distance = (distanceMeters / 1000.0d) * 0.621371d;
        double time = timeMillisecond / 3600000.0d;
        return distance / time;
    }

    public static double calculateSpeedInKph(double distanceMeters, long timeMillisecond) {
        if (distanceMeters == -2.147483648E9d || timeMillisecond == -2147483648L) {
            return 0.0d;
        }
        double distance = distanceMeters / 1000.0d;
        double time = timeMillisecond / 3600000.0d;
        return distance / time;
    }

    public static double metersToMiles(double meters) {
        return Utilities.convertMetersToMiles(meters);
    }

    public static double metersToKilometers(double meters) {
        return Utilities.convertMetersToKilometers(meters);
    }

    public static String formatDateAndTimeDisplay(long timeMillisecond) {
        String str;
        synchronized (DISPLAY_SDF) {
            str = DISPLAY_SDF.format(new Date(timeMillisecond));
        }
        return str;
    }

    public static String formatDateAndTimeUpload(long timeMillisecond, boolean noMillisecond) {
        String str;
        if (noMillisecond) {
            synchronized (UPLOAD_SDF_URL) {
                str = UPLOAD_SDF_URL.format(new Date(timeMillisecond));
            }
        } else {
            synchronized (UPLOAD_SDF) {
                str = UPLOAD_SDF.format(new Date(timeMillisecond));
            }
        }
        return str;
    }

    public static String formatDateAndTimeCompact(long timeMillisecond) {
        String str;
        synchronized (COMPACT_SDF) {
            str = COMPACT_SDF.format(new Date(timeMillisecond));
        }
        return str;
    }

    public static String formatDate(long timeMillisecond) {
        String str;
        synchronized (DATE_SDF) {
            str = DATE_SDF.format(new Date(timeMillisecond));
        }
        return str;
    }

    public static String formatTimeForPage() {
        String str;
        synchronized (PAGE_TIME_SDF) {
            str = PAGE_TIME_SDF.format(Long.valueOf(System.currentTimeMillis()));
        }
        return str;
    }

    public static String formatTimeOnly(long timeMillisecond) {
        String str;
        synchronized (PAGE_TIME_SDF) {
            str = PAGE_TIME_SDF.format(Long.valueOf(timeMillisecond));
        }
        return str;
    }

    public static float metersToFeet(float meters) {
        return 3.2808f * meters;
    }

    public static double feetToMeters(double feet) {
        return feet / 3.2808d;
    }

    public static double metersToFeet(double meters) {
        return 3.2808d * meters;
    }

    public static double metersToYards(double meters) {
        return 0.9144d * meters;
    }

    public static String formatTime(long timeMillisecond) {
        return formatTime(timeMillisecond, false);
    }

    public static String formatTime(long timeMillisecond, boolean roundUpHalf) {
        StringBuilder sb = new StringBuilder();
        long time = timeMillisecond / 1000;
        long timeDecimal = timeMillisecond % 1000;
        if (roundUpHalf && timeDecimal >= 500) {
            time++;
        }
        String sec = String.format("%02d", Long.valueOf(time % 60));
        long time2 = time / 60;
        String min = String.format("%02d", Long.valueOf(time2 % 60));
        String hour = String.format("%02d", Long.valueOf(time2 / 60));
        sb.append(hour).append(":").append(min).append(":").append(sec);
        return sb.toString();
    }

    public static String toTime(long millis, boolean includeHours) {
        if (millis % 1000 >= 500) {
            millis += 1000;
        }
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (includeHours) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.MINUTES.toMillis(minutes));
        return includeHours ? String.format(Locale.US, "%02d:%02d:%02d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)) : String.format(Locale.US, "%d:%02d", Long.valueOf(minutes), Long.valueOf(seconds));
    }

    public static String toTime(long hours, long mins, long seconds) {
        return String.format(Locale.US, "%02d:%02d:%02d", Long.valueOf(hours), Long.valueOf(mins), Long.valueOf(seconds));
    }

    public static String toTime(long mins, long seconds) {
        return String.format(Locale.US, "%02d:%02d", Long.valueOf(mins), Long.valueOf(seconds));
    }

    public static String formatTime1(long timeMillisecond) {
        StringBuilder sb = new StringBuilder();
        long hours = TimeUnit.MILLISECONDS.toHours(timeMillisecond);
        long timeMillisecond2 = timeMillisecond - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillisecond2);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillisecond2 - TimeUnit.MINUTES.toMillis(minutes));
        sb.append(String.format("%02d", Long.valueOf(hours))).append(":").append(String.format("%02d", Long.valueOf(minutes))).append(":").append(String.format("%02d", Long.valueOf(seconds)));
        return sb.toString();
    }

    private static long getStartOfDayTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static String formatTimeToRelativeDays(Context context, long time) {
        String format;
        long startOfDay = getStartOfDayTime();
        long delta = time - startOfDay;
        if (delta >= 0) {
            if (delta < 86400000) {
                format = context.getString(R.string.today);
            } else if (delta < 172800000) {
                format = context.getString(R.string.tomorrow);
            } else if (delta < 604800000) {
                format = new SimpleDateFormat("EEEE").format(new Date(time));
            } else {
                format = new SimpleDateFormat("dd MMM").format(new Date(time));
            }
        } else if (Math.abs(delta) <= 86400000) {
            format = context.getString(R.string.yesterday);
        } else {
            format = new SimpleDateFormat("dd MMM").format(new Date(time));
        }
        Log.d(Lap.TRIGGER_TIME, format);
        return format;
    }

    public static String escapeXMLChars(String s) {
        return s.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "&#xA;");
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double poundsToKilograms(double pounds) {
        return pounds / POUND_KG_CONSTANT;
    }

    public static double kilogramsToPounds(double kg) {
        return POUND_KG_CONSTANT * kg;
    }

    public static float dpToPx(float dp, Resources res) {
        return TypedValue.applyDimension(1, dp, res.getDisplayMetrics());
    }

    public static void setTextColor(View view, int color) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof ViewGroup) {
            setTextColor((ViewGroup) view, color);
        }
    }

    public static void setTextColor(ViewGroup v, int color) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View view = v.getChildAt(i);
            setTextColor(view, color);
        }
    }

    public static Bitmap getBitmapForDensity(Context context, int drawableResId, int density) {
        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawableForDensity(drawableResId, density);
        return drawable.getBitmap();
    }

    public static void setUnitSystem(Prefs.UnitSystem unitSystem) {
        sUnitSystem = unitSystem;
    }

    public static boolean isMetric() {
        return sUnitSystem == Prefs.UnitSystem.METRIC;
    }

    public static String getUnitOfWeight(Context context) {
        return getUnitOfWeight(context, Prefs.getUnitSystem());
    }

    public static String getUnitOfWeight(Context context, Prefs.UnitSystem unitSystem) {
        return context.getString(unitSystem == Prefs.UnitSystem.IMPERIAL ? R.string.unit_weight_imperial : R.string.unit_weight_metric);
    }

    public static String convertToKg(Context context, String text) {
        if (Prefs.getUnitSystem() != Prefs.UnitSystem.IMPERIAL) {
            return text;
        }
        return String.valueOf(poundsToKilograms(doubleFromString(text, context.getResources().getInteger(R.integer.weight_default_pounds))));
    }

    public static Double convertToKg(Double value) {
        double dDoubleValue;
        if (Prefs.getUnitSystem() == Prefs.UnitSystem.IMPERIAL) {
            dDoubleValue = poundsToKilograms(value.doubleValue());
        } else {
            dDoubleValue = value.doubleValue();
        }
        return Double.valueOf(dDoubleValue);
    }

    public static double weightInUnitSystem(String text, Prefs.UnitSystem unitSystem, int defaultWeight) {
        double weight = doubleFromString(text, defaultWeight);
        return unitSystem == Prefs.UnitSystem.IMPERIAL ? kilogramsToPounds(weight) : weight;
    }

    public static double weightInUnitSystem(String text, int defaultWeight) {
        double weight = doubleFromString(text, defaultWeight);
        return Prefs.getUnitSystem() == Prefs.UnitSystem.IMPERIAL ? kilogramsToPounds(weight) : weight;
    }

    public static double distanceInUnitSystem(double value, Prefs.UnitSystem unitSystem) {
        return (unitSystem == Prefs.UnitSystem.METRIC || value == -2.147483648E9d) ? value : Conversion.kmToMiles(value);
    }

    public static String getUserDefinedUnit(Context context, int unitType) {
        switch (unitType) {
            case 1:
                return context.getString(isMetric() ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
            case 2:
                return context.getString(isMetric() ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
            case 3:
            default:
                return context.getString(isMetric() ? R.string.unit_length_metric : R.string.unit_length_imperial);
            case 4:
                return context.getString(isMetric() ? R.string.unit_distance_metric_small : R.string.unit_distance_imperial_small);
        }
    }

    public static String getUserDefinedUnitShort(Context context, int unitType, boolean useShort) {
        if (context == null) {
            return "";
        }
        switch (unitType) {
            case 1:
                if (useShort) {
                    return context.getString(isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short);
                }
                return context.getString(isMetric() ? R.string.unit_distance_metric_short : R.string.unit_distance_imperial_short);
            case 2:
                return context.getString(isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short);
            default:
                return context.getString(isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short);
        }
    }

    public static String getUserUnitLabel(Context context, int unitType) {
        switch (unitType) {
            case 1:
                return context.getString(isMetric() ? R.string.caps_km : R.string.caps_miles);
            case 2:
                return context.getString(isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short);
            case 3:
                return context.getString(isMetric() ? R.string.unit_length_metric : R.string.unit_length_imperial);
            case 4:
                return context.getString(isMetric() ? R.string.caps_metres : R.string.caps_feet);
            default:
                return context.getString(isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short);
        }
    }

    public static double convertToUserUnits(int unitType, double unitValue) {
        switch (unitType) {
            case 1:
                if (!isMetric()) {
                }
                break;
            case 2:
                if (!isMetric()) {
                }
                break;
            default:
                if (!isMetric()) {
                    break;
                }
                break;
        }
        return unitValue;
    }

    public static int indexOf(String toFind, int arrayResId, Context context) {
        String[] array = context.getResources().getStringArray(arrayResId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(toFind)) {
                return i;
            }
        }
        return -1;
    }

    public static String increaseDecimalsIfSameValue(double main, double comparison) {
        String s1 = "" + ((int) main);
        String s2 = "" + ((int) comparison);
        if (s1.compareTo(s2) != 0) {
            return s1;
        }
        String s12 = String.format("%.1f", Double.valueOf(main));
        String s22 = String.format("%.1f", Double.valueOf(comparison));
        return s12.compareTo(s22) != 0 ? s12 : String.format("%.2f", Double.valueOf(main));
    }

    public static String formatDecimal(Number number, int numDecimalPlaces) {
        return numDecimalPlaces > 0 ? String.format("%." + numDecimalPlaces + "f", number) : String.format("%d", Long.valueOf(number.longValue()));
    }

    public static String formatDecimal(Number number, int numDecimalPlaces, boolean ignoreWholeNumbers) {
        boolean hasDecimalPart = ("" + number).contains(".");
        if (ignoreWholeNumbers && !hasDecimalPart) {
            numDecimalPlaces = 0;
        }
        return formatDecimal(number, numDecimalPlaces);
    }

    public static String formatDecimal(Number number, boolean condition, int numDecimalPlacesTrue, int numDecimalPlacesFalse) {
        if (!condition) {
            numDecimalPlacesTrue = numDecimalPlacesFalse;
        }
        return formatDecimal(number, numDecimalPlacesTrue);
    }

    public static String trimEmptyDecimal(String number) {
        if (number != null && number.endsWith(".0")) {
            return number.replace(".0", "");
        }
        return number;
    }

    public static boolean isNetworkAvailable(WeakReference<Context> context) {
        return (context == null || context.get() == null || !isNetworkAvailable(context.get())) ? false : true;
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getUnitOfDistanceRes() {
        return isMetric() ? R.string.unit_distance_metric_short : R.string.unit_distance_imperial_short;
    }

    public static int getUnitOfSpeedRes() {
        return isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short;
    }

    public static int getUnitOfStrideRes() {
        return isMetric() ? R.string.unit_cm : R.string.unit_inches;
    }

    public static int getUnitOfElevationRes() {
        return isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short;
    }

    public static int getUnitOfPaceRes() {
        return isMetric() ? R.string.unit_pace_slash_metric_short : R.string.unit_pace_slash_imperial_short;
    }

    public static String formatMetric(MetricType metricType, double value) {
        if (value >= 0.0d) {
            switch (metricType) {
                case AVERAGE_TARGET_SPEED:
                case GHOST_AVERAGE_SPEED:
                case SPEED:
                case DISTANCE:
                    int decimals = value > 1.0d ? 1 : 2;
                    return formatDecimal(Double.valueOf(value), decimals, true);
                case AVERAGE_TARGET_PACE:
                case GHOST_AVERAGE_PACE:
                case PACE:
                    return formatPace(value);
                default:
                    return String.valueOf((int) Math.round(value));
            }
        }
        return "---";
    }

    public static String formatMetricWithUnit(Context context, MetricType metricType, double value) {
        int decimals = value > 1.0d ? 1 : 2;
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return formatDecimal(Double.valueOf(Conversion.speedForLocale(value)), decimals, true) + " " + Conversion.getUnitOfSpeed(context);
            case GHOST_AVERAGE_SPEED:
            case SPEED:
            case DISTANCE:
            case GHOST_AVERAGE_PACE:
            case PACE:
            default:
                return null;
            case AVERAGE_TARGET_PACE:
                return formatPace(Conversion.paceForLocale(value)) + " " + Conversion.getUnitOfPace(context);
            case AVERAGE_TARGET_CADENCE:
                return ((int) Math.round(value)) + " " + context.getString(R.string.caps_rpm).toLowerCase();
            case AVERAGE_TARGET_STEP:
                return ((int) Math.round(value)) + " " + context.getString(R.string.spm).toLowerCase();
            case AVERAGE_TARGET_HEARTRATE:
                return ((int) Math.round(value)) + " " + context.getString(R.string.caps_bpm).toLowerCase();
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                return ((int) Math.round(value)) + " " + context.getString(R.string.lower_watts);
        }
    }

    public static String formatMetricRange(Context context, MetricType metricType, double min, double max) {
        int decimals = min > 1.0d ? 1 : 2;
        String unit = "";
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                unit = Conversion.getUnitOfSpeed(context);
                min = Conversion.speedForLocale(min);
                max = Conversion.speedForLocale(max);
                break;
            case AVERAGE_TARGET_PACE:
                return formatPace(Conversion.paceForLocale(min)) + " - " + formatPace(Conversion.paceForLocale(max)) + " " + Conversion.getUnitOfPace(context);
            case AVERAGE_TARGET_CADENCE:
                unit = context.getString(R.string.caps_rpm).toLowerCase();
                decimals = 0;
                break;
            case AVERAGE_TARGET_STEP:
                unit = context.getString(R.string.spm).toLowerCase();
                decimals = 0;
                break;
            case AVERAGE_TARGET_HEARTRATE:
                unit = context.getString(R.string.caps_bpm).toLowerCase();
                decimals = 0;
                break;
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                unit = context.getString(R.string.lower_watts).toLowerCase();
                decimals = 0;
                break;
        }
        return formatDecimal(Double.valueOf(min), decimals, true) + " - " + formatDecimal(Double.valueOf(max), decimals, true) + " " + unit;
    }

    public static String formatPace(double rawPace) {
        double paceInSeconds = Math.round(Math.max(rawPace, 0.0d));
        if (rawPace < 60.0d) {
            return String.valueOf((int) paceInSeconds);
        }
        int mins = (int) (paceInSeconds / 60.0d);
        int seconds = (int) (paceInSeconds % 60.0d);
        return String.format(Locale.US, "%d:%02d", Integer.valueOf(mins), Integer.valueOf(seconds));
    }
}
