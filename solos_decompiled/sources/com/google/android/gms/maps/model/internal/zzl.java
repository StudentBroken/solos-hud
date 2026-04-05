package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class zzl extends zzed implements zzj {
    zzl(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IIndoorBuildingDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final int getActiveLevelIndex() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final int getDefaultLevelIndex() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final List<IBinder> getLevels() throws RemoteException {
        Parcel parcelZza = zza(3, zzY());
        ArrayList<IBinder> arrayListCreateBinderArrayList = parcelZza.createBinderArrayList();
        parcelZza.recycle();
        return arrayListCreateBinderArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final boolean isUnderground() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzj
    public final boolean zzb(zzj zzjVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzjVar);
        Parcel parcelZza = zza(5, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
