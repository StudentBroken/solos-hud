package com.kopin.solos.sensors;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.debug.EventLog;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.btle.BTLEScanner;
import com.kopin.solos.sensors.btle.BTLESensor;
import com.kopin.solos.sensors.btle.CableSensor;
import com.kopin.solos.sensors.fake.FakeConfigurator;
import com.kopin.solos.sensors.fake.FakeSensor;
import com.kopin.solos.sensors.utils.DialogUtils;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes28.dex */
public class SensorsConnector {
    private static final boolean FAKE_SENSORS = false;
    private static final String TAG = "SensorsConnector";
    private static BTLEScanner mBTLEScanner;
    private static FakeConfigurator mFakeScanner;
    private static SensorDataSink mSink;
    private static final Sensor.SensorType[] SUPPORTED_TYPES_DEFAULT = {Sensor.SensorType.HEARTRATE, Sensor.SensorType.COMBO, Sensor.SensorType.CABLE};
    private static final Sensor.SensorType[] SUPPORTED_TYPES_RIDE = {Sensor.SensorType.HEARTRATE, Sensor.SensorType.SPEED, Sensor.SensorType.CADENCE, Sensor.SensorType.POWER, Sensor.SensorType.SPEED_AND_CADENCE, Sensor.SensorType.COMBO, Sensor.SensorType.CABLE};
    private static final Sensor.SensorType[] SUPPORTED_TYPES_RUN = {Sensor.SensorType.HEARTRATE, Sensor.SensorType.STRIDE, Sensor.SensorType.RUNNING_SPEED_CADENCE, Sensor.SensorType.FOOT_POD, Sensor.SensorType.CABLE};
    private static Sensor.SensorType[] SUPPORTED_TYPES = SUPPORTED_TYPES_DEFAULT;
    private static final Sensor.DataType[] ALLOWED_TYPES_DEFAULT = {Sensor.DataType.HEARTRATE, Sensor.DataType.SPEED, Sensor.DataType.ALTITUDE};
    private static final Sensor.DataType[] ALLOWED_TYPES_RIDE = {Sensor.DataType.HEARTRATE, Sensor.DataType.SPEED, Sensor.DataType.CADENCE, Sensor.DataType.POWER, Sensor.DataType.ALTITUDE};
    private static final Sensor.DataType[] ALLOWED_TYPES_RUN = {Sensor.DataType.HEARTRATE, Sensor.DataType.PACE, Sensor.DataType.STEP, Sensor.DataType.KICK, Sensor.DataType.STRIDE, Sensor.DataType.ALTITUDE};
    private static Sensor.DataType[] ALLOWED_TYPES = ALLOWED_TYPES_DEFAULT;
    private static boolean mDiscovering = false;
    private static final ScannerListener mScannerCallback = new ScannerListener() { // from class: com.kopin.solos.sensors.SensorsConnector.1
        @Override // com.kopin.solos.sensors.SensorsConnector.ScannerListener
        public void onSensorFound(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor FOUND " + sensor.getName());
            SensorList.add(sensor);
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.ScannerListener
        public void onSensorLost(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor LOST " + sensor.getName());
            SensorList.remove(sensor);
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.ScannerListener
        public void onAntBridgeConnected(CableSensor cable) {
            SensorList.onNewSensorProvider();
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.ScannerListener
        public void checkAntBridgeConnected(CableSensor cable) {
            if (SensorsConnector.mBTLEScanner != null) {
                SensorsConnector.mBTLEScanner.checkCableDevice(cable);
            }
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorChanged(Sensor sensor) {
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorData(Sensor sensor, Sensor.DataType dataType, int value) {
            if (!sensor.isConnected() || !sensor.isUsedFor(dataType)) {
                EventLog.d(EventLog.ModuleTag.SENSORS, "Ignoring " + dataType + " data from sensor " + sensor.getName());
            }
            EventLog.d(EventLog.ModuleTag.SENSORS, "Got " + dataType + " data from sensor " + sensor.getName() + ", value = " + value);
            switch (AnonymousClass2.$SwitchMap$com$kopin$solos$sensors$Sensor$DataType[dataType.ordinal()]) {
                case 1:
                    SensorsConnector.mSink.onHeartRate(value);
                    break;
                case 2:
                    SensorsConnector.mSink.onOxygen(value);
                    break;
            }
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorData(Sensor sensor, Sensor.DataType dataType, double value) {
            if (!sensor.isConnected() || !sensor.isUsedFor(dataType)) {
                EventLog.d(EventLog.ModuleTag.SENSORS, "Ignoring " + dataType + " data from sensor " + sensor.getName());
            }
            EventLog.d(EventLog.ModuleTag.SENSORS, "Got " + dataType + " data from sensor " + sensor.getName() + ", value = " + value);
            switch (AnonymousClass2.$SwitchMap$com$kopin$solos$sensors$Sensor$DataType[dataType.ordinal()]) {
                case 3:
                case 4:
                    SensorsConnector.mSink.onCadence(value);
                    break;
                case 5:
                case 6:
                    SensorsConnector.mSink.onBikePower(value);
                    break;
                case 7:
                case 8:
                    SensorsConnector.mSink.onSpeed(value);
                    break;
                case 9:
                    SensorsConnector.mSink.onStride(value);
                    break;
            }
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorData(Sensor sensor, Sensor.DataType dataType, double value, long interval) {
            if (!sensor.isConnected() || !sensor.isUsedFor(dataType)) {
                EventLog.d(EventLog.ModuleTag.SENSORS, "Ignoring " + dataType + " data from sensor " + sensor.getName());
            }
            EventLog.d(EventLog.ModuleTag.SENSORS, "Got " + dataType + " data from sensor " + sensor.getName() + ", value = " + value + ", interval = " + interval);
            switch (AnonymousClass2.$SwitchMap$com$kopin$solos$sensors$Sensor$DataType[dataType.ordinal()]) {
                case 7:
                    SensorsConnector.mSink.onWheelRevs(value, interval);
                    break;
                case 8:
                    SensorsConnector.mSink.onSpeed(value);
                    double distTravelled = (interval * value) / 1000.0d;
                    SensorsConnector.mSink.onDistance(distTravelled);
                    break;
            }
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorConnected(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor CONNECTED " + sensor.getName());
            SensorList.sensorConnected(sensor);
            SensorsConnector.selectSensor(sensor);
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorDisconnected(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor DISCONNECTED " + sensor.getName());
            SensorList.sensorDisconnected(sensor);
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorUnavailable(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor UNAVAILABLE " + sensor.getName());
            SensorsConnector.refreshSensorSelection(sensor.getDataTypes());
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorAvailable(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor AVAILABLE " + sensor.getName());
            SensorsConnector.selectSensor(sensor);
            SensorsConnector.mSink.onSensorsChanged();
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorTimeout(Sensor sensor) {
            EventLog.d(EventLog.ModuleTag.SENSORS, "Sensor TIMEOUT " + sensor.getName());
            SensorsConnector.mSink.onSensorsChanged();
            if (sensor instanceof CableSensor) {
                SensorsConnector.mSink.onCableEvent(sensor.getConnectionState().isConnectingOrConnected(), ((CableSensor) sensor).isActive());
            }
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorObserver
        public void onSensorConfigMessage(Sensor sensor, boolean ok, String msg) {
            SensorsConnector.mSink.onSensorConfig(ok, msg);
        }
    };

    public interface ScannerListener extends Sensor.SensorObserver {
        void checkAntBridgeConnected(CableSensor cableSensor);

        void onAntBridgeConnected(CableSensor cableSensor);

        void onSensorFound(Sensor sensor);

        void onSensorLost(Sensor sensor);
    }

    public interface SensorDataSink {
        void onBikePower(double d);

        void onButtonPress(Sensor.ButtonAction buttonAction);

        void onCableEvent(boolean z, boolean z2);

        void onCadence(double d);

        void onDistance(double d);

        void onHeartRate(int i);

        void onLocation(Location location);

        void onOxygen(int i);

        void onSensorConfig(boolean z, String str);

        void onSensorsChanged();

        void onSpeed(double d);

        void onStride(double d);

        void onWheelRevs(double d, long j);
    }

    public static void init(Context context, SensorDataSink sink) {
        mSink = sink;
        if (Build.VERSION.SDK_INT >= 21) {
            mBTLEScanner = new BTLEScanner(context, mScannerCallback);
        }
    }

    public static void setForSport(SportType sport) {
        switch (sport) {
            case RIDE:
                SUPPORTED_TYPES = SUPPORTED_TYPES_RIDE;
                ALLOWED_TYPES = ALLOWED_TYPES_RIDE;
                break;
            case RUN:
                SUPPORTED_TYPES = SUPPORTED_TYPES_RUN;
                ALLOWED_TYPES = ALLOWED_TYPES_RUN;
                break;
            default:
                SUPPORTED_TYPES = SUPPORTED_TYPES_DEFAULT;
                ALLOWED_TYPES = ALLOWED_TYPES_DEFAULT;
                break;
        }
        ArrayList<Sensor> sensors = SensorList.getConnectedAndSavedList();
        for (Sensor s : sensors) {
            if (s.isConnected() && !isSupportedType(s.getType())) {
                s.disconnect();
            }
        }
    }

    public static void enable() {
        Log.d(TAG, "ENABLE: SensorsConnector now active");
        SensorList.enable();
    }

    public static void disable() {
        Log.d(TAG, "DISABLE: SensorsConnector now idle");
        SensorList.disable();
        connectAntBridge(0);
    }

    public static boolean isSupportedType(Sensor.SensorType type) {
        for (Sensor.SensorType t : SUPPORTED_TYPES) {
            if (t.ordinal() == type.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllowedType(Sensor.DataType type) {
        for (Sensor.DataType t : ALLOWED_TYPES) {
            if (t.ordinal() == type.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAntBridgeConnected() {
        if (mBTLEScanner == null) {
            return false;
        }
        return mBTLEScanner.isCableConnected();
    }

    public static boolean isAntBridgeActive() {
        if (mBTLEScanner == null) {
            return false;
        }
        return mBTLEScanner.isCableActive();
    }

    public static boolean isAntBridgeActive(int brigdeId) {
        if (mBTLEScanner == null) {
            return false;
        }
        return mBTLEScanner.isCableActive(brigdeId);
    }

    static boolean hasCableId() {
        return mBTLEScanner != null && mBTLEScanner.hasCableId();
    }

    static void checkCable() {
        if (mBTLEScanner != null) {
            mBTLEScanner.checkCable();
        }
    }

    public static void connectAntBridge(int bridgeId) {
        if (mBTLEScanner != null) {
            mBTLEScanner.setCableDevName(bridgeId);
        }
    }

    public static void reset() {
        SensorList.reset();
        if (mBTLEScanner != null) {
            mBTLEScanner.reset();
        }
    }

    public static void startDiscovery() {
        if (mBTLEScanner != null) {
            try {
                mBTLEScanner.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        if (mFakeScanner != null) {
            mFakeScanner.load();
        }
        mDiscovering = true;
    }

    public static void stopDiscovery() {
        try {
            if (mBTLEScanner != null) {
                mBTLEScanner.stop();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mDiscovering = false;
    }

    public static boolean isDiscovering() {
        return mDiscovering;
    }

    public static void refreshSensorSinks() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void refreshSensorSelection(Sensor.DataType[] forTypes) {
        for (Sensor s : SensorList.getConnectedAndSavedList()) {
            if (s != null && s.isConnected() && !s.isInUse()) {
                for (Sensor.DataType forType : forTypes) {
                    if (s.equals(forType)) {
                        tryToUse(s, forType);
                    }
                }
            }
        }
    }

    private static void refreshSensorSelection(Sensor.SensorType forType) {
        for (Sensor s : SensorList.getConnectedAndSavedList()) {
            if (s != null && s.isConnected() && !s.isInUse() && s.equals(forType)) {
                selectSensor(s);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void selectSensor(Sensor sensor) {
        Sensor.SensorType type = sensor.getType();
        Sensor.DataType[] dataTypes = sensor.getDataTypes();
        if (type == Sensor.SensorType.UNKNOWN || dataTypes.length == 0) {
            Log.d(TAG, "Don't know how to use sensor " + sensor.getName());
            return;
        }
        for (Sensor.DataType dataType : dataTypes) {
            tryToUse(sensor, dataType);
        }
    }

    private static void tryToUse(Sensor sensor, Sensor.DataType forType) {
        if (SensorList.isSensorConnected(forType)) {
            Log.d(TAG, "Already have a sensor of type " + forType + " connected and in use, ignoring for now.");
        } else {
            sensor.useFor(forType);
        }
    }

    public static ScannerListener getScannerListener() {
        return mScannerCallback;
    }

    public static void configureSensor(Context context, Sensor sensor) {
        Dialog configDialog = null;
        if ((sensor instanceof BTLESensor) && mBTLEScanner != null) {
            configDialog = mBTLEScanner.configureSensor(context, (BTLESensor) sensor);
        }
        if ((sensor instanceof FakeSensor) && mFakeScanner != null) {
            configDialog = mFakeScanner.configureSensor(context, (FakeSensor) sensor);
        }
        if (configDialog != null) {
            stopDiscovery();
            configDialog.show();
            DialogUtils.setDialogTitleDivider(configDialog);
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.sensors.SensorsConnector$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$sensors$Sensor$DataType = new int[Sensor.DataType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.HEARTRATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.OXYGEN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.STEP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.CADENCE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.KICK.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.POWER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.SPEED.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.PACE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$DataType[Sensor.DataType.STRIDE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$com$kopin$solos$common$SportType = new int[SportType.values().length];
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RUN.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
        }
    }
}
