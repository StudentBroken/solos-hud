package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.SpeedDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class SpeedDataPointImpl implements SpeedDataPoint {
    Date dateTime;
    Double speed;

    public SpeedDataPointImpl(Double speed, Date dateTime) {
        this.dateTime = dateTime;
        this.speed = speed;
    }

    @Override // com.ua.sdk.datapoint.SpeedDataPoint
    public Double getSpeed() {
        return this.speed;
    }

    @Override // com.ua.sdk.datapoint.SpeedDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
