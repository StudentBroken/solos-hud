package com.google.android.gms.internal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes67.dex */
final class zzaj extends FilterInputStream {
    private int zzaA;

    private zzaj(InputStream inputStream) {
        super(inputStream);
        this.zzaA = 0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read() throws IOException {
        int i = super.read();
        if (i != -1) {
            this.zzaA++;
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = super.read(bArr, i, i2);
        if (i3 != -1) {
            this.zzaA += i3;
        }
        return i3;
    }
}
