package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.ChannelIOException;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes6.dex */
public final class zzav extends InputStream {
    private final InputStream zzbSs;
    private volatile zzah zzbSt;

    public zzav(InputStream inputStream) {
        this.zzbSs = (InputStream) com.google.android.gms.common.internal.zzbr.zzu(inputStream);
    }

    private final int zzbQ(int i) throws ChannelIOException {
        zzah zzahVar;
        if (i != -1 || (zzahVar = this.zzbSt) == null) {
            return i;
        }
        throw new ChannelIOException("Channel closed unexpectedly before stream was finished", zzahVar.zzbSj, zzahVar.zzbSk);
    }

    @Override // java.io.InputStream
    public final int available() throws IOException {
        return this.zzbSs.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.zzbSs.close();
    }

    @Override // java.io.InputStream
    public final void mark(int i) {
        this.zzbSs.mark(i);
    }

    @Override // java.io.InputStream
    public final boolean markSupported() {
        return this.zzbSs.markSupported();
    }

    @Override // java.io.InputStream
    public final int read() throws IOException {
        return zzbQ(this.zzbSs.read());
    }

    @Override // java.io.InputStream
    public final int read(byte[] bArr) throws IOException {
        return zzbQ(this.zzbSs.read(bArr));
    }

    @Override // java.io.InputStream
    public final int read(byte[] bArr, int i, int i2) throws IOException {
        return zzbQ(this.zzbSs.read(bArr, i, i2));
    }

    @Override // java.io.InputStream
    public final void reset() throws IOException {
        this.zzbSs.reset();
    }

    @Override // java.io.InputStream
    public final long skip(long j) throws IOException {
        return this.zzbSs.skip(j);
    }

    final void zza(zzah zzahVar) {
        this.zzbSt = (zzah) com.google.android.gms.common.internal.zzbr.zzu(zzahVar);
    }
}
