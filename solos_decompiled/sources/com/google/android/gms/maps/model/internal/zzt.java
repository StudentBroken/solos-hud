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
public abstract class zzt extends zzee implements zzs {
    public static zzs zzag(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IPolygonDelegate");
        return iInterfaceQueryLocalInterface instanceof zzs ? (zzs) iInterfaceQueryLocalInterface : new zzu(iBinder);
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzs zzuVar;
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
                setPoints(parcel.createTypedArrayList(LatLng.CREATOR));
                parcel2.writeNoException();
                break;
            case 4:
                List<LatLng> points = getPoints();
                parcel2.writeNoException();
                parcel2.writeTypedList(points);
                break;
            case 5:
                setHoles(zzef.zzb(parcel));
                parcel2.writeNoException();
                break;
            case 6:
                List holes = getHoles();
                parcel2.writeNoException();
                parcel2.writeList(holes);
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
                setGeodesic(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 18:
                boolean zIsGeodesic = isGeodesic();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsGeodesic);
                break;
            case 19:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder == null) {
                    zzuVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IPolygonDelegate");
                    zzuVar = iInterfaceQueryLocalInterface instanceof zzs ? (zzs) iInterfaceQueryLocalInterface : new zzu(strongBinder);
                }
                boolean zZzb = zzb(zzuVar);
                parcel2.writeNoException();
                zzef.zza(parcel2, zZzb);
                break;
            case 20:
                int iHashCodeRemote = hashCodeRemote();
                parcel2.writeNoException();
                parcel2.writeInt(iHashCodeRemote);
                break;
            case 21:
                setClickable(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 22:
                boolean zIsClickable = isClickable();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsClickable);
                break;
            case 23:
                setStrokeJointType(parcel.readInt());
                parcel2.writeNoException();
                break;
            case 24:
                int strokeJointType = getStrokeJointType();
                parcel2.writeNoException();
                parcel2.writeInt(strokeJointType);
                break;
            case 25:
                setStrokePattern(parcel.createTypedArrayList(PatternItem.CREATOR));
                parcel2.writeNoException();
                break;
            case 26:
                List<PatternItem> strokePattern = getStrokePattern();
                parcel2.writeNoException();
                parcel2.writeTypedList(strokePattern);
                break;
            case 27:
                setTag(IObjectWrapper.zza.zzM(parcel.readStrongBinder()));
                parcel2.writeNoException();
                break;
            case 28:
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
