package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.util.TrainingStressScore;

/* JADX INFO: loaded from: classes54.dex */
public class SavedRun extends SavedWorkout {
    private static final String TAG = SavedRun.class.getSimpleName();
    protected double mTargetAveragePaceMinutesPerKm;

    public SavedRun(Run run) {
        super(run);
        this.mId = run.mId;
        this.mRouteId = run.mRouteId;
        this.mMode = run.mMode;
        this.mGhostId = run.mGhostId;
        this.mStartTime = run.getStartTime();
        this.mActualStartTime = run.getActualStartTime();
        this.mDuration = run.getTotalTime();
        this.mEndTime = run.getEndTime();
        this.mDistance = run.getDistance();
        this.mGainedAltitude = run.mAltitude.getGain().floatValue();
        this.mTargetAverageHeartrate = run.getTargetAverageHeartrate();
        this.mTargetAveragePower = run.getTargetAveragePower();
        this.mTargetAverageSpeedKm = run.getTargetAverageSpeedKm();
        this.mTargetAveragePaceMinutesPerKm = run.getTargetAveragePaceMinutesPerKm();
        this.mTargetAverageCadence = run.getTargetAverageCadence();
        this.mFunctionalThresholdPower = run.getFunctionalThresholdPower();
        this.mEffectivePeakHR = run.mEffectivePeakHR;
        this.mCalories = run.getCalories();
        this.mTrainingStress = TrainingStressScore.calculate(this.mDuration, getAverageNormalisedPower(), getAverageIntensity(), this.mFunctionalThresholdPower);
        if (Ride.hasData(run.mAltitude.getMinimum().floatValue()) && Ride.hasData(run.mAltitude.getMaximum().floatValue())) {
            this.mMaxAltitudeDiff = run.mAltitude.getRange().floatValue();
        }
        this.mComplete = 1;
        this.mHasLocations = run.hasLocations();
        setPace();
    }

    public SavedRun(String title, long actualStartTime, long duration, int gearId) {
        this.mTitle = title;
        this.mActualStartTime = actualStartTime;
        this.mDuration = duration;
        this.mGearId = gearId;
    }

    public SavedRun(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mRouteId = Run.Field.ROUTE_ID.getLong(cursor);
        this.mMode = Workout.RideMode.values()[Run.Field.RUN_MODE.getInt(cursor)];
        this.mActivity = Run.Field.RUN_TYPE.getInt(cursor);
        this.mGhostId = Run.Field.GHOST_OR_ROUTE_ID.getLong(cursor);
        this.mActualStartTime = Run.Field.START_TIME.getLong(cursor);
        this.mStartTime = 0L;
        this.mEndTime = Run.Field.END_TIME.getLong(cursor);
        this.mDuration = Run.Field.DURATION.getLong(cursor);
        setCadence(cursor, null, Run.Field.MAX_CADENCE.name(), Run.Field.AVG_CADENCE.name());
        setPower(cursor, null, Run.Field.MAX_POWER.name(), Run.Field.AVG_POWER.name());
        setHeartRate(cursor, null, Run.Field.MAX_HEARTRATE.name(), Run.Field.AVG_HEARTRATE.name());
        setOxygen(cursor, Run.Field.MIN_OXYGEN.name(), null, Run.Field.AVG_OXYGEN.name());
        setNormPower(cursor, null, Run.Field.MAX_NORMALISED_POWER.name(), Run.Field.AVG_NORMALISED_POWER.name());
        setIntensity(cursor, null, Run.Field.MAX_IF.name(), Run.Field.AVG_IF.name());
        setStride(cursor, null, Run.Field.MAX_STRIDE.name(), Run.Field.AVG_STRIDE.name());
        setPace(cursor, null, Run.Field.MAX_PACE.name(), Run.Field.AVG_PACE.name());
        this.mDistance = Run.Field.DISTANCE.getDouble(cursor);
        this.mGainedAltitude = Run.Field.OVERALL_CLIMB.getFloat(cursor);
        this.mMaxAltitudeDiff = Run.Field.ALTITUDE_RANGE.getFloat(cursor);
        this.mCalories = Run.Field.CALORIES.getInt(cursor);
        this.mTitle = Run.Field.TITLE.getString(cursor);
        this.mComment = Run.Field.COMMENT.getString(cursor);
        this.mGearId = Run.Field.GEAR_ID.getInt(cursor);
        this.mRiderId = Run.Field.RUNNER_ID.getInt(cursor);
        this.mTargetAverageCadence = Run.Field.TARGET_CADENCE.getInt(cursor);
        this.mTargetAverageHeartrate = Run.Field.TARGET_HEARTRATE.getInt(cursor);
        this.mTargetAveragePower = Run.Field.TARGET_POWER.getInt(cursor);
        this.mTargetAveragePaceMinutesPerKm = Run.Field.TARGET_PACE.getDouble(cursor);
        this.mFunctionalThresholdPower = Run.Field.FTP.getDouble(cursor);
        this.mTrainingStress = Run.Field.RSS.getDouble(cursor);
        this.mEffectivePeakHR = Run.Field.PEAK_HR.getInt(cursor);
        this.mHasLocations = SQLHelper.hasLocations(this.mRouteId);
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public SportType getSportType() {
        return SportType.RUN;
    }

    public static String getTitleFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Run.Field.TITLE.name());
        return pos != -1 ? cursor.getString(pos) : "";
    }

    static double getDistanceFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Run.Field.DISTANCE.name());
        if (pos != -1) {
            return cursor.getDouble(pos);
        }
        return -2.147483648E9d;
    }

    static long getDurationFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Run.Field.DURATION.name());
        if (pos != -1) {
            return cursor.getLong(pos);
        }
        return -2147483648L;
    }

    static float getOverallClimbFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Run.Field.OVERALL_CLIMB.name());
        if (pos != -1) {
            return cursor.getFloat(pos);
        }
        return -2.14748365E9f;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public void setGearId(long localGearId) {
        this.mGearId = localGearId;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public long getGearId() {
        return this.mGearId;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public void setRiderId(long riderId) {
        this.mRiderId = riderId;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public long getRiderId() {
        return this.mRiderId;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public void setActivity(int rideType) {
        this.mActivity = rideType;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public int getActivity() {
        return this.mActivity;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public int getTargetAverageCadence() {
        return this.mTargetAverageCadence;
    }

    @Override // com.kopin.solos.storage.SavedWorkout, com.kopin.solos.storage.StatsSheet
    public String toString() {
        return "{ id: " + this.mId + ", start time: " + this.mStartTime + ", duration: " + this.mDuration + ", end time: " + this.mEndTime + ", average speed: " + getAverageSpeed() + " true avg: " + getTrueAverageSpeed() + ", distance: " + this.mDistance + ", calories: " + this.mCalories + ", max[altitudeDiff: " + this.mMaxAltitudeDiff + ", cadence: " + getMaxCadence() + ", heartRate: " + getMaxHeartrate() + ", speed: " + getMaxSpeed() + ", power: , average oxygen: " + getAverageOxygen() + ", max oxygen: " + getMaxOxygen() + ", min oxygen: " + getMinOxygen() + ", power: " + getMaxPower() + "], title: " + this.mTitle + ", rider: N/Aride: " + this.mActivity + ",comment: " + this.mComment + " }";
    }

    @Override // com.kopin.solos.storage.SavedWorkout, com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.RUN, getActualStartTime());
    }

    public double getTargetAveragePaceMinutesPerKm() {
        return this.mTargetAveragePaceMinutesPerKm;
    }

    public double calculateRunningStressScore() {
        this.mTrainingStress = TrainingStressScore.calculate(this.mDuration, getAverageNormalisedPower(), getAverageIntensity(), this.mFunctionalThresholdPower);
        return this.mTrainingStress;
    }
}
