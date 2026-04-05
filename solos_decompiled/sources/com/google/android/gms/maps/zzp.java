package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzbe;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;

/* JADX INFO: loaded from: classes10.dex */
final class zzp extends zzbe {
    private /* synthetic */ GoogleMap.OnPolylineClickListener zzblS;

    zzp(GoogleMap googleMap, GoogleMap.OnPolylineClickListener onPolylineClickListener) {
        this.zzblS = onPolylineClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzbd
    public final void zza(IPolylineDelegate iPolylineDelegate) {
        this.zzblS.onPolylineClick(new Polyline(iPolylineDelegate));
    }
}
