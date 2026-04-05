package com.google.android.gms.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes36.dex */
final class zzcji implements Callable<String> {
    private /* synthetic */ zzcix zzbtx;

    zzcji(zzcix zzcixVar) {
        this.zzbtx = zzcixVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ String call() throws Exception {
        String strZzyF = this.zzbtx.zzwF().zzyF();
        if (strZzyF == null) {
            strZzyF = this.zzbtx.zzws().zzac(120000L);
            if (strZzyF == null) {
                throw new TimeoutException();
            }
            this.zzbtx.zzwF().zzef(strZzyF);
        }
        return strZzyF;
    }
}
