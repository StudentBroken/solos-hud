package com.kopin.solos.views;

/* JADX INFO: loaded from: classes37.dex */
public class ZoneHelper {
    public float mDurationPercent = Float.MIN_VALUE;
    public double mMax;
    public double mMin;
    public float mTime;

    public ZoneHelper(float time, double min, double max) {
        this.mTime = 0.0f;
        this.mMin = Double.MAX_VALUE;
        this.mMax = Double.MIN_VALUE;
        this.mTime = time;
        this.mMin = min;
        this.mMax = max;
    }
}
