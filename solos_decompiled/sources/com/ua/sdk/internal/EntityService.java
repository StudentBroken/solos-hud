package com.ua.sdk.internal;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface EntityService<T extends Resource> {
    T create(T t) throws UaException;

    void delete(Reference reference) throws UaException;

    T fetch(Reference reference) throws UaException;

    EntityList<T> fetchPage(EntityListRef<T> entityListRef) throws UaException;

    T patch(T t, Reference reference) throws UaException;

    T save(T t) throws UaException;
}
