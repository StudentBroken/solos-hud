package com.kopin.solos.virtualworkout;

import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.notifications.TargetNotification;
import com.kopin.solos.notifications.TargetNotifier;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class GhostWorkout extends VirtualWorkout {
    private static final long GHOST_SHOW_RESULT_PERIOD = 10000;
    private static final int GHOST_UPDATE_PERIOD = 1000;
    private static final String TAG = "GhostWorkout";
    private double ghostDistance;
    private long ghostResultPageStart;
    private boolean isActive;
    long lapId;
    private Metrics.FloatMetric mCadence;
    private Record mCurrentData;
    private double mDistanceByTime;
    private double mDistanceDifference;
    private final Handler mGhostHandler;
    private final Runnable mGhostRunner;
    private Metrics.IntegerMetric mHeartRate;
    private boolean mLapMode;
    private Metrics.IntegerMetric mOxygen;
    private Metrics.FloatMetric mPower;
    private long mRideId;
    private Metrics.FloatMetric mSpeed;
    private Metrics.FloatMetric mStride;
    private long mTTFirstTs;
    private long mTimeDifference;
    private Cursor mTimeLaps;
    private Cursor mTimeRecords;

    public GhostWorkout(AppService service, VirtualWorkout.WorkoutObserver obs, long rideId, boolean isLapGhostMode) {
        super(service, obs);
        this.mTimeLaps = null;
        this.mTimeRecords = null;
        this.mCurrentData = new Record(0L);
        this.mDistanceByTime = 0.0d;
        this.mCadence = new Metrics.FloatMetric();
        this.mHeartRate = new Metrics.IntegerMetric();
        this.mSpeed = new Metrics.FloatMetric();
        this.mPower = new Metrics.FloatMetric();
        this.mOxygen = new Metrics.IntegerMetric();
        this.mStride = new Metrics.FloatMetric();
        this.mTTFirstTs = -1L;
        this.lapId = -1L;
        this.ghostResultPageStart = 0L;
        this.ghostDistance = 0.0d;
        this.isActive = false;
        this.mGhostHandler = new Handler();
        this.mGhostRunner = new Runnable() { // from class: com.kopin.solos.virtualworkout.GhostWorkout.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (GhostWorkout.this.mGhostHandler) {
                    if (!LiveRide.isPaused() && LiveRide.isStartedAndRunning()) {
                        Lap.Saved lastLap = LiveRide.getLastLap();
                        long time = (!GhostWorkout.this.mLapMode || lastLap == null) ? LiveRide.getTime() : LiveRide.getTime() - (lastLap.getEndTime() - lastLap.getStartTime());
                        GhostWorkout.this.travelTime(time);
                        Log.d(GhostWorkout.TAG, "ghost active: " + GhostWorkout.this.hasData() + ", distance: " + GhostWorkout.this.mDistanceByTime);
                        double currentDistance = LiveRide.getDistance();
                        if (GhostWorkout.this.ghostResultPageStart == 0) {
                            GhostWorkout.this.mDistanceDifference = currentDistance - GhostWorkout.this.mDistanceByTime;
                            double speed = GhostWorkout.this.mAppService.getHardwareReceiverService().getLastSpeed();
                            if (speed <= 0.0d) {
                                speed = LiveRide.getAverageSpeed();
                            }
                            if (speed > 0.0d) {
                                GhostWorkout.this.mTimeDifference = (long) (GhostWorkout.this.mDistanceDifference / speed);
                            } else {
                                GhostWorkout.this.mTimeDifference = LiveRide.getTime() / 1000;
                            }
                            Log.d(GhostWorkout.TAG, "Ghost is " + (GhostWorkout.this.isAhead() ? "BEHIND" : "AHEAD") + ", " + GhostWorkout.this.mDistanceDifference + " metres, " + GhostWorkout.this.mTimeDifference + " seconds");
                        }
                        if (!GhostWorkout.this.hasData() || (GhostWorkout.this.ghostDistance > 0.0d && currentDistance >= GhostWorkout.this.ghostDistance)) {
                            if (GhostWorkout.this.ghostResultPageStart == 0) {
                                GhostWorkout.this.ghostResultPageStart = System.currentTimeMillis();
                                PageNav.showPage(MetricType.GHOST_STATS.getResource());
                                GhostWorkout.this.close();
                                if (GhostWorkout.this.isAhead()) {
                                    Log.i(GhostWorkout.TAG, "ghost result: distance reached, rider won.");
                                    long timeRaw = Math.abs(GhostWorkout.this.getTimeDiff());
                                    String timeStr = timeRaw < 60 ? String.format(GhostWorkout.this.mAppService.getString(R.string.tts_format_time_seconds), Integer.valueOf((int) timeRaw)) : String.format(GhostWorkout.this.mAppService.getString(R.string.tts_format_time_minutes_seconds), Integer.valueOf((int) (timeRaw / 60)), Integer.valueOf((int) (timeRaw % 60)));
                                    GhostWorkout.this.mAppService.speakText(AppService.NOTIFICATION_TTS, String.format(GhostWorkout.this.mAppService.getString(R.string.tts_ghost_result_ahead), timeStr));
                                } else {
                                    Log.i(GhostWorkout.TAG, "ghost result: distance reached, rider lost!");
                                    double distanceRaw = Math.abs(GhostWorkout.this.getDistanceDiff());
                                    String distStr = distanceRaw > 250.0d ? String.format("%.2f %s", Double.valueOf(Conversion.distanceForLocale(distanceRaw)), Conversion.getUnitOfDistanceTTS(GhostWorkout.this.mAppService)) : String.format("%.0f %s", Double.valueOf(Conversion.distanceSmallForLocale(distanceRaw)), Conversion.getUnitOfDistanceSmallTTS(GhostWorkout.this.mAppService));
                                    GhostWorkout.this.mAppService.speakText(AppService.NOTIFICATION_TTS, String.format(GhostWorkout.this.mAppService.getString(R.string.tts_ghost_result_behind), distStr));
                                }
                            } else if (GhostWorkout.this.ghostResultPageStart + 10000 < System.currentTimeMillis()) {
                                Log.i(GhostWorkout.TAG, "ghost result - stop showing, resume screens ");
                                GhostWorkout.this.ghostDistance = 0.0d;
                                GhostWorkout.this.ghostResultPageStart = 0L;
                                GhostWorkout.this.workoutComplete();
                                PageNav.nextPage();
                                return;
                            }
                        }
                        GhostWorkout.this.mGhostHandler.postDelayed(GhostWorkout.this.mGhostRunner, 1000L);
                    } else {
                        Log.d(GhostWorkout.TAG, "Pausing Ghost");
                        GhostWorkout.this.isActive = false;
                    }
                }
            }
        };
        this.mRideId = rideId;
        this.mLapMode = isLapGhostMode;
        this.mTimeLaps = Lap.getCursor(this.mRideId);
        this.mTimeRecords = nextLap(this.mTimeLaps);
        this.mCurrentData = new Record(0L);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void start(TargetNotifier notifier) {
        super.start(notifier);
        this.ghostDistance = 0.0d;
        this.ghostResultPageStart = 0L;
        this.mDistanceDifference = 0.0d;
        this.mTimeDifference = 0L;
        getGhostDistance();
        resumeGhostRunner();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void pause() {
        super.pause();
        stopGhostRunner();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void resume() {
        super.resume();
        resumeGhostRunner();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void stop() {
        super.stop();
        this.ghostResultPageStart = 0L;
        stopGhostRunner();
        close();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public boolean canNotify() {
        return false;
    }

    public long getId() {
        return this.mRideId;
    }

    public boolean hasData() {
        return this.mTimeRecords != null;
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public boolean isWorkoutComplete() {
        return !hasData();
    }

    public boolean isAhead() {
        return getDistanceDiff() >= 0.0d;
    }

    public double getDistanceDiff() {
        return this.mDistanceDifference;
    }

    public long getTimeDiff() {
        return this.mTimeDifference;
    }

    public boolean isLapMode() {
        return this.mLapMode;
    }

    private void resumeGhostRunner() {
        synchronized (this.mGhostHandler) {
            if (!this.isActive) {
                Log.d(TAG, "Resuming Ghost");
                this.mGhostHandler.postDelayed(this.mGhostRunner, 100L);
                this.isActive = true;
            }
        }
    }

    private void stopGhostRunner() {
    }

    private void getGhostDistance() {
        SavedWorkout workout = SavedRides.getWorkout(LiveRide.getCurrentSport(), this.mRideId);
        if (workout != null) {
            this.ghostDistance = workout.getDistance();
        }
    }

    public void travelTime(long timestamp) {
        Lap.Saved lap = LiveRide.getLastLap();
        if (this.mLapMode && lap != null) {
            this.lapId = lap.getId();
            this.mTimeRecords = lap.getRecordsCursor();
        }
        if (this.mTTFirstTs == -1 || this.mTTFirstTs <= timestamp) {
            Boolean found = false;
            while (this.mTimeRecords != null) {
                while (true) {
                    if (!this.mTimeRecords.moveToNext()) {
                        break;
                    }
                    Record record = new Record(this.mTimeRecords);
                    if (this.mTTFirstTs == -1) {
                        this.mTTFirstTs = record.getTimestamp();
                    }
                    if (record.getTimestamp() > timestamp) {
                        this.mTimeRecords.moveToPrevious();
                        found = true;
                        break;
                    }
                    if (record.hasDistance()) {
                        this.mDistanceByTime += record.getDistance();
                    }
                    if (record.hasHeartrate()) {
                        this.mCurrentData.setHeartrate(record.getHeartrate());
                        this.mHeartRate.addValue(Integer.valueOf(record.getHeartrate()));
                    }
                    if (record.hasSpeed()) {
                        Log.d(TAG, "travelTime getSpeed " + record.getSpeed());
                        this.mCurrentData.setSpeed(record.getSpeed());
                        this.mSpeed.addValue(Float.valueOf((float) record.getSpeed()));
                    }
                    if (record.hasCadence()) {
                        this.mCurrentData.setCadence(record.getCadence());
                        this.mCadence.addValue(Float.valueOf((float) record.getCadence()));
                    }
                    if (record.hasPower()) {
                        this.mCurrentData.setPower(record.getPower());
                        this.mPower.addValue(Float.valueOf((float) record.getPower()));
                    }
                    if (record.hasOxygen()) {
                        this.mCurrentData.setOxygen(record.getOxygen());
                        this.mOxygen.addValue(Integer.valueOf(record.getOxygen()));
                    }
                    if (record.hasStride()) {
                        this.mCurrentData.setStride(record.getStride());
                        this.mStride.addValue(Float.valueOf((float) record.getStride()));
                    }
                }
                if (found.booleanValue()) {
                    break;
                }
                if (this.mTimeRecords != null) {
                    this.mTimeRecords.close();
                }
                Lap.Saved lp = LiveRide.getLastLap();
                if (!this.mLapMode || lp == null) {
                    this.mTimeRecords = nextLap(this.mTimeLaps);
                } else if (lp.getId() != this.lapId) {
                    this.lapId = lp.getId();
                    this.mTimeRecords = lp.getRecordsCursor();
                } else {
                    this.mTimeRecords = null;
                    this.mCurrentData.setHeartrate(Integer.MIN_VALUE);
                    this.mCurrentData.setSpeed(-2.147483648E9d);
                    this.mCurrentData.setCadence(-2.147483648E9d);
                    this.mCurrentData.setPower(-2.147483648E9d);
                    this.mCurrentData.setStride(-2.147483648E9d);
                }
            }
            if (!hasData()) {
            }
        }
    }

    private Cursor nextLap(Cursor lap) {
        if (lap.moveToNext()) {
            Lap.Saved lapInfo = new Lap.Saved(lap);
            return lapInfo.getRecordsCursor();
        }
        this.mCurrentData.setHeartrate(Integer.MIN_VALUE);
        this.mCurrentData.setSpeed(-2.147483648E9d);
        this.mCurrentData.setCadence(-2.147483648E9d);
        this.mCurrentData.setPower(-2.147483648E9d);
        this.mCurrentData.setStride(-2.147483648E9d);
        return null;
    }

    public void close() {
        if (this.mTimeLaps != null) {
            this.mTimeLaps.close();
        }
        this.mTimeLaps = null;
        if (this.mTimeRecords != null) {
            this.mTimeRecords.close();
        }
        this.mTimeRecords = null;
    }

    public double getDistance() {
        return this.mDistanceByTime;
    }

    public double getDistanceForLocale() {
        return Utility.convertToUserUnits(1, this.mDistanceByTime / 1000.0d);
    }

    public double getSpeedForLocale() {
        return Conversion.speedForLocale(this.mCurrentData.getSpeed());
    }

    public double getAverageSpeedForLocale() {
        float ave = this.mSpeed.getAverage().floatValue();
        if (ave == -2.14748365E9f) {
            return -2.147483648E9d;
        }
        return Conversion.speedForLocale(ave);
    }

    public double getStrideForLocale() {
        return Conversion.strideForLocale(this.mCurrentData.getStride());
    }

    public double getPaceForLocale() {
        return Conversion.speedToPaceForLocale(this.mCurrentData.getSpeed());
    }

    public double getAveragePaceForLocale() {
        return Conversion.speedToPaceForLocale(this.mSpeed.getAverage().floatValue());
    }

    public int getHeartRate() {
        return this.mCurrentData.getHeartrate();
    }

    public double getSpeed() {
        return this.mCurrentData.getSpeed();
    }

    public double getCadence() {
        return this.mCurrentData.getCadence();
    }

    public double getPower() {
        return this.mCurrentData.getPower();
    }

    public int getOxygen() {
        return this.mCurrentData.getOxygen();
    }

    public double getAverageCadence() {
        return this.mCadence.getAverage().floatValue();
    }

    public double getAverageHeartRate() {
        return this.mHeartRate.getAverage().intValue();
    }

    public double getAverageSpeed() {
        return this.mSpeed.getAverage().floatValue();
    }

    public double getAveragePower() {
        return this.mPower.getAverage().floatValue();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public double getLiveData(MetricType metricType) {
        switch (metricType) {
            case DISTANCE:
                return this.mAppService.getHardwareReceiverService().getLastDistanceForLocale();
            case CADENCE:
            case STEP:
                return (int) this.mAppService.getHardwareReceiverService().getLastCadence();
            case GHOST_AVERAGE_CADENCE:
            case GHOST_AVERAGE_STEP:
                return (int) LiveRide.getAverageCadence();
            case POWER:
            case KICK:
                return (int) this.mAppService.getHardwareReceiverService().getLastPower();
            case GHOST_AVERAGE_POWER:
            case GHOST_AVERAGE_KICK:
                return (int) LiveRide.getAveragePower();
            case HEARTRATE:
                return this.mAppService.getHardwareReceiverService().getLastHeartRate();
            case GHOST_AVERAGE_HEARTRATE:
                return (int) LiveRide.getAverageHeartRate();
            case SPEED:
                return this.mAppService.getHardwareReceiverService().getLastSpeedForLocale();
            case GHOST_AVERAGE_SPEED:
                return LiveRide.getAverageSpeedForLocale();
            case PACE:
                return this.mAppService.getHardwareReceiverService().getLastPaceForLocale();
            case GHOST_AVERAGE_PACE:
                return LiveRide.getAveragePaceLocale();
            case STRIDE:
                return this.mAppService.getHardwareReceiverService().getLastStrideForLocale();
            default:
                return super.getLiveData(metricType);
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public Target getTarget(MetricType metricType) {
        switch (metricType) {
            case DISTANCE:
                return Target.createTargetAbove(Conversion.distanceForLocale(this.mDistanceByTime));
            case CADENCE:
            case STEP:
                return Target.createTargetAbove((int) this.mCurrentData.getCadence());
            case GHOST_AVERAGE_CADENCE:
            case GHOST_AVERAGE_STEP:
                return Target.createTargetAbove(this.mCadence.getAverage().intValue());
            case POWER:
            case KICK:
                return Target.createTargetAbove((int) this.mCurrentData.getPower());
            case GHOST_AVERAGE_POWER:
            case GHOST_AVERAGE_KICK:
                return Target.createTargetAbove(this.mPower.getAverage().intValue());
            case HEARTRATE:
                return Target.createTargetAbove(this.mCurrentData.getHeartrate());
            case GHOST_AVERAGE_HEARTRATE:
                return Target.createTargetAbove(this.mHeartRate.getAverage().intValue());
            case SPEED:
                return Target.createTargetAbove(this.mCurrentData.getSpeedForLocale());
            case GHOST_AVERAGE_SPEED:
                return Target.createTargetAbove(Conversion.speedForLocale(this.mSpeed.getAverage().doubleValue()));
            case PACE:
                return Target.createTargetBelow(getPaceForLocale());
            case GHOST_AVERAGE_PACE:
                return Target.createTargetBelow(getAveragePaceForLocale());
            case STRIDE:
                return Target.createTargetAbove(getStrideForLocale());
            default:
                return super.getTarget(metricType);
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public TargetNotification getNotification() {
        return new TargetNotification(0, R.string.ghost_notification_speech);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public List<MetricType> getTTSMetrics() {
        return Prefs.getSingleMetricGhostChoices();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public int[] getHeadsetPages() {
        return new int[]{R.xml.single_metric_ghost, R.xml.time_ghost};
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public PageNav.PageMode getHeadsetPageMode() {
        return PageNav.PageMode.GHOST;
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public String getHeadsetPageId() {
        return "single_metric_ghost";
    }
}
