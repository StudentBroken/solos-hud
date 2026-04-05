package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes27.dex */
public final class ConfigurationHelper {
    private ConfigurationHelper() {
    }

    @Deprecated
    public static int getScreenHeightDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenHeightDp;
    }

    @Deprecated
    public static int getScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenWidthDp;
    }

    @Deprecated
    public static int getSmallestScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp;
    }

    public static int getDensityDpi(@NonNull Resources resources) {
        return Build.VERSION.SDK_INT >= 17 ? resources.getConfiguration().densityDpi : resources.getDisplayMetrics().densityDpi;
    }
}
