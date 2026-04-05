package com.google.android.gms.common;

import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes67.dex */
abstract class zzi extends zzg {
    private static final WeakReference<byte[]> zzaAl = new WeakReference<>(null);
    private WeakReference<byte[]> zzaAk;

    zzi(byte[] bArr) {
        super(bArr);
        this.zzaAk = zzaAl;
    }

    @Override // com.google.android.gms.common.zzg
    final byte[] getBytes() {
        byte[] bArrZzoY;
        synchronized (this) {
            bArrZzoY = this.zzaAk.get();
            if (bArrZzoY == null) {
                bArrZzoY = zzoY();
                this.zzaAk = new WeakReference<>(bArrZzoY);
            }
        }
        return bArrZzoY;
    }

    protected abstract byte[] zzoY();
}
