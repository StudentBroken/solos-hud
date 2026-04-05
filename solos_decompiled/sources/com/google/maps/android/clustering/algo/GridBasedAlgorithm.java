package com.google.maps.android.clustering.algo;

import android.support.v4.util.LongSparseArray;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes69.dex */
public class GridBasedAlgorithm<T extends ClusterItem> implements Algorithm<T> {
    private static final int GRID_SIZE = 100;
    private final Set<T> mItems = Collections.synchronizedSet(new HashSet());

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItem(T item) {
        this.mItems.add(item);
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItems(Collection<T> items) {
        this.mItems.addAll(items);
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void clearItems() {
        this.mItems.clear();
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void removeItem(T item) {
        this.mItems.remove(item);
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        long numCells = (long) Math.ceil((256.0d * Math.pow(2.0d, zoom)) / 100.0d);
        SphericalMercatorProjection proj = new SphericalMercatorProjection(numCells);
        HashSet<Cluster<T>> clusters = new HashSet<>();
        LongSparseArray<StaticCluster<T>> sparseArray = new LongSparseArray<>();
        synchronized (this.mItems) {
            for (T item : this.mItems) {
                Point p = proj.toPoint(item.getPosition());
                long coord = getCoord(numCells, p.x, p.y);
                StaticCluster<T> cluster = sparseArray.get(coord);
                if (cluster == null) {
                    cluster = new StaticCluster<>(proj.toLatLng(new Point(Math.floor(p.x) + 0.5d, Math.floor(p.y) + 0.5d)));
                    sparseArray.put(coord, cluster);
                    clusters.add(cluster);
                }
                cluster.add(item);
            }
        }
        return clusters;
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Collection<T> getItems() {
        return this.mItems;
    }

    private static long getCoord(long numCells, double x, double y) {
        return (long) ((numCells * Math.floor(x)) + Math.floor(y));
    }
}
