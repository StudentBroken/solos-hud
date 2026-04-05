package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.TimeHelper;

/* JADX INFO: loaded from: classes37.dex */
public class TargetTime extends Template<Long> {
    private boolean hasCompleted;
    private boolean hasShownCompletion;
    private boolean mShownMessage;
    private TimeHelper mTimeHelper;
    private long shownTime;

    public TargetTime(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        this.mShownMessage = false;
        this.hasShownCompletion = false;
        this.hasCompleted = false;
        this.shownTime = 0L;
        addPage(MetricType.TARGET_TIME);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        if (!Prefs.hasTargetTime() || this.hasCompleted) {
            return false;
        }
        long time = (this.mTimeHelper.getTime() - this.shownTime) / 1000;
        return !this.hasShownCompletion || time < ((long) Prefs.getHeadsetScreenRefreshTime());
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mTimeHelper = service.getTimeHelper();
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
        if (isAvailable(updateValue)) {
            long targetTime = Prefs.getTargetRideTimeInMillis();
            boolean showDropDown = (this.mShownMessage || !LiveRide.isActiveRide() || this.mTimeHelper.getTime() <= targetTime || LiveRide.isActiveFtp() || LiveRide.isVirtualWorkout()) ? false : true;
            if (showDropDown) {
                this.mAppService.sendDropDownNotification(AppService.PUPIL_IMG, 4000, this.mAppService.getString(R.string.target_reached_text, new Object[]{this.mAppService.getString(R.string.time)}));
                this.mShownMessage = true;
            }
        }
    }

    @Override // com.kopin.solos.metrics.Template
    protected void start() {
        super.start();
        this.mShownMessage = false;
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
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        long targetTime = LiveRide.getTargetTime();
        long time = (targetTime - this.mTimeHelper.getTime()) / 1000;
        long absTime = Math.abs(time);
        long sec = absTime % 60;
        long absTime2 = absTime / 60;
        long min = absTime2 % 60;
        long hour = absTime2 / 60;
        this.mAppService.updateElement(MetricType.TARGET_TIME.getResource(), "bottom_text", "content", this.mAppService.getString(time > 0 ? R.string.count_down_time : R.string.target_time_passed));
        this.mAppService.updateElement(pageBoxInfo.metric, pageBoxInfo.value, "color", this.mTimeHelper.isStarted() ? "default_font" : Integer.valueOf(this.mAppService.getResources().getColor(R.color.unfocused_grey)));
        if (time <= 0) {
            int timePassedColor = this.mAppService.getResources().getColor(R.color.single_unit_text);
            this.mAppService.updateElement(pageBoxInfo.metric, pageBoxInfo.value, "color", Integer.valueOf(timePassedColor));
            this.shownTime = this.mTimeHelper.getTime();
            this.hasShownCompletion = true;
            return TimeHelper.ZERO;
        }
        return String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
    }
}
