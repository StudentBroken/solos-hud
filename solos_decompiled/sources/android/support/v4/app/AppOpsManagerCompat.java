package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes27.dex */
public final class AppOpsManagerCompat {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_IGNORED = 1;

    private AppOpsManagerCompat() {
    }

    public static String permissionToOp(@NonNull String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return AppOpsManager.permissionToOp(permission);
        }
        return null;
    }

    public static int noteOp(@NonNull Context context, @NonNull String op, int uid, @NonNull String packageName) {
        if (Build.VERSION.SDK_INT < 23) {
            return 1;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteOp(op, uid, packageName);
    }

    public static int noteProxyOp(@NonNull Context context, @NonNull String op, @NonNull String proxiedPackageName) {
        if (Build.VERSION.SDK_INT < 23) {
            return 1;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteProxyOp(op, proxiedPackageName);
    }
}
