package com.google.android.gms.internal;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfz extends zzbcq<Status> {
    @Deprecated
    public zzbfz(Looper looper) {
        super(looper);
    }

    public zzbfz(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
