package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;

/* JADX INFO: loaded from: classes3.dex */
abstract class zzbdz implements Runnable {
    private /* synthetic */ zzbdp zzaDr;

    private zzbdz(zzbdp zzbdpVar) {
        this.zzaDr = zzbdpVar;
    }

    /* synthetic */ zzbdz(zzbdp zzbdpVar, zzbdq zzbdqVar) {
        this(zzbdpVar);
    }

    @Override // java.lang.Runnable
    @WorkerThread
    public void run() {
        this.zzaDr.zzaCx.lock();
        try {
            if (Thread.interrupted()) {
                return;
            }
            zzpT();
            return;
        } catch (RuntimeException e) {
            this.zzaDr.zzaDb.zza(e);
            return;
        } finally {
            this.zzaDr.zzaCx.unlock();
        }
        this.zzaDr.zzaCx.unlock();
    }

    @WorkerThread
    protected abstract void zzpT();
}
