package com.kopin.solos.storage.util;

import android.content.Context;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes54.dex */
public class Conversion {
    public static final double KMH_MPS = 0.27778d;
    private static final double KM_MILES = 0.62137d;
    private static final float METERS_FEET = 3.28084f;
    private static final double METERS_TO_CMS = 100.0d;
    private static final double METERS_TO_INCHES = 39.3701d;
    private static final float METERS_YARDS = 1.0936133f;
    private static final double MILES_KM = 1.60934d;
    private static final double MM_INTO_INCHES = 0.03937008d;
    private static final double MPH_MPS = 0.44704d;
    private static final double MPS_KMH = 3.6d;
    private static final double MPS_MPH = 2.23694d;
    private static final double SPM_MIN_P_KM = 16.6667d;
    private static final double SPM_MIN_P_MILE = 26.8224d;
    private static final double SPM_SS_KM = 1000.0d;
    private static final double SPM_SS_MILE = 1609.34d;

    public static double speedForLocale(double value) {
        return convert(value, Prefs.isMetric() ? MPS_KMH : MPS_MPH);
    }

    public static double speedForLocale(double value, Prefs.UnitSystem locale) {
        return convert(value, locale == Prefs.UnitSystem.METRIC ? MPS_KMH : MPS_MPH);
    }

    public static double paceForLocale(double value, Prefs.UnitSystem locale) {
        return convert(value, locale == Prefs.UnitSystem.METRIC ? MPS_KMH : MPS_MPH);
    }

    public static double distanceForLocale(double value) {
        double distanceInKm = value * 0.001d;
        return Prefs.isMetric() ? distanceInKm : convert(distanceInKm, KM_MILES);
    }

    public static double distanceSmallForLocale(double value) {
        return Prefs.isMetric() ? value : convert(value, 1.0936132669448853d);
    }

    public static double kmToMiles(double value) {
        return convert(value, KM_MILES);
    }

    public static double milesToKm(double value) {
        return convert(value, MILES_KM);
    }

    public static double minutesPerKmToMinutesPerMile(double value) {
        return convert(value, MILES_KM);
    }

    public static double minutesPerMileToMinutesPerKm(double value) {
        return convert(value, KM_MILES);
    }

    public static double millisPerKmToSecondsPerMetre(long millisKm) {
        return millisKm / 1000000.0d;
    }

    public static long secondsPerMetreToMillisPerKm(double secondsMetre) {
        return (long) (1000000.0d * secondsMetre);
    }

    public static long secondsPerMetreToLocale(double secondPerMetre) {
        double value = Utility.isMetric() ? secondPerMetre * 1000.0d : secondPerMetre * SPM_SS_MILE;
        return (long) value;
    }

    public static long secondsPerMetreToSecondsPerMile(double secondsMetre) {
        return (long) (SPM_SS_MILE * secondsMetre);
    }

    public static double speedFromLocale(double value) {
        return convert(value, Prefs.isMetric() ? 0.27778d : MPH_MPS);
    }

    public static float elevationForLocale(float value) {
        return Prefs.isMetric() ? value : convert(value, METERS_FEET);
    }

    public static double strideForLocale(double value) {
        return Prefs.isMetric() ? convert(value, METERS_TO_CMS) : convert(value, 39.3701d);
    }

    public static double paceForLocale(double value) {
        return convert(value, Prefs.isMetric() ? 1000.0d : SPM_SS_MILE);
    }

    public static double speedToPaceForLocale(double value) {
        return paceForLocale(speedToPace(value));
    }

    public static double speedToPace(double value) {
        if (value == -2.147483648E9d) {
            return -2.147483648E9d;
        }
        if (value < 1.0d) {
            return 0.0d;
        }
        return 1.0d / value;
    }

    public static double speedToPace2(double value) {
        if (value == -2.147483648E9d) {
            return -2.147483648E9d;
        }
        if (value <= 0.0d) {
            return 0.0d;
        }
        return 1.0d / value;
    }

    public static long speedToTime(double speed, double distance) {
        if (speed == 0.0d) {
            return 0L;
        }
        return (long) (distance / speed);
    }

    public static double metresPerSecondToMph(double valueMPS) {
        return convert(valueMPS, MPS_MPH);
    }

    public static double mmIntoInches(double valuemm) {
        return convert(valuemm, MM_INTO_INCHES);
    }

    private static double convert(double value, double mulitplier) {
        if (value == -2.147483648E9d) {
            return -2.147483648E9d;
        }
        return value * mulitplier;
    }

    private static float convert(float value, float multiplier) {
        if (value == -2.14748365E9f) {
            return -2.14748365E9f;
        }
        return value * multiplier;
    }

    public static String getUnitOfDistance(Context context) {
        return getUnitOfDistance(context, false);
    }

    public static String getUnitOfDistance(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_distance_metric_short_vert : R.string.unit_distance_imperial_short_vert);
        }
        return context.getString(Utility.isMetric() ? R.string.unit_distance_metric_short : R.string.unit_distance_imperial_short);
    }

    public static String getUnitOfDistanceTTS(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
    }

    public static String getUnitOfDistanceSmall(Context context) {
        return getUnitOfDistance(context, false);
    }

    public static String getUnitOfDistanceSmall(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_distance_small_metric_short_vert : R.string.unit_distance_small_imperial_short_vert);
        }
        return context.getString(Utility.isMetric() ? R.string.unit_distance_small_metric_short : R.string.unit_distance_small_imperial_short);
    }

    public static String getUnitOfDistanceSmallTTS(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_distance_metric_small : R.string.unit_distance_yards);
    }

    public static String getUnitOfLength(Context context) {
        return getUnitOfLength(context, false);
    }

    public static String getUnitOfLength(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_length_metric_short_upper : R.string.unit_length_imperial_short_vert);
        }
        return context.getString(Utility.isMetric() ? R.string.unit_length_metric_short : R.string.unit_length_imperial_short);
    }

    public static String getUnitOfLengthSmall(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_length_metric_small : R.string.unit_length_imperial_small);
    }

    public static String getUnitOfLengthTTS(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_length_metric : R.string.unit_length_imperial);
    }

    public static String getUnitOfSpeed(Context context) {
        return getUnitOfSpeed(context, false);
    }

    public static String getUnitOfSpeed(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_speed_metric_short_vert : R.string.unit_speed_imperial_short_vert);
        }
        return context.getString(Utility.isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short);
    }

    public static String getUnitOfStride(Context context) {
        return getUnitOfStride(context, false);
    }

    public static String getUnitOfStride(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_cm_vert : R.string.unit_inches_vert);
        }
        return context.getString(Utility.isMetric() ? R.string.unit_cm : R.string.unit_inches);
    }

    public static String getUnitOfStrideLong(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_cm_long : R.string.unit_inches_long);
    }

    public static String getUnitOfPace(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_pace_slash_metric_short : R.string.unit_pace_slash_imperial_short);
    }

    public static String getUnitOfPace(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_pace_metric_short_vert : R.string.unit_pace_imperial_short_vert);
        }
        return getUnitOfPace(context);
    }

    public static String getUnitOfPaceTTS(Context context) {
        return context.getString(Utility.isMetric() ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
    }

    public static String getUnitOfPaceShort(Context context) {
        return getUnitOfPaceShort(context, false);
    }

    public static String getUnitOfPaceShort(Context context, boolean vertical) {
        if (vertical) {
            return context.getString(Utility.isMetric() ? R.string.unit_pace_metric_short_vert : R.string.unit_pace_imperial_short_vert);
        }
        return getUnitOfPace(context).toUpperCase();
    }
}
