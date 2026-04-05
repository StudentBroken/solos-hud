package com.google.android.gms.maps;

import android.location.Location;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzay;

/* JADX INFO: loaded from: classes10.dex */
final class zzh extends zzay {
    private /* synthetic */ GoogleMap.OnMyLocationChangeListener zzblK;

    zzh(GoogleMap googleMap, GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener) {
        this.zzblK = onMyLocationChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzax
    public final void zzF(IObjectWrapper iObjectWrapper) {
        this.zzblK.onMyLocationChange((Location) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapper));
    }
}
