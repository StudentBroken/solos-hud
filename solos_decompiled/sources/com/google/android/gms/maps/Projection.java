package com.google.android.gms.maps;

import android.graphics.Point;
import android.os.RemoteException;
import com.google.android.gms.maps.internal.IProjectionDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.VisibleRegion;

/* JADX INFO: loaded from: classes10.dex */
public final class Projection {
    private final IProjectionDelegate zzbmF;

    Projection(IProjectionDelegate iProjectionDelegate) {
        this.zzbmF = iProjectionDelegate;
    }

    public final LatLng fromScreenLocation(Point point) {
        try {
            return this.zzbmF.fromScreenLocation(com.google.android.gms.dynamic.zzn.zzw(point));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final VisibleRegion getVisibleRegion() {
        try {
            return this.zzbmF.getVisibleRegion();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Point toScreenLocation(LatLng latLng) {
        try {
            return (Point) com.google.android.gms.dynamic.zzn.zzE(this.zzbmF.toScreenLocation(latLng));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
