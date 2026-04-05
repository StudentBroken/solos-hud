package com.ua.sdk;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface FetchCallback<T extends Resource> {
    void onFetched(T t, UaException uaException);
}
