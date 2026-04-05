package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzar implements zzc<ChannelApi.ChannelListener> {
    private /* synthetic */ String zzaks;
    private /* synthetic */ IntentFilter[] zzbRZ;

    zzar(String str, IntentFilter[] intentFilterArr) {
        this.zzaks = str;
        this.zzbRZ = intentFilterArr;
    }

    @Override // com.google.android.gms.wearable.internal.zzc
    public final /* synthetic */ void zza(zzfw zzfwVar, zzbcl zzbclVar, ChannelApi.ChannelListener channelListener, zzbfi<ChannelApi.ChannelListener> zzbfiVar) throws RemoteException {
        zzfwVar.zza((zzbcl<Status>) zzbclVar, channelListener, zzbfiVar, this.zzaks, this.zzbRZ);
    }
}
