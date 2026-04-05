package com.google.android.gms.flags.impl;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes67.dex */
final class zzk implements Callable<SharedPreferences> {
    private /* synthetic */ Context zztI;

    zzk(Context context) {
        this.zztI = context;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ SharedPreferences call() throws Exception {
        return this.zztI.getSharedPreferences("google_sdk_flags", 0);
    }
}
