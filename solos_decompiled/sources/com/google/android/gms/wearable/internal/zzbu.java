package com.google.android.gms.wearable.internal;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbu implements DataApi.GetFdForAssetResult {
    private volatile boolean mClosed = false;
    private final Status mStatus;
    private volatile ParcelFileDescriptor zzbSF;
    private volatile InputStream zzbSq;

    public zzbu(Status status, ParcelFileDescriptor parcelFileDescriptor) {
        this.mStatus = status;
        this.zzbSF = parcelFileDescriptor;
    }

    @Override // com.google.android.gms.wearable.DataApi.GetFdForAssetResult
    public final ParcelFileDescriptor getFd() {
        if (this.mClosed) {
            throw new IllegalStateException("Cannot access the file descriptor after release().");
        }
        return this.zzbSF;
    }

    @Override // com.google.android.gms.wearable.DataApi.GetFdForAssetResult
    public final InputStream getInputStream() {
        if (this.mClosed) {
            throw new IllegalStateException("Cannot access the input stream after release().");
        }
        if (this.zzbSF == null) {
            return null;
        }
        if (this.zzbSq == null) {
            this.zzbSq = new ParcelFileDescriptor.AutoCloseInputStream(this.zzbSF);
        }
        return this.zzbSq;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.mStatus;
    }

    @Override // com.google.android.gms.common.api.Releasable
    public final void release() {
        if (this.zzbSF == null) {
            return;
        }
        if (this.mClosed) {
            throw new IllegalStateException("releasing an already released result.");
        }
        try {
            if (this.zzbSq != null) {
                this.zzbSq.close();
            } else {
                this.zzbSF.close();
            }
            this.mClosed = true;
            this.zzbSF = null;
            this.zzbSq = null;
        } catch (IOException e) {
        }
    }
}
