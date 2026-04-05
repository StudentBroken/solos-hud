package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.RunCadenceDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class RunCadenceDataPointImpl implements RunCadenceDataPoint {
    Long cadence;
    Date dateTime;

    public RunCadenceDataPointImpl(Long cadence, Date dateTime) {
        this.dateTime = dateTime;
        this.cadence = cadence;
    }

    @Override // com.ua.sdk.datapoint.RunCadenceDataPoint
    public Long getRunCadence() {
        return this.cadence;
    }

    @Override // com.ua.sdk.datapoint.RunCadenceDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
