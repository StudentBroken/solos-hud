package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.net.Uri;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzds implements MessageApi {
    private static PendingResult<Status> zza(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener, IntentFilter[] intentFilterArr) {
        return googleApiClient.zzd(new zzdv(googleApiClient, messageListener, googleApiClient.zzp(messageListener), intentFilterArr, null));
    }

    @Override // com.google.android.gms.wearable.MessageApi
    public final PendingResult<Status> addListener(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener) {
        return zza(googleApiClient, messageListener, new IntentFilter[]{zzez.zzgn(MessageApi.ACTION_MESSAGE_RECEIVED)});
    }

    @Override // com.google.android.gms.wearable.MessageApi
    public final PendingResult<Status> addListener(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener, Uri uri, int i) {
        com.google.android.gms.common.internal.zzbr.zzb(uri != null, "uri must not be null");
        com.google.android.gms.common.internal.zzbr.zzb(i == 0 || i == 1, "invalid filter type");
        return zza(googleApiClient, messageListener, new IntentFilter[]{zzez.zza(MessageApi.ACTION_MESSAGE_RECEIVED, uri, i)});
    }

    @Override // com.google.android.gms.wearable.MessageApi
    public final PendingResult<Status> removeListener(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener) {
        return googleApiClient.zzd(new zzdu(this, googleApiClient, messageListener));
    }

    @Override // com.google.android.gms.wearable.MessageApi
    public final PendingResult<MessageApi.SendMessageResult> sendMessage(GoogleApiClient googleApiClient, String str, String str2, byte[] bArr) {
        return googleApiClient.zzd(new zzdt(this, googleApiClient, str, str2, bArr));
    }
}
