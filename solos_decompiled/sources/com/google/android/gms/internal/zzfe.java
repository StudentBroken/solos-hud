package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import io.fabric.sdk.android.services.common.AdvertisingInfoServiceStrategy;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzfe extends zzee implements zzfd {
    public static zzfd zzc(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(AdvertisingInfoServiceStrategy.AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
        return iInterfaceQueryLocalInterface instanceof zzfd ? (zzfd) iInterfaceQueryLocalInterface : new zzff(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                String id = getId();
                parcel2.writeNoException();
                parcel2.writeString(id);
                break;
            case 2:
                boolean zZzb = zzb(zzef.zza(parcel));
                parcel2.writeNoException();
                zzef.zza(parcel2, zZzb);
                break;
            case 3:
                String strZzq = zzq(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeString(strZzq);
                break;
            case 4:
                zzc(parcel.readString(), zzef.zza(parcel));
                parcel2.writeNoException();
                break;
        }
        return true;
    }
}
