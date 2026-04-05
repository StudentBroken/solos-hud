package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;

/* JADX INFO: loaded from: classes67.dex */
public class Response<T extends Result> {
    private T zzaBl;

    public Response() {
    }

    protected Response(@NonNull T t) {
        this.zzaBl = t;
    }

    @NonNull
    protected T getResult() {
        return this.zzaBl;
    }

    public void setResult(@NonNull T t) {
        this.zzaBl = t;
    }
}
