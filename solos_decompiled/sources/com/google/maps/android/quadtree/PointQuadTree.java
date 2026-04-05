package com.google.maps.android.quadtree;

import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.quadtree.PointQuadTree.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class PointQuadTree<T extends Item> {
    private static final int MAX_DEPTH = 40;
    private static final int MAX_ELEMENTS = 50;
    private final Bounds mBounds;
    private List<PointQuadTree<T>> mChildren;
    private final int mDepth;
    private List<T> mItems;

    public interface Item {
        Point getPoint();
    }

    public PointQuadTree(double minX, double maxX, double minY, double maxY) {
        this(new Bounds(minX, maxX, minY, maxY));
    }

    public PointQuadTree(Bounds bounds) {
        this(bounds, 0);
    }

    private PointQuadTree(double minX, double maxX, double minY, double maxY, int depth) {
        this(new Bounds(minX, maxX, minY, maxY), depth);
    }

    private PointQuadTree(Bounds bounds, int depth) {
        this.mChildren = null;
        this.mBounds = bounds;
        this.mDepth = depth;
    }

    public void add(T item) {
        Point point = item.getPoint();
        if (this.mBounds.contains(point.x, point.y)) {
            insert(point.x, point.y, item);
        }
    }

    private void insert(double x, double y, T item) {
        if (this.mChildren == null) {
            if (this.mItems == null) {
                this.mItems = new ArrayList();
            }
            this.mItems.add(item);
            if (this.mItems.size() > 50 && this.mDepth < 40) {
                split();
                return;
            }
            return;
        }
        if (y < this.mBounds.midY) {
            if (x < this.mBounds.midX) {
                this.mChildren.get(0).insert(x, y, item);
                return;
            } else {
                this.mChildren.get(1).insert(x, y, item);
                return;
            }
        }
        if (x < this.mBounds.midX) {
            this.mChildren.get(2).insert(x, y, item);
        } else {
            this.mChildren.get(3).insert(x, y, item);
        }
    }

    private void split() {
        this.mChildren = new ArrayList(4);
        this.mChildren.add(new PointQuadTree<>(this.mBounds.minX, this.mBounds.midX, this.mBounds.minY, this.mBounds.midY, this.mDepth + 1));
        this.mChildren.add(new PointQuadTree<>(this.mBounds.midX, this.mBounds.maxX, this.mBounds.minY, this.mBounds.midY, this.mDepth + 1));
        this.mChildren.add(new PointQuadTree<>(this.mBounds.minX, this.mBounds.midX, this.mBounds.midY, this.mBounds.maxY, this.mDepth + 1));
        this.mChildren.add(new PointQuadTree<>(this.mBounds.midX, this.mBounds.maxX, this.mBounds.midY, this.mBounds.maxY, this.mDepth + 1));
        List<T> items = this.mItems;
        this.mItems = null;
        for (T item : items) {
            insert(item.getPoint().x, item.getPoint().y, item);
        }
    }

    public boolean remove(T item) {
        Point point = item.getPoint();
        if (this.mBounds.contains(point.x, point.y)) {
            return remove(point.x, point.y, item);
        }
        return false;
    }

    private boolean remove(double x, double y, T item) {
        if (this.mChildren == null) {
            return this.mItems.remove(item);
        }
        if (y < this.mBounds.midY) {
            if (x < this.mBounds.midX) {
                return this.mChildren.get(0).remove(x, y, item);
            }
            return this.mChildren.get(1).remove(x, y, item);
        }
        if (x < this.mBounds.midX) {
            return this.mChildren.get(2).remove(x, y, item);
        }
        return this.mChildren.get(3).remove(x, y, item);
    }

    public void clear() {
        this.mChildren = null;
        if (this.mItems != null) {
            this.mItems.clear();
        }
    }

    public Collection<T> search(Bounds searchBounds) {
        List<T> results = new ArrayList<>();
        search(searchBounds, results);
        return results;
    }

    private void search(Bounds searchBounds, Collection<T> results) {
        if (this.mBounds.intersects(searchBounds)) {
            if (this.mChildren != null) {
                for (PointQuadTree<T> quad : this.mChildren) {
                    quad.search(searchBounds, results);
                }
                return;
            }
            if (this.mItems != null) {
                if (searchBounds.contains(this.mBounds)) {
                    results.addAll(this.mItems);
                    return;
                }
                for (T item : this.mItems) {
                    if (searchBounds.contains(item.getPoint())) {
                        results.add(item);
                    }
                }
            }
        }
    }
}
