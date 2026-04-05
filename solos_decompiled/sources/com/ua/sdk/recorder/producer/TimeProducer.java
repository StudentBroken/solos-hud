package com.ua.sdk.recorder.producer;

import com.ua.sdk.recorder.MessageQueue;
import com.ua.sdk.recorder.RecorderClock;
import com.ua.sdk.recorder.message.TimeMessage;
import com.ua.sdk.util.Pools;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes65.dex */
public class TimeProducer extends MessageProducer {
    private static final long INTERVAL = 1000;
    private Pools.Pool<TimeMessage> timeMessagePool;
    private Timer timer;

    public TimeProducer(RecorderClock recorderClock, MessageQueue messageQueue) {
        super(recorderClock, messageQueue);
        this.timeMessagePool = new Pools.SynchronizedPool(512);
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void beginRecorder() {
        this.timer = new Timer("RecordTimeProducer");
        this.timer.scheduleAtFixedRate(new MyTimerTask(), 0L, 1000L);
    }

    @Override // com.ua.sdk.recorder.producer.MessageProducer
    public void finishRecorder() {
        this.timer.cancel();
        this.timer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TimeMessage acquireTimeMessage() {
        TimeMessage timeMessage = this.timeMessagePool.acquire();
        if (timeMessage == null) {
            return new TimeMessage(null, null, this.timeMessagePool);
        }
        return timeMessage;
    }

    protected class MyTimerTask extends TimerTask {
        protected MyTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TimeMessage timeMessage = TimeProducer.this.acquireTimeMessage();
            TimeProducer.this.offer(timeMessage);
        }
    }
}
