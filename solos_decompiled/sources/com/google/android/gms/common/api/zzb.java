package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult;

/* JADX INFO: loaded from: classes3.dex */
final class zzb implements PendingResult.zza {
    private /* synthetic */ Batch zzaAI;

    zzb(Batch batch) {
        this.zzaAI = batch;
    }

    @Override // com.google.android.gms.common.api.PendingResult.zza
    public final void zzo(Status status) {
        synchronized (this.zzaAI.mLock) {
            if (this.zzaAI.isCanceled()) {
                return;
            }
            if (status.isCanceled()) {
                Batch.zza(this.zzaAI, true);
            } else if (!status.isSuccess()) {
                Batch.zzb(this.zzaAI, true);
            }
            Batch.zzb(this.zzaAI);
            if (this.zzaAI.zzaAE == 0) {
                if (this.zzaAI.zzaAG) {
                    super/*com.google.android.gms.internal.zzbcq*/.cancel();
                } else {
                    this.zzaAI.setResult(new BatchResult(this.zzaAI.zzaAF ? new Status(13) : Status.zzaBo, this.zzaAI.zzaAH));
                }
            }
        }
    }
}
