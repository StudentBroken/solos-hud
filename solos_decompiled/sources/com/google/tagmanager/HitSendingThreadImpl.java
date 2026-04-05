package com.google.tagmanager;

import android.content.Context;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

/* JADX INFO: loaded from: classes49.dex */
class HitSendingThreadImpl extends Thread implements HitSendingThread {
    private static HitSendingThreadImpl sInstance;
    private volatile boolean mClosed;
    private final Context mContext;
    private volatile boolean mDisabled;
    private volatile HitStore mUrlStore;
    private final LinkedBlockingQueue<Runnable> queue;

    static HitSendingThreadImpl getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new HitSendingThreadImpl(ctx);
        }
        return sInstance;
    }

    private HitSendingThreadImpl(Context ctx) {
        super("GAThread");
        this.queue = new LinkedBlockingQueue<>();
        this.mDisabled = false;
        this.mClosed = false;
        if (ctx != null) {
            this.mContext = ctx.getApplicationContext();
        } else {
            this.mContext = ctx;
        }
        start();
    }

    @VisibleForTesting
    HitSendingThreadImpl(Context ctx, HitStore store) {
        super("GAThread");
        this.queue = new LinkedBlockingQueue<>();
        this.mDisabled = false;
        this.mClosed = false;
        if (ctx != null) {
            this.mContext = ctx.getApplicationContext();
        } else {
            this.mContext = ctx;
        }
        this.mUrlStore = store;
        start();
    }

    @VisibleForTesting
    HitStore getStore() {
        return this.mUrlStore;
    }

    @Override // com.google.tagmanager.HitSendingThread
    public void sendHit(String url) {
        sendHit(url, System.currentTimeMillis());
    }

    @VisibleForTesting
    void sendHit(final String url, final long hitTime) {
        queueToThread(new Runnable() { // from class: com.google.tagmanager.HitSendingThreadImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (HitSendingThreadImpl.this.mUrlStore == null) {
                    ServiceManagerImpl instance = ServiceManagerImpl.getInstance();
                    instance.initialize(HitSendingThreadImpl.this.mContext, this);
                    HitSendingThreadImpl.this.mUrlStore = instance.getStore();
                }
                HitSendingThreadImpl.this.mUrlStore.putHit(hitTime, url);
            }
        });
    }

    @Override // com.google.tagmanager.HitSendingThread
    public void queueToThread(Runnable r) {
        this.queue.add(r);
    }

    @VisibleForTesting
    int getQueueSize() {
        return this.queue.size();
    }

    private String printStackTrace(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        t.printStackTrace(stream);
        stream.flush();
        return new String(baos.toByteArray());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.mClosed) {
            try {
                try {
                    Runnable r = this.queue.take();
                    if (!this.mDisabled) {
                        r.run();
                    }
                } catch (InterruptedException e) {
                    Log.i(e.toString());
                }
            } catch (Throwable t) {
                Log.e("Error on GAThread: " + printStackTrace(t));
                Log.e("Google Analytics is shutting down.");
                this.mDisabled = true;
            }
        }
    }

    @VisibleForTesting
    void close() {
        this.mClosed = true;
        interrupt();
    }

    @VisibleForTesting
    boolean isDisabled() {
        return this.mDisabled;
    }
}
