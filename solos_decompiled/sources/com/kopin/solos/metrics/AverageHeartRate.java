package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class AverageHeartRate extends GhostPage<Long> {
    public AverageHeartRate(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.GHOST_AVERAGE_HEARTRATE);
        addPage(MetricType.AVERAGE_TARGET_HEARTRATE);
        setUnit(appService.getString(R.string.caps_bpm_vertical));
        setLabel(appService.getString(R.string.vc_title_average_heartrate));
        setImage(MetricResource.HEARTRATE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    protected void prepare() {
        super.prepare();
        setUnit(this.mAppService.getString(LiveRide.isGhostWorkout() ? R.string.caps_bpm_vertical : R.string.caps_bpm));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) LiveRide.getAverageHeartRate()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        return String.valueOf((int) LiveRide.getAverageHeartRate());
    }
}
