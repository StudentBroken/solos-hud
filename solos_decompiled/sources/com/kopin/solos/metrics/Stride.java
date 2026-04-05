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

/* JADX INFO: loaded from: classes37.dex */
public class Stride extends GhostPage<Double> {
    public Stride(AppService appService) {
        super(appService, TemplateManager.DataType.STRIDE);
        addPage(MetricType.STRIDE);
        setUnit(Conversion.getUnitOfStride(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_stride));
        setImage(MetricResource.STRIDE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        return isBound() && value.doubleValue() > 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        double stride = Conversion.strideForLocale(updateValue.doubleValue());
        return String.valueOf(Utility.trimDecimalPlaces(stride, 0, true));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfStride(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }
}
