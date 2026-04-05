package com.ua.sdk.concurrent;

import com.ua.sdk.MultipleCreateCallback;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class MultipleCreateRequest<T extends Resource> extends AsyncRequest<List<T>> {
    private final MultipleCreateCallback<T> callback;

    public MultipleCreateRequest(MultipleCreateCallback<T> callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(List<T> result, UaException e) {
        EntityEventHandler.callOnMultipleCreated(result, e, this.callback);
    }
}
