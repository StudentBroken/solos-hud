package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.DistanceDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class DistanceDataPointImpl implements DistanceDataPoint {
    Date dateTime;
    Double distance;

    public DistanceDataPointImpl(Double distance, Date dateTime) {
        this.distance = distance;
        this.dateTime = dateTime;
    }

    @Override // com.ua.sdk.datapoint.DistanceDataPoint
    public Double getDistance() {
        return this.distance;
    }

    @Override // com.ua.sdk.datapoint.DistanceDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
