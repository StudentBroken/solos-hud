package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

/* JADX INFO: loaded from: classes65.dex */
public class BluetoothAction {
    protected Action actionType;
    protected BluetoothGattCharacteristic characteristic;
    protected byte[] value;

    public enum Action {
        READ,
        NOTIFY,
        WRITE
    }

    public BluetoothAction(Action action, BluetoothGattCharacteristic characteristic, byte[] value) {
        this.actionType = action;
        this.characteristic = characteristic;
        this.value = value;
    }

    public BluetoothAction(Action action, BluetoothGattCharacteristic characteristic) {
        this(action, characteristic, null);
    }
}
