package com.ua.sdk.recorder.message;

import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.Message;
import com.ua.sdk.recorder.MessagePersistence;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.util.Pools;

/* JADX INFO: loaded from: classes65.dex */
public class DataPointMessage extends Message {
    private DataPoint dataPoint;
    private DataTypeRef dataTypeRef;

    public DataPointMessage(DataSourceIdentifier dataSourceIdentifier, MessagePersistence messagePersistence, Pools.Pool pool) {
        super(dataSourceIdentifier, messagePersistence, pool);
    }

    public void setDataPoint(DataPoint dataPoint) {
        this.dataPoint = dataPoint;
    }

    public void setDataTypeRef(DataTypeRef dataType) {
        this.dataTypeRef = dataType;
    }

    @Override // com.ua.sdk.recorder.Message
    public void processMessage(RecorderCalculator recorderCalculator) {
        recorderCalculator.updateDataPoint(getDataSourceIdentifier(), this.dataPoint, this.dataTypeRef);
    }

    @Override // com.ua.sdk.recorder.Message
    public void reset() {
        this.dataPoint = null;
    }
}
