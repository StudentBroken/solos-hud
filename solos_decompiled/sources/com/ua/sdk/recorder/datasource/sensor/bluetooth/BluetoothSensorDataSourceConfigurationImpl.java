package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.BluetoothSensorDataSourceConfiguration;
import com.ua.sdk.recorder.data.BluetoothServiceType;
import com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.DataSource;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes65.dex */
public class BluetoothSensorDataSourceConfigurationImpl extends AbstractDataSourceConfiguration implements BluetoothSensorDataSourceConfiguration {

    @SerializedName("device_address")
    public String deviceAddress;

    @SerializedName("profile_type")
    public Set<BluetoothServiceType> serviceTypes;

    @Override // com.ua.sdk.recorder.BluetoothSensorDataSourceConfiguration
    public BluetoothSensorDataSourceConfiguration addProfileTypes(BluetoothServiceType... type) {
        if (this.serviceTypes == null) {
            this.serviceTypes = new HashSet();
        }
        this.serviceTypes.addAll(Arrays.asList(type));
        return this;
    }

    @Override // com.ua.sdk.recorder.BluetoothSensorDataSourceConfiguration
    public BluetoothSensorDataSourceConfiguration setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.recorder.DataSourceConfiguration
    public BluetoothSensorDataSourceConfiguration setDataSourceIdentifier(DataSourceIdentifier dataSourceIdentifier) {
        this.dataSourceIdentifier = dataSourceIdentifier;
        return this;
    }

    @Override // com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration
    public DataSource build(SensorMessageProducer sensorMessageProducer, SensorDataSource.SensorDataSourceListener listener) {
        if (this.serviceTypes == null) {
            throw new IllegalArgumentException("Must specify at least one bluetooth service type");
        }
        if (this.deviceAddress == null) {
            throw new IllegalArgumentException("Must specify a valid device address");
        }
        BluetoothClient bluetoothClient = new BluetoothClientBuilder(this.serviceTypes).build();
        List<DataTypeRef> dataTypeRefs = new ArrayList<>();
        for (BluetoothServiceType serviceType : this.serviceTypes) {
            dataTypeRefs.addAll(BluetoothDataTypeRefMapper.getDataTypeRefFromService(serviceType));
        }
        return new BluetoothSensorDataSource(this.dataSourceIdentifier, dataTypeRefs, sensorMessageProducer, bluetoothClient, this.deviceAddress, listener);
    }
}
