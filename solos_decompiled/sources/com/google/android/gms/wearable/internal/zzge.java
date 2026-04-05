package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbfl;
import com.google.android.gms.wearable.NodeApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzge implements zzbfl<NodeApi.NodeListener> {
    private /* synthetic */ zzeg zzbRE;

    zzge(zzeg zzegVar) {
        this.zzbRE = zzegVar;
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final /* synthetic */ void zzq(NodeApi.NodeListener nodeListener) {
        nodeListener.onPeerDisconnected(this.zzbRE);
    }
}
