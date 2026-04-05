package com.kopin.solos.sensors.btle;

import com.kopin.accessory.utility.CallHelper;
import com.kopin.solos.sensors.Sensor;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;

/* JADX INFO: loaded from: classes28.dex */
public class CableAnt {
    private static final boolean SWITCH_SETS_FOR_SPD_CAD = true;

    enum DeviceType {
        NONE(0, Sensor.SensorType.UNKNOWN),
        HR(TwitterApiErrorConstants.EMAIL_ALREADY_REGISTERED, Sensor.SensorType.HEARTRATE),
        BIKE_SPD(123, Sensor.SensorType.SPEED),
        BIKE_CAD(122, Sensor.SensorType.CADENCE),
        BIKE_SC(121, Sensor.SensorType.SPEED_AND_CADENCE),
        BIKE_PWR(11, Sensor.SensorType.POWER),
        RUNNING_SPD(124, Sensor.SensorType.RUNNING_SPEED_CADENCE);

        private final byte mDevType;
        private final Sensor.SensorType mSensorType;

        DeviceType(int type, Sensor.SensorType mapsTo) {
            this.mDevType = (byte) type;
            this.mSensorType = mapsTo;
        }

        byte deviceType() {
            return this.mDevType;
        }

        Sensor.SensorType mappedSensorType() {
            return this.mSensorType;
        }

        public static DeviceType fromSensorType(Sensor.SensorType type) {
            switch (type) {
                case HEARTRATE:
                    return HR;
                case POWER:
                    return BIKE_PWR;
                case SPEED_AND_CADENCE:
                    return BIKE_SC;
                case SPEED:
                    return BIKE_SPD;
                case CADENCE:
                    return BIKE_CAD;
                case RUNNING_SPEED_CADENCE:
                    return RUNNING_SPD;
                default:
                    return NONE;
            }
        }

        public static DeviceType fromDataType(Sensor.DataType type) {
            switch (type) {
                case HEARTRATE:
                    return HR;
                case POWER:
                    return BIKE_PWR;
                case SPEED:
                    return BIKE_SPD;
                case CADENCE:
                    return BIKE_CAD;
                default:
                    return NONE;
            }
        }

        public static DeviceType fromByte(byte type) {
            for (DeviceType t : values()) {
                if (t.mDevType == type) {
                    return t;
                }
            }
            return NONE;
        }

        public static DeviceType defaultForChannel(int set, int channel) {
            switch (channel) {
                case 0:
                    return HR;
                case 1:
                    return BIKE_PWR;
                case 2:
                    return set == 0 ? BIKE_SC : RUNNING_SPD;
                case 3:
                    return set == 1 ? BIKE_CAD : RUNNING_SPD;
                case 4:
                    return set == 1 ? BIKE_SPD : NONE;
                default:
                    return NONE;
            }
        }

        public int defaultChannel(int forSet) {
            switch (this) {
                case HR:
                    return 0;
                case BIKE_PWR:
                    return 1;
                case BIKE_SC:
                    return forSet != 0 ? -1 : 2;
                case BIKE_SPD:
                    return forSet == 1 ? 4 : -1;
                case BIKE_CAD:
                    return forSet == 1 ? 3 : -1;
                case RUNNING_SPD:
                    return forSet != 0 ? 2 : 3;
                default:
                    return -1;
            }
        }

        public int checkSet(int set) {
            switch (this) {
                case BIKE_SC:
                    return 0;
                case BIKE_SPD:
                case BIKE_CAD:
                    return 1;
                default:
                    return set;
            }
        }
    }

    enum ChannelStatus {
        UNASSIGNED,
        ASSIGNED,
        SCANNING,
        TRACKING,
        UNKNOWN;

        private boolean flag;

        public static ChannelStatus fromByte(byte val) {
            ChannelStatus self = UNASSIGNED;
            int stat = val & 15;
            if (stat < 4) {
                self = values()[stat];
            }
            self.flag = (val & CallHelper.CallState.FLAG_CALL_SETUP) == 64;
            return self;
        }

        public boolean isIdle() {
            return this == UNASSIGNED || this == ASSIGNED;
        }

        public boolean isActive() {
            return this == TRACKING;
        }

        public boolean isScanning() {
            return this == SCANNING;
        }

        public boolean isAssigned() {
            return this == ASSIGNED;
        }
    }

    static class ChannelSlot {
        int mDevId;
        DeviceType mDevType;
        long mLastScan;
        ChannelStatus mStatus;

        ChannelSlot() {
            reset();
        }

        void reset() {
            this.mStatus = ChannelStatus.UNKNOWN;
            this.mDevType = DeviceType.NONE;
            this.mDevId = 0;
        }

        boolean scanTimeout() {
            return System.currentTimeMillis() - this.mLastScan > 250;
        }
    }
}
