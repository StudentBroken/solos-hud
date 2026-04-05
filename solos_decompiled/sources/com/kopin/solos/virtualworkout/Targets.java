package com.kopin.solos.virtualworkout;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class Targets extends VirtualWorkout {
    public Targets(AppService service, VirtualWorkout.WorkoutObserver obs) {
        super(service, obs);
        this.mTargets = new HashMap<>();
        if (Prefs.hasTargetAverageCadence()) {
            double target = Prefs.getTargetAverageCadence();
            this.mTargets.put(MetricType.AVERAGE_TARGET_CADENCE, new Target(target));
        }
        if (Prefs.hasTargetAverageHeartRate()) {
            double target2 = Prefs.getTargetAverageHeartrate();
            this.mTargets.put(MetricType.AVERAGE_TARGET_HEARTRATE, new Target(target2));
        }
        if (Prefs.hasTargetAveragePower()) {
            double target3 = Prefs.getTargetAveragePower();
            this.mTargets.put(MetricType.AVERAGE_TARGET_POWER, new Target(target3));
        }
        if (Prefs.hasTargetAverageSpeed()) {
            double target4 = Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue());
            this.mTargets.put(MetricType.AVERAGE_TARGET_SPEED, new Target(target4));
        }
        if (Prefs.hasTargetAveragePace()) {
            double target5 = Conversion.paceForLocale(Prefs.getTargetAveragePaceValue());
            this.mTargets.put(MetricType.AVERAGE_TARGET_PACE, new Target(target5));
        }
        if (Prefs.hasTargetAverageStep()) {
            double target6 = Prefs.getTargetAverageStep();
            this.mTargets.put(MetricType.AVERAGE_TARGET_STEP, new Target(target6));
        }
        if (Prefs.hasTargetAverageKick()) {
            double target7 = Prefs.getTargetAverageKick();
            this.mTargets.put(MetricType.AVERAGE_TARGET_KICK, new Target(target7));
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout, com.kopin.solos.notifications.TargetNotifier.NotificationProvider
    public double getLiveData(MetricType metricType) {
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return LiveRide.getAverageSpeedForLocale();
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                return (int) LiveRide.getAveragePower();
            case AVERAGE_TARGET_CADENCE:
            case AVERAGE_TARGET_STEP:
                return (int) LiveRide.getAverageCadence();
            case AVERAGE_TARGET_HEARTRATE:
                return (int) LiveRide.getAverageHeartRate();
            case AVERAGE_TARGET_PACE:
                return LiveRide.getAveragePaceLocale();
            default:
                return super.getLiveData(metricType);
        }
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public List<MetricType> getTTSMetrics() {
        return new ArrayList<MetricType>() { // from class: com.kopin.solos.virtualworkout.Targets.1
            {
                add(MetricType.AVERAGE_TARGET_CADENCE);
                add(MetricType.AVERAGE_TARGET_STEP);
                add(MetricType.AVERAGE_TARGET_HEARTRATE);
                add(MetricType.AVERAGE_TARGET_SPEED);
                add(MetricType.AVERAGE_TARGET_POWER);
                add(MetricType.AVERAGE_TARGET_PACE);
                add(MetricType.AVERAGE_TARGET_KICK);
            }
        };
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public int[] getHeadsetPages() {
        return new int[]{R.xml.time, R.xml.common_average_target};
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public PageNav.PageMode getHeadsetPageMode() {
        return PageNav.PageMode.NORMAL;
    }

    @Override // com.kopin.solos.virtualworkout.VirtualWorkout
    public String getHeadsetPageId() {
        return "common_average_target";
    }
}
