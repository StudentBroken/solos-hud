package com.ua.sdk.concurrent;

import com.ua.sdk.FetchCallback;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class FetchRequest<T extends Resource> extends AsyncRequest<T> {
    private final FetchCallback<T> callback;

    public FetchRequest(FetchCallback<T> callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(T entity, UaException e) {
        EntityEventHandler.callOnFetched(entity, e, this.callback);
    }
}
