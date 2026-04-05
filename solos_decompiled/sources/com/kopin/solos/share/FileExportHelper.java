package com.kopin.solos.share;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.storage.SQLHelper;

/* JADX INFO: loaded from: classes4.dex */
public class FileExportHelper {
    private static final String TAG = "FileExportHelper";

    public static void login(Activity activity, int requestCode) {
    }

    public static void login(Fragment fragment, int requestCode) {
    }

    private static Intent getLoginIntent(Context context) {
        return null;
    }

    public static boolean isLoggedIn() {
        return true;
    }

    public static void logOut(Context context) {
    }

    public static boolean hasAutoShare(Context context) {
        return false;
    }

    public static boolean isShared(Context context, long rideId) {
        return false;
    }

    public static ShareHelper.ShareTask getShareTask(String id, Context context, SQLHelper sqlHelper) {
        return null;
    }
}
