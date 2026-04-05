package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzaw;

/* JADX INFO: loaded from: classes10.dex */
final class zzi extends zzaw {
    private /* synthetic */ GoogleMap.OnMyLocationButtonClickListener zzblL;

    zzi(GoogleMap googleMap, GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
        this.zzblL = onMyLocationButtonClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzav
    public final boolean onMyLocationButtonClick() throws RemoteException {
        return this.zzblL.onMyLocationButtonClick();
    }
}
