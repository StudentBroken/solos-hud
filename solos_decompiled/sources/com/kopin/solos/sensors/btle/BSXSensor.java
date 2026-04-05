package com.kopin.solos.sensors.btle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorData;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/* JADX INFO: loaded from: classes28.dex */
public class BSXSensor extends BTLESensor {
    private static final String TAG = "BSXSensor";
    static final UUID GATT_SERVICE_OXYGEN = UUID.fromString("2e4ed00a-d9f0-5490-ff4b-d17374c433ef");
    static final UUID GATT_CHARACTERISTIC_OXYGEN_ENABLE = UUID.fromString("2e4ee00a-d9f0-5490-ff4b-d17374c433ef");
    static final UUID GATT_CHARACTERISTIC_OXYGEN_POS = UUID.fromString("2e4ee00b-d9f0-5490-ff4b-d17374c433ef");

    BSXSensor(Context context, BluetoothDevice dev, Sensor.SensorObserver obs) {
        super(context, dev, Sensor.SensorType.OXYGEN, obs);
    }

    BSXSensor(Context context, BluetoothDevice dev, String name, Sensor.SensorObserver obs) {
        super(context, dev, name, Sensor.SensorType.OXYGEN, obs);
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public String serialize() {
        return "BSX," + getId() + "," + getName();
    }

    public static Sensor deserialize(String params, Context context, Sensor.SensorObserver obs) {
        if (!params.startsWith("BSX")) {
            return null;
        }
        String[] values = params.split(",");
        try {
            BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(values[1]);
            return new BSXSensor(context, dev, values[2], obs);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public void activate() {
        enableBsx(true);
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public void deactivate() {
        enableBsx(false);
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public void disconnect() {
        if (this.mState == Sensor.ConnectionState.DISCONNECTING) {
            super.disconnect();
        } else {
            this.mState = Sensor.ConnectionState.DISCONNECTING;
            deactivate();
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_OXYGEN_ENABLE)) {
            return false;
        }
        setLastValue(Sensor.SensorType.OXYGEN, Sensor.DataType.OXYGEN, OxygenEnable.fromValue(characteristic.getValue()));
        activate();
        return true;
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_OXYGEN_POS)) {
            return false;
        }
        OxygenPos value = OxygenPos.fromValue(characteristic.getValue());
        Log.d(TAG, "Oxygen Sensor contact changed: " + value.hasContact());
        return true;
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        if (this.mState != Sensor.ConnectionState.DISCONNECTING) {
            return false;
        }
        disconnect();
        return true;
    }

    private void enableBsx(boolean onOrOff) {
        BluetoothGattCharacteristic chr;
        OxygenEnable enable;
        BluetoothGattService service = this.mGatt.getService(GATT_SERVICE_OXYGEN);
        if (service != null && (chr = service.getCharacteristic(GATT_CHARACTERISTIC_OXYGEN_ENABLE)) != null && (enable = (OxygenEnable) getLastValue(Sensor.SensorType.OXYGEN, Sensor.DataType.OXYGEN)) != null) {
            enable.setEnable(onOrOff);
            chr.setValue(enable.toValue());
            this.mGatt.writeCharacteristic(chr);
        }
    }

    public static class OxygenPos extends SensorData.Oxygen {
        private static byte FLAG_CONTACT = 3;
        private byte mFlags;

        /* JADX INFO: Access modifiers changed from: private */
        public static OxygenPos fromValue(byte[] value) {
            OxygenPos self = new OxygenPos();
            self.mFlags = value[6];
            return self;
        }

        public boolean hasContact() {
            return (this.mFlags & FLAG_CONTACT) == 0;
        }
    }

    public static class OxygenEnable extends SensorData.Oxygen {
        private static byte ENABLE_BIT = 2;
        private byte mEnable;
        private byte mFlags;
        private byte[] mRaw = {4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        /* JADX INFO: Access modifiers changed from: private */
        public static OxygenEnable fromValue(byte[] value) {
            OxygenEnable self = new OxygenEnable();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mFlags = buffer.get();
                self.mEnable = buffer.get();
            } catch (BufferUnderflowException bue) {
                bue.printStackTrace();
            }
            return self;
        }

        public boolean isEnabled() {
            return (this.mEnable & ENABLE_BIT) != 0;
        }

        public void setEnable(boolean onOrOff) {
            this.mEnable = onOrOff ? ENABLE_BIT : (byte) 0;
        }

        public byte[] toValue() {
            this.mRaw[1] = this.mEnable;
            return this.mRaw;
        }
    }
}
