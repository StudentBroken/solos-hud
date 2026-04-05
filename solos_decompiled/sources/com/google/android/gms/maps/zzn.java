package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;

/* JADX INFO: loaded from: classes10.dex */
final class zzn extends com.google.android.gms.maps.internal.zzw {
    private /* synthetic */ GoogleMap.OnCircleClickListener zzblQ;

    zzn(GoogleMap googleMap, GoogleMap.OnCircleClickListener onCircleClickListener) {
        this.zzblQ = onCircleClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzv
    public final void zza(com.google.android.gms.maps.model.internal.zzd zzdVar) {
        this.zzblQ.onCircleClick(new Circle(zzdVar));
    }
}
