package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
public class zzed implements IInterface {
    private final IBinder zzrG;
    private final String zzrH;

    protected zzed(IBinder iBinder, String str) {
        this.zzrG = iBinder;
        this.zzrH = str;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.zzrG;
    }

    protected final Parcel zzY() {
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.writeInterfaceToken(this.zzrH);
        return parcelObtain;
    }

    protected final Parcel zza(int i, Parcel parcel) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        try {
            try {
                this.zzrG.transact(i, parcel, parcelObtain, 0);
                parcelObtain.readException();
                return parcelObtain;
            } catch (RuntimeException e) {
                parcelObtain.recycle();
                throw e;
            }
        } finally {
            parcel.recycle();
        }
    }

    protected final void zzb(int i, Parcel parcel) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        try {
            this.zzrG.transact(i, parcel, parcelObtain, 0);
            parcelObtain.readException();
        } finally {
            parcel.recycle();
            parcelObtain.recycle();
        }
    }

    protected final void zzc(int i, Parcel parcel) throws RemoteException {
        try {
            this.zzrG.transact(i, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }
}
