package com.ua.sdk.recorder;

import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;

/* JADX INFO: loaded from: classes65.dex */
public interface SensorDataSourceObserver {
    void onSensorDataSourceStatus(DataSourceIdentifier dataSourceIdentifier, SensorStatus sensorStatus, SensorHealth sensorHealth);
}
