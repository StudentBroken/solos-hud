package com.ua.sdk.concurrent;

import com.ua.sdk.Request;

/* JADX INFO: loaded from: classes65.dex */
public class SynchronousRequest implements Request {
    public static final SynchronousRequest INSTANCE = new SynchronousRequest();

    private SynchronousRequest() {
    }

    @Override // com.ua.sdk.Request
    public boolean cancel() {
        return false;
    }

    @Override // com.ua.sdk.Request
    public boolean isCanceled() {
        return false;
    }

    @Override // com.ua.sdk.Request
    public boolean isAsynchronous() {
        return false;
    }
}
