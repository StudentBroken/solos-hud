package com.google.android.gms.internal;

import android.os.Handler;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes67.dex */
public final class zzh implements zzw {
    private final Executor zzr;

    public zzh(Handler handler) {
        this.zzr = new zzi(this, handler);
    }

    @Override // com.google.android.gms.internal.zzw
    public final void zza(zzp<?> zzpVar, zzaa zzaaVar) {
        zzpVar.zzb("post-error");
        this.zzr.execute(new zzj(this, zzpVar, zzt.zzc(zzaaVar), null));
    }

    @Override // com.google.android.gms.internal.zzw
    public final void zza(zzp<?> zzpVar, zzt<?> zztVar) {
        zza(zzpVar, zztVar, null);
    }

    @Override // com.google.android.gms.internal.zzw
    public final void zza(zzp<?> zzpVar, zzt<?> zztVar, Runnable runnable) {
        zzpVar.zzk();
        zzpVar.zzb("post-response");
        this.zzr.execute(new zzj(this, zzpVar, zztVar, runnable));
    }
}
