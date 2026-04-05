package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbev<O extends Api.ApiOptions> extends zzbdl {
    private final GoogleApi<O> zzaEB;

    public zzbev(GoogleApi<O> googleApi) {
        super("Method is not supported by connectionless client. APIs supporting connectionless client must not call this method.");
        this.zzaEB = googleApi;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Context getContext() {
        return this.zzaEB.getApplicationContext();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Looper getLooper() {
        return this.zzaEB.getLooper();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void zza(zzbge zzbgeVar) {
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void zzb(zzbge zzbgeVar) {
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        return (T) this.zzaEB.zza(t);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        return (T) this.zzaEB.zzb(t);
    }
}
