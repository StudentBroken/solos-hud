package com.google.android.gms.maps;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: loaded from: classes10.dex */
final class zzg extends com.google.android.gms.maps.internal.zzi {
    private /* synthetic */ GoogleMap.InfoWindowAdapter zzblJ;

    zzg(GoogleMap googleMap, GoogleMap.InfoWindowAdapter infoWindowAdapter) {
        this.zzblJ = infoWindowAdapter;
    }

    @Override // com.google.android.gms.maps.internal.zzh
    public final IObjectWrapper zzh(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        return com.google.android.gms.dynamic.zzn.zzw(this.zzblJ.getInfoWindow(new Marker(zzpVar)));
    }

    @Override // com.google.android.gms.maps.internal.zzh
    public final IObjectWrapper zzi(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        return com.google.android.gms.dynamic.zzn.zzw(this.zzblJ.getInfoContents(new Marker(zzpVar)));
    }
}
