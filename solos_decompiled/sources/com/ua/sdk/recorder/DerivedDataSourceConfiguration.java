package com.ua.sdk.recorder;

/* JADX INFO: loaded from: classes65.dex */
public interface DerivedDataSourceConfiguration extends DataSourceConfiguration<DerivedDataSourceConfiguration> {

    public enum DataSourceType {
        BEARING,
        CYCLING_POWER_SUMMARY,
        DISTANCE,
        ELEVATION_SUMMARY,
        ENERGY_EXPENDED,
        HEART_RATE_SUMMARY,
        INTENSITY,
        LOCATION,
        RUN_CADENCE_SUMMARY,
        SPEED,
        SPEED_SUMMARY
    }

    DerivedDataSourceConfiguration setDataSource(DataSourceType dataSourceType);

    DerivedDataSourceConfiguration setPriority(int i);
}
