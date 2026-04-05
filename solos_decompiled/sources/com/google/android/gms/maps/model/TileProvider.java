package com.google.android.gms.maps.model;

/* JADX INFO: loaded from: classes10.dex */
public interface TileProvider {
    public static final Tile NO_TILE = new Tile(-1, -1, null);

    Tile getTile(int i, int i2, int i3);
}
