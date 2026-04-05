package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzao extends zzee implements zzan {
    public zzao() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnMapLongClickListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        onMapLongClick((LatLng) zzef.zza(parcel, LatLng.CREATOR));
        parcel2.writeNoException();
        return true;
    }
}
