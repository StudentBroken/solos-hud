package com.kopin.solos.storage;

import android.database.Cursor;
import android.location.Location;
import android.provider.BaseColumns;
import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public abstract class Workout extends Trackable implements BaseColumns, IWorkout {
    private static final String TAG = "Sport";
    protected int mEffectivePeakHR;
    protected volatile Lap mFinalLap;
    protected double mFunctionalThresholdPower;
    protected long mGhostId;
    protected long mId;
    protected RideMode mMode;
    protected long mNumLocations;
    protected double mRiderWeight;
    protected long mRouteId;
    protected SplitHelper mSplitHelper;
    protected final List<Lap.Split> mSplits;
    protected SportType mSportType;
    protected int mTargetAverageCadence;
    protected int mTargetAverageHeartrate;
    protected int mTargetAveragePower;
    protected float mTargetAverageSpeedKm;
    protected TimeHelper mTimeHelper;
    protected NormalisedPower normalisedPowerCalc;

    public abstract SavedWorkout end();

    public abstract int getCalories();

    public enum RideMode {
        NORMAL,
        GHOST_RIDE,
        GHOST_LAP,
        ROUTE,
        TARGETS,
        TRAINING,
        FTP;

        public static RideMode fromValue(String value) {
            if (value != null) {
                String value2 = value.trim().toUpperCase();
                for (RideMode mode : values()) {
                    if (mode.equals(value2)) {
                        return mode;
                    }
                }
            }
            return null;
        }
    }

    public Workout() {
        this.mSplits = new ArrayList();
        this.mRouteId = -1L;
        this.mGhostId = -1L;
        this.mMode = RideMode.NORMAL;
        this.mFunctionalThresholdPower = 0.0d;
        this.mEffectivePeakHR = 0;
        this.mNumLocations = 0L;
        this.mSportType = SportType.DEFAULT_TYPE;
    }

    public Workout(long id, long route) {
        this.mSplits = new ArrayList();
        this.mRouteId = -1L;
        this.mGhostId = -1L;
        this.mMode = RideMode.NORMAL;
        this.mFunctionalThresholdPower = 0.0d;
        this.mEffectivePeakHR = 0;
        this.mNumLocations = 0L;
        this.mSportType = SportType.DEFAULT_TYPE;
        this.mId = id;
        this.mRouteId = route;
    }

    public Workout(TimeHelper timeHelper, SplitHelper splitHelper, RideMode mode, long ghostId) {
        this.mSplits = new ArrayList();
        this.mRouteId = -1L;
        this.mGhostId = -1L;
        this.mMode = RideMode.NORMAL;
        this.mFunctionalThresholdPower = 0.0d;
        this.mEffectivePeakHR = 0;
        this.mNumLocations = 0L;
        this.mSportType = SportType.DEFAULT_TYPE;
        this.mSplitHelper = splitHelper;
        if (splitHelper == null) {
            throw new NullPointerException("Null SplitHelper.");
        }
        this.mTimeHelper = timeHelper;
        if (timeHelper == null) {
            throw new NullPointerException("Null TimeHelper.");
        }
        this.mMode = mode;
        if (shouldStoreRoute()) {
            this.mRouteId = SQLHelper.addRoute();
        } else {
            this.mRouteId = -1L;
        }
        this.mGhostId = ghostId;
        this.mRiderWeight = UserProfile.getWeightKG();
    }

    protected void init() {
        this.mFinalLap = new Lap(this, this.mSplitHelper.getType(), this.mTimeHelper);
    }

    @Override // com.kopin.solos.storage.IRidePartData
    public long getId() {
        return this.mId;
    }

    @Override // com.kopin.solos.storage.IWorkout
    public long getRouteId() {
        return this.mRouteId;
    }

    public boolean shouldStoreRoute() {
        return this.mMode == RideMode.NORMAL || this.mMode == RideMode.GHOST_RIDE || this.mMode == RideMode.ROUTE || this.mMode == RideMode.TRAINING;
    }

    @Override // com.kopin.solos.storage.IWorkout
    public SavedWorkout end(double riderWeight, double bikeWeight) {
        return null;
    }

    public long getActualStartTime() {
        return this.mTimeHelper.getActualRideStartTimestamp();
    }

    public long getStartTime() {
        return this.mTimeHelper.getStartTimestamp();
    }

    public long getEndTime() {
        return this.mTimeHelper.getStartTimestamp() + this.mTimeHelper.getTime();
    }

    public double getDistance() {
        return this.mSplitHelper.getTotalDistance();
    }

    protected long getTotalTime() {
        return this.mSplitHelper.getTotalTime();
    }

    @Override // com.kopin.solos.storage.IWorkout
    public boolean isStartedAndRunning() {
        return (this.mTimeHelper == null || !this.mTimeHelper.isStarted() || this.mTimeHelper.isPaused()) ? false : true;
    }

    public boolean shouldAddMetric() {
        return this.mTimeHelper.isStarted() && !this.mTimeHelper.isPaused();
    }

    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
    }

    @Override // com.kopin.solos.storage.IWorkout
    public int getCurrentHeartRateZone() {
        return HeartRateZones.computeHeartRateZone(getHeartRate());
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.IWorkout
    public float getElevation() {
        return this.mAltitude.getCurrent().floatValue();
    }

    @Override // com.kopin.solos.storage.Trackable
    public float getElevationChange() {
        return this.mAltitude.getDiff().floatValue();
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.IWorkout
    public float getOverallClimb() {
        return this.mAltitude.getGain().floatValue();
    }

    @Override // com.kopin.solos.storage.IWorkout
    public long getSplitTime() {
        return this.mSplitHelper.getSplitTime();
    }

    @Override // com.kopin.solos.storage.IWorkout
    public double getSplitDistance() {
        return this.mSplitHelper.getSplitDistance();
    }

    @Override // com.kopin.solos.storage.IWorkout
    public ArrayList<Lap.Split> getSplits() {
        return new ArrayList<>(this.mSplits);
    }

    @Override // com.kopin.solos.storage.IWorkout
    public int getSplitCount() {
        return this.mSplits.size();
    }

    public int getTargetAverageCadence() {
        return this.mTargetAverageCadence;
    }

    public int getTargetAveragePower() {
        return this.mTargetAveragePower;
    }

    public int getTargetAverageHeartrate() {
        return this.mTargetAverageHeartrate;
    }

    public float getTargetAverageSpeedKm() {
        return this.mTargetAverageSpeedKm;
    }

    public Lap getCurrentLap() {
        return this.mFinalLap;
    }

    public Lap.Split getLastSplit() {
        List<Lap.Split> splits = new ArrayList<>();
        splits.addAll(this.mSplits);
        if (splits.size() == 0) {
            return null;
        }
        return splits.get(splits.size());
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onSpeed(double speed) {
        if (shouldAddMetric()) {
            super.onSpeed(speed);
            if (this.mFinalLap != null) {
                this.mFinalLap.onSpeed(speed);
            }
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onCadence(double cadenceRPM) {
        if (shouldAddMetric()) {
            super.onCadence(cadenceRPM);
            if (this.mFinalLap != null) {
                this.mFinalLap.onCadence(cadenceRPM);
            }
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onHeartRate(int heartrate) {
        if (shouldAddMetric()) {
            super.onHeartRate(heartrate);
            if (this.mFinalLap != null) {
                this.mFinalLap.onHeartRate(heartrate);
            }
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onBikePower(double power) {
        if (shouldAddMetric()) {
            super.onBikePower(power);
            if (this.mFinalLap != null) {
                this.mFinalLap.onBikePower(power);
            }
            addNormalisedPower(power);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onOxygen(int oxygen) {
        if (shouldAddMetric()) {
            super.onOxygen(oxygen);
            if (this.mFinalLap != null) {
                this.mFinalLap.onOxygen(oxygen);
            }
        }
    }

    protected void addNormalisedPower(double power) {
        if (this.normalisedPowerCalc != null) {
            long timeElapsed = this.mTimeHelper.getTime();
            if (timeElapsed > 0) {
                double normalisePower = this.normalisedPowerCalc.calculateNormalisedPower(power, timeElapsed);
                Log.d("Normalised Power: ", String.format("%f", Double.valueOf(normalisePower)));
                if (normalisePower >= 0.0d) {
                    this.mNormPower.addValue(Double.valueOf(normalisePower));
                    this.mIntensity.addValue(Double.valueOf(normalisePower / this.mFunctionalThresholdPower));
                }
            }
        }
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onLocation(Location location) {
        if (isStartedAndRunning()) {
            this.mFinalLap.onLocation(location);
            if (location.hasAltitude()) {
                super.onAltitude((float) location.getAltitude());
            }
            this.mNumLocations++;
        }
    }

    public boolean hasLocations() {
        return this.mNumLocations >= 2;
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onAltitude(float value) {
        if (shouldAddMetric()) {
            super.onAltitude(value);
            if (this.mFinalLap != null) {
                this.mFinalLap.onAltitude(value);
            }
        }
    }

    @Override // com.kopin.solos.storage.IWorkout
    public SportType getSport() {
        return this.mSportType;
    }

    public static boolean hasData(int value) {
        return value != Integer.MIN_VALUE;
    }

    public static boolean hasData(long value) {
        return value != -2147483648L;
    }

    public static boolean hasData(float value) {
        return value != -2.14748365E9f;
    }

    public static boolean hasData(double value) {
        return value != -2.147483648E9d;
    }

    public static abstract class Header {
        private int mActivity;
        private long mActualStartTime;
        private double mDistance;
        private long mDuration;
        protected long mGhostId;
        private long mId;
        private RideMode mMode;
        private String mTitle;

        public abstract SportType getSportType();

        public Header(Cursor cursor) {
            this.mGhostId = -1L;
            this.mId = cursor.getLong(0);
            this.mMode = RideMode.values()[cursor.getInt(1)];
            this.mGhostId = cursor.getLong(2);
            this.mActualStartTime = cursor.getLong(3);
            this.mDuration = cursor.getLong(4);
            this.mDistance = cursor.getDouble(5);
            this.mTitle = cursor.getString(6);
            this.mActivity = cursor.getInt(7);
        }

        public long getId() {
            return this.mId;
        }

        public long getVirtualWorkoutId() {
            return this.mGhostId;
        }

        public long getGhostRideId() {
            if (this.mMode == RideMode.GHOST_RIDE) {
                return this.mGhostId;
            }
            return -1L;
        }

        public RideMode getMode() {
            return this.mMode;
        }

        public long getActualStartTime() {
            return this.mActualStartTime;
        }

        public long getDuration() {
            return this.mDuration;
        }

        public double getDistance() {
            return this.mDistance;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getOrGenerateTitle() {
            return (this.mTitle == null || this.mTitle.trim().isEmpty()) ? "ride_" + Utility.formatDateAndTimeCompact(this.mActualStartTime) : this.mTitle;
        }

        public boolean isGeneratedTitle() {
            return isGeneratedTitle(getOrGenerateTitle());
        }

        public static boolean isGeneratedTitle(String title) {
            int i = 0;
            if (title != null && title.startsWith("ride_") && title.length() == 17) {
                try {
                    i = Integer.parseInt(title.substring(6));
                } catch (NumberFormatException e) {
                }
            }
            return i > 0;
        }

        public double getDistanceForLocale() {
            return Utility.convertToUserUnits(1, this.mDistance / 1000.0d);
        }

        public boolean hasTitle() {
            return (this.mTitle == null || this.mTitle.trim().isEmpty()) ? false : true;
        }

        public int getActivity() {
            return this.mActivity;
        }

        public int numLaps() {
            return SQLHelper.countLaps(this.mId, true);
        }

        public String toString() {
            return "{ id: " + this.mId + ", start time: " + this.mActualStartTime + ", duration: " + this.mDuration + ", distance: " + this.mDistance + ", title: " + this.mTitle + ", bike: N/A, rider: N/Aride: " + this.mActivity + " }";
        }
    }
}
