package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzaf implements ChannelApi.OpenChannelResult {
    private final Status mStatus;
    private final Channel zzbSh;

    zzaf(Status status, Channel channel) {
        this.mStatus = (Status) com.google.android.gms.common.internal.zzbr.zzu(status);
        this.zzbSh = channel;
    }

    @Override // com.google.android.gms.wearable.ChannelApi.OpenChannelResult
    public final Channel getChannel() {
        return this.zzbSh;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }
}
