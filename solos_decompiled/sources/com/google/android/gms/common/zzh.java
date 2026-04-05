package com.google.android.gms.common;

import java.util.Arrays;

/* JADX INFO: loaded from: classes67.dex */
final class zzh extends zzg {
    private final byte[] zzaAj;

    zzh(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zzaAj = bArr;
    }

    @Override // com.google.android.gms.common.zzg
    final byte[] getBytes() {
        return this.zzaAj;
    }
}
