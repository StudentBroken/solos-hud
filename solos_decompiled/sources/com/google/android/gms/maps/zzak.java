package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.zzbo;

/* JADX INFO: loaded from: classes10.dex */
final class zzak extends zzbo {
    private /* synthetic */ OnStreetViewPanoramaReadyCallback zzbmN;

    zzak(SupportStreetViewPanoramaFragment.zza zzaVar, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
        this.zzbmN = onStreetViewPanoramaReadyCallback;
    }

    @Override // com.google.android.gms.maps.internal.zzbn
    public final void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) throws RemoteException {
        this.zzbmN.onStreetViewPanoramaReady(new StreetViewPanorama(iStreetViewPanoramaDelegate));
    }
}
