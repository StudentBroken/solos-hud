package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
final class zze implements Runnable {
    private /* synthetic */ zzp zzl;
    private /* synthetic */ zzd zzm;

    zze(zzd zzdVar, zzp zzpVar) {
        this.zzm = zzdVar;
        this.zzl = zzpVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.zzm.zzh.put(this.zzl);
        } catch (InterruptedException e) {
        }
    }
}
