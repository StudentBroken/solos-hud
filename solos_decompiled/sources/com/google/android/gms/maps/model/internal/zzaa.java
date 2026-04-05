package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.Tile;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzaa extends zzee implements zzz {
    public zzaa() {
        attachInterface(this, "com.google.android.gms.maps.model.internal.ITileProviderDelegate");
    }

    public static zzz zzaj(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ITileProviderDelegate");
        return iInterfaceQueryLocalInterface instanceof zzz ? (zzz) iInterfaceQueryLocalInterface : new zzab(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        Tile tile = getTile(parcel.readInt(), parcel.readInt(), parcel.readInt());
        parcel2.writeNoException();
        zzef.zzb(parcel2, tile);
        return true;
    }
}
