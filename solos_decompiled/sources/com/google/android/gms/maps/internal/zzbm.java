package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzbm extends zzee implements zzbl {
    public zzbm() {
        attachInterface(this, "com.google.android.gms.maps.internal.IOnStreetViewPanoramaLongClickListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        onStreetViewPanoramaLongClick((StreetViewPanoramaOrientation) zzef.zza(parcel, StreetViewPanoramaOrientation.CREATOR));
        parcel2.writeNoException();
        return true;
    }
}
