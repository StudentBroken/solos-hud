package com.google.android.gms.internal;

import android.support.annotation.BinderThread;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdw extends zzcvb {
    private final WeakReference<zzbdp> zzaDs;

    zzbdw(zzbdp zzbdpVar) {
        this.zzaDs = new WeakReference<>(zzbdpVar);
    }

    @Override // com.google.android.gms.internal.zzcvb, com.google.android.gms.internal.zzcvc
    @BinderThread
    public final void zzb(zzcvj zzcvjVar) {
        zzbdp zzbdpVar = this.zzaDs.get();
        if (zzbdpVar == null) {
            return;
        }
        zzbdpVar.zzaDb.zza(new zzbdx(this, zzbdpVar, zzbdpVar, zzcvjVar));
    }
}
