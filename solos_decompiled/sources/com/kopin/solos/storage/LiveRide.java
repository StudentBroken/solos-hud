package com.kopin.solos.storage;

import android.location.Location;
import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes54.dex */
public class LiveRide {
    public static final int FTP_TIME = 1200000;
    private static final int FTP_TIME_TEST_SHORT = 60000;
    private static final String TAG = "LiveRide";
    private static int mCalories;
    private static SavedWorkout mLastWorkout;
    private static RideRecorder mRecorder;
    private static SplitHelper mSplitHelper;
    private static TimeHelper mTimeHelper;
    private static Workout sWorkout;
    private static Workout.RideMode mMode = Workout.RideMode.NORMAL;
    private static long mVirtualWorkoutId = -1;
    private static Lap.Saved mLastLap = null;
    private static SportType sportType = SportType.DEFAULT_TYPE;
    private static final DataListener mDataListener = new DataListener() { // from class: com.kopin.solos.storage.LiveRide.1
        @Override // com.kopin.solos.storage.DataListener
        public void onAltitude(float value) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onAltitude(value);
            }
            LiveRide.mRecorder.onAltitude(value);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onBikePower(double power) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onBikePower(power);
            }
            LiveRide.mRecorder.onBikePower(power);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onButtonPress(Sensor.ButtonAction action) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onButtonPress(action);
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onCadence(double cadenceRPM) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onCadence(cadenceRPM);
            }
            LiveRide.mRecorder.onCadence(cadenceRPM);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onHeartRate(int heartrate) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onHeartRate(heartrate);
            }
            LiveRide.mRecorder.onHeartRate(heartrate);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onLocation(Location location) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onLocation(location);
            }
            LiveRide.mRecorder.onLocation(location);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onSpeed(double speed) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onSpeed(speed);
            }
            LiveRide.mRecorder.onSpeed(speed);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onDistance(double metres) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onDistance(metres);
            }
            LiveRide.mRecorder.onDistance(metres);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onOxygen(int oxygen) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onOxygen(oxygen);
            }
            LiveRide.mRecorder.onOxygen(oxygen);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onStride(double stride) {
            if (LiveRide.sWorkout != null) {
                LiveRide.sWorkout.onStride(stride);
            }
            LiveRide.mRecorder.onStride(stride);
        }
    };

    public static void init(TimeHelper timeHelper, SplitHelper splitHelper) {
        mLastLap = null;
        mTimeHelper = timeHelper;
        mSplitHelper = splitHelper;
        refreshSplitType();
        mRecorder = new RideRecorder(mTimeHelper);
        sportType = Prefs.getSportType();
    }

    public static void reset() {
        mLastLap = null;
        mLastWorkout = null;
        mCalories = 0;
    }

    public static void clear() {
        mMode = Workout.RideMode.NORMAL;
        mCalories = 0;
        mVirtualWorkoutId = -1L;
        mLastLap = null;
        sportType = SportType.DEFAULT_TYPE;
    }

    public static void setSport(SportType type) {
        sportType = type;
        Prefs.setSportType(type);
    }

    public static SportType getCurrentSport() {
        return sportType;
    }

    public static DataListener getDataListener() {
        return mDataListener;
    }

    public static boolean isActiveRide() {
        return sWorkout != null;
    }

    public static boolean isActiveFtp() {
        return sWorkout != null && mMode == Workout.RideMode.FTP;
    }

    public static IWorkout getCurrentRide() {
        return sWorkout;
    }

    public static boolean isStartedAndRunning() {
        if (sWorkout != null) {
            return sWorkout.shouldAddMetric();
        }
        return false;
    }

    public static void setLastLap(Lap.Saved lastLap) {
        mLastLap = lastLap;
    }

    public static Lap.Saved getLastLap() {
        return mLastLap;
    }

    public static boolean isFunctionalThresholdPowerMode() {
        return mMode == Workout.RideMode.FTP;
    }

    public static boolean isNavigtionRideMode() {
        return mMode == Workout.RideMode.ROUTE;
    }

    public static boolean isGhostWorkout() {
        return mMode == Workout.RideMode.GHOST_RIDE && mVirtualWorkoutId != -1;
    }

    public static boolean isVirtualWorkout() {
        return mMode == Workout.RideMode.TRAINING;
    }

    public static Workout.RideMode getMode() {
        return mMode;
    }

    public static void setMode(Workout.RideMode mode, long id) {
        mMode = mode;
        mVirtualWorkoutId = id;
    }

    public static void resetMode() {
        mMode = Workout.RideMode.NORMAL;
        mVirtualWorkoutId = -1L;
    }

    public static long getVirtualWorkoutId() {
        return mVirtualWorkoutId;
    }

    public static void start(Workout.RideMode mode) {
        if (mTimeHelper != null) {
            mTimeHelper.reset();
            mTimeHelper.start();
        }
        if (mSplitHelper != null) {
            mSplitHelper.reset();
            refreshSplitType();
        }
        if (sWorkout == null) {
            sWorkout = sportType == SportType.RIDE ? new Ride(mTimeHelper, mSplitHelper, mMode, mVirtualWorkoutId) : new Run(mTimeHelper, mSplitHelper, mMode, mVirtualWorkoutId);
            mSplitHelper.addSplitListener(sWorkout);
        }
        if (mSplitHelper != null) {
            mSplitHelper.start(mMode == Workout.RideMode.TRAINING);
        }
        Log.i("LiveRide start", "");
        mSplitHelper.addSplitListener(mRecorder);
        mRecorder.start(sWorkout.getId(), sWorkout.getRouteId());
    }

    public static void pause() {
        if (mTimeHelper != null) {
            mTimeHelper.pause();
        }
        if (mSplitHelper != null) {
            mSplitHelper.pause();
        }
        if (mRecorder != null) {
            mRecorder.pause();
        }
    }

    public static void resume() {
        if (mTimeHelper != null) {
            mTimeHelper.resume();
        }
        if (mSplitHelper != null) {
            mSplitHelper.resume();
        }
        if (mRecorder != null) {
            mRecorder.resume();
        }
    }

    public static SavedWorkout stop() {
        if (mTimeHelper != null) {
            mTimeHelper.stop();
        }
        mLastWorkout = null;
        if (sWorkout != null) {
            if (mSplitHelper != null) {
                mSplitHelper.stop(false);
                mSplitHelper.removeSplitListener(sWorkout);
            }
            mLastWorkout = sWorkout.end();
            sWorkout = null;
            mMode = Workout.RideMode.NORMAL;
            mSplitHelper.removeSplitListener(mRecorder);
            mRecorder.end();
        }
        return mLastWorkout;
    }

    public static SavedWorkout lastRide() {
        return mLastWorkout;
    }

    public static int getCurrentCalories() {
        if (sWorkout == null) {
            return Integer.MIN_VALUE;
        }
        int calories = sWorkout.getCalories();
        if (mCalories < calories) {
            mCalories = calories;
        }
        return mCalories;
    }

    public static double getPowerNormalised() {
        if (sWorkout != null) {
            return sWorkout.getPowerNormalised();
        }
        return -2.147483648E9d;
    }

    public static double getAverageCadence() {
        if (sWorkout != null) {
            return sWorkout.getAverageCadence();
        }
        return -2.147483648E9d;
    }

    public static double getAverageHeartRate() {
        if (sWorkout != null) {
            return sWorkout.getAverageHeartrate();
        }
        return -2.147483648E9d;
    }

    public static double getAveragePower() {
        if (sWorkout != null) {
            return sWorkout.getAveragePower();
        }
        return -2.147483648E9d;
    }

    public static double getAverageSpeed() {
        if (sWorkout == null) {
            return -2.147483648E9d;
        }
        if (sWorkout.getTotalTime() > 1000) {
            return sWorkout.getDistance() / (sWorkout.getTotalTime() / 1000);
        }
        return 0.0d;
    }

    public static double getAveragePace() {
        double avgSpeed = getAverageSpeed();
        return (avgSpeed == 0.0d || avgSpeed == -2.147483648E9d) ? avgSpeed : 1.0d / avgSpeed;
    }

    public static double getTrueAverageSpeed() {
        if (sWorkout != null) {
            return sWorkout.getAverageSpeed();
        }
        return -2.147483648E9d;
    }

    public static int getAverageOxygen() {
        if (sWorkout != null) {
            return sWorkout.getAverageOxygen();
        }
        return Integer.MIN_VALUE;
    }

    public static int getCurrentHeartRateZone() {
        if (sWorkout != null) {
            return sWorkout.getCurrentHeartRateZone();
        }
        return 0;
    }

    public static double getOverallClimb() {
        if (sWorkout != null) {
            return sWorkout.getOverallClimb();
        }
        return -2.147483648E9d;
    }

    public static double getAverageStride() {
        if (sWorkout != null) {
            return sWorkout.getAverageStride();
        }
        return -2.147483648E9d;
    }

    public static Lap.Split getLastLapSplit() {
        if (sWorkout != null) {
            return sWorkout.getLastSplit();
        }
        return null;
    }

    public static void refreshSplitType() {
        if (mSplitHelper != null) {
            if (isFunctionalThresholdPowerMode()) {
                mSplitHelper.setSplitType(SplitHelper.SplitType.TIME, -1.0d, getTargetTime());
            } else {
                mSplitHelper.setDefaultType();
            }
        }
    }

    public static void split() {
        if (mSplitHelper != null) {
            mSplitHelper.buttonPress();
        }
    }

    public static void setSplitType(SplitHelper.SplitType type, double distance, long time) {
        if (mSplitHelper != null) {
            mSplitHelper.setSplitType(type, distance, time);
        }
    }

    public static int getSplitCount() {
        if (sWorkout != null) {
            return sWorkout.getSplitCount();
        }
        return 0;
    }

    public static ArrayList<Lap.Split> getSplits() {
        return sWorkout != null ? sWorkout.getSplits() : new ArrayList<>();
    }

    public static double getSplitDistance() {
        if (sWorkout != null) {
            return sWorkout.getSplitDistance();
        }
        return -2.147483648E9d;
    }

    public static long getSplitTime() {
        if (sWorkout != null) {
            return sWorkout.getSplitTime();
        }
        return -2147483648L;
    }

    public static boolean isStarted() {
        if (sWorkout == null || mTimeHelper == null) {
            return false;
        }
        return mTimeHelper.isStarted();
    }

    public static boolean isPaused() {
        if (sWorkout == null || mTimeHelper == null) {
            return false;
        }
        return mTimeHelper.isPaused();
    }

    public static long getTime() {
        if (mTimeHelper != null) {
            return mTimeHelper.getTime();
        }
        return 0L;
    }

    public static long getCountdownTime() {
        if (mTimeHelper != null) {
            return getTargetTime() - mTimeHelper.getTime();
        }
        return 0L;
    }

    public static double getDistance() {
        if (sWorkout != null) {
            return sWorkout.getDistance();
        }
        return -2.147483648E9d;
    }

    public static double getDistanceForLocale() {
        if (sWorkout != null) {
            return Utility.convertToUserUnits(1, getDistance() / 1000.0d);
        }
        return -2.147483648E9d;
    }

    public static double getAverageSpeedForLocale() {
        if (sWorkout != null) {
            return Conversion.speedForLocale(getAverageSpeed());
        }
        return -2.147483648E9d;
    }

    public static float getOverallClimbForLocale() {
        if (sWorkout != null) {
            return (float) Utility.convertToUserUnits(0, getOverallClimb());
        }
        return -2.14748365E9f;
    }

    public static double getAverageStrideLocale() {
        if (sWorkout != null) {
            return Conversion.strideForLocale(getAverageStride());
        }
        return -2.147483648E9d;
    }

    public static double getAveragePaceLocale() {
        if (sWorkout != null) {
            return Conversion.speedToPaceForLocale(getAverageSpeed());
        }
        return -2.147483648E9d;
    }

    public static double getLapAverageSpeedForLocale() {
        if (sWorkout != null) {
            return Conversion.speedForLocale(sWorkout.getCurrentLap().getAverageSpeed());
        }
        return -2.147483648E9d;
    }

    public static double getLapAveragePaceForLocale() {
        if (sWorkout != null) {
            return Conversion.speedToPaceForLocale(sWorkout.getCurrentLap().getAverageSpeed());
        }
        return -2.147483648E9d;
    }

    public static double getLapAveragePower() {
        if (sWorkout != null) {
            return sWorkout.getCurrentLap().getAveragePower();
        }
        return -2.147483648E9d;
    }

    public static double getLapAverageStride() {
        if (sWorkout != null) {
            return Conversion.strideForLocale(sWorkout.getCurrentLap().getAverageStride());
        }
        return -2.147483648E9d;
    }

    public static double getLapAverageHeartRate() {
        if (sWorkout != null) {
            return sWorkout.getCurrentLap().getAverageHeartrate();
        }
        return -2.147483648E9d;
    }

    public static double getLapAverageCadence() {
        if (sWorkout != null) {
            return sWorkout.getCurrentLap().getAverageCadence();
        }
        return -2.147483648E9d;
    }

    public static double getLapAverageSpeed() {
        if (sWorkout != null) {
            return sWorkout.getCurrentLap().getAverageSpeed();
        }
        return -2.147483648E9d;
    }

    public static long getLapDuration() {
        if (sWorkout != null) {
            return sWorkout.getSplitTime();
        }
        return -2147483648L;
    }

    public static double getLapDistance() {
        if (sWorkout != null) {
            return sWorkout.getSplitDistance();
        }
        return -2.147483648E9d;
    }

    public static double getLapDistanceForLocale() {
        if (sWorkout != null) {
            return Conversion.distanceForLocale(sWorkout.getSplitDistance());
        }
        return -2.147483648E9d;
    }

    public static long getTargetTime() {
        if (isFunctionalThresholdPowerMode()) {
            return Config.SHORT_FTP ? 60000 : FTP_TIME;
        }
        return Prefs.getTargetRideTimeInMillis();
    }
}
