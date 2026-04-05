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
public class BluetoothCyclingPowerService implements BaseGattCallback.GattCallbackListener {
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
        if (status == 0 && gatt.getService(UUID.fromString(GattAttributes.CYCLING_POWER_CHARACTERISTIC)) != null) {
            BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString(GattAttributes.CYCLING_POWER_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.CYCLING_POWER_CHARACTERISTIC));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.NOTIFY, characteristic, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == 0) {
            parseCharacteristic(characteristic);
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
        if (UUID.fromString(GattAttributes.CYCLING_POWER_CHARACTERISTIC).equals(characteristic.getUuid())) {
            int flags = characteristic.getIntValue(18, 0).intValue();
            int offset = 0 + 2;
            int power = characteristic.getIntValue(34, offset).intValue();
            int offset2 = offset + 2;
            double pedalPowerBalance = -1.0d;
            if ((flags & 1) != 0) {
                Integer pedalPowerRaw = characteristic.getIntValue(17, offset2);
                pedalPowerBalance = ((double) pedalPowerRaw.intValue()) / 2.0d;
                offset2++;
            }
            double accumulatedTorque = -1.0d;
            if ((flags & 4) != 0) {
                Integer torqueRaw = characteristic.getIntValue(18, offset2);
                accumulatedTorque = ((double) torqueRaw.intValue()) / 32.0d;
                offset2 += 2;
            }
            long wheelRevs = -1;
            if ((flags & 16) != 0) {
                wheelRevs = characteristic.getIntValue(20, offset2).intValue();
                offset2 += 4;
            }
            long crankRevs = -1;
            if ((flags & 32) != 0) {
                crankRevs = characteristic.getIntValue(18, offset2).intValue();
                offset2 += 2;
            }
            long maxForce = -1;
            long minForce = -1;
            if ((flags & 64) != 0) {
                maxForce = characteristic.getIntValue(34, offset2).intValue();
                int offset3 = offset2 + 2;
                minForce = characteristic.getIntValue(34, offset3).intValue();
                offset2 = offset3 + 2;
            }
            double maxTorque = -1.0d;
            double minTorque = -1.0d;
            if ((flags & 128) != 0) {
                Integer maxTorqueRaw = characteristic.getIntValue(34, offset2);
                maxTorque = ((double) maxTorqueRaw.intValue()) / 32.0d;
                int offset4 = offset2 + 2;
                Integer minTorqueRaw = characteristic.getIntValue(34, offset4);
                minTorque = ((double) minTorqueRaw.intValue()) / 32.0d;
                offset2 = offset4 + 2;
            }
            long maxAngle = -1;
            long minAngle = -1;
            if ((flags & 256) != 0) {
                byte[] data = characteristic.getValue();
                byte data0 = data[offset2];
                byte data1 = data[offset2 + 1];
                byte data2 = data[offset2 + 2];
                int data1MaxAngle = data1 & 255;
                int data1MinAngle = data1 & 65280;
                maxAngle = data0 + data1MaxAngle;
                minAngle = data2 + data1MinAngle;
                offset2 += 3;
            }
            long topDeadSpot = -1;
            if ((flags & 512) != 0) {
                topDeadSpot = characteristic.getIntValue(18, offset2).intValue();
                offset2 += 2;
            }
            long bottomDeadSpot = -1;
            if ((flags & 1024) != 0) {
                bottomDeadSpot = characteristic.getIntValue(18, offset2).intValue();
                offset2 += 2;
            }
            long joules = -1;
            if ((flags & 2048) != 0) {
                int energyExpended = characteristic.getIntValue(18, offset2).intValue();
                joules = energyExpended / 1000;
            }
            this.listener.onCyclingPowerMeasurement(power, pedalPowerBalance, accumulatedTorque, wheelRevs, crankRevs, maxForce, minForce, maxTorque, minTorque, maxAngle, minAngle, topDeadSpot, bottomDeadSpot, joules);
        }
    }
}
