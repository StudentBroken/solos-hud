package com.ua.sdk.aggregate;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface AggregateManager {
    EntityList<Aggregate> fetchAggregateList(EntityListRef<Aggregate> entityListRef) throws UaException;

    Request fetchAggregateList(EntityListRef<Aggregate> entityListRef, FetchCallback<EntityList<Aggregate>> fetchCallback);
}
