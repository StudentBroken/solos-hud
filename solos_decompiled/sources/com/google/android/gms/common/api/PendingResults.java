package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbcq;
import com.google.android.gms.internal.zzbfo;
import com.google.android.gms.internal.zzbfz;

/* JADX INFO: loaded from: classes3.dex */
public final class PendingResults {

    static final class zza<R extends Result> extends zzbcq<R> {
        private final R zzaBk;

        public zza(R r) {
            super(Looper.getMainLooper());
            this.zzaBk = r;
        }

        @Override // com.google.android.gms.internal.zzbcq
        protected final R zzb(Status status) {
            if (status.getStatusCode() != this.zzaBk.getStatus().getStatusCode()) {
                throw new UnsupportedOperationException("Creating failed results is not supported");
            }
            return this.zzaBk;
        }
    }

    static final class zzb<R extends Result> extends zzbcq<R> {
        private final R zzaBl;

        public zzb(GoogleApiClient googleApiClient, R r) {
            super(googleApiClient);
            this.zzaBl = r;
        }

        @Override // com.google.android.gms.internal.zzbcq
        protected final R zzb(Status status) {
            return this.zzaBl;
        }
    }

    static final class zzc<R extends Result> extends zzbcq<R> {
        public zzc(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        @Override // com.google.android.gms.internal.zzbcq
        protected final R zzb(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private PendingResults() {
    }

    public static PendingResult<Status> canceledPendingResult() {
        zzbfz zzbfzVar = new zzbfz(Looper.getMainLooper());
        zzbfzVar.cancel();
        return zzbfzVar;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        zzbr.zzb(r, "Result must not be null");
        zzbr.zzb(r.getStatus().getStatusCode() == 16, "Status code must be CommonStatusCodes.CANCELED");
        zza zzaVar = new zza(r);
        zzaVar.cancel();
        return zzaVar;
    }

    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r) {
        zzbr.zzb(r, "Result must not be null");
        zzc zzcVar = new zzc(null);
        zzcVar.setResult(r);
        return new zzbfo(zzcVar);
    }

    public static PendingResult<Status> immediatePendingResult(Status status) {
        zzbr.zzb(status, "Result must not be null");
        zzbfz zzbfzVar = new zzbfz(Looper.getMainLooper());
        zzbfzVar.setResult(status);
        return zzbfzVar;
    }

    public static <R extends Result> PendingResult<R> zza(R r, GoogleApiClient googleApiClient) {
        zzbr.zzb(r, "Result must not be null");
        zzbr.zzb(!r.getStatus().isSuccess(), "Status code must not be SUCCESS");
        zzb zzbVar = new zzb(googleApiClient, r);
        zzbVar.setResult(r);
        return zzbVar;
    }

    public static PendingResult<Status> zza(Status status, GoogleApiClient googleApiClient) {
        zzbr.zzb(status, "Result must not be null");
        zzbfz zzbfzVar = new zzbfz(googleApiClient);
        zzbfzVar.setResult(status);
        return zzbfzVar;
    }

    public static <R extends Result> OptionalPendingResult<R> zzb(R r, GoogleApiClient googleApiClient) {
        zzbr.zzb(r, "Result must not be null");
        zzc zzcVar = new zzc(googleApiClient);
        zzcVar.setResult(r);
        return new zzbfo(zzcVar);
    }
}
