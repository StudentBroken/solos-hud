package com.ua.sdk;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface SaveCallback<T extends Resource> {
    void onSaved(T t, UaException uaException);
}
