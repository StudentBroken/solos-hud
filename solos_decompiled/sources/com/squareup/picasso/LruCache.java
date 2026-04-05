package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes11.dex */
public class LruCache implements Cache {
    private int evictionCount;
    private int hitCount;
    final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(Context context) {
        this(Utils.calculateMemoryCacheSize(context));
    }

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive.");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    @Override // com.squareup.picasso.Cache
    public Bitmap get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            Bitmap mapValue = this.map.get(key);
            if (mapValue != null) {
                this.hitCount++;
                return mapValue;
            }
            this.missCount++;
            return null;
        }
    }

    @Override // com.squareup.picasso.Cache
    public void set(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new NullPointerException("key == null || bitmap == null");
        }
        synchronized (this) {
            this.putCount++;
            this.size += Utils.getBitmapBytes(bitmap);
            Bitmap previous = this.map.put(key, bitmap);
            if (previous != null) {
                this.size -= Utils.getBitmapBytes(previous);
            }
        }
        trimToSize(this.maxSize);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0031, code lost:
    
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void trimToSize(int r7) {
        /*
            r6 = this;
        L0:
            monitor-enter(r6)
            int r3 = r6.size     // Catch: java.lang.Throwable -> L32
            if (r3 < 0) goto L11
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch: java.lang.Throwable -> L32
            boolean r3 = r3.isEmpty()     // Catch: java.lang.Throwable -> L32
            if (r3 == 0) goto L35
            int r3 = r6.size     // Catch: java.lang.Throwable -> L32
            if (r3 == 0) goto L35
        L11:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L32
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L32
            r4.<init>()     // Catch: java.lang.Throwable -> L32
            java.lang.Class r5 = r6.getClass()     // Catch: java.lang.Throwable -> L32
            java.lang.String r5 = r5.getName()     // Catch: java.lang.Throwable -> L32
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L32
            java.lang.String r5 = ".sizeOf() is reporting inconsistent results!"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L32
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L32
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L32
            throw r3     // Catch: java.lang.Throwable -> L32
        L32:
            r3 = move-exception
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L32
            throw r3
        L35:
            int r3 = r6.size     // Catch: java.lang.Throwable -> L32
            if (r3 <= r7) goto L41
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch: java.lang.Throwable -> L32
            boolean r3 = r3.isEmpty()     // Catch: java.lang.Throwable -> L32
            if (r3 == 0) goto L43
        L41:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L32
            return
        L43:
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch: java.lang.Throwable -> L32
            java.util.Set r3 = r3.entrySet()     // Catch: java.lang.Throwable -> L32
            java.util.Iterator r3 = r3.iterator()     // Catch: java.lang.Throwable -> L32
            java.lang.Object r1 = r3.next()     // Catch: java.lang.Throwable -> L32
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch: java.lang.Throwable -> L32
            java.lang.Object r0 = r1.getKey()     // Catch: java.lang.Throwable -> L32
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L32
            java.lang.Object r2 = r1.getValue()     // Catch: java.lang.Throwable -> L32
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2     // Catch: java.lang.Throwable -> L32
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch: java.lang.Throwable -> L32
            r3.remove(r0)     // Catch: java.lang.Throwable -> L32
            int r3 = r6.size     // Catch: java.lang.Throwable -> L32
            int r4 = com.squareup.picasso.Utils.getBitmapBytes(r2)     // Catch: java.lang.Throwable -> L32
            int r3 = r3 - r4
            r6.size = r3     // Catch: java.lang.Throwable -> L32
            int r3 = r6.evictionCount     // Catch: java.lang.Throwable -> L32
            int r3 = r3 + 1
            r6.evictionCount = r3     // Catch: java.lang.Throwable -> L32
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L32
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.LruCache.trimToSize(int):void");
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    @Override // com.squareup.picasso.Cache
    public final synchronized int size() {
        return this.size;
    }

    @Override // com.squareup.picasso.Cache
    public final synchronized int maxSize() {
        return this.maxSize;
    }

    @Override // com.squareup.picasso.Cache
    public final synchronized void clear() {
        evictAll();
    }

    @Override // com.squareup.picasso.Cache
    public final synchronized void clearKeyUri(String uri) {
        boolean sizeChanged = false;
        int uriLength = uri.length();
        Iterator<Map.Entry<String, Bitmap>> i = this.map.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, Bitmap> entry = i.next();
            String key = entry.getKey();
            Bitmap value = entry.getValue();
            int newlineIndex = key.indexOf(10);
            if (newlineIndex == uriLength && key.substring(0, newlineIndex).equals(uri)) {
                i.remove();
                this.size -= Utils.getBitmapBytes(value);
                sizeChanged = true;
            }
        }
        if (sizeChanged) {
            trimToSize(this.maxSize);
        }
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }
}
