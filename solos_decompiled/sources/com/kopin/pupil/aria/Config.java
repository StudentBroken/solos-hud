package com.kopin.pupil.aria;

import android.content.Context;

/* JADX INFO: loaded from: classes43.dex */
public class Config {
    public static final boolean ALLOW_DIAL_NUMBERS = false;
    public static final boolean ALLOW_DICTATION = false;
    public static final boolean ARIA_CALLS_ENABLED = true;
    public static final boolean ARIA_MESSAGES_ENABLED = false;
    static final boolean IS_VOCON_ENABLED = true;
    static final boolean USE_HEAD_COMMANDS = true;
    static boolean SLEEP_ON_FINISHED = true;
    static boolean SLEEP_ON_INACTIVITY = false;
    static boolean HOME_ON_INACTIVITY = true;
    static long INACTIVITY_TIMEOUT = 15000;
    static boolean SLEEP_ON_APP_FINISHED = false;
    static boolean ALLOW_LOCAL_PLAYBACK = false;
    static boolean ALLOW_LOCAL_RECORD = false;
    static boolean IS_PROVISIONED = false;

    static void init(Context ctx) {
        IS_PROVISIONED = ctx.getSharedPreferences("Aria", 0).getBoolean("provisioned", false);
    }

    public static void setProvisioned(Context context, boolean isProvisioned) {
        context.getSharedPreferences("Aria", 0).edit().putBoolean("provisioned", isProvisioned).commit();
        IS_PROVISIONED = isProvisioned;
    }
}
