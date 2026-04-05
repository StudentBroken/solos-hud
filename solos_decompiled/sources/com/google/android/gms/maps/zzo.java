package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzbc;
import com.google.android.gms.maps.model.Polygon;

/* JADX INFO: loaded from: classes10.dex */
final class zzo extends zzbc {
    private /* synthetic */ GoogleMap.OnPolygonClickListener zzblR;

    zzo(GoogleMap googleMap, GoogleMap.OnPolygonClickListener onPolygonClickListener) {
        this.zzblR = onPolygonClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbb
    public final void zza(com.google.android.gms.maps.model.internal.zzs zzsVar) {
        this.zzblR.onPolygonClick(new Polygon(zzsVar));
    }
}
