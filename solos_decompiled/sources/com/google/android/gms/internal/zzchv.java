package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes36.dex */
final class zzchv<V> extends FutureTask<V> implements Comparable<zzchv> {
    private final String zzbsk;
    private /* synthetic */ zzchs zzbsl;
    private final long zzbsm;
    private final boolean zzbsn;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzchv(zzchs zzchsVar, Runnable runnable, boolean z, String str) {
        super(runnable, null);
        this.zzbsl = zzchsVar;
        zzbr.zzu(str);
        this.zzbsm = zzchs.zzbsj.getAndIncrement();
        this.zzbsk = str;
        this.zzbsn = false;
        if (this.zzbsm == Long.MAX_VALUE) {
            zzchsVar.zzwE().zzyv().log("Tasks index overflow");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzchv(zzchs zzchsVar, Callable<V> callable, boolean z, String str) {
        super(callable);
        this.zzbsl = zzchsVar;
        zzbr.zzu(str);
        this.zzbsm = zzchs.zzbsj.getAndIncrement();
        this.zzbsk = str;
        this.zzbsn = z;
        if (this.zzbsm == Long.MAX_VALUE) {
            zzchsVar.zzwE().zzyv().log("Tasks index overflow");
        }
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(@NonNull zzchv zzchvVar) {
        zzchv zzchvVar2 = zzchvVar;
        if (this.zzbsn != zzchvVar2.zzbsn) {
            return this.zzbsn ? -1 : 1;
        }
        if (this.zzbsm < zzchvVar2.zzbsm) {
            return -1;
        }
        if (this.zzbsm > zzchvVar2.zzbsm) {
            return 1;
        }
        this.zzbsl.zzwE().zzyw().zzj("Two tasks share the same index. index", Long.valueOf(this.zzbsm));
        return 0;
    }

    @Override // java.util.concurrent.FutureTask
    protected final void setException(Throwable th) {
        this.zzbsl.zzwE().zzyv().zzj(this.zzbsk, th);
        if (th instanceof zzcht) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }
}
