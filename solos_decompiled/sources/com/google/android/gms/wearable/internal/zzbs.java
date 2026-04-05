package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbs implements DataApi.DataItemResult {
    private final Status mStatus;
    private final DataItem zzbSD;

    public zzbs(Status status, DataItem dataItem) {
        this.mStatus = status;
        this.zzbSD = dataItem;
    }

    @Override // com.google.android.gms.wearable.DataApi.DataItemResult
    public final DataItem getDataItem() {
        return this.zzbSD;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
