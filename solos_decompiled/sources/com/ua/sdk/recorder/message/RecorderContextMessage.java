package com.ua.sdk.recorder.message;

import com.ua.sdk.datasourceidentifier.DataSourceIdentifierImpl;
import com.ua.sdk.recorder.Message;
import com.ua.sdk.recorder.MessagePersistence;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.util.Pools;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderContextMessage extends Message {
    private RecorderContext recorderContext;

    public RecorderContextMessage(DataSourceIdentifierImpl dataSourceIdentifier, MessagePersistence messagePersistence, Pools.Pool pool) {
        super(dataSourceIdentifier, messagePersistence, pool);
    }

    public void setRecorderContext(RecorderContext recorderContext) {
        this.recorderContext = recorderContext;
    }

    @Override // com.ua.sdk.recorder.Message
    public void processMessage(RecorderCalculator recorderCalculator) {
        recorderCalculator.updateRecorderContext(this.recorderContext);
    }

    @Override // com.ua.sdk.recorder.Message
    public void reset() {
        this.recorderContext = null;
    }
}
