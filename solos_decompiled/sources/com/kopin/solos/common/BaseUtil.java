package com.kopin.solos.common;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.kopin.solos.common.log.LogEvent;
import com.kopin.solos.common.log.RemoteLog;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import java.util.List;

/* JADX INFO: loaded from: classes52.dex */
public class BaseUtil {
    public static void onStart(Activity activity, Permission... mStartPermissions) {
        if (!activity.isFinishing()) {
            RemoteLog.track(LogEvent.START, activity.getLocalClassName());
            PermissionUtil.askPermission(activity, mStartPermissions);
        }
    }

    public static void event(LogEvent logEvent, Activity activity) {
        if (!activity.isFinishing()) {
            RemoteLog.track(logEvent, activity.getLocalClassName());
        }
    }

    public static Fragment getVisibleSupportFragment(FragmentActivity activity) {
        List<Fragment> fragments;
        if (activity != null && !activity.isFinishing() && (fragments = activity.getSupportFragmentManager().getFragments()) != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public static android.app.Fragment getVisibleFragment(Activity activity, int containerId) {
        android.app.Fragment fragment;
        if (activity == null || activity.isFinishing() || (fragment = activity.getFragmentManager().findFragmentById(containerId)) == null || !fragment.isVisible()) {
            return null;
        }
        return fragment;
    }
}
