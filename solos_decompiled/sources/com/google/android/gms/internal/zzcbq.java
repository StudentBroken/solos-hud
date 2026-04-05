package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbq extends zzed implements zzcbo {
    zzcbq(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.flags.IFlagProvider");
    }

    @Override // com.google.android.gms.internal.zzcbo
    public final boolean getBooleanFlagValue(String str, boolean z, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzef.zza(parcelZzY, z);
        parcelZzY.writeInt(i);
        Parcel parcelZza = zza(2, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.internal.zzcbo
    public final int getIntFlagValue(String str, int i, int i2) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeInt(i);
        parcelZzY.writeInt(i2);
        Parcel parcelZza = zza(3, parcelZzY);
        int i3 = parcelZza.readInt();
        parcelZza.recycle();
        return i3;
    }

    @Override // com.google.android.gms.internal.zzcbo
    public final long getLongFlagValue(String str, long j, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeLong(j);
        parcelZzY.writeInt(i);
        Parcel parcelZza = zza(4, parcelZzY);
        long j2 = parcelZza.readLong();
        parcelZza.recycle();
        return j2;
    }

    @Override // com.google.android.gms.internal.zzcbo
    public final String getStringFlagValue(String str, String str2, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        parcelZzY.writeInt(i);
        Parcel parcelZza = zza(5, parcelZzY);
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.internal.zzcbo
    public final void init(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(1, parcelZzY);
    }
}
