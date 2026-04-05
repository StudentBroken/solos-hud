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
public class BluetoothRscService implements BaseGattCallback.GattCallbackListener {
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
        if (status == 0 && gatt.getService(UUID.fromString(GattAttributes.RSC_SERVICE)) != null) {
            BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString(GattAttributes.RSC_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.RSC_MEASUREMENT_CHARACTERISTIC));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.NOTIFY, characteristic, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
            this.actionQueue.addAction(new BluetoothAction(BluetoothAction.Action.READ, characteristic, null));
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.GattCallbackListener
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        BluetoothGattCharacteristic controlPoint;
        if (status == 0 && characteristic.getUuid().equals(UUID.fromString(GattAttributes.RSC_MEASUREMENT_CHARACTERISTIC))) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            if ((flags & 2) != 0 && (controlPoint = gatt.getService(UUID.fromString(GattAttributes.RSC_SERVICE)).getCharacteristic(UUID.fromString(GattAttributes.CONTROL_POINT_CHARACTERISTIC))) != null) {
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
        if (UUID.fromString(GattAttributes.RSC_MEASUREMENT_CHARACTERISTIC).equals(characteristic.getUuid())) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            int offset = 0 + 1;
            Integer speed = characteristic.getIntValue(18, offset);
            int offset2 = offset + 2;
            double speedWithResolution = ((double) speed.intValue()) / 256.0d;
            long cadence = characteristic.getIntValue(17, offset2).intValue();
            int offset3 = offset2 + 1;
            double strideWithResolution = -1.0d;
            if ((flags & 1) != 0) {
                Integer strideLength = characteristic.getIntValue(18, offset3);
                offset3 += 2;
                strideWithResolution = ((double) strideLength.intValue()) / 100.0d;
            }
            double totalDistanceWithResolution = -1.0d;
            if ((flags & 2) != 0) {
                Integer totalDistance = characteristic.getIntValue(20, offset3);
                totalDistanceWithResolution = ((double) totalDistance.intValue()) / 10.0d;
            }
            this.listener.onRscMeasurement(speedWithResolution, cadence, strideWithResolution, totalDistanceWithResolution, (flags & 4) != 0);
        }
    }
}
