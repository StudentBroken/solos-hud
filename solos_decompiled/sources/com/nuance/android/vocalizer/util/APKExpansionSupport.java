package com.nuance.android.vocalizer.util;

import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

/* JADX INFO: loaded from: classes16.dex */
public class APKExpansionSupport {
    private static final String EXP_PATH = "/Android/obb/";

    public static String getSDDirPath() {
        String absolutePath;
        Iterator it = Arrays.asList("ext_card", "external_sd", "ext_sd", "external", "extSdCard", "externalSdCard").iterator();
        while (true) {
            if (!it.hasNext()) {
                absolutePath = null;
                break;
            }
            File file = new File("/mnt/", (String) it.next());
            if (file.isDirectory() && file.canWrite()) {
                absolutePath = file.getAbsolutePath();
                break;
            }
        }
        return absolutePath == null ? Environment.getExternalStorageDirectory().getAbsolutePath() : absolutePath;
    }

    public static String[] getAPKExpansionFiles(String str, int i, int i2) {
        Vector vector = new Vector();
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(Environment.getExternalStorageDirectory().toString() + EXP_PATH + str);
            if (!file.exists()) {
                file = new File(getSDDirPath() + EXP_PATH + str);
            }
            if (file.exists()) {
                if (i > 0) {
                    String str2 = file + File.separator + "main." + i + "." + str + ".obb";
                    File file2 = new File(str2);
                    if (file2 != null && file2.exists() && file2.isFile()) {
                        vector.add(str2);
                    }
                }
                if (i2 > 0) {
                    String str3 = file + File.separator + "patch." + i + "." + str + ".obb";
                    File file3 = new File(str3);
                    if (file3 != null && file3.exists() && file3.isFile()) {
                        vector.add(str3);
                    }
                }
            }
        }
        String[] strArr = new String[vector.size()];
        vector.toArray(strArr);
        return strArr;
    }

    public static ZipResourceFile getAPKExpansionZipFile(String str, int i, int i2) throws IOException {
        String[] aPKExpansionFiles = getAPKExpansionFiles(str, i, i2);
        ZipResourceFile zipResourceFile = null;
        int length = aPKExpansionFiles.length;
        int i3 = 0;
        while (i3 < length) {
            ZipResourceFile zipResourceFile2 = new ZipResourceFile(aPKExpansionFiles[i3], zipResourceFile);
            i3++;
            zipResourceFile = zipResourceFile2;
        }
        return zipResourceFile;
    }
}
