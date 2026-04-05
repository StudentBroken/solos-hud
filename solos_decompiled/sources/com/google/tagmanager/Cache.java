package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface Cache<K, V> {
    V get(K k);

    void put(K k, V v);
}
