package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzas extends zzee implements zzar {
    public zzas() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnMarkerClickListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        boolean zZza = zza(com.google.android.gms.maps.model.internal.zzq.zzaf(parcel.readStrongBinder()));
        parcel2.writeNoException();
        zzef.zza(parcel2, zZza);
        return true;
    }
}
