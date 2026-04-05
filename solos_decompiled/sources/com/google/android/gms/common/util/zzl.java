package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.SystemClock;
import com.google.firebase.analytics.FirebaseAnalytics;

/* JADX INFO: loaded from: classes67.dex */
public final class zzl {
    private static long zzaJR;
    private static IntentFilter zzaJQ = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    private static float zzaJS = Float.NaN;

    @TargetApi(20)
    public static int zzaK(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }
        Intent intentRegisterReceiver = context.getApplicationContext().registerReceiver(null, zzaJQ);
        boolean z = ((intentRegisterReceiver == null ? 0 : intentRegisterReceiver.getIntExtra("plugged", 0)) & 7) != 0;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            return -1;
        }
        return ((zzs.zzsc() ? powerManager.isInteractive() : powerManager.isScreenOn() ? 1 : 0) << 1) | (z ? 1 : 0);
    }

    public static synchronized float zzaL(Context context) {
        float f;
        if (SystemClock.elapsedRealtime() - zzaJR >= 60000 || Float.isNaN(zzaJS)) {
            if (context.getApplicationContext().registerReceiver(null, zzaJQ) != null) {
                zzaJS = r0.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1) / r0.getIntExtra("scale", -1);
            }
            zzaJR = SystemClock.elapsedRealtime();
            f = zzaJS;
        } else {
            f = zzaJS;
        }
        return f;
    }
}
