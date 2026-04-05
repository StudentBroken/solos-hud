package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes10.dex */
final class zzx extends com.google.android.gms.maps.internal.zzak {
    private /* synthetic */ GoogleMap.OnMapClickListener zzbma;

    zzx(GoogleMap googleMap, GoogleMap.OnMapClickListener onMapClickListener) {
        this.zzbma = onMapClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzaj
    public final void onMapClick(LatLng latLng) {
        this.zzbma.onMapClick(latLng);
    }
}
