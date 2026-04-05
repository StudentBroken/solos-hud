package com.google.android.gms.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjr implements Runnable {
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ AtomicReference zzbuf;

    zzcjr(zzcjp zzcjpVar, AtomicReference atomicReference) {
        this.zzbue = zzcjpVar;
        this.zzbuf = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcgp zzcgpVar;
        synchronized (this.zzbuf) {
            try {
                try {
                    zzcgpVar = this.zzbue.zzbtY;
                } catch (RemoteException e) {
                    this.zzbue.zzwE().zzyv().zzj("Failed to get app instance id", e);
                    this.zzbuf.notify();
                }
                if (zzcgpVar == null) {
                    this.zzbue.zzwE().zzyv().log("Failed to get app instance id");
                    return;
                }
                this.zzbuf.set(zzcgpVar.zzc(this.zzbue.zzwt().zzdW(null)));
                String str = (String) this.zzbuf.get();
                if (str != null) {
                    this.zzbue.zzws().zzef(str);
                    this.zzbue.zzwF().zzbru.zzeg(str);
                }
                this.zzbue.zzkO();
            } finally {
                this.zzbuf.notify();
            }
        }
    }
}
