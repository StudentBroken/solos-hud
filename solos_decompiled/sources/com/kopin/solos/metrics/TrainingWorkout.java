package com.kopin.solos.metrics;

import android.util.Pair;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.virtualworkout.Target;
import com.kopin.solos.virtualworkout.Trainer;
import com.kopin.solos.virtualworkout.VirtualCoach;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes37.dex */
public class TrainingWorkout extends GhostPage<Long> {
    public TrainingWorkout(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.TRAINING_STEP);
        addPage(MetricType.TRAINING_STEP_2);
        addPage(MetricType.TRAINING_MANUAL_STEP);
        addPage(MetricType.TRAINING_MANUAL_STEP_2);
        addPage(MetricType.TRAINING_NEXT_STEP);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void start() {
        super.start();
        VirtualWorkout workout = VirtualCoach.getVirtualWorkout();
        if (workout != null && (workout instanceof Trainer)) {
            update(0L, false, true);
        }
    }

    private boolean isTargetAvailable() {
        VirtualWorkout workout = VirtualCoach.getVirtualWorkout();
        return (workout == null || !(workout instanceof Trainer) || workout.getTargets().isEmpty()) ? false : true;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        if (!isBound()) {
            return false;
        }
        if (VirtualCoach.isActive()) {
            return isTargetAvailable();
        }
        return value.doubleValue() != -2.147483648E9d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        VirtualWorkout virtualWorkout = VirtualCoach.getVirtualWorkout();
        if (virtualWorkout == null || !(virtualWorkout instanceof Trainer)) {
            return null;
        }
        return updateTrainingTarget(pageBoxInfo, (Trainer) virtualWorkout);
    }

    private String updateTrainingTarget(PageBoxInfo boxInfo, Trainer trainer) {
        String intervalText;
        MetricType metricType = trainer.getPrimaryMetric();
        Target target = trainer.getTarget(metricType);
        double liveData = trainer.getLiveData(metricType);
        boolean inRange = target.isInRange(Double.valueOf(liveData));
        this.mAppService.updateElement(boxInfo.page, boxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(inRange ? R.color.ahead_color : R.color.behind_color)));
        String newTarget = updateTrainingTargetElements(boxInfo, target, metricType, "currentUpper", "currentLower");
        this.mAppService.updateElement(boxInfo.page, "newTarget", "content", newTarget);
        if (trainer.getStepDistance() > 0.0d) {
            intervalText = new DecimalFormat("0.##").format(Conversion.distanceForLocale(trainer.getStepDistance())) + " " + Conversion.getUnitOfDistance(this.mAppService);
        } else {
            intervalText = Utility.formatTime(trainer.getStepDuration() * 1000);
        }
        this.mAppService.updateElement(boxInfo.page, "newTargetInterval", "content", intervalText);
        updateTrainingTargetElements(boxInfo, trainer.nextStepTarget(), metricType, "nextUpper", "nextLower");
        this.mAppService.updateElement(boxInfo.page, "imageCurrentStep", "source", getMetricImageSource(metricType));
        this.mAppService.updateElement(boxInfo.page, "imageNextStep", "source", trainer.nextStepTarget() != null ? getMetricImageSource(metricType) : "goal_flag");
        updateTrainingSplitElements(boxInfo, trainer);
        updateTrainingSecondaryTarget(boxInfo, trainer);
        return Utility.formatMetric(metricType, liveData);
    }

    @Override // com.kopin.solos.metrics.Template
    public String getUnit() {
        VirtualWorkout workout = VirtualCoach.getVirtualWorkout();
        if (workout != null && (workout instanceof Trainer)) {
            switch (((Trainer) workout).getPrimaryMetric()) {
                case AVERAGE_TARGET_POWER:
                case AVERAGE_TARGET_KICK:
                    return this.mAppService.getString(R.string.power_unit).toUpperCase();
                case AVERAGE_TARGET_HEARTRATE:
                    return this.mAppService.getString(R.string.caps_bpm).toUpperCase();
                case AVERAGE_TARGET_STEP:
                    return this.mAppService.getString(R.string.run_cadence_unit_caps);
                case AVERAGE_TARGET_CADENCE:
                    return this.mAppService.getString(R.string.cadence_unit_caps);
                case AVERAGE_TARGET_PACE:
                    return Conversion.getUnitOfPace(this.mAppService).toUpperCase();
                default:
                    return Conversion.getUnitOfSpeed(this.mAppService).toUpperCase();
            }
        }
        return super.getUnit();
    }

    private String getMetricImageSource(MetricType metricType) {
        return metricType.getBaseMetricType().name();
    }

    private void updateTrainingSplitElements(PageBoxInfo boxInfo, Trainer trainer) {
        String splitStr;
        if (trainer.getStepDistance() > 0.0d) {
            double remainingDist = trainer.getStepDistance() - LiveRide.getSplitDistance();
            String distStr = new DecimalFormat("0.##").format(Conversion.distanceForLocale(remainingDist));
            String unitStr = Conversion.getUnitOfDistance(this.mAppService);
            splitStr = distStr + " " + unitStr;
        } else {
            long time = (trainer.getStepDuration() * 1000) - LiveRide.getSplitTime();
            splitStr = Utility.formatTime(time);
        }
        this.mAppService.updateElement(boxInfo.page, "countdown", "content", splitStr);
        this.mAppService.updateElement(boxInfo.metric, "progressbar", "progress", Integer.valueOf(trainer.getProgress()));
        this.mAppService.updateElement(boxInfo.page, "textButtonPress", "content", this.mAppService.getString(trainer.nextStepTarget() != null ? R.string.button_press : R.string.button_press_end));
    }

    private boolean updateTrainingSecondaryTarget(PageBoxInfo boxInfo, Trainer trainer) {
        Pair<MetricType, Target> secondaryTarget = trainer.getSecondaryTarget();
        if (secondaryTarget == null) {
            return false;
        }
        MetricType metricType = (MetricType) secondaryTarget.first;
        Target target = (Target) secondaryTarget.second;
        double liveData = trainer.getLiveData(metricType);
        boolean inRange = target.isInRange(Double.valueOf(liveData));
        this.mAppService.updateElement(boxInfo.page, "imageSecondaryMetric", "source", getMetricImageSource(metricType));
        if (inRange) {
            this.mAppService.updateElement(boxInfo.page, "imageSecondaryMetric", "color", Integer.valueOf(this.mAppService.getColor(R.color.ahead_color)));
            this.mAppService.updateElement(boxInfo.page, "imageChevronUp", "color", Integer.valueOf(this.mAppService.getColor(R.color.sync_bar_background)));
            this.mAppService.updateElement(boxInfo.page, "imageChevronDown", "color", Integer.valueOf(this.mAppService.getColor(R.color.sync_bar_background)));
        } else {
            this.mAppService.updateElement(boxInfo.page, "imageSecondaryMetric", "color", Integer.valueOf(this.mAppService.getColor(R.color.sync_bar_background)));
            boolean isAbove = target.isAbove(Double.valueOf(liveData), boxInfo.page.contains("pace"));
            if (isAbove) {
                this.mAppService.updateElement(boxInfo.page, "imageChevronDown", "color", Integer.valueOf(this.mAppService.getColor(R.color.behind_color)));
                this.mAppService.updateElement(boxInfo.page, "imageChevronUp", "color", Integer.valueOf(this.mAppService.getColor(R.color.sync_bar_background)));
            } else {
                this.mAppService.updateElement(boxInfo.page, "imageChevronDown", "color", Integer.valueOf(this.mAppService.getColor(R.color.sync_bar_background)));
                this.mAppService.updateElement(boxInfo.page, "imageChevronUp", "color", Integer.valueOf(this.mAppService.getColor(R.color.behind_color)));
            }
        }
        return true;
    }

    private String updateTrainingTargetElements(PageBoxInfo boxInfo, Target target, MetricType metricType, String elementUpper, String elementLower) {
        if (target == null) {
            this.mAppService.updateElement(boxInfo.page, elementUpper, "content", "");
            this.mAppService.updateElement(boxInfo.page, elementLower, "content", "");
            return "---";
        }
        if (target.hasRange) {
            String upperStr = Utility.formatMetric(metricType, target.maxTarget);
            String lowerStr = Utility.formatMetric(metricType, target.minTarget);
            this.mAppService.updateElement(boxInfo.page, elementUpper, "content", upperStr);
            this.mAppService.updateElement(boxInfo.page, elementLower, "content", lowerStr);
            return lowerStr + " - " + upperStr;
        }
        String targetStr = Utility.formatMetric(metricType, target.threshold);
        this.mAppService.updateElement(boxInfo.page, elementUpper, "content", targetStr);
        this.mAppService.updateElement(boxInfo.page, elementLower, "content", "");
        return targetStr;
    }
}
