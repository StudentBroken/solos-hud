package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
@WorkerThread
final class zzche implements Runnable {
    private final String mPackageName;
    private final int zzLj;
    private final Throwable zzaaU;
    private final zzchd zzbre;
    private final byte[] zzbrf;
    private final Map<String, List<String>> zzbrg;

    private zzche(String str, zzchd zzchdVar, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        zzbr.zzu(zzchdVar);
        this.zzbre = zzchdVar;
        this.zzLj = i;
        this.zzaaU = th;
        this.zzbrf = bArr;
        this.mPackageName = str;
        this.zzbrg = map;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbre.zza(this.mPackageName, this.zzLj, this.zzaaU, this.zzbrf, this.zzbrg);
    }
}
