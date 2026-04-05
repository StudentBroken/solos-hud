package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.TrainingStressScore;

/* JADX INFO: loaded from: classes54.dex */
public class SavedRide extends SavedWorkout {
    private static final String TAG = SavedRide.class.getSimpleName();

    public SavedRide(Ride ride) {
        super(ride);
        this.mId = ride.mId;
        this.mRouteId = ride.mRouteId;
        this.mMode = ride.mMode;
        this.mGhostId = ride.mGhostId;
        this.mFunctionalThresholdPower = ride.getFunctionalThresholdPower();
        this.mEffectivePeakHR = ride.mEffectivePeakHR;
        this.mStartTime = ride.getStartTime();
        this.mActualStartTime = ride.getActualStartTime();
        this.mDuration = ride.getTotalTime();
        this.mEndTime = ride.getEndTime();
        this.mDistance = ride.getDistance();
        this.mGainedAltitude = ride.mAltitude.getGain().floatValue();
        this.mTrainingStress = TrainingStressScore.calculate(this.mDuration, getAverageNormalisedPower(), getAverageIntensity(), this.mFunctionalThresholdPower);
        this.mTargetAverageCadence = ride.getTargetAverageCadence();
        this.mTargetAverageHeartrate = ride.getTargetAverageHeartrate();
        this.mTargetAveragePower = ride.getTargetAveragePower();
        this.mTargetAverageSpeedKm = ride.getTargetAverageSpeedKm();
        this.mCalories = ride.getCalories();
        if (Ride.hasData(ride.mAltitude.getMinimum().floatValue()) && Ride.hasData(ride.mAltitude.getMaximum().floatValue())) {
            this.mMaxAltitudeDiff = ride.mAltitude.getRange().floatValue();
        }
        this.mComplete = 1;
        this.mHasLocations = ride.hasLocations();
    }

    public SavedRide(Workout.RideMode rideMode, String title, long actualStartTime, long duration, int bikedId) {
        this.mMode = rideMode;
        this.mTitle = title;
        this.mActualStartTime = actualStartTime;
        this.mDuration = duration;
        this.mBikeId = bikedId;
    }

    public SavedRide(Cursor cursor) {
        int pos = cursor.getColumnIndex("_id");
        this.mId = pos != -1 ? cursor.getLong(pos) : 0L;
        int pos2 = cursor.getColumnIndex("routeId");
        this.mRouteId = pos2 != -1 ? cursor.getLong(pos2) : -1L;
        int pos3 = cursor.getColumnIndex(Ride.RIDE_MODE);
        this.mMode = Workout.RideMode.values()[pos3 != -1 ? cursor.getInt(pos3) : 0];
        int pos4 = cursor.getColumnIndex(Ride.GHOST_RIDE_ID);
        this.mGhostId = pos4 != -1 ? cursor.getLong(pos4) : -1L;
        int pos5 = cursor.getColumnIndex(Ride.START_TIME_ACTUAL);
        this.mActualStartTime = pos5 != -1 ? cursor.getLong(pos5) : -2147483648L;
        int pos6 = cursor.getColumnIndex(Ride.START_TIME);
        this.mStartTime = pos6 != -1 ? cursor.getLong(pos6) : -2147483648L;
        int pos7 = cursor.getColumnIndex(Ride.DURATION);
        this.mDuration = pos7 != -1 ? cursor.getLong(pos7) : -2147483648L;
        int pos8 = cursor.getColumnIndex(Ride.END_TIME);
        this.mEndTime = pos8 != -1 ? cursor.getLong(pos8) : -2147483648L;
        setSpeed(cursor, null, Ride.MAX_SPEED, Ride.AVERAGE_SPEED);
        setCadence(cursor, null, Ride.MAX_CADENCE, Ride.AVERAGE_CADENCE);
        setPower(cursor, null, Ride.MAX_POWER, Ride.AVERAGE_POWER);
        setHeartRate(cursor, null, Ride.MAX_HEARTRATE, Ride.AVERAGE_HEARTRATE);
        setOxygen(cursor, Ride.MIN_OXYGEN, null, Ride.AVERAGE_OXYGEN);
        setNormPower(cursor, null, Ride.MAX_NORM_POWER, Ride.AVERAGE_NORM_POWER);
        setIntensity(cursor, null, Ride.MAX_INTENSITY, Ride.AVERAGE_INTENSITY);
        int pos9 = cursor.getColumnIndex(Ride.DISTANCE);
        this.mDistance = pos9 != -1 ? cursor.getDouble(pos9) : -2.147483648E9d;
        int pos10 = cursor.getColumnIndex(Ride.GAINED_ALTITUDE);
        this.mGainedAltitude = pos10 != -1 ? cursor.getFloat(pos10) : -2.14748365E9f;
        int pos11 = cursor.getColumnIndex(Ride.MAX_ALTITUDE_DIFF);
        this.mMaxAltitudeDiff = pos11 != -1 ? cursor.getFloat(pos11) : -2.14748365E9f;
        int pos12 = cursor.getColumnIndex(Ride.CALORIES);
        this.mCalories = pos12 != -1 ? cursor.getInt(pos12) : Integer.MIN_VALUE;
        int pos13 = cursor.getColumnIndex(Ride.TITLE);
        this.mTitle = pos13 != -1 ? cursor.getString(pos13) : null;
        int pos14 = cursor.getColumnIndex(Ride.COMMENT);
        this.mComment = pos14 != -1 ? cursor.getString(pos14) : null;
        int pos15 = cursor.getColumnIndex(Ride.ACTIVITY);
        this.mActivity = pos15 != -1 ? cursor.getInt(pos15) : 0;
        int pos16 = cursor.getColumnIndex(Ride.BIKE_ID);
        this.mBikeId = pos16 != -1 ? cursor.getInt(pos16) : 0L;
        int pos17 = cursor.getColumnIndex(Ride.RIDER_ID);
        this.mRiderId = pos17 != -1 ? cursor.getInt(pos17) : 0L;
        int pos18 = cursor.getColumnIndex(Ride.TARGET_AVERAGE_CADENCE);
        this.mTargetAverageCadence = pos18 != -1 ? cursor.getInt(pos18) : Integer.MIN_VALUE;
        int pos19 = cursor.getColumnIndex(Ride.TARGET_AVERAGE_HEARTRATE);
        this.mTargetAverageHeartrate = pos19 != -1 ? cursor.getInt(pos19) : Integer.MIN_VALUE;
        int pos20 = cursor.getColumnIndex(Ride.TARGET_AVERAGE_POWER);
        this.mTargetAveragePower = pos20 != -1 ? cursor.getInt(pos20) : Integer.MIN_VALUE;
        int pos21 = cursor.getColumnIndex("ftp");
        this.mFunctionalThresholdPower = pos21 != -1 ? cursor.getFloat(pos21) : -2.147483648E9d;
        int pos22 = cursor.getColumnIndex(Ride.PEAK_HR);
        this.mEffectivePeakHR = pos22 != -1 ? cursor.getInt(pos22) : Integer.MIN_VALUE;
        int pos23 = cursor.getColumnIndex(Ride.TSS);
        this.mTrainingStress = pos23 != -1 ? cursor.getFloat(pos23) : -2.147483648E9d;
        int pos24 = cursor.getColumnIndex(Ride.TARGET_AVERAGE_SPEED);
        this.mTargetAverageSpeedKm = pos24 != -1 ? cursor.getFloat(pos24) : -2.147483648E9d;
        int pos25 = cursor.getColumnIndex(Ride.FINAL);
        this.mComplete = pos25 != -1 ? cursor.getInt(pos25) : 0;
        this.mHasLocations = SQLHelper.hasLocations(this.mRouteId);
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public SportType getSportType() {
        return SportType.RIDE;
    }

    @Override // com.kopin.solos.storage.SavedWorkout, com.kopin.solos.storage.IRidePartData
    public long getId() {
        return this.mId;
    }

    public static String getTitleFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Ride.TITLE);
        return pos != -1 ? cursor.getString(pos) : "";
    }

    static double getDistanceFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Ride.DISTANCE);
        if (pos != -1) {
            return cursor.getDouble(pos);
        }
        return -2.147483648E9d;
    }

    static long getDurationFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Ride.DURATION);
        if (pos != -1) {
            return cursor.getLong(pos);
        }
        return -2147483648L;
    }

    static float getOverallClimbFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex(Ride.GAINED_ALTITUDE);
        if (pos != -1) {
            return cursor.getFloat(pos);
        }
        return -2.14748365E9f;
    }

    static double getFTP(Cursor cursor) {
        int pos = cursor.getColumnIndex("ftp");
        if (pos != -1) {
            return cursor.getFloat(pos);
        }
        return -2.147483648E9d;
    }

    static int getPeakHR(Cursor cursor) {
        int pos = cursor.getColumnIndex(Ride.PEAK_HR);
        if (pos != -1) {
            return cursor.getInt(pos);
        }
        return Integer.MIN_VALUE;
    }

    static double getTSS(Cursor cursor) {
        double ftp = getFTP(cursor);
        if (ftp <= 0.0d) {
            return -2.147483648E9d;
        }
        int pos = cursor.getColumnIndex(Ride.AVERAGE_NORM_POWER);
        double normPower = pos != -1 ? cursor.getDouble(pos) : -2.147483648E9d;
        if (normPower <= 0.0d) {
            return -2.147483648E9d;
        }
        int pos2 = cursor.getColumnIndex(Ride.AVERAGE_INTENSITY);
        double intensity = pos2 != -1 ? cursor.getDouble(pos2) : -2.147483648E9d;
        if (intensity != -2.147483648E9d) {
            return TrainingStressScore.calculate(getDurationFromCursor(cursor), normPower, intensity, ftp);
        }
        return -2.147483648E9d;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public void setBikeId(long localBikeId) {
        this.mBikeId = localBikeId;
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public long getBikeId() {
        return this.mBikeId;
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
        return "{ id: " + this.mId + ", start time: " + this.mStartTime + ", duration: " + this.mDuration + ", end time: " + this.mEndTime + ", average speed: " + getAverageSpeed() + " true avg: " + getTrueAverageSpeed() + ", distance: " + this.mDistance + ", calories: " + this.mCalories + ", max[altitudeDiff: " + this.mMaxAltitudeDiff + ", cadence: " + getMaxCadence() + ", heartRate: " + getMaxHeartrate() + ", speed: " + getMaxSpeed() + ", power: , average oxygen: " + getAverageOxygen() + ", max oxygen: " + getMaxOxygen() + ", min oxygen: " + getMinOxygen() + ", power: " + getMaxPower() + "], title: " + this.mTitle + ", bike: N/A, rider: N/Aride: " + this.mActivity + ",comment: " + this.mComment + " }";
    }

    @Override // com.kopin.solos.storage.SavedWorkout
    public void setRouteId(long routeId) {
        this.mRouteId = routeId;
    }

    public double calculateTrainingStressScore() {
        this.mTrainingStress = TrainingStressScore.calculate(this.mDuration, getAverageNormalisedPower(), getAverageIntensity(), this.mFunctionalThresholdPower);
        return this.mTrainingStress;
    }

    @Override // com.kopin.solos.storage.SavedWorkout, com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.RIDE, getActualStartTime());
    }
}
