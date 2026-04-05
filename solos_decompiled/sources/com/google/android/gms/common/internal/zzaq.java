package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzaq extends zzee implements zzap {
    public static zzap zzH(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ICancelToken");
        return iInterfaceQueryLocalInterface instanceof zzap ? (zzap) iInterfaceQueryLocalInterface : new zzar(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 2) {
            return false;
        }
        cancel();
        return true;
    }
}
