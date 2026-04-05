package com.google.android.gms.maps.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/* JADX INFO: loaded from: classes10.dex */
public abstract class UrlTileProvider implements TileProvider {
    private final int zzrZ;
    private final int zzsa;

    public UrlTileProvider(int i, int i2) {
        this.zzrZ = i;
        this.zzsa = i2;
    }

    private static long zza(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                return j;
            }
            outputStream.write(bArr, 0, i);
            j += (long) i;
        }
    }

    @Override // com.google.android.gms.maps.model.TileProvider
    public final Tile getTile(int i, int i2, int i3) {
        URL tileUrl = getTileUrl(i, i2, i3);
        if (tileUrl == null) {
            return NO_TILE;
        }
        try {
            int i4 = this.zzrZ;
            int i5 = this.zzsa;
            InputStream inputStreamOpenStream = tileUrl.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            zza(inputStreamOpenStream, byteArrayOutputStream);
            return new Tile(i4, i5, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public abstract URL getTileUrl(int i, int i2, int i3);
}
