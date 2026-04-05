package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzbr;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes3.dex */
public final class BatchResult implements Result {
    private final Status mStatus;
    private final PendingResult<?>[] zzaAH;

    BatchResult(Status status, PendingResult<?>[] pendingResultArr) {
        this.mStatus = status;
        this.zzaAH = pendingResultArr;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }

    public final <R extends Result> R take(BatchResultToken<R> batchResultToken) {
        zzbr.zzb(batchResultToken.mId < this.zzaAH.length, "The result token does not belong to this batch");
        return (R) this.zzaAH[batchResultToken.mId].await(0L, TimeUnit.MILLISECONDS);
    }
}
