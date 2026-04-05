package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzckt extends zzcgd {
    private /* synthetic */ zzcks zzbuB;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzckt(zzcks zzcksVar, zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbuB = zzcksVar;
    }

    @Override // com.google.android.gms.internal.zzcgd
    public final void run() {
        this.zzbuB.zzwE().zzyB().log("Sending upload intent from DelayedRunnable");
        this.zzbuB.zzzp();
    }
}
