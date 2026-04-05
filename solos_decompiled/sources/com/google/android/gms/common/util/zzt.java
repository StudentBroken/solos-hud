package com.google.android.gms.common.util;

import android.os.Process;
import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzt {
    private static String zzaKa = null;
    private static final int zzaKb = Process.myPid();

    private static String zzaD(int i) throws Throwable {
        BufferedReader bufferedReader;
        Throwable th;
        StrictMode.ThreadPolicy threadPolicyAllowThreadDiskReads;
        String strTrim = null;
        if (i > 0) {
            try {
                threadPolicyAllowThreadDiskReads = StrictMode.allowThreadDiskReads();
                try {
                    bufferedReader = new BufferedReader(new FileReader(new StringBuilder(25).append("/proc/").append(i).append("/cmdline").toString()));
                } finally {
                }
            } catch (IOException e) {
                bufferedReader = null;
            } catch (Throwable th2) {
                bufferedReader = null;
                th = th2;
            }
            try {
                StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskReads);
                strTrim = bufferedReader.readLine().trim();
                zzp.closeQuietly(bufferedReader);
            } catch (IOException e2) {
                zzp.closeQuietly(bufferedReader);
            } catch (Throwable th3) {
                th = th3;
                zzp.closeQuietly(bufferedReader);
                throw th;
            }
        }
        return strTrim;
    }

    public static String zzse() {
        if (zzaKa == null) {
            zzaKa = zzaD(zzaKb);
        }
        return zzaKa;
    }
}
