package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.internal.zzcbs;

/* JADX INFO: loaded from: classes67.dex */
public final class zzb extends zza<Boolean> {
    public static Boolean zza(SharedPreferences sharedPreferences, String str, Boolean bool) {
        try {
            return (Boolean) zzcbs.zzb(new zzc(sharedPreferences, str, bool));
        } catch (Exception e) {
            String strValueOf = String.valueOf(e.getMessage());
            Log.w("FlagDataUtils", strValueOf.length() != 0 ? "Flag value not available, returning default: ".concat(strValueOf) : new String("Flag value not available, returning default: "));
            return bool;
        }
    }
}
