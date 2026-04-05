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
public class TargetSpeed extends GhostPage<Long> {
    public TargetSpeed(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.AVERAGE_TARGET_SPEED);
        addPage(MetricType.GHOST_AVERAGE_SPEED);
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, true));
        setImage(MetricResource.SPEED);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return super.isAvailable(Long.valueOf((long) Math.ceil(LiveRide.getAverageSpeed())));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        setLabel(this.mAppService.getString(R.string.vc_title_average_speed_abbrev));
        super.updatePage(pageBoxInfo, updateValue);
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
        return String.format("%.1f", Double.valueOf(LiveRide.getAverageSpeedForLocale()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }
}
