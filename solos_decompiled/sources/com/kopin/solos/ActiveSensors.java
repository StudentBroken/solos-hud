package com.kopin.solos;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.SensorMenuAdapter;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes24.dex */
public class ActiveSensors {
    private static final String TAG = "ActiveSensors";
    private static final Sensor mNoSensor = new Sensor(null) { // from class: com.kopin.solos.ActiveSensors.2
        @Override // com.kopin.solos.sensors.Sensor
        public boolean equals(Sensor.SensorType type) {
            return false;
        }

        @Override // com.kopin.solos.sensors.Sensor
        public String getName() {
            return "NONE";
        }

        @Override // com.kopin.solos.sensors.Sensor
        public String getId() {
            return null;
        }

        @Override // com.kopin.solos.sensors.Sensor
        public Sensor.SensorType getType() {
            return null;
        }

        @Override // com.kopin.solos.sensors.Sensor
        public void connect() {
        }

        @Override // com.kopin.solos.sensors.Sensor
        public void disconnect() {
        }

        @Override // com.kopin.solos.sensors.Sensor
        public Sensor.ConnectionState getConnectionState() {
            return null;
        }

        @Override // com.kopin.solos.sensors.Sensor
        public void activate() {
        }

        @Override // com.kopin.solos.sensors.Sensor
        public void deactivate() {
        }

        @Override // com.kopin.solos.sensors.Sensor
        protected long getLastUpdateTime(Sensor.DataType dataType) {
            return 0L;
        }

        @Override // com.kopin.solos.sensors.Sensor
        public boolean isBatteryOk() {
            return false;
        }
    };
    private static ActiveSensors self;
    private CustomActionProvider<SensorMenuAdapter.SensorMenuItem> mActionProvider;
    private Context mContext;
    private SensorMenuAdapter mMenuAdapter;
    private HardwareReceiverService mService;
    private ArrayList<SensorMenuAdapter.SensorMenuItem> mSensorMenuItems = new ArrayList<>();
    private final DataListener mDataListener = new DataListener() { // from class: com.kopin.solos.ActiveSensors.1
        @Override // com.kopin.solos.storage.DataListener
        public void onSpeed(double speedMPS) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.SPEED);
            if (item != null) {
                item.setUnit(Conversion.getUnitOfSpeed(ActiveSensors.this.mContext));
                item.setValue((int) Conversion.speedForLocale(speedMPS));
            }
            SensorMenuAdapter.SensorMenuItem item2 = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.PACE);
            if (item2 != null) {
                item2.setUnit(Conversion.getUnitOfPace(ActiveSensors.this.mContext));
                item2.setValue((int) Conversion.speedToPaceForLocale(speedMPS));
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onCadence(double cadenceRPM) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.CADENCE);
            if (item != null) {
                item.setValue((int) cadenceRPM);
            }
            SensorMenuAdapter.SensorMenuItem item2 = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.STEP);
            if (item2 != null) {
                item2.setValue((int) cadenceRPM);
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onHeartRate(int heartrate) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.HEARTRATE);
            if (item != null) {
                item.setValue(heartrate);
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onBikePower(double power) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.POWER);
            if (item != null) {
                item.setValue((int) power);
            }
            SensorMenuAdapter.SensorMenuItem item2 = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.KICK);
            if (item2 != null) {
                item2.setValue((int) power);
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onOxygen(int oxygen) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.OXYGEN);
            if (item != null) {
                item.setValue(oxygen);
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onStride(double stride) {
            SensorMenuAdapter.SensorMenuItem item = ActiveSensors.this.getSensorMenuItem(Sensor.DataType.STRIDE);
            if (item != null) {
                item.setValue((int) Conversion.strideForLocale(ActiveSensors.this.mService.getLastStride()));
            }
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onAltitude(float value) {
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onLocation(Location location) {
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onButtonPress(Sensor.ButtonAction action) {
        }
    };

    private enum SensorStatus {
        UNKNOWN_SENSOR(Sensor.DataType.UNKOWN),
        HR_SENSOR(Sensor.DataType.HEARTRATE),
        SPEED_SENSOR(Sensor.DataType.SPEED),
        PACE_SENSOR(Sensor.DataType.PACE),
        POWER_SENSOR(Sensor.DataType.POWER),
        CADENCE_SENSOR(Sensor.DataType.CADENCE),
        STEP_SENSOR(Sensor.DataType.STEP),
        STRIDE_SENSOR(Sensor.DataType.STRIDE),
        KICK_SENSOR(Sensor.DataType.KICK),
        OXYGEN_SENSOR(Sensor.DataType.OXYGEN),
        OPTION_SENSORS(Sensor.DataType.UNKOWN),
        MESSAGE_EMPTY(Sensor.DataType.UNKOWN);

        private final Sensor.DataType type;

        SensorStatus(Sensor.DataType backingType) {
            this.type = backingType;
        }

        static SensorStatus value(int ordinal) {
            return (ordinal < 0 || ordinal > values().length) ? UNKNOWN_SENSOR : values()[ordinal];
        }

        static SensorStatus getSensorId(Sensor.DataType forType) {
            switch (forType) {
                case HEARTRATE:
                    return HR_SENSOR;
                case SPEED:
                    return SPEED_SENSOR;
                case PACE:
                    return PACE_SENSOR;
                case CADENCE:
                    return CADENCE_SENSOR;
                case STEP:
                    return STEP_SENSOR;
                case POWER:
                    return POWER_SENSOR;
                case OXYGEN:
                    return OXYGEN_SENSOR;
                case STRIDE:
                    return STRIDE_SENSOR;
                case KICK:
                    return KICK_SENSOR;
                default:
                    return UNKNOWN_SENSOR;
            }
        }
    }

    private ActiveSensors(Context context) {
        this.mContext = context;
        this.mMenuAdapter = new SensorMenuAdapter(context);
        this.mActionProvider = new CustomActionProvider<>(context);
        this.mActionProvider.setMenuAdapter(this.mMenuAdapter);
        createSensorMenuItems();
    }

    public static void init(Context context) {
        self = new ActiveSensors(context);
    }

    public static void onServiceConnected(HardwareReceiverService service) {
        self.mService = service;
        service.addDataListener(self.mDataListener);
        self.checkSensors();
    }

    public static void onServiceDisconnected(HardwareReceiverService service) {
        service.removeDataListener(self.mDataListener);
        self.mService = null;
    }

    public static CustomActionProvider<SensorMenuAdapter.SensorMenuItem> getActionProvider() {
        return self.mActionProvider;
    }

    public static SensorMenuAdapter.SensorMenuItem createMenuItem(String title) {
        return new SensorMenuAdapter.SensorMenuItem(title, SensorStatus.OPTION_SENSORS.ordinal(), "");
    }

    public static boolean checkForActiveSensors() {
        self.checkSensors();
        return self.anySensorProviding();
    }

    public static void onPrepareActionProvider(CustomActionProvider<SensorMenuAdapter.SensorMenuItem> actionProvider) {
        actionProvider.clear();
        self.checkSensors();
        for (SensorMenuAdapter.SensorMenuItem item : self.mSensorMenuItems) {
            actionProvider.addMenuItem(item);
        }
    }

    public static void onMenuItemSelected(CustomMenuItem menuItem) {
        switch (SensorStatus.value(menuItem.getId())) {
            case OPTION_SENSORS:
                Intent intent = new Intent(self.mContext, (Class<?>) SetupActivity.class);
                intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 8);
                self.mContext.startActivity(intent);
                break;
        }
    }

    private void createSensorMenuItem(Sensor.DataType sensorType) {
        if (SensorsConnector.isAllowedType(sensorType)) {
            SensorMenuAdapter.SensorMenuItem menuItem = createMenuItem(sensorType);
            this.mSensorMenuItems.add(menuItem);
            setDefaultValue(menuItem);
        }
    }

    private void createSensorMenuItems() {
        createSensorMenuItem(Sensor.DataType.SPEED);
        createSensorMenuItem(Sensor.DataType.PACE);
        createSensorMenuItem(Sensor.DataType.CADENCE);
        createSensorMenuItem(Sensor.DataType.STEP);
        createSensorMenuItem(Sensor.DataType.STRIDE);
        createSensorMenuItem(Sensor.DataType.HEARTRATE);
        createSensorMenuItem(Sensor.DataType.POWER);
        createSensorMenuItem(Sensor.DataType.KICK);
        this.mMenuAdapter.notifyDataSetInvalidated();
    }

    private String getSensorTitle(Sensor.DataType forType) {
        int resId;
        switch (forType) {
            case HEARTRATE:
                resId = R.string.sensor_desc_heartrate;
                break;
            case SPEED:
                resId = R.string.sensor_desc_speed;
                break;
            case PACE:
                resId = R.string.sensor_desc_pace;
                break;
            case CADENCE:
                resId = R.string.sensor_desc_cadence;
                break;
            case STEP:
                resId = R.string.sensor_desc_step;
                break;
            case POWER:
            case KICK:
                resId = R.string.sensor_desc_power;
                break;
            case OXYGEN:
                resId = R.string.sensor_desc_oxygen;
                break;
            case STRIDE:
                resId = R.string.sensor_desc_stride;
                break;
            default:
                resId = R.string.sensor_desc_unknown;
                break;
        }
        return this.mContext.getString(resId);
    }

    private String getSensorUnits(Sensor.DataType forType) {
        int resId;
        switch (forType) {
            case HEARTRATE:
                resId = R.string.heart_unit;
                break;
            case SPEED:
                return Conversion.getUnitOfSpeed(this.mContext);
            case PACE:
                return Conversion.getUnitOfPace(this.mContext);
            case CADENCE:
                resId = R.string.cadence_unit;
                break;
            case STEP:
                resId = R.string.unit_cadence_run_short;
                break;
            case POWER:
            case KICK:
                resId = R.string.power_unit;
                break;
            case OXYGEN:
                resId = R.string.oxygen_unit;
                break;
            case STRIDE:
                return Conversion.getUnitOfStride(this.mContext);
            default:
                resId = android.R.string.unknownName;
                break;
        }
        return this.mContext.getString(resId);
    }

    private void setDefaultValue(SensorMenuAdapter.SensorMenuItem item) {
        if (this.mService == null) {
            item.setValue(Integer.MIN_VALUE);
        }
        switch (SensorStatus.value(item.getId())) {
            case HR_SENSOR:
                item.setValue(this.mService.getLastHeartRate());
                break;
            case SPEED_SENSOR:
                double speed = Utility.distanceInUnitSystem(this.mService.getLastSpeed(), Prefs.getUnitSystem());
                item.setUnit(Conversion.getUnitOfSpeed(this.mContext));
                item.setValue((int) speed);
                break;
            case POWER_SENSOR:
            case KICK_SENSOR:
                item.setValue((int) this.mService.getLastPower());
                break;
            case CADENCE_SENSOR:
                item.setValue((int) this.mService.getLastCadence());
                break;
            case OXYGEN_SENSOR:
                item.setValue(this.mService.getLastOxygen());
                break;
            case STRIDE_SENSOR:
                item.setValue((int) Conversion.strideForLocale(this.mService.getLastStride()));
                break;
            case PACE_SENSOR:
                double speed2 = Utility.distanceInUnitSystem(this.mService.getLastSpeed(), Prefs.getUnitSystem());
                item.setUnit(Conversion.getUnitOfPace(this.mContext));
                item.setValue((int) speed2);
                break;
            case STEP_SENSOR:
                item.setValue((int) this.mService.getLastCadence());
                break;
        }
    }

    private SensorMenuAdapter.SensorMenuItem createMenuItem(Sensor sensor, Sensor.DataType type) {
        return new SensorMenuAdapter.SensorMenuItem(getSensorTitle(type), SensorStatus.getSensorId(type).ordinal(), getSensorUnits(type));
    }

    private SensorMenuAdapter.SensorMenuItem createMenuItem(Sensor.DataType type) {
        return new SensorMenuAdapter.SensorMenuItem(type, getSensorTitle(type), SensorStatus.getSensorId(type).ordinal(), getSensorUnits(type));
    }

    private void updateMenuItem(SensorMenuAdapter.SensorMenuItem item) {
        Sensor sensor = SensorList.getSensorProviding(item.getSensorDataType());
        if (sensor != null) {
            item.update(getSensorTitle(item.getSensorDataType()), getSensorUnits(item.getSensorDataType()), sensor.getName(), sensor.getConnectionState());
        } else {
            item.update(getSensorTitle(item.getSensorDataType()), getSensorUnits(item.getSensorDataType()), null, Sensor.ConnectionState.DISCONNECTED);
        }
    }

    private boolean anySensorProviding() {
        for (SensorMenuAdapter.SensorMenuItem menuItem : this.mSensorMenuItems) {
            if (SensorList.getSensorProviding(menuItem.getSensorDataType()) != null) {
                return true;
            }
        }
        return false;
    }

    private void checkSensors() {
        for (SensorMenuAdapter.SensorMenuItem sensorMenuItem : this.mSensorMenuItems) {
            updateMenuItem(sensorMenuItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SensorMenuAdapter.SensorMenuItem getSensorMenuItem(Sensor.DataType type) {
        for (SensorMenuAdapter.SensorMenuItem item : this.mSensorMenuItems) {
            if (item.isSensorType(type)) {
                return item;
            }
        }
        return null;
    }
}
