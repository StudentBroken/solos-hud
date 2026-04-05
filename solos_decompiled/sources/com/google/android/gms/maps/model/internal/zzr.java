package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes10.dex */
public final class zzr extends zzed implements zzp {
    zzr(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IMarkerDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final float getAlpha() throws RemoteException {
        Parcel parcelZza = zza(26, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final LatLng getPosition() throws RemoteException {
        Parcel parcelZza = zza(4, zzY());
        LatLng latLng = (LatLng) zzef.zza(parcelZza, LatLng.CREATOR);
        parcelZza.recycle();
        return latLng;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final float getRotation() throws RemoteException {
        Parcel parcelZza = zza(23, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final String getSnippet() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final IObjectWrapper getTag() throws RemoteException {
        Parcel parcelZza = zza(30, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final String getTitle() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(28, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(17, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void hideInfoWindow() throws RemoteException {
        zzb(12, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final boolean isDraggable() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final boolean isFlat() throws RemoteException {
        Parcel parcelZza = zza(21, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final boolean isInfoWindowShown() throws RemoteException {
        Parcel parcelZza = zza(13, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(15, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setAlpha(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(25, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setAnchor(float f, float f2) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        parcelZzY.writeFloat(f2);
        zzb(19, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setDraggable(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setFlat(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(20, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setInfoWindowAnchor(float f, float f2) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        parcelZzY.writeFloat(f2);
        zzb(24, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setPosition(LatLng latLng) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLng);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setRotation(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(22, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setSnippet(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setTag(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(29, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setTitle(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(14, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(27, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void showInfoWindow() throws RemoteException {
        zzb(11, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final void zzK(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(18, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzp
    public final boolean zzj(zzp zzpVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzpVar);
        Parcel parcelZza = zza(16, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
