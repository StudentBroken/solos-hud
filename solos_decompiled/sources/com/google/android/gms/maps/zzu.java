package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;

/* JADX INFO: loaded from: classes10.dex */
final class zzu extends com.google.android.gms.maps.internal.zzs {
    private /* synthetic */ GoogleMap.OnCameraMoveListener zzblX;

    zzu(GoogleMap googleMap, GoogleMap.OnCameraMoveListener onCameraMoveListener) {
        this.zzblX = onCameraMoveListener;
    }

    @Override // com.google.android.gms.maps.internal.zzr
    public final void onCameraMove() {
        this.zzblX.onCameraMove();
    }
}
