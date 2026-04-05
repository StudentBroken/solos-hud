package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.WillPowerDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WillPowerDataPointImpl implements WillPowerDataPoint {
    Date dateTime;
    Double willPower;

    public WillPowerDataPointImpl(Double willPower, Date dateTime) {
        this.dateTime = dateTime;
        this.willPower = willPower;
    }

    @Override // com.ua.sdk.datapoint.WillPowerDataPoint
    public Double getWillPower() {
        return this.willPower;
    }

    @Override // com.ua.sdk.datapoint.WillPowerDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
