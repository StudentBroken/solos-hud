package com.google.android.gms.internal;

import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzbr;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbcq<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> zzaBX = new zzbcr();
    private Status mStatus;
    private boolean zzJ;
    private final Object zzaBY;
    private zzbcs<R> zzaBZ;
    private R zzaBl;
    private WeakReference<GoogleApiClient> zzaCa;
    private final ArrayList<PendingResult.zza> zzaCb;
    private ResultCallback<? super R> zzaCc;
    private final AtomicReference<zzbgj> zzaCd;
    private zzbct zzaCe;
    private volatile boolean zzaCf;
    private boolean zzaCg;
    private com.google.android.gms.common.internal.zzap zzaCh;
    private volatile zzbge<R> zzaCi;
    private boolean zzaCj;
    private final CountDownLatch zztM;

    @Deprecated
    zzbcq() {
        this.zzaBY = new Object();
        this.zztM = new CountDownLatch(1);
        this.zzaCb = new ArrayList<>();
        this.zzaCd = new AtomicReference<>();
        this.zzaCj = false;
        this.zzaBZ = new zzbcs<>(Looper.getMainLooper());
        this.zzaCa = new WeakReference<>(null);
    }

    @Deprecated
    protected zzbcq(Looper looper) {
        this.zzaBY = new Object();
        this.zztM = new CountDownLatch(1);
        this.zzaCb = new ArrayList<>();
        this.zzaCd = new AtomicReference<>();
        this.zzaCj = false;
        this.zzaBZ = new zzbcs<>(looper);
        this.zzaCa = new WeakReference<>(null);
    }

    protected zzbcq(GoogleApiClient googleApiClient) {
        this.zzaBY = new Object();
        this.zztM = new CountDownLatch(1);
        this.zzaCb = new ArrayList<>();
        this.zzaCd = new AtomicReference<>();
        this.zzaCj = false;
        this.zzaBZ = new zzbcs<>(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zzaCa = new WeakReference<>(googleApiClient);
    }

    private final R get() {
        R r;
        synchronized (this.zzaBY) {
            zzbr.zza(this.zzaCf ? false : true, "Result has already been consumed.");
            zzbr.zza(isReady(), "Result is not ready.");
            r = this.zzaBl;
            this.zzaBl = null;
            this.zzaCc = null;
            this.zzaCf = true;
        }
        zzbgj andSet = this.zzaCd.getAndSet(null);
        if (andSet != null) {
            andSet.zzc(this);
        }
        return r;
    }

    private final void zzb(R r) {
        zzbcr zzbcrVar = null;
        this.zzaBl = r;
        this.zzaCh = null;
        this.zztM.countDown();
        this.mStatus = this.zzaBl.getStatus();
        if (this.zzJ) {
            this.zzaCc = null;
        } else if (this.zzaCc != null) {
            this.zzaBZ.removeMessages(2);
            this.zzaBZ.zza(this.zzaCc, get());
        } else if (this.zzaBl instanceof Releasable) {
            this.zzaCe = new zzbct(this, zzbcrVar);
        }
        ArrayList<PendingResult.zza> arrayList = this.zzaCb;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            PendingResult.zza zzaVar = arrayList.get(i);
            i++;
            zzaVar.zzo(this.mStatus);
        }
        this.zzaCb.clear();
    }

    public static void zzc(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String strValueOf = String.valueOf(result);
                Log.w("BasePendingResult", new StringBuilder(String.valueOf(strValueOf).length() + 18).append("Unable to release ").append(strValueOf).toString(), e);
            }
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final R await() {
        zzbr.zza(Looper.myLooper() != Looper.getMainLooper(), "await must not be called on the UI thread");
        zzbr.zza(!this.zzaCf, "Result has already been consumed");
        zzbr.zza(this.zzaCi == null, "Cannot await if then() has been called.");
        try {
            this.zztM.await();
        } catch (InterruptedException e) {
            zzs(Status.zzaBp);
        }
        zzbr.zza(isReady(), "Result is not ready.");
        return (R) get();
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final R await(long j, TimeUnit timeUnit) {
        zzbr.zza(j <= 0 || Looper.myLooper() != Looper.getMainLooper(), "await must not be called on the UI thread when time is greater than zero.");
        zzbr.zza(!this.zzaCf, "Result has already been consumed.");
        zzbr.zza(this.zzaCi == null, "Cannot await if then() has been called.");
        try {
            if (!this.zztM.await(j, timeUnit)) {
                zzs(Status.zzaBr);
            }
        } catch (InterruptedException e) {
            zzs(Status.zzaBp);
        }
        zzbr.zza(isReady(), "Result is not ready.");
        return (R) get();
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public void cancel() {
        synchronized (this.zzaBY) {
            if (this.zzJ || this.zzaCf) {
                return;
            }
            if (this.zzaCh != null) {
                try {
                    this.zzaCh.cancel();
                } catch (RemoteException e) {
                }
            }
            zzc(this.zzaBl);
            this.zzJ = true;
            zzb(zzb(Status.zzaBs));
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzaBY) {
            z = this.zzJ;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zztM.getCount() == 0;
    }

    public final void setResult(R r) {
        synchronized (this.zzaBY) {
            if (this.zzaCg || this.zzJ) {
                zzc(r);
                return;
            }
            if (isReady()) {
            }
            zzbr.zza(!isReady(), "Results have already been set");
            zzbr.zza(this.zzaCf ? false : true, "Result has already been consumed");
            zzb(r);
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final void setResultCallback(ResultCallback<? super R> resultCallback) {
        synchronized (this.zzaBY) {
            if (resultCallback == null) {
                this.zzaCc = null;
                return;
            }
            zzbr.zza(!this.zzaCf, "Result has already been consumed.");
            zzbr.zza(this.zzaCi == null, "Cannot set callbacks if then() has been called.");
            if (isCanceled()) {
                return;
            }
            if (isReady()) {
                this.zzaBZ.zza(resultCallback, get());
            } else {
                this.zzaCc = resultCallback;
            }
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final void setResultCallback(ResultCallback<? super R> resultCallback, long j, TimeUnit timeUnit) {
        synchronized (this.zzaBY) {
            if (resultCallback == null) {
                this.zzaCc = null;
                return;
            }
            zzbr.zza(!this.zzaCf, "Result has already been consumed.");
            zzbr.zza(this.zzaCi == null, "Cannot set callbacks if then() has been called.");
            if (isCanceled()) {
                return;
            }
            if (isReady()) {
                this.zzaBZ.zza(resultCallback, get());
            } else {
                this.zzaCc = resultCallback;
                zzbcs<R> zzbcsVar = this.zzaBZ;
                zzbcsVar.sendMessageDelayed(zzbcsVar.obtainMessage(2, this), timeUnit.toMillis(j));
            }
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        TransformedResult<S> transformedResultThen;
        zzbr.zza(!this.zzaCf, "Result has already been consumed.");
        synchronized (this.zzaBY) {
            zzbr.zza(this.zzaCi == null, "Cannot call then() twice.");
            zzbr.zza(this.zzaCc == null, "Cannot call then() if callbacks are set.");
            zzbr.zza(this.zzJ ? false : true, "Cannot call then() if result was canceled.");
            this.zzaCj = true;
            this.zzaCi = new zzbge<>(this.zzaCa);
            transformedResultThen = this.zzaCi.then(resultTransform);
            if (isReady()) {
                this.zzaBZ.zza(this.zzaCi, get());
            } else {
                this.zzaCc = this.zzaCi;
            }
        }
        return transformedResultThen;
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final void zza(PendingResult.zza zzaVar) {
        zzbr.zzb(zzaVar != null, "Callback cannot be null.");
        synchronized (this.zzaBY) {
            if (isReady()) {
                zzaVar.zzo(this.mStatus);
            } else {
                this.zzaCb.add(zzaVar);
            }
        }
    }

    protected final void zza(com.google.android.gms.common.internal.zzap zzapVar) {
        synchronized (this.zzaBY) {
            this.zzaCh = zzapVar;
        }
    }

    public final void zza(zzbgj zzbgjVar) {
        this.zzaCd.set(zzbgjVar);
    }

    @NonNull
    protected abstract R zzb(Status status);

    public final void zzpA() {
        this.zzaCj = this.zzaCj || zzaBX.get().booleanValue();
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final Integer zzpm() {
        return null;
    }

    public final boolean zzpz() {
        boolean zIsCanceled;
        synchronized (this.zzaBY) {
            if (this.zzaCa.get() == null || !this.zzaCj) {
                cancel();
            }
            zIsCanceled = isCanceled();
        }
        return zIsCanceled;
    }

    public final void zzs(Status status) {
        synchronized (this.zzaBY) {
            if (!isReady()) {
                setResult(zzb(status));
                this.zzaCg = true;
            }
        }
    }
}
