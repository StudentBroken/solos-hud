package com.ua.sdk.recorder;

import com.ua.sdk.datapoint.DataFrameImpl;
import com.ua.sdk.util.Pools;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes65.dex */
public class RecyclableDataFrameImpl extends DataFrameImpl {
    private Pools.Pool<RecyclableDataFrameImpl> pool;
    private AtomicInteger referenceCount = new AtomicInteger();

    public RecyclableDataFrameImpl(Pools.Pool<RecyclableDataFrameImpl> pool) {
        this.pool = pool;
    }

    protected void retain() {
        this.referenceCount.incrementAndGet();
    }

    protected void release() {
        int ct = this.referenceCount.decrementAndGet();
        if (ct == 0) {
            this.pool.release(this);
        } else if (ct < 0) {
            throw new IllegalStateException("Release but not retained!");
        }
    }
}
