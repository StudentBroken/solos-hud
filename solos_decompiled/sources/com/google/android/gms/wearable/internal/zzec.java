package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.NodeApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzec implements zzc<NodeApi.NodeListener> {
    private /* synthetic */ IntentFilter[] zzbRZ;

    zzec(IntentFilter[] intentFilterArr) {
        this.zzbRZ = intentFilterArr;
    }

    @Override // com.google.android.gms.wearable.internal.zzc
    public final /* synthetic */ void zza(zzfw zzfwVar, zzbcl zzbclVar, NodeApi.NodeListener nodeListener, zzbfi<NodeApi.NodeListener> zzbfiVar) throws RemoteException {
        zzfwVar.zza((zzbcl<Status>) zzbclVar, nodeListener, zzbfiVar, this.zzbRZ);
    }
}
