package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes6.dex */
public abstract class zzdh extends com.google.android.gms.internal.zzee implements zzdg {
    public zzdh() {
        attachInterface(this, "com.google.android.gms.wearable.internal.IChannelStreamCallbacks");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 2) {
            return false;
        }
        zzm(parcel.readInt(), parcel.readInt());
        parcel2.writeNoException();
        return true;
    }
}
