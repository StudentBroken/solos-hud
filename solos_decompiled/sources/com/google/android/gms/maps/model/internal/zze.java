package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public abstract class zze extends zzee implements zzd {
    public static zzd zzab(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
        return iInterfaceQueryLocalInterface instanceof zzd ? (zzd) iInterfaceQueryLocalInterface : new zzf(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzd zzfVar;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                remove();
                parcel2.writeNoException();
                break;
            case 2:
                String id = getId();
                parcel2.writeNoException();
                parcel2.writeString(id);
                break;
            case 3:
                setCenter((LatLng) zzef.zza(parcel, LatLng.CREATOR));
                parcel2.writeNoException();
                break;
            case 4:
                LatLng center = getCenter();
                parcel2.writeNoException();
                zzef.zzb(parcel2, center);
                break;
            case 5:
                setRadius(parcel.readDouble());
                parcel2.writeNoException();
                break;
            case 6:
                double radius = getRadius();
                parcel2.writeNoException();
                parcel2.writeDouble(radius);
                break;
            case 7:
                setStrokeWidth(parcel.readFloat());
                parcel2.writeNoException();
                break;
            case 8:
                float strokeWidth = getStrokeWidth();
                parcel2.writeNoException();
                parcel2.writeFloat(strokeWidth);
                break;
            case 9:
                setStrokeColor(parcel.readInt());
                parcel2.writeNoException();
                break;
            case 10:
                int strokeColor = getStrokeColor();
                parcel2.writeNoException();
                parcel2.writeInt(strokeColor);
                break;
            case 11:
                setFillColor(parcel.readInt());
                parcel2.writeNoException();
                break;
            case 12:
                int fillColor = getFillColor();
                parcel2.writeNoException();
                parcel2.writeInt(fillColor);
                break;
            case 13:
                setZIndex(parcel.readFloat());
                parcel2.writeNoException();
                break;
            case 14:
                float zIndex = getZIndex();
                parcel2.writeNoException();
                parcel2.writeFloat(zIndex);
                break;
            case 15:
                setVisible(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 16:
                boolean zIsVisible = isVisible();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsVisible);
                break;
            case 17:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder == null) {
                    zzfVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    zzfVar = iInterfaceQueryLocalInterface instanceof zzd ? (zzd) iInterfaceQueryLocalInterface : new zzf(strongBinder);
                }
                boolean zZzb = zzb(zzfVar);
                parcel2.writeNoException();
                zzef.zza(parcel2, zZzb);
                break;
            case 18:
                int iHashCodeRemote = hashCodeRemote();
                parcel2.writeNoException();
                parcel2.writeInt(iHashCodeRemote);
                break;
            case 19:
                setClickable(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 20:
                boolean zIsClickable = isClickable();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsClickable);
                break;
            case 21:
                setStrokePattern(parcel.createTypedArrayList(PatternItem.CREATOR));
                parcel2.writeNoException();
                break;
            case 22:
                List<PatternItem> strokePattern = getStrokePattern();
                parcel2.writeNoException();
                parcel2.writeTypedList(strokePattern);
                break;
            case 23:
                setTag(IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
                parcel2.writeNoException();
                break;
            case 24:
                IObjectWrapper tag = getTag();
                parcel2.writeNoException();
                zzef.zza(parcel2, tag);
                break;
            default:
                return false;
        }
        return true;
    }
}
