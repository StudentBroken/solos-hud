package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzau;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zzc extends zzau {
    private /* synthetic */ GoogleMap.OnMarkerDragListener zzblF;

    zzc(GoogleMap googleMap, GoogleMap.OnMarkerDragListener onMarkerDragListener) {
        this.zzblF = onMarkerDragListener;
    }

    @Override // com.google.android.gms.maps.internal.zzat
    public final void zzb(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblF.onMarkerDragStart(new Marker(zzpVar));
    }

    @Override // com.google.android.gms.maps.internal.zzat
    public final void zzc(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblF.onMarkerDragEnd(new Marker(zzpVar));
    }

    @Override // com.google.android.gms.maps.internal.zzat
    public final void zzd(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblF.onMarkerDrag(new Marker(zzpVar));
    }
}
