package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/* JADX INFO: loaded from: classes10.dex */
public final class zzbs extends zzed implements IStreetViewPanoramaDelegate {
    zzbs(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void animateTo(StreetViewPanoramaCamera streetViewPanoramaCamera, long j) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, streetViewPanoramaCamera);
        parcelZzY.writeLong(j);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void enablePanning(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void enableStreetNames(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(4, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void enableUserNavigation(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void enableZoom(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(1, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final StreetViewPanoramaCamera getPanoramaCamera() throws RemoteException {
        Parcel parcelZza = zza(10, zzY());
        StreetViewPanoramaCamera streetViewPanoramaCamera = (StreetViewPanoramaCamera) zzef.zza(parcelZza, StreetViewPanoramaCamera.CREATOR);
        parcelZza.recycle();
        return streetViewPanoramaCamera;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final StreetViewPanoramaLocation getStreetViewPanoramaLocation() throws RemoteException {
        Parcel parcelZza = zza(14, zzY());
        StreetViewPanoramaLocation streetViewPanoramaLocation = (StreetViewPanoramaLocation) zzef.zza(parcelZza, StreetViewPanoramaLocation.CREATOR);
        parcelZza.recycle();
        return streetViewPanoramaLocation;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final boolean isPanningGesturesEnabled() throws RemoteException {
        Parcel parcelZza = zza(6, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final boolean isStreetNamesEnabled() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final boolean isUserNavigationEnabled() throws RemoteException {
        Parcel parcelZza = zza(7, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final boolean isZoomGesturesEnabled() throws RemoteException {
        Parcel parcelZza = zza(5, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final IObjectWrapper orientationToPoint(StreetViewPanoramaOrientation streetViewPanoramaOrientation) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, streetViewPanoramaOrientation);
        Parcel parcelZza = zza(19, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final StreetViewPanoramaOrientation pointToOrientation(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(18, parcelZzY);
        StreetViewPanoramaOrientation streetViewPanoramaOrientation = (StreetViewPanoramaOrientation) zzef.zza(parcelZza, StreetViewPanoramaOrientation.CREATOR);
        parcelZza.recycle();
        return streetViewPanoramaOrientation;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setOnStreetViewPanoramaCameraChangeListener(zzbf zzbfVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbfVar);
        zzb(16, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setOnStreetViewPanoramaChangeListener(zzbh zzbhVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbhVar);
        zzb(15, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setOnStreetViewPanoramaClickListener(zzbj zzbjVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbjVar);
        zzb(17, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setOnStreetViewPanoramaLongClickListener(zzbl zzblVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzblVar);
        zzb(20, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setPosition(LatLng latLng) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLng);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setPositionWithID(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzb(11, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate
    public final void setPositionWithRadius(LatLng latLng, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLng);
        parcelZzY.writeInt(i);
        zzb(13, parcelZzY);
    }
}
