package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzbo extends zzee implements zzbn {
    public zzbo() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnStreetViewPanoramaReadyCallback");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        IStreetViewPanoramaDelegate zzbsVar;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        IBinder strongBinder = parcel.readStrongBinder();
        if (strongBinder == null) {
            zzbsVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
            zzbsVar = iInterfaceQueryLocalInterface instanceof IStreetViewPanoramaDelegate ? (IStreetViewPanoramaDelegate) iInterfaceQueryLocalInterface : new zzbs(strongBinder);
        }
        zza(zzbsVar);
        parcel2.writeNoException();
        return true;
    }
}
