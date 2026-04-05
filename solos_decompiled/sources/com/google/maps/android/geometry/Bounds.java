package com.google.maps.android.geometry;

/* JADX INFO: loaded from: classes69.dex */
public class Bounds {
    public final double maxX;
    public final double maxY;
    public final double midX;
    public final double midY;
    public final double minX;
    public final double minY;

    public Bounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.midX = (minX + maxX) / 2.0d;
        this.midY = (minY + maxY) / 2.0d;
    }

    public boolean contains(double x, double y) {
        return this.minX <= x && x <= this.maxX && this.minY <= y && y <= this.maxY;
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public boolean intersects(double minX, double maxX, double minY, double maxY) {
        return minX < this.maxX && this.minX < maxX && minY < this.maxY && this.minY < maxY;
    }

    public boolean intersects(Bounds bounds) {
        return intersects(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY);
    }

    public boolean contains(Bounds bounds) {
        return bounds.minX >= this.minX && bounds.maxX <= this.maxX && bounds.minY >= this.minY && bounds.maxY <= this.maxY;
    }
}
