package com.google.android.gms.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbda<O extends Api.ApiOptions> extends GoogleApi<O> {
    private final Api.zza<? extends zzcuw, zzcux> zzaBg;
    private final Api.zze zzaCA;
    private final zzbcu zzaCB;
    private final com.google.android.gms.common.internal.zzq zzaCC;

    public zzbda(@NonNull Context context, Api<O> api, Looper looper, @NonNull Api.zze zzeVar, @NonNull zzbcu zzbcuVar, com.google.android.gms.common.internal.zzq zzqVar, Api.zza<? extends zzcuw, zzcux> zzaVar) {
        super(context, api, looper);
        this.zzaCA = zzeVar;
        this.zzaCB = zzbcuVar;
        this.zzaCC = zzqVar;
        this.zzaBg = zzaVar;
        this.zzaAP.zzb(this);
    }

    @Override // com.google.android.gms.common.api.GoogleApi
    public final Api.zze zza(Looper looper, zzbep<O> zzbepVar) {
        this.zzaCB.zza(zzbepVar);
        return this.zzaCA;
    }

    @Override // com.google.android.gms.common.api.GoogleApi
    public final zzbfv zza(Context context, Handler handler) {
        return new zzbfv(context, handler, this.zzaCC, this.zzaBg);
    }

    public final Api.zze zzpH() {
        return this.zzaCA;
    }
}
