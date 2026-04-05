package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public interface IPolylineDelegate extends IInterface {

    public static abstract class zza extends zzee implements IPolylineDelegate {
        public static IPolylineDelegate zzah(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IPolylineDelegate");
            return iInterfaceQueryLocalInterface instanceof IPolylineDelegate ? (IPolylineDelegate) iInterfaceQueryLocalInterface : new zzv(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IPolylineDelegate zzvVar;
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
                    setWidth(parcel.readFloat());
                    parcel2.writeNoException();
                    break;
                case 6:
                    float width = getWidth();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    break;
                case 7:
                    setColor(parcel.readInt());
                    parcel2.writeNoException();
                    break;
                case 8:
                    int color = getColor();
                    parcel2.writeNoException();
                    parcel2.writeInt(color);
                    break;
                case 9:
                    setZIndex(parcel.readFloat());
                    parcel2.writeNoException();
                    break;
                case 10:
                    float zIndex = getZIndex();
                    parcel2.writeNoException();
                    parcel2.writeFloat(zIndex);
                    break;
                case 11:
                    setVisible(zzef.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 12:
                    boolean zIsVisible = isVisible();
                    parcel2.writeNoException();
                    zzef.zza(parcel2, zIsVisible);
                    break;
                case 13:
                    setGeodesic(zzef.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 14:
                    boolean zIsGeodesic = isGeodesic();
                    parcel2.writeNoException();
                    zzef.zza(parcel2, zIsGeodesic);
                    break;
                case 15:
                    IBinder strongBinder = parcel.readStrongBinder();
                    if (strongBinder == null) {
                        zzvVar = null;
                    } else {
                        IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IPolylineDelegate");
                        zzvVar = iInterfaceQueryLocalInterface instanceof IPolylineDelegate ? (IPolylineDelegate) iInterfaceQueryLocalInterface : new zzv(strongBinder);
                    }
                    boolean zEqualsRemote = equalsRemote(zzvVar);
                    parcel2.writeNoException();
                    zzef.zza(parcel2, zEqualsRemote);
                    break;
                case 16:
                    int iHashCodeRemote = hashCodeRemote();
                    parcel2.writeNoException();
                    parcel2.writeInt(iHashCodeRemote);
                    break;
                case 17:
                    setClickable(zzef.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 18:
                    boolean zIsClickable = isClickable();
                    parcel2.writeNoException();
                    zzef.zza(parcel2, zIsClickable);
                    break;
                case 19:
                    setStartCap((Cap) zzef.zza(parcel, Cap.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 20:
                    Cap startCap = getStartCap();
                    parcel2.writeNoException();
                    zzef.zzb(parcel2, startCap);
                    break;
                case 21:
                    setEndCap((Cap) zzef.zza(parcel, Cap.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 22:
                    Cap endCap = getEndCap();
                    parcel2.writeNoException();
                    zzef.zzb(parcel2, endCap);
                    break;
                case 23:
                    setJointType(parcel.readInt());
                    parcel2.writeNoException();
                    break;
                case 24:
                    int jointType = getJointType();
                    parcel2.writeNoException();
                    parcel2.writeInt(jointType);
                    break;
                case 25:
                    setPattern(parcel.createTypedArrayList(PatternItem.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 26:
                    List<PatternItem> pattern = getPattern();
                    parcel2.writeNoException();
                    parcel2.writeTypedList(pattern);
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

    boolean equalsRemote(IPolylineDelegate iPolylineDelegate) throws RemoteException;

    int getColor() throws RemoteException;

    Cap getEndCap() throws RemoteException;

    String getId() throws RemoteException;

    int getJointType() throws RemoteException;

    List<PatternItem> getPattern() throws RemoteException;

    List<LatLng> getPoints() throws RemoteException;

    Cap getStartCap() throws RemoteException;

    IObjectWrapper getTag() throws RemoteException;

    float getWidth() throws RemoteException;

    float getZIndex() throws RemoteException;

    int hashCodeRemote() throws RemoteException;

    boolean isClickable() throws RemoteException;

    boolean isGeodesic() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void remove() throws RemoteException;

    void setClickable(boolean z) throws RemoteException;

    void setColor(int i) throws RemoteException;

    void setEndCap(Cap cap) throws RemoteException;

    void setGeodesic(boolean z) throws RemoteException;

    void setJointType(int i) throws RemoteException;

    void setPattern(List<PatternItem> list) throws RemoteException;

    void setPoints(List<LatLng> list) throws RemoteException;

    void setStartCap(Cap cap) throws RemoteException;

    void setTag(IObjectWrapper iObjectWrapper) throws RemoteException;

    void setVisible(boolean z) throws RemoteException;

    void setWidth(float f) throws RemoteException;

    void setZIndex(float f) throws RemoteException;
}
