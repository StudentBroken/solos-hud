package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zzx extends zzee implements zzw {
    public static zzw zzai(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ITileOverlayDelegate");
        return iInterfaceQueryLocalInterface instanceof zzw ? (zzw) iInterfaceQueryLocalInterface : new zzy(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzw zzyVar;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                remove();
                parcel2.writeNoException();
                break;
            case 2:
                clearTileCache();
                parcel2.writeNoException();
                break;
            case 3:
                String id = getId();
                parcel2.writeNoException();
                parcel2.writeString(id);
                break;
            case 4:
                setZIndex(parcel.readFloat());
                parcel2.writeNoException();
                break;
            case 5:
                float zIndex = getZIndex();
                parcel2.writeNoException();
                parcel2.writeFloat(zIndex);
                break;
            case 6:
                setVisible(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 7:
                boolean zIsVisible = isVisible();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsVisible);
                break;
            case 8:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder == null) {
                    zzyVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ITileOverlayDelegate");
                    zzyVar = iInterfaceQueryLocalInterface instanceof zzw ? (zzw) iInterfaceQueryLocalInterface : new zzy(strongBinder);
                }
                boolean zZza = zza(zzyVar);
                parcel2.writeNoException();
                zzef.zza(parcel2, zZza);
                break;
            case 9:
                int iHashCodeRemote = hashCodeRemote();
                parcel2.writeNoException();
                parcel2.writeInt(iHashCodeRemote);
                break;
            case 10:
                setFadeIn(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 11:
                boolean fadeIn = getFadeIn();
                parcel2.writeNoException();
                zzef.zza(parcel2, fadeIn);
                break;
            case 12:
                setTransparency(parcel.readFloat());
                parcel2.writeNoException();
                break;
            case 13:
                float transparency = getTransparency();
                parcel2.writeNoException();
                parcel2.writeFloat(transparency);
                break;
            default:
                return false;
        }
        return true;
    }
}
