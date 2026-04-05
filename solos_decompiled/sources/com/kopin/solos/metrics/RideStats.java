package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;

/* JADX INFO: loaded from: classes37.dex */
public class RideStats extends Template<Long> {
    public RideStats(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.RIDE_STATS);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean before(Long value) {
        return false;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceConnected(HardwareReceiverService service) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceDisconnected() {
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return LiveRide.isActiveRide() || LiveRide.lastRide() != null;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    private void updateIcon() {
        if (LiveRide.isStarted()) {
            if (LiveRide.isPaused()) {
                this.mAppService.addBitmapFromResource("stats_icon", R.drawable.ic_pause);
                return;
            } else {
                this.mAppService.addBitmapFromResource("stats_icon", R.drawable.time_icon);
                return;
            }
        }
        this.mAppService.addBitmapFromResource("stats_icon", R.drawable.ic_trophy);
    }

    private String updateStats(PageBoxInfo pageBoxInfo, long time, double distance, double avSpeed, double avPace) {
        long sec = time % 60;
        long time2 = time / 60;
        long min = time2 % 60;
        long hour = time2 / 60;
        String rideTime = String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
        updateElement(pageBoxInfo.page, "timeElapsedValue", "content", rideTime);
        updateElement(pageBoxInfo.page, "timeElapsedLabel", "content", this.mAppService.getString(R.string.elapsed_time));
        updateElement(pageBoxInfo.page, "distanceValue", "content", String.format("%.1f", Double.valueOf(distance)));
        updateElement(pageBoxInfo.page, "distanceUnits", "content", Conversion.getUnitOfDistance(this.mAppService, true));
        updateElement(pageBoxInfo.page, "distanceLabel", "content", this.mAppService.getString(R.string.vc_title_distance));
        Object unit = "---";
        Object value = "---";
        Object label = "--------";
        switch (Prefs.getSportType()) {
            case RIDE:
                value = avSpeed == -2.147483648E9d ? "---" : String.format("%.1f", Double.valueOf(avSpeed));
                unit = Conversion.getUnitOfSpeed(this.mAppService, true);
                label = this.mAppService.getString(R.string.vc_title_average_speed_abbrev);
                break;
            case RUN:
                value = avPace == -2.147483648E9d ? "---" : PaceUtil.formatPace(avPace);
                unit = Conversion.getUnitOfPace(this.mAppService, true);
                label = this.mAppService.getString(R.string.vc_title_average_pace);
                break;
        }
        updateElement(pageBoxInfo.page, "speedValue", "content", value);
        updateElement(pageBoxInfo.page, "speedUnits", "content", unit);
        updateElement(pageBoxInfo.page, "speedLabel", "content", label);
        return rideTime;
    }

    private String updateTrainingStats(PageBoxInfo pageBoxInfo, SavedWorkout workout) {
        long time;
        SavedTraining savedTraining;
        if (workout != null) {
            time = workout.getDuration();
            savedTraining = SavedTrainingWorkouts.get(workout.getVirtualWorkoutId());
        } else {
            time = LiveRide.getSplitTime();
            savedTraining = SavedTrainingWorkouts.get(LiveRide.getVirtualWorkoutId());
        }
        long time2 = time / 1000;
        long sec = time2 % 60;
        long time3 = time2 / 60;
        long min = time3 % 60;
        long hour = time3 / 60;
        String rideTime = String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
        updateElement(pageBoxInfo.page, "timeElapsedValue", "content", rideTime);
        updateElement(pageBoxInfo.page, "timeElapsedLabel", "content", this.mAppService.getString(R.string.elapsed_time));
        Object unitPrimary = "---";
        String valuePrimary = "---";
        Object labelPrimary = "---";
        switch (savedTraining.getTrainingType()) {
            case AVERAGE_TARGET_SPEED:
                double value = workout != null ? workout.getAverageSpeedForLocale() : LiveRide.getLapAverageSpeedForLocale();
                unitPrimary = Conversion.getUnitOfSpeed(this.mAppService, true);
                valuePrimary = formatValue(value, 1);
                labelPrimary = this.mAppService.getString(R.string.vc_title_average_speed_abbrev);
                break;
            case AVERAGE_TARGET_PACE:
                double value2 = workout != null ? workout.getAveragePaceForLocale() : LiveRide.getLapAveragePaceForLocale();
                unitPrimary = Conversion.getUnitOfPace(this.mAppService, true);
                valuePrimary = DataHolder.isValid(value2) ? Utility.formatPace(value2) : "---";
                labelPrimary = this.mAppService.getString(R.string.vc_title_avg_pace);
                break;
            case AVERAGE_TARGET_HEARTRATE:
                double value3 = workout != null ? workout.getAverageHeartrate() : LiveRide.getLapAverageHeartRate();
                unitPrimary = this.mAppService.getString(R.string.caps_bpm_vertical);
                valuePrimary = formatValue(value3, 0);
                labelPrimary = this.mAppService.getString(R.string.vc_title_average_heartrate);
                break;
            case AVERAGE_TARGET_POWER:
            case AVERAGE_TARGET_KICK:
                double value4 = workout != null ? workout.getAveragePower() : LiveRide.getLapAveragePower();
                unitPrimary = this.mAppService.getString(R.string.caps_watts_vertical);
                valuePrimary = formatValue(value4, 0);
                labelPrimary = this.mAppService.getString(R.string.vc_title_average_power);
                break;
        }
        Object unitSecondary = "---";
        Object valueSecondary = "---";
        Object labelSecondary = "---";
        MetricType secondaryMetric = savedTraining.getSecondaryMetric();
        if (secondaryMetric == null) {
            secondaryMetric = MetricType.DISTANCE;
        }
        switch (secondaryMetric) {
            case AVERAGE_TARGET_CADENCE:
                unitSecondary = this.mAppService.getString(R.string.cadence_unit_vert);
                valueSecondary = formatValue(workout != null ? workout.getAverageCadence() : LiveRide.getLapAverageCadence(), 0);
                labelSecondary = this.mAppService.getString(R.string.vc_title_average_cadence);
                break;
            case AVERAGE_TARGET_STEP:
                unitSecondary = this.mAppService.getString(R.string.unit_cadence_run_short_caps_vert);
                valueSecondary = formatValue(workout != null ? workout.getAverageCadence() : LiveRide.getLapAverageCadence(), 0);
                labelSecondary = this.mAppService.getString(R.string.vc_title_average_cadence);
                break;
            case DISTANCE:
                unitSecondary = Conversion.getUnitOfDistance(this.mAppService, true);
                valueSecondary = formatValue(workout != null ? workout.getDistanceForLocale() : LiveRide.getLapDistanceForLocale(), 1);
                labelSecondary = this.mAppService.getString(R.string.vc_title_distance);
                break;
        }
        updateElement(pageBoxInfo.page, "distanceValue", "content", valuePrimary);
        updateElement(pageBoxInfo.page, "distanceUnits", "content", unitPrimary);
        updateElement(pageBoxInfo.page, "distanceLabel", "content", labelPrimary);
        updateElement(pageBoxInfo.page, "speedValue", "content", valueSecondary);
        updateElement(pageBoxInfo.page, "speedUnits", "content", unitSecondary);
        updateElement(pageBoxInfo.page, "speedLabel", "content", labelSecondary);
        return rideTime;
    }

    private String formatValue(double value, int decimals) {
        return DataHolder.isValid(value) ? Utility.formatDecimal(Double.valueOf(value), decimals) : "---";
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        updateIcon();
        if (LiveRide.isActiveRide()) {
            if (LiveRide.getMode() == Workout.RideMode.TRAINING) {
                return updateTrainingStats(pageBoxInfo, null);
            }
            return updateStats(pageBoxInfo, LiveRide.getTime() / 1000, LiveRide.getDistanceForLocale(), LiveRide.getAverageSpeedForLocale(), LiveRide.getAveragePaceLocale());
        }
        if (LiveRide.lastRide() != null) {
            SavedWorkout lastRide = LiveRide.lastRide();
            if (lastRide.getWorkoutMode() == Workout.RideMode.TRAINING) {
                return updateTrainingStats(pageBoxInfo, lastRide);
            }
            return updateStats(pageBoxInfo, lastRide.getDuration() / 1000, lastRide.getDistanceForLocale(), lastRide.getAverageSpeedForLocale(), lastRide.getAveragePaceForLocale());
        }
        return "--:--:--";
    }
}
