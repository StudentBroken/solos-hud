package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzba;
import com.google.android.gms.maps.model.PointOfInterest;

/* JADX INFO: loaded from: classes10.dex */
final class zzr extends zzba {
    private /* synthetic */ GoogleMap.OnPoiClickListener zzblU;

    zzr(GoogleMap googleMap, GoogleMap.OnPoiClickListener onPoiClickListener) {
        this.zzblU = onPoiClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzaz
    public final void zza(PointOfInterest pointOfInterest) throws RemoteException {
        this.zzblU.onPoiClick(pointOfInterest);
    }
}
