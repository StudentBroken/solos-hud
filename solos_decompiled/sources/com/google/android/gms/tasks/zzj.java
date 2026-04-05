package com.google.android.gms.tasks;

/* JADX INFO: loaded from: classes56.dex */
final class zzj implements Runnable {
    private /* synthetic */ Task zzbLV;
    private /* synthetic */ zzi zzbMd;

    zzj(zzi zziVar, Task task) {
        this.zzbMd = zziVar;
        this.zzbLV = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzbMd.mLock) {
            if (this.zzbMd.zzbMc != null) {
                this.zzbMd.zzbMc.onSuccess(this.zzbLV.getResult());
            }
        }
    }
}
