package com.google.android.gms.maps;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.internal.zzbk;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/* JADX INFO: loaded from: classes10.dex */
final class zzae extends zzbk {
    private /* synthetic */ StreetViewPanorama.OnStreetViewPanoramaClickListener zzbmJ;

    zzae(StreetViewPanorama streetViewPanorama, StreetViewPanorama.OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener) {
        this.zzbmJ = onStreetViewPanoramaClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbj
    public final void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
        this.zzbmJ.onStreetViewPanoramaClick(streetViewPanoramaOrientation);
    }
}
