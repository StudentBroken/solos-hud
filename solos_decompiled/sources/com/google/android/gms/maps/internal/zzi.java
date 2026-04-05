package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzi extends zzee implements zzh {
    public zzi() {
        attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                IObjectWrapper iObjectWrapperZzh = zzh(com.google.android.gms.maps.model.internal.zzq.zzaf(parcel.readStrongBinder()));
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzh);
                break;
            case 2:
                IObjectWrapper iObjectWrapperZzi = zzi(com.google.android.gms.maps.model.internal.zzq.zzaf(parcel.readStrongBinder()));
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzi);
                break;
        }
        return true;
    }
}
