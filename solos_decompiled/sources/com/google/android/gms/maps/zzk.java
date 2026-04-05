package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlay;

/* JADX INFO: loaded from: classes10.dex */
final class zzk extends com.google.android.gms.maps.internal.zzy {
    private /* synthetic */ GoogleMap.OnGroundOverlayClickListener zzblN;

    zzk(GoogleMap googleMap, GoogleMap.OnGroundOverlayClickListener onGroundOverlayClickListener) {
        this.zzblN = onGroundOverlayClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzx
    public final void zza(com.google.android.gms.maps.model.internal.zzg zzgVar) {
        this.zzblN.onGroundOverlayClick(new GroundOverlay(zzgVar));
    }
}
