package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes67.dex */
final class zze implements Callable<Integer> {
    private /* synthetic */ SharedPreferences zzaXO;
    private /* synthetic */ String zzaXP;
    private /* synthetic */ Integer zzaXR;

    zze(SharedPreferences sharedPreferences, String str, Integer num) {
        this.zzaXO = sharedPreferences;
        this.zzaXP = str;
        this.zzaXR = num;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Integer call() throws Exception {
        return Integer.valueOf(this.zzaXO.getInt(this.zzaXP, this.zzaXR.intValue()));
    }
}
