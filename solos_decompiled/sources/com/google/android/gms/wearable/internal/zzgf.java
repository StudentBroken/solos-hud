package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbfl;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzgf implements zzbfl<ChannelApi.ChannelListener> {
    private /* synthetic */ zzai zzbRJ;

    zzgf(zzai zzaiVar) {
        this.zzbRJ = zzaiVar;
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final /* synthetic */ void zzq(ChannelApi.ChannelListener channelListener) {
        this.zzbRJ.zza(channelListener);
    }
}
