package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import com.ua.sdk.UaLog;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.SensorStatus;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothArmour39Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
@TargetApi(18)
public class BaseGattCallback extends BluetoothGattCallback {
    private BluetoothActionQueue bluetoothActionQueue;
    private BluetoothClient.BluetoothClientListener clientListener;
    private ConnectionLostListener connectionLostListener;
    private List<GattCallbackListener> gattCallbackListeners = new ArrayList();
    private boolean isClientDisconnect;

    public interface ConnectionLostListener {
        void onScheduleReconnect(long j);
    }

    public interface GattCallbackListener {
        void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

        void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

        void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i);

        void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i);

        void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i);

        void onUnexpectedDisconnect();

        void setBluetoothActionQueue(BluetoothActionQueue bluetoothActionQueue);

        void setClientListener(BluetoothClient.BluetoothClientListener bluetoothClientListener);
    }

    public void setClientListener(BluetoothClient.BluetoothClientListener clientListener) {
        this.clientListener = clientListener;
    }

    public void addGattCallbackListener(GattCallbackListener listener) {
        this.gattCallbackListeners.add(listener);
    }

    public void configure(RecorderContext recorderContext) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            if (listener instanceof BluetoothArmour39Service) {
                ((BluetoothArmour39Service) listener).configureUser(recorderContext.getUser(), recorderContext.getHeartRateZones());
            }
        }
    }

    public void disconnect() {
        this.isClientDisconnect = true;
        if (this.bluetoothActionQueue != null) {
            for (GattCallbackListener listener : this.gattCallbackListeners) {
                if (listener instanceof BluetoothArmour39Service) {
                    ((BluetoothArmour39Service) listener).stopWorkout();
                    ((BluetoothArmour39Service) listener).disconnect();
                }
            }
            while (!this.bluetoothActionQueue.isEmpty()) {
                this.bluetoothActionQueue.poll();
            }
        }
    }

    public void startSegment() {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            if (listener instanceof BluetoothArmour39Service) {
                ((BluetoothArmour39Service) listener).startWorkout();
            }
        }
    }

    public void setConnectionLostListener(ConnectionLostListener connectionLostListener) {
        this.connectionLostListener = connectionLostListener;
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (status == 0 && newState == 2) {
            this.bluetoothActionQueue = new BluetoothActionQueue(gatt);
            for (GattCallbackListener listener : this.gattCallbackListeners) {
                listener.setBluetoothActionQueue(this.bluetoothActionQueue);
                listener.setClientListener(this.clientListener);
            }
            if (this.clientListener != null) {
                this.clientListener.onConnectionStatusChanged(SensorStatus.CONNECTED);
            }
            gatt.discoverServices();
            return;
        }
        if (!this.isClientDisconnect) {
            UaLog.error("Bluetooth Connection has been lost unexpectedly for " + gatt.getDevice().getAddress());
            if (this.clientListener != null) {
                this.clientListener.onConnectionStatusChanged(SensorStatus.DISCONNECTED);
            }
            if (this.connectionLostListener != null) {
                this.connectionLostListener.onScheduleReconnect(5000L);
            }
            Iterator<GattCallbackListener> it = this.gattCallbackListeners.iterator();
            while (it.hasNext()) {
                it.next().onUnexpectedDisconnect();
            }
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onServicesDiscovered(gatt, status);
        }
        this.bluetoothActionQueue.poll();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onCharacteristicRead(gatt, characteristic, status);
        }
        this.bluetoothActionQueue.poll();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onCharacteristicWrite(gatt, characteristic, status);
        }
        this.bluetoothActionQueue.poll();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onCharacteristicChanged(gatt, characteristic);
        }
        this.bluetoothActionQueue.poll();
        gatt.readRemoteRssi();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onDescriptorRead(gatt, descriptor, status);
        }
        this.bluetoothActionQueue.poll();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        for (GattCallbackListener listener : this.gattCallbackListeners) {
            listener.onDescriptorWrite(gatt, descriptor, status);
        }
        this.bluetoothActionQueue.poll();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        this.clientListener.onRssiMeasurement(rssi);
    }
}
