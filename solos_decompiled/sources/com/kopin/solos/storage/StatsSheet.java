package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.storage.Statistics;
import com.kopin.solos.storage.util.Conversion;

/* JADX INFO: loaded from: classes54.dex */
public class StatsSheet {
    private static final String TAG = "StatsSheet";
    private Statistics.DoubleStatistic mSpeed = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.IntegerStatistic mOxygen = Statistics.IntegerStatistic.NO_DATA;
    private Statistics.DoubleStatistic mCadence = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.DoubleStatistic mPower = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.IntegerStatistic mHeartrate = Statistics.IntegerStatistic.NO_DATA;
    private Statistics.DoubleStatistic mNormPower = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.DoubleStatistic mIntensity = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.DoubleStatistic mStride = Statistics.DoubleStatistic.NO_DATA;
    private Statistics.DoubleStatistic mPace = Statistics.DoubleStatistic.NO_DATA;

    protected StatsSheet(Trackable parent) {
        setSpeed(parent.getSpeedStats());
        setCadence(parent.getCadenceStats());
        setPower(parent.getPowerStats());
        setHeartRate(parent.getHeartRateStats());
        setOxygen(parent.getOxygenStats());
        setNormPower(parent.getNormalisedPowerStats());
        setIntensity(parent.getIntensityStats());
        setStride(parent.getStrideStats());
    }

    protected StatsSheet() {
    }

    protected void setSpeed(Statistics.DoubleStatistic speed) {
        this.mSpeed = speed;
    }

    protected void setPower(Statistics.DoubleStatistic power) {
        this.mPower = power;
    }

    protected void setCadence(Statistics.DoubleStatistic rpm) {
        this.mCadence = rpm;
    }

    protected void setHeartRate(Statistics.IntegerStatistic heart) {
        this.mHeartrate = heart;
    }

    protected void setOxygen(Statistics.IntegerStatistic oxygen) {
        this.mOxygen = oxygen;
    }

    protected void setNormPower(Statistics.DoubleStatistic normPower) {
        this.mNormPower = normPower;
    }

    protected void setIntensity(Statistics.DoubleStatistic intensity) {
        this.mIntensity = intensity;
    }

    protected void setStride(Statistics.DoubleStatistic stride) {
        this.mStride = stride;
    }

    protected void setPace() {
        double avgPace = Conversion.speedToPace(getAverageSpeed());
        double maxPace = Conversion.speedToPace(getMaxSpeed());
        this.mPace = new Statistics.DoubleStatistic(Double.valueOf(-2.147483648E9d), Double.valueOf(maxPace), Double.valueOf(avgPace));
    }

    protected void setOxygen(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mOxygen = intStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setSpeed(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mSpeed = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setPower(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mPower = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setCadence(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mCadence = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setHeartRate(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mHeartrate = intStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setNormPower(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mNormPower = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setIntensity(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mIntensity = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setStride(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mStride = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    protected void setPace(Cursor cursor, String minCol, String maxCol, String avgCol) {
        this.mPace = doubleStatFromCursor(cursor, minCol, maxCol, avgCol);
    }

    private Statistics.DoubleStatistic doubleStatFromCursor(Cursor cursor, String minCol, String maxCol, String avgCol) {
        int pos = avgCol == null ? -1 : cursor.getColumnIndex(avgCol);
        double avg = pos != -1 ? cursor.getDouble(pos) : -2.147483648E9d;
        int pos2 = minCol == null ? -1 : cursor.getColumnIndex(minCol);
        double min = pos2 != -1 ? cursor.getDouble(pos2) : -2.147483648E9d;
        int pos3 = maxCol == null ? -1 : cursor.getColumnIndex(maxCol);
        double max = pos3 != -1 ? cursor.getDouble(pos3) : -2.147483648E9d;
        return new Statistics.DoubleStatistic(Double.valueOf(min), Double.valueOf(max), Double.valueOf(avg));
    }

    private Statistics.IntegerStatistic intStatFromCursor(Cursor cursor, String minCol, String maxCol, String avgCol) {
        int pos = avgCol == null ? -1 : cursor.getColumnIndex(avgCol);
        int avg = pos != -1 ? cursor.getInt(pos) : Integer.MIN_VALUE;
        int pos2 = minCol == null ? -1 : cursor.getColumnIndex(minCol);
        int min = pos2 != -1 ? cursor.getInt(pos2) : Integer.MIN_VALUE;
        int pos3 = maxCol == null ? -1 : cursor.getColumnIndex(maxCol);
        int max = pos3 != -1 ? cursor.getInt(pos3) : Integer.MIN_VALUE;
        return new Statistics.IntegerStatistic(Integer.valueOf(min), Integer.valueOf(max), Integer.valueOf(avg));
    }

    public int getAverageOxygen() {
        return this.mOxygen.getAverage().intValue();
    }

    public int getMinOxygen() {
        return this.mOxygen.getMinimum().intValue();
    }

    public int getMaxOxygen() {
        return this.mOxygen.getMaximum().intValue();
    }

    public double getAverageSpeed() {
        return this.mSpeed.getAverage().doubleValue();
    }

    public double getAverageCadence() {
        return this.mCadence.getAverage().doubleValue();
    }

    public int getAverageHeartrate() {
        return this.mHeartrate.getAverage().intValue();
    }

    public double getAveragePower() {
        return this.mPower.getAverage().doubleValue();
    }

    public double getMaxSpeed() {
        return this.mSpeed.getMaximum().doubleValue();
    }

    public double getMaxCadence() {
        return this.mCadence.getMaximum().doubleValue();
    }

    public double getMaxPower() {
        return this.mPower.getMaximum().doubleValue();
    }

    public int getMaxHeartrate() {
        return this.mHeartrate.getMaximum().intValue();
    }

    public double getAverageNormalisedPower() {
        return this.mNormPower.getAverage().doubleValue();
    }

    public double getMaxNormalisedPower() {
        return this.mNormPower.getMaximum().doubleValue();
    }

    public double getAverageIntensity() {
        return this.mIntensity.getAverage().doubleValue();
    }

    public double getMaxIntensity() {
        return this.mIntensity.getMaximum().doubleValue();
    }

    public double getAverageStride() {
        return this.mStride.getAverage().doubleValue();
    }

    public double getMaxStride() {
        return this.mStride.getMaximum().doubleValue();
    }

    public double getAveragePace() {
        return this.mPace.getAverage().doubleValue();
    }

    public double getMaxPace() {
        return this.mPace.getMaximum().doubleValue();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" cadence ").append(this.mCadence.toString()).append(" heartrate ").append(this.mHeartrate.toString()).append(" power ").append(this.mPower.toString()).append(" speed ").append(this.mSpeed.toString()).append(" mOxygen ").append(this.mOxygen.toString()).append(" mNormPower ").append(this.mNormPower.toString()).append(" mIntensity ").append(this.mIntensity.toString());
        return builder.toString();
    }
}
