package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes67.dex */
final class zzg implements Callable<Long> {
    private /* synthetic */ SharedPreferences zzaXO;
    private /* synthetic */ String zzaXP;
    private /* synthetic */ Long zzaXS;

    zzg(SharedPreferences sharedPreferences, String str, Long l) {
        this.zzaXO = sharedPreferences;
        this.zzaXP = str;
        this.zzaXS = l;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Long call() throws Exception {
        return Long.valueOf(this.zzaXO.getLong(this.zzaXP, this.zzaXS.longValue()));
    }
}
