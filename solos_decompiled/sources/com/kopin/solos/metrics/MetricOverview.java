package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.virtualworkout.GhostWorkout;
import com.kopin.solos.virtualworkout.Target;
import com.kopin.solos.virtualworkout.VirtualCoach;
import com.ua.sdk.datapoint.BaseDataTypes;

/* JADX INFO: loaded from: classes37.dex */
public class MetricOverview extends Template<Long> {
    public MetricOverview(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.METRIC_OVERVIEW);
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
        return LiveRide.isActiveRide();
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    private String formatValue(double value, int decimals) {
        return DataHolder.isValid(value) ? Utility.formatDecimal(Double.valueOf(value), decimals) : "---";
    }

    private void updateSpeed(PageBoxInfo pageBoxInfo) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastSpeedForLocale();
        int noGhostColour = getGhostColourFor(BaseDataTypes.ID_SPEED);
        String value = curVal == -2.147483648E9d ? "---" : String.format("%.1f", Double.valueOf(curVal));
        String unit = Conversion.getUnitOfSpeed(this.mAppService, true);
        String label = this.mAppService.getString(R.string.vc_title_speed);
        updateElement(pageBoxInfo.page, "speedValue", "content", value);
        updateElement(pageBoxInfo.page, "speedValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "speedUnits", "content", unit);
        updateElement(pageBoxInfo.page, "speedLabel", "content", label);
    }

    private void updatePace(PageBoxInfo pageBoxInfo) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastPaceForLocale();
        int noGhostColour = getGhostColourFor("pace");
        String value = curVal == -2.147483648E9d ? "---" : PaceUtil.formatPace(curVal);
        String unit = Conversion.getUnitOfPace(this.mAppService, true);
        String label = this.mAppService.getString(R.string.vc_title_pace);
        updateElement(pageBoxInfo.page, "speedValue", "content", value);
        updateElement(pageBoxInfo.page, "speedValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "speedUnits", "content", unit);
        updateElement(pageBoxInfo.page, "speedLabel", "content", label);
    }

    private void updateCadence(PageBoxInfo pageBoxInfo, String cadenceMetric, int unitResId) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastCadence();
        int noGhostColour = getGhostColourFor(cadenceMetric);
        String value = curVal == -2.147483648E9d ? "---" : String.format("%d", Integer.valueOf((int) curVal));
        String unit = this.mAppService.getString(unitResId);
        String label = this.mAppService.getString(R.string.vc_title_cadence);
        updateElement(pageBoxInfo.page, "cadenceValue", "content", value);
        updateElement(pageBoxInfo.page, "cadenceValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "cadenceUnits", "content", unit);
        updateElement(pageBoxInfo.page, "cadenceLabel", "content", label);
    }

    private void updatePower(PageBoxInfo pageBoxInfo, String powerMetric) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastPower();
        int noGhostColour = getGhostColourFor(powerMetric);
        String value = curVal == -2.147483648E9d ? "---" : String.format("%.0f", Double.valueOf(curVal));
        String unit = this.mAppService.getString(R.string.caps_watts_vertical);
        String label = this.mAppService.getString(R.string.vc_title_power);
        updateElement(pageBoxInfo.page, "powerValue", "content", value);
        updateElement(pageBoxInfo.page, "powerValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "powerUnits", "content", unit);
        updateElement(pageBoxInfo.page, "powerLabel", "content", label);
    }

    private void updateHeartrate(PageBoxInfo pageBoxInfo) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastHeartRate();
        int noGhostColour = getGhostColourFor("heartrate");
        String value = curVal == -2.147483648E9d ? "---" : String.format("%d", Integer.valueOf((int) curVal));
        String unit = this.mAppService.getString(R.string.caps_bpm_vertical);
        String label = this.mAppService.getString(R.string.vc_title_heartrate);
        updateElement(pageBoxInfo.page, "heartrateValue", "content", value);
        updateElement(pageBoxInfo.page, "heartrateValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "heartrateUnits", "content", unit);
        updateElement(pageBoxInfo.page, "heartrateLabel", "content", label);
    }

    private void updateStride(PageBoxInfo pageBoxInfo) {
        double curVal = this.mAppService.getHardwareReceiverService().getLastStrideForLocale();
        int noGhostColour = getGhostColourFor("stride");
        String value = curVal == -2.147483648E9d ? "---" : String.format("%.1f", Double.valueOf(curVal));
        String unit = Conversion.getUnitOfStride(this.mAppService, true);
        String label = this.mAppService.getString(R.string.vc_title_stride);
        updateElement(pageBoxInfo.page, "powerValue", "content", value);
        updateElement(pageBoxInfo.page, "powerValue", "color", Integer.valueOf(noGhostColour));
        updateElement(pageBoxInfo.page, "powerUnits", "content", unit);
        updateElement(pageBoxInfo.page, "powerLabel", "content", label);
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        switch (Prefs.getSportType()) {
            case RIDE:
                updateSpeed(pageBoxInfo);
                updatePower(pageBoxInfo, "power");
                updateCadence(pageBoxInfo, "cadence", R.string.cadence_unit_vert);
                break;
            case RUN:
                updatePace(pageBoxInfo);
                if (SensorList.isSensorConnected(Sensor.DataType.KICK, true)) {
                    updatePower(pageBoxInfo, "kick");
                } else {
                    updateStride(pageBoxInfo);
                }
                updateCadence(pageBoxInfo, "step", R.string.unit_cadence_run_short_caps_vert);
                break;
        }
        updateHeartrate(pageBoxInfo);
        long time = LiveRide.getTime();
        long sec = time % 60;
        long time2 = time / 60;
        long min = time2 % 60;
        long hour = time2 / 60;
        return String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
    }

    private int getGhostColourFor(String metric) {
        double curVal;
        if (LiveRide.isGhostWorkout()) {
            curVal = VirtualCoach.getLiveValue(metric);
        } else {
            curVal = VirtualCoach.getAverageValue(metric);
        }
        Target target = VirtualCoach.getTarget(metric);
        int noGhostColour = this.mAppService.getColor(R.color.single_unit_text);
        if (curVal != -2.147483648E9d && target != null) {
            if (target.isOutOfRange(curVal)) {
                return this.mAppService.getColor(R.color.behind_color);
            }
            return this.mAppService.getColor(R.color.ahead_color);
        }
        return noGhostColour;
    }

    private void updateGhostTopBar(String page) {
        String distanceLabel;
        String timeLabel;
        double distanceDiff;
        int titleColor;
        long timeDiff;
        String timeLabel2;
        if (isBound()) {
            GhostWorkout ghostWorkout = null;
            if (VirtualCoach.getVirtualWorkout() instanceof GhostWorkout) {
                ghostWorkout = (GhostWorkout) VirtualCoach.getVirtualWorkout();
            }
            int noGhostColour = this.mAppService.getColor(R.color.black);
            if (ghostWorkout == null) {
                this.mAppService.updateElement(page, "ghost_bar", "color", Integer.valueOf(noGhostColour));
                this.mAppService.updateElement(page, "ghost_time", "content", "");
                this.mAppService.updateElement(page, "ghost_distance", "content", "");
                this.mAppService.updateElement(page, "ghost_time_label", "content", "");
                this.mAppService.updateElement(page, "ghost_distance_label", "content", "");
                return;
            }
            double ghostDistance = ghostWorkout.getDistance();
            double currentDistance = this.mAppService.getHardwareReceiverService().getLastDistance();
            if (ghostDistance >= currentDistance) {
                distanceLabel = this.mAppService.getResources().getString(R.string.vc_title_ghost_behind);
                timeLabel = distanceLabel;
                distanceDiff = ghostDistance - currentDistance;
                titleColor = this.mAppService.getColor(currentDistance <= ghostDistance ? R.color.behind_color : R.color.ahead_color);
            } else {
                distanceLabel = this.mAppService.getResources().getString(R.string.vc_title_ghost_ahead);
                timeLabel = distanceLabel;
                distanceDiff = currentDistance - ghostDistance;
                titleColor = this.mAppService.getColor(R.color.ahead_color);
            }
            double speed = this.mAppService.getHardwareReceiverService().getLastSpeed();
            if (speed <= 0.0d) {
                speed = LiveRide.getAverageSpeed();
            }
            if (speed > 0.0d) {
                timeDiff = (long) (distanceDiff / speed);
            } else {
                timeDiff = LiveRide.getTime() / 1000;
            }
            if (timeDiff <= 60) {
                timeLabel2 = timeLabel + String.format(": 00:%02d", Long.valueOf(timeDiff));
            } else {
                int min = (int) (timeDiff / 60);
                int sec = (int) (timeDiff % 60);
                timeLabel2 = timeLabel + String.format(": %02d:%02d", Integer.valueOf(min), Integer.valueOf(sec));
            }
            String unit = Conversion.getUnitOfDistance(this.mAppService);
            boolean isMetricSystem = unit.equals(this.mAppService.getString(R.string.unit_distance_metric_short));
            double distance = isMetricSystem ? Utility.metersToKilometers(distanceDiff) : Utility.metersToMiles(distanceDiff);
            String distanceLabel2 = distanceLabel + String.format(": %.2f %s", Double.valueOf(distance), unit);
            boolean hideGhost = ghostWorkout.isLapMode() || !ghostWorkout.hasData();
            AppService appService = this.mAppService;
            if (!hideGhost) {
                noGhostColour = titleColor;
            }
            appService.updateElement(page, "ghost_bar", "color", Integer.valueOf(noGhostColour));
            AppService appService2 = this.mAppService;
            if (hideGhost) {
                timeLabel2 = "";
            }
            appService2.updateElement(page, "ghost_time", "content", timeLabel2);
            AppService appService3 = this.mAppService;
            if (hideGhost) {
                distanceLabel2 = "";
            }
            appService3.updateElement(page, "ghost_distance", "content", distanceLabel2);
            this.mAppService.updateElement(page, "ghost_time_label", "content", hideGhost ? "" : this.mAppService.getString(R.string.vc_title_ghost_time));
            this.mAppService.updateElement(page, "ghost_distance_label", "content", hideGhost ? "" : this.mAppService.getString(R.string.vc_title_ghost_distance));
        }
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateTopBar(PageBoxInfo pageBoxInfo) {
        super.updateTopBar(pageBoxInfo);
        updateGhostTopBar(pageBoxInfo.page);
    }
}
