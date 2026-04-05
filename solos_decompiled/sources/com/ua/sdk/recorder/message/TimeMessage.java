package com.ua.sdk.recorder.message;

import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.Message;
import com.ua.sdk.recorder.MessagePersistence;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.util.Pools;

/* JADX INFO: loaded from: classes65.dex */
public class TimeMessage extends Message {
    public TimeMessage(DataSourceIdentifier dataSourceIdentifier, MessagePersistence messagePersistence, Pools.Pool pool) {
        super(dataSourceIdentifier, messagePersistence, pool);
    }

    @Override // com.ua.sdk.recorder.Message
    public void processMessage(RecorderCalculator recorderCalculator) {
        recorderCalculator.updateTime(getDataSourceIdentifier(), getTimestamp());
    }

    @Override // com.ua.sdk.recorder.Message
    public void reset() {
    }
}
