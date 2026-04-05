package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgr extends zzed implements zzcgp {
    zzcgr(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final List<zzcku> zza(zzcft zzcftVar, boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcftVar);
        zzef.zza(parcelZzY, z);
        Parcel parcelZza = zza(7, parcelZzY);
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(zzcku.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final List<zzcfw> zza(String str, String str2, zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        zzef.zza(parcelZzY, zzcftVar);
        Parcel parcelZza = zza(16, parcelZzY);
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(zzcfw.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final List<zzcku> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        parcelZzY.writeString(str3);
        zzef.zza(parcelZzY, z);
        Parcel parcelZza = zza(15, parcelZzY);
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(zzcku.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final List<zzcku> zza(String str, String str2, boolean z, zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        zzef.zza(parcelZzY, z);
        zzef.zza(parcelZzY, zzcftVar);
        Parcel parcelZza = zza(14, parcelZzY);
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(zzcku.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(long j, String str, String str2, String str3) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeLong(j);
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        parcelZzY.writeString(str3);
        zzb(10, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcftVar);
        zzb(4, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(zzcfw zzcfwVar, zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcfwVar);
        zzef.zza(parcelZzY, zzcftVar);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(zzcgl zzcglVar, zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcglVar);
        zzef.zza(parcelZzY, zzcftVar);
        zzb(1, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(zzcgl zzcglVar, String str, String str2) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcglVar);
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zza(zzcku zzckuVar, zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzckuVar);
        zzef.zza(parcelZzY, zzcftVar);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final byte[] zza(zzcgl zzcglVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcglVar);
        parcelZzY.writeString(str);
        Parcel parcelZza = zza(9, parcelZzY);
        byte[] bArrCreateByteArray = parcelZza.createByteArray();
        parcelZza.recycle();
        return bArrCreateByteArray;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zzb(zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcftVar);
        zzb(6, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final void zzb(zzcfw zzcfwVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcfwVar);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final String zzc(zzcft zzcftVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcftVar);
        Parcel parcelZza = zza(11, parcelZzY);
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.internal.zzcgp
    public final List<zzcfw> zzk(String str, String str2, String str3) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        parcelZzY.writeString(str3);
        Parcel parcelZza = zza(17, parcelZzY);
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(zzcfw.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }
}
