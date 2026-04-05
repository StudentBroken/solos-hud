package com.kopin.solos.storage;

/* JADX INFO: loaded from: classes54.dex */
public class Metrics {
    public static final double NO_DATA_D = -2.147483648E9d;
    public static final float NO_DATA_F = -2.14748365E9f;
    public static final int NO_DATA_I = Integer.MIN_VALUE;
    public static final long NO_DATA_L = -2147483648L;

    public enum MetricType {
        ACCUMULATOR,
        RANGE
    }

    public static abstract class Metric<T extends Number> {
        protected T mLast;
        protected T mMax;
        protected T mMin;
        protected T mStart;
        protected T mTotal;
        protected T mGain = (T) noData();
        protected int mNum = 0;

        protected abstract void doAdd(T t);

        public abstract T getAverage();

        public abstract T getDiff();

        public abstract T getRange();

        protected abstract T noData();

        public void addValue(T val) {
            if (this.mNum == 0) {
                this.mStart = val;
                this.mTotal = val;
                this.mMin = val;
                this.mMax = val;
            } else {
                doAdd(val);
            }
            this.mNum++;
            this.mLast = val;
        }

        public T getInitial() {
            return this.mNum == 0 ? (T) noData() : this.mStart;
        }

        public T getCurrent() {
            return this.mNum == 0 ? (T) noData() : this.mLast;
        }

        public T getTotal() {
            return this.mNum == 0 ? (T) noData() : this.mTotal;
        }

        public T getMinimum() {
            return this.mNum == 0 ? (T) noData() : this.mMin;
        }

        public T getMaximum() {
            return this.mNum == 0 ? (T) noData() : this.mMax;
        }

        public T getGain() {
            return this.mNum == 0 ? (T) noData() : this.mGain;
        }

        public int getCount() {
            return this.mNum;
        }

        public boolean hasData() {
            return this.mNum > 0;
        }

        public String toString() {
            return " min " + getMinimum() + " max " + getMinimum() + " ave " + getAverage();
        }
    }

    public static class IntegerMetric extends Metric<Integer> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public Integer noData() {
            return Integer.MIN_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public void doAdd(Integer val) {
            this.mTotal = Integer.valueOf(((Integer) this.mTotal).intValue() + val.intValue());
            if (val.intValue() < ((Integer) this.mMin).intValue()) {
                this.mMin = val;
            }
            if (val.intValue() > ((Integer) this.mMax).intValue()) {
                this.mMax = val;
            }
            if (this.mNum > 0 && val.intValue() > ((Integer) this.mLast).intValue()) {
                if (this.mGain == noData()) {
                    this.mGain = Integer.valueOf(val.intValue() - ((Integer) this.mLast).intValue());
                } else {
                    this.mGain = Integer.valueOf((val.intValue() - ((Integer) this.mLast).intValue()) + ((Integer) this.mGain).intValue());
                }
            }
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Integer getAverage() {
            if (this.mNum == 0) {
                return Integer.MIN_VALUE;
            }
            return Integer.valueOf(((Integer) this.mTotal).intValue() / this.mNum);
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Integer getDiff() {
            if (this.mNum == 0) {
                return Integer.MIN_VALUE;
            }
            return Integer.valueOf(((Integer) this.mLast).intValue() - ((Integer) this.mStart).intValue());
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Integer getRange() {
            if (this.mNum == 0) {
                return Integer.MIN_VALUE;
            }
            return Integer.valueOf(((Integer) this.mMax).intValue() - ((Integer) this.mMin).intValue());
        }

        public void addMax(Integer val) {
            if (val.intValue() > ((Integer) this.mMax).intValue()) {
                this.mMax = val;
            }
        }
    }

    public static class LongMetric extends Metric<Long> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public Long noData() {
            return -2147483648L;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public void doAdd(Long val) {
            this.mTotal = Long.valueOf(((Long) this.mTotal).longValue() + val.longValue());
            if (val.longValue() < ((Long) this.mMin).longValue()) {
                this.mMin = val;
            }
            if (val.longValue() > ((Long) this.mMax).longValue()) {
                this.mMax = val;
            }
            if (this.mNum > 0 && val.longValue() > ((Long) this.mLast).longValue()) {
                if (this.mGain == noData()) {
                    this.mGain = Long.valueOf(val.longValue() - ((Long) this.mLast).longValue());
                } else {
                    this.mGain = Long.valueOf((val.longValue() - ((Long) this.mLast).longValue()) + ((Long) this.mGain).longValue());
                }
            }
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Long getAverage() {
            if (this.mNum == 0) {
                return -2147483648L;
            }
            return Long.valueOf(((Long) this.mTotal).longValue() / ((long) this.mNum));
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Long getDiff() {
            if (this.mNum == 0) {
                return -2147483648L;
            }
            return Long.valueOf(((Long) this.mLast).longValue() - ((Long) this.mStart).longValue());
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Long getRange() {
            if (this.mNum == 0) {
                return -2147483648L;
            }
            return Long.valueOf(((Long) this.mMax).longValue() - ((Long) this.mMin).longValue());
        }

        public void addMax(Long val) {
            if (val.longValue() > ((Long) this.mMax).longValue()) {
                this.mMax = val;
            }
        }
    }

    public static class FloatMetric extends Metric<Float> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public void doAdd(Float val) {
            this.mTotal = Float.valueOf(((Float) this.mTotal).floatValue() + val.floatValue());
            if (val.floatValue() < ((Float) this.mMin).floatValue()) {
                this.mMin = val;
            }
            if (val.floatValue() > ((Float) this.mMax).floatValue()) {
                this.mMax = val;
            }
            if (this.mNum > 0 && val.floatValue() > ((Float) this.mLast).floatValue()) {
                if (((Float) this.mGain).equals(noData())) {
                    this.mGain = Float.valueOf(val.floatValue() - ((Float) this.mLast).floatValue());
                } else {
                    this.mGain = Float.valueOf((val.floatValue() - ((Float) this.mLast).floatValue()) + ((Float) this.mGain).floatValue());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public Float noData() {
            return Float.valueOf(-2.14748365E9f);
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Float getAverage() {
            return this.mNum == 0 ? Float.valueOf(-2.14748365E9f) : Float.valueOf(((Float) this.mTotal).floatValue() / this.mNum);
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Float getDiff() {
            return this.mNum == 0 ? Float.valueOf(-2.14748365E9f) : Float.valueOf(((Float) this.mLast).floatValue() - ((Float) this.mStart).floatValue());
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Float getRange() {
            return this.mNum == 0 ? Float.valueOf(-2.14748365E9f) : Float.valueOf(((Float) this.mMax).floatValue() - ((Float) this.mMin).floatValue());
        }

        public void addMax(Float val) {
            if (val.floatValue() > ((Float) this.mMax).floatValue()) {
                this.mMax = val;
            }
        }
    }

    public static class DoubleMetric extends Metric<Double> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public void doAdd(Double val) {
            this.mTotal = Double.valueOf(((Double) this.mTotal).doubleValue() + val.doubleValue());
            if (val.doubleValue() < ((Double) this.mMin).doubleValue()) {
                this.mMin = val;
            }
            if (val.doubleValue() > ((Double) this.mMax).doubleValue()) {
                this.mMax = val;
            }
            if (this.mNum > 0 && val.doubleValue() > ((Double) this.mLast).doubleValue()) {
                if (((Double) this.mGain).equals(noData())) {
                    this.mGain = Double.valueOf(val.doubleValue() - ((Double) this.mLast).doubleValue());
                } else {
                    this.mGain = Double.valueOf((val.doubleValue() - ((Double) this.mLast).doubleValue()) + ((Double) this.mGain).doubleValue());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.Metrics.Metric
        public Double noData() {
            return Double.valueOf(-2.147483648E9d);
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Double getAverage() {
            return this.mNum == 0 ? Double.valueOf(-2.147483648E9d) : Double.valueOf(((Double) this.mTotal).doubleValue() / ((double) this.mNum));
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Double getDiff() {
            return this.mNum == 0 ? Double.valueOf(-2.147483648E9d) : Double.valueOf(((Double) this.mLast).doubleValue() - ((Double) this.mStart).doubleValue());
        }

        @Override // com.kopin.solos.storage.Metrics.Metric
        public Double getRange() {
            return this.mNum == 0 ? Double.valueOf(-2.147483648E9d) : Double.valueOf(((Double) this.mMax).doubleValue() - ((Double) this.mMin).doubleValue());
        }

        public void addMax(Double val) {
            if (this.mMax == 0 || val.doubleValue() > ((Double) this.mMax).doubleValue()) {
                this.mMax = val;
            }
        }
    }
}
