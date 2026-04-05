package com.google.android.gms.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzckb implements Runnable {
    private /* synthetic */ boolean zzbtA;
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ AtomicReference zzbuf;

    zzckb(zzcjp zzcjpVar, AtomicReference atomicReference, boolean z) {
        this.zzbue = zzcjpVar;
        this.zzbuf = atomicReference;
        this.zzbtA = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcgp zzcgpVar;
        synchronized (this.zzbuf) {
            try {
                try {
                    zzcgpVar = this.zzbue.zzbtY;
                } catch (RemoteException e) {
                    this.zzbue.zzwE().zzyv().zzj("Failed to get user properties", e);
                    this.zzbuf.notify();
                }
                if (zzcgpVar == null) {
                    this.zzbue.zzwE().zzyv().log("Failed to get user properties");
                } else {
                    this.zzbuf.set(zzcgpVar.zza(this.zzbue.zzwt().zzdW(null), this.zzbtA));
                    this.zzbue.zzkO();
                }
            } finally {
                this.zzbuf.notify();
            }
        }
    }
}
