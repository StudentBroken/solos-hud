package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;

/* JADX INFO: loaded from: classes10.dex */
final class zzv extends com.google.android.gms.maps.internal.zzq {
    private /* synthetic */ GoogleMap.OnCameraMoveCanceledListener zzblY;

    zzv(GoogleMap googleMap, GoogleMap.OnCameraMoveCanceledListener onCameraMoveCanceledListener) {
        this.zzblY = onCameraMoveCanceledListener;
    }

    @Override // com.google.android.gms.maps.internal.zzp
    public final void onCameraMoveCanceled() {
        this.zzblY.onCameraMoveCanceled();
    }
}
