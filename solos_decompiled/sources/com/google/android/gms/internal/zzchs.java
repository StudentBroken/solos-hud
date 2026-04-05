package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.internal.zzbr;
import java.lang.Thread;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchs extends zzciv {
    private static final AtomicLong zzbsj = new AtomicLong(Long.MIN_VALUE);
    private ExecutorService zzbrZ;
    private zzchw zzbsa;
    private zzchw zzbsb;
    private final PriorityBlockingQueue<FutureTask<?>> zzbsc;
    private final BlockingQueue<FutureTask<?>> zzbsd;
    private final Thread.UncaughtExceptionHandler zzbse;
    private final Thread.UncaughtExceptionHandler zzbsf;
    private final Object zzbsg;
    private final Semaphore zzbsh;
    private volatile boolean zzbsi;

    zzchs(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbsg = new Object();
        this.zzbsh = new Semaphore(2);
        this.zzbsc = new PriorityBlockingQueue<>();
        this.zzbsd = new LinkedBlockingQueue();
        this.zzbse = new zzchu(this, "Thread death: Uncaught exception on worker thread");
        this.zzbsf = new zzchu(this, "Thread death: Uncaught exception on network thread");
    }

    public static boolean zzR() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    static /* synthetic */ zzchw zza(zzchs zzchsVar, zzchw zzchwVar) {
        zzchsVar.zzbsa = null;
        return null;
    }

    private final void zza(zzchv<?> zzchvVar) {
        synchronized (this.zzbsg) {
            this.zzbsc.add(zzchvVar);
            if (this.zzbsa == null) {
                this.zzbsa = new zzchw(this, "Measurement Worker", this.zzbsc);
                this.zzbsa.setUncaughtExceptionHandler(this.zzbse);
                this.zzbsa.start();
            } else {
                this.zzbsa.zzfE();
            }
        }
    }

    static /* synthetic */ zzchw zzb(zzchs zzchsVar, zzchw zzchwVar) {
        zzchsVar.zzbsb = null;
        return null;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final <V> Future<V> zze(Callable<V> callable) throws IllegalStateException {
        zzkC();
        zzbr.zzu(callable);
        zzchv<?> zzchvVar = new zzchv<>(this, (Callable<?>) callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzbsa) {
            if (!this.zzbsc.isEmpty()) {
                super.zzwE().zzyx().log("Callable skipped the worker queue.");
            }
            zzchvVar.run();
        } else {
            zza(zzchvVar);
        }
        return zzchvVar;
    }

    public final <V> Future<V> zzf(Callable<V> callable) throws IllegalStateException {
        zzkC();
        zzbr.zzu(callable);
        zzchv<?> zzchvVar = new zzchv<>(this, (Callable<?>) callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzbsa) {
            zzchvVar.run();
        } else {
            zza(zzchvVar);
        }
        return zzchvVar;
    }

    public final void zzj(Runnable runnable) throws IllegalStateException {
        zzkC();
        zzbr.zzu(runnable);
        zza(new zzchv<>(this, runnable, false, "Task exception on worker thread"));
    }

    @Override // com.google.android.gms.internal.zzciu
    public final void zzjB() {
        if (Thread.currentThread() != this.zzbsa) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    public final void zzk(Runnable runnable) throws IllegalStateException {
        zzkC();
        zzbr.zzu(runnable);
        zzchv zzchvVar = new zzchv(this, runnable, false, "Task exception on network thread");
        synchronized (this.zzbsg) {
            this.zzbsd.add(zzchvVar);
            if (this.zzbsb == null) {
                this.zzbsb = new zzchw(this, "Measurement Network", this.zzbsd);
                this.zzbsb.setUncaughtExceptionHandler(this.zzbsf);
                this.zzbsb.start();
            } else {
                this.zzbsb.zzfE();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final void zzwp() {
        if (Thread.currentThread() != this.zzbsb) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }

    public final boolean zzyK() {
        return Thread.currentThread() == this.zzbsa;
    }

    final ExecutorService zzyL() {
        ExecutorService executorService;
        synchronized (this.zzbsg) {
            if (this.zzbrZ == null) {
                this.zzbrZ = new ThreadPoolExecutor(0, 1, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue(100));
            }
            executorService = this.zzbrZ;
        }
        return executorService;
    }
}
