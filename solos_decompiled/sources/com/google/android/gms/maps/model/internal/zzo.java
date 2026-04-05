package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public final class zzo extends zzed implements zzm {
    zzo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IIndoorLevelDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzm
    public final void activate() throws RemoteException {
        zzb(3, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzm
    public final String getName() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzm
    public final String getShortName() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzm
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(5, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzm
    public final boolean zza(zzm zzmVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzmVar);
        Parcel parcelZza = zza(4, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
