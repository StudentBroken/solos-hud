package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdt extends zzbek {
    private /* synthetic */ ConnectionResult zzaDu;
    private /* synthetic */ zzbds zzaDv;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbdt(zzbds zzbdsVar, zzbei zzbeiVar, ConnectionResult connectionResult) {
        super(zzbeiVar);
        this.zzaDv = zzbdsVar;
        this.zzaDu = connectionResult;
    }

    @Override // com.google.android.gms.internal.zzbek
    public final void zzpT() {
        this.zzaDv.zzaDr.zze(this.zzaDu);
    }
}
