package com.ua.sdk.recorder.data;

import com.ua.sdk.datapoint.BaseDataTypes;

/* JADX INFO: loaded from: classes65.dex */
public enum BluetoothServiceType {
    HEART_RATE(BaseDataTypes.ID_HEART_RATE),
    RUN_SPEED_CADENCE("run_speed_cadence"),
    BIKE_POWER("bike_power"),
    BIKE_SPEED_CADENCE("bike_speed_cadence"),
    ARMOUR_39("armour_39");

    private String type;

    BluetoothServiceType(String type) {
        this.type = type;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.type;
    }

    public static BluetoothServiceType fromString(String type) {
        if (type != null) {
            BluetoothServiceType[] arr$ = values();
            for (BluetoothServiceType bluetoothServiceType : arr$) {
                if (bluetoothServiceType.type.equalsIgnoreCase(type)) {
                    return bluetoothServiceType;
                }
            }
        }
        return null;
    }
}
