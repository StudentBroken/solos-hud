package com.google.android.gms.maps;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.internal.zzbi;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

/* JADX INFO: loaded from: classes10.dex */
final class zzac extends zzbi {
    private /* synthetic */ StreetViewPanorama.OnStreetViewPanoramaChangeListener zzbmH;

    zzac(StreetViewPanorama streetViewPanorama, StreetViewPanorama.OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener) {
        this.zzbmH = onStreetViewPanoramaChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbh
    public final void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
        this.zzbmH.onStreetViewPanoramaChange(streetViewPanoramaLocation);
    }
}
