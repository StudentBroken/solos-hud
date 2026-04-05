package com.google.android.gms.maps.model;

import com.google.android.gms.maps.model.internal.zzaa;

/* JADX INFO: loaded from: classes10.dex */
final class zzs extends zzaa {
    private /* synthetic */ TileProvider zzboh;

    zzs(TileOverlayOptions tileOverlayOptions, TileProvider tileProvider) {
        this.zzboh = tileProvider;
    }

    @Override // com.google.android.gms.maps.model.internal.zzz
    public final Tile getTile(int i, int i2, int i3) {
        return this.zzboh.getTile(i, i2, i3);
    }
}
