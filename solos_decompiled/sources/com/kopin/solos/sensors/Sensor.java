package com.kopin.solos.sensors;

import android.content.Context;
import com.kopin.solos.storage.settings.Prefs;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.HashMap;
import java.util.HashSet;

/* JADX INFO: loaded from: classes28.dex */
public abstract class Sensor {
    public static final long DATA_VALIDITY = 5000;
    private static final String TAG = "Sensor";
    private SensorConfigObserver mConfigObs;
    private final SensorObserver mObs;
    private HashSet<DataType> mHasMap = new HashSet<>();
    private HashSet<DataType> mUseMap = new HashSet<>();
    private HashMap<DataType, SensorData> mLastData = new HashMap<>();
    private String mLastValueString = "---";

    public enum ButtonAction {
        LAP,
        MENU,
        BLUE_THINGY
    }

    public interface SensorConfigObserver {
        void onSensorConfigChanged(Sensor sensor);

        void onSensorDisconnected(Sensor sensor);
    }

    public interface SensorObserver {
        void onSensorAvailable(Sensor sensor);

        void onSensorChanged(Sensor sensor);

        void onSensorConfigMessage(Sensor sensor, boolean z, String str);

        void onSensorConnected(Sensor sensor);

        void onSensorData(Sensor sensor, DataType dataType, double d);

        void onSensorData(Sensor sensor, DataType dataType, double d, long j);

        void onSensorData(Sensor sensor, DataType dataType, int i);

        void onSensorDisconnected(Sensor sensor);

        void onSensorTimeout(Sensor sensor);

        void onSensorUnavailable(Sensor sensor);
    }

    public abstract void activate();

    public abstract void connect();

    public abstract void deactivate();

    public abstract void disconnect();

    public abstract boolean equals(SensorType sensorType);

    public abstract ConnectionState getConnectionState();

    public abstract String getId();

    protected abstract long getLastUpdateTime(DataType dataType);

    public abstract String getName();

    public abstract SensorType getType();

    public abstract boolean isBatteryOk();

    public enum DataType {
        UNKOWN,
        CADENCE,
        SPEED,
        HEARTRATE,
        POWER,
        OXYGEN,
        PACE,
        STRIDE,
        ALTITUDE,
        LOCATION,
        STEP,
        KICK;

        static String defaultUnits(DataType type) {
            switch (type) {
                case HEARTRATE:
                    return "bpm";
                case SPEED:
                case CADENCE:
                    return "rpm";
                case POWER:
                case KICK:
                    return "Watts";
                case STRIDE:
                    return Prefs.MODE_MANUAL;
                case OXYGEN:
                    return "%";
                case STEP:
                    return "spm";
                default:
                    return "";
            }
        }

        public String defaultUnits() {
            return defaultUnits(this);
        }

        public static DataType fromOrdinal(int id) {
            return (id < 0 || id >= values().length) ? UNKOWN : values()[id];
        }

        public boolean equals(SensorType sensorType) {
            switch (this) {
                case HEARTRATE:
                    return sensorType.equals(SensorType.HEARTRATE);
                case SPEED:
                    return sensorType.equals(SensorType.SPEED) || sensorType.equals(SensorType.SPEED_AND_CADENCE);
                case CADENCE:
                    return sensorType.equals(SensorType.CADENCE) || sensorType.equals(SensorType.SPEED_AND_CADENCE);
                case POWER:
                    return sensorType.equals(SensorType.POWER);
                case KICK:
                case STRIDE:
                case OXYGEN:
                default:
                    return sensorType.defaultDataType() == this;
                case STEP:
                    return sensorType.equals(SensorType.RUNNING_SPEED_CADENCE);
            }
        }

        public static DataType guessFromString(String name) {
            return name.contains("cadence") ? CADENCE : name.contains("heart") ? HEARTRATE : name.contains("oxygen") ? OXYGEN : name.contains("power") ? POWER : name.contains(BaseDataTypes.ID_SPEED) ? SPEED : name.contains("stride") ? STRIDE : name.contains("pace") ? PACE : name.contains("step") ? STEP : name.contains("kick") ? KICK : UNKOWN;
        }
    }

    public enum SensorType {
        UNKNOWN,
        CADENCE,
        SPEED,
        SPEED_AND_CADENCE,
        HEARTRATE,
        POWER,
        BUTTONS,
        COMBO,
        FOOT_POD,
        OXYGEN,
        CABLE,
        STRIDE,
        RUNNING_SPEED_CADENCE;

        public DataType defaultDataType() {
            switch (this) {
                case CADENCE:
                    return DataType.CADENCE;
                case SPEED:
                    return DataType.SPEED;
                case HEARTRATE:
                    return DataType.HEARTRATE;
                case POWER:
                    return DataType.POWER;
                case STRIDE:
                    return DataType.STRIDE;
                default:
                    return DataType.UNKOWN;
            }
        }

        public boolean equals(SensorType type) {
            switch (this) {
                case CADENCE:
                    return type == CADENCE || type == SPEED_AND_CADENCE;
                case SPEED:
                    return type == SPEED || type == SPEED_AND_CADENCE;
                case HEARTRATE:
                case POWER:
                case STRIDE:
                default:
                    return type == this;
                case SPEED_AND_CADENCE:
                    return type == SPEED || type == SPEED_AND_CADENCE || type == CADENCE;
                case COMBO:
                    return type == SPEED || type == SPEED_AND_CADENCE || type == CADENCE || type == POWER || type == HEARTRATE;
                case FOOT_POD:
                    return type == SPEED || type == RUNNING_SPEED_CADENCE || type == CADENCE || type == STRIDE || type == HEARTRATE;
                case RUNNING_SPEED_CADENCE:
                    return type == SPEED || type == RUNNING_SPEED_CADENCE || type == CADENCE || type == STRIDE;
                case UNKNOWN:
                    return false;
            }
        }

        public static SensorType fromOrdinal(int id) {
            return (id < 0 || id >= values().length) ? UNKNOWN : values()[id];
        }
    }

    public enum ConnectionState {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING,
        RECONNECTING,
        ERROR;

        public boolean isConnectingOrConnected() {
            switch (this) {
                case CONNECTED:
                case CONNECTING:
                case RECONNECTING:
                    return true;
                default:
                    return false;
            }
        }
    }

    protected Sensor(SensorObserver obs) {
        this.mObs = obs;
    }

    protected void onSensorChanged() {
        if (this.mObs != null) {
            this.mObs.onSensorChanged(this);
        }
    }

    protected void onSensorConnected() {
        this.mLastData.clear();
        if (this.mObs != null) {
            this.mObs.onSensorConnected(this);
        }
    }

    protected void onSensorDisconnected() {
        this.mUseMap.clear();
        if (this.mObs != null) {
            this.mObs.onSensorDisconnected(this);
        }
        if (this.mConfigObs != null) {
            this.mConfigObs.onSensorDisconnected(this);
        }
    }

    protected void onSensorUnavailable() {
        if (this.mObs != null) {
            this.mObs.onSensorUnavailable(this);
        }
    }

    protected void onSensorAvailable() {
        if (this.mObs != null) {
            this.mObs.onSensorAvailable(this);
        }
    }

    protected void onSensorTimeout() {
        if (this.mObs != null) {
            this.mObs.onSensorTimeout(this);
        }
    }

    public boolean equals(Sensor other) {
        return other.getId() != null && other.getId().contentEquals(getId());
    }

    public boolean equals(DataType type) {
        return this.mHasMap.contains(type);
    }

    public boolean hasType() {
        return getType() != SensorType.UNKNOWN;
    }

    public int getTypeName() {
        switch (getType()) {
            case CADENCE:
                return R.string.sensor_desc_cadence;
            case SPEED:
                return R.string.sensor_desc_speed;
            case HEARTRATE:
                return R.string.sensor_desc_heartrate;
            case POWER:
                return R.string.sensor_desc_power;
            case STRIDE:
            case UNKNOWN:
            default:
                return R.string.sensor_desc_unknown;
            case SPEED_AND_CADENCE:
                return R.string.sensor_desc_speed_cadence;
            case COMBO:
                return R.string.sensor_desc_combo;
            case FOOT_POD:
                return R.string.sensor_desc_running_combo;
            case RUNNING_SPEED_CADENCE:
                return R.string.sensor_desc_running_scm;
            case BUTTONS:
                return R.string.sensor_desc_buttons;
            case OXYGEN:
                return R.string.sensor_desc_oxygen;
        }
    }

    public boolean isConnected() {
        return getConnectionState() == ConnectionState.CONNECTED;
    }

    public boolean isInUse() {
        return !this.mUseMap.isEmpty();
    }

    public boolean isUsedFor(DataType type) {
        return this.mUseMap.contains(type);
    }

    protected void useFor(DataType type) {
        if (this.mHasMap.contains(type)) {
            this.mUseMap.add(type);
        }
    }

    protected void stopUsing(DataType type) {
        if (this.mUseMap.contains(type)) {
            this.mUseMap.remove(type);
        }
    }

    protected void gainDataType(DataType type) {
        this.mHasMap.add(type);
    }

    protected void loseDataType(DataType type) {
        this.mHasMap.remove(type);
        this.mUseMap.remove(type);
    }

    public DataType[] getDataTypes() {
        return (DataType[]) this.mHasMap.toArray(new DataType[0]);
    }

    public boolean hasType(DataType type) {
        return this.mHasMap.contains(type);
    }

    public String getLastValue() {
        return this.mLastValueString;
    }

    public void setLastValue(SensorType sensorType, DataType dataType, SensorData value) {
        this.mLastData.put(dataType, value);
    }

    public SensorData getLastValue(SensorType sensorType, DataType dataType) {
        return this.mLastData.containsKey(dataType) ? this.mLastData.get(dataType) : SensorData.EMPTY;
    }

    public boolean isReceivingData(DataType dataType) {
        return System.currentTimeMillis() - getLastUpdateTime(dataType) < 5000;
    }

    protected void onNewData(SensorType sensorType, DataType dataType, int value) {
        this.mLastValueString = String.format("%d %s", Integer.valueOf(value), dataType.defaultUnits());
        if (this.mObs != null) {
            this.mObs.onSensorData(this, dataType, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNewData(SensorType sensorType, DataType dataType, double value) {
        this.mLastValueString = String.format("%.1f %s", Double.valueOf(value), dataType.defaultUnits());
        if (this.mObs != null) {
            this.mObs.onSensorData(this, dataType, value);
        }
    }

    protected void onNewData(SensorType sensorType, DataType dataType, double value, long interval) {
        this.mLastValueString = String.format("%.1f %s", Double.valueOf(value), dataType.defaultUnits());
        if (this.mObs != null) {
            this.mObs.onSensorData(this, dataType, value, interval);
        }
    }

    public boolean hasConfig() {
        return false;
    }

    public void onConfigMessage(boolean success, String msg) {
        if (this.mObs != null) {
            this.mObs.onSensorConfigMessage(this, success, msg);
        }
        if (this.mConfigObs != null) {
            this.mConfigObs.onSensorConfigChanged(this);
        }
    }

    public void setConfigObserver(SensorConfigObserver obs) {
        this.mConfigObs = obs;
    }

    public String serialize() {
        return "";
    }

    public static Sensor deserialize(String params, Context context) {
        return null;
    }

    public String toString() {
        return getName() + " (" + getId() + ") - " + getTypeName();
    }
}
