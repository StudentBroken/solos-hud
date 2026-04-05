package com.ua.sdk.concurrent;

import com.ua.sdk.DeleteCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class DeleteRequest<T extends Reference> extends AsyncRequest<T> {
    private final DeleteCallback<T> callback;

    public DeleteRequest(DeleteCallback<T> callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(T ref, UaException e) {
        EntityEventHandler.callOnDeleted(ref, e, this.callback);
    }
}
