package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class AverageCadence extends GhostPage<Long> {
    public AverageCadence(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.GHOST_AVERAGE_CADENCE);
        addPage(MetricType.AVERAGE_TARGET_CADENCE);
        setUnit(appService.getString(R.string.cadence_unit_vert));
        setLabel(appService.getString(R.string.vc_title_average_cadence));
        setImage(MetricResource.CADENCE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    protected void prepare() {
        super.prepare();
        setUnit(this.mAppService.getString(LiveRide.isGhostWorkout() ? R.string.cadence_unit_vert : R.string.cadence_unit_caps));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) LiveRide.getAverageCadence()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        return String.valueOf((int) LiveRide.getAverageCadence());
    }
}
