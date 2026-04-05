package com.google.tagmanager;

import android.os.Build;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
class CacheFactory<K, V> {

    @VisibleForTesting
    final CacheSizeManager<K, V> mDefaultSizeManager = new CacheSizeManager<K, V>() { // from class: com.google.tagmanager.CacheFactory.1
        @Override // com.google.tagmanager.CacheFactory.CacheSizeManager
        public int sizeOf(K key, V value) {
            return 1;
        }
    };

    public interface CacheSizeManager<K, V> {
        int sizeOf(K k, V v);
    }

    public Cache<K, V> createCache(int maxSize) {
        return createCache(maxSize, this.mDefaultSizeManager);
    }

    public Cache<K, V> createCache(int maxSize, CacheSizeManager<K, V> sizeManager) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        return getSdkVersion() < 12 ? new SimpleCache(maxSize, sizeManager) : new LRUCache(maxSize, sizeManager);
    }

    @VisibleForTesting
    int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
}
