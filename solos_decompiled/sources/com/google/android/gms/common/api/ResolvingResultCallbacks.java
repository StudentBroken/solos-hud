package com.google.android.gms.common.api;

import android.app.Activity;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes67.dex */
public abstract class ResolvingResultCallbacks<R extends Result> extends ResultCallbacks<R> {
    private final Activity mActivity;
    private final int zzaBm;

    protected ResolvingResultCallbacks(@NonNull Activity activity, int i) {
        this.mActivity = (Activity) zzbr.zzb(activity, "Activity must not be null");
        this.zzaBm = i;
    }

    @Override // com.google.android.gms.common.api.ResultCallbacks
    @KeepForSdk
    public final void onFailure(@NonNull Status status) {
        if (!status.hasResolution()) {
            onUnresolvableFailure(status);
            return;
        }
        try {
            status.startResolutionForResult(this.mActivity, this.zzaBm);
        } catch (IntentSender.SendIntentException e) {
            Log.e("ResolvingResultCallback", "Failed to start resolution", e);
            onUnresolvableFailure(new Status(8));
        }
    }

    @Override // com.google.android.gms.common.api.ResultCallbacks
    public abstract void onSuccess(@NonNull R r);

    public abstract void onUnresolvableFailure(@NonNull Status status);
}
