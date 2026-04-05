package com.kopin.pupil.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes25.dex */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public enum GrantResult {
        GRANTED,
        DENIED,
        UNSPECIFIED
    }

    public static boolean permitted(Context context, Permission permission) {
        boolean b = context.checkSelfPermission(permission.manifestPermission) == 0;
        Log.i(TAG, "permitted " + permission.name() + " " + b);
        return b;
    }

    public static boolean permittedAll(Context context, Permission... permissions) {
        boolean permitted = true;
        for (Permission permission : permissions) {
            if (!permitted(context, permission)) {
                permitted = false;
            }
        }
        return permitted;
    }

    public static boolean askPermission(Activity activity, Permission... permissions) {
        if (Build.VERSION.SDK_INT < 23 || permissions == null) {
            return false;
        }
        for (Permission permission : permissions) {
            if (!permitted(activity, permission)) {
                return askPermission(activity, permission.ordinal(), permissions);
            }
        }
        return false;
    }

    public static boolean askPermission(Activity activity, int callbackId, Permission... permissions) {
        if (Build.VERSION.SDK_INT < 23 || permissions == null) {
            return false;
        }
        List<String> newPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            if (!permitted(activity, permission)) {
                Log.i(TAG, "askPermission " + permission.name() + " by " + activity.getLocalClassName());
                newPermissions.add(permission.manifestPermission);
            }
        }
        if (newPermissions.size() <= 0) {
            return false;
        }
        activity.requestPermissions((String[]) newPermissions.toArray(new String[newPermissions.size()]), callbackId);
        return true;
    }

    public static boolean granted(Permission permission, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT <= 23) {
            return true;
        }
        List<String> permissionsList = Arrays.asList(permissions);
        int pos = permissionsList.indexOf(permission.manifestPermission);
        Log.i(TAG, "granted now " + permission.name() + " " + (pos >= 0 && grantResults[pos] == 0));
        return pos >= 0 && grantResults[pos] == 0;
    }

    public static boolean denied(Permission permission, String[] permissions, int[] grantResults) {
        List<String> permissionsList = Arrays.asList(permissions);
        return permissionsList.contains(permission.manifestPermission) && !granted(permission, permissions, grantResults);
    }

    public static GrantResult grantedState(Permission permission, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT <= 23) {
            return GrantResult.GRANTED;
        }
        List<String> permissionsList = Arrays.asList(permissions);
        int pos = permissionsList.indexOf(permission.manifestPermission);
        if (pos < 0) {
            return GrantResult.UNSPECIFIED;
        }
        if (grantResults[pos] == 0) {
            return GrantResult.GRANTED;
        }
        return GrantResult.DENIED;
    }
}
