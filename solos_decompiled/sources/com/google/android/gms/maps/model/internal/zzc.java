package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public final class zzc extends zzed implements zza {
    zzc(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzbn(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        Parcel parcelZza = zza(1, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzd(Bitmap bitmap) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bitmap);
        Parcel parcelZza = zza(6, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzdD(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        Parcel parcelZza = zza(2, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzdE(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        Parcel parcelZza = zza(3, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzdF(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        Parcel parcelZza = zza(7, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zze(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        Parcel parcelZza = zza(5, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zza
    public final IObjectWrapper zzwk() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }
}
