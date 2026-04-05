package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Result;

/* JADX INFO: loaded from: classes67.dex */
public abstract class ResultCallbacks<R extends Result> implements ResultCallback<R> {
    public abstract void onFailure(@NonNull Status status);

    @Override // com.google.android.gms.common.api.ResultCallback
    @KeepForSdk
    public final void onResult(@NonNull R r) {
        Status status = r.getStatus();
        if (status.isSuccess()) {
            onSuccess(r);
            return;
        }
        onFailure(status);
        if (r instanceof Releasable) {
            try {
                ((Releasable) r).release();
            } catch (RuntimeException e) {
                String strValueOf = String.valueOf(r);
                Log.w("ResultCallbacks", new StringBuilder(String.valueOf(strValueOf).length() + 18).append("Unable to release ").append(strValueOf).toString(), e);
            }
        }
    }

    public abstract void onSuccess(@NonNull R r);
}
