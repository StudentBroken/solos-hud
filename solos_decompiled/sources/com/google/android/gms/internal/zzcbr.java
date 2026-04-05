package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbr {
    private static zzcbr zzaXL;
    private final zzcbm zzaXM = new zzcbm();
    private final zzcbn zzaXN = new zzcbn();

    static {
        zzcbr zzcbrVar = new zzcbr();
        synchronized (zzcbr.class) {
            zzaXL = zzcbrVar;
        }
    }

    private zzcbr() {
    }

    private static zzcbr zztZ() {
        zzcbr zzcbrVar;
        synchronized (zzcbr.class) {
            zzcbrVar = zzaXL;
        }
        return zzcbrVar;
    }

    public static zzcbm zzua() {
        return zztZ().zzaXM;
    }

    public static zzcbn zzub() {
        return zztZ().zzaXN;
    }
}
