package com.ua.sdk.concurrent;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class CreateRequest<T extends Resource> extends AsyncRequest<T> {
    private final CreateCallback<T> callback;

    public CreateRequest(CreateCallback<T> callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(T entity, UaException e) {
        EntityEventHandler.callOnCreated(entity, e, this.callback);
    }
}
