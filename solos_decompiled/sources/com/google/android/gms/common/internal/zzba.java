package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
final class zzba implements zzay {
    private final IBinder zzrG;

    zzba(IBinder iBinder) {
        this.zzrG = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.zzrG;
    }

    @Override // com.google.android.gms.common.internal.zzay
    public final void zza(zzav zzavVar, zzy zzyVar) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        Parcel parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
            parcelObtain.writeStrongBinder(zzavVar != null ? zzavVar.asBinder() : null);
            if (zzyVar != null) {
                parcelObtain.writeInt(1);
                zzyVar.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            this.zzrG.transact(46, parcelObtain, parcelObtain2, 0);
            parcelObtain2.readException();
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }
}
