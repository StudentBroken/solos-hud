package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcju extends zzcgd {
    private /* synthetic */ zzcjp zzbue;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzcju(zzcjp zzcjpVar, zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbue = zzcjpVar;
    }

    @Override // com.google.android.gms.internal.zzcgd
    public final void run() {
        this.zzbue.zzwE().zzyx().log("Tasks have been queued for a long time");
    }
}
