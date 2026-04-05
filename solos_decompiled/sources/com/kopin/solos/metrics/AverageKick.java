package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class AverageKick extends GhostPage<Long> {
    public AverageKick(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.AVERAGE_TARGET_KICK);
        addPage(MetricType.GHOST_AVERAGE_KICK);
        addPage(MetricType.AVERAGE_KICK);
        setUnit(appService.getString(R.string.power_unit_abbrev));
        setLabel(appService.getString(R.string.vc_title_average_power));
        setImage(MetricResource.KICK);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) LiveRide.getAveragePower()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        return String.valueOf((int) LiveRide.getAveragePower());
    }
}
