package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzn extends zzee implements zzm {
    public static zzm zzae(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IIndoorLevelDelegate");
        return iInterfaceQueryLocalInterface instanceof zzm ? (zzm) iInterfaceQueryLocalInterface : new zzo(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzm zzoVar;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                String name = getName();
                parcel2.writeNoException();
                parcel2.writeString(name);
                break;
            case 2:
                String shortName = getShortName();
                parcel2.writeNoException();
                parcel2.writeString(shortName);
                break;
            case 3:
                activate();
                parcel2.writeNoException();
                break;
            case 4:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder == null) {
                    zzoVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IIndoorLevelDelegate");
                    zzoVar = iInterfaceQueryLocalInterface instanceof zzm ? (zzm) iInterfaceQueryLocalInterface : new zzo(strongBinder);
                }
                boolean zZza = zza(zzoVar);
                parcel2.writeNoException();
                zzef.zza(parcel2, zZza);
                break;
            case 5:
                int iHashCodeRemote = hashCodeRemote();
                parcel2.writeNoException();
                parcel2.writeInt(iHashCodeRemote);
                break;
            default:
                return false;
        }
        return true;
    }
}
