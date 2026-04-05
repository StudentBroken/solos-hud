package com.google.android.gms.common.api;

import com.google.android.gms.internal.zzbcq;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public final class Batch extends zzbcq<BatchResult> {
    private final Object mLock;
    private int zzaAE;
    private boolean zzaAF;
    private boolean zzaAG;
    private final PendingResult<?>[] zzaAH;

    public static final class Builder {
        private List<PendingResult<?>> zzaAJ = new ArrayList();
        private GoogleApiClient zzapw;

        public Builder(GoogleApiClient googleApiClient) {
            this.zzapw = googleApiClient;
        }

        public final <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken<>(this.zzaAJ.size());
            this.zzaAJ.add(pendingResult);
            return batchResultToken;
        }

        public final Batch build() {
            return new Batch(this.zzaAJ, this.zzapw, null);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.mLock = new Object();
        this.zzaAE = list.size();
        this.zzaAH = new PendingResult[this.zzaAE];
        if (list.isEmpty()) {
            setResult(new BatchResult(Status.zzaBo, this.zzaAH));
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                return;
            }
            PendingResult<?> pendingResult = list.get(i2);
            this.zzaAH[i2] = pendingResult;
            pendingResult.zza(new zzb(this));
            i = i2 + 1;
        }
    }

    /* synthetic */ Batch(List list, GoogleApiClient googleApiClient, zzb zzbVar) {
        this(list, googleApiClient);
    }

    static /* synthetic */ boolean zza(Batch batch, boolean z) {
        batch.zzaAG = true;
        return true;
    }

    static /* synthetic */ int zzb(Batch batch) {
        int i = batch.zzaAE;
        batch.zzaAE = i - 1;
        return i;
    }

    static /* synthetic */ boolean zzb(Batch batch, boolean z) {
        batch.zzaAF = true;
        return true;
    }

    @Override // com.google.android.gms.internal.zzbcq, com.google.android.gms.common.api.PendingResult
    public final void cancel() {
        super.cancel();
        for (PendingResult<?> pendingResult : this.zzaAH) {
            pendingResult.cancel();
        }
    }

    @Override // com.google.android.gms.internal.zzbcq
    /* JADX INFO: renamed from: createFailedResult, reason: merged with bridge method [inline-methods] */
    public final BatchResult zzb(Status status) {
        return new BatchResult(status, this.zzaAH);
    }
}
