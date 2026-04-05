package com.kopin.solos.storage;

import android.database.Cursor;
import android.util.Log;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.CalorieCounter;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes54.dex */
public class IncompleteRide extends Ride {
    private long mActualStartTime;
    private double mDistance;
    private long mDuration;
    private long mLastLapTime;
    private SplitHelper.SplitType mSplitType;
    private long mStartTime;

    public IncompleteRide(SplitHelper.SplitType splitType, Cursor cursor) {
        super(cursor);
        int pos = cursor.getColumnIndex(Ride.START_TIME);
        this.mStartTime = pos != -1 ? cursor.getLong(pos) : 0L;
        int pos2 = cursor.getColumnIndex(Ride.START_TIME_ACTUAL);
        this.mActualStartTime = pos2 != -1 ? cursor.getLong(pos2) : 0L;
        this.mSplitType = splitType;
        this.mRiderWeight = UserProfile.getWeightKG();
        this.mBikeWeight = SQLHelper.getBike(Prefs.getChosenBikeId()).getWeight();
    }

    public void recoverMetrics() {
        Cursor cursor = SQLHelper.getMetricsCursor(this.mId, -1L, -1L, Record.MetricData.ALTITUDE, 0);
        SavedRide.foreachMetric(cursor, Record.MetricData.ALTITUDE, 0, new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.storage.IncompleteRide.1
            @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
            public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                IncompleteRide.this.onAltitude((float) realValue);
                return true;
            }
        });
        Cursor cursor2 = SQLHelper.getMetricsCursor(this.mId, -1L, -1L, Record.MetricData.POWER, 0);
        SavedRide.foreachMetric(cursor2, Record.MetricData.POWER, 0, new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.storage.IncompleteRide.2
            @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
            public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                IncompleteRide.this.mDuration = timestamp;
                IncompleteRide.this.onBikePower(realValue);
                return true;
            }
        });
        SQLHelper.foreachLap(this.mId, false, new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.storage.IncompleteRide.3
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                long lapEndTime = lap.getEndTime();
                Log.d("IncompleteRide", "Lap End Time: " + lapEndTime);
                if (lapEndTime != 0) {
                    IncompleteRide.this.mLastLapTime = lapEndTime;
                    IncompleteRide.this.addLapMetrics(lap);
                    return true;
                }
                IncompleteRide.this.mFinalLap = new IncompleteLap(lap, IncompleteRide.this.mSplitType);
                return true;
            }
        });
        Cursor rideCursor = SQLHelper.getRecordsCursor(this.mId, this.mLastLapTime, Long.MAX_VALUE, 0);
        int recordsCount = rideCursor.getCount();
        Log.d("IncompleteRide", "Record count: " + recordsCount);
        SavedRide.foreachRecord(rideCursor, 0, new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.storage.IncompleteRide.4
            @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
            public boolean onRecord(Record record) {
                IncompleteRide.this.mDuration = record.getTimestamp();
                if (record.hasDistance()) {
                    double distance = record.getDistance();
                    IncompleteRide.this.mDistance += distance;
                    if (IncompleteRide.this.mFinalLap != null) {
                        ((IncompleteLap) IncompleteRide.this.mFinalLap).addDistance(distance);
                    }
                }
                if (record.hasSpeed()) {
                    IncompleteRide.this.onSpeed(record.getSpeed());
                }
                if (record.hasHeartrate()) {
                    IncompleteRide.this.onHeartRate(record.getHeartrate());
                }
                if (record.hasCadence()) {
                    IncompleteRide.this.onCadence(record.getCadence());
                }
                if (record.hasPower()) {
                    double power = record.getPower();
                    IncompleteRide.this.mFinalLap.onBikePower(power);
                    ((IncompleteLap) IncompleteRide.this.mFinalLap).addNormalisedPower(power, IncompleteRide.this.mDuration - IncompleteRide.this.mLastLapTime, IncompleteRide.this.mFunctionalThresholdPower);
                }
                if (record.hasOxygen()) {
                    IncompleteRide.this.onOxygen(record.getOxygen());
                }
                if (record.hasAltitude()) {
                    IncompleteRide.this.onAltitude(record.getAltitude());
                    return true;
                }
                return true;
            }
        });
        if (this.mFinalLap != null && recordsCount > 0) {
            IncompleteLap finalLap = (IncompleteLap) this.mFinalLap;
            int calories = Integer.MIN_VALUE;
            if (finalLap.getDistance() > 0.0d) {
                int elevation = (int) Utility.metersToFeet(this.mAltitude.getRange().floatValue());
                calories = CalorieCounter.calculate(this.mRiderWeight, this.mBikeWeight, this.mDuration - finalLap.getStartTime(), finalLap.getAverageSpeed(), elevation);
            }
            ((IncompleteLap) this.mFinalLap).end(calories, this.mDuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLapMetrics(Lap.Saved lap) {
        this.mDistance += lap.getDistance();
        onSpeed(lap.getAverageSpeed());
        addMaxSpeed(lap.getMaxSpeed());
        onHeartRate(lap.getAverageHeartrate());
        addMaxHeartRate(lap.getMaxHeartrate());
        onCadence(lap.getAverageCadence());
        addMaxCadence(lap.getMaxCadence());
        onBikePower(lap.getAveragePower());
        addMaxBikePower(lap.getMaxPower());
    }

    @Override // com.kopin.solos.storage.Workout
    protected void addNormalisedPower(double power) {
        if (this.mDuration > 0) {
            double normalisePower = this.normalisedPowerCalc.calculateNormalisedPower(power, this.mDuration);
            if (normalisePower > 0.0d) {
                this.mNormPower.addValue(Double.valueOf(normalisePower));
                double intensityF = normalisePower / this.mFunctionalThresholdPower;
                this.mIntensity.addValue(Double.valueOf(intensityF));
            }
        }
    }

    @Override // com.kopin.solos.storage.Workout
    public boolean shouldAddMetric() {
        return true;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public long getActualStartTime() {
        return this.mActualStartTime;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public long getStartTime() {
        return this.mStartTime;
    }

    @Override // com.kopin.solos.storage.Workout
    public long getTotalTime() {
        return this.mDuration;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public long getEndTime() {
        return this.mStartTime + this.mDuration;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public double getDistance() {
        return this.mDistance;
    }
}
