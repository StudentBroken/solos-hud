package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzef implements NodeApi.GetLocalNodeResult {
    private final Status mStatus;
    private final Node zzbTb;

    public zzef(Status status, Node node) {
        this.mStatus = status;
        this.zzbTb = node;
    }

    @Override // com.google.android.gms.wearable.NodeApi.GetLocalNodeResult
    public final Node getNode() {
        return this.zzbTb;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
