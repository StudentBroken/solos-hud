package com.google.android.gms.tasks;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbfe;
import com.google.android.gms.internal.zzbff;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zzn<TResult> extends Task<TResult> {
    private final Object mLock = new Object();
    private final zzl<TResult> zzbMi = new zzl<>();
    private boolean zzbMj;
    private TResult zzbMk;
    private Exception zzbMl;

    static class zza extends zzbfe {
        private final List<WeakReference<zzk<?>>> mListeners;

        private zza(zzbff zzbffVar) {
            super(zzbffVar);
            this.mListeners = new ArrayList();
            this.zzaEI.zza("TaskOnStopCallback", this);
        }

        public static zza zzr(Activity activity) {
            zzbff zzbffVarZzn = zzn(activity);
            zza zzaVar = (zza) zzbffVarZzn.zza("TaskOnStopCallback", zza.class);
            return zzaVar == null ? new zza(zzbffVarZzn) : zzaVar;
        }

        @Override // com.google.android.gms.internal.zzbfe
        @MainThread
        public final void onStop() {
            synchronized (this.mListeners) {
                Iterator<WeakReference<zzk<?>>> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    zzk<?> zzkVar = it.next().get();
                    if (zzkVar != null) {
                        zzkVar.cancel();
                    }
                }
                this.mListeners.clear();
            }
        }

        public final <T> void zzb(zzk<T> zzkVar) {
            synchronized (this.mListeners) {
                this.mListeners.add(new WeakReference<>(zzkVar));
            }
        }
    }

    zzn() {
    }

    private final void zzDD() {
        zzbr.zza(this.zzbMj, "Task is not yet complete");
    }

    private final void zzDE() {
        zzbr.zza(!this.zzbMj, "Task is already complete");
    }

    private final void zzDF() {
        synchronized (this.mLock) {
            if (this.zzbMj) {
                this.zzbMi.zza(this);
            }
        }
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        zze zzeVar = new zze(TaskExecutors.MAIN_THREAD, onCompleteListener);
        this.zzbMi.zza(zzeVar);
        zza.zzr(activity).zzb(zzeVar);
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, onCompleteListener);
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        this.zzbMi.zza(new zze(executor, onCompleteListener));
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        zzg zzgVar = new zzg(TaskExecutors.MAIN_THREAD, onFailureListener);
        this.zzbMi.zza(zzgVar);
        zza.zzr(activity).zzb(zzgVar);
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return addOnFailureListener(TaskExecutors.MAIN_THREAD, onFailureListener);
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzbMi.zza(new zzg(executor, onFailureListener));
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzi zziVar = new zzi(TaskExecutors.MAIN_THREAD, onSuccessListener);
        this.zzbMi.zza(zziVar);
        zza.zzr(activity).zzb(zziVar);
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        return addOnSuccessListener(TaskExecutors.MAIN_THREAD, onSuccessListener);
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzbMi.zza(new zzi(executor, onSuccessListener));
        zzDF();
        return this;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Continuation<TResult, TContinuationResult> continuation) {
        return continueWith(TaskExecutors.MAIN_THREAD, continuation);
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        zzn zznVar = new zzn();
        this.zzbMi.zza(new com.google.android.gms.tasks.zza(executor, continuation, zznVar));
        zzDF();
        return zznVar;
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return continueWithTask(TaskExecutors.MAIN_THREAD, continuation);
    }

    @Override // com.google.android.gms.tasks.Task
    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        zzn zznVar = new zzn();
        this.zzbMi.zza(new zzc(executor, continuation, zznVar));
        zzDF();
        return zznVar;
    }

    @Override // com.google.android.gms.tasks.Task
    @Nullable
    public final Exception getException() {
        Exception exc;
        synchronized (this.mLock) {
            exc = this.zzbMl;
        }
        return exc;
    }

    @Override // com.google.android.gms.tasks.Task
    public final TResult getResult() {
        TResult tresult;
        synchronized (this.mLock) {
            zzDD();
            if (this.zzbMl != null) {
                throw new RuntimeExecutionException(this.zzbMl);
            }
            tresult = this.zzbMk;
        }
        return tresult;
    }

    @Override // com.google.android.gms.tasks.Task
    public final <X extends Throwable> TResult getResult(@NonNull Class<X> cls) throws Throwable {
        TResult tresult;
        synchronized (this.mLock) {
            zzDD();
            if (cls.isInstance(this.zzbMl)) {
                throw cls.cast(this.zzbMl);
            }
            if (this.zzbMl != null) {
                throw new RuntimeExecutionException(this.zzbMl);
            }
            tresult = this.zzbMk;
        }
        return tresult;
    }

    @Override // com.google.android.gms.tasks.Task
    public final boolean isComplete() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzbMj;
        }
        return z;
    }

    @Override // com.google.android.gms.tasks.Task
    public final boolean isSuccessful() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzbMj && this.zzbMl == null;
        }
        return z;
    }

    public final void setException(@NonNull Exception exc) {
        zzbr.zzb(exc, "Exception must not be null");
        synchronized (this.mLock) {
            zzDE();
            this.zzbMj = true;
            this.zzbMl = exc;
        }
        this.zzbMi.zza(this);
    }

    public final void setResult(TResult tresult) {
        synchronized (this.mLock) {
            zzDE();
            this.zzbMj = true;
            this.zzbMk = tresult;
        }
        this.zzbMi.zza(this);
    }

    public final boolean trySetException(@NonNull Exception exc) {
        boolean z = true;
        zzbr.zzb(exc, "Exception must not be null");
        synchronized (this.mLock) {
            if (this.zzbMj) {
                z = false;
            } else {
                this.zzbMj = true;
                this.zzbMl = exc;
                this.zzbMi.zza(this);
            }
        }
        return z;
    }

    public final boolean trySetResult(TResult tresult) {
        boolean z = true;
        synchronized (this.mLock) {
            if (this.zzbMj) {
                z = false;
            } else {
                this.zzbMj = true;
                this.zzbMk = tresult;
                this.zzbMi.zza(this);
            }
        }
        return z;
    }
}
