package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class Calories extends Template<Long> {
    public Calories(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.CALORIES);
        setUnit(appService.getString(R.string.calories_unit_upper));
        setLabel(appService.getString(R.string.vc_title_calories));
        setImage(MetricResource.CALORIES);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return isBound() && LiveRide.getCurrentCalories() > 0;
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        return String.valueOf(LiveRide.getCurrentCalories());
    }
}
