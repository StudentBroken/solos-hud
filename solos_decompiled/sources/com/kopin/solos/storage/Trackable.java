package com.kopin.solos.storage;

import android.provider.BaseColumns;
import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.Statistics;

/* JADX INFO: loaded from: classes54.dex */
public abstract class Trackable extends DataListener implements BaseColumns {
    private Metrics.DoubleMetric mCadence = new Metrics.DoubleMetric();
    private Metrics.IntegerMetric mHeartRate = new Metrics.IntegerMetric();
    private Metrics.DoubleMetric mPower = new Metrics.DoubleMetric();
    private Metrics.DoubleMetric mSpeed = new Metrics.DoubleMetric();
    private Metrics.IntegerMetric mOxygen = new Metrics.IntegerMetric();
    protected Metrics.FloatMetric mAltitude = new Metrics.FloatMetric();
    protected Metrics.DoubleMetric mNormPower = new Metrics.DoubleMetric();
    protected Metrics.DoubleMetric mIntensity = new Metrics.DoubleMetric();
    private Metrics.DoubleMetric mStride = new Metrics.DoubleMetric();

    public double getAverageSpeed() {
        return this.mSpeed.getAverage().doubleValue();
    }

    public double getAverageCadence() {
        return this.mCadence.getAverage().doubleValue();
    }

    public int getAverageHeartrate() {
        return this.mHeartRate.getAverage().intValue();
    }

    public double getAveragePower() {
        return this.mPower.getAverage().doubleValue();
    }

    public int getAverageOxygen() {
        return this.mOxygen.getAverage().intValue();
    }

    public double getAverageStride() {
        return this.mStride.getAverage().doubleValue();
    }

    double getMaxSpeed() {
        return this.mSpeed.getMaximum().doubleValue();
    }

    double getMaxCadence() {
        return this.mCadence.getMaximum().doubleValue();
    }

    int getMaxHeartRate() {
        return this.mHeartRate.getMaximum().intValue();
    }

    double getMaxPower() {
        return this.mPower.getMaximum().doubleValue();
    }

    int getMinOxygen() {
        return this.mOxygen.getMinimum().intValue();
    }

    double getMaxStride() {
        return this.mStride.getMaximum().doubleValue();
    }

    public float getElevation() {
        return this.mAltitude.getCurrent().floatValue();
    }

    public float getElevationChange() {
        return this.mAltitude.getDiff().floatValue();
    }

    public float getElevationRange() {
        return this.mAltitude.getRange().floatValue();
    }

    public float getOverallClimb() {
        return this.mAltitude.getGain().floatValue();
    }

    public float getAverageAltitude() {
        return this.mAltitude.getAverage().floatValue();
    }

    protected int getHeartRate() {
        return this.mHeartRate.getCurrent().intValue();
    }

    public double getPowerNormalised() {
        return this.mNormPower.getCurrent().doubleValue();
    }

    protected Statistics.DoubleStatistic getSpeedStats() {
        return new Statistics.DoubleStatistic(this.mSpeed);
    }

    protected Statistics.DoubleStatistic getPowerStats() {
        return new Statistics.DoubleStatistic(this.mPower);
    }

    protected Statistics.DoubleStatistic getCadenceStats() {
        return new Statistics.DoubleStatistic(this.mCadence);
    }

    protected Statistics.IntegerStatistic getOxygenStats() {
        return new Statistics.IntegerStatistic(this.mOxygen);
    }

    protected Statistics.IntegerStatistic getHeartRateStats() {
        return new Statistics.IntegerStatistic(this.mHeartRate);
    }

    protected Statistics.DoubleStatistic getNormalisedPowerStats() {
        return new Statistics.DoubleStatistic(this.mNormPower);
    }

    protected Statistics.DoubleStatistic getIntensityStats() {
        return new Statistics.DoubleStatistic(this.mIntensity);
    }

    protected Statistics.DoubleStatistic getStrideStats() {
        return new Statistics.DoubleStatistic(this.mStride);
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onSpeed(double speed) {
        this.mSpeed.addValue(Double.valueOf(speed));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onCadence(double cadenceRPM) {
        this.mCadence.addValue(Double.valueOf(cadenceRPM));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onHeartRate(int heartrate) {
        this.mHeartRate.addValue(Integer.valueOf(heartrate));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onBikePower(double power) {
        this.mPower.addValue(Double.valueOf(power));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onAltitude(float value) {
        this.mAltitude.addValue(Float.valueOf(value));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onOxygen(int oxygen) {
        this.mOxygen.addValue(Integer.valueOf(oxygen));
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onStride(double stride) {
        this.mStride.addValue(Double.valueOf(stride));
    }

    public void addMaxSpeed(double speed) {
        this.mSpeed.addMax(Double.valueOf(speed));
    }

    public void addMaxCadence(double cadenceRPM) {
        this.mCadence.addMax(Double.valueOf(cadenceRPM));
    }

    public void addMaxHeartRate(int heartRate) {
        this.mHeartRate.addMax(Integer.valueOf(heartRate));
    }

    public void addMaxBikePower(double power) {
        this.mPower.addMax(Double.valueOf(power));
    }

    public void addMaxOxygen(int oxygen) {
        this.mOxygen.addMax(Integer.valueOf(oxygen));
    }

    public void addMaxStride(double stride) {
        this.mStride.addMax(Double.valueOf(stride));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" cadence ").append(this.mCadence.toString()).append(" heartrate ").append(this.mHeartRate.toString()).append(" power ").append(this.mPower.toString()).append(" speed ").append(this.mSpeed.toString()).append(" mOxygen ").append(this.mOxygen.toString()).append(" mAltitude ").append(this.mAltitude.toString()).append(" mNormPower ").append(this.mNormPower.toString()).append(" mIntensity ").append(this.mIntensity.toString()).append(" stride ").append(this.mStride.toString());
        return builder.toString();
    }
}
