package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzas;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zzb extends zzas {
    private /* synthetic */ GoogleMap.OnMarkerClickListener zzblE;

    zzb(GoogleMap googleMap, GoogleMap.OnMarkerClickListener onMarkerClickListener) {
        this.zzblE = onMarkerClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzar
    public final boolean zza(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        return this.zzblE.onMarkerClick(new Marker(zzpVar));
    }
}
