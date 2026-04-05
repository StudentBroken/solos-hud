package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdu extends zzbek {
    private /* synthetic */ com.google.android.gms.common.internal.zzj zzaDw;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbdu(zzbds zzbdsVar, zzbei zzbeiVar, com.google.android.gms.common.internal.zzj zzjVar) {
        super(zzbeiVar);
        this.zzaDw = zzjVar;
    }

    @Override // com.google.android.gms.internal.zzbek
    public final void zzpT() {
        this.zzaDw.zzf(new ConnectionResult(16, null));
    }
}
