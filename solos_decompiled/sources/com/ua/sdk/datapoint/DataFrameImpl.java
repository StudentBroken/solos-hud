package com.ua.sdk.datapoint;

import com.ua.sdk.datapoint.wrapper.DistanceDataPointImpl;
import com.ua.sdk.datapoint.wrapper.EnergyExpendedDataPointImpl;
import com.ua.sdk.datapoint.wrapper.HeartRateDataPointImpl;
import com.ua.sdk.datapoint.wrapper.IntensityDataPointImpl;
import com.ua.sdk.datapoint.wrapper.LocationDataPointImpl;
import com.ua.sdk.datapoint.wrapper.RunCadenceDataPointImpl;
import com.ua.sdk.datapoint.wrapper.SpeedDataPointImpl;
import com.ua.sdk.datapoint.wrapper.WillPowerDataPointImpl;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.TypeSourceKey;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes65.dex */
public class DataFrameImpl implements DataFrame {
    private Double activeTime;
    private Map<String, DataPoint> dataPoints;
    private List<DataSourceIdentifier> dataSourceIdentifiersChanged;
    private Set<DataTypeRef> dataTypesChanged;
    private Double elapsedTime;
    private Double firstSegmentStartTime;
    private boolean isSegmentStarted;
    private Map<DataTypeRef, DataSourceIdentifier> primaryDataSources;

    @Override // com.ua.sdk.datapoint.DataFrame
    public Double getFirstSegmentStartTime() {
        return this.firstSegmentStartTime;
    }

    public void setFirstSegmentStartTime(Double firstSegmentStartTime) {
        this.firstSegmentStartTime = firstSegmentStartTime;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public Double getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(Double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public Double getActiveTime() {
        return this.activeTime;
    }

    public void setActiveTime(Double activeTime) {
        this.activeTime = activeTime;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public boolean isSegmentStarted() {
        return this.isSegmentStarted;
    }

    public void setSegmentStarted(boolean isSegmentStarted) {
        this.isSegmentStarted = isSegmentStarted;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public DataPoint getDataPoint(DataTypeRef dataTypeRef) {
        DataSourceIdentifier dataSourceIdentifier = this.primaryDataSources.get(dataTypeRef);
        if (dataSourceIdentifier == null) {
            return null;
        }
        return this.dataPoints.get(TypeSourceKey.createDataPointKey(dataTypeRef, dataSourceIdentifier));
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public DataPoint getDataPoint(DataTypeRef dataTypeRef, DataSourceIdentifier dataSourceIdentifier) {
        return this.dataPoints.get(TypeSourceKey.createDataPointKey(dataTypeRef, dataSourceIdentifier));
    }

    public Map<String, DataPoint> getDataPoints() {
        return this.dataPoints;
    }

    public void setDataPoints(Map<String, DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void setPrimaryDataSources(Map<DataTypeRef, DataSourceIdentifier> primaryDataSources) {
        this.primaryDataSources = primaryDataSources;
    }

    public Map<DataTypeRef, DataSourceIdentifier> getPrimaryDataSources() {
        return this.primaryDataSources;
    }

    public Set<DataTypeRef> getDataTypesChanged() {
        return this.dataTypesChanged;
    }

    public void setDataTypesChanged(Set<DataTypeRef> dataTypesChanged) {
        this.dataTypesChanged = dataTypesChanged;
    }

    public List<DataSourceIdentifier> getDataSourceIdentifiersChanged() {
        return this.dataSourceIdentifiersChanged;
    }

    public void setDataSourceIdentifiersChanged(List<DataSourceIdentifier> dataSourceIdentifiersChanged) {
        this.dataSourceIdentifiersChanged = dataSourceIdentifiersChanged;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public DistanceDataPoint getDistanceDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_DISTANCE.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        DistanceDataPoint distanceDataPoint = new DistanceDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_DISTANCE), dataPoint.getDatetime());
        return distanceDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public DistanceDataPoint getDistanceDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_DISTANCE.getRef());
        if (dataPoint == null) {
            return null;
        }
        DistanceDataPoint distanceDataPoint = new DistanceDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_DISTANCE), dataPoint.getDatetime());
        return distanceDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public EnergyExpendedDataPoint getEnergyExpendedDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        EnergyExpendedDataPoint energyExpendedDataPoint = new EnergyExpendedDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_ENERGY_EXPENDED), dataPoint.getDatetime());
        return energyExpendedDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public EnergyExpendedDataPoint getEnergyExpendedDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef());
        if (dataPoint == null) {
            return null;
        }
        EnergyExpendedDataPoint energyExpendedDataPoint = new EnergyExpendedDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_ENERGY_EXPENDED), dataPoint.getDatetime());
        return energyExpendedDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public HeartRateDataPoint getHeartRateDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_HEART_RATE.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        HeartRateDataPoint heartRateDataPoint = new HeartRateDataPointImpl(dataPoint.getValueLong(BaseDataTypes.FIELD_HEART_RATE), dataPoint.getDatetime());
        return heartRateDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public HeartRateDataPoint getHeartRateDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_HEART_RATE.getRef());
        if (dataPoint == null) {
            return null;
        }
        HeartRateDataPoint heartRateDataPoint = new HeartRateDataPointImpl(dataPoint.getValueLong(BaseDataTypes.FIELD_HEART_RATE), dataPoint.getDatetime());
        return heartRateDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public IntensityDataPoint getIntensityDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_INTENSITY.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        IntensityDataPoint intensityDataPoint = new IntensityDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_INTENSITY), dataPoint.getDatetime());
        return intensityDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public IntensityDataPoint getIntensityDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_INTENSITY.getRef());
        if (dataPoint == null) {
            return null;
        }
        IntensityDataPoint intensityDataPoint = new IntensityDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_INTENSITY), dataPoint.getDatetime());
        return intensityDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public LocationDataPoint getLocationDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_LOCATION.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        LocationDataPoint locationDataPoint = new LocationDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE), dataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE), dataPoint.getValueDouble(BaseDataTypes.FIELD_HORIZONTAL_ACCURACY), dataPoint.getDatetime());
        return locationDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public LocationDataPoint getLocationDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_LOCATION.getRef());
        if (dataPoint == null) {
            return null;
        }
        LocationDataPoint locationDataPoint = new LocationDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE), dataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE), dataPoint.getValueDouble(BaseDataTypes.FIELD_HORIZONTAL_ACCURACY), dataPoint.getDatetime());
        return locationDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public RunCadenceDataPoint getRunCadenceDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_RUN_CADENCE.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        RunCadenceDataPoint runCadenceDataPoint = new RunCadenceDataPointImpl(dataPoint.getValueLong(BaseDataTypes.FIELD_RUN_CADENCE), dataPoint.getDatetime());
        return runCadenceDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public RunCadenceDataPoint getRunCadenceDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_RUN_CADENCE.getRef());
        if (dataPoint == null) {
            return null;
        }
        RunCadenceDataPoint runCadenceDataPoint = new RunCadenceDataPointImpl(dataPoint.getValueLong(BaseDataTypes.FIELD_RUN_CADENCE), dataPoint.getDatetime());
        return runCadenceDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public SpeedDataPoint getSpeedDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_SPEED.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        SpeedDataPoint speedDataPoint = new SpeedDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_SPEED), dataPoint.getDatetime());
        return speedDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public SpeedDataPoint getSpeedDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_SPEED.getRef());
        if (dataPoint == null) {
            return null;
        }
        SpeedDataPoint speedDataPoint = new SpeedDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_SPEED), dataPoint.getDatetime());
        return speedDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public WillPowerDataPoint getWillPowerDataPoint(DataSourceIdentifier dataSourceIdentifier) {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_WILLPOWER.getRef(), dataSourceIdentifier);
        if (dataPoint == null) {
            return null;
        }
        WillPowerDataPoint willPowerDataPoint = new WillPowerDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_WILLPOWER), dataPoint.getDatetime());
        return willPowerDataPoint;
    }

    @Override // com.ua.sdk.datapoint.DataFrame
    public WillPowerDataPoint getWillPowerDataPoint() {
        DataPoint dataPoint = getDataPoint(BaseDataTypes.TYPE_WILLPOWER.getRef());
        if (dataPoint == null) {
            return null;
        }
        WillPowerDataPoint willPowerDataPoint = new WillPowerDataPointImpl(dataPoint.getValueDouble(BaseDataTypes.FIELD_WILLPOWER), dataPoint.getDatetime());
        return willPowerDataPoint;
    }
}
