package com.google.android.gms.internal;

import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
final class zzcia implements zzchd {
    private /* synthetic */ zzchx zzbtc;

    zzcia(zzchx zzchxVar) {
        this.zzbtc = zzchxVar;
    }

    @Override // com.google.android.gms.internal.zzchd
    public final void zza(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.zzbtc.zza(i, th, bArr);
    }
}
