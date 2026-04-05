package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.CalorieCounter;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class Ride extends Workout {
    public static final String ACTIVITY = "activity";
    public static final String AVERAGE_CADENCE = "rAverageCadence";
    public static final String AVERAGE_HEARTRATE = "rAverageHR";
    public static final String AVERAGE_INTENSITY = "rAvgIntensity";
    public static final String AVERAGE_NORM_POWER = "rAverageNormPower";
    public static final String AVERAGE_OXYGEN = "rAverageOxygen";
    public static final String AVERAGE_POWER = "rAveragePower";
    public static final String AVERAGE_SPEED = "rAverageSpeed";
    public static final String BIKE_ID = "bikeType";
    public static final String CALORIES = "rCalories";
    public static final String COMMENT = "rComment";
    public static final String DISTANCE = "rDistance";
    public static final String DURATION = "rDuration";
    public static final String END_TIME = "rEndTime";
    public static final String FINAL = "rFinal";
    public static final String FTP = "ftp";
    public static final String GAINED_ALTITUDE = "rGainedAltitude";
    public static final String GHOST_RIDE_ID = "ghostRideId";
    public static final String MAX_ALTITUDE_DIFF = "rMaxAltitudeDiff";
    public static final String MAX_CADENCE = "rMaxCadence";
    public static final String MAX_HEARTRATE = "rMaxHR";
    public static final String MAX_INTENSITY = "rMaxIntensity";
    public static final String MAX_NORM_POWER = "rMaxNormPower";
    public static final String MAX_POWER = "rMaxPower";
    public static final String MAX_SPEED = "rMaxSpeed";
    public static final String MIN_OXYGEN = "rMaxOxygen";
    public static final String NAME = "Ride";
    public static final String PEAK_HR = "peakHR";
    public static final String RIDER_ID = "riderId";
    public static final String RIDE_MODE = "rType";
    public static final String ROUTE_ID = "routeId";
    public static final String START_TIME = "rStartTime";
    public static final String START_TIME_ACTUAL = "rActualStartTime";
    public static final String TAG = "Ride";
    public static final String TARGET_AVERAGE_CADENCE = "rTargetAverageCadence";
    public static final String TARGET_AVERAGE_HEARTRATE = "rTargetAverageHeartrate";
    public static final String TARGET_AVERAGE_POWER = "rTargetAveragePower";
    public static final String TARGET_AVERAGE_SPEED = "rTargetAverageSpeed";
    public static final String TITLE = "rTitle";
    public static final String TSS = "rTSS";
    protected double mBikeWeight;

    public Ride(long id, long route) {
        super(id, route);
    }

    public Ride(TimeHelper timeHelper, SplitHelper splitHelper) {
        super(timeHelper, splitHelper, Workout.RideMode.NORMAL, -1L);
    }

    public Ride(Cursor cursor) {
        this.mTargetAverageCadence = Prefs.getTargetAverageCadence();
        this.mTargetAveragePower = Prefs.getTargetAveragePower();
        this.mTargetAverageHeartrate = Prefs.getTargetAverageHeartrate();
        this.mTargetAverageSpeedKm = (float) Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue(), Prefs.UnitSystem.METRIC);
        int pos = cursor.getColumnIndex("_id");
        this.mId = pos != -1 ? cursor.getLong(pos) : 0L;
        int pos2 = cursor.getColumnIndex("routeId");
        this.mRouteId = pos2 != -1 ? cursor.getLong(pos2) : -1L;
        int pos3 = cursor.getColumnIndex(RIDE_MODE);
        this.mMode = Workout.RideMode.values()[pos3 != -1 ? cursor.getInt(pos3) : 0];
        int pos4 = cursor.getColumnIndex(GHOST_RIDE_ID);
        this.mGhostId = pos4 != -1 ? cursor.getLong(pos4) : -1L;
        int pos5 = cursor.getColumnIndex("ftp");
        this.mFunctionalThresholdPower = pos5 != -1 ? cursor.getDouble(pos5) : 0.0d;
        int pos6 = cursor.getColumnIndex(PEAK_HR);
        this.mEffectivePeakHR = pos6 != -1 ? cursor.getInt(pos6) : 0;
        this.normalisedPowerCalc = new NormalisedPower();
    }

    public Ride(TimeHelper timeHelper, SplitHelper splitHelper, Workout.RideMode mode, long ghostId) {
        super(timeHelper, splitHelper, mode, ghostId);
        this.mSplitHelper = splitHelper;
        if (splitHelper == null) {
            throw new NullPointerException("Null SplitHelper.");
        }
        this.mTimeHelper = timeHelper;
        if (timeHelper == null) {
            throw new NullPointerException("Null TimeHelper.");
        }
        this.mMode = mode;
        this.mBikeWeight = SQLHelper.getBike(Prefs.getChosenBikeId()).getWeight();
        this.mTargetAverageCadence = Prefs.getTargetAverageCadence();
        this.mTargetAveragePower = Prefs.getTargetAveragePower();
        this.mTargetAverageHeartrate = Prefs.getTargetAverageHeartrate();
        this.mTargetAverageSpeedKm = (float) Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue(), Prefs.UnitSystem.METRIC);
        FTP ftp = SQLHelper.getLatestFTP(SportType.RIDE);
        this.mFunctionalThresholdPower = ftp == null ? 0.0d : ftp.mValue;
        FTP phr = SQLHelper.getLatestPeakHR();
        this.mEffectivePeakHR = phr == null ? 0 : (int) phr.mValue;
        if (this.mEffectivePeakHR == 0) {
            this.mEffectivePeakHR = HeartRateZones.setFromAge(UserProfile.getAge());
        } else {
            HeartRateZones.setMaxHR(this.mEffectivePeakHR);
        }
        this.mId = SQLHelper.addRide(this.mRouteId, mode, ghostId);
        if (this.mId != -1) {
            SQLHelper.setRideFTP(this.mId, this.mFunctionalThresholdPower);
            SQLHelper.setPeakHR(SportType.RIDE, this.mId, this.mEffectivePeakHR);
        }
        this.normalisedPowerCalc = new NormalisedPower();
        init();
    }

    @Override // com.kopin.solos.storage.Workout
    public SavedRide end() {
        SavedRide saved = new SavedRide(this);
        SavedRides.saveWorkout(saved);
        return saved;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public int getCalories() {
        return CalorieCounter.calculate(this.mRiderWeight, this.mBikeWeight, getTotalTime(), getAverageSpeed(), getElevationChange());
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public boolean isLastLap() {
        return false;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.util.SplitHelper.SplitListener
    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
        super.onSplit(startTime, splitDistance, splitTime, isEnd);
        Lap lap = this.mFinalLap;
        this.mFinalLap = new Lap(this, this.mSplitHelper.getType(), this.mTimeHelper);
        int calories = Integer.MIN_VALUE;
        if (splitDistance > 0.0d) {
            calories = CalorieCounter.calculate(this.mRiderWeight, this.mBikeWeight, splitTime, lap.getAverageSpeed(), lap.getElevationChange());
        }
        Lap.Split split = lap.end(startTime, splitDistance, splitTime, calories);
        this.mSplits.add(split);
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IWorkout
    public Lap.Split getLastSplit() {
        List<Lap.Split> splits = new ArrayList<>();
        splits.addAll(this.mSplits);
        if (splits.size() == 0) {
            return null;
        }
        return splits.get(splits.size());
    }

    public double getFunctionalThresholdPower() {
        return this.mFunctionalThresholdPower;
    }

    public DataListener getDataListener() {
        return this;
    }

    public static class Header extends Workout.Header {
        public Header(Cursor cursor) {
            super(cursor);
            this.mGhostId = cursor.getLong(2);
        }

        @Override // com.kopin.solos.storage.Workout.Header
        public SportType getSportType() {
            return SportType.RIDE;
        }
    }
}
