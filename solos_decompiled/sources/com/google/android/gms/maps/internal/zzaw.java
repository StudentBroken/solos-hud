package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzaw extends zzee implements zzav {
    public zzaw() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        boolean zOnMyLocationButtonClick = onMyLocationButtonClick();
        parcel2.writeNoException();
        zzef.zza(parcel2, zOnMyLocationButtonClick);
        return true;
    }
}
