package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.TimeHelper;

/* JADX INFO: loaded from: classes37.dex */
public class TargetDistance extends Template<Long> {
    private boolean hasCompleted;
    private boolean hasShownCompletion;
    private HardwareReceiverService mService;
    private TimeHelper mTimeHelper;
    private long mTimeStamp;
    private boolean showDropDown;
    private long timeSinceTargetReached;

    public TargetDistance(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        this.hasShownCompletion = false;
        this.showDropDown = false;
        this.timeSinceTargetReached = 0L;
        this.hasCompleted = false;
        addPage(MetricType.TARGET_DISTANCE);
        setUnit(Conversion.getUnitOfDistance(this.mAppService).toUpperCase());
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        if (LiveRide.isActiveFtp() || LiveRide.isGhostWorkout()) {
            return false;
        }
        double targetDistance = Prefs.getTargetDistance();
        if (targetDistance == 0.0d || this.hasCompleted) {
            return false;
        }
        return !this.hasShownCompletion || this.timeSinceTargetReached < ((long) Prefs.getHeadsetScreenRefreshTime());
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mService = service;
        this.mTimeHelper = service.getTimeHelper();
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
        this.mTimeHelper = null;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void start() {
        super.start();
        this.showDropDown = false;
        this.hasCompleted = false;
        this.hasShownCompletion = false;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        if (this.hasShownCompletion) {
            this.hasCompleted = true;
        }
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
        if (isAvailable(updateValue)) {
            double distance = this.mService.getLastDistanceForLocale();
            double targetDistance = Prefs.getTargetDistance();
            if (distance < targetDistance) {
                this.showDropDown = false;
            } else if (LiveRide.isActiveRide() && Prefs.hasTargetDistance() && !this.showDropDown) {
                this.mAppService.sendDropDownNotification(AppService.PUPIL_IMG, 4000, this.mAppService.getString(R.string.target_reached_text, new Object[]{this.mAppService.getString(R.string.distance)}));
                this.showDropDown = true;
            }
            this.timeSinceTargetReached = (this.mTimeHelper.getTime() - this.mTimeStamp) / 1000;
        }
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        double targetDistance = Prefs.getTargetDistance();
        double distance = this.mService.getLastDistanceForLocale();
        String distStr = "0";
        int progress = 100;
        if (distance < targetDistance) {
            double diff = targetDistance - distance;
            distStr = String.format(diff < 100.0d ? "%.2f" : "%.1f", Double.valueOf(diff));
            progress = (int) ((100.0d * distance) / targetDistance);
        } else {
            this.hasShownCompletion = true;
            this.mTimeStamp = this.mTimeHelper.getTime();
        }
        this.mAppService.updateElement(MetricType.TARGET_DISTANCE.getResource(), "bottom_text", "content", this.mAppService.getString(distance < targetDistance ? R.string.vc_title_target_distance : R.string.target_distance_passed));
        this.mAppService.updateElement(pageBoxInfo.metric, "progressbar", "progress", Integer.valueOf(progress));
        return distStr;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfDistance(this.mAppService).toUpperCase());
    }
}
