package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;

/* JADX INFO: loaded from: classes10.dex */
final class zzt extends com.google.android.gms.maps.internal.zzu {
    private /* synthetic */ GoogleMap.OnCameraMoveStartedListener zzblW;

    zzt(GoogleMap googleMap, GoogleMap.OnCameraMoveStartedListener onCameraMoveStartedListener) {
        this.zzblW = onCameraMoveStartedListener;
    }

    @Override // com.google.android.gms.maps.internal.zzt
    public final void onCameraMoveStarted(int i) {
        this.zzblW.onCameraMoveStarted(i);
    }
}
