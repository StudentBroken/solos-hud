package com.ua.sdk.recorder;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderClock {
    private long baseTimestamp;

    public void init() {
        this.baseTimestamp = System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public double getTimestamp() {
        return (this.baseTimestamp + SystemClock.elapsedRealtime()) / 1000.0d;
    }
}
