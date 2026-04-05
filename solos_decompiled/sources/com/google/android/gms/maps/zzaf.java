package com.google.android.gms.maps;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.internal.zzbm;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/* JADX INFO: loaded from: classes10.dex */
final class zzaf extends zzbm {
    private /* synthetic */ StreetViewPanorama.OnStreetViewPanoramaLongClickListener zzbmK;

    zzaf(StreetViewPanorama streetViewPanorama, StreetViewPanorama.OnStreetViewPanoramaLongClickListener onStreetViewPanoramaLongClickListener) {
        this.zzbmK = onStreetViewPanoramaLongClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbl
    public final void onStreetViewPanoramaLongClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
        this.zzbmK.onStreetViewPanoramaLongClick(streetViewPanoramaOrientation);
    }
}
