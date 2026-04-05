package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzdv extends zzn<Status> {
    private zzbfi<MessageApi.MessageListener> zzaEW;
    private MessageApi.MessageListener zzbSX;
    private IntentFilter[] zzbSY;

    private zzdv(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener, zzbfi<MessageApi.MessageListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        super(googleApiClient);
        this.zzbSX = (MessageApi.MessageListener) com.google.android.gms.common.internal.zzbr.zzu(messageListener);
        this.zzaEW = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        this.zzbSY = (IntentFilter[]) com.google.android.gms.common.internal.zzbr.zzu(intentFilterArr);
    }

    /* synthetic */ zzdv(GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener, zzbfi zzbfiVar, IntentFilter[] intentFilterArr, zzdt zzdtVar) {
        this(googleApiClient, messageListener, zzbfiVar, intentFilterArr);
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSX, this.zzaEW, this.zzbSY);
        this.zzbSX = null;
        this.zzaEW = null;
        this.zzbSY = null;
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        this.zzbSX = null;
        this.zzaEW = null;
        this.zzbSY = null;
        return status;
    }
}
