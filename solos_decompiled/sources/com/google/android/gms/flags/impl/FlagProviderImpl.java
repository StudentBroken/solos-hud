package com.google.android.gms.flags.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.internal.zzcbp;

/* JADX INFO: loaded from: classes67.dex */
@DynamiteApi
public class FlagProviderImpl extends zzcbp {
    private SharedPreferences zzBW;
    private boolean zzuK = false;

    @Override // com.google.android.gms.internal.zzcbo
    public boolean getBooleanFlagValue(String str, boolean z, int i) {
        return !this.zzuK ? z : zzb.zza(this.zzBW, str, Boolean.valueOf(z)).booleanValue();
    }

    @Override // com.google.android.gms.internal.zzcbo
    public int getIntFlagValue(String str, int i, int i2) {
        return !this.zzuK ? i : zzd.zza(this.zzBW, str, Integer.valueOf(i)).intValue();
    }

    @Override // com.google.android.gms.internal.zzcbo
    public long getLongFlagValue(String str, long j, int i) {
        return !this.zzuK ? j : zzf.zza(this.zzBW, str, Long.valueOf(j)).longValue();
    }

    @Override // com.google.android.gms.internal.zzcbo
    public String getStringFlagValue(String str, String str2, int i) {
        return !this.zzuK ? str2 : zzh.zza(this.zzBW, str, str2);
    }

    @Override // com.google.android.gms.internal.zzcbo
    public void init(IObjectWrapper iObjectWrapper) {
        Context context = (Context) zzn.zzE(iObjectWrapper);
        if (this.zzuK) {
            return;
        }
        try {
            this.zzBW = zzj.zzaW(context.createPackageContext("com.google.android.gms", 0));
            this.zzuK = true;
        } catch (PackageManager.NameNotFoundException e) {
        } catch (Exception e2) {
            String strValueOf = String.valueOf(e2.getMessage());
            Log.w("FlagProviderImpl", strValueOf.length() != 0 ? "Could not retrieve sdk flags, continuing with defaults: ".concat(strValueOf) : new String("Could not retrieve sdk flags, continuing with defaults: "));
        }
    }
}
