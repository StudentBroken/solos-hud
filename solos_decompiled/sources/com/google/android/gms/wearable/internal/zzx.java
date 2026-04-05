package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
public final class zzx implements CapabilityApi.GetAllCapabilitiesResult {
    private final Status mStatus;
    private final Map<String, CapabilityInfo> zzbSd;

    public zzx(Status status, Map<String, CapabilityInfo> map) {
        this.mStatus = status;
        this.zzbSd = map;
    }

    @Override // com.google.android.gms.wearable.CapabilityApi.GetAllCapabilitiesResult
    public final Map<String, CapabilityInfo> getAllCapabilities() {
        return this.zzbSd;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
