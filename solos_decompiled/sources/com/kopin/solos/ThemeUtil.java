package com.kopin.solos;

import android.content.Context;
import android.util.TypedValue;
import com.kopin.solos.common.SportType;

/* JADX INFO: loaded from: classes37.dex */
public class ThemeUtil {
    public static CharSequence getString(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.string;
        }
        return null;
    }

    public static int getResourceId(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.resourceId;
        }
        return 0;
    }

    public static int getTheme(SportType sportType) {
        switch (sportType) {
            case RIDE:
                return com.kopin.solos.core.R.style.Theme_Ride;
            case RUN:
                return com.kopin.solos.core.R.style.Theme_Run;
            default:
                return 0;
        }
    }

    public static int getSportNameRes(SportType sportType) {
        switch (sportType) {
            case RIDE:
                return com.kopin.solos.core.R.string.ride;
            case RUN:
                return com.kopin.solos.core.R.string.run;
            default:
                return 0;
        }
    }

    public static int getSportIconRes(SportType sportType) {
        switch (sportType) {
            case RIDE:
                return com.kopin.solos.core.R.drawable.ic_bike;
            case RUN:
                return com.kopin.solos.core.R.drawable.ic_run_sport;
            default:
                return 0;
        }
    }
}
