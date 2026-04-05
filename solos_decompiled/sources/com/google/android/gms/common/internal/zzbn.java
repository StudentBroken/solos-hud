package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;

/* JADX INFO: Add missing generic type declarations: [R, T] */
/* JADX INFO: loaded from: classes3.dex */
final class zzbn<R, T> implements zzbp<R, T> {
    private /* synthetic */ Response zzaIp;

    zzbn(Response response) {
        this.zzaIp = response;
    }

    @Override // com.google.android.gms.common.internal.zzbp
    public final /* synthetic */ Object zzd(Result result) {
        this.zzaIp.setResult(result);
        return this.zzaIp;
    }
}
