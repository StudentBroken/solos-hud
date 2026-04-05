package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzbck;
import com.google.android.gms.wearable.Wearable;

/* JADX INFO: loaded from: classes6.dex */
abstract class zzn<R extends Result> extends zzbck<R, zzfw> {
    public zzn(GoogleApiClient googleApiClient) {
        super(Wearable.API, googleApiClient);
    }

    @Override // com.google.android.gms.internal.zzbck, com.google.android.gms.internal.zzbcl
    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((Result) obj);
    }
}
