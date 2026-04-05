package com.kopin.solos.virtualworkout;

import com.kopin.solos.AppService;
import com.kopin.solos.RideControl;
import com.kopin.solos.notifications.TargetNotifier;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class VirtualCoach {
    private static final RideControl.RideObserver mRideObserver = new RideControl.RideObserver() { // from class: com.kopin.solos.virtualworkout.VirtualCoach.1
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
            VirtualCoach.self.createVirtualCoach(mode, rideOrRouteId);
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(RideControl.StartMode startMode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(Workout.RideMode mode) {
            VirtualCoach.self.startVirtualCoach();
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            return true;
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(SavedWorkout ride) {
            VirtualCoach.self.stopVirtualCoach();
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(boolean userOrAuto) {
            if (VirtualCoach.self.mWorkout != null) {
                VirtualCoach.self.mWorkout.resume();
            }
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(double lastDistance, long lastTime) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(int counter) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(boolean wasCancelled) {
        }
    };
    private static final VirtualWorkout.WorkoutObserver mWorkoutObserver = new VirtualWorkout.WorkoutObserver() { // from class: com.kopin.solos.virtualworkout.VirtualCoach.2
        @Override // com.kopin.solos.virtualworkout.VirtualWorkout.WorkoutObserver
        public void onWorkoutEnded() {
            if (VirtualCoach.self != null) {
                VirtualCoach.self.stopVirtualCoach();
            }
        }
    };
    private static VirtualCoach self;
    AppService mAppService;
    private TargetNotifier mTargetNotifier;
    VirtualWorkout mWorkout;

    private VirtualCoach(AppService service) {
        this.mAppService = service;
        this.mTargetNotifier = new TargetNotifier(service);
    }

    public static void init(AppService service) {
        self = new VirtualCoach(service);
        RideControl.registerObserver(mRideObserver);
    }

    public static VirtualWorkout getVirtualWorkout() {
        return self.mWorkout;
    }

    public static boolean isActive() {
        return self.mWorkout != null;
    }

    public static boolean isWorkoutComplete() {
        return self.mWorkout != null && self.mWorkout.isWorkoutComplete();
    }

    public static void notifyNext(int priority) {
        if (self.mWorkout != null) {
            self.mWorkout.notifyNext(priority);
        }
    }

    public static int[] getHeadsetPages() {
        return self.mWorkout.getHeadsetPages();
    }

    public static List<MetricType> getAllMetrics() {
        ArrayList<MetricType> metrics = new ArrayList<>();
        if (self.mWorkout != null) {
            List<MetricType> current = self.mWorkout.getTTSMetrics();
            for (MetricType m : current) {
                if (SensorsConnector.isAllowedType(m.getSensorType())) {
                    metrics.add(m.getBaseMetricType());
                    metrics.add(m);
                }
            }
        }
        return metrics;
    }

    public static HashMap<String, String> getAllMetricsMap() {
        HashMap<String, String> metrics = new HashMap<>();
        if (self.mWorkout != null) {
            List<MetricType> current = self.mWorkout.getTTSMetrics();
            for (MetricType m : current) {
                if (SensorsConnector.isAllowedType(m.getSensorType())) {
                    metrics.put(m.getResource(), m.getBaseMetricType().getMetricName(self.mAppService));
                    MetricType m2 = m.getBaseMetricType();
                    metrics.put(m2.getResource(), m2.getBaseMetricType().getMetricName(self.mAppService));
                }
            }
        }
        return metrics;
    }

    public static HashMap<String, String> getTargetMetrics() {
        HashMap<String, String> targets = new HashMap<>();
        if (self.mWorkout != null) {
            List<MetricType> current = self.mWorkout.getTTSMetrics();
            for (MetricType m : current) {
                if (SensorsConnector.isAllowedType(m.getSensorType())) {
                    targets.put(m.getResource(), m.getBaseMetricType().getMetricName(self.mAppService));
                }
            }
        }
        return targets;
    }

    public static PageNav.PageMode getHeadsetPageMode() {
        return self.mWorkout.getHeadsetPageMode();
    }

    public static String getCurrentHeadsetPageId() {
        return self.mWorkout.getHeadsetPageId();
    }

    public static double getLiveValue(String metric) {
        if (self.mWorkout == null) {
            return -2.147483648E9d;
        }
        MetricType metricType = MetricType.getMetricType(metric);
        return self.mWorkout.getLiveData(metricType);
    }

    public static double getAverageValue(String metric) {
        if (self.mWorkout == null) {
            return -2.147483648E9d;
        }
        MetricType metricType = MetricType.getMetricType(metric);
        return self.mWorkout.getLiveData(metricType.getTargetAverageType());
    }

    public static Target getTarget(String metric) {
        if (self.mWorkout == null) {
            return null;
        }
        MetricType metricType = MetricType.getMetricType(metric);
        Target target = self.mWorkout.getTarget(metricType);
        if (target == null) {
            return self.mWorkout.getTarget(metricType.getTargetAverageType());
        }
        return target;
    }

    public static double getTargetForLocale(String targetMetric) {
        if (self.mWorkout == null) {
            return -2.147483648E9d;
        }
        MetricType metricType = MetricType.getMetricType(targetMetric);
        Target target = self.mWorkout.getTarget(metricType);
        if (target == null) {
            target = self.mWorkout.getTarget(metricType.getTargetAverageType());
        }
        if (target == null) {
            return -2.147483648E9d;
        }
        switch (metricType.getBaseMetricType()) {
        }
        return -2.147483648E9d;
    }

    public static boolean hasTarget(String targetMetric) {
        if (self.mWorkout == null) {
            return false;
        }
        MetricType metricType = MetricType.getMetricType(targetMetric);
        return self.mWorkout.getTarget(metricType) != null;
    }

    public static boolean hasLiveData(String targetMetric) {
        if (self.mWorkout == null) {
            return false;
        }
        MetricType metricType = MetricType.getMetricType(targetMetric);
        return DataHolder.isValid(self.mWorkout.getLiveData(metricType));
    }

    public static void updateTarget(String targetMetric) {
        if (self.mWorkout != null) {
            MetricType metricType = MetricType.getMetricType(targetMetric);
            self.mTargetNotifier.updateNow(metricType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createVirtualCoach(Workout.RideMode mode, long coachId) {
        cancelVirtualCoach();
        switch (mode) {
            case GHOST_RIDE:
                this.mWorkout = new GhostWorkout(this.mAppService, mWorkoutObserver, coachId, false);
                break;
            case GHOST_LAP:
                this.mWorkout = new GhostWorkout(this.mAppService, mWorkoutObserver, coachId, true);
                break;
            case TARGETS:
                this.mWorkout = new Targets(this.mAppService, mWorkoutObserver);
                break;
            case TRAINING:
                this.mWorkout = new Trainer(this.mAppService, mWorkoutObserver, coachId);
                break;
        }
        if (this.mWorkout != null) {
            this.mAppService.getHardwareReceiverService().getSplitHelper().addSplitListener(this.mWorkout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVirtualCoach() {
        if (this.mWorkout != null) {
            this.mWorkout.start(this.mTargetNotifier);
        }
    }

    private void cancelVirtualCoach() {
        if (this.mWorkout != null) {
            this.mWorkout.cancel();
            this.mAppService.getHardwareReceiverService().getSplitHelper().removeSplitListener(this.mWorkout);
        }
        this.mWorkout = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopVirtualCoach() {
        if (this.mWorkout != null) {
            this.mWorkout.stop();
            this.mAppService.getHardwareReceiverService().getSplitHelper().removeSplitListener(this.mWorkout);
        }
        this.mWorkout = null;
    }
}
