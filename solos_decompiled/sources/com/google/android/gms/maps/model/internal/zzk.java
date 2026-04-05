package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzk extends zzee implements zzj {
    public static zzj zzad(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IIndoorBuildingDelegate");
        return iInterfaceQueryLocalInterface instanceof zzj ? (zzj) iInterfaceQueryLocalInterface : new zzl(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzj zzlVar;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                int activeLevelIndex = getActiveLevelIndex();
                parcel2.writeNoException();
                parcel2.writeInt(activeLevelIndex);
                break;
            case 2:
                int defaultLevelIndex = getDefaultLevelIndex();
                parcel2.writeNoException();
                parcel2.writeInt(defaultLevelIndex);
                break;
            case 3:
                List<IBinder> levels = getLevels();
                parcel2.writeNoException();
                parcel2.writeBinderList(levels);
                break;
            case 4:
                boolean zIsUnderground = isUnderground();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsUnderground);
                break;
            case 5:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder == null) {
                    zzlVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IIndoorBuildingDelegate");
                    zzlVar = iInterfaceQueryLocalInterface instanceof zzj ? (zzj) iInterfaceQueryLocalInterface : new zzl(strongBinder);
                }
                boolean zZzb = zzb(zzlVar);
                parcel2.writeNoException();
                zzef.zza(parcel2, zZzb);
                break;
            case 6:
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
