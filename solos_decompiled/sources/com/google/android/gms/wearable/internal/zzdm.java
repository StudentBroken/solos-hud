package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdm extends com.google.android.gms.internal.zzed implements zzdk {
    zzdm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wearable.internal.IWearableListener");
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void onConnectedNodes(List<zzeg> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzc(5, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zzS(DataHolder dataHolder) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, dataHolder);
        zzc(1, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzaa zzaaVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzaaVar);
        zzc(8, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzai zzaiVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzaiVar);
        zzc(7, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzdx zzdxVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdxVar);
        zzc(2, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzeg zzegVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzegVar);
        zzc(3, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzi zziVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zziVar);
        zzc(9, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzl zzlVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzlVar);
        zzc(6, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zzb(zzeg zzegVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzegVar);
        zzc(4, parcelZzY);
    }
}
