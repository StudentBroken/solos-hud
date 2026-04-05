package com.google.maps.android.clustering.algo;

import android.support.v4.util.LruCache;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes69.dex */
public class PreCachingAlgorithmDecorator<T extends ClusterItem> implements Algorithm<T> {
    private final Algorithm<T> mAlgorithm;
    private final LruCache<Integer, Set<? extends Cluster<T>>> mCache = new LruCache<>(5);
    private final ReadWriteLock mCacheLock = new ReentrantReadWriteLock();

    public PreCachingAlgorithmDecorator(Algorithm<T> algorithm) {
        this.mAlgorithm = algorithm;
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItem(T item) {
        this.mAlgorithm.addItem(item);
        clearCache();
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItems(Collection<T> items) {
        this.mAlgorithm.addItems(items);
        clearCache();
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void clearItems() {
        this.mAlgorithm.clearItems();
        clearCache();
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void removeItem(T item) {
        this.mAlgorithm.removeItem(item);
        clearCache();
    }

    private void clearCache() {
        this.mCache.evictAll();
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        int discreteZoom = (int) zoom;
        Set<? extends Cluster<T>> results = getClustersInternal(discreteZoom);
        if (this.mCache.get(Integer.valueOf(discreteZoom + 1)) == null) {
            new Thread(new PrecacheRunnable(discreteZoom + 1)).start();
        }
        if (this.mCache.get(Integer.valueOf(discreteZoom - 1)) == null) {
            new Thread(new PrecacheRunnable(discreteZoom - 1)).start();
        }
        return results;
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Collection<T> getItems() {
        return this.mAlgorithm.getItems();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<? extends Cluster<T>> getClustersInternal(int discreteZoom) {
        this.mCacheLock.readLock().lock();
        Set<? extends Cluster<T>> results = this.mCache.get(Integer.valueOf(discreteZoom));
        this.mCacheLock.readLock().unlock();
        if (results == null) {
            this.mCacheLock.writeLock().lock();
            results = this.mCache.get(Integer.valueOf(discreteZoom));
            if (results == null) {
                results = this.mAlgorithm.getClusters(discreteZoom);
                this.mCache.put(Integer.valueOf(discreteZoom), results);
            }
            this.mCacheLock.writeLock().unlock();
        }
        return results;
    }

    private class PrecacheRunnable implements Runnable {
        private final int mZoom;

        public PrecacheRunnable(int zoom) {
            this.mZoom = zoom;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Thread.sleep((long) ((Math.random() * 500.0d) + 500.0d));
            } catch (InterruptedException e) {
            }
            PreCachingAlgorithmDecorator.this.getClustersInternal(this.mZoom);
        }
    }
}
