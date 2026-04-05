package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbcb extends zzbbz {
    private zzbfq<Api.zzb, ?> zzaBw;
    private zzbgk<Api.zzb, ?> zzaBx;

    public zzbcb(zzbfr zzbfrVar, TaskCompletionSource<Void> taskCompletionSource) {
        super(3, taskCompletionSource);
        this.zzaBw = zzbfrVar.zzaBw;
        this.zzaBx = zzbfrVar.zzaBx;
    }

    @Override // com.google.android.gms.internal.zzbbz, com.google.android.gms.internal.zzbby
    public final /* bridge */ /* synthetic */ void zza(@NonNull zzbdf zzbdfVar, boolean z) {
    }

    @Override // com.google.android.gms.internal.zzbbz
    public final void zzb(zzbep<?> zzbepVar) throws RemoteException {
        this.zzaBw.zzb(zzbepVar.zzpH(), this.zzalG);
        if (this.zzaBw.zzqE() != null) {
            zzbepVar.zzqq().put(this.zzaBw.zzqE(), new zzbfr(this.zzaBw, this.zzaBx));
        }
    }

    @Override // com.google.android.gms.internal.zzbbz, com.google.android.gms.internal.zzbby
    public final /* bridge */ /* synthetic */ void zzp(@NonNull Status status) {
        super.zzp(status);
    }
}
