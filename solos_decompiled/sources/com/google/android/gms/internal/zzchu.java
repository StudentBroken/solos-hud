package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;
import java.lang.Thread;

/* JADX INFO: loaded from: classes36.dex */
final class zzchu implements Thread.UncaughtExceptionHandler {
    private final String zzbsk;
    private /* synthetic */ zzchs zzbsl;

    public zzchu(zzchs zzchsVar, String str) {
        this.zzbsl = zzchsVar;
        zzbr.zzu(str);
        this.zzbsk = str;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zzbsl.zzwE().zzyv().zzj(this.zzbsk, th);
    }
}
