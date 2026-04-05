package com.kopin.solos.storage.util;

/* JADX INFO: loaded from: classes54.dex */
public class TimeHelper {
    public static final int HOUR = 3600000;
    public static final int MINUTE = 60000;
    public static final String ZERO = "00:00:00";
    private long mActualStartTimestamp;
    private long mLastTime;
    private volatile boolean mPaused;
    private long mPreviousDuration;
    private long mStartTimestamp;
    private volatile boolean mStarted;
    private boolean mUseLogger;

    public TimeHelper() {
        this(true);
    }

    public TimeHelper(boolean useLogger) {
        this.mPreviousDuration = 0L;
        this.mLastTime = 0L;
        this.mStartTimestamp = 0L;
        this.mActualStartTimestamp = 0L;
        this.mStarted = false;
        this.mUseLogger = useLogger;
    }

    public synchronized void start() {
        if (!this.mStarted) {
            this.mStarted = true;
            this.mPreviousDuration = 0L;
            this.mLastTime = Utility.getTimeMilliseconds();
            this.mStartTimestamp = 0L;
            this.mActualStartTimestamp = Utility.getActualDateTimeMilliseconds();
            this.mPaused = false;
        }
    }

    public synchronized void pause() {
        if (this.mStarted && !this.mPaused) {
            this.mPaused = true;
            this.mPreviousDuration += Utility.getTimeMilliseconds() - this.mLastTime;
            this.mLastTime = 0L;
        }
    }

    public synchronized void resume() {
        if (this.mStarted && this.mPaused) {
            this.mPaused = false;
            this.mLastTime = Utility.getTimeMilliseconds();
        }
    }

    public synchronized void stop() {
        if (this.mStarted) {
            if (!this.mPaused) {
                this.mPreviousDuration += Utility.getTimeMilliseconds() - this.mLastTime;
            }
            this.mStarted = false;
            this.mPaused = false;
            this.mLastTime = 0L;
        }
    }

    public synchronized void reset() {
        if (this.mStarted || this.mPaused || this.mLastTime != 0 || this.mPreviousDuration != 0) {
            this.mPreviousDuration = 0L;
            this.mLastTime = 0L;
            this.mStartTimestamp = 0L;
            this.mActualStartTimestamp = 0L;
            this.mPaused = false;
            this.mStarted = false;
        }
    }

    public long getStartTimestamp() {
        return this.mStartTimestamp;
    }

    public long getActualRideStartTimestamp() {
        return this.mActualStartTimestamp;
    }

    public synchronized long getTime() {
        long timeMilliseconds;
        if (!this.mStarted || this.mPaused) {
            timeMilliseconds = this.mPreviousDuration;
        } else {
            timeMilliseconds = this.mPreviousDuration + (Utility.getTimeMilliseconds() - this.mLastTime);
        }
        return timeMilliseconds;
    }

    public String getTimeAsString() {
        return Utility.formatTime(getTime());
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public static String timeToString(long time) {
        int hours = 0;
        int mins = 0;
        if (time >= 3600000) {
            hours = (int) (time / 3600000);
            time %= 3600000;
        }
        if (time >= 60000) {
            mins = (int) (time / 60000);
            time %= 60000;
        }
        int secs = time >= 1000 ? (int) (time / 1000) : 0;
        return String.format("%02d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(mins), Integer.valueOf(secs));
    }
}
