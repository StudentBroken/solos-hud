package com.ua.sdk.recorder;

import com.ua.sdk.UaLog;
import com.ua.sdk.internal.Precondition;

/* JADX INFO: loaded from: classes65.dex */
public class RecordProcessor {
    private static final String TAG = "RecordProcessor";
    private boolean finished;
    private final MessageQueue processorMessageQueue;
    private final Thread processorThread = new Thread(new MyProcessLoopRunnable());
    private final RecorderCalculator recorderCalculator;
    private final String recorderName;

    public RecordProcessor(String recorderName, MessageQueue processorMessageQueue, RecorderCalculator recorderCalculator) {
        this.recorderName = recorderName;
        this.processorMessageQueue = processorMessageQueue;
        this.recorderCalculator = recorderCalculator;
    }

    public void begin() {
        Precondition.check(!this.finished, "Can not begin an already finished RecordProcessor.");
        this.finished = false;
        this.processorThread.setName("RecordProcessor-" + this.recorderName);
        this.processorThread.start();
    }

    public void finish() {
        Precondition.check(this.finished ? false : true, "Can not finish a RecordProcessor that has not called begin.");
        synchronized (this.processorMessageQueue) {
            this.finished = true;
            this.processorMessageQueue.notify();
        }
    }

    protected void processLoop() {
        Message message;
        while (!this.finished) {
            try {
                synchronized (this.processorMessageQueue) {
                    message = this.processorMessageQueue.poll();
                    if (message == null) {
                        this.processorMessageQueue.wait();
                    }
                }
                if (message != null) {
                    this.recorderCalculator.onProcessMessage(message);
                }
            } catch (InterruptedException e) {
                UaLog.info("MessageProducerQueue InterruptedException, moving on.");
            }
        }
    }

    protected class MyProcessLoopRunnable implements Runnable {
        protected MyProcessLoopRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RecordProcessor.this.processLoop();
        }
    }
}
