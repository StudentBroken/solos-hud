package com.google.android.gms.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
abstract class zzcgd {
    private static volatile Handler zzaha;
    private volatile long zzahb;
    private final zzchx zzboi;
    private boolean zzbpE;
    private final Runnable zzv;

    zzcgd(zzchx zzchxVar) {
        zzbr.zzu(zzchxVar);
        this.zzboi = zzchxVar;
        this.zzbpE = true;
        this.zzv = new zzcge(this);
    }

    private final Handler getHandler() {
        Handler handler;
        if (zzaha != null) {
            return zzaha;
        }
        synchronized (zzcgd.class) {
            if (zzaha == null) {
                zzaha = new Handler(this.zzboi.getContext().getMainLooper());
            }
            handler = zzaha;
        }
        return handler;
    }

    static /* synthetic */ long zza(zzcgd zzcgdVar, long j) {
        zzcgdVar.zzahb = 0L;
        return 0L;
    }

    public final void cancel() {
        this.zzahb = 0L;
        getHandler().removeCallbacks(this.zzv);
    }

    public abstract void run();

    public final boolean zzbn() {
        return this.zzahb != 0;
    }

    public final void zzs(long j) {
        cancel();
        if (j >= 0) {
            this.zzahb = this.zzboi.zzkp().currentTimeMillis();
            if (getHandler().postDelayed(this.zzv, j)) {
                return;
            }
            this.zzboi.zzwE().zzyv().zzj("Failed to schedule delayed post. time", Long.valueOf(j));
        }
    }
}
