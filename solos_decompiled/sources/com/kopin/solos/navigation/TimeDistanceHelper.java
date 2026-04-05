package com.kopin.solos.navigation;

import android.content.Context;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.settings.Prefs;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes37.dex */
public class TimeDistanceHelper {
    public static List<String> speedToUnits(double metrespersecond) {
        List<String> returner = new ArrayList<>();
        switch (getUnits()) {
            case METRIC:
                double theSpeed = metrespersecond * 3.5999999046325684d;
                returner.add(String.format("%.2f", Double.valueOf(theSpeed)));
                returner.add("Km/h");
                break;
        }
        double theSpeed2 = metrespersecond * 2.236999988555908d;
        returner.add(String.format("%.2f", Double.valueOf(theSpeed2)));
        returner.add("mph");
        return returner;
    }

    public static String metresToKilometresString(double metres, boolean shortForm) {
        double distanceInKilometres = metres / 1000.0d;
        return String.format(shortForm ? "%.1f" : "%.2f", Double.valueOf(distanceInKilometres));
    }

    private static Prefs.UnitSystem getUnits() {
        return Prefs.getUnitSystem();
    }

    private static String metresToMilesString(double distanceToGo, boolean shortForm) {
        double distanceInLargerUnit = distanceToGo / 1609.343994140625d;
        return String.format(shortForm ? "%.1f" : "%.2f", Double.valueOf(distanceInLargerUnit));
    }

    public static int metresToFeet(double metres) {
        return (int) (3.2799999713897705d * metres);
    }

    public static List<String> metresToUnits(Context context, double metres) {
        return metresToUnits(context, metres, false);
    }

    public static List<String> metresToUnits(Context context, double metres, boolean TTS) {
        List<String> returner = new ArrayList<>();
        switch (getUnits()) {
            case METRIC:
                if (TTS) {
                    metres = (((int) metres) / 5) * 5;
                }
                if (metres > 999.0d) {
                    returner.add(metresToKilometresString(metres, true));
                    returner.add(context.getString(TTS ? R.string.nav_tts_distance_km : R.string.unit_distance_metric_short));
                } else {
                    returner.add(((int) metres) + "");
                    returner.add(context.getString(TTS ? R.string.nav_tts_distance_m : R.string.unit_length_metric_short));
                }
                break;
        }
        int feet = metresToFeet(metres);
        if (TTS) {
            feet = (feet / 5) * 5;
        }
        if (feet > 999) {
            returner.add(metresToMilesString(metres, true));
            returner.add(context.getString(TTS ? R.string.nav_tts_distance_miles : R.string.unit_distance_imperial_short));
        } else {
            returner.add(feet + "");
            returner.add(context.getString(TTS ? R.string.nav_tts_distance_feet : R.string.unit_length_imperial_short));
        }
        return returner;
    }

    public static List<String> metresToFullUnits(Context context, double metres) {
        List<String> returner = new ArrayList<>();
        switch (getUnits()) {
            case METRIC:
                if (metres > 999.0d) {
                    returner.add(metresToKilometresString(metres, false));
                    returner.add(context.getString(R.string.unit_distance_metric));
                } else {
                    returner.add(((int) metres) + "");
                    returner.add(context.getString(R.string.unit_distance_metric_small));
                }
                break;
        }
        int feet = metresToFeet(metres);
        if (feet > 999) {
            returner.add(metresToMilesString(metres, false));
            returner.add(context.getString(R.string.unit_distance_imperial));
        } else {
            returner.add(feet + "");
            returner.add(context.getString(R.string.unit_distance_imperial_small));
        }
        return returner;
    }

    public static String secondsToString(int seconds) {
        Date date = new Date(seconds * 1000);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static List<String> kphToUnits(Context context, double speed) {
        List<String> returner = new ArrayList<>();
        switch (getUnits()) {
            case METRIC:
                returner.add(String.format("%.2f", Double.valueOf(speed)));
                returner.add(context.getString(R.string.unit_speed_metric_short));
                break;
        }
        double theSpeed = speed * 0.6213709712028503d;
        returner.add(String.format("%.2f", Double.valueOf(theSpeed)));
        returner.add(context.getString(R.string.unit_speed_imperial_short));
        return returner;
    }
}
