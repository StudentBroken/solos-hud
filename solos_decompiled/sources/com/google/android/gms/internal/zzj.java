package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
final class zzj implements Runnable {
    private final zzp zzt;
    private final zzt zzu;
    private final Runnable zzv;

    public zzj(zzh zzhVar, zzp zzpVar, zzt zztVar, Runnable runnable) {
        this.zzt = zzpVar;
        this.zzu = zztVar;
        this.zzv = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzu.zzaf == null) {
            this.zzt.zza(this.zzu.result);
        } else {
            this.zzt.zzb(this.zzu.zzaf);
        }
        if (this.zzu.zzag) {
            this.zzt.zzb("intermediate-response");
        } else {
            this.zzt.zzc("done");
        }
        if (this.zzv != null) {
            this.zzv.run();
        }
    }
}
