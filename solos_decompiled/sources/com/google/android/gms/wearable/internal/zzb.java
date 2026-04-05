package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbfi;

/* JADX INFO: loaded from: classes6.dex */
final class zzb<T> extends zzn<Status> {
    private T mListener;
    private zzbfi<T> zzaEW;
    private zzc<T> zzbRK;

    private zzb(GoogleApiClient googleApiClient, T t, zzbfi<T> zzbfiVar, zzc<T> zzcVar) {
        super(googleApiClient);
        this.mListener = (T) com.google.android.gms.common.internal.zzbr.zzu(t);
        this.zzaEW = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        this.zzbRK = (zzc) com.google.android.gms.common.internal.zzbr.zzu(zzcVar);
    }

    static <T> PendingResult<Status> zza(GoogleApiClient googleApiClient, zzc<T> zzcVar, T t) {
        return googleApiClient.zzd(new zzb(googleApiClient, t, googleApiClient.zzp(t), zzcVar));
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        this.zzbRK.zza((zzfw) zzbVar, this, this.mListener, this.zzaEW);
        this.mListener = null;
        this.zzaEW = null;
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        this.mListener = null;
        this.zzaEW = null;
        return status;
    }
}
