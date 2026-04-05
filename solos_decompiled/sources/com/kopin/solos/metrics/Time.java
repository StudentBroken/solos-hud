package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.TimeHelper;

/* JADX INFO: loaded from: classes37.dex */
public class Time extends GhostPage<Long> {
    public Time(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.TIME);
        addPage(MetricType.TIME_GHOST);
        addPage(AppService.PAUSED_PAGE);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean before(Long value) {
        if (!Prefs.isReady() && this.mTimeHelper.isStarted()) {
            RideControl.stop(true);
            return false;
        }
        return false;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return Prefs.isReady();
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        int greyColor = this.mAppService.getColor(R.color.unfocused_grey);
        if (!isBound()) {
            if (!this.mTimeHelper.isStarted()) {
                this.mAppService.updateElement(MetricType.TIME.getResource(), "bottom_text", "content", this.mAppService.getStringFromTheme(R.attr.strAutoStartMsg));
                this.mAppService.updateElement(pageBoxInfo.metric, pageBoxInfo.value, "color", Integer.valueOf(greyColor));
            }
            return TimeHelper.ZERO;
        }
        long time = this.mTimeHelper.getTime() / 1000;
        long sec = time % 60;
        long time2 = time / 60;
        long min = time2 % 60;
        long hour = time2 / 60;
        if (this.mTimeHelper.isStarted()) {
            if (!this.mTimeHelper.isPaused()) {
                this.mAppService.updateElement(MetricType.TIME.getResource(), "bottom_text", "content", this.mAppService.getString(R.string.elapsed_time));
            } else {
                this.mAppService.updateElement(pageBoxInfo.metric, "pause_message", "content", this.mAppService.getString(Prefs.isAutoPauseEnabled() ? R.string.auto_pause_message : R.string.manual_pause_message));
            }
            return String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
        }
        this.mAppService.updateElement(MetricType.TIME.getResource(), "bottom_text", "content", this.mAppService.getStringFromTheme(R.attr.strAutoStartMsg));
        this.mAppService.updateElement(pageBoxInfo.metric, pageBoxInfo.value, "color", Integer.valueOf(greyColor));
        return TimeHelper.ZERO;
    }
}
