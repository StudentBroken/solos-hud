package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.StreetViewPanoramaOptions;

/* JADX INFO: loaded from: classes10.dex */
public interface zze extends IInterface {
    IMapFragmentDelegate zzH(IObjectWrapper iObjectWrapper) throws RemoteException;

    IStreetViewPanoramaFragmentDelegate zzI(IObjectWrapper iObjectWrapper) throws RemoteException;

    IMapViewDelegate zza(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions) throws RemoteException;

    IStreetViewPanoramaViewDelegate zza(IObjectWrapper iObjectWrapper, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException;

    void zzi(IObjectWrapper iObjectWrapper, int i) throws RemoteException;

    ICameraUpdateFactoryDelegate zzwg() throws RemoteException;

    com.google.android.gms.maps.model.internal.zza zzwh() throws RemoteException;
}
