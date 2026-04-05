package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzb extends zzee implements zza {
    public static zza zzaa(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
        return iInterfaceQueryLocalInterface instanceof zza ? (zza) iInterfaceQueryLocalInterface : new zzc(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                IObjectWrapper iObjectWrapperZzbn = zzbn(parcel.readInt());
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzbn);
                break;
            case 2:
                IObjectWrapper iObjectWrapperZzdD = zzdD(parcel.readString());
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzdD);
                break;
            case 3:
                IObjectWrapper iObjectWrapperZzdE = zzdE(parcel.readString());
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzdE);
                break;
            case 4:
                IObjectWrapper iObjectWrapperZzwk = zzwk();
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzwk);
                break;
            case 5:
                IObjectWrapper iObjectWrapperZze = zze(parcel.readFloat());
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZze);
                break;
            case 6:
                IObjectWrapper iObjectWrapperZzd = zzd((Bitmap) zzef.zza(parcel, Bitmap.CREATOR));
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzd);
                break;
            case 7:
                IObjectWrapper iObjectWrapperZzdF = zzdF(parcel.readString());
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzdF);
                break;
            default:
                return false;
        }
        return true;
    }
}
