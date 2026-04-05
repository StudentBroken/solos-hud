package com.kopin.solos.common.permission;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.kopin.solos.common.DialogUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes52.dex */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public enum GrantResult {
        GRANTED,
        DENIED,
        UNSPECIFIED
    }

    public static boolean permitted(Context context, Permission... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (Permission permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission.manifestPermission) != 0) {
                Log.i(TAG, "permitted " + permission.name() + " false");
                return false;
            }
            if (Build.VERSION.SDK_INT >= 29 && permission.equals(Permission.ACCESS_FINE_LOCATION) && ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_BACKGROUND_LOCATION") != 0) {
                Log.i(TAG, "permitted " + permission.name() + " (BACKGROUND extra) false");
                return false;
            }
        }
        Log.i(TAG, "permitted true");
        return true;
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
            if (Build.VERSION.SDK_INT >= 29 && permission.equals(Permission.ACCESS_FINE_LOCATION) && ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_BACKGROUND_LOCATION") != 0) {
                newPermissions.add("android.permission.ACCESS_BACKGROUND_LOCATION");
            }
        }
        if (newPermissions.size() <= 0) {
            return false;
        }
        ActivityCompat.requestPermissions(activity, (String[]) newPermissions.toArray(new String[newPermissions.size()]), callbackId);
        return true;
    }

    public static boolean askPermissionInfo(Activity activity, int title, int message, Permission permission) {
        return askPermissionInfo(activity, activity.getString(title), activity.getString(message), permission);
    }

    public static boolean askPermissionInfo(final Activity activity, String title, String message, final Permission permission) {
        if (Build.VERSION.SDK_INT < 23 || permission == null || permitted(activity, permission)) {
            return false;
        }
        Log.i(TAG, "askPermissionInfo " + permission.name() + " by " + activity.getLocalClassName());
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.manifestPermission)) {
            Log.i(TAG, "askPermissionInfo showing the info dialog");
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(title);
            alertBuilder.setMessage(message);
            alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.common.permission.PermissionUtil.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission.manifestPermission}, permission.ordinal());
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
            DialogUtils.setDialogTitleDivider(alert);
            return true;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission.manifestPermission}, permission.ordinal());
        return true;
    }

    public static boolean granted(Permission permission, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT < 23) {
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
        if (Build.VERSION.SDK_INT < 23) {
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
