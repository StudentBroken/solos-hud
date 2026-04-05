package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;

/* JADX INFO: loaded from: classes36.dex */
final class zzckn extends zzcgd {
    private /* synthetic */ zzckm zzbuy;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzckn(zzckm zzckmVar, zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbuy = zzckmVar;
    }

    @Override // com.google.android.gms.internal.zzcgd
    @WorkerThread
    public final void run() {
        this.zzbuy.zzzm();
    }
}
