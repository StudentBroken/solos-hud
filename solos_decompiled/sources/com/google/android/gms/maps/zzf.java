package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zzf extends com.google.android.gms.maps.internal.zzae {
    private /* synthetic */ GoogleMap.OnInfoWindowCloseListener zzblI;

    zzf(GoogleMap googleMap, GoogleMap.OnInfoWindowCloseListener onInfoWindowCloseListener) {
        this.zzblI = onInfoWindowCloseListener;
    }

    @Override // com.google.android.gms.maps.internal.zzad
    public final void zzg(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzblI.onInfoWindowClose(new Marker(zzpVar));
    }
}
