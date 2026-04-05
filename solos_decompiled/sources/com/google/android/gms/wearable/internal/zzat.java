package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes6.dex */
final class zzat implements Channel.GetOutputStreamResult {
    private final Status mStatus;
    private final OutputStream zzbSr;

    zzat(Status status, OutputStream outputStream) {
        this.mStatus = (Status) com.google.android.gms.common.internal.zzbr.zzu(status);
        this.zzbSr = outputStream;
    }

    @Override // com.google.android.gms.wearable.Channel.GetOutputStreamResult
    public final OutputStream getOutputStream() {
        return this.zzbSr;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }

    @Override // com.google.android.gms.common.api.Releasable
    public final void release() {
        if (this.zzbSr != null) {
            try {
                this.zzbSr.close();
            } catch (IOException e) {
            }
        }
    }
}
