package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzao;
import com.google.android.gms.maps.model.LatLng;

/* JADX INFO: loaded from: classes10.dex */
final class zzy extends zzao {
    private /* synthetic */ GoogleMap.OnMapLongClickListener zzbmb;

    zzy(GoogleMap googleMap, GoogleMap.OnMapLongClickListener onMapLongClickListener) {
        this.zzbmb = onMapLongClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzan
    public final void onMapLongClick(LatLng latLng) {
        this.zzbmb.onMapLongClick(latLng);
    }
}
