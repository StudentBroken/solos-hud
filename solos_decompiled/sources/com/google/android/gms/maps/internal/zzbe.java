package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzbe extends zzee implements zzbd {
    public zzbe() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnPolylineClickListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        zza(IPolylineDelegate.zza.zzah(parcel.readStrongBinder()));
        parcel2.writeNoException();
        return true;
    }
}
