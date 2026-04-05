package com.kopin.solos.util;

import android.content.Context;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.util.Locale;

/* JADX INFO: loaded from: classes37.dex */
public class PaceUtil {
    public static String formatPace(double rawPace) {
        return formatPace(rawPace, 1);
    }

    public static String formatPace(double rawPace, int stepSeconds) {
        double paceInSeconds = Utility.round(Math.max(rawPace, 0.0d), stepSeconds);
        int mins = 0;
        int seconds = (int) paceInSeconds;
        if (paceInSeconds >= 60.0d) {
            mins = (int) (paceInSeconds / 60.0d);
            seconds = (int) (paceInSeconds % 60.0d);
        }
        return String.format(Locale.US, "%d:%02d", Integer.valueOf(mins), Integer.valueOf(seconds));
    }

    public static String formatPace(double rawPace, boolean showUnit, Context context) {
        return showUnit ? String.format("%s %s", formatPace(rawPace), Conversion.getUnitOfPace(context)) : formatPace(rawPace);
    }
}
