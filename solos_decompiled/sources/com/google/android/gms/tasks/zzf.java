package com.google.android.gms.tasks;

/* JADX INFO: loaded from: classes56.dex */
final class zzf implements Runnable {
    private /* synthetic */ Task zzbLV;
    private /* synthetic */ zze zzbLZ;

    zzf(zze zzeVar, Task task) {
        this.zzbLZ = zzeVar;
        this.zzbLV = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzbLZ.mLock) {
            if (this.zzbLZ.zzbLY != null) {
                this.zzbLZ.zzbLY.onComplete(this.zzbLV);
            }
        }
    }
}
