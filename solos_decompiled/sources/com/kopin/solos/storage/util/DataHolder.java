package com.kopin.solos.storage.util;

import android.location.Location;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes54.dex */
public class DataHolder {
    public static final int DEFAULT_VALIDITY = 4;
    public static final int NO_DATA = Integer.MIN_VALUE;
    public static final float NO_ELEVATION = -2.14748365E9f;
    public static final int ROLLING_3_SECOND_VALIDITY = 3;
    public static final int ROLLING_5_SECOND_VALIDITY = 5;
    private final DoubleDataHolder mCadence;
    private long mElevationStamp;
    private final IntDataHolder mHeartRate;
    private float mInitialElevation;
    private float mLastElevation;
    private Location mLocation;
    private long mLocationStamp;
    private final IntDataHolder mOxygen;
    private final DoubleDataHolder mPower;
    private final DoubleDataHolder mSpeedMPS;
    private final DoubleDataHolder mStride;
    private long mValidity;

    public enum ValidityMode {
        INSTANTANEOUS,
        ROLLING_AVERAGE_3,
        ROLLING_AVERAGE_5;

        static ValidityMode getCurrentSetting(MetricDataType type) {
            String value = Prefs.getRollingAverageModeFor(type);
            return fromString(value);
        }

        static ValidityMode fromString(String value) {
            if (value == null || value.isEmpty() || value.contentEquals("0")) {
                return INSTANTANEOUS;
            }
            if (value.contentEquals("3")) {
                return ROLLING_AVERAGE_3;
            }
            return ROLLING_AVERAGE_5;
        }
    }

    public static boolean isValid(int value) {
        return value != Integer.MIN_VALUE;
    }

    public static boolean isValid(double value) {
        return value != -2.147483648E9d;
    }

    public DataHolder() {
        this(4);
    }

    public DataHolder(int dataValidity) {
        this.mSpeedMPS = new DoubleDataHolder();
        this.mPower = new DoubleDataHolder();
        this.mCadence = new DoubleDataHolder();
        this.mStride = new DoubleDataHolder();
        this.mHeartRate = new IntDataHolder();
        this.mOxygen = new IntDataHolder();
        this.mInitialElevation = -2.14748365E9f;
        this.mLastElevation = -2.14748365E9f;
        this.mValidity = dataValidity * 1000;
        configure();
    }

    public void configure() {
        switch (LiveRide.getCurrentSport()) {
            case RIDE:
                setMetricValidity(this.mSpeedMPS, ValidityMode.getCurrentSetting(MetricDataType.SPEED));
                setMetricValidity(this.mCadence, ValidityMode.getCurrentSetting(MetricDataType.CADENCE));
                break;
            case RUN:
                setMetricValidity(this.mSpeedMPS, ValidityMode.getCurrentSetting(MetricDataType.PACE));
                setMetricValidity(this.mCadence, ValidityMode.getCurrentSetting(MetricDataType.STEP));
                break;
        }
        setMetricValidity(this.mPower, ValidityMode.getCurrentSetting(MetricDataType.POWER));
        setMetricValidity(this.mHeartRate, ValidityMode.getCurrentSetting(MetricDataType.HEART_RATE));
        setMetricValidity(this.mStride, ValidityMode.getCurrentSetting(MetricDataType.STRIDE));
        setMetricValidity(this.mOxygen, ValidityMode.getCurrentSetting(MetricDataType.OXYGEN));
    }

    public void configure(Sensor.DataType dataType, String val) {
        ValidityMode mode = ValidityMode.fromString(val);
        switch (dataType) {
            case SPEED:
            case PACE:
                setMetricValidity(this.mSpeedMPS, mode);
                break;
            case CADENCE:
            case STEP:
                setMetricValidity(this.mCadence, mode);
                break;
            case POWER:
                setMetricValidity(this.mPower, mode);
                break;
            case HEARTRATE:
                setMetricValidity(this.mHeartRate, mode);
                break;
            case STRIDE:
                setMetricValidity(this.mStride, mode);
                break;
            case OXYGEN:
                setMetricValidity(this.mOxygen, mode);
                break;
        }
    }

    public void setLastCadence(double lastCadence) {
        this.mCadence.setData(Utility.getTimeMilliseconds(), Double.valueOf(lastCadence));
    }

    public void setLastOxygen(int lastOxygen) {
        this.mOxygen.setData(Utility.getTimeMilliseconds(), Integer.valueOf(lastOxygen));
    }

    public void setLastHeartRate(int lastHeartRate) {
        this.mHeartRate.setData(Utility.getTimeMilliseconds(), Integer.valueOf(lastHeartRate));
    }

    public void setLastSpeed(double lastSpeedMetresPerSecond) {
        this.mSpeedMPS.setData(Utility.getTimeMilliseconds(), Double.valueOf(lastSpeedMetresPerSecond));
    }

    public void setLastPower(double lastPower) {
        this.mPower.setData(Utility.getTimeMilliseconds(), Double.valueOf(lastPower));
    }

    public void setLastStride(double stride) {
        this.mStride.setData(Utility.getTimeMilliseconds(), Double.valueOf(stride));
    }

    public void setLastLocation(Location location) {
        this.mLocationStamp = Utility.getTimeMilliseconds();
        this.mLocation = location;
    }

    public Location getLastLocation() {
        if (this.mLocationStamp < Utility.getTimeMilliseconds() - this.mValidity) {
            return null;
        }
        return this.mLocation;
    }

    public void setLastElevation(float lastElevation) {
        if (this.mInitialElevation == -2.14748365E9f) {
            this.mInitialElevation = lastElevation;
        }
        this.mElevationStamp = Utility.getTimeMilliseconds();
        this.mLastElevation = lastElevation;
    }

    public int getLastHeartRate() {
        return this.mHeartRate.getData().intValue();
    }

    public int getLastOxygen() {
        return this.mOxygen.getData().intValue();
    }

    public double getLastSpeed() {
        return this.mSpeedMPS.getData().doubleValue();
    }

    public double getLastPace() {
        double lastSpeed = this.mSpeedMPS.getData().doubleValue();
        return lastSpeed <= 0.0d ? lastSpeed : 1.0d / lastSpeed;
    }

    public double getLastCadence() {
        return this.mCadence.getData().doubleValue();
    }

    public double getLastPower() {
        return this.mPower.getData().doubleValue();
    }

    public double getLastStride() {
        return this.mStride.getData().doubleValue();
    }

    public float getLastElevation() {
        if (this.mElevationStamp < Utility.getTimeMilliseconds() - this.mValidity) {
            return -2.14748365E9f;
        }
        return this.mLastElevation;
    }

    public float getInitialElevation() {
        return this.mInitialElevation;
    }

    public void resetInitialElevation() {
        this.mInitialElevation = -2.14748365E9f;
        this.mLastElevation = -2.14748365E9f;
    }

    private void setMetricValidity(MetricDataHolder holder, ValidityMode mode) {
        switch (mode) {
            case INSTANTANEOUS:
                holder.setInstantaneous(4000L);
                break;
            case ROLLING_AVERAGE_3:
                holder.setRollingAvg(3000L);
                break;
            case ROLLING_AVERAGE_5:
                holder.setRollingAvg(5000L);
                break;
        }
    }

    private static class DoubleDataHolder extends MetricDataHolder<Double> {
        private DoubleDataHolder() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.util.DataHolder.MetricDataHolder
        public Double noData() {
            return Double.valueOf(-2.147483648E9d);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.util.DataHolder.MetricDataHolder
        public Double getValue() {
            int count = 0;
            double tot = 0.0d;
            for (Long t : this.mData.keySet()) {
                Double v = (Double) this.mData.get(t);
                tot += v.doubleValue();
                count++;
            }
            return count > 0 ? new Double(tot / ((double) count)) : noData();
        }
    }

    private static class IntDataHolder extends MetricDataHolder<Integer> {
        private IntDataHolder() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.util.DataHolder.MetricDataHolder
        public Integer noData() {
            return Integer.MIN_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.kopin.solos.storage.util.DataHolder.MetricDataHolder
        public Integer getValue() {
            int count = 0;
            int tot = 0;
            for (Long t : this.mData.keySet()) {
                Integer v = (Integer) this.mData.get(t);
                tot += v.intValue();
                count++;
            }
            return count > 0 ? new Integer(tot / count) : noData();
        }
    }

    private static abstract class MetricDataHolder<T extends Number> {
        protected HashMap<Long, T> mData;
        private boolean mRollingAvg;
        private long mValidity;

        protected abstract T getValue();

        protected abstract T noData();

        private MetricDataHolder() {
            this.mData = new HashMap<>();
        }

        public void setRollingAvg(long validity) {
            this.mRollingAvg = true;
            this.mValidity = validity;
            this.mData.clear();
        }

        public void setInstantaneous(long validity) {
            this.mRollingAvg = false;
            this.mValidity = validity;
            this.mData.clear();
        }

        private boolean hasExpired(Long now, Long time) {
            return time.longValue() + this.mValidity < now.longValue();
        }

        private void pruneOldData() {
            ArrayList<Long> prune = new ArrayList<>();
            long now = Utility.getTimeMilliseconds();
            for (Long t : this.mData.keySet()) {
                if (hasExpired(Long.valueOf(now), t)) {
                    prune.add(t);
                }
            }
            Iterator<Long> it = prune.iterator();
            while (it.hasNext()) {
                this.mData.remove(it.next());
            }
        }

        public void setData(long stamp, T value) {
            if (!this.mRollingAvg) {
                this.mData.clear();
            }
            this.mData.put(Long.valueOf(stamp), value);
        }

        public T getData() {
            pruneOldData();
            return this.mData.isEmpty() ? (T) noData() : (T) getValue();
        }
    }
}
