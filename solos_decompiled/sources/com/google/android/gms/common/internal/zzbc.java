package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzbc extends zzee implements zzbb {
    public static zzbb zzJ(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGoogleCertificatesApi");
        return iInterfaceQueryLocalInterface instanceof zzbb ? (zzbb) iInterfaceQueryLocalInterface : new zzbd(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                IObjectWrapper iObjectWrapperZzrE = zzrE();
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzrE);
                break;
            case 2:
                IObjectWrapper iObjectWrapperZzrF = zzrF();
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZzrF);
                break;
            case 3:
                boolean zZze = zze(parcel.readString(), IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
                parcel2.writeNoException();
                zzef.zza(parcel2, zZze);
                break;
            case 4:
                boolean zZzf = zzf(parcel.readString(), IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
                parcel2.writeNoException();
                zzef.zza(parcel2, zZzf);
                break;
            case 5:
                boolean zZza = zza((com.google.android.gms.common.zzm) zzef.zza(parcel, com.google.android.gms.common.zzm.CREATOR), IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
                parcel2.writeNoException();
                zzef.zza(parcel2, zZza);
                break;
            default:
                return false;
        }
        return true;
    }
}
