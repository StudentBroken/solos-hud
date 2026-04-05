package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

/* JADX INFO: loaded from: classes10.dex */
final class zzs extends com.google.android.gms.maps.internal.zzm {
    private /* synthetic */ GoogleMap.OnCameraChangeListener zzblV;

    zzs(GoogleMap googleMap, GoogleMap.OnCameraChangeListener onCameraChangeListener) {
        this.zzblV = onCameraChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzl
    public final void onCameraChange(CameraPosition cameraPosition) {
        this.zzblV.onCameraChange(cameraPosition);
    }
}
