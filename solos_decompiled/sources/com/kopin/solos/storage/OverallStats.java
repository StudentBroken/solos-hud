package com.kopin.solos.storage;

import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes54.dex */
public class OverallStats {
    private Metrics.LongMetric mDuration = new Metrics.LongMetric();
    private Metrics.DoubleMetric mDistance = new Metrics.DoubleMetric();
    private Metrics.FloatMetric mOverallClimb = new Metrics.FloatMetric();
    private Metrics.DoubleMetric mTss = new Metrics.DoubleMetric();

    void addDuration(long duration) {
        if (duration != -2147483648L) {
            this.mDuration.addValue(Long.valueOf(duration));
        }
    }

    void addDistance(double distance) {
        if (distance != -2.147483648E9d) {
            this.mDistance.addValue(Double.valueOf(distance));
        }
    }

    void addOverallClimb(float climb) {
        if (climb != -2.14748365E9f) {
            this.mOverallClimb.addValue(Float.valueOf(climb));
        }
    }

    void addTSS(double tss) {
        if (tss != -2.147483648E9d) {
            this.mTss.addValue(Double.valueOf(tss));
        }
    }

    public boolean hasDistance() {
        return this.mDistance.hasData();
    }

    public boolean hasDuration() {
        return this.mDuration.hasData();
    }

    public boolean hasOverallClimb() {
        return this.mOverallClimb.hasData();
    }

    public long getDuration() {
        return this.mDuration.getTotal().longValue();
    }

    public double getDistanceForLocale() {
        return Utility.convertToUserUnits(1, this.mDistance.getTotal().doubleValue() / 1000.0d);
    }

    public float getOverallClimbForLocale() {
        return (float) Utility.convertToUserUnits(0, this.mOverallClimb.getTotal().floatValue());
    }

    public double getTSS() {
        return this.mTss.getAverage().doubleValue();
    }
}
