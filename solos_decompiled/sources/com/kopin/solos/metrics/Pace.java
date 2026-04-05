package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.util.PaceUtil;

/* JADX INFO: loaded from: classes37.dex */
public class Pace extends GhostPage<Double> {
    public Pace(AppService appService) {
        super(appService, TemplateManager.DataType.PACE);
        addPage(MetricType.PACE);
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_pace));
        setImage(MetricResource.PACE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        this.mService = service;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
        super.onServiceDisconnected();
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
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
        return PaceUtil.formatPace(Conversion.paceForLocale(this.mService.getLastPace()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }
}
