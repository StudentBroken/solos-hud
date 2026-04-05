package com.google.android.gms.internal;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes36.dex */
final class zzchz implements Callable<String> {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzchx zzbtc;

    zzchz(zzchx zzchxVar, String str) {
        this.zzbtc = zzchxVar;
        this.zzbjl = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ String call() throws Exception {
        zzcfs zzcfsVarZzdR = this.zzbtc.zzwy().zzdR(this.zzbjl);
        if (zzcfsVarZzdR != null) {
            return zzcfsVarZzdR.getAppInstanceId();
        }
        this.zzbtc.zzwE().zzyx().log("App info was null when attempting to get app instance id");
        return null;
    }
}
