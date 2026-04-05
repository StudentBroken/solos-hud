package android.support.wearable.view;

import android.content.Context;
import android.support.annotation.FractionRes;

/* JADX INFO: loaded from: classes33.dex */
public final class ResourcesUtil {
    public static int getScreenWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getFractionOfScreenPx(Context context, int screenPx, @FractionRes int resId) {
        float marginPercent = context.getResources().getFraction(resId, 1, 1);
        return (int) (screenPx * marginPercent);
    }

    private ResourcesUtil() {
    }
}
