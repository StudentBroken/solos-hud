package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;

/* JADX INFO: loaded from: classes37.dex */
public class AveragePace extends GhostPage<Long> {
    public AveragePace(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.GHOST_AVERAGE_PACE);
        addPage(MetricType.AVERAGE_TARGET_PACE);
        addPage(MetricType.AVERAGE_PACE);
        setUnit(Conversion.getUnitOfPaceShort(appService));
        setLabel(appService.getString(R.string.vc_title_average_pace));
        setImage(MetricResource.PACE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) Math.ceil(LiveRide.getAveragePace())));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, LiveRide.isGhostWorkout()));
        return PaceUtil.formatPace(LiveRide.getAveragePaceLocale());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, LiveRide.isGhostWorkout()));
    }

    private void updateTargetAverage(PageBoxInfo pageBoxInfo) {
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService));
        long target = Utility.round(Conversion.paceForLocale(Prefs.getTargetAveragePaceValue()), 5);
        String targetAverage = target > 0 ? PaceUtil.formatPace(target) : "----";
        double currentAverageValue = LiveRide.getAveragePaceLocale();
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.target, "content", targetAverage);
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getResources().getColor(currentAverageValue <= ((double) target) ? R.color.ahead_color : R.color.behind_color)));
    }
}
