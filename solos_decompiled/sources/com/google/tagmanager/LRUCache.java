package com.google.tagmanager;

import android.annotation.TargetApi;
import android.util.LruCache;
import com.google.tagmanager.CacheFactory;

/* JADX INFO: loaded from: classes49.dex */
@TargetApi(12)
class LRUCache<K, V> implements Cache<K, V> {
    private LruCache<K, V> lruCache;

    LRUCache(int maxSize, final CacheFactory.CacheSizeManager<K, V> sizeManager) {
        this.lruCache = new LruCache<K, V>(maxSize) { // from class: com.google.tagmanager.LRUCache.1
            @Override // android.util.LruCache
            protected int sizeOf(K key, V value) {
                return sizeManager.sizeOf(key, value);
            }
        };
    }

    @Override // com.google.tagmanager.Cache
    public void put(K key, V value) {
        this.lruCache.put(key, value);
    }

    @Override // com.google.tagmanager.Cache
    public V get(K key) {
        return this.lruCache.get(key);
    }
}
