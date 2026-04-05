package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.HeartRateDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateDataPointImpl implements HeartRateDataPoint {
    Date dateTime;
    Long hearRate;

    public HeartRateDataPointImpl(Long hearRate, Date dateTime) {
        this.dateTime = dateTime;
        this.hearRate = hearRate;
    }

    @Override // com.ua.sdk.datapoint.HeartRateDataPoint
    public Long getHeartRate() {
        return this.hearRate;
    }

    @Override // com.ua.sdk.datapoint.HeartRateDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
