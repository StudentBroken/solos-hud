package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class zzv extends zzed implements IPolylineDelegate {
    zzv(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IPolylineDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final boolean equalsRemote(IPolylineDelegate iPolylineDelegate) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iPolylineDelegate);
        Parcel parcelZza = zza(15, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final int getColor() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final Cap getEndCap() throws RemoteException {
        Parcel parcelZza = zza(22, zzY());
        Cap cap = (Cap) zzef.zza(parcelZza, Cap.CREATOR);
        parcelZza.recycle();
        return cap;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final int getJointType() throws RemoteException {
        Parcel parcelZza = zza(24, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final List<PatternItem> getPattern() throws RemoteException {
        Parcel parcelZza = zza(26, zzY());
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(PatternItem.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final List<LatLng> getPoints() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(LatLng.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final Cap getStartCap() throws RemoteException {
        Parcel parcelZza = zza(20, zzY());
        Cap cap = (Cap) zzef.zza(parcelZza, Cap.CREATOR);
        parcelZza.recycle();
        return cap;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final IObjectWrapper getTag() throws RemoteException {
        Parcel parcelZza = zza(28, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final float getWidth() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(16, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final boolean isClickable() throws RemoteException {
        Parcel parcelZza = zza(18, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final boolean isGeodesic() throws RemoteException {
        Parcel parcelZza = zza(14, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(12, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setClickable(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(17, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setColor(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setEndCap(Cap cap) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, cap);
        zzb(21, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setGeodesic(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setJointType(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(23, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setPattern(List<PatternItem> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzb(25, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setPoints(List<LatLng> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setStartCap(Cap cap) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, cap);
        zzb(19, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setTag(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(27, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(11, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setWidth(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.IPolylineDelegate
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(9, parcelZzY);
    }
}
