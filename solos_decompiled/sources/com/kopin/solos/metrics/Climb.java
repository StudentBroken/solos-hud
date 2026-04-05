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
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes37.dex */
public class Climb extends Template<Float> {
    public Climb(AppService appService) {
        super(appService, TemplateManager.DataType.ELEVATION);
        addPage(MetricType.OVERALL_CLIMB);
        setUnit(Conversion.getUnitOfLength(this.mAppService, true).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_overall_climb));
        setImage(MetricResource.ELEVATION);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Float value) {
        return isBound() && value.floatValue() != -2.14748365E9f;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Float updateValue) {
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Float updateValue) {
        double overallClimb = Utility.convertToUserUnits(0, LiveRide.getOverallClimb());
        return String.valueOf((int) overallClimb);
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfLength(this.mAppService, true).toUpperCase());
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceConnected(HardwareReceiverService service) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceDisconnected() {
    }
}
