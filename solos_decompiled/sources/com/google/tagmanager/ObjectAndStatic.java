package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class ObjectAndStatic<T> {
    private final boolean mIsStatic;
    private final T mObject;

    ObjectAndStatic(T object, boolean isStatic) {
        this.mObject = object;
        this.mIsStatic = isStatic;
    }

    public T getObject() {
        return this.mObject;
    }

    public boolean isStatic() {
        return this.mIsStatic;
    }
}
