package com.kopin.solos.storage;

import com.kopin.solos.storage.Metrics;

/* JADX INFO: loaded from: classes54.dex */
public class Statistics {

    static abstract class Statistic<T extends Number> {
        protected T mAvg;
        protected T mMax;
        protected T mMin;

        public abstract T getRange();

        public Statistic(Metrics.Metric<T> metric) {
            this.mMin = (T) metric.getMinimum();
            this.mMax = (T) metric.getMaximum();
            this.mAvg = (T) metric.getAverage();
        }

        public Statistic(T min, T max, T avg) {
            this.mMax = max;
            this.mMin = min;
            this.mAvg = avg;
        }

        public T getAverage() {
            return this.mAvg;
        }

        public T getMinimum() {
            return this.mMin;
        }

        public T getMaximum() {
            return this.mMax;
        }
    }

    public static class IntegerStatistic extends Statistic<Integer> {
        public static final IntegerStatistic NO_DATA = new IntegerStatistic(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        public IntegerStatistic(Metrics.IntegerMetric metric) {
            super(metric);
        }

        public IntegerStatistic(Integer min, Integer max, Integer avg) {
            super(min, max, avg);
        }

        @Override // com.kopin.solos.storage.Statistics.Statistic
        public Integer getRange() {
            if (((Integer) this.mMax).intValue() == Integer.MIN_VALUE) {
                return (Integer) this.mMax;
            }
            if (((Integer) this.mMin).intValue() == Integer.MIN_VALUE) {
                return (Integer) this.mMin;
            }
            return Integer.valueOf(((Integer) this.mMax).intValue() - ((Integer) this.mMin).intValue());
        }
    }

    public static class LongStatistic extends Statistic<Long> {
        public static final LongStatistic NO_DATA = new LongStatistic(-2147483648L, -2147483648L, -2147483648L);

        public LongStatistic(Long min, Long max, Long avg) {
            super(min, max, avg);
        }

        @Override // com.kopin.solos.storage.Statistics.Statistic
        public Long getRange() {
            return Long.valueOf(((Long) this.mMax).longValue() - ((Long) this.mMin).longValue());
        }
    }

    public static class FloatStatistic extends Statistic<Float> {
        public FloatStatistic(Float min, Float max, Float avg) {
            super(min, max, avg);
        }

        @Override // com.kopin.solos.storage.Statistics.Statistic
        public Float getRange() {
            return Float.valueOf(((Float) this.mMax).floatValue() - ((Float) this.mMin).floatValue());
        }
    }

    public static class DoubleStatistic extends Statistic<Double> {
        public static final DoubleStatistic NO_DATA = new DoubleStatistic(Double.valueOf(-2.147483648E9d), Double.valueOf(-2.147483648E9d), Double.valueOf(-2.147483648E9d));

        public DoubleStatistic(Double min, Double max, Double avg) {
            super(min, max, avg);
        }

        public DoubleStatistic(Metrics.DoubleMetric metric) {
            super(metric);
        }

        @Override // com.kopin.solos.storage.Statistics.Statistic
        public Double getRange() {
            if (((Double) this.mMax).doubleValue() == -2.147483648E9d) {
                return (Double) this.mMax;
            }
            if (((Double) this.mMin).doubleValue() == -2.147483648E9d) {
                return (Double) this.mMin;
            }
            return Double.valueOf(((Double) this.mMax).doubleValue() - ((Double) this.mMin).doubleValue());
        }
    }
}
