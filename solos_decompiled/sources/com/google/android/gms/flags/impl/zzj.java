package com.google.android.gms.flags.impl;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.internal.zzcbs;

/* JADX INFO: loaded from: classes67.dex */
public final class zzj {
    private static SharedPreferences zzaXU = null;

    public static SharedPreferences zzaW(Context context) throws Exception {
        SharedPreferences sharedPreferences;
        synchronized (SharedPreferences.class) {
            if (zzaXU == null) {
                zzaXU = (SharedPreferences) zzcbs.zzb(new zzk(context));
            }
            sharedPreferences = zzaXU;
        }
        return sharedPreferences;
    }
}
