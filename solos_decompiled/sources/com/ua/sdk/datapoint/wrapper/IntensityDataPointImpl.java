package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.IntensityDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class IntensityDataPointImpl implements IntensityDataPoint {
    Date dateTime;
    Double intensity;

    public IntensityDataPointImpl(Double intensity, Date dateTime) {
        this.dateTime = dateTime;
        this.intensity = intensity;
    }

    @Override // com.ua.sdk.datapoint.IntensityDataPoint
    public Double getIntensity() {
        return this.intensity;
    }

    @Override // com.ua.sdk.datapoint.IntensityDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
