package com.ua.sdk.recorder.datasource.sensor;

import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.SensorHealth;
import com.ua.sdk.recorder.SensorStatus;
import com.ua.sdk.recorder.datasource.DataSource;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public abstract class SensorDataSource extends DataSource {
    protected SensorDataSourceListener sensorDataSourceListener;
    protected SensorHealth sensorHealth;
    protected SensorMessageProducer sensorMessageProducer;
    protected SensorStatus sensorStatus;

    public interface SensorDataSourceListener {
        void onSensorStateChanged(DataSourceIdentifier dataSourceIdentifier, SensorStatus sensorStatus, SensorHealth sensorHealth);
    }

    public SensorDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs, SensorMessageProducer sensorMessageProducer, SensorDataSourceListener listener) {
        super(dataSourceIdentifier, dataTypeRefs);
        this.sensorStatus = SensorStatus.DISCONNECTED;
        this.sensorHealth = SensorHealth.UNKNOWN;
        this.sensorMessageProducer = sensorMessageProducer;
        this.sensorDataSourceListener = listener;
    }

    public void sendData(DataPoint dataPoint, DataTypeRef dataTypeRef, DataSourceIdentifier dataSourceIdentifier) {
        this.sensorMessageProducer.dealWithIt(dataSourceIdentifier, dataPoint, dataTypeRef);
    }

    public SensorStatus getSensorStatus() {
        return this.sensorStatus;
    }

    public SensorHealth getSensorHealth() {
        return this.sensorHealth;
    }
}
