package com.google.analytics.tracking.android;

import android.os.Build;
import java.io.File;

/* JADX INFO: loaded from: classes49.dex */
class FutureApis {
    private FutureApis() {
    }

    public static int version() {
        try {
            int version = Integer.parseInt(Build.VERSION.SDK);
            return version;
        } catch (NumberFormatException e) {
            Log.e("Invalid version number: " + Build.VERSION.SDK);
            return 0;
        }
    }

    static boolean setOwnerOnlyReadWrite(String path) {
        if (version() < 9) {
            return false;
        }
        File file = new File(path);
        file.setReadable(false, false);
        file.setWritable(false, false);
        file.setReadable(true, true);
        file.setWritable(true, true);
        return true;
    }
}
