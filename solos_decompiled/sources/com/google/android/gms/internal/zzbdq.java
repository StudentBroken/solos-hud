package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdq implements Runnable {
    private /* synthetic */ zzbdp zzaDr;

    zzbdq(zzbdp zzbdpVar) {
        this.zzaDr = zzbdpVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.google.android.gms.common.zze.zzat(this.zzaDr.mContext);
    }
}
