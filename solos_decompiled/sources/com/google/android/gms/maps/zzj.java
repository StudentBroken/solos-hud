package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzam;

/* JADX INFO: loaded from: classes10.dex */
final class zzj extends zzam {
    private /* synthetic */ GoogleMap.OnMapLoadedCallback zzblM;

    zzj(GoogleMap googleMap, GoogleMap.OnMapLoadedCallback onMapLoadedCallback) {
        this.zzblM = onMapLoadedCallback;
    }

    @Override // com.google.android.gms.maps.internal.zzal
    public final void onMapLoaded() throws RemoteException {
        this.zzblM.onMapLoaded();
    }
}
