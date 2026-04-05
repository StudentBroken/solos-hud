package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzbq implements zzc<DataApi.DataListener> {
    private /* synthetic */ IntentFilter[] zzbRZ;

    zzbq(IntentFilter[] intentFilterArr) {
        this.zzbRZ = intentFilterArr;
    }

    @Override // com.google.android.gms.wearable.internal.zzc
    public final /* synthetic */ void zza(zzfw zzfwVar, zzbcl zzbclVar, DataApi.DataListener dataListener, zzbfi<DataApi.DataListener> zzbfiVar) throws RemoteException {
        zzfwVar.zza((zzbcl<Status>) zzbclVar, dataListener, zzbfiVar, this.zzbRZ);
    }
}
