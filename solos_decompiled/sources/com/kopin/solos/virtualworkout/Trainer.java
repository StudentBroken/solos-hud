package com.kopin.solos.virtualworkout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import com.kopin.pupil.aria.app.TimedAppState;
import com.kopin.solos.AppService;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.notifications.TargetNotifier;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.TTSHelper;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes37.dex */
public class Trainer extends VirtualWorkout {
    private static final long APPROXIMATE_PRE_WARN_PLAY_TIME = 3000;
    private static final long MAX_NEW_TARGET_DISPLAY_TIME = 10000;
    private static final int NOTIFICATION_IDX_NEW_TARGET = 2;
    private static final int NOTIFICATION_IDX_WARNING = 1;
    private static final int NUMBER_OF_NOTIFICATION = 2;
    private static final int PAGE_CYCLING_COUNTER = 19;
    private static final int PAGE_CYCLING_COUNTER_MEDIAN = 9;
    private static final long PRE_WARN_TIME = 10000;
    private static final long PRE_WARN_TIME_IN_SECONDS = 10;
    private static final long TARGET_TTS_TIME = 20000;
    private static final long TARGET_TTS_TIME_IN_SEC = 20;
    private static final long THRESHOLD_NEW_TARGET_TIME = 10;
    SavedTraining.Step mCurrentStep;
    private int mCurrentStepIdx;
    private Handler mHandler;
    private int mNotificationCounter;
    private int mPageSwitchCounter;
    Pair<MetricType, Target> mSecondaryTarget;
    List<SavedTraining.Step> mSteps;
    private SavedTraining mTraining;
    private long mTrainingId;
    private Runnable mWarningTTSRunnable;

    static /* synthetic */ int access$110(Trainer x0) {
        int i = x0.mNotificationCounter;
        x0.mNotificationCounter = i - 1;
        return i;
    }

    public Trainer(AppService service, VirtualWorkout.WorkoutObserver obs, long id) {
        super(service, obs);
        this.mSteps = new ArrayList();
        this.mWarningTTSRunnable = new Runnable() { // from class: com.kopin.solos.virtualworkout.Trainer.5
            @Override // java.lang.Runnable
            public void run() {
                boolean playTTS;
                if (Trainer.this.mNotificationCounter == 1) {
                    long duration = Trainer.this.mCurrentStep.getDuration() * 1000;
                    long lapDuration = LiveRide.getLapDuration();
                    long deltaTime = -2147483648L;
                    double deltaDistance = -2.147483648E9d;
                    double avgSpeed = -2.147483648E9d;
                    if (duration <= 0) {
                        avgSpeed = LiveRide.getLapAverageSpeed();
                        if (avgSpeed != -2.147483648E9d) {
                            deltaDistance = Trainer.this.mCurrentStep.getDistance() - LiveRide.getLapDistance();
                        } else {
                            return;
                        }
                    } else if (lapDuration != -2147483648L) {
                        deltaTime = duration - lapDuration;
                    } else {
                        return;
                    }
                    switch (AnonymousClass6.$SwitchMap$com$kopin$solos$storage$util$SplitHelper$SplitType[Trainer.this.mCurrentStep.getSplitType().ordinal()]) {
                        case 1:
                            if (deltaTime == -2147483648L) {
                                playTTS = deltaDistance <= 0.0d;
                            } else if (deltaTime > 0) {
                                playTTS = false;
                            } else {
                                playTTS = true;
                            }
                            break;
                        case 2:
                            playTTS = avgSpeed != -2.147483648E9d && deltaDistance <= 10.0d * avgSpeed;
                            break;
                        default:
                            long delta = duration - lapDuration;
                            playTTS = delta <= TimedAppState.DEFAULT_CONFIRM_TIMEOUT && delta > Trainer.APPROXIMATE_PRE_WARN_PLAY_TIME;
                            break;
                    }
                    if (playTTS) {
                        Trainer.access$110(Trainer.this);
                        Trainer.this.mAppService.speakText(AppService.NOTIFICATION_TTS, Trainer.this.getEndOfStepWarningNotification());
                    }
                }
            }
        };
        this.mTrainingId = id;
        this.mTraining = SavedTrainingWorkouts.get(id);
        this.mSteps = this.mTraining.getStepFlatList();
        this.mCurrentStepIdx = 0;
        this.mPageSwitchCounter = -1;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    private SavedTraining.Step nextStep() {
        if (this.mCurrentStepIdx >= this.mSteps.size()) {
            return null;
        }
        List<SavedTraining.Step> list = this.mSteps;
        int i = this.mCurrentStepIdx;
        this.mCurrentStepIdx = i + 1;
        return list.get(i);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void start(TargetNotifier notifier) {
        super.start(notifier);
        moveToNext();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void stop() {
        super.stop();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void cancel() {
        super.cancel();
        SavedTrainingWorkouts.remove(this.mTrainingId);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public void notifyNext(int priority) {
        if (priority == 264) {
            showStepScreen();
        } else if (canWarnPrimaryTargetsOnly()) {
            super.skipSecondaryWarnings();
        } else {
            super.notifyNext(priority);
        }
    }

    private void moveToNext() {
        SavedTraining.Step step = nextStep();
        this.mSecondaryTarget = null;
        this.mCurrentStep = step;
        SplitHelper.SplitType splitType = step.getSplitType();
        LiveRide.setSplitType(splitType, step.getDistance(), step.getDuration() * 1000);
        HashMap<MetricType, Target> rangeHashMap = new HashMap<>();
        List<SavedTraining.Target> trainingTargets = step.getTargets();
        int targetsCount = trainingTargets.size();
        if (targetsCount > 0) {
            SavedTraining.Target trainingTarget = trainingTargets.get(0);
            rangeHashMap.put(trainingTarget.getMetric(), createTarget(trainingTarget));
        }
        if (targetsCount > 1) {
            SavedTraining.Target trainingTarget2 = trainingTargets.get(1);
            Target notificationTarget = createTarget(trainingTarget2);
            this.mSecondaryTarget = new Pair<>(trainingTarget2.getMetric(), notificationTarget);
            rangeHashMap.put(trainingTarget2.getMetric(), notificationTarget);
        }
        this.mTargets = rangeHashMap;
        this.mHandler.removeCallbacksAndMessages(null);
        this.mNotificationCounter = 2;
        showNewTargetScreen();
        checkNextStepNotification();
        resetTargetNotificationVerbosityTimer();
    }

    private void showNewTargetScreen() {
        this.mHandler.post(new Runnable() { // from class: com.kopin.solos.virtualworkout.Trainer.1
            @Override // java.lang.Runnable
            public void run() {
                if (!Trainer.this.canShowNewTarget()) {
                    Trainer.access$110(Trainer.this);
                }
                if (Trainer.this.mNotificationCounter == 2) {
                    Trainer.this.notifyNewTarget();
                }
                PageNav.showPage(Trainer.this.getHeadsetPageId());
            }
        });
    }

    private void showStepScreen() {
        this.mHandler.post(new Runnable() { // from class: com.kopin.solos.virtualworkout.Trainer.2
            @Override // java.lang.Runnable
            public void run() {
                Trainer.access$110(Trainer.this);
                PageNav.showPage(Trainer.this.getHeadsetPageId());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canShowNewTarget() {
        return this.mCurrentStep.getDuration() >= 10 || this.mCurrentStep.getDistance() >= UserProfile.getAverageSpeed(this.mTraining.getSport()) * 10.0d;
    }

    private Target createTarget(SavedTraining.Target target) {
        MetricType metricType = target.getMetric();
        double threshold = target.getThresholdTarget();
        double min = target.getMinTarget();
        double max = target.getMaxTarget();
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                threshold = Conversion.speedForLocale(threshold);
                min = Conversion.speedForLocale(min);
                max = Conversion.speedForLocale(max);
                break;
            case AVERAGE_TARGET_PACE:
                threshold = Conversion.paceForLocale(threshold);
                min = Conversion.paceForLocale(min);
                max = Conversion.paceForLocale(max);
                break;
        }
        return new Target(threshold, min, max);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.storage.util.SplitHelper.SplitListener
    public boolean isLastLap() {
        return !hasNextStep();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.storage.util.SplitHelper.SplitListener
    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
        Log.d("Trainer", "New Step : " + splitDistance + ", " + splitTime + ", " + isEnd);
        if (isEnd) {
            this.mTargets.clear();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.kopin.solos.virtualworkout.Trainer.3
                @Override // java.lang.Runnable
                public void run() {
                    RideControl.stop(false);
                }
            }, 500L);
        } else {
            moveToNext();
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public double getLiveData(MetricType metricType) {
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return this.mAppService.getHardwareReceiverService().getLastSpeedForLocale();
            case AVERAGE_TARGET_PACE:
                return this.mAppService.getHardwareReceiverService().getLastPaceForLocale();
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                return (int) this.mAppService.getHardwareReceiverService().getLastPower();
            case AVERAGE_TARGET_HEARTRATE:
                return this.mAppService.getHardwareReceiverService().getLastHeartRate();
            case AVERAGE_TARGET_STEP:
            case AVERAGE_TARGET_CADENCE:
                return (int) this.mAppService.getHardwareReceiverService().getLastCadence();
            default:
                return super.getLiveData(metricType);
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public List<MetricType> getTTSMetrics() {
        return new ArrayList<MetricType>() { // from class: com.kopin.solos.virtualworkout.Trainer.4
            {
                add(Trainer.this.getPrimaryMetric());
                if (Trainer.this.getSecondaryMetric() != null) {
                    add(Trainer.this.getSecondaryMetric());
                }
            }
        };
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public int[] getHeadsetPages() {
        return new int[]{R.xml.training_next_step, R.xml.training_step, R.xml.training_step_2, R.xml.training_manual_step, R.xml.training_manual_step_2};
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public String getHeadsetPageId() {
        checkNextStepNotification();
        if (this.mNotificationCounter == 2) {
            if (LiveRide.getLapDuration() >= TimedAppState.DEFAULT_CONFIRM_TIMEOUT) {
                this.mNotificationCounter--;
            }
            return MetricType.TRAINING_NEXT_STEP.getResource();
        }
        if (this.mCurrentStep != null && this.mCurrentStep.isManualLap()) {
            boolean switchPages = this.mCurrentStep.getDistance() > 0.0d ? LiveRide.getLapDistance() < this.mCurrentStep.getDistance() : LiveRide.getLapDuration() < this.mCurrentStep.getDuration() * 1000;
            if (switchPages) {
                this.mPageSwitchCounter = (this.mPageSwitchCounter + 1) % 19;
                if (this.mPageSwitchCounter > 9) {
                    return getHeadsetPageId(false);
                }
            }
            return getHeadsetPageId(true);
        }
        return getHeadsetPageId(false);
    }

    private String getHeadsetPageId(boolean manual) {
        boolean hasSecondaryTarget = this.mSecondaryTarget != null;
        if (manual) {
            if (hasSecondaryTarget) {
                return MetricType.TRAINING_MANUAL_STEP_2.getResource();
            }
            return MetricType.TRAINING_MANUAL_STEP.getResource();
        }
        if (hasSecondaryTarget) {
            return MetricType.TRAINING_STEP_2.getResource();
        }
        return MetricType.TRAINING_STEP.getResource();
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public PageNav.PageMode getHeadsetPageMode() {
        return PageNav.PageMode.TRAINING;
    }

    public Target nextStepTarget() {
        SavedTraining.Step step;
        if (this.mCurrentStepIdx < this.mSteps.size() && (step = this.mSteps.get(this.mCurrentStepIdx)) != null && step.getTargets().size() > 0) {
            return createTarget(step.getTargets().get(0));
        }
        return null;
    }

    private boolean hasNextStep() {
        return this.mCurrentStepIdx < this.mSteps.size() && this.mSteps.get(this.mCurrentStepIdx) != null;
    }

    public MetricType getPrimaryMetric() {
        return this.mTraining.getTrainingType();
    }

    public MetricType getSecondaryMetric() {
        return this.mTraining.getSecondaryMetric();
    }

    private MetricType getSecondaryMetric(int stepPosition) {
        if (stepPosition < this.mSteps.size()) {
            List<SavedTraining.Target> targets = this.mSteps.get(stepPosition).getTargets();
            if (this.mSteps.get(stepPosition).getTargets().size() > 1) {
                return targets.get(1).getMetric();
            }
        }
        return null;
    }

    public boolean stepHasSecondaryTarget() {
        return this.mCurrentStep == null ? getSecondaryMetric(0) != null : this.mSecondaryTarget != null;
    }

    public Pair<MetricType, Target> getSecondaryTarget() {
        return this.mSecondaryTarget;
    }

    public double getStepDistance() {
        if (this.mCurrentStep != null) {
            return this.mCurrentStep.getDistance();
        }
        return -2.147483648E9d;
    }

    public long getStepDuration() {
        if (this.mCurrentStep != null) {
            return this.mCurrentStep.getDuration();
        }
        return -2147483648L;
    }

    public SplitHelper.SplitType getStepSplit() {
        return this.mCurrentStep != null ? this.mCurrentStep.getSplitType() : SplitHelper.SplitType.DISABLED;
    }

    public int getProgress() {
        return ((this.mCurrentStepIdx - 1) * 100) / this.mSteps.size();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyNewTarget() {
        this.mAppService.speakText(AppService.NOTIFICATION_TTS, getNewTargetNotificationText());
    }

    private void checkNextStepNotification() {
        this.mHandler.post(this.mWarningTTSRunnable);
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public boolean canNotify() {
        if (LiveRide.isPaused()) {
            return false;
        }
        if (this.mCurrentStep.getSplitType() == SplitHelper.SplitType.MANUAL) {
            return true;
        }
        if (this.mNotificationCounter < 1) {
            return false;
        }
        long duration = this.mCurrentStep.getDuration() * 1000;
        long lapDuration = LiveRide.getLapDuration();
        long deltaTime = -2147483648L;
        double deltaDistance = -2.147483648E9d;
        double avgSpeed = -2.147483648E9d;
        if (duration <= 0) {
            avgSpeed = LiveRide.getLapAverageSpeed();
            if (avgSpeed == -2.147483648E9d) {
                return false;
            }
            deltaDistance = this.mCurrentStep.getDistance() - LiveRide.getLapDistance();
        } else {
            if (lapDuration == -2147483648L) {
                return false;
            }
            deltaTime = duration - lapDuration;
        }
        return deltaTime != -2147483648L ? deltaTime >= TARGET_TTS_TIME : deltaDistance >= 20.0d * avgSpeed;
    }

    /* JADX INFO: renamed from: com.kopin.solos.virtualworkout.Trainer$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$storage$util$SplitHelper$SplitType = new int[SplitHelper.SplitType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$storage$util$SplitHelper$SplitType[SplitHelper.SplitType.MANUAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$SplitHelper$SplitType[SplitHelper.SplitType.DISTANCE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$com$kopin$solos$storage$util$MetricType = new int[MetricType.values().length];
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_SPEED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_PACE.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_POWER.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_KICK.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_HEARTRATE.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_STEP.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_CADENCE.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    private String getNewTargetNotificationText() {
        MetricType primaryMetricType = getPrimaryMetric();
        Target primaryTarget = this.mTargets.get(primaryMetricType);
        if (primaryTarget == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String unit = TargetNotifier.getUnitForTTS(this.mAppService, primaryMetricType);
        if (primaryTarget.hasRange) {
            String minTarget = getValueText(primaryMetricType, primaryTarget.minTarget);
            String maxTarget = getValueText(primaryMetricType, primaryTarget.maxTarget);
            sb.append(this.mAppService.getString(R.string.tts_training_new_target_range, new Object[]{minTarget, maxTarget, unit, getTargetTypeText()}));
        } else {
            String threshold = getValueText(primaryMetricType, primaryTarget.threshold);
            sb.append(this.mAppService.getString(R.string.tts_training_new_target, new Object[]{threshold, unit, getTargetTypeText()}));
        }
        if (this.mSecondaryTarget != null) {
            sb.append(this.mAppService.getString(R.string.tts_training_secondary_target, new Object[]{Integer.valueOf((int) Math.round(((Target) this.mSecondaryTarget.second).minTarget)), Integer.valueOf((int) Math.round(((Target) this.mSecondaryTarget.second).maxTarget)), TargetNotifier.getUnitForTTS(this.mAppService, (MetricType) this.mSecondaryTarget.first)}));
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getEndOfStepWarningNotification() {
        int ttsRes;
        int ttsRes2 = 0;
        if (this.mCurrentStep.getSplitType() == SplitHelper.SplitType.MANUAL) {
            ttsRes2 = this.mCurrentStep.getDuration() > 0 ? R.string.tts_training_next_step_manual_time : R.string.tts_training_next_step_manual_distance;
        }
        if (hasNextStep()) {
            if (ttsRes2 != 0) {
                return this.mAppService.getString(ttsRes2) + ". " + this.mAppService.getString(R.string.tts_training_advance);
            }
            ttsRes = R.string.tts_training_next_step;
        } else {
            if (ttsRes2 != 0) {
                return this.mAppService.getString(ttsRes2) + this.mAppService.getString(R.string.tts_training_end_workout_manual);
            }
            ttsRes = R.string.tts_training_end_workout;
        }
        return this.mAppService.getString(ttsRes);
    }

    private String getTargetTypeText() {
        if (this.mCurrentStep.getDistance() <= 0.0d) {
            return this.mAppService.getString(R.string.tts_training_for, new Object[]{TTSHelper.timeAsString(this.mAppService, this.mCurrentStep.getDuration()), ""});
        }
        String unit = Utility.getUserDefinedUnit(this.mAppService, 1);
        double distanceForLocale = Conversion.distanceForLocale(this.mCurrentStep.getDistance());
        String distStr = new DecimalFormat("0.##").format(distanceForLocale);
        return this.mAppService.getString(R.string.tts_training_for, new Object[]{distStr, unit}) + ". ";
    }

    private String getValueText(MetricType metricType, double value) {
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return new DecimalFormat("0.#").format(value);
            case AVERAGE_TARGET_PACE:
                return TTSHelper.addTimeDiff(value, this.mAppService);
            default:
                return String.format(Locale.US, "%d", Long.valueOf(Math.round(value)));
        }
    }
}
