package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.internal.zzcbs;

/* JADX INFO: loaded from: classes67.dex */
public final class zzf extends zza<Long> {
    public static Long zza(SharedPreferences sharedPreferences, String str, Long l) {
        try {
            return (Long) zzcbs.zzb(new zzg(sharedPreferences, str, l));
        } catch (Exception e) {
            String strValueOf = String.valueOf(e.getMessage());
            Log.w("FlagDataUtils", strValueOf.length() != 0 ? "Flag value not available, returning default: ".concat(strValueOf) : new String("Flag value not available, returning default: "));
            return l;
        }
    }
}
