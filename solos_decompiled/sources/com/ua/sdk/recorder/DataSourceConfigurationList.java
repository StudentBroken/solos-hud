package com.ua.sdk.recorder;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes65.dex */
public class DataSourceConfigurationList {

    @SerializedName("bluetooth_data_source_configuration")
    ArrayList<BluetoothSensorDataSourceConfiguration> bluetoothDataSourceConfigurations;

    @SerializedName("derived_data_source_configuration")
    ArrayList<DerivedDataSourceConfiguration> derivedDataSourceConfigurations;

    @SerializedName("location_data_source_configuration")
    ArrayList<LocationSensorDataSourceConfiguration> locationDataSourceConfigurations;

    public ArrayList<BluetoothSensorDataSourceConfiguration> getBluetoothDataSourceConfigurations() {
        return this.bluetoothDataSourceConfigurations;
    }

    public ArrayList<LocationSensorDataSourceConfiguration> getLocationDataSourceConfigurations() {
        return this.locationDataSourceConfigurations;
    }

    public ArrayList<DerivedDataSourceConfiguration> getDerivedDataSourceConfigurations() {
        return this.derivedDataSourceConfigurations;
    }

    public boolean isBluetoothDataSourceConfigurationEmpty() {
        return this.bluetoothDataSourceConfigurations == null || this.bluetoothDataSourceConfigurations.isEmpty();
    }

    public boolean isLocationDataSourceConfigurationEmpty() {
        return this.locationDataSourceConfigurations == null || this.locationDataSourceConfigurations.isEmpty();
    }

    public boolean isDerivedDataSourceConfigurationEmpty() {
        return this.derivedDataSourceConfigurations == null || this.derivedDataSourceConfigurations.isEmpty();
    }

    public void addBluetoothDataSourceConfiguration(BluetoothSensorDataSourceConfiguration bluetoothMessageProducerConfiguration) {
        if (this.bluetoothDataSourceConfigurations == null) {
            this.bluetoothDataSourceConfigurations = new ArrayList<>();
        }
        this.bluetoothDataSourceConfigurations.add(bluetoothMessageProducerConfiguration);
    }

    public void addLocationDataSourceConfiguration(LocationSensorDataSourceConfiguration locationMessageProducerConfiguration) {
        if (this.locationDataSourceConfigurations == null) {
            this.locationDataSourceConfigurations = new ArrayList<>();
        }
        this.locationDataSourceConfigurations.add(locationMessageProducerConfiguration);
    }

    public void addDerivedDataSourceConfiguration(DerivedDataSourceConfiguration derivedDataSourceConfiguration) {
        if (this.derivedDataSourceConfigurations == null) {
            this.derivedDataSourceConfigurations = new ArrayList<>();
        }
        this.derivedDataSourceConfigurations.add(derivedDataSourceConfiguration);
    }
}
