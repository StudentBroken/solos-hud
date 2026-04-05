package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class zzu extends zzed implements zzs {
    zzu(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IPolygonDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final int getFillColor() throws RemoteException {
        Parcel parcelZza = zza(12, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final List getHoles() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        ArrayList arrayListZzb = zzef.zzb(parcelZza);
        parcelZza.recycle();
        return arrayListZzb;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final List<LatLng> getPoints() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(LatLng.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final int getStrokeColor() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final int getStrokeJointType() throws RemoteException {
        Parcel parcelZza = zza(24, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final List<PatternItem> getStrokePattern() throws RemoteException {
        Parcel parcelZza = zza(26, zzY());
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(PatternItem.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final float getStrokeWidth() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final IObjectWrapper getTag() throws RemoteException {
        Parcel parcelZza = zza(28, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(14, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(20, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final boolean isClickable() throws RemoteException {
        Parcel parcelZza = zza(22, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final boolean isGeodesic() throws RemoteException {
        Parcel parcelZza = zza(18, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(16, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setClickable(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(21, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setFillColor(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(11, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setGeodesic(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(17, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setHoles(List list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeList(list);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setPoints(List<LatLng> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setStrokeColor(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setStrokeJointType(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(23, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setStrokePattern(List<PatternItem> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzb(25, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setStrokeWidth(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setTag(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(27, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(15, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzs
    public final boolean zzb(zzs zzsVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzsVar);
        Parcel parcelZza = zza(19, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
