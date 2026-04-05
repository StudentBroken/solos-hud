package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.util.zzl;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zze {
    private static zze zzaJC = new zze();
    private static Boolean zzaJD;

    public static void zza(Context context, String str, int i, String str2, String str3, String str4, int i2, List<String> list) {
        zza(context, str, 8, str2, str3, str4, i2, list, 0L);
    }

    public static void zza(Context context, String str, int i, String str2, String str3, String str4, int i2, List<String> list, long j) {
        List<String> list2;
        if (zzaJD == null) {
            zzaJD = false;
        }
        if (zzaJD.booleanValue()) {
            if (TextUtils.isEmpty(str)) {
                String strValueOf = String.valueOf(str);
                Log.e("WakeLockTracker", strValueOf.length() != 0 ? "missing wakeLock key. ".concat(strValueOf) : new String("missing wakeLock key. "));
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (7 == i || 8 == i || 10 == i || 11 == i) {
                if (list == null || list.size() != 1) {
                    list2 = list;
                } else {
                    if ("com.google.android.gms".equals(list.get(0))) {
                        list = null;
                    }
                    list2 = list;
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                int iZzaK = zzl.zzaK(context);
                String packageName = context.getPackageName();
                if ("com.google.android.gms".equals(packageName)) {
                    packageName = null;
                }
                try {
                    context.startService(new Intent().setComponent(zzb.zzaJh).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", new WakeLockEvent(jCurrentTimeMillis, i, str2, i2, list2, str, jElapsedRealtime, iZzaK, str3, packageName, zzl.zzaL(context), j, str4)));
                } catch (Exception e) {
                    Log.wtf("WakeLockTracker", e);
                }
            }
        }
    }

    public static zze zzrW() {
        return zzaJC;
    }
}
