package com.google.android.gms.internal;

import android.os.Looper;

/* JADX INFO: loaded from: classes36.dex */
final class zzcge implements Runnable {
    private /* synthetic */ zzcgd zzbpF;

    zzcge(zzcgd zzcgdVar) {
        this.zzbpF = zzcgdVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.zzbpF.zzboi.zzwD().zzj(this);
            return;
        }
        boolean zZzbn = this.zzbpF.zzbn();
        zzcgd.zza(this.zzbpF, 0L);
        if (zZzbn && this.zzbpF.zzbpE) {
            this.zzbpF.run();
        }
    }
}
