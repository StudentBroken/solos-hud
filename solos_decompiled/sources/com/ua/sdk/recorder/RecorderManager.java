package com.ua.sdk.recorder;

import com.ua.sdk.UaException;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilder;
import com.ua.sdk.device.DeviceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public interface RecorderManager {

    public interface CreateCallback {
        void onCreated(Recorder recorder, UaException uaException);
    }

    void addRecorderManagerObserver(RecorderManagerObserver recorderManagerObserver);

    BluetoothSensorDataSourceConfiguration createBluetoothDataSourceConfiguration();

    DerivedDataSourceConfiguration createDerivedDataSourceConfiguration();

    LocationSensorDataSourceConfiguration createLocationDataSourceConfiguration();

    void createRecorder(RecorderConfiguration recorderConfiguration, CreateCallback createCallback);

    RecorderConfiguration createRecorderConfiguration();

    DataSourceIdentifierBuilder getDataSourceIdentifierBuilder();

    DeviceBuilder getDeviceBuilder();

    Recorder getRecorder(String str);

    void removeRecorderManagerObserver(RecorderManagerObserver recorderManagerObserver);
}
