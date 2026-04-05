package com.google.android.gms.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzbr;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbge<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    private final WeakReference<GoogleApiClient> zzaCa;
    private final zzbgg zzaFh;
    private ResultTransform<? super R, ? extends Result> zzaFc = null;
    private zzbge<? extends Result> zzaFd = null;
    private volatile ResultCallbacks<? super R> zzaFe = null;
    private PendingResult<R> zzaFf = null;
    private final Object zzaBY = new Object();
    private Status zzaFg = null;
    private boolean zzaFi = false;

    public zzbge(WeakReference<GoogleApiClient> weakReference) {
        zzbr.zzb(weakReference, "GoogleApiClient reference must not be null");
        this.zzaCa = weakReference;
        GoogleApiClient googleApiClient = this.zzaCa.get();
        this.zzaFh = new zzbgg(this, googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void zzc(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String strValueOf = String.valueOf(result);
                Log.w("TransformedResultImpl", new StringBuilder(String.valueOf(strValueOf).length() + 18).append("Unable to release ").append(strValueOf).toString(), e);
            }
        }
    }

    private final void zzqH() {
        if (this.zzaFc == null && this.zzaFe == null) {
            return;
        }
        GoogleApiClient googleApiClient = this.zzaCa.get();
        if (!this.zzaFi && this.zzaFc != null && googleApiClient != null) {
            googleApiClient.zza(this);
            this.zzaFi = true;
        }
        if (this.zzaFg != null) {
            zzw(this.zzaFg);
        } else if (this.zzaFf != null) {
            this.zzaFf.setResultCallback(this);
        }
    }

    private final boolean zzqJ() {
        return (this.zzaFe == null || this.zzaCa.get() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzv(Status status) {
        synchronized (this.zzaBY) {
            this.zzaFg = status;
            zzw(this.zzaFg);
        }
    }

    private final void zzw(Status status) {
        synchronized (this.zzaBY) {
            if (this.zzaFc != null) {
                Status statusOnFailure = this.zzaFc.onFailure(status);
                zzbr.zzb(statusOnFailure, "onFailure must not return null");
                this.zzaFd.zzv(statusOnFailure);
            } else if (zzqJ()) {
                this.zzaFe.onFailure(status);
            }
        }
    }

    @Override // com.google.android.gms.common.api.TransformedResult
    public final void andFinally(@NonNull ResultCallbacks<? super R> resultCallbacks) {
        synchronized (this.zzaBY) {
            zzbr.zza(this.zzaFe == null, "Cannot call andFinally() twice.");
            zzbr.zza(this.zzaFc == null, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzaFe = resultCallbacks;
            zzqH();
        }
    }

    @Override // com.google.android.gms.common.api.ResultCallback
    public final void onResult(R r) {
        synchronized (this.zzaBY) {
            if (!r.getStatus().isSuccess()) {
                zzv(r.getStatus());
                zzc(r);
            } else if (this.zzaFc != null) {
                zzbfs.zzqh().submit(new zzbgf(this, r));
            } else if (zzqJ()) {
                this.zzaFe.onSuccess(r);
            }
        }
    }

    @Override // com.google.android.gms.common.api.TransformedResult
    @NonNull
    public final <S extends Result> TransformedResult<S> then(@NonNull ResultTransform<? super R, ? extends S> resultTransform) {
        zzbge<? extends Result> zzbgeVar;
        synchronized (this.zzaBY) {
            zzbr.zza(this.zzaFc == null, "Cannot call then() twice.");
            zzbr.zza(this.zzaFe == null, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzaFc = resultTransform;
            zzbgeVar = new zzbge<>(this.zzaCa);
            this.zzaFd = zzbgeVar;
            zzqH();
        }
        return zzbgeVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zza(PendingResult<?> pendingResult) {
        synchronized (this.zzaBY) {
            this.zzaFf = pendingResult;
            zzqH();
        }
    }

    final void zzqI() {
        this.zzaFe = null;
    }
}
