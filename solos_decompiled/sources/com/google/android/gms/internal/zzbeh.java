package com.google.android.gms.internal;

import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes3.dex */
final class zzbeh extends zzbex {
    private WeakReference<zzbeb> zzaDT;

    zzbeh(zzbeb zzbebVar) {
        this.zzaDT = new WeakReference<>(zzbebVar);
    }

    @Override // com.google.android.gms.internal.zzbex
    public final void zzpy() {
        zzbeb zzbebVar = this.zzaDT.get();
        if (zzbebVar == null) {
            return;
        }
        zzbebVar.resume();
    }
}
