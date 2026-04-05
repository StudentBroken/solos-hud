package com.google.maps.android.clustering.algo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;
import com.google.maps.android.quadtree.PointQuadTree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes69.dex */
public class NonHierarchicalDistanceBasedAlgorithm<T extends ClusterItem> implements Algorithm<T> {
    public static final int MAX_DISTANCE_AT_ZOOM = 100;
    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1.0d);
    private final Collection<QuadItem<T>> mItems = new ArrayList();
    private final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<>(0.0d, 1.0d, 0.0d, 1.0d);

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItem(T item) {
        QuadItem<T> quadItem = new QuadItem<>(item);
        synchronized (this.mQuadTree) {
            this.mItems.add(quadItem);
            this.mQuadTree.add(quadItem);
        }
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void clearItems() {
        synchronized (this.mQuadTree) {
            this.mItems.clear();
            this.mQuadTree.clear();
        }
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public void removeItem(T item) {
        QuadItem<T> quadItem = new QuadItem<>(item);
        synchronized (this.mQuadTree) {
            this.mItems.remove(quadItem);
            this.mQuadTree.remove(quadItem);
        }
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        int discreteZoom = (int) zoom;
        double zoomSpecificSpan = (100.0d / Math.pow(2.0d, discreteZoom)) / 256.0d;
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        Map<QuadItem<T>, Double> distanceToCluster = new HashMap<>();
        Map<QuadItem<T>, StaticCluster<T>> itemToCluster = new HashMap<>();
        synchronized (this.mQuadTree) {
            for (QuadItem<T> candidate : this.mItems) {
                if (!hashSet.contains(candidate)) {
                    Bounds searchBounds = createBoundsFromSpan(candidate.getPoint(), zoomSpecificSpan);
                    Collection<T> collectionSearch = this.mQuadTree.search(searchBounds);
                    if (collectionSearch.size() == 1) {
                        hashSet2.add(candidate);
                        hashSet.add(candidate);
                        distanceToCluster.put(candidate, Double.valueOf(0.0d));
                    } else {
                        StaticCluster<T> cluster = new StaticCluster<>(((QuadItem) candidate).mClusterItem.getPosition());
                        hashSet2.add(cluster);
                        for (T clusterItem : collectionSearch) {
                            Double existingDistance = distanceToCluster.get(clusterItem);
                            double distance = distanceSquared(clusterItem.getPoint(), candidate.getPoint());
                            if (existingDistance != null) {
                                if (existingDistance.doubleValue() >= distance) {
                                    itemToCluster.get(clusterItem).remove(((QuadItem) clusterItem).mClusterItem);
                                }
                            }
                            distanceToCluster.put(clusterItem, Double.valueOf(distance));
                            cluster.add(((QuadItem) clusterItem).mClusterItem);
                            itemToCluster.put(clusterItem, cluster);
                        }
                        hashSet.addAll(collectionSearch);
                    }
                }
            }
        }
        return hashSet2;
    }

    @Override // com.google.maps.android.clustering.algo.Algorithm
    public Collection<T> getItems() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mQuadTree) {
            for (QuadItem<T> quadItem : this.mItems) {
                arrayList.add(((QuadItem) quadItem).mClusterItem);
            }
        }
        return arrayList;
    }

    private double distanceSquared(Point a, Point b) {
        return ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
    }

    private Bounds createBoundsFromSpan(Point p, double span) {
        double halfSpan = span / 2.0d;
        return new Bounds(p.x - halfSpan, p.x + halfSpan, p.y - halfSpan, p.y + halfSpan);
    }

    private static class QuadItem<T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private QuadItem(T item) {
            this.mClusterItem = item;
            this.mPosition = item.getPosition();
            this.mPoint = NonHierarchicalDistanceBasedAlgorithm.PROJECTION.toPoint(this.mPosition);
            this.singletonSet = Collections.singleton(this.mClusterItem);
        }

        @Override // com.google.maps.android.quadtree.PointQuadTree.Item
        public Point getPoint() {
            return this.mPoint;
        }

        @Override // com.google.maps.android.clustering.Cluster
        public LatLng getPosition() {
            return this.mPosition;
        }

        @Override // com.google.maps.android.clustering.Cluster
        public Set<T> getItems() {
            return this.singletonSet;
        }

        @Override // com.google.maps.android.clustering.Cluster
        public int getSize() {
            return 1;
        }

        public int hashCode() {
            return this.mClusterItem.hashCode();
        }

        public boolean equals(Object other) {
            if (other instanceof QuadItem) {
                return ((QuadItem) other).mClusterItem.equals(this.mClusterItem);
            }
            return false;
        }
    }
}
