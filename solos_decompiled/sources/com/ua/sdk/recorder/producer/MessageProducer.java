package com.ua.sdk.recorder.producer;

import com.ua.sdk.recorder.Message;
import com.ua.sdk.recorder.MessageQueue;
import com.ua.sdk.recorder.RecorderClock;

/* JADX INFO: loaded from: classes65.dex */
public abstract class MessageProducer {
    private MessageQueue messageQueue;
    private RecorderClock recorderClock;

    public abstract void beginRecorder();

    public abstract void finishRecorder();

    protected MessageProducer(RecorderClock recorderClock, MessageQueue messageQueue) {
        this.recorderClock = recorderClock;
        this.messageQueue = messageQueue;
    }

    protected void offer(Message message) {
        message.setTimestamp(this.recorderClock.getTimestamp());
        this.messageQueue.offer(message);
    }
}
