package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class Distance extends GhostPage<Long> {
    public Distance(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.DISTANCE);
        setUnit(Conversion.getUnitOfDistance(appService).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_distance));
        setImage(MetricResource.DISTANCE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return isBound() && this.mService.getLastDistance() > 0.0d;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    protected void prepare() {
        super.prepare();
        setUnit(Conversion.getUnitOfDistance(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        if (!isBound()) {
            return "";
        }
        super.updatePage(pageBoxInfo, updateValue);
        double distance = this.mService.getLastDistanceForLocale();
        return String.format(distance < 100.0d ? "%.2f" : "%.1f", Double.valueOf(distance));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfDistance(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }
}
