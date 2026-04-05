package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class Countdown extends Template<Number> {
    int mCounter;

    public Countdown(AppService appService) {
        super(appService, TemplateManager.DataType.VOID);
        this.mCounter = 3;
        addPage(MetricType.COUNTDOWN);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Number value) {
        return RideControl.hasCountdown();
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceConnected(HardwareReceiverService service) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected void onServiceDisconnected() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Number updateValue) {
    }

    @Override // com.kopin.solos.metrics.Template
    protected String updatePage(PageBoxInfo pageBoxInfo, Number updateValue) {
        setMessage();
        this.mAppService.updateElement(MetricType.COUNTDOWN.getResource(), "countdown_counter", "content", String.format("%d", Integer.valueOf(this.mCounter)));
        return "";
    }

    @Override // com.kopin.solos.metrics.Template
    void update(Number value, boolean sensorValue, boolean force) {
        super.update(value, sensorValue, force);
        if (value != null) {
            this.mCounter = value.intValue();
            this.mAppService.updateElement(MetricType.COUNTDOWN.getResource(), "countdown_counter", "content", String.format("%d", Integer.valueOf(this.mCounter)));
        }
        setMessage();
    }

    void setMessage() {
        this.mAppService.updateElement(MetricType.COUNTDOWN.getResource(), "countdown_msg", "content", this.mAppService.getStringFromTheme(R.attr.strCountdownMsgStart));
    }
}
