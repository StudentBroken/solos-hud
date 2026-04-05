package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzak extends com.google.android.gms.common.internal.safeparcel.zza implements Channel {
    public static final Parcelable.Creator<zzak> CREATOR = new zzau();
    private final String mPath;
    private final String zzakx;
    private final String zzbRg;

    public zzak(String str, String str2, String str3) {
        this.zzakx = (String) com.google.android.gms.common.internal.zzbr.zzu(str);
        this.zzbRg = (String) com.google.android.gms.common.internal.zzbr.zzu(str2);
        this.mPath = (String) com.google.android.gms.common.internal.zzbr.zzu(str3);
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> addListener(GoogleApiClient googleApiClient, ChannelApi.ChannelListener channelListener) {
        return zzb.zza(googleApiClient, new zzar(this.zzakx, new IntentFilter[]{zzez.zzgn(ChannelApi.ACTION_CHANNEL_EVENT)}), channelListener);
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> close(GoogleApiClient googleApiClient) {
        return googleApiClient.zzd(new zzal(this, googleApiClient));
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> close(GoogleApiClient googleApiClient, int i) {
        return googleApiClient.zzd(new zzam(this, googleApiClient, i));
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzak)) {
            return false;
        }
        zzak zzakVar = (zzak) obj;
        return this.zzakx.equals(zzakVar.zzakx) && com.google.android.gms.common.internal.zzbh.equal(zzakVar.zzbRg, this.zzbRg) && com.google.android.gms.common.internal.zzbh.equal(zzakVar.mPath, this.mPath);
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Channel.GetInputStreamResult> getInputStream(GoogleApiClient googleApiClient) {
        return googleApiClient.zzd(new zzan(this, googleApiClient));
    }

    @Override // com.google.android.gms.wearable.Channel
    public final String getNodeId() {
        return this.zzbRg;
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Channel.GetOutputStreamResult> getOutputStream(GoogleApiClient googleApiClient) {
        return googleApiClient.zzd(new zzao(this, googleApiClient));
    }

    @Override // com.google.android.gms.wearable.Channel
    public final String getPath() {
        return this.mPath;
    }

    public final int hashCode() {
        return this.zzakx.hashCode();
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> receiveFile(GoogleApiClient googleApiClient, Uri uri, boolean z) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(uri, "uri is null");
        return googleApiClient.zzd(new zzap(this, googleApiClient, uri, z));
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> removeListener(GoogleApiClient googleApiClient, ChannelApi.ChannelListener channelListener) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(channelListener, "listener is null");
        return googleApiClient.zzd(new zzag(googleApiClient, channelListener, this.zzakx));
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> sendFile(GoogleApiClient googleApiClient, Uri uri) {
        return sendFile(googleApiClient, uri, 0L, -1L);
    }

    @Override // com.google.android.gms.wearable.Channel
    public final PendingResult<Status> sendFile(GoogleApiClient googleApiClient, Uri uri, long j, long j2) {
        com.google.android.gms.common.internal.zzbr.zzb(googleApiClient, "client is null");
        com.google.android.gms.common.internal.zzbr.zzb(this.zzakx, "token is null");
        com.google.android.gms.common.internal.zzbr.zzb(uri, "uri is null");
        com.google.android.gms.common.internal.zzbr.zzb(j >= 0, "startOffset is negative: %s", Long.valueOf(j));
        com.google.android.gms.common.internal.zzbr.zzb(j2 >= 0 || j2 == -1, "invalid length: %s", Long.valueOf(j2));
        return googleApiClient.zzd(new zzaq(this, googleApiClient, uri, j, j2));
    }

    public final String toString() {
        String str = this.zzakx;
        String str2 = this.zzbRg;
        String str3 = this.mPath;
        return new StringBuilder(String.valueOf(str).length() + 43 + String.valueOf(str2).length() + String.valueOf(str3).length()).append("ChannelImpl{, token='").append(str).append("', nodeId='").append(str2).append("', path='").append(str3).append("'}").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzakx, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getNodeId(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getPath(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
