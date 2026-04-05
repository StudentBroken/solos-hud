package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
abstract class zzciv extends zzciu {
    private boolean zzafM;

    zzciv(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzboi.zzb(this);
    }

    public final void initialize() {
        if (this.zzafM) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzjC();
        this.zzboi.zzzb();
        this.zzafM = true;
    }

    final boolean isInitialized() {
        return this.zzafM;
    }

    protected abstract void zzjC();

    protected final void zzkC() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
