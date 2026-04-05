package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdw implements MessageApi.SendMessageResult {
    private final Status mStatus;
    private final int zzaLX;

    public zzdw(Status status, int i) {
        this.mStatus = status;
        this.zzaLX = i;
    }

    @Override // com.google.android.gms.wearable.MessageApi.SendMessageResult
    public final int getRequestId() {
        return this.zzaLX;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
