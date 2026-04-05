package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public final class zzai extends zzed implements zzah {
    zzai(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IOnLocationChangeListener");
    }

    @Override // com.google.android.gms.maps.internal.zzah
    public final void zzd(Location location) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, location);
        zzb(2, parcelZzY);
    }
}
