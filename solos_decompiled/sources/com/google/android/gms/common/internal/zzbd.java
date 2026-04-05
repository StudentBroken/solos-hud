package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbd extends zzed implements zzbb {
    zzbd(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
    }

    @Override // com.google.android.gms.common.internal.zzbb
    public final boolean zza(com.google.android.gms.common.zzm zzmVar, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzmVar);
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(5, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.common.internal.zzbb
    public final boolean zze(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(3, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.common.internal.zzbb
    public final boolean zzf(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(4, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.common.internal.zzbb
    public final IObjectWrapper zzrE() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.common.internal.zzbb
    public final IObjectWrapper zzrF() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }
}
