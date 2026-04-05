package com.ua.sdk.group.objective;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface CriteriaItem<T> extends Parcelable {
    String getName();

    T getValue();

    void setValue(T t);
}
