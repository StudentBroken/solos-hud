package com.kopin.solos.sensors.btle;

import android.bluetooth.BluetoothGattCharacteristic;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorData;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.GattAttributes;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/* JADX INFO: loaded from: classes28.dex */
public abstract class BTLESensorData {
    static final UUID GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG = UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR);
    static final UUID GATT_CHARACTERISTIC_HEARTRATE = UUID.fromString(GattAttributes.HEART_RATE_CHARACTERISTIC);
    static final UUID GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE = UUID.fromString(GattAttributes.RSC_MEASUREMENT_CHARACTERISTIC);
    static final UUID GATT_CHARACTERISTIC_SPEED_AND_CADENCE = UUID.fromString(GattAttributes.CSC_MEASUREMENT_CHARACTERISTIC);
    static final UUID GATT_CHARACTERISTIC_CRANK_POWER = UUID.fromString(GattAttributes.CYCLING_POWER_CHARACTERISTIC);
    static final UUID GATT_CHARACTERISTIC_OXYGEN_OLD = UUID.fromString("2e4ee00e-d9f0-5490-ff4b-d17374c433ef");
    static final UUID GATT_CHARACTERISTIC_OXYGEN = UUID.fromString("2e4ee00d-d9f0-5490-ff4b-d17374c433ef");

    private BTLESensorData() {
    }

    public static SensorData fromCharacteristicValue(BluetoothGattCharacteristic characteristic, byte[] value) {
        if (value == null || value.length == 0) {
            return SensorData.EMPTY;
        }
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_HEARTRATE)) {
            if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_SPEED_AND_CADENCE) && !characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE)) {
                if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_CRANK_POWER) && !characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
                    if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_OXYGEN)) {
                        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE) && !characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
                            return SensorData.EMPTY;
                        }
                        return RunningSpeedAndCadence.fromValue(value);
                    }
                    return Oxygen.fromValue(value);
                }
                return CrankPower.fromValue(value);
            }
            return SpeedAndCadence.fromValue(value);
        }
        return HeartRate.fromValue(value);
    }

    public static class HeartRate extends SensorData.HeartRate {
        private static final int HEARTRATE_FLAG_ENERGY = 8;
        private static final int HEARTRATE_FLAG_FORMAT = 1;
        private static final int HEARTRATE_FLAG_RR_INTERVAL = 16;
        private static final int HEARTRATE_FLAG_SENSOR_CONTACT = 6;
        private int mValue;

        /* JADX INFO: Access modifiers changed from: private */
        public static SensorData.HeartRate fromValue(byte[] value) {
            HeartRate self = new HeartRate();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            byte flags = buffer.get();
            if ((flags & 1) == 0) {
                self.mValue = buffer.get() & 255;
            } else {
                self.mValue = buffer.getShort() & 65535;
            }
            return self;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public int valueFor(Sensor.DataType sensor) {
            if (sensor.equals(Sensor.DataType.HEARTRATE)) {
                return this.mValue;
            }
            return 0;
        }

        public String toString() {
            return String.format("Heartrate data: ", Integer.valueOf(this.mValue));
        }
    }

    public static class SpeedAndCadence extends SensorData.SpeedAndCadence {
        private static final int SPEED_CADENCE_FLAG_HAS_CADENCE = 2;
        private static final int SPEED_CADENCE_FLAG_HAS_SPEED = 1;
        private byte mDataFlags;
        private int mWheelRevs = -1;
        private int mWheelStamp = 0;
        private int mCrankRevs = -1;
        private int mCrankStamp = 0;

        @Override // com.kopin.solos.sensors.SensorData.SpeedAndCadence
        protected boolean hasSpeedData() {
            return (this.mDataFlags & 1) != 0;
        }

        @Override // com.kopin.solos.sensors.SensorData.SpeedAndCadence
        protected boolean hasCadenceData() {
            return (this.mDataFlags & 2) != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SensorData.SpeedAndCadence fromValue(byte[] value) {
            SpeedAndCadence self = new SpeedAndCadence();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mDataFlags = buffer.get();
                if (self.hasSpeedData()) {
                    self.mWheelRevs = buffer.getInt();
                    self.mWheelStamp = buffer.getShort() & 65535;
                    if (self.mWheelStamp == 0) {
                        self.mDataFlags = (byte) (self.mDataFlags & (-2));
                    }
                }
                if (self.hasCadenceData()) {
                    self.mCrankRevs = buffer.getShort() & 65535;
                    self.mCrankStamp = buffer.getShort() & 65535;
                    if (self.mCrankStamp == 0) {
                        self.mDataFlags = (byte) (self.mDataFlags & (-3));
                    }
                }
            } catch (BufferUnderflowException e) {
            }
            return self;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public int valueFor(Sensor.DataType sensor) {
            if (sensor.equals(Sensor.DataType.SPEED)) {
                return this.mWheelRevs;
            }
            if (sensor.equals(Sensor.DataType.CADENCE)) {
                return this.mCrankRevs;
            }
            return 0;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public long timestamp(Sensor.DataType data) {
            if (data.equals(Sensor.DataType.SPEED)) {
                return this.mWheelStamp;
            }
            if (data.equals(Sensor.DataType.CADENCE)) {
                return this.mCrankStamp;
            }
            return super.timestamp(data);
        }

        public String toString() {
            return "Wheel data: " + (hasSpeedData() ? String.format("%d (%d)", Integer.valueOf(this.mWheelRevs), Integer.valueOf(this.mWheelStamp)) : "--") + " Crank data: " + (hasCadenceData() ? String.format("%d (%d)", Integer.valueOf(this.mCrankRevs), Integer.valueOf(this.mCrankStamp)) : "--");
        }
    }

    public static class CrankPower extends SensorData.CrankPower {
        private static final int FLAG_HAS_ACC_TORQUE = 4;
        private static final int FLAG_HAS_CRANK_REVS = 32;
        private static final int FLAG_HAS_PEDAL_BALANCE = 1;
        private static final int FLAG_HAS_WHEEL_REVS = 16;
        private int mFlags;
        private int mPowerValue;
        private int mWheelRevs = -1;
        private int mWheelStamp = 0;
        private int mCrankRevs = -1;
        private int mCrankStamp = 0;

        /* JADX INFO: Access modifiers changed from: private */
        public static SensorData.CrankPower fromValue(byte[] value) {
            CrankPower self = new CrankPower();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mFlags = buffer.getShort() & 65535;
                self.mPowerValue = buffer.getShort() & 65535;
                if ((self.mFlags & 1) != 0) {
                    buffer.get();
                }
                if ((self.mFlags & 4) != 0) {
                    buffer.getShort();
                }
                if (self.hasSpeedData()) {
                    self.mWheelRevs = buffer.getInt();
                    self.mWheelStamp = (buffer.getShort() & 65535) / 2;
                    if (self.mWheelStamp == 0) {
                        self.mFlags &= -17;
                    }
                }
                if (self.hasCadenceData()) {
                    self.mCrankRevs = buffer.getShort() & 65535;
                    self.mCrankStamp = buffer.getShort() & 65535;
                    if (self.mCrankStamp == 0) {
                        self.mFlags &= -33;
                    }
                }
            } catch (BufferUnderflowException bue) {
                bue.printStackTrace();
            }
            return self;
        }

        @Override // com.kopin.solos.sensors.SensorData.CrankPower
        protected boolean hasSpeedData() {
            return (this.mFlags & 16) != 0;
        }

        @Override // com.kopin.solos.sensors.SensorData.CrankPower
        protected boolean hasCadenceData() {
            return (this.mFlags & 32) != 0;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public int valueFor(Sensor.DataType sensor) {
            if (sensor.equals(Sensor.DataType.POWER)) {
                return this.mPowerValue;
            }
            if (sensor.equals(Sensor.DataType.SPEED)) {
                return this.mWheelRevs;
            }
            if (sensor.equals(Sensor.DataType.CADENCE)) {
                return this.mCrankRevs;
            }
            return 0;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public long timestamp(Sensor.DataType data) {
            if (data.equals(Sensor.DataType.SPEED)) {
                return this.mWheelStamp;
            }
            if (data.equals(Sensor.DataType.CADENCE)) {
                return this.mCrankStamp;
            }
            return super.timestamp(data);
        }

        public String toString() {
            return "Power data: " + String.format("%d", Integer.valueOf(this.mPowerValue)) + " Wheel data: " + (hasSpeedData() ? String.format("%d (%d)", Integer.valueOf(this.mWheelRevs), Integer.valueOf(this.mWheelStamp)) : "--") + " Crank data: " + (hasCadenceData() ? String.format("%d (%d)", Integer.valueOf(this.mCrankRevs), Integer.valueOf(this.mCrankStamp)) : "--");
        }
    }

    public static class Oxygen extends SensorData.Oxygen {
        private static int MAX_OXYGEN = 1023;
        private int mCount;
        private int mFlags;
        private int mValue;

        /* JADX INFO: Access modifiers changed from: private */
        public static SensorData.Oxygen fromValue(byte[] value) {
            Oxygen self = new Oxygen();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mCount = buffer.getShort() & 65535;
                buffer.position(18);
                self.mValue = buffer.getShort() & 65535;
            } catch (BufferUnderflowException bue) {
                bue.printStackTrace();
            }
            return self;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public int valueFor(Sensor.DataType sensor) {
            if (sensor.equals(Sensor.DataType.OXYGEN)) {
                return (this.mValue * 100) / MAX_OXYGEN;
            }
            return 0;
        }
    }

    public static class RunningSpeedAndCadence extends SensorData.RunningSpeedAndCadence {
        private static final int RSC_FLAG_HAS_STRIDE = 1;
        private static final int RSC_FLAG_IS_RUNNING = 4;
        private byte mDataFlags;
        private int mSpeed = -1;
        private int mCadence = -1;
        private int mStride = -1;
        private long mTimestamp = 0;

        @Override // com.kopin.solos.sensors.SensorData
        public long timestamp(Sensor.DataType data) {
            return this.mTimestamp;
        }

        @Override // com.kopin.solos.sensors.SensorData.RunningSpeedAndCadence
        protected boolean hasSpeedData() {
            return true;
        }

        @Override // com.kopin.solos.sensors.SensorData.RunningSpeedAndCadence
        protected boolean hasCadenceData() {
            return true;
        }

        @Override // com.kopin.solos.sensors.SensorData.RunningSpeedAndCadence
        protected boolean hasStrideData() {
            return (this.mDataFlags & 1) != 0;
        }

        @Override // com.kopin.solos.sensors.SensorData.RunningSpeedAndCadence
        protected boolean isRunning() {
            return (this.mDataFlags & 4) != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SensorData.RunningSpeedAndCadence fromValue(byte[] value) {
            RunningSpeedAndCadence self = new RunningSpeedAndCadence();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mTimestamp = System.currentTimeMillis();
                self.mDataFlags = buffer.get();
                self.mSpeed = buffer.getShort() & 65535;
                self.mCadence = buffer.get() & 255;
                if (self.hasStrideData()) {
                    self.mStride = buffer.getShort() & 65535;
                }
            } catch (BufferUnderflowException e) {
            }
            return self;
        }

        @Override // com.kopin.solos.sensors.SensorData
        public int valueFor(Sensor.DataType sensor) {
            if (sensor.equals(Sensor.DataType.PACE)) {
                return this.mSpeed;
            }
            if (sensor.equals(Sensor.DataType.STEP)) {
                return this.mCadence;
            }
            if (sensor.equals(Sensor.DataType.STRIDE)) {
                return this.mStride;
            }
            return 0;
        }

        public String toString() {
            return "Speed data: " + (hasSpeedData() ? String.format("%d", Integer.valueOf(this.mSpeed)) : "--") + " Cadence data: " + (hasCadenceData() ? String.format("%d", Integer.valueOf(this.mCadence)) : "--") + " Stride data: " + (hasSpeedData() ? String.format("%d", Integer.valueOf(this.mStride)) : "--");
        }
    }
}
