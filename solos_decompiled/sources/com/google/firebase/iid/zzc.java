package com.google.firebase.iid;

import android.content.Intent;

/* JADX INFO: loaded from: classes56.dex */
final class zzc implements Runnable {
    private /* synthetic */ Intent val$intent;
    private /* synthetic */ Intent zzcna;
    private /* synthetic */ zzb zzcnb;

    zzc(zzb zzbVar, Intent intent, Intent intent2) {
        this.zzcnb = zzbVar;
        this.val$intent = intent;
        this.zzcna = intent2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzcnb.handleIntent(this.val$intent);
        this.zzcnb.zzm(this.zzcna);
    }
}
