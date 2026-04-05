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
public final class zzf extends zzed implements zzd {
    zzf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.ICircleDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final LatLng getCenter() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        LatLng latLng = (LatLng) zzef.zza(parcelZza, LatLng.CREATOR);
        parcelZza.recycle();
        return latLng;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final int getFillColor() throws RemoteException {
        Parcel parcelZza = zza(12, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final double getRadius() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        double d = parcelZza.readDouble();
        parcelZza.recycle();
        return d;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final int getStrokeColor() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final List<PatternItem> getStrokePattern() throws RemoteException {
        Parcel parcelZza = zza(22, zzY());
        ArrayList arrayListCreateTypedArrayList = parcelZza.createTypedArrayList(PatternItem.CREATOR);
        parcelZza.recycle();
        return arrayListCreateTypedArrayList;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final float getStrokeWidth() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final IObjectWrapper getTag() throws RemoteException {
        Parcel parcelZza = zza(24, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(14, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(18, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final boolean isClickable() throws RemoteException {
        Parcel parcelZza = zza(20, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(16, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setCenter(LatLng latLng) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLng);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setClickable(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(19, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setFillColor(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(11, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setRadius(double d) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeDouble(d);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setStrokeColor(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setStrokePattern(List<PatternItem> list) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeTypedList(list);
        zzb(21, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setStrokeWidth(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setTag(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(23, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(15, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzd
    public final boolean zzb(zzd zzdVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzdVar);
        Parcel parcelZza = zza(17, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
