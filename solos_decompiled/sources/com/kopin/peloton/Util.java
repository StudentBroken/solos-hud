package com.kopin.peloton;

import android.util.Log;

/* JADX INFO: loaded from: classes61.dex */
public class Util {
    public static final int LOG_TEXT_CHUNK = 4000;
    public static final double NO_DATA_D = -2.147483648E9d;
    public static final long NO_DATA_L = -2147483648L;

    public static void logLongString(String tag, String text) {
        if (text != null && !text.isEmpty()) {
            Log.d(tag, " __ long data start __");
            for (int index = 0; index < text.length(); index += 4000) {
                Log.d(tag, text.substring(index, Math.min(index + 4000, text.length())));
            }
            Log.d(tag, " __ long data end __");
        }
    }
}
