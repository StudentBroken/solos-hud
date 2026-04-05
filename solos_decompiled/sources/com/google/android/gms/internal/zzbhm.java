package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhm extends zzed implements zzbhl {
    zzbhm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.service.ICommonService");
    }

    @Override // com.google.android.gms.internal.zzbhl
    public final void zza(zzbhj zzbhjVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbhjVar);
        zzc(1, parcelZzY);
    }
}
