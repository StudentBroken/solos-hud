package android.support.v4.view;

import android.R;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes27.dex */
@Deprecated
public final class ViewConfigurationCompat {
    private static final String TAG = "ViewConfigCompat";
    private static Method sGetScaledScrollFactorMethod;

    static {
        if (Build.VERSION.SDK_INT == 25) {
            try {
                sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor", new Class[0]);
            } catch (Exception e) {
                Log.i(TAG, "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
    }

    @Deprecated
    public static int getScaledPagingTouchSlop(ViewConfiguration config) {
        return config.getScaledPagingTouchSlop();
    }

    @Deprecated
    public static boolean hasPermanentMenuKey(ViewConfiguration config) {
        return config.hasPermanentMenuKey();
    }

    public static float getScaledHorizontalScrollFactor(@NonNull ViewConfiguration config, @NonNull Context context) {
        return Build.VERSION.SDK_INT >= 26 ? config.getScaledHorizontalScrollFactor() : getLegacyScrollFactor(config, context);
    }

    public static float getScaledVerticalScrollFactor(@NonNull ViewConfiguration config, @NonNull Context context) {
        return Build.VERSION.SDK_INT >= 26 ? config.getScaledVerticalScrollFactor() : getLegacyScrollFactor(config, context);
    }

    private static float getLegacyScrollFactor(ViewConfiguration config, Context context) {
        if (Build.VERSION.SDK_INT >= 25 && sGetScaledScrollFactorMethod != null) {
            try {
                return ((Integer) sGetScaledScrollFactorMethod.invoke(config, new Object[0])).intValue();
            } catch (Exception e) {
                Log.i(TAG, "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
        TypedValue outValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, outValue, true)) {
            return outValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return 0.0f;
    }

    private ViewConfigurationCompat() {
    }
}
