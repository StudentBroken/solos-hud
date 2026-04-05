package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes10.dex */
public interface ILocationSourceDelegate extends IInterface {

    public static abstract class zza extends zzee implements ILocationSourceDelegate {
        public zza() {
            attachInterface(this, "com.google.android.gms.maps.internal.ILocationSourceDelegate");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            zzah zzaiVar;
            if (zza(i, parcel, parcel2, i2)) {
                return true;
            }
            switch (i) {
                case 1:
                    IBinder strongBinder = parcel.readStrongBinder();
                    if (strongBinder == null) {
                        zzaiVar = null;
                    } else {
                        IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnLocationChangeListener");
                        zzaiVar = iInterfaceQueryLocalInterface instanceof zzah ? (zzah) iInterfaceQueryLocalInterface : new zzai(strongBinder);
                    }
                    activate(zzaiVar);
                    break;
                case 2:
                    deactivate();
                    break;
                default:
                    return false;
            }
            parcel2.writeNoException();
            return true;
        }
    }

    void activate(zzah zzahVar) throws RemoteException;

    void deactivate() throws RemoteException;
}
