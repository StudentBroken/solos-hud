package com.ua.sdk;

import com.ua.sdk.EntityRef;

/* JADX INFO: loaded from: classes65.dex */
public interface Entity<R extends EntityRef> extends Resource<R> {
    @Override // com.ua.sdk.Resource
    R getRef();
}
