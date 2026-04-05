package com.kopin.solos.sensors;

import com.kopin.solos.sensors.Sensor;

/* JADX INFO: loaded from: classes28.dex */
public abstract class SensorData {
    public static final SensorData EMPTY = new SensorData() { // from class: com.kopin.solos.sensors.SensorData.1
        public String toString() {
            return "EMPTY / NO DATA";
        }
    };

    public boolean hasValueFor(Sensor.DataType data) {
        return false;
    }

    public int valueFor(Sensor.DataType data) {
        return -1;
    }

    public long timestamp(Sensor.DataType data) {
        return System.currentTimeMillis();
    }

    public static abstract class HeartRate extends SensorData {
        @Override // com.kopin.solos.sensors.SensorData
        public boolean hasValueFor(Sensor.DataType data) {
            return data.equals(Sensor.DataType.HEARTRATE);
        }
    }

    public static abstract class Oxygen extends SensorData {
        @Override // com.kopin.solos.sensors.SensorData
        public boolean hasValueFor(Sensor.DataType data) {
            return data.equals(Sensor.DataType.OXYGEN);
        }
    }

    public static abstract class SpeedAndCadence extends SensorData {
        protected abstract boolean hasCadenceData();

        protected abstract boolean hasSpeedData();

        @Override // com.kopin.solos.sensors.SensorData
        public boolean hasValueFor(Sensor.DataType data) {
            switch (data) {
                case SPEED:
                    return hasSpeedData();
                case CADENCE:
                    return hasCadenceData();
                default:
                    return false;
            }
        }
    }

    public static abstract class RunningSpeedAndCadence extends SensorData {
        protected abstract boolean hasCadenceData();

        protected abstract boolean hasSpeedData();

        protected abstract boolean hasStrideData();

        protected abstract boolean isRunning();

        @Override // com.kopin.solos.sensors.SensorData
        public boolean hasValueFor(Sensor.DataType data) {
            switch (data) {
                case PACE:
                case STEP:
                    return true;
                case STRIDE:
                    return hasStrideData();
                default:
                    return false;
            }
        }
    }

    public static abstract class CrankPower extends SensorData {
        protected abstract boolean hasCadenceData();

        protected abstract boolean hasSpeedData();

        @Override // com.kopin.solos.sensors.SensorData
        public boolean hasValueFor(Sensor.DataType data) {
            switch (data) {
                case SPEED:
                    return hasSpeedData();
                case CADENCE:
                    return hasCadenceData();
                default:
                    return data.equals(Sensor.DataType.POWER);
            }
        }
    }
}
