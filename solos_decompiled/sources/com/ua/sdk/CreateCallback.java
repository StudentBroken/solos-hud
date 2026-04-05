package com.ua.sdk;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface CreateCallback<T extends Resource> {
    void onCreated(T t, UaException uaException);
}
