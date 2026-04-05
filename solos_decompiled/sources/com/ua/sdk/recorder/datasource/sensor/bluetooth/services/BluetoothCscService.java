package com.ua.sdk.recorder.datasource.sensor.bluetooth.services;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothAction;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothActionQueue;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.GattAttributes;
import java.util.UUID;

/* JADX INFO: loaded from: classes65.dex */
@TargetApi(18)
public class BluetoothCscService implements BaseGattCallback.GattCallbackListener {
    private BluetoothActionQueue actionQueue;
    private BluetoothClient.BluetoothClientListener listener;

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void setClientListener(BluetoothClient.BluetoothClientListener listener) {
        this.listener = listener;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void setBluetoothActionQueue(BluetoothActionQueue actionQueue) {
        this.actionQueue = actionQueue;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onUnexpectedDisconnect() {
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == 0 && gatt.getService(UUID.fromString(GattAttributes.CSC_SERVICE)) != null) {
            BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString(GattAttributes.CSC_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.CSC_MEASUREMENT_CHARACTERISTIC));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.NOTIFY, characteristic, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.READ, characteristic, null));
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        BluetoothGattCharacteristic controlPoint;
        if (status == 0 && characteristic.getUuid().equals(UUID.fromString(GattAttributes.RSC_MEASUREMENT_CHARACTERISTIC))) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            if ((flags & 1) != 0 && (controlPoint = gatt.getService(UUID.fromString(GattAttributes.CSC_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.CONTROL_POINT_CHARACTERISTIC))) != null) {
                this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.NOTIFY, controlPoint, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE));
            }
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        parseCharacteristic(characteristic);
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    private void parseCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (UUID.fromString(GattAttributes.CSC_MEASUREMENT_CHARACTERISTIC).equals(characteristic.getUuid())) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            int offset = 0 + 1;
            long wheelRevs = -1;
            if ((flags & 1) != 0) {
                wheelRevs = characteristic.getIntValue(20, offset).intValue();
                offset += 4;
            }
            long crankRevs = -1;
            if ((flags & 2) != 0) {
                crankRevs = characteristic.getIntValue(18, offset).intValue();
            }
            this.listener.onCSCMeasurement(wheelRevs, crankRevs);
        }
    }
}
