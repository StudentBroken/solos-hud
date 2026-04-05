package com.kopin.pupil.util;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/* JADX INFO: loaded from: classes25.dex */
public class FileUtil {
    public static File getExternalFile(Context context, String filename) {
        return new File(getExternalPath(context), filename);
    }

    public static File getExternalPath(Context context) {
        return isExternalStorageWritable() ? context.getExternalFilesDir(null) : context.getFilesDir();
    }

    private static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static File getStorageDirectory(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return context.getExternalFilesDir(null);
        }
        return context.getFilesDir();
    }

    public static String getStoragePath(Context context) {
        return getStorageDirectory(context).getAbsolutePath();
    }

    public static File getLogFile(Context context, String prefix) {
        Calendar now = Calendar.getInstance();
        String name = String.format("%s_%d_%d-%d_%d.log", prefix, Integer.valueOf(now.get(5) + 1), Integer.valueOf(now.get(2)), Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)));
        return new File(getStorageDirectory(context), name);
    }

    public static void writeLogLine(File logFile, String line) {
        if (logFile != null) {
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (logFile.canWrite()) {
                try {
                    try {
                        FileOutputStream out = new FileOutputStream(logFile, true);
                        try {
                            OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
                            writer.append((CharSequence) line);
                            writer.append('\n');
                            writer.close();
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        out.close();
                    } catch (FileNotFoundException e4) {
                        e4.printStackTrace();
                    }
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }
}
