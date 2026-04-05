package com.ua.sdk.datapoint.wrapper;

import com.ua.sdk.datapoint.LocationDataPoint;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class LocationDataPointImpl implements LocationDataPoint {
    Double accuracy;
    Date dateTime;
    Double latitude;
    Double longitude;

    public LocationDataPointImpl(Double latitude, Double longitude, Double accuracy, Date dateTime) {
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    @Override // com.ua.sdk.datapoint.LocationDataPoint
    public Double getLatitude() {
        return this.latitude;
    }

    @Override // com.ua.sdk.datapoint.LocationDataPoint
    public Double getLongitude() {
        return this.longitude;
    }

    @Override // com.ua.sdk.datapoint.LocationDataPoint
    public Double getAccuracy() {
        return this.accuracy;
    }

    @Override // com.ua.sdk.datapoint.LocationDataPoint
    public Date getDateTime() {
        return this.dateTime;
    }
}
