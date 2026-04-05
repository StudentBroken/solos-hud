package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbfa extends zzee implements zzbez {
    public zzbfa() {
        attachInterface(this, "com.google.android.gms.common.api.internal.IStatusCallback");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i != 1) {
            return false;
        }
        zzu((Status) zzef.zza(parcel, Status.CREATOR));
        return true;
    }
}
