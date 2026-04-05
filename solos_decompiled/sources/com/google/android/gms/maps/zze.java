package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zze extends com.google.android.gms.maps.internal.zzag {
    private /* synthetic */ GoogleMap.OnInfoWindowLongClickListener zzblH;

    zze(GoogleMap googleMap, GoogleMap.OnInfoWindowLongClickListener onInfoWindowLongClickListener) {
        this.zzblH = onInfoWindowLongClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzaf
    public final void zzf(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblH.onInfoWindowLongClick(new Marker(zzpVar));
    }
}
