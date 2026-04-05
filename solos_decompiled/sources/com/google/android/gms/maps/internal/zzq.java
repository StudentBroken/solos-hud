package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzq extends zzee implements zzp {
    public zzq() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnCameraMoveCanceledListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        onCameraMoveCanceled();
        parcel2.writeNoException();
        return true;
    }
}
