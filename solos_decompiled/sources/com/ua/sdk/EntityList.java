package com.ua.sdk;

import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface EntityList<T> extends Resource, Parcelable {
    T get(int i);

    List<T> getAll();

    EntityListRef<T> getNextPage();

    EntityListRef<T> getPreviousPage();

    @Override // com.ua.sdk.Resource
    EntityListRef<T> getRef();

    int getSize();

    int getTotalCount();

    boolean hasNext();

    boolean hasPrevious();

    boolean isEmpty();

    T remove(int i);

    void set(int i, T t);
}
