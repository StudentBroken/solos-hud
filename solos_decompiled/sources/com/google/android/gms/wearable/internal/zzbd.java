package com.google.android.gms.wearable.internal;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbd extends zzdh {
    private final Object mLock = new Object();
    private zzah zzbSt;
    private zzbe zzbSx;

    public final void zza(zzbe zzbeVar) {
        zzah zzahVar;
        synchronized (this.mLock) {
            this.zzbSx = (zzbe) com.google.android.gms.common.internal.zzbr.zzu(zzbeVar);
            zzahVar = this.zzbSt;
        }
        if (zzahVar != null) {
            zzbeVar.zzb(zzahVar);
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdg
    public final void zzm(int i, int i2) {
        zzbe zzbeVar;
        zzah zzahVar;
        synchronized (this.mLock) {
            zzbeVar = this.zzbSx;
            zzahVar = new zzah(i, i2);
            this.zzbSt = zzahVar;
        }
        if (zzbeVar != null) {
            zzbeVar.zzb(zzahVar);
        }
    }
}
