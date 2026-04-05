package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdn extends zzbek {
    private /* synthetic */ zzbdm zzaDd;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbdn(zzbdm zzbdmVar, zzbei zzbeiVar) {
        super(zzbeiVar);
        this.zzaDd = zzbdmVar;
    }

    @Override // com.google.android.gms.internal.zzbek
    public final void zzpT() {
        this.zzaDd.onConnectionSuspended(1);
    }
}
