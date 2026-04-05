package com.ua.sdk.recorder.datasource;

import java.lang.Number;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes65.dex */
public class RollingAverage<T extends Number> {
    private int maxSize;
    private LinkedList<T> values = new LinkedList<>();
    private double avg = 0.0d;

    public RollingAverage(int maxSize) {
        this.maxSize = 0;
        this.maxSize = maxSize;
    }

    public double getAverage() {
        return this.avg;
    }

    public void addValue(T value) {
        double v = value.doubleValue();
        if (this.maxSize > 0 && !Double.isInfinite(v) && !Double.isNaN(v)) {
            if (this.values.size() == this.maxSize) {
                this.values.removeFirst();
            }
            this.values.add(value);
            this.avg = calculateAvg();
        }
    }

    public void reset() {
        this.values = new LinkedList<>();
        this.avg = 0.0d;
    }

    private double calculateAvg() {
        double sum = 0.0d;
        for (T value : this.values) {
            sum += value.doubleValue();
        }
        return sum / ((double) this.values.size());
    }
}
