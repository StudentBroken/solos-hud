package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;

/* JADX INFO: loaded from: classes10.dex */
final class zzw extends com.google.android.gms.maps.internal.zzo {
    private /* synthetic */ GoogleMap.OnCameraIdleListener zzblZ;

    zzw(GoogleMap googleMap, GoogleMap.OnCameraIdleListener onCameraIdleListener) {
        this.zzblZ = onCameraIdleListener;
    }

    @Override // com.google.android.gms.maps.internal.zzn
    public final void onCameraIdle() {
        this.zzblZ.onCameraIdle();
    }
}
