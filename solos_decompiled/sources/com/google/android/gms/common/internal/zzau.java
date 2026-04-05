package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;

/* JADX INFO: loaded from: classes67.dex */
public final class zzau extends zzed implements zzas {
    zzau(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICertData");
    }

    @Override // com.google.android.gms.common.internal.zzas
    public final IObjectWrapper zzoW() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.common.internal.zzas
    public final int zzoX() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }
}
