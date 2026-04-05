package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
abstract class zzbek {
    private final zzbei zzaEb;

    protected zzbek(zzbei zzbeiVar) {
        this.zzaEb = zzbeiVar;
    }

    public final void zzc(zzbej zzbejVar) {
        zzbejVar.zzaCx.lock();
        try {
            if (zzbejVar.zzaDX != this.zzaEb) {
                return;
            }
            zzpT();
        } finally {
            zzbejVar.zzaCx.unlock();
        }
    }

    protected abstract void zzpT();
}
