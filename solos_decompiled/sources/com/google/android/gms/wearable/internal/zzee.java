package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzee implements NodeApi.GetConnectedNodesResult {
    private final Status mStatus;
    private final List<Node> zzbTa;

    public zzee(Status status, List<Node> list) {
        this.mStatus = status;
        this.zzbTa = list;
    }

    @Override // com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult
    public final List<Node> getNodes() {
        return this.zzbTa;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
