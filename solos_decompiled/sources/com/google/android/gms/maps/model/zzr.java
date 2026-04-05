package com.google.android.gms.maps.model;

import android.os.RemoteException;
import com.google.android.gms.maps.model.internal.zzz;

/* JADX INFO: loaded from: classes10.dex */
final class zzr implements TileProvider {
    private final zzz zzbof;
    private /* synthetic */ TileOverlayOptions zzbog;

    zzr(TileOverlayOptions tileOverlayOptions) {
        this.zzbog = tileOverlayOptions;
        this.zzbof = this.zzbog.zzboc;
    }

    @Override // com.google.android.gms.maps.model.TileProvider
    public final Tile getTile(int i, int i2, int i3) {
        try {
            return this.zzbof.getTile(i, i2, i3);
        } catch (RemoteException e) {
            return null;
        }
    }
}
