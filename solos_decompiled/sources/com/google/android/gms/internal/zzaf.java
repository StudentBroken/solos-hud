package com.google.android.gms.internal;

import java.util.Comparator;

/* JADX INFO: loaded from: classes67.dex */
final class zzaf implements Comparator<byte[]> {
    zzaf() {
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(byte[] bArr, byte[] bArr2) {
        return bArr.length - bArr2.length;
    }
}
