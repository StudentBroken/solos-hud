package com.ua.sdk;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface UploadCallback<T extends Resource> {
    void onProgress(long j);

    void onUploaded(T t, UaException uaException);
}
