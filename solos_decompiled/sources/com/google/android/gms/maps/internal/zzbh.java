package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

/* JADX INFO: loaded from: classes10.dex */
public interface zzbh extends IInterface {
    void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) throws RemoteException;
}
