package com.kopin.solos.metrics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class NormalisedPower extends GhostPage<Double> {
    public NormalisedPower(AppService appService) {
        super(appService, TemplateManager.DataType.DERIVED_POWER);
        addPage(MetricType.POWER_NORMALISED);
        setUnit(appService.getString(R.string.power_unit_abbrev));
        setImage(MetricResource.DERIVED_POWER);
        setLabel(appService.getString(R.string.vc_title_normalised_power));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        return value.doubleValue() > 0.0d && LiveRide.getPowerNormalised() > 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        this.mAppService.updateElement(MetricType.POWER.getResource(), FirebaseAnalytics.Param.VALUE, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        return String.valueOf((int) LiveRide.getPowerNormalised());
    }
}
