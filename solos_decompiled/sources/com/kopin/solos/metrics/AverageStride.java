package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes37.dex */
public class AverageStride extends GhostPage<Long> {
    public AverageStride(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        setUnit(Conversion.getUnitOfLength(this.mAppService).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_average_stride));
        setImage(MetricResource.STRIDE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) LiveRide.getAverageStride()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        return String.valueOf(Utility.roundToOneDecimalPlaces(LiveRide.getAverageStrideLocale()));
    }
}
