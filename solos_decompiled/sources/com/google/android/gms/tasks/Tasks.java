package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes56.dex */
public final class Tasks {

    static final class zza implements zzb {
        private final CountDownLatch zztM;

        private zza() {
            this.zztM = new CountDownLatch(1);
        }

        /* synthetic */ zza(zzo zzoVar) {
            this();
        }

        public final void await() throws InterruptedException {
            this.zztM.await();
        }

        public final boolean await(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.zztM.await(j, timeUnit);
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public final void onFailure(@NonNull Exception exc) {
            this.zztM.countDown();
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public final void onSuccess(Object obj) {
            this.zztM.countDown();
        }
    }

    interface zzb extends OnFailureListener, OnSuccessListener<Object> {
    }

    static final class zzc implements zzb {
        private final Object mLock = new Object();
        private final zzn<Void> zzbMg;
        private Exception zzbMl;
        private final int zzbMn;
        private int zzbMo;
        private int zzbMp;

        public zzc(int i, zzn<Void> zznVar) {
            this.zzbMn = i;
            this.zzbMg = zznVar;
        }

        private final void zzDG() {
            if (this.zzbMo + this.zzbMp == this.zzbMn) {
                if (this.zzbMl == null) {
                    this.zzbMg.setResult(null);
                    return;
                }
                zzn<Void> zznVar = this.zzbMg;
                int i = this.zzbMp;
                zznVar.setException(new ExecutionException(new StringBuilder(54).append(i).append(" out of ").append(this.zzbMn).append(" underlying tasks failed").toString(), this.zzbMl));
            }
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public final void onFailure(@NonNull Exception exc) {
            synchronized (this.mLock) {
                this.zzbMp++;
                this.zzbMl = exc;
                zzDG();
            }
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public final void onSuccess(Object obj) {
            synchronized (this.mLock) {
                this.zzbMo++;
                zzDG();
            }
        }
    }

    private Tasks() {
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task) throws ExecutionException, InterruptedException {
        zzbr.zzcG("Must not be called on the main application thread");
        zzbr.zzb(task, "Task must not be null");
        if (task.isComplete()) {
            return (TResult) zzb(task);
        }
        zza zzaVar = new zza(null);
        zza(task, zzaVar);
        zzaVar.await();
        return (TResult) zzb(task);
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task, long j, @NonNull TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        zzbr.zzcG("Must not be called on the main application thread");
        zzbr.zzb(task, "Task must not be null");
        zzbr.zzb(timeUnit, "TimeUnit must not be null");
        if (task.isComplete()) {
            return (TResult) zzb(task);
        }
        zza zzaVar = new zza(null);
        zza(task, zzaVar);
        if (zzaVar.await(j, timeUnit)) {
            return (TResult) zzb(task);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }

    public static <TResult> Task<TResult> call(@NonNull Callable<TResult> callable) {
        return call(TaskExecutors.MAIN_THREAD, callable);
    }

    public static <TResult> Task<TResult> call(@NonNull Executor executor, @NonNull Callable<TResult> callable) {
        zzbr.zzb(executor, "Executor must not be null");
        zzbr.zzb(callable, "Callback must not be null");
        zzn zznVar = new zzn();
        executor.execute(new zzo(zznVar, callable));
        return zznVar;
    }

    public static <TResult> Task<TResult> forException(@NonNull Exception exc) {
        zzn zznVar = new zzn();
        zznVar.setException(exc);
        return zznVar;
    }

    public static <TResult> Task<TResult> forResult(TResult tresult) {
        zzn zznVar = new zzn();
        zznVar.setResult(tresult);
        return zznVar;
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> collection) {
        if (collection.isEmpty()) {
            return forResult(null);
        }
        Iterator<? extends Task<?>> it = collection.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                throw new NullPointerException("null tasks are not accepted");
            }
        }
        zzn zznVar = new zzn();
        zzc zzcVar = new zzc(collection.size(), zznVar);
        Iterator<? extends Task<?>> it2 = collection.iterator();
        while (it2.hasNext()) {
            zza(it2.next(), zzcVar);
        }
        return zznVar;
    }

    public static Task<Void> whenAll(Task<?>... taskArr) {
        return taskArr.length == 0 ? forResult(null) : whenAll(Arrays.asList(taskArr));
    }

    private static void zza(Task<?> task, zzb zzbVar) {
        task.addOnSuccessListener(TaskExecutors.zzbMh, zzbVar);
        task.addOnFailureListener(TaskExecutors.zzbMh, zzbVar);
    }

    private static <TResult> TResult zzb(Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        throw new ExecutionException(task.getException());
    }
}
