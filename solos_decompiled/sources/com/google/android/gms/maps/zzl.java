package com.google.android.gms.maps;

import com.google.android.gms.maps.internal.ILocationSourceDelegate;

/* JADX INFO: loaded from: classes10.dex */
final class zzl extends ILocationSourceDelegate.zza {
    private /* synthetic */ LocationSource zzblO;

    zzl(GoogleMap googleMap, LocationSource locationSource) {
        this.zzblO = locationSource;
    }

    @Override // com.google.android.gms.maps.internal.ILocationSourceDelegate
    public final void activate(com.google.android.gms.maps.internal.zzah zzahVar) {
        this.zzblO.activate(new zzm(this, zzahVar));
    }

    @Override // com.google.android.gms.maps.internal.ILocationSourceDelegate
    public final void deactivate() {
        this.zzblO.deactivate();
    }
}
