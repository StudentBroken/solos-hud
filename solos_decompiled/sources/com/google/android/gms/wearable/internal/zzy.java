package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;

/* JADX INFO: loaded from: classes6.dex */
public final class zzy implements CapabilityApi.GetCapabilityResult {
    private final Status mStatus;
    private final CapabilityInfo zzbSe;

    public zzy(Status status, CapabilityInfo capabilityInfo) {
        this.mStatus = status;
        this.zzbSe = capabilityInfo;
    }

    @Override // com.google.android.gms.wearable.CapabilityApi.GetCapabilityResult
    public final CapabilityInfo getCapability() {
        return this.zzbSe;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
