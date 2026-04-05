package com.kopin.solos.sensors.btle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.btle.BTLESensorConfig;
import com.kopin.solos.sensors.btle.BTLESensorData;

/* JADX INFO: loaded from: classes28.dex */
public class ComboSensor extends BTLESensor {
    private static final String TAG = "ComboSensor";
    private final Caps Cadence;
    private final Caps HeartRate;
    private final Caps Power;
    private final Caps Speed;
    private final Caps Stride;
    private boolean isFootPod;

    private static class Caps {
        boolean hasType;
        long lastStamp;
        boolean usingType;

        Caps() {
            reset();
        }

        void reset() {
            this.hasType = false;
            this.usingType = false;
            this.lastStamp = 0L;
        }

        void clearStamp() {
            this.lastStamp = 0L;
        }

        void tryToUse() {
            this.usingType = this.hasType;
        }
    }

    public ComboSensor(Context context, BluetoothDevice dev, SensorsConnector.ScannerListener obs) {
        this(context, dev, false, obs);
    }

    public ComboSensor(Context context, BluetoothDevice dev, boolean footpod, SensorsConnector.ScannerListener obs) {
        super(context, dev, Sensor.SensorType.COMBO, obs);
        this.HeartRate = new Caps();
        this.Power = new Caps();
        this.Speed = new Caps();
        this.Cadence = new Caps();
        this.Stride = new Caps();
        this.isFootPod = false;
        this.isFootPod = footpod;
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public String serialize() {
        return "COMBO," + getId() + "," + getName();
    }

    public static Sensor deserialize(String params, Context context, SensorsConnector.ScannerListener obs) {
        if (!params.startsWith("COMBO") && !params.startsWith("FOOT_POD")) {
            return null;
        }
        String[] values = params.split(",");
        try {
            BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(values[1]);
            return new ComboSensor(context, dev, params.startsWith("FOOT_POD"), obs);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public Sensor.SensorType getType() {
        return this.isFootPod ? Sensor.SensorType.FOOT_POD : Sensor.SensorType.COMBO;
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public boolean equals(Sensor.SensorType type) {
        switch (type) {
            case HEARTRATE:
                return this.HeartRate.hasType;
            case POWER:
                return this.Power.hasType;
            case SPEED:
                return this.Speed.hasType;
            case CADENCE:
                return this.Cadence.hasType;
            case STRIDE:
                return this.Stride.hasType;
            case FOOT_POD:
            case COMBO:
                return true;
            default:
                return super.equals(type);
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isUsedFor(Sensor.DataType type) {
        switch (type) {
            case HEARTRATE:
                return this.HeartRate.usingType;
            case KICK:
                return this.isFootPod && this.Power.usingType;
            case POWER:
                return !this.isFootPod && this.Power.usingType;
            case PACE:
                return this.isFootPod && this.Speed.usingType;
            case SPEED:
                return !this.isFootPod && this.Speed.usingType;
            case STEP:
                return this.isFootPod && this.Cadence.usingType;
            case CADENCE:
                return !this.isFootPod && this.Cadence.usingType;
            case STRIDE:
                return this.Stride.usingType;
            default:
                return super.isUsedFor(type);
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isInUse() {
        return this.HeartRate.usingType || this.Power.usingType || this.Speed.usingType || this.Cadence.usingType || this.Stride.usingType;
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void useFor(Sensor.DataType type) {
        switch (type) {
            case HEARTRATE:
                this.HeartRate.tryToUse();
                break;
            case KICK:
                if (this.isFootPod) {
                    this.Power.tryToUse();
                }
                break;
            case POWER:
                if (!this.isFootPod) {
                    this.Power.tryToUse();
                }
                break;
            case PACE:
                if (this.isFootPod) {
                    this.Speed.tryToUse();
                }
                break;
            case SPEED:
                if (!this.isFootPod) {
                    this.Speed.tryToUse();
                }
                break;
            case STEP:
                if (this.isFootPod) {
                    this.Cadence.tryToUse();
                }
                break;
            case CADENCE:
                if (!this.isFootPod) {
                    this.Cadence.tryToUse();
                }
                break;
            case STRIDE:
                this.Stride.tryToUse();
                break;
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void stopUsing(Sensor.DataType type) {
        switch (type) {
            case HEARTRATE:
                this.HeartRate.usingType = false;
                break;
            case KICK:
                if (this.isFootPod) {
                    this.Power.usingType = false;
                }
                break;
            case POWER:
                if (!this.isFootPod) {
                    this.Power.usingType = false;
                }
                break;
            case PACE:
                if (this.isFootPod) {
                    this.Speed.usingType = false;
                }
                break;
            case SPEED:
                if (!this.isFootPod) {
                    this.Speed.usingType = false;
                }
                break;
            case STEP:
                if (this.isFootPod) {
                    this.Cadence.usingType = false;
                }
                break;
            case CADENCE:
                if (!this.isFootPod) {
                    this.Cadence.usingType = false;
                }
                break;
            case STRIDE:
                this.Stride.usingType = false;
                break;
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    protected void onSensorConnected() {
        super.onSensorConnected();
        this.HeartRate.clearStamp();
        this.Power.clearStamp();
        this.Speed.clearStamp();
        this.Cadence.clearStamp();
        this.Stride.clearStamp();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    protected void onSensorDisconnected() {
        super.onSensorDisconnected();
        this.HeartRate.reset();
        this.Power.reset();
        this.Speed.reset();
        this.Cadence.reset();
        this.Stride.reset();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onServicesDiscovered() {
        this.HeartRate.hasType = this.mGatt.getService(GATT_SERVICE_HEARTRATE) != null;
        this.Power.hasType = this.mGatt.getService(GATT_SERVICE_CRANK_POWER) != null;
        this.Cadence.hasType = this.mGatt.getService(GATT_SERVICE_SPEED_AND_CADENCE) != null;
        if (!this.Cadence.hasType) {
            this.Cadence.hasType = this.mGatt.getService(GATT_SERVICE_RUNNING_SPEED_CADENCE) != null;
            this.isFootPod = this.Cadence.hasType;
        }
        this.Speed.hasType = this.Cadence.hasType;
        onSensorAvailable();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.sensors.Sensor
    public void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, int value) {
        if (dataType.equals(Sensor.DataType.HEARTRATE)) {
            this.HeartRate.lastStamp = System.currentTimeMillis();
        }
        super.onNewData(sensorType, dataType, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.sensors.Sensor
    public void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, double value) {
        if (dataType.equals(Sensor.DataType.PACE)) {
            this.Speed.lastStamp = System.currentTimeMillis();
        }
        if (dataType.equals(Sensor.DataType.CADENCE)) {
            this.Cadence.lastStamp = System.currentTimeMillis();
        }
        if (dataType.equals(Sensor.DataType.STEP)) {
            this.Cadence.lastStamp = System.currentTimeMillis();
        }
        if (dataType.equals(Sensor.DataType.POWER)) {
            this.Power.lastStamp = System.currentTimeMillis();
        }
        if (dataType.equals(Sensor.DataType.KICK)) {
            this.Power.lastStamp = System.currentTimeMillis();
        }
        if (dataType.equals(Sensor.DataType.STRIDE)) {
            this.Stride.lastStamp = System.currentTimeMillis();
        }
        super.onNewData(sensorType, dataType, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.sensors.Sensor
    public void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, double value, long interval) {
        if (dataType.equals(Sensor.DataType.SPEED)) {
            this.Speed.lastStamp = System.currentTimeMillis();
        }
        super.onNewData(sensorType, dataType, value, interval);
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public long getLastUpdateTime(Sensor.DataType dataType) {
        switch (dataType) {
            case HEARTRATE:
                return this.HeartRate.lastStamp;
            case KICK:
            case POWER:
                return this.Power.lastStamp;
            case PACE:
            case SPEED:
                return this.Speed.lastStamp;
            case STEP:
            case CADENCE:
                return this.Cadence.lastStamp;
            case STRIDE:
                return this.Stride.lastStamp;
            default:
                return super.getLastUpdateTime(dataType);
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
        if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE)) {
            BTLESensorData.SpeedAndCadence features = (BTLESensorData.SpeedAndCadence) BTLESensorData.fromCharacteristicValue(characteristic, characteristic.getValue());
            this.Speed.hasType = features.hasSpeedData();
            this.Cadence.hasType = features.hasCadenceData();
            onSensorAvailable();
        } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
            BTLESensorData.RunningSpeedAndCadence features2 = (BTLESensorData.RunningSpeedAndCadence) BTLESensorData.fromCharacteristicValue(characteristic, characteristic.getValue());
            this.Speed.hasType = features2.hasSpeedData();
            this.Cadence.hasType = features2.hasCadenceData();
            this.Stride.hasType = features2.hasStrideData();
            this.isFootPod = true;
            onSensorAvailable();
        } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
            this.Power.hasType = true;
            if (!this.isFootPod) {
                this.mLastConfig = new BTLESensorConfig.CrankPowerConfig();
                ((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).flagsFromValue(characteristic.getValue());
                this.Cadence.hasType = ((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).hasCadence();
                this.Speed.hasType = ((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).hasSpeed();
            }
            onSensorAvailable();
        }
        return super.onCharacteristicRead(characteristic, status);
    }
}
