package com.ua.sdk.util;

import android.content.Context;
import com.ua.sdk.UaLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/* JADX INFO: loaded from: classes65.dex */
public class FileUtil {
    public static File getFile(Context context, String subDirectory, String fileName) throws FileNotFoundException {
        String filePath = getFullPathToFile(context.getFilesDir().toString(), subDirectory, fileName);
        return new File(filePath);
    }

    public static FileOutputStream openFileOutput(Context context, String subDirectory, String fileName) throws FileNotFoundException {
        File f = getFile(context, subDirectory, fileName);
        try {
            return new FileOutputStream(f, false);
        } catch (FileNotFoundException e) {
            UaLog.info("Could not open file " + fileName + " for writing, creating parent dir");
            File parent = f.getParentFile();
            parent.mkdirs();
            parent.setWritable(true);
            return new FileOutputStream(f, false);
        }
    }

    public static FileInputStream openFileInput(Context context, String subDirectory, String fileName) throws FileNotFoundException {
        String filePath = getFullPathToFile(context.getFilesDir().toString(), subDirectory, fileName);
        return new FileInputStream(new File(filePath));
    }

    private static String getFullPathToFile(String baseDirectory, String subDirectory, String fileName) {
        UaLog.debug("Asked for full path to %s in %s", fileName, subDirectory);
        String result = baseDirectory + File.separator + subDirectory + File.separator + fileName;
        return result;
    }
}
