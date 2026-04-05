package com.ua.sdk.datapoint;

import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;

/* JADX INFO: loaded from: classes65.dex */
public interface DataFrame {
    Double getActiveTime();

    DataPoint getDataPoint(DataTypeRef dataTypeRef);

    DataPoint getDataPoint(DataTypeRef dataTypeRef, DataSourceIdentifier dataSourceIdentifier);

    DistanceDataPoint getDistanceDataPoint();

    DistanceDataPoint getDistanceDataPoint(DataSourceIdentifier dataSourceIdentifier);

    Double getElapsedTime();

    EnergyExpendedDataPoint getEnergyExpendedDataPoint();

    EnergyExpendedDataPoint getEnergyExpendedDataPoint(DataSourceIdentifier dataSourceIdentifier);

    Double getFirstSegmentStartTime();

    HeartRateDataPoint getHeartRateDataPoint();

    HeartRateDataPoint getHeartRateDataPoint(DataSourceIdentifier dataSourceIdentifier);

    IntensityDataPoint getIntensityDataPoint();

    IntensityDataPoint getIntensityDataPoint(DataSourceIdentifier dataSourceIdentifier);

    LocationDataPoint getLocationDataPoint();

    LocationDataPoint getLocationDataPoint(DataSourceIdentifier dataSourceIdentifier);

    RunCadenceDataPoint getRunCadenceDataPoint();

    RunCadenceDataPoint getRunCadenceDataPoint(DataSourceIdentifier dataSourceIdentifier);

    SpeedDataPoint getSpeedDataPoint();

    SpeedDataPoint getSpeedDataPoint(DataSourceIdentifier dataSourceIdentifier);

    WillPowerDataPoint getWillPowerDataPoint();

    WillPowerDataPoint getWillPowerDataPoint(DataSourceIdentifier dataSourceIdentifier);

    boolean isSegmentStarted();
}
