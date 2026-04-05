package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes67.dex */
final class zzc implements Callable<Boolean> {
    private /* synthetic */ SharedPreferences zzaXO;
    private /* synthetic */ String zzaXP;
    private /* synthetic */ Boolean zzaXQ;

    zzc(SharedPreferences sharedPreferences, String str, Boolean bool) {
        this.zzaXO = sharedPreferences;
        this.zzaXP = str;
        this.zzaXQ = bool;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Boolean call() throws Exception {
        return Boolean.valueOf(this.zzaXO.getBoolean(this.zzaXP, this.zzaXQ.booleanValue()));
    }
}
