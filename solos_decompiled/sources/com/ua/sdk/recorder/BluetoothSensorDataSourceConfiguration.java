package com.ua.sdk.recorder;

import com.ua.sdk.recorder.data.BluetoothServiceType;

/* JADX INFO: loaded from: classes65.dex */
public interface BluetoothSensorDataSourceConfiguration extends DataSourceConfiguration<BluetoothSensorDataSourceConfiguration> {
    BluetoothSensorDataSourceConfiguration addProfileTypes(BluetoothServiceType... bluetoothServiceTypeArr);

    BluetoothSensorDataSourceConfiguration setDeviceAddress(String str);
}
