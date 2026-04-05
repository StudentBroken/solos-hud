package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.EnergyExpendedDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class EnergyExpendedDataPointImpl implements EnergyExpendedDataPoint {
    Date dateTime;
    Double energy;

    public EnergyExpendedDataPointImpl(Double energy, Date dateTime) {
        this.energy = energy;
        this.dateTime = dateTime;
    }

    @Override // com.ua.sdk.datapoint.EnergyExpendedDataPoint
    public Double getEnergyExpended() {
        return this.energy;
    }

    @Override // com.ua.sdk.datapoint.EnergyExpendedDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
