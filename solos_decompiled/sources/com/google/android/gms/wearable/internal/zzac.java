package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzac implements ChannelApi {
    @Override // com.google.android.gms.wearable.ChannelApi
    public final PendingResult<Status> addListener(GoogleApiClient googleApiClient, ChannelApi.ChannelListener channelListener) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(channelListener, "listener is null");
        return zzb.zza(googleApiClient, new zzae(new IntentFilter[]{zzez.zzgn(ChannelApi.ACTION_CHANNEL_EVENT)}), channelListener);
    }

    @Override // com.google.android.gms.wearable.ChannelApi
    public final PendingResult<ChannelApi.OpenChannelResult> openChannel(GoogleApiClient googleApiClient, String str, String str2) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(str, "nodeId is null");
        com.google.android.gms.common.internal.zzbr.zzb(str2, "path is null");
        return googleApiClient.zzd(new zzad(this, googleApiClient, str, str2));
    }

    @Override // com.google.android.gms.wearable.ChannelApi
    public final PendingResult<Status> removeListener(GoogleApiClient googleApiClient, ChannelApi.ChannelListener channelListener) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(channelListener, "listener is null");
        return googleApiClient.zzd(new zzag(googleApiClient, channelListener, null));
    }
}
