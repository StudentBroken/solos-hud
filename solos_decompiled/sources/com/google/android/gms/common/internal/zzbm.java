package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes3.dex */
final class zzbm implements PendingResult.zza {
    private /* synthetic */ PendingResult zzaIl;
    private /* synthetic */ TaskCompletionSource zzaIm;
    private /* synthetic */ zzbp zzaIn;
    private /* synthetic */ zzbq zzaIo;

    zzbm(PendingResult pendingResult, TaskCompletionSource taskCompletionSource, zzbp zzbpVar, zzbq zzbqVar) {
        this.zzaIl = pendingResult;
        this.zzaIm = taskCompletionSource;
        this.zzaIn = zzbpVar;
        this.zzaIo = zzbqVar;
    }

    @Override // com.google.android.gms.common.api.PendingResult.zza
    public final void zzo(Status status) {
        if (!status.isSuccess()) {
            this.zzaIm.setException(this.zzaIo.zzy(status));
        } else {
            this.zzaIm.setResult(this.zzaIn.zzd(this.zzaIl.await(0L, TimeUnit.MILLISECONDS)));
        }
    }
}
