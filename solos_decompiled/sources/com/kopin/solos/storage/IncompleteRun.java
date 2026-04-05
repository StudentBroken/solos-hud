package com.kopin.solos.storage;

import android.database.Cursor;
import android.util.Log;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.CalorieCounter;
import com.kopin.solos.storage.util.SplitHelper;

/* JADX INFO: loaded from: classes54.dex */
public class IncompleteRun extends Run {
    private long mActualStartTime;
    private double mDistance;
    private long mDuration;
    private long mLastLapTime;
    private SplitHelper.SplitType mSplitType;
    private long mStartTime;

    public IncompleteRun(SplitHelper.SplitType splitType, Cursor cursor) {
        super(cursor);
        int pos = cursor.getColumnIndex(Run.Field.START_TIME.name());
        long j = pos != -1 ? cursor.getLong(pos) : 0L;
        this.mActualStartTime = j;
        this.mStartTime = j;
        this.mSplitType = splitType;
        this.mRiderWeight = UserProfile.getWeightKG();
    }

    public void recoverMetrics() {
        Cursor cursor = SQLHelper.getMetricsCursor(this.mId, -1L, -1L, Record.MetricData.ALTITUDE, 0);
        SavedRide.foreachMetric(cursor, Record.MetricData.ALTITUDE, 0, new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.storage.IncompleteRun.1
            @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
            public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                IncompleteRun.this.onAltitude((float) realValue);
                return true;
            }
        });
        Cursor cursor2 = SQLHelper.getMetricsCursor(this.mId, -1L, -1L, Record.MetricData.POWER, 0);
        SavedRide.foreachMetric(cursor2, Record.MetricData.POWER, 0, new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.storage.IncompleteRun.2
            @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
            public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                IncompleteRun.this.mDuration = timestamp;
                IncompleteRun.this.onBikePower(realValue);
                return true;
            }
        });
        SQLHelper.foreachLap(this.mId, false, new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.storage.IncompleteRun.3
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                long lapEndTime = lap.getEndTime();
                Log.d("IncompleteRide", "Lap End Time: " + lapEndTime);
                if (lapEndTime != 0) {
                    IncompleteRun.this.mLastLapTime = lapEndTime;
                    IncompleteRun.this.addLapMetrics(lap);
                    return true;
                }
                IncompleteRun.this.mFinalLap = new IncompleteLap(lap, IncompleteRun.this.mSplitType);
                return true;
            }
        });
        Cursor rideCursor = SQLHelper.getRecordsCursor(this.mId, this.mLastLapTime, Long.MAX_VALUE, 0);
        int recordsCount = rideCursor.getCount();
        Log.d("IncompleteRide", "Record count: " + recordsCount);
        SavedRide.foreachRecord(rideCursor, 0, new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.storage.IncompleteRun.4
            @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
            public boolean onRecord(Record record) {
                IncompleteRun.this.mDuration = record.getTimestamp();
                if (record.hasDistance()) {
                    double distance = record.getDistance();
                    IncompleteRun.this.mDistance += distance;
                    if (IncompleteRun.this.mFinalLap != null) {
                        ((IncompleteLap) IncompleteRun.this.mFinalLap).addDistance(distance);
                    }
                }
                if (record.hasSpeed()) {
                    IncompleteRun.this.onSpeed(record.getSpeed());
                }
                if (record.hasHeartrate()) {
                    IncompleteRun.this.onHeartRate(record.getHeartrate());
                }
                if (record.hasCadence()) {
                    IncompleteRun.this.onCadence(record.getCadence());
                }
                if (record.hasPower() && IncompleteRun.this.mFinalLap != null) {
                    double power = record.getPower();
                    IncompleteRun.this.mFinalLap.onBikePower(power);
                    ((IncompleteLap) IncompleteRun.this.mFinalLap).addNormalisedPower(power, IncompleteRun.this.mDuration - IncompleteRun.this.mLastLapTime, IncompleteRun.this.mFunctionalThresholdPower);
                }
                if (record.hasOxygen()) {
                    IncompleteRun.this.onOxygen(record.getOxygen());
                }
                if (record.hasAltitude()) {
                    IncompleteRun.this.onAltitude(record.getAltitude());
                }
                if (record.hasStride()) {
                    IncompleteRun.this.onStride(record.getStride());
                    return true;
                }
                return true;
            }
        });
        if (this.mFinalLap != null && recordsCount > 0) {
            int calories = CalorieCounter.calculate(getDistance(), this.mRiderWeight);
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
        onStride(lap.getAverageStride());
        addMaxStride(lap.getMaxStride());
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
