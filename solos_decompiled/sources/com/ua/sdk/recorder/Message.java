package com.ua.sdk.recorder;

import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.util.Pools;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public abstract class Message {
    private final DataSourceIdentifier dataSourceIdentifier;
    private final MessagePersistence messagePersistence;
    private final Pools.Pool pool;
    private double timestamp;

    public abstract void processMessage(RecorderCalculator recorderCalculator);

    public abstract void reset();

    protected Message(DataSourceIdentifier dataSourceIdentifier, MessagePersistence messagePersistence, Pools.Pool pool) {
        this.dataSourceIdentifier = dataSourceIdentifier;
        this.messagePersistence = messagePersistence;
        this.pool = pool;
    }

    public DataSourceIdentifier getDataSourceIdentifier() {
        return this.dataSourceIdentifier;
    }

    public void recycle() {
        this.timestamp = 0.0d;
        reset();
        if (this.pool != null) {
            this.pool.release(this);
        }
    }

    public void serialize(OutputStream outputStream) {
        this.messagePersistence.serialize(this, outputStream);
    }

    public double getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
