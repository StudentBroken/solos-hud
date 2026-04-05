package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import java.util.Set;

/* JADX INFO: loaded from: classes6.dex */
public final class zzw implements CapabilityInfo {
    private final String mName;
    private final Set<Node> zzbSc;

    public zzw(CapabilityInfo capabilityInfo) {
        this(capabilityInfo.getName(), capabilityInfo.getNodes());
    }

    private zzw(String str, Set<Node> set) {
        this.mName = str;
        this.zzbSc = set;
    }

    @Override // com.google.android.gms.wearable.CapabilityInfo
    public final String getName() {
        return this.mName;
    }

    @Override // com.google.android.gms.wearable.CapabilityInfo
    public final Set<Node> getNodes() {
        return this.zzbSc;
    }
}
