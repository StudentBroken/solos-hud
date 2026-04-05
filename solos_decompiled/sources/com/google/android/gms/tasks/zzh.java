package com.google.android.gms.tasks;

/* JADX INFO: loaded from: classes56.dex */
final class zzh implements Runnable {
    private /* synthetic */ Task zzbLV;
    private /* synthetic */ zzg zzbMb;

    zzh(zzg zzgVar, Task task) {
        this.zzbMb = zzgVar;
        this.zzbLV = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzbMb.mLock) {
            if (this.zzbMb.zzbMa != null) {
                this.zzbMb.zzbMa.onFailure(this.zzbLV.getException());
            }
        }
    }
}
