package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes6.dex */
final class zzas implements Channel.GetInputStreamResult {
    private final Status mStatus;
    private final InputStream zzbSq;

    zzas(Status status, InputStream inputStream) {
        this.mStatus = (Status) com.google.android.gms.common.internal.zzbr.zzu(status);
        this.zzbSq = inputStream;
    }

    @Override // com.google.android.gms.wearable.Channel.GetInputStreamResult
    public final InputStream getInputStream() {
        return this.zzbSq;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }

    @Override // com.google.android.gms.common.api.Releasable
    public final void release() {
        if (this.zzbSq != null) {
            try {
                this.zzbSq.close();
            } catch (IOException e) {
            }
        }
    }
}
