package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes10.dex */
public interface zzan extends IInterface {
    void onMapLongClick(LatLng latLng) throws RemoteException;
}
