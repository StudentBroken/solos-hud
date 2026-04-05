package com.kopin.solos.storage.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.settings.Prefs;
import java.util.Comparator;
import java.util.HashSet;

/* JADX INFO: loaded from: classes54.dex */
public class SplitHelper {
    public static final Comparator<Lap.Split> ELEVATION_COMPARATOR = new Comparator<Lap.Split>() { // from class: com.kopin.solos.storage.util.SplitHelper.1
        @Override // java.util.Comparator
        public int compare(Lap.Split lhs, Lap.Split rhs) {
            if (lhs.elevation < rhs.elevation) {
                return -1;
            }
            if (lhs.elevation == rhs.elevation) {
                return 0;
            }
            return 1;
        }
    };
    public static final long MINIMUM_LAP_TIME = 5000;
    private Context mContext;
    private int mLapCount;
    private long mLastRevs;
    private double mSplitDistance;
    private long mSplitRevs;
    private long mSplitRevsAdd;
    private long mStartRevs;
    private double mTargetDistance;
    private long mTargetTime;
    private double mTotalDistance;
    private long mTotalRevsAdd;
    private boolean mVariableLaps;
    private TimeHelper mTimeHelper = new TimeHelper(false);
    private TimeHelper mTotalTime = new TimeHelper(false);
    private volatile boolean mInit = false;
    private volatile boolean mSplitInit = false;
    private volatile boolean mUseLocation = false;
    private SplitType mType = SplitType.MANUAL;
    private final HashSet<SplitListener> mSplitListeners = new HashSet<>();

    public interface SplitListener {
        boolean isLastLap();

        void onSplit(long j, double d, long j2, boolean z);
    }

    public enum SplitUnit {
        ELEVATION,
        SPEED
    }

    public enum SplitType {
        MANUAL(Prefs.MODE_MANUAL),
        DISTANCE(Prefs.MODE_DISTANCE),
        TIME(Prefs.MODE_TIME),
        DISABLED("");

        private String mPrefValue;

        SplitType(String pref) {
            this.mPrefValue = pref;
        }

        public static SplitType fromPreference(String val) {
            for (SplitType type : values()) {
                if (type.mPrefValue.equals(val)) {
                    return type;
                }
            }
            return MANUAL;
        }
    }

    public SplitHelper(Context context) {
        this.mContext = context;
        if (context == null) {
            throw new NullPointerException("null context");
        }
    }

    public void onDistance(double metres) {
        if (this.mTotalTime.isStarted() && !this.mTotalTime.isPaused()) {
            if (!this.mInit) {
                this.mInit = true;
                this.mTotalDistance = 0.0d;
            }
            if (!this.mSplitInit) {
                this.mSplitInit = true;
                this.mSplitDistance = 0.0d;
            }
            this.mTotalDistance += metres;
            this.mSplitDistance += metres;
            checkForSplit();
        }
    }

    public void onTime(long time) {
        if (this.mTotalTime.isStarted() && !this.mTotalTime.isPaused()) {
            checkForSplit();
        }
    }

    private void checkForSplit() {
        switch (this.mType) {
            case DISTANCE:
                if (this.mTargetDistance <= getSplitDistance()) {
                    split();
                }
                break;
            case TIME:
                if (this.mTargetTime <= this.mTimeHelper.getTime() + 150) {
                    split();
                }
                break;
        }
    }

    public void buttonPress() {
        switch (this.mType) {
            case MANUAL:
                split();
                break;
        }
    }

    public double getSplitDistance() {
        return this.mSplitDistance;
    }

    public long getSplitTime() {
        return this.mTimeHelper.getTime();
    }

    public double getTotalDistance() {
        return this.mTotalDistance;
    }

    public long getTotalTime() {
        return this.mTotalTime.getTime();
    }

    public void setDefaultType() {
        setSplitType(getSplitMode(this.mContext), Prefs.getLapDistanceMeters(), getSplitTime(this.mContext));
    }

    public void setSplitType(SplitType type, double targetDistance, long targetTime) {
        this.mType = type;
        this.mTargetDistance = targetDistance;
        this.mTargetTime = targetTime;
    }

    public void start(boolean variableLaps) {
        if (!this.mTotalTime.isStarted()) {
            this.mVariableLaps = variableLaps;
            this.mTimeHelper.start();
            this.mTotalTime.start();
        }
    }

    public void pause() {
        if (!this.mTotalTime.isPaused()) {
            this.mTimeHelper.pause();
            this.mTotalTime.pause();
            this.mTotalRevsAdd += this.mLastRevs - this.mStartRevs;
            this.mSplitRevsAdd += this.mLastRevs - this.mSplitRevs;
        }
    }

    public void resume() {
        if (this.mTotalTime.isPaused()) {
            this.mTimeHelper.resume();
            this.mTotalTime.resume();
        }
    }

    public void split() {
        if (this.mTotalTime.isStarted() && this.mTimeHelper.getTime() > 5000) {
            boolean isLasLap = false;
            for (SplitListener listener : this.mSplitListeners) {
                isLasLap |= listener.isLastLap();
            }
            if (isLasLap) {
                stop(true);
                return;
            }
            this.mLapCount++;
            switch (this.mType) {
                case TIME:
                    sendSplit(this.mTimeHelper.getStartTimestamp(), getSplitDistance(), this.mTargetTime, false);
                    break;
                default:
                    sendSplit(this.mTimeHelper.getStartTimestamp(), getSplitDistance(), this.mTimeHelper.getTime(), false);
                    break;
            }
            this.mTimeHelper.reset();
            this.mTimeHelper.start();
            this.mSplitInit = false;
        }
    }

    public void stop(boolean autoFinish) {
        long splitTime;
        if (this.mTotalTime.isStarted()) {
            switch (this.mType) {
                case TIME:
                    if (autoFinish) {
                        splitTime = this.mTargetTime;
                    } else if (this.mVariableLaps) {
                        splitTime = this.mTimeHelper.getTime();
                    } else {
                        splitTime = this.mTotalTime.getTime() - (this.mTargetTime * ((long) this.mLapCount));
                    }
                    sendSplit(this.mTimeHelper.getStartTimestamp(), getSplitDistance(), splitTime, true);
                    break;
                default:
                    sendSplit(this.mTimeHelper.getStartTimestamp(), getSplitDistance(), this.mTimeHelper.getTime(), true);
                    break;
            }
            this.mTimeHelper.stop();
            this.mTotalTime.stop();
        }
    }

    public void reset() {
        this.mTimeHelper.reset();
        this.mTotalTime.reset();
        this.mInit = false;
        this.mSplitInit = false;
        this.mStartRevs = 0L;
        this.mSplitRevs = 0L;
        this.mLastRevs = 0L;
        this.mTotalRevsAdd = 0L;
        this.mSplitRevsAdd = 0L;
        this.mTotalDistance = 0.0d;
        this.mSplitDistance = 0.0d;
        this.mLapCount = 0;
        this.mVariableLaps = false;
    }

    public void addSplitListener(SplitListener splitListener) {
        synchronized (this.mSplitListeners) {
            this.mSplitListeners.add(splitListener);
        }
    }

    public void removeSplitListener(SplitListener splitListener) {
        synchronized (this.mSplitListeners) {
            this.mSplitListeners.remove(splitListener);
        }
    }

    private void sendSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
        synchronized (this.mSplitListeners) {
            for (SplitListener splitListener : this.mSplitListeners) {
                splitListener.onSplit(startTime, splitDistance, splitTime, isEnd);
            }
        }
    }

    public SplitType getType() {
        return this.mType;
    }

    public static SplitType getSplitMode(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = sharedPref.getString(Prefs.LAP_MODE_SETTING, Prefs.MODE_MANUAL);
        return SplitType.fromPreference(mode);
    }

    public static SplitUnit getSplitUnit(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = sharedPref.getString(Prefs.SPLIT_UNIT, Prefs.UNIT_SPEED);
        switch (mode) {
            case "e":
                return SplitUnit.ELEVATION;
            case "s":
                return SplitUnit.SPEED;
            default:
                return SplitUnit.SPEED;
        }
    }

    public static long getSplitTime(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getLong(Prefs.LAP_TIME, 600000L);
    }

    public static void splitInit(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putLong(Prefs.LAP_TIME, getSplitTime(context)).apply();
    }
}
