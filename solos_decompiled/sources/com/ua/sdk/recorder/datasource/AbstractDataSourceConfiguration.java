package com.ua.sdk.recorder.datasource;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.producer.SensorMessageProducer;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AbstractDataSourceConfiguration {

    @SerializedName("data_source")
    public DataSourceIdentifier dataSourceIdentifier;

    public abstract DataSource build(SensorMessageProducer sensorMessageProducer, SensorDataSource.SensorDataSourceListener sensorDataSourceListener);
}
