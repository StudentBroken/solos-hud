package com.ua.sdk.concurrent;

import com.ua.sdk.Resource;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class SaveRequest<T extends Resource> extends AsyncRequest<T> {
    private final SaveCallback<T> callback;

    public SaveRequest(SaveCallback<T> callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(T entity, UaException e) {
        EntityEventHandler.callOnSaved(entity, e, this.callback);
    }
}
