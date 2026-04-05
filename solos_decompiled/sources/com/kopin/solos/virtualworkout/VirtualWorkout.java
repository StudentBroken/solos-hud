package com.kopin.solos.virtualworkout;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.notifications.TargetNotification;
import com.kopin.solos.notifications.TargetNotifier;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.SplitHelper;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public abstract class VirtualWorkout implements SplitHelper.SplitListener, TargetNotifier.NotificationProvider {
    protected AppService mAppService;
    protected TargetNotifier mNotifier;
    private WorkoutObserver mObserver;
    HashMap<MetricType, Target> mTargets = new HashMap<>();

    interface WorkoutObserver {
        void onWorkoutEnded();
    }

    public abstract String getHeadsetPageId();

    public abstract PageNav.PageMode getHeadsetPageMode();

    public abstract int[] getHeadsetPages();

    public abstract List<MetricType> getTTSMetrics();

    VirtualWorkout(AppService appService, WorkoutObserver obs) {
        this.mAppService = appService;
        this.mObserver = obs;
    }

    public void start(TargetNotifier notifier) {
        this.mNotifier = notifier;
        this.mNotifier.start(this, getTTSMetrics());
    }

    public void cancel() {
        if (this.mNotifier != null) {
            this.mNotifier.stop();
        }
    }

    public void stop() {
        if (this.mNotifier != null) {
            this.mNotifier.stop();
        }
    }

    public void resetTargetNotificationVerbosityTimer() {
        if (this.mNotifier != null) {
            this.mNotifier.resetVerbosityTimer();
        }
    }

    public void pause() {
        if (this.mNotifier != null) {
            this.mNotifier.pause();
        }
    }

    public void resume() {
        if (this.mNotifier != null) {
            this.mNotifier.resume();
        }
    }

    protected void workoutComplete() {
        if (this.mObserver != null) {
            this.mObserver.onWorkoutEnded();
        }
    }

    public void notifyNext(int priority) {
        this.mNotifier.notifyNext();
    }

    void skipSecondaryWarnings() {
        this.mNotifier.clearQueue();
    }

    boolean canWarnPrimaryTargetsOnly() {
        return Prefs.getNotifyPrimaryTargetsOnly();
    }

    public boolean isWorkoutComplete() {
        return false;
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public boolean isLastLap() {
        return false;
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
    }

    public TargetNotification getNotification() {
        return new TargetNotification(R.string.target_notification_screen, R.string.target_notification_speech);
    }

    public double getLiveData(MetricType metricType) {
        return -2.147483648E9d;
    }

    public Target getTarget(MetricType metricType) {
        return this.mTargets.get(metricType);
    }

    public boolean canNotify() {
        return !LiveRide.isPaused();
    }

    public HashMap<MetricType, Target> getTargets() {
        return this.mTargets;
    }
}
