package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public final class zzax extends zzed implements zzav {
    zzax(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGmsCallbacks");
    }

    @Override // com.google.android.gms.common.internal.zzav
    public final void zza(int i, Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzef.zza(parcelZzY, bundle);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.common.internal.zzav
    public final void zza(int i, IBinder iBinder, Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        parcelZzY.writeStrongBinder(iBinder);
        zzef.zza(parcelZzY, bundle);
        zzb(1, parcelZzY);
    }
}
