package com.ua.sdk.recorder.producer;

import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.MessageQueue;
import com.ua.sdk.recorder.RecorderClock;
import com.ua.sdk.recorder.message.DataPointMessage;
import com.ua.sdk.util.Pools;

/* JADX INFO: loaded from: classes65.dex */
public class SensorMessageProducer extends MessageProducer {
    private Pools.Pool<DataPointMessage> dataPointMessagePool;

    public SensorMessageProducer(RecorderClock recorderClock, MessageQueue messageQueue) {
        super(recorderClock, messageQueue);
        this.dataPointMessagePool = new Pools.SynchronizedPool(512);
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void beginRecorder() {
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void finishRecorder() {
    }

    private DataPointMessage acquireDatePointMessage(DataSourceIdentifier identifier) {
        DataPointMessage dataPointMessage = this.dataPointMessagePool.acquire();
        if (dataPointMessage == null) {
            return new DataPointMessage(identifier, null, this.dataPointMessagePool);
        }
        return dataPointMessage;
    }

    public void dealWithIt(DataSourceIdentifier dataSourceIdentifier, DataPoint dataPoint, DataTypeRef dataTypeRef) {
        DataPointMessage dataPointMessage = acquireDatePointMessage(dataSourceIdentifier);
        dataPointMessage.setDataPoint(dataPoint);
        dataPointMessage.setDataTypeRef(dataTypeRef);
        offer(dataPointMessage);
    }
}
