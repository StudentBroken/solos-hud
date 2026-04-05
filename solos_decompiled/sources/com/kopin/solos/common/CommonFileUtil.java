package com.kopin.solos.common;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/* JADX INFO: loaded from: classes52.dex */
public class CommonFileUtil {
    public static File getExternalFile(Context context, String filename) {
        return new File(getExternalPath(context), filename);
    }

    public static File getExternalPath(Context context) {
        return isExternalStorageWritable() ? context.getExternalFilesDir(null) : context.getFilesDir();
    }

    private static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }
}
