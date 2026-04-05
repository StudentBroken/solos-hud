package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/* JADX INFO: loaded from: classes10.dex */
public final class zzi extends zzed implements zzg {
    zzi(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final float getBearing() throws RemoteException {
        Parcel parcelZza = zza(12, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final LatLngBounds getBounds() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        LatLngBounds latLngBounds = (LatLngBounds) zzef.zza(parcelZza, LatLngBounds.CREATOR);
        parcelZza.recycle();
        return latLngBounds;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final float getHeight() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final LatLng getPosition() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        LatLng latLng = (LatLng) zzef.zza(parcelZza, LatLng.CREATOR);
        parcelZza.recycle();
        return latLng;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final IObjectWrapper getTag() throws RemoteException {
        Parcel parcelZza = zza(25, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final float getTransparency() throws RemoteException {
        Parcel parcelZza = zza(18, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final float getWidth() throws RemoteException {
        Parcel parcelZza = zza(7, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(14, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(20, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final boolean isClickable() throws RemoteException {
        Parcel parcelZza = zza(23, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(16, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setBearing(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(11, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setClickable(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(22, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setDimensions(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setPosition(LatLng latLng) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLng);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setPositionFromBounds(LatLngBounds latLngBounds) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLngBounds);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setTag(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(24, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setTransparency(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(17, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(15, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void zzJ(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(21, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final boolean zzb(zzg zzgVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzgVar);
        Parcel parcelZza = zza(19, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzg
    public final void zzf(float f, float f2) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        parcelZzY.writeFloat(f2);
        zzb(6, parcelZzY);
    }
}
