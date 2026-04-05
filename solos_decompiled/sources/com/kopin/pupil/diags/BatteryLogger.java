package com.kopin.pupil.diags;

import android.content.Context;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/* JADX INFO: loaded from: classes25.dex */
public class BatteryLogger implements Runnable {
    private static final int FIVE_MINUTES = 300000;
    private static File mLogFile;
    private static long mStartTime;
    private static Thread self;

    @Override // java.lang.Runnable
    public void run() {
        while (mLogFile != null) {
            PupilDevice.requestStatus();
            try {
                Thread.sleep(300000L);
            } catch (InterruptedException e) {
            }
        }
    }

    public static String startLog(Context context) {
        Calendar now = Calendar.getInstance();
        String name = String.format("battery_%d_%d-%d_%d.log", Integer.valueOf(now.get(5)), Integer.valueOf(now.get(2)), Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)));
        mLogFile = new File(FileUtil.getStorageDirectory(context), name);
        try {
            mLogFile.createNewFile();
            mStartTime = System.currentTimeMillis();
            writeHeader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        self = new Thread(new BatteryLogger(), "Battery Logger");
        self.start();
        return name;
    }

    public static String stopLog() {
        if (mLogFile == null) {
            return null;
        }
        String name = mLogFile.getName();
        mLogFile = null;
        self.interrupt();
        self = null;
        return name;
    }

    public static boolean isActive() {
        return mLogFile != null;
    }

    private static void writeHeader() {
        try {
            try {
                FileOutputStream out = new FileOutputStream(mLogFile, true);
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
                    writer.append((CharSequence) "Time, Level\n");
                    writer.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                out.close();
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    public static void writeEntry(int level) {
        if (mLogFile != null) {
            try {
                try {
                    FileOutputStream out = new FileOutputStream(mLogFile, true);
                    try {
                        new OutputStreamWriter(out, "utf-8").append((CharSequence) Long.toString(System.currentTimeMillis() - mStartTime)).append((CharSequence) ", ").append((CharSequence) Integer.toString(level)).append((CharSequence) "\n").close();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    out.close();
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
    }
}
