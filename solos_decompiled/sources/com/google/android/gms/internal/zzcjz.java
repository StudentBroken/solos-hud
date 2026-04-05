package com.google.android.gms.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjz implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ boolean zzbtA;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ String zzbtm;
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ AtomicReference zzbuf;

    zzcjz(zzcjp zzcjpVar, AtomicReference atomicReference, String str, String str2, String str3, boolean z) {
        this.zzbue = zzcjpVar;
        this.zzbuf = atomicReference;
        this.zzbjl = str;
        this.zzbtl = str2;
        this.zzbtm = str3;
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
                    this.zzbue.zzwE().zzyv().zzd("Failed to get user properties", zzcgx.zzea(this.zzbjl), this.zzbtl, e);
                    this.zzbuf.set(Collections.emptyList());
                    this.zzbuf.notify();
                }
                if (zzcgpVar == null) {
                    this.zzbue.zzwE().zzyv().zzd("Failed to get user properties", zzcgx.zzea(this.zzbjl), this.zzbtl, this.zzbtm);
                    this.zzbuf.set(Collections.emptyList());
                } else {
                    if (TextUtils.isEmpty(this.zzbjl)) {
                        this.zzbuf.set(zzcgpVar.zza(this.zzbtl, this.zzbtm, this.zzbtA, this.zzbue.zzwt().zzdW(this.zzbue.zzwE().zzyC())));
                    } else {
                        this.zzbuf.set(zzcgpVar.zza(this.zzbjl, this.zzbtl, this.zzbtm, this.zzbtA));
                    }
                    this.zzbue.zzkO();
                }
            } finally {
                this.zzbuf.notify();
            }
        }
    }
}
