package com.ua.sdk;

import com.ua.sdk.Reference;

/* JADX INFO: loaded from: classes65.dex */
public interface DeleteCallback<R extends Reference> {
    void onDeleted(R r, UaException uaException);
}
