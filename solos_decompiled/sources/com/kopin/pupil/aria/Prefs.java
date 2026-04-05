package com.kopin.pupil.aria;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes43.dex */
public class Prefs {
    private static final String VOCON_ALWAYS_ON = "2";
    private static final String VOCON_DOUBLE_TAP = "1";
    public static final String VOCON_KEY = "vocon";
    private static final String VOCON_OFF = "0";
    private static SharedPreferences prefs;

    public enum AriaMode {
        OFF,
        DOUBLE_TAP,
        ALWAYS_ON;

        /* JADX INFO: Access modifiers changed from: private */
        public static AriaMode fromValue(String val) {
            int ord = val != null ? Integer.parseInt(val) : -1;
            if (ord < 0 || ord >= values().length) {
                return OFF;
            }
            return values()[ord];
        }
    }

    static void init(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setMode(AriaMode mode) {
        if (prefs != null) {
            prefs.edit().putString(VOCON_KEY, Integer.toString(mode.ordinal())).commit();
        }
    }

    public static AriaMode getMode() {
        if (prefs != null) {
            return AriaMode.fromValue(prefs.getString(VOCON_KEY, null));
        }
        return AriaMode.OFF;
    }

    public static boolean isVoconEnabled() {
        return !getMode().equals(AriaMode.OFF);
    }

    public static boolean isDoubleTapEnabled() {
        return getMode().equals(AriaMode.DOUBLE_TAP);
    }

    public static boolean isVoconAlwaysOn() {
        return getMode().equals(AriaMode.ALWAYS_ON);
    }
}
