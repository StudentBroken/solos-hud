package com.ua.sdk.recorder.producer;

import com.ua.sdk.recorder.MessageQueue;
import com.ua.sdk.recorder.RecorderClock;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.message.RecorderContextMessage;
import com.ua.sdk.recorder.message.StartMessage;
import com.ua.sdk.recorder.message.StopMessage;

/* JADX INFO: loaded from: classes65.dex */
public class CommandProducer extends MessageProducer {
    public CommandProducer(RecorderClock clock, MessageQueue messageQueue) {
        super(clock, messageQueue);
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void beginRecorder() {
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void finishRecorder() {
    }

    public void produceStartSegment() {
        offer(new StartMessage(null, null, null));
    }

    public void produceStopSegment() {
        offer(new StopMessage(null, null, null));
    }

    public void produceRecorderContext(RecorderContext recorderContext) {
        RecorderContextMessage recorderContextMessage = new RecorderContextMessage(null, null, null);
        recorderContextMessage.setRecorderContext(recorderContext);
        offer(recorderContextMessage);
    }
}
