package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes36.dex */
public abstract class zzcgq extends zzee implements zzcgp {
    public zzcgq() {
        attachInterface(this, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                zza((zzcgl) zzef.zza(parcel, zzcgl.CREATOR), (zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                break;
            case 2:
                zza((zzcku) zzef.zza(parcel, zzcku.CREATOR), (zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                break;
            case 3:
            case 8:
            default:
                return false;
            case 4:
                zza((zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                break;
            case 5:
                zza((zzcgl) zzef.zza(parcel, zzcgl.CREATOR), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                break;
            case 6:
                zzb((zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                break;
            case 7:
                List<zzcku> listZza = zza((zzcft) zzef.zza(parcel, zzcft.CREATOR), zzef.zza(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(listZza);
                break;
            case 9:
                byte[] bArrZza = zza((zzcgl) zzef.zza(parcel, zzcgl.CREATOR), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeByteArray(bArrZza);
                break;
            case 10:
                zza(parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                break;
            case 11:
                String strZzc = zzc((zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                parcel2.writeString(strZzc);
                break;
            case 12:
                zza((zzcfw) zzef.zza(parcel, zzcfw.CREATOR), (zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                break;
            case 13:
                zzb((zzcfw) zzef.zza(parcel, zzcfw.CREATOR));
                parcel2.writeNoException();
                break;
            case 14:
                List<zzcku> listZza2 = zza(parcel.readString(), parcel.readString(), zzef.zza(parcel), (zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(listZza2);
                break;
            case 15:
                List<zzcku> listZza3 = zza(parcel.readString(), parcel.readString(), parcel.readString(), zzef.zza(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(listZza3);
                break;
            case 16:
                List<zzcfw> listZza4 = zza(parcel.readString(), parcel.readString(), (zzcft) zzef.zza(parcel, zzcft.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(listZza4);
                break;
            case 17:
                List<zzcfw> listZzk = zzk(parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeTypedList(listZzk);
                break;
        }
        return true;
    }
}
