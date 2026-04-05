package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.IndoorBuilding;

/* JADX INFO: loaded from: classes10.dex */
final class zza extends com.google.android.gms.maps.internal.zzaa {
    private /* synthetic */ GoogleMap.OnIndoorStateChangeListener zzblD;

    zza(GoogleMap googleMap, GoogleMap.OnIndoorStateChangeListener onIndoorStateChangeListener) {
        this.zzblD = onIndoorStateChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzz
    public final void onIndoorBuildingFocused() {
        this.zzblD.onIndoorBuildingFocused();
    }

    @Override // com.google.android.gms.maps.internal.zzz
    public final void zza(com.google.android.gms.maps.model.internal.zzj zzjVar) {
        this.zzblD.onIndoorLevelActivated(new IndoorBuilding(zzjVar));
    }
}
