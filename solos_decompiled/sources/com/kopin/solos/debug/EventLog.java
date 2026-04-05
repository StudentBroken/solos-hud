package com.kopin.solos.debug;

import android.content.Context;
import android.util.Log;
import com.kopin.solos.common.CommonFileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/* JADX INFO: loaded from: classes52.dex */
public class EventLog {
    private static File mCurrentLog;

    public enum ModuleTag {
        CORE,
        SENSORS,
        HEADSET,
        RECORD
    }

    private static File getLogFile(Context context, String prefix) {
        Calendar now = Calendar.getInstance();
        String name = String.format("%s_%d_%d-%d_%d.log", prefix, Integer.valueOf(now.get(5) + 1), Integer.valueOf(now.get(2)), Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)));
        File log = CommonFileUtil.getExternalFile(context, name);
        if (log.exists()) {
            int suffix = 1;
            while (log.exists()) {
                String name2 = String.format("%s_%d_%d-%d_%d_%d.log", prefix, Integer.valueOf(now.get(5) + 1), Integer.valueOf(now.get(2)), Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)), Integer.valueOf(suffix));
                log = CommonFileUtil.getExternalFile(context, name2);
                suffix++;
            }
        }
        return log;
    }

    private static void writeLogLine(File logFile, String line) {
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
                            Calendar now = Calendar.getInstance();
                            String time = String.format("%02d:%02d:%02d.%03d", Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)), Integer.valueOf(now.get(13)), Integer.valueOf(now.get(14)));
                            writer.append((CharSequence) time);
                            writer.append('\t');
                            writer.append((CharSequence) line);
                            writer.append('\n');
                            writer.close();
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        out.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                } catch (FileNotFoundException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    public static void clear() {
        if (mCurrentLog != null && mCurrentLog.exists()) {
            Log.d("SolosLog", "Removing log: " + mCurrentLog.getAbsolutePath());
            mCurrentLog.delete();
        }
    }

    public static void start(Context context) {
        mCurrentLog = getLogFile(context, "debug");
        if (mCurrentLog != null) {
            Log.d("SolosLog", "New log started: " + mCurrentLog.getAbsolutePath());
        } else {
            Log.e("SolosLog", "Unable to start new log!");
        }
    }

    public static void stop() {
        if (mCurrentLog != null) {
            Log.d("SolosLog", "Log complete: " + mCurrentLog.getAbsolutePath());
            mCurrentLog = null;
        }
    }

    public static void d(ModuleTag tag, String msg) {
        if (mCurrentLog != null) {
            writeLogLine(mCurrentLog, String.format("%s:\t%s", tag.name(), msg));
        }
        Log.d("SolosLog", String.format("%s:\t%s", tag.name(), msg));
    }
}
