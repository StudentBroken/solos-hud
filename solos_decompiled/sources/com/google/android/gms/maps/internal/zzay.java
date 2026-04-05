package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzay extends zzee implements zzax {
    public zzay() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnMyLocationChangeListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        zzF(IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
        parcel2.writeNoException();
        return true;
    }
}
