package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvf extends zzed implements zzcve {
    zzcvf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    @Override // com.google.android.gms.internal.zzcve
    public final void zza(com.google.android.gms.common.internal.zzam zzamVar, int i, boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzamVar);
        parcelZzY.writeInt(i);
        zzef.zza(parcelZzY, z);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcve
    public final void zza(zzcvh zzcvhVar, zzcvc zzcvcVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzcvhVar);
        zzef.zza(parcelZzY, zzcvcVar);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.internal.zzcve
    public final void zzbu(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(7, parcelZzY);
    }
}
