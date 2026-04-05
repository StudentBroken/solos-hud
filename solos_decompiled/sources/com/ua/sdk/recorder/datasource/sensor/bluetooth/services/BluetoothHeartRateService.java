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
public class BluetoothHeartRateService implements BaseGattCallback.GattCallbackListener {
    private BluetoothActionQueue actionQueue;
    private BluetoothGattCharacteristic characteristic;
    private boolean isReconnect;
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
        this.isReconnect = true;
    }

    public void startWorkout() {
        this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.READ, this.characteristic, null));
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == 0 && gatt.getService(UUID.fromString(GattAttributes.HEART_RATE_SERVICE)) != null) {
            this.characteristic = gatt.getService(UUID.fromString(GattAttributes.HEART_RATE_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.HEART_RATE_CHARACTERISTIC));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.NOTIFY, this.characteristic, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
            startWorkout();
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        BluetoothGattCharacteristic controlPointCharacteristic;
        if (status == 0 && gatt.getService(UUID.fromString(GattAttributes.HEART_RATE_SERVICE)) != null && characteristic.getUuid().equals(UUID.fromString(GattAttributes.HEART_RATE_CHARACTERISTIC))) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            if ((flags & 8) != 0 && !this.isReconnect && (controlPointCharacteristic = gatt.getService(UUID.fromString(GattAttributes.HEART_RATE_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.HEART_RATE_CONTROL_POINT_CHARACTERISTIC))) != null) {
                this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.WRITE, controlPointCharacteristic, new byte[]{0}));
            }
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (gatt.getService(UUID.fromString(GattAttributes.HEART_RATE_SERVICE)) != null) {
            parseCharacteristic(characteristic);
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    private void parseCharacteristic(BluetoothGattCharacteristic characteristic) {
        int format;
        int offset;
        if (UUID.fromString(GattAttributes.HEART_RATE_CHARACTERISTIC).equals(characteristic.getUuid())) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            if ((flags & 1) != 0) {
                format = 18;
                offset = 3;
            } else {
                format = 17;
                offset = 2;
            }
            Integer heartRate = characteristic.getIntValue(format, 1);
            long joules = -1;
            if ((flags & 8) != 0) {
                Integer kJoules = characteristic.getIntValue(18, offset);
                joules = kJoules.intValue() / 1000;
            }
            this.listener.onHeartRateMeasurement(heartRate.longValue(), joules);
        }
    }
}
