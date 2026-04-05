package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zzd extends com.google.android.gms.maps.internal.zzac {
    private /* synthetic */ GoogleMap.OnInfoWindowClickListener zzblG;

    zzd(GoogleMap googleMap, GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener) {
        this.zzblG = onInfoWindowClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzab
    public final void zze(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblG.onInfoWindowClick(new Marker(zzpVar));
    }
}
