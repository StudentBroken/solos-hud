package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzaq;

/* JADX INFO: loaded from: classes10.dex */
final class zzaj extends zzaq {
    private /* synthetic */ OnMapReadyCallback zzbmv;

    zzaj(SupportMapFragment.zza zzaVar, OnMapReadyCallback onMapReadyCallback) {
        this.zzbmv = onMapReadyCallback;
    }

    @Override // com.google.android.gms.maps.internal.zzap
    public final void zza(IGoogleMapDelegate iGoogleMapDelegate) throws RemoteException {
        this.zzbmv.onMapReady(new GoogleMap(iGoogleMapDelegate));
    }
}
