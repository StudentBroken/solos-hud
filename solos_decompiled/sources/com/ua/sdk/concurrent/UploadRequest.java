package com.ua.sdk.concurrent;

import com.ua.sdk.Resource;
import com.ua.sdk.UaException;
import com.ua.sdk.UploadCallback;

/* JADX INFO: loaded from: classes65.dex */
public class UploadRequest<T extends Resource> extends AsyncRequest<T> {
    private final UploadCallback callback;

    public UploadRequest(UploadCallback callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(T result, UaException e) {
        EntityEventHandler.callOnUploadUploaded(result, e, this.callback);
    }
}
