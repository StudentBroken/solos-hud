package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbt implements DataApi.DeleteDataItemsResult {
    private final Status mStatus;
    private final int zzbSE;

    public zzbt(Status status, int i) {
        this.mStatus = status;
        this.zzbSE = i;
    }

    @Override // com.google.android.gms.wearable.DataApi.DeleteDataItemsResult
    public final int getNumDeleted() {
        return this.zzbSE;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
