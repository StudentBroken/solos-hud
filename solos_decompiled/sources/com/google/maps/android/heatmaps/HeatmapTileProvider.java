package com.google.maps.android.heatmaps;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.util.LongSparseArray;
import com.facebook.internal.FacebookRequestErrorClassification;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.quadtree.PointQuadTree;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes69.dex */
public class HeatmapTileProvider implements TileProvider {
    private static final int DEFAULT_MAX_ZOOM = 11;
    private static final int DEFAULT_MIN_ZOOM = 5;
    public static final double DEFAULT_OPACITY = 0.7d;
    public static final int DEFAULT_RADIUS = 20;
    private static final int MAX_RADIUS = 50;
    private static final int MAX_ZOOM_LEVEL = 22;
    private static final int MIN_RADIUS = 10;
    private static final int SCREEN_SIZE = 1280;
    private static final int TILE_DIM = 512;
    static final double WORLD_WIDTH = 1.0d;
    private Bounds mBounds;
    private int[] mColorMap;
    private Collection<WeightedLatLng> mData;
    private Gradient mGradient;
    private double[] mKernel;
    private double[] mMaxIntensity;
    private double mOpacity;
    private int mRadius;
    private PointQuadTree<WeightedLatLng> mTree;
    private static final int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(FacebookRequestErrorClassification.EC_INVALID_SESSION, 225, 0), Color.rgb(255, 0, 0)};
    private static final float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1.0f};
    public static final Gradient DEFAULT_GRADIENT = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);

    public static class Builder {
        private Collection<WeightedLatLng> data;
        private int radius = 20;
        private Gradient gradient = HeatmapTileProvider.DEFAULT_GRADIENT;
        private double opacity = 0.7d;

        public Builder data(Collection<LatLng> val) {
            return weightedData(HeatmapTileProvider.wrapData(val));
        }

        public Builder weightedData(Collection<WeightedLatLng> val) {
            this.data = val;
            if (this.data.isEmpty()) {
                throw new IllegalArgumentException("No input points.");
            }
            return this;
        }

        public Builder radius(int val) {
            this.radius = val;
            if (this.radius < 10 || this.radius > 50) {
                throw new IllegalArgumentException("Radius not within bounds.");
            }
            return this;
        }

        public Builder gradient(Gradient val) {
            this.gradient = val;
            return this;
        }

        public Builder opacity(double val) {
            this.opacity = val;
            if (this.opacity < 0.0d || this.opacity > 1.0d) {
                throw new IllegalArgumentException("Opacity must be in range [0, 1]");
            }
            return this;
        }

        public HeatmapTileProvider build() {
            if (this.data == null) {
                throw new IllegalStateException("No input data: you must use either .data or .weightedData before building");
            }
            return new HeatmapTileProvider(this);
        }
    }

    private HeatmapTileProvider(Builder builder) {
        this.mData = builder.data;
        this.mRadius = builder.radius;
        this.mGradient = builder.gradient;
        this.mOpacity = builder.opacity;
        this.mKernel = generateKernel(this.mRadius, ((double) this.mRadius) / 3.0d);
        setGradient(this.mGradient);
        setWeightedData(this.mData);
    }

    public void setWeightedData(Collection<WeightedLatLng> data) {
        this.mData = data;
        if (this.mData.isEmpty()) {
            throw new IllegalArgumentException("No input points.");
        }
        this.mBounds = getBounds(this.mData);
        this.mTree = new PointQuadTree<>(this.mBounds);
        for (WeightedLatLng l : this.mData) {
            this.mTree.add(l);
        }
        this.mMaxIntensity = getMaxIntensities(this.mRadius);
    }

    public void setData(Collection<LatLng> data) {
        setWeightedData(wrapData(data));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<WeightedLatLng> wrapData(Collection<LatLng> data) {
        ArrayList<WeightedLatLng> weightedData = new ArrayList<>();
        for (LatLng l : data) {
            weightedData.add(new WeightedLatLng(l));
        }
        return weightedData;
    }

    @Override // com.google.android.gms.maps.model.TileProvider
    public Tile getTile(int x, int y, int zoom) {
        double tileWidth = 1.0d / Math.pow(2.0d, zoom);
        double padding = (((double) this.mRadius) * tileWidth) / 512.0d;
        double tileWidthPadded = tileWidth + (2.0d * padding);
        double bucketWidth = tileWidthPadded / ((double) ((this.mRadius * 2) + 512));
        double minX = (((double) x) * tileWidth) - padding;
        double maxX = (((double) (x + 1)) * tileWidth) + padding;
        double minY = (((double) y) * tileWidth) - padding;
        double maxY = (((double) (y + 1)) * tileWidth) + padding;
        double xOffset = 0.0d;
        Collection<WeightedLatLng> arrayList = new ArrayList();
        if (minX < 0.0d) {
            Bounds overlapBounds = new Bounds(1.0d + minX, 1.0d, minY, maxY);
            xOffset = -1.0d;
            arrayList = this.mTree.search(overlapBounds);
        } else if (maxX > 1.0d) {
            Bounds overlapBounds2 = new Bounds(0.0d, maxX - 1.0d, minY, maxY);
            xOffset = 1.0d;
            arrayList = this.mTree.search(overlapBounds2);
        }
        Bounds tileBounds = new Bounds(minX, maxX, minY, maxY);
        Bounds paddedBounds = new Bounds(this.mBounds.minX - padding, this.mBounds.maxX + padding, this.mBounds.minY - padding, this.mBounds.maxY + padding);
        if (!tileBounds.intersects(paddedBounds)) {
            return TileProvider.NO_TILE;
        }
        Collection<T> collectionSearch = this.mTree.search(tileBounds);
        if (collectionSearch.isEmpty()) {
            return TileProvider.NO_TILE;
        }
        double[][] intensity = (double[][]) Array.newInstance((Class<?>) Double.TYPE, (this.mRadius * 2) + 512, (this.mRadius * 2) + 512);
        for (T w : collectionSearch) {
            Point p = w.getPoint();
            int bucketX = (int) ((p.x - minX) / bucketWidth);
            int bucketY = (int) ((p.y - minY) / bucketWidth);
            double[] dArr = intensity[bucketX];
            dArr[bucketY] = dArr[bucketY] + w.getIntensity();
        }
        for (WeightedLatLng w2 : arrayList) {
            Point p2 = w2.getPoint();
            int bucketX2 = (int) (((p2.x + xOffset) - minX) / bucketWidth);
            int bucketY2 = (int) ((p2.y - minY) / bucketWidth);
            double[] dArr2 = intensity[bucketX2];
            dArr2[bucketY2] = dArr2[bucketY2] + w2.getIntensity();
        }
        double[][] convolved = convolve(intensity, this.mKernel);
        Bitmap bitmap = colorize(convolved, this.mColorMap, this.mMaxIntensity[zoom]);
        return convertBitmap(bitmap);
    }

    public void setGradient(Gradient gradient) {
        this.mGradient = gradient;
        this.mColorMap = gradient.generateColorMap(this.mOpacity);
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        this.mKernel = generateKernel(this.mRadius, ((double) this.mRadius) / 3.0d);
        this.mMaxIntensity = getMaxIntensities(this.mRadius);
    }

    public void setOpacity(double opacity) {
        this.mOpacity = opacity;
        setGradient(this.mGradient);
    }

    private double[] getMaxIntensities(int radius) {
        double[] maxIntensityArray = new double[22];
        for (int i = 5; i < 11; i++) {
            maxIntensityArray[i] = getMaxValue(this.mData, this.mBounds, radius, (int) (1280.0d * Math.pow(2.0d, i - 3)));
            if (i == 5) {
                for (int j = 0; j < i; j++) {
                    maxIntensityArray[j] = maxIntensityArray[i];
                }
            }
        }
        for (int i2 = 11; i2 < 22; i2++) {
            maxIntensityArray[i2] = maxIntensityArray[10];
        }
        return maxIntensityArray;
    }

    private static Tile convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return new Tile(512, 512, bitmapdata);
    }

    static Bounds getBounds(Collection<WeightedLatLng> points) {
        Iterator<WeightedLatLng> iter = points.iterator();
        WeightedLatLng first = iter.next();
        double minX = first.getPoint().x;
        double maxX = first.getPoint().x;
        double minY = first.getPoint().y;
        double maxY = first.getPoint().y;
        while (iter.hasNext()) {
            WeightedLatLng l = iter.next();
            double x = l.getPoint().x;
            double y = l.getPoint().y;
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        return new Bounds(minX, maxX, minY, maxY);
    }

    static double[] generateKernel(int radius, double sd) {
        double[] kernel = new double[(radius * 2) + 1];
        for (int i = -radius; i <= radius; i++) {
            kernel[i + radius] = Math.exp(((double) ((-i) * i)) / ((2.0d * sd) * sd));
        }
        return kernel;
    }

    static double[][] convolve(double[][] grid, double[] kernel) {
        int initial;
        int initial2;
        int radius = (int) Math.floor(((double) kernel.length) / 2.0d);
        int dimOld = grid.length;
        int dim = dimOld - (radius * 2);
        int upperLimit = (radius + dim) - 1;
        double[][] intermediate = (double[][]) Array.newInstance((Class<?>) Double.TYPE, dimOld, dimOld);
        for (int x = 0; x < dimOld; x++) {
            for (int y = 0; y < dimOld; y++) {
                double val = grid[x][y];
                if (val != 0.0d) {
                    int xUpperLimit = (upperLimit < x + radius ? upperLimit : x + radius) + 1;
                    if (radius > x - radius) {
                        initial2 = radius;
                    } else {
                        initial2 = x - radius;
                    }
                    for (int x2 = initial2; x2 < xUpperLimit; x2++) {
                        double[] dArr = intermediate[x2];
                        dArr[y] = dArr[y] + (kernel[x2 - (x - radius)] * val);
                    }
                }
            }
        }
        double[][] outputGrid = (double[][]) Array.newInstance((Class<?>) Double.TYPE, dim, dim);
        for (int x3 = radius; x3 < upperLimit + 1; x3++) {
            for (int y2 = 0; y2 < dimOld; y2++) {
                double val2 = intermediate[x3][y2];
                if (val2 != 0.0d) {
                    int yUpperLimit = (upperLimit < y2 + radius ? upperLimit : y2 + radius) + 1;
                    if (radius > y2 - radius) {
                        initial = radius;
                    } else {
                        initial = y2 - radius;
                    }
                    for (int y22 = initial; y22 < yUpperLimit; y22++) {
                        double[] dArr2 = outputGrid[x3 - radius];
                        int i = y22 - radius;
                        dArr2[i] = dArr2[i] + (kernel[y22 - (y2 - radius)] * val2);
                    }
                }
            }
        }
        return outputGrid;
    }

    static Bitmap colorize(double[][] grid, int[] colorMap, double max) {
        int maxColor = colorMap[colorMap.length - 1];
        double colorMapScaling = ((double) (colorMap.length - 1)) / max;
        int dim = grid.length;
        int[] colors = new int[dim * dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                double val = grid[j][i];
                int index = (i * dim) + j;
                int col = (int) (val * colorMapScaling);
                if (val != 0.0d) {
                    if (col < colorMap.length) {
                        colors[index] = colorMap[col];
                    } else {
                        colors[index] = maxColor;
                    }
                } else {
                    colors[index] = 0;
                }
            }
        }
        Bitmap tile = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
        tile.setPixels(colors, 0, dim, 0, 0, dim, dim);
        return tile;
    }

    static double getMaxValue(Collection<WeightedLatLng> points, Bounds bounds, int radius, int screenDim) {
        double minX = bounds.minX;
        double maxX = bounds.maxX;
        double minY = bounds.minY;
        double maxY = bounds.maxY;
        double boundsDim = maxX - minX > maxY - minY ? maxX - minX : maxY - minY;
        int nBuckets = (int) (((double) (screenDim / (radius * 2))) + 0.5d);
        double scale = ((double) nBuckets) / boundsDim;
        LongSparseArray<LongSparseArray<Double>> buckets = new LongSparseArray<>();
        double max = 0.0d;
        for (WeightedLatLng l : points) {
            double x = l.getPoint().x;
            double y = l.getPoint().y;
            int xBucket = (int) ((x - minX) * scale);
            int yBucket = (int) ((y - minY) * scale);
            LongSparseArray<Double> column = buckets.get(xBucket);
            if (column == null) {
                column = new LongSparseArray<>();
                buckets.put(xBucket, column);
            }
            Double value = column.get(yBucket);
            if (value == null) {
                value = Double.valueOf(0.0d);
            }
            Double value2 = Double.valueOf(value.doubleValue() + l.getIntensity());
            column.put(yBucket, value2);
            if (value2.doubleValue() > max) {
                max = value2.doubleValue();
            }
        }
        return max;
    }
}
