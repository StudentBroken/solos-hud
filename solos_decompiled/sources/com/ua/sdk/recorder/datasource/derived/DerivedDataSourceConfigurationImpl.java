package com.ua.sdk.recorder.datasource.derived;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.DerivedDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.DataSource;
import com.ua.sdk.recorder.datasource.derived.location.LocationDerivedDataSource;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes65.dex */
public class DerivedDataSourceConfigurationImpl extends AbstractDataSourceConfiguration implements DerivedDataSourceConfiguration {

    @SerializedName("data_source_type")
    public DerivedDataSourceConfiguration.DataSourceType dataSourceType;

    @SerializedName("priority")
    public int priority;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.recorder.DataSourceConfiguration
    public DerivedDataSourceConfiguration setDataSourceIdentifier(DataSourceIdentifier dataSourceIdentifier) {
        this.dataSourceIdentifier = dataSourceIdentifier;
        return this;
    }

    @Override // com.ua.sdk.recorder.DerivedDataSourceConfiguration
    public DerivedDataSourceConfiguration setDataSource(DerivedDataSourceConfiguration.DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
        return this;
    }

    @Override // com.ua.sdk.recorder.DerivedDataSourceConfiguration
    public DerivedDataSourceConfiguration setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override // com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration
    public DataSource build(SensorMessageProducer sensorMessageProducer, SensorDataSource.SensorDataSourceListener listener) {
        if (this.dataSourceIdentifier == null) {
            throw new IllegalArgumentException("A data source must be specified.");
        }
        if (this.dataSourceType == null) {
            throw new IllegalArgumentException("A data source type must be specified.");
        }
        switch (this.dataSourceType) {
            case BEARING:
                return new BearingDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_BEARING.getRef())));
            case CYCLING_POWER_SUMMARY:
                return new CyclingPowerSummaryDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_CYCLING_POWER_SUMMARY.getRef())));
            case DISTANCE:
                return new DistanceDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_DISTANCE.getRef())));
            case ELEVATION_SUMMARY:
                return new ElevationSummaryDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_ELEVATION_SUMMARY.getRef())));
            case ENERGY_EXPENDED:
                return new EnergyExpendedDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef())));
            case HEART_RATE_SUMMARY:
                return new HeartRateSummaryDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_HEART_RATE_SUMMARY.getRef())));
            case INTENSITY:
                return new IntensityDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_INTENSITY.getRef())));
            case LOCATION:
                return new LocationDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_LOCATION.getRef())));
            case RUN_CADENCE_SUMMARY:
                return new RunCadenceSummaryDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_RUN_CADENCE_SUMMARY.getRef())));
            case SPEED:
                return new SpeedDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_SPEED.getRef())));
            case SPEED_SUMMARY:
                return new SpeedSummaryDerivedDataSource(this.dataSourceIdentifier, new ArrayList(Arrays.asList(BaseDataTypes.TYPE_SPEED_SUMMARY.getRef())));
            default:
                return null;
        }
    }
}
