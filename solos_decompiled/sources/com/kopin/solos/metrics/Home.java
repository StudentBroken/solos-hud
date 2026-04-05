package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;

/* JADX INFO: loaded from: classes37.dex */
public class Home extends Template<Number> {
    private HardwareReceiverService mService;

    public Home(AppService appService) {
        super(appService, TemplateManager.DataType.VOID);
        addPage("home");
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Number value) {
        return true;
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mService = service;
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Number updateValue) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected String updatePage(PageBoxInfo pageBoxInfo, Number updateValue) {
        return "";
    }
}
