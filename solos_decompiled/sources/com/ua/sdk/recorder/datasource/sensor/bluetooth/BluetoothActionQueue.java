package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;
import java.util.ArrayDeque;
import java.util.UUID;

/* JADX INFO: loaded from: classes65.dex */
@TargetApi(18)
public class BluetoothActionQueue {
    private final ArrayDeque<BluetoothAction> actions = new ArrayDeque<>(32);
    private BluetoothGatt gatt;

    public BluetoothActionQueue(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public void addAction(BluetoothAction action) {
        this.actions.add(action);
    }

    protected boolean isEmpty() {
        return this.actions.isEmpty();
    }

    protected void poll() {
        synchronized (this.actions) {
            if (this.actions.peek() != null) {
                BluetoothAction action = this.actions.poll();
                switch (action.actionType) {
                    case WRITE:
                        action.characteristic.setValue(action.value);
                        this.gatt.writeCharacteristic(action.characteristic);
                        break;
                    case NOTIFY:
                        this.gatt.setCharacteristicNotification(action.characteristic, true);
                        BluetoothGattDescriptor descriptor = action.characteristic.getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR));
                        descriptor.setValue(action.value);
                        this.gatt.writeDescriptor(descriptor);
                        break;
                    case READ:
                        this.gatt.readCharacteristic(action.characteristic);
                        break;
                }
            }
        }
    }
}
