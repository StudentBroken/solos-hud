package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbck;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbca<A extends zzbck<? extends Result, Api.zzb>> extends zzbby {
    private A zzaBv;

    public zzbca(int i, A a) {
        super(i);
        this.zzaBv = a;
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zza(@NonNull zzbdf zzbdfVar, boolean z) {
        zzbdfVar.zza(this.zzaBv, z);
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zza(zzbep<?> zzbepVar) throws DeadObjectException {
        this.zzaBv.zzb(zzbepVar.zzpH());
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zzp(@NonNull Status status) {
        this.zzaBv.zzr(status);
    }
}
