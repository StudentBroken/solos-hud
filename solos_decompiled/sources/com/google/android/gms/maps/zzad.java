package com.google.android.gms.maps;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.internal.zzbg;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/* JADX INFO: loaded from: classes10.dex */
final class zzad extends zzbg {
    private /* synthetic */ StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener zzbmI;

    zzad(StreetViewPanorama streetViewPanorama, StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener) {
        this.zzbmI = onStreetViewPanoramaCameraChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbf
    public final void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera) {
        this.zzbmI.onStreetViewPanoramaCameraChange(streetViewPanoramaCamera);
    }
}
