package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;

/* JADX INFO: loaded from: classes67.dex */
public interface ResultCallback<R extends Result> {
    void onResult(@NonNull R r);
}
