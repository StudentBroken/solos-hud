package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;
import java.net.URL;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
@WorkerThread
final class zzchf implements Runnable {
    private final String mPackageName;
    private final URL zzJx;
    private final byte[] zzaKE;
    private final zzchd zzbrh;
    private final Map<String, String> zzbri;
    private /* synthetic */ zzchb zzbrj;

    public zzchf(zzchb zzchbVar, String str, URL url, byte[] bArr, Map<String, String> map, zzchd zzchdVar) {
        this.zzbrj = zzchbVar;
        zzbr.zzcF(str);
        zzbr.zzu(url);
        zzbr.zzu(zzchdVar);
        this.zzJx = url;
        this.zzaKE = bArr;
        this.zzbrh = zzchdVar;
        this.mPackageName = str;
        this.zzbri = map;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0020 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0114 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 356
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchf.run():void");
    }
}
