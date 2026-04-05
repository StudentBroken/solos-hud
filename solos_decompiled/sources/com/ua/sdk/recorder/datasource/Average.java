package com.ua.sdk.recorder.datasource;

import java.lang.Number;

/* JADX INFO: loaded from: classes65.dex */
public class Average<T extends Number> {
    private double avg = 0.0d;
    private long size = 0;

    public void addValue(T value) {
        this.avg = ((this.avg * this.size) + value.doubleValue()) / (this.size + 1);
        this.size++;
    }

    public double getAverage() {
        return this.avg;
    }

    public long getSize() {
        return this.size;
    }

    public void reset() {
        this.avg = 0.0d;
        this.size = 0L;
    }
}
