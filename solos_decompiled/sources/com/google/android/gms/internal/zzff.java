package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import io.fabric.sdk.android.services.common.AdvertisingInfoServiceStrategy;

/* JADX INFO: loaded from: classes67.dex */
public final class zzff extends zzed implements zzfd {
    zzff(IBinder iBinder) {
        super(iBinder, AdvertisingInfoServiceStrategy.AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
    }

    @Override // com.google.android.gms.internal.zzfd
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.internal.zzfd
    public final boolean zzb(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        Parcel parcelZza = zza(2, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.internal.zzfd
    public final void zzc(String str, boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzef.zza(parcelZzY, z);
        zzb(4, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzfd
    public final String zzq(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        Parcel parcelZza = zza(3, parcelZzY);
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }
}
