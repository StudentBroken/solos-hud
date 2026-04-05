package com.kopin.solos.sensors.btle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorData;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.btle.BTLESensorConfig;
import com.kopin.solos.sensors.btle.CableAnt;
import com.kopin.solos.sensors.btle.CableControl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/* JADX INFO: loaded from: classes28.dex */
public class CableSensor extends BTLESensor {
    private static final long ACTIVITY_TIMEOUT = 5000;
    private static final boolean ALLOW_FACTORY_RESET = false;
    private static final int ANT_SCAN_DELAY = 12;
    private static final int ANT_SCAN_TIME = 3;
    static final int CABLE_MANUFACTURER_ID = 816;
    private static final long INACTIVE_RESET_TIMEOUT = 15000;
    private static final long REFRESH_STATUS_INITIAL = 500;
    private static final long REFRESH_STATUS_REPEAT = 5000;
    private static final long SCAN_REPEAT_DELAY = 3000;
    static final String TAG = "CABLE";
    private int mActiveSet;
    private BluetoothGattCharacteristic mCableControlPoint;
    private boolean mHardReset;
    private boolean mIsScanActive;
    private boolean mIsScanEnabled;
    private long mLastReq;
    private long mLastRx;
    private long mLastScanTime;
    private SensorsConnector.ScannerListener mListCb;
    private boolean mRefreshQueued;
    private final Runnable mRefreshStatus;
    private int mRequiredSet;
    private HashMap<String, CableSensorStub> mScannedList;
    private CableAnt.ChannelSlot[] mSlots;
    static final UUID GATT_SERVICE_CABLE = UUID.fromString("4B480001-6E6F-7274-6870-6F6C65656E67");
    static final UUID GATT_CHARACTERISTIC_CABLE_CONTROL = UUID.fromString("4B480002-6E6F-7274-6870-6F6C65656E67");
    static final UUID GATT_CHARACTERISTIC_CABLE_RX = UUID.fromString("4B480003-6E6F-7274-6870-6F6C65656E67");
    private static HashSet<CableSensor> CABLE_CACHE = new HashSet<>();

    public CableSensor(Context context, BluetoothDevice dev, String name, SensorsConnector.ScannerListener obs) {
        super(context, dev, name, Sensor.SensorType.COMBO, obs);
        this.mHardReset = false;
        this.mRefreshQueued = false;
        this.mActiveSet = -1;
        this.mRequiredSet = 0;
        this.mScannedList = new HashMap<>();
        this.mRefreshStatus = new Runnable() { // from class: com.kopin.solos.sensors.btle.CableSensor.2
            @Override // java.lang.Runnable
            public void run() {
                boolean again;
                synchronized (this) {
                    CableSensor.this.mRefreshQueued = false;
                    Log.d(CableSensor.TAG, "Refreshing statuses...");
                    again = CableSensor.this.queryActiveSlots();
                }
                if (again) {
                    CableSensor.this.refreshStatus(false);
                }
            }
        };
        this.mListCb = obs;
        this.mSlots = new CableAnt.ChannelSlot[]{new CableAnt.ChannelSlot(), new CableAnt.ChannelSlot(), new CableAnt.ChannelSlot(), new CableAnt.ChannelSlot(), new CableAnt.ChannelSlot()};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLegacyCable() {
        String version = getVersion();
        if (version == null || version.isEmpty()) {
            return true;
        }
        String[] v1 = version.split("\\.");
        if (v1 == null || v1.length != 3) {
            return true;
        }
        if (Integer.valueOf(v1[0]).intValue() > 1) {
            return false;
        }
        if (Integer.valueOf(v1[0]).intValue() < 1) {
            return true;
        }
        if (Integer.valueOf(v1[1]).intValue() > 6) {
            return false;
        }
        if (Integer.valueOf(v1[1]).intValue() < 6) {
            return true;
        }
        return Integer.valueOf(v1[2]).intValue() <= 11 && Integer.valueOf(v1[2]).intValue() < 11;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isUsedFor(Sensor.DataType type) {
        return false;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isInUse() {
        return false;
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void useFor(Sensor.DataType type) {
    }

    @Override // com.kopin.solos.sensors.Sensor
    public SensorData getLastValue(Sensor.SensorType sensorType, Sensor.DataType dataType) {
        CableSensorStub sensor = findActiveDummySensor(CableAnt.DeviceType.fromSensorType(sensorType));
        if (sensor == null) {
            sensor = findActiveDummySensor(CableAnt.DeviceType.fromDataType(dataType));
        }
        return sensor != null ? sensor.getLastValue(sensorType, dataType) : SensorData.EMPTY;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void setLastValue(Sensor.SensorType sensorType, Sensor.DataType dataType, SensorData value) {
        CableSensorStub sensor = findActiveDummySensor(CableAnt.DeviceType.fromSensorType(sensorType));
        if (sensor == null) {
            sensor = findActiveDummySensor(CableAnt.DeviceType.fromDataType(dataType));
        }
        if (sensor != null) {
            sensor.setLastValue(sensorType, dataType, value);
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    protected void onSensorConnected() {
        Log.d(TAG, "Connected!");
        this.mListCb.onAntBridgeConnected(this);
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub stub = this.mScannedList.get(fakeMac);
                this.mListCb.onSensorFound(stub);
            }
        }
        clearReqs();
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onSensorUnavailable() {
        Log.d(TAG, "Reconnecting...");
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub stub = this.mScannedList.get(fakeMac);
                stub.setConnectionStatus(stub.isInUse() ? CableAnt.ChannelStatus.SCANNING : CableAnt.ChannelStatus.UNKNOWN);
            }
        }
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    protected void onSensorDisconnected() {
        Log.d(TAG, "Disconnected!");
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub stub = this.mScannedList.get(fakeMac);
                this.mListCb.onSensorLost(stub);
            }
        }
        super.onSensorDisconnected();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean isGattCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().equals(GATT_CHARACTERISTIC_CABLE_RX)) {
            return true;
        }
        return super.isGattCharacteristic(characteristic);
    }

    void startScan() {
        this.mIsScanEnabled = true;
        startScan(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startScan(boolean andQuerySlots) {
        if (andQuerySlots) {
            queryActiveSettings();
        }
        this.mIsScanActive = true;
        writeReq(new CableControl.AntDiscovery(3));
    }

    boolean queryActiveSettings() {
        long now = System.currentTimeMillis();
        if (now - this.mLastScanTime < SCAN_REPEAT_DELAY) {
            return true;
        }
        this.mLastScanTime = now;
        if (this.mLastReq != 0) {
            this.mLastReq = now;
        }
        writeReq(new CableControl.RequestActiveSet());
        return false;
    }

    boolean queryActiveSlots() {
        boolean again = false;
        long now = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            CableSensorStub stub = findDummySensor(this.mActiveSet, i);
            if (stub != null && ((!isLegacyCable() || stub.isConnecting()) && stub.isReconnecting())) {
                if (now - this.mSlots[i].mLastScan < SCAN_REPEAT_DELAY) {
                    again = true;
                } else {
                    writeReq(new CableControl.RequestSettings(i, this.mActiveSet));
                    this.mSlots[i].mLastScan = now;
                }
            }
        }
        return again;
    }

    void reset() {
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub stub = this.mScannedList.get(fakeMac);
                if (stub.isInUse()) {
                    stub.disconnect();
                }
                this.mListCb.onSensorLost(stub);
            }
            this.mScannedList.clear();
        }
        this.mHardReset = true;
        factoryReset();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public void disconnect() {
        super.disconnect();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
    public void activate() {
        super.activate();
        Log.d(TAG, "Version: " + getHardwareVersion() + " / " + getVersion());
        BluetoothGattService service = this.mGatt.getService(GATT_SERVICE_CABLE);
        if (service != null) {
            this.mCableControlPoint = service.getCharacteristic(GATT_CHARACTERISTIC_CABLE_CONTROL);
        }
        if (!this.mHardReset) {
            Log.d(TAG, "Ensuring CABLE is ready for use");
            this.mHardReset = true;
            factoryReset();
        }
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub stub = this.mScannedList.get(fakeMac);
                if (SensorList.isDeviceSaved(stub) && SensorsConnector.isSupportedType(stub.getType())) {
                    this.mRequiredSet = stub.checkSet(this.mRequiredSet);
                    Log.d(TAG, "Saved sensor: " + stub.getName() + " uses set: " + this.mRequiredSet);
                }
            }
            this.mActiveSet = this.mRequiredSet;
        }
        this.mLastRx = System.currentTimeMillis();
        this.mLastReq = this.mLastRx;
        writeReq(new CableControl.SelectActiveSet(this.mActiveSet));
        writeReq(new CableControl.RequestActiveSet());
        synchronized (this.mScannedList) {
            if (!this.mScannedList.isEmpty()) {
                for (String fakeMac2 : this.mScannedList.keySet()) {
                    this.mListCb.onSensorFound(this.mScannedList.get(fakeMac2));
                }
                return;
            }
            this.mIsScanEnabled = SensorsConnector.isDiscovering();
            if (this.mIsScanEnabled) {
                startScan(true);
            }
        }
    }

    public boolean isActive() {
        if (inactivityTimeout()) {
            Log.d(TAG, "Warning: CABLE may be inactive");
        }
        long now = System.currentTimeMillis();
        return now - this.mLastRx < 5000;
    }

    void checkForInactive() {
        if (inactivityTimeout()) {
            Log.d(TAG, "Warning: CABLE may be inactive, forcing reset");
            onSensorTimeout();
        }
    }

    private boolean inactivityTimeout() {
        if (this.mLastReq == 0) {
            return false;
        }
        long now = System.currentTimeMillis();
        Log.d(TAG, "Check CABLE activity: time since last request: " + (now - this.mLastReq) + " ms");
        return now - this.mLastReq > INACTIVE_RESET_TIMEOUT;
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onSensorTimeout() {
        this.mLastReq = 0L;
        super.onSensorTimeout();
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean doWriteReq(BTLESensorConfig.ConfigControlRequest req) {
        if ((req instanceof CableControl.Request) && this.mCableControlPoint != null && this.mState == Sensor.ConnectionState.CONNECTED) {
            if (inactivityTimeout()) {
                Log.d(TAG, "Warning: CABLE may be inactive, forcing reset");
                onSensorTimeout();
                return false;
            }
            try {
                Log.d(TAG, "TX: " + req.debugName());
                Log.v(TAG, ">> " + BTLESensor.bytesToString(req.toValue()));
                this.mCableControlPoint.setValue(req.toValue());
                if (this.mGatt != null) {
                    return this.mGatt.writeCharacteristic(this.mCableControlPoint);
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return super.doWriteReq(req);
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected boolean onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_CABLE_RX)) {
            return super.onCharacteristicChanged(characteristic);
        }
        CableControl.Rx info = CableControl.Rx.fromValue(characteristic.getValue());
        if (info != null) {
            handleEvent(info.mOpcode, info.mParams);
        }
        return true;
    }

    @Override // com.kopin.solos.sensors.btle.BTLESensor
    protected void onServiceDiscoveryComplete() {
        activate();
    }

    private void factoryReset() {
    }

    public void startScan(int channel) {
        if (!this.mSlots[channel].mStatus.isScanning() && this.mSlots[channel].scanTimeout()) {
            this.mSlots[channel].mLastScan = System.currentTimeMillis();
            writeReq(new CableControl.RequestScan(true, channel));
        }
    }

    public void stopScan() {
        this.mIsScanEnabled = false;
        queryActiveSettings();
    }

    public void stopScan(int channel) {
        writeReq(new CableControl.RequestScan(false, channel));
    }

    private void handleEvent(CableControl.Opcode event, CableControl.Params data) {
        CableSensorStub sensor;
        Log.d(TAG, "Event " + event);
        boolean shouldUpdateStatus = !isActive();
        this.mLastRx = System.currentTimeMillis();
        if (shouldUpdateStatus) {
            onSensorChanged();
        }
        switch (event) {
            case SELECT_ACTIVE_SET:
                this.mLastReq = 0L;
                CableControl.ParamsActiveSet setData = (CableControl.ParamsActiveSet) data;
                if (setData.mSet == this.mRequiredSet) {
                    this.mActiveSet = setData.mSet;
                    Log.d(TAG, "Active set: " + this.mActiveSet);
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (CableAnt.DeviceType.defaultForChannel(i, j) != CableAnt.DeviceType.NONE && i == this.mActiveSet) {
                                this.mSlots[i].reset();
                                writeReq(new CableControl.RequestSettings(j, i));
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Active set: " + setData.mSet + " doesn't match required for selected sensors, switching now: " + this.mRequiredSet);
                    writeReq(new CableControl.SelectActiveSet(this.mRequiredSet));
                }
                break;
            case REQ_ACTIVE_DEV_SETTINGS:
                this.mLastReq = 0L;
                CableControl.ParamsDevSettings devData = (CableControl.ParamsDevSettings) data;
                Log.d(TAG, "  Req dev settings for channel " + devData.mChannel + ", set " + devData.mSet + " Channel status: " + devData.mRxStatus + " dev ID: " + devData.mDevId + " type: " + devData.mDevType + " chan: " + ((int) devData.mAntChan) + " Slot enabled: " + devData.isSlotEnabled() + " BLE Active: " + devData.isBleEnabled());
                if (devData.mDevType != CableAnt.DeviceType.NONE && devData.mChannel != devData.mDevType.defaultChannel(devData.mSet)) {
                    Log.d(TAG, "Wrong channel for device type: " + devData.mDevType + " - unassigning and ignoring status");
                    unassignChannel(devData.mSet, devData.mChannel);
                    break;
                } else {
                    if (devData.mDevId > 0 && devData.mDevType != CableAnt.DeviceType.NONE) {
                        sensor = findDummySensor(devData.mDevType, devData.mDevId);
                        if (sensor == null) {
                            sensor = addSensorStub(devData);
                        }
                    } else {
                        sensor = findDummySensor(devData.mSet, devData.mChannel);
                        if (sensor != null && (sensor.isInUse() || SensorList.isDeviceSaved(sensor))) {
                            Log.d(TAG, "Reattempting Assign to channel for sensor: " + sensor);
                            sensor.assign(devData.mSet, devData.mChannel);
                        }
                    }
                    if (devData.isSlotEnabled() && sensor != null && devData.mSet == sensor.mSet && devData.mChannel == sensor.mSlot) {
                        CableAnt.ChannelStatus rxStat = devData.mRxStatus;
                        boolean refresh = sensor.setConnectionStatus(rxStat);
                        if (refresh) {
                            refreshStatus(true);
                        }
                        break;
                    }
                }
                break;
            case ANT_DISCOVERY_DATA:
                CableControl.ParamsAntDiscovery scanData = (CableControl.ParamsAntDiscovery) data;
                Log.d(TAG, "  ANT+ device found: " + scanData.mDevId + ", type: " + scanData.mDevType + " Trans: " + ((int) scanData.mTrans) + " RSSI: " + ((int) scanData.mRssi));
                addSensorStub(scanData);
                break;
            case END_ANT_DISCOVERY:
                this.mIsScanActive = false;
                if (this.mIsScanEnabled) {
                    BTLEScanner.runOnScannerThread(new Runnable() { // from class: com.kopin.solos.sensors.btle.CableSensor.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (CableSensor.this.mIsScanEnabled && !CableSensor.this.mIsScanActive) {
                                CableSensor.this.startScan(false);
                            }
                        }
                    }, 12000L);
                }
                break;
            case RESP_ANT_CALIBRATION:
                Log.d(TAG, "ANT+ Calibration Response");
                CableControl.ParamsAntCalibration calibData = (CableControl.ParamsAntCalibration) data;
                CableSensorStub mPower = findActiveDummySensor(CableAnt.DeviceType.BIKE_PWR);
                if (mPower != null) {
                    mPower.onConfigMessage(calibData.mSuccess, calibData.toString());
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshStatus(boolean initial) {
        synchronized (this.mRefreshStatus) {
            if (!this.mRefreshQueued) {
                this.mRefreshQueued = true;
                BTLEScanner.runOnScannerThread(this.mRefreshStatus, initial ? REFRESH_STATUS_INITIAL : 5000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assignChannel(int set, int slot, int devId, CableAnt.DeviceType type, int trans) {
        Log.d(TAG, "Assigning slot " + slot + " to device " + devId + " for type " + type);
        if (this.mIsScanActive) {
            Log.d(TAG, "  Discovery still active, ending before assigning");
            this.mIsScanActive = false;
            writeReq(new CableControl.EndDiscovery());
        }
        if (devId > 0) {
            if (set != this.mRequiredSet) {
                Log.d(TAG, "  Switching to set " + set);
                this.mRequiredSet = set;
                writeReq(new CableControl.SelectActiveSet(set));
            } else if (set != this.mActiveSet) {
                Log.d(TAG, "  Re-Checking current set " + set);
                writeReq(new CableControl.RequestActiveSet());
            }
        }
        if (this.mLastReq == 0) {
            this.mLastReq = System.currentTimeMillis();
        }
        writeReq(new CableControl.SetDevSettings(set, slot, devId, type, trans));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unassignChannel(int set, int slot) {
        Log.d(TAG, "UnAssigning slot " + slot);
        writeReq(new CableControl.SetDevSettings(set, slot, 0, CableAnt.DeviceType.NONE, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeChannel(int slot) {
        Log.d(TAG, "Closing slot " + slot);
        writeReq(new CableControl.CloseChannel(slot));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emptySlot(int set, int slot, CableSensorStub forSensor) {
        CableSensorStub oldSensor;
        do {
            oldSensor = findDummySensor(set, slot);
            if (oldSensor == forSensor) {
                oldSensor = null;
            }
            if (oldSensor != null) {
                SensorList.forgetDevice(oldSensor);
                oldSensor.disconnect();
            }
        } while (oldSensor != null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSetAndSlot(int set, int slot, CableAnt.DeviceType type) {
        CableSensorStub oldSensor;
        boolean shouldSwitch = this.mActiveSet != set;
        if (shouldSwitch) {
            CableSensorStub oldSensor2 = findActiveDummySensor(CableAnt.DeviceType.HR);
            if (oldSensor2 != null) {
                oldSensor2.swapSet(set);
            }
            CableSensorStub oldSensor3 = findActiveDummySensor(CableAnt.DeviceType.BIKE_PWR);
            if (oldSensor3 != null) {
                oldSensor3.swapSet(set);
            }
            if (type == CableAnt.DeviceType.BIKE_SC) {
                CableSensorStub oldSensor4 = findActiveDummySensor(CableAnt.DeviceType.BIKE_CAD);
                if (oldSensor4 != null) {
                    SensorList.forgetDevice(oldSensor4);
                    oldSensor4.disconnect();
                }
                CableSensorStub oldSensor5 = findActiveDummySensor(CableAnt.DeviceType.BIKE_SPD);
                if (oldSensor5 != null) {
                    SensorList.forgetDevice(oldSensor5);
                    oldSensor5.disconnect();
                }
            } else {
                CableSensorStub oldSensor6 = findActiveDummySensor(CableAnt.DeviceType.BIKE_SC);
                if (oldSensor6 != null) {
                    SensorList.forgetDevice(oldSensor6);
                    oldSensor6.disconnect();
                }
            }
            this.mRequiredSet = set;
            writeReq(new CableControl.SelectActiveSet(set));
            return;
        }
        if (type == CableAnt.DeviceType.BIKE_SC) {
            CableSensorStub oldSensor7 = findActiveDummySensor(CableAnt.DeviceType.BIKE_CAD);
            if (oldSensor7 != null) {
                oldSensor7.unassign();
            }
            CableSensorStub oldSensor8 = findActiveDummySensor(CableAnt.DeviceType.BIKE_SPD);
            if (oldSensor8 == null) {
                return;
            }
            oldSensor8.unassign();
            return;
        }
        if ((type != CableAnt.DeviceType.BIKE_SPD && type != CableAnt.DeviceType.BIKE_CAD) || (oldSensor = findActiveDummySensor(CableAnt.DeviceType.BIKE_SC)) == null) {
            return;
        }
        oldSensor.unassign();
    }

    private CableSensorStub findDummySensor(CableAnt.DeviceType type, int devId) {
        CableSensorStub cableSensorStub;
        synchronized (this.mScannedList) {
            String fakeMac = devId + ":" + type.mappedSensorType();
            cableSensorStub = this.mScannedList.get(fakeMac);
        }
        return cableSensorStub;
    }

    private CableSensorStub findDummySensor(int set, int slot) {
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub sensor = this.mScannedList.get(fakeMac);
                if (sensor.mSet == set && sensor.mSlot == slot) {
                    return sensor;
                }
            }
            return null;
        }
    }

    private CableSensorStub findActiveDummySensor(CableAnt.DeviceType type) {
        synchronized (this.mScannedList) {
            for (String fakeMac : this.mScannedList.keySet()) {
                CableSensorStub sensor = this.mScannedList.get(fakeMac);
                if (sensor.mAntType == type && sensor.isInUse()) {
                    return sensor;
                }
            }
            return null;
        }
    }

    private void addSensorStub(CableControl.ParamsAntDiscovery scanData) {
        this.mHardReset = true;
        synchronized (this.mScannedList) {
            String fakeMac = scanData.mDevId + ":" + scanData.mDevType.mappedSensorType();
            if (!SensorList.hasSensor(fakeMac)) {
                CableSensorStub sensor = this.mScannedList.get(fakeMac);
                if (sensor == null) {
                    Log.d(TAG, " Adding new device: " + fakeMac);
                    sensor = new CableSensorStub(scanData, this.mListCb);
                    this.mScannedList.put(fakeMac, sensor);
                }
                Log.d(TAG, "++ Sensor Found ++ " + sensor.getName());
                this.mListCb.onSensorFound(sensor);
            }
        }
    }

    private CableSensorStub addSensorStub(CableControl.ParamsDevSettings devParams) {
        CableSensorStub cableSensorStub;
        this.mHardReset = true;
        synchronized (this.mScannedList) {
            String fakeMac = devParams.mDevId + ":" + devParams.mDevType.mappedSensorType();
            if (!SensorList.hasSensor(fakeMac)) {
                CableSensorStub sensor = this.mScannedList.get(fakeMac);
                if (sensor == null) {
                    Log.d(TAG, " Adding new device: " + fakeMac);
                    sensor = new CableSensorStub(devParams, this.mListCb);
                    this.mScannedList.put(fakeMac, sensor);
                }
                Log.d(TAG, "++ Sensor Found ++ " + sensor.getName());
                this.mListCb.onSensorFound(sensor);
            }
            cableSensorStub = this.mScannedList.get(fakeMac);
        }
        return cableSensorStub;
    }

    private CableSensorStub addSensorStub(int devId, Sensor.SensorType type, int trans) {
        CableSensorStub cableSensorStub;
        this.mHardReset = true;
        synchronized (this.mScannedList) {
            String fakeMac = devId + ":" + type;
            if (!SensorList.hasSensor(fakeMac)) {
                CableSensorStub sensor = this.mScannedList.get(fakeMac);
                if (sensor == null) {
                    Log.d(TAG, " Adding new device: " + fakeMac);
                    sensor = new CableSensorStub(devId, type, trans, this.mListCb);
                    this.mScannedList.put(fakeMac, sensor);
                }
                Log.d(TAG, "++ Sensor Found ++ " + sensor.getName());
                this.mListCb.onSensorFound(sensor);
            }
            cableSensorStub = this.mScannedList.get(fakeMac);
        }
        return cableSensorStub;
    }

    private CableSensorStub removeSensorStub(int devId, CableAnt.DeviceType type) {
        CableSensorStub sensor;
        synchronized (this.mScannedList) {
            String fakeMac = devId + ":" + type.mappedSensorType();
            sensor = this.mScannedList.remove(fakeMac);
            if (sensor != null) {
                Log.d(TAG, "-- Sensor Lost -- " + sensor.getName());
                this.mListCb.onSensorLost(sensor);
            }
        }
        return sensor;
    }

    static boolean isKnownCable(BluetoothDevice dev) {
        for (CableSensor c : CABLE_CACHE) {
            if (c.getId().contentEquals(dev.getAddress())) {
                return true;
            }
        }
        return false;
    }

    static CableSensor getCachedCable(Context context, BluetoothDevice dev, String name, CableScanner cableScanner, SensorsConnector.ScannerListener obs) {
        if (cableScanner != null) {
            cableScanner.addToCache(dev, name);
        }
        for (CableSensor c : CABLE_CACHE) {
            if (c.getId().contentEquals(dev.getAddress())) {
                return c;
            }
        }
        CableSensor c2 = new CableSensor(context, dev, name, obs);
        CABLE_CACHE.add(c2);
        return c2;
    }

    public static Sensor deserialize(String params, Context context, SensorsConnector.ScannerListener obs) {
        if (!params.startsWith(TAG)) {
            return null;
        }
        String[] values = params.split(",");
        try {
            BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(values[1]);
            CableSensor cable = getCachedCable(context, dev, values[2], null, obs);
            Sensor.SensorType type = Sensor.SensorType.values()[Integer.parseInt(values[3])];
            int trans = values.length > 6 ? Integer.parseInt(values[6]) : 5;
            return cable.addSensorStub(Integer.parseInt(values[5]), type, trans);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, int value) {
        CableSensorStub sensor = findActiveDummySensor(CableAnt.DeviceType.fromSensorType(sensorType));
        if (sensor == null) {
            sensor = findActiveDummySensor(CableAnt.DeviceType.fromDataType(dataType));
        }
        if (sensor != null && sensor.hasType(dataType)) {
            sensor.newData(dataType, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.sensors.Sensor
    public void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, double value) {
        CableSensorStub sensor = findActiveDummySensor(CableAnt.DeviceType.fromSensorType(sensorType));
        if (sensor == null) {
            sensor = findActiveDummySensor(CableAnt.DeviceType.fromDataType(dataType));
        }
        if (sensor != null) {
            sensor.newData(dataType, value);
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onNewData(Sensor.SensorType sensorType, Sensor.DataType dataType, double value, long interval) {
        CableSensorStub sensor = findActiveDummySensor(CableAnt.DeviceType.fromSensorType(sensorType));
        if (sensor == null) {
            sensor = findActiveDummySensor(CableAnt.DeviceType.fromDataType(dataType));
        }
        if (sensor != null) {
            sensor.newData(dataType, value, interval);
        }
    }

    public class CableSensorStub extends BTLESensor {
        private final int mAntDevId;
        private final CableAnt.DeviceType mAntType;
        private long mLastUpdate;
        private int mSet;
        private int mSlot;
        private CableAnt.ChannelStatus mStatus;
        private int mTrans;
        private final Sensor.SensorType mType;

        public CableSensorStub(CableControl.ParamsAntDiscovery scanData, Sensor.SensorObserver obs) {
            super(CableSensor.this, CableSensor.stubName(scanData.mDevId), scanData.mDevType.mappedSensorType(), obs);
            this.mStatus = CableAnt.ChannelStatus.UNKNOWN;
            this.mSet = -1;
            this.mSlot = -1;
            this.mAntDevId = scanData.mDevId;
            this.mType = scanData.mDevType.mappedSensorType();
            this.mAntType = scanData.mDevType;
            this.mTrans = scanData.mTrans;
        }

        public CableSensorStub(CableControl.ParamsDevSettings scanData, Sensor.SensorObserver obs) {
            super(CableSensor.this, CableSensor.stubName(scanData.mDevId), scanData.mDevType.mappedSensorType(), obs);
            this.mStatus = CableAnt.ChannelStatus.UNKNOWN;
            this.mSet = -1;
            this.mSlot = -1;
            this.mAntDevId = scanData.mDevId;
            this.mType = scanData.mDevType.mappedSensorType();
            this.mAntType = scanData.mDevType;
            this.mTrans = scanData.mTxStatus;
        }

        public CableSensorStub(int devId, Sensor.SensorType type, int trans, Sensor.SensorObserver obs) {
            super(CableSensor.this, CableSensor.stubName(devId), type, obs);
            this.mStatus = CableAnt.ChannelStatus.UNKNOWN;
            this.mSet = -1;
            this.mSlot = -1;
            this.mAntDevId = devId;
            this.mType = type;
            this.mAntType = CableAnt.DeviceType.fromSensorType(type);
            this.mTrans = trans;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public String serialize() {
            return "CABLE," + CableSensor.this.getId() + "," + CableSensor.this.getName() + "," + getType().ordinal() + "," + this.mSlot + "," + this.mAntDevId + "," + this.mTrans;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public boolean equals(Sensor.SensorType type) {
            return this.mType.equals(type);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public String getName() {
            return CableSensor.stubName(this.mAntDevId);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public String getId() {
            return this.mAntDevId + ":" + this.mType;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public Sensor.SensorType getType() {
            return this.mType;
        }

        private void use(Sensor.SensorType type) {
            onSensorAvailable();
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public void connect() {
            if (CableSensor.this.isConnected()) {
                if (CableSensor.this.mActiveSet == -1) {
                    Log.d(CableSensor.TAG, "Not connecting to " + getName() + " as CABLE isn't configured for use yet");
                    return;
                }
                if (!isInUse() && !isReconnecting()) {
                    int newSet = this.mAntType.checkSet(CableSensor.this.mActiveSet);
                    int newSlot = this.mAntType.defaultChannel(newSet);
                    if (newSlot != -1) {
                        CableSensor.this.emptySlot(newSet, newSlot, this);
                        CableSensor.this.checkSetAndSlot(newSet, newSlot, this.mAntType);
                        assign(newSet, newSlot);
                        use(this.mType);
                        return;
                    }
                    return;
                }
                CableSensor.this.writeReq(new CableControl.RequestSettings(this.mSlot, this.mSet));
                return;
            }
            Log.d(CableSensor.TAG, "Not retrying connection to " + getName() + " as CABLE isn't active");
            CableSensor.this.mListCb.checkAntBridgeConnected(CableSensor.this);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public void disconnect() {
            if (this.mSlot != -1 && this.mSet != -1) {
                Log.d(CableSensor.TAG, "Disconnect devId: " + this.mAntDevId + " - " + this.mAntType + " from slot " + this.mSlot + ", set " + this.mSet);
                super.onSensorUnavailable();
                unassign();
            }
        }

        int checkSet(int set) {
            return this.mAntType.checkSet(set);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void swapSet(int newSet) {
            this.mSet = newSet;
            CableSensor.this.assignChannel(this.mSet, this.mSlot, this.mAntDevId, this.mAntType, this.mTrans);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void assign(int set, int slot) {
            this.mSet = set;
            this.mSlot = slot;
            assign();
        }

        private void assign() {
            CableSensor.this.assignChannel(this.mSet, this.mSlot, this.mAntDevId, this.mAntType, this.mTrans);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unassign() {
            setConnectionStatus(CableAnt.ChannelStatus.UNASSIGNED);
            if (this.mSlot != -1) {
                if (CableSensor.this.isLegacyCable()) {
                    CableSensor.this.unassignChannel(this.mSet, this.mSlot);
                } else {
                    CableSensor.this.closeChannel(this.mSlot);
                }
                this.mSlot = -1;
            }
        }

        boolean setConnectionStatus(CableAnt.ChannelStatus status) {
            if (this.mSlot == -1) {
                return false;
            }
            boolean newStatus = status != this.mStatus;
            boolean refresh = status.isScanning() || status.isAssigned();
            this.mStatus = status;
            if (newStatus) {
                if (this.mStatus.isScanning()) {
                    onSensorUnavailable();
                    refresh = false;
                } else if (this.mStatus.isAssigned()) {
                    if (CableSensor.this.isLegacyCable()) {
                        CableSensor.this.writeReq(new CableControl.ReopenChannels());
                    } else {
                        assign();
                    }
                    refresh = false;
                } else if (this.mStatus.isActive()) {
                    onSensorConnected();
                    refresh = false;
                } else {
                    if (!isInUse()) {
                        refresh = false;
                    }
                    onSensorUnavailable();
                    onSensorDisconnected();
                }
            }
            if (isInUse() && status == CableAnt.ChannelStatus.UNASSIGNED) {
                CableSensor.this.assignChannel(this.mSet, this.mSlot, this.mAntDevId, this.mAntType, this.mTrans);
                refresh = true;
            }
            Log.d(CableSensor.TAG, "Sensor " + getId() + " new state: " + getConnectionState() + " (refresh: " + refresh + ")");
            return refresh;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public Sensor.ConnectionState getConnectionState() {
            if (this.mStatus != null) {
                switch (this.mStatus) {
                    case ASSIGNED:
                    case SCANNING:
                        if (!isInUse()) {
                        }
                        break;
                }
                return Sensor.ConnectionState.DISCONNECTED;
            }
            return Sensor.ConnectionState.DISCONNECTED;
        }

        boolean isConnecting() {
            return this.mStatus == CableAnt.ChannelStatus.ASSIGNED;
        }

        boolean isReconnecting() {
            return this.mSlot != -1 && (this.mStatus.isAssigned() || this.mStatus.isScanning());
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public void activate() {
            use(this.mType);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public void deactivate() {
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        protected void onSensorConnected() {
            switch (this.mType) {
                case POWER:
                    gainDataType(Sensor.DataType.POWER);
                    break;
                case SPEED_AND_CADENCE:
                    gainDataType(Sensor.DataType.SPEED);
                    gainDataType(Sensor.DataType.CADENCE);
                    break;
                case RUNNING_SPEED_CADENCE:
                    gainDataType(Sensor.DataType.PACE);
                    gainDataType(Sensor.DataType.STEP);
                    break;
                case HEARTRATE:
                case SPEED:
                case CADENCE:
                    gainDataType(this.mType.defaultDataType());
                    break;
            }
            super.onSensorConnected();
        }

        void newData(Sensor.DataType dataType, int value) {
            if (!hasType(dataType)) {
                gainDataType(dataType);
                onSensorAvailable();
            }
            this.mLastUpdate = System.currentTimeMillis();
            onNewData(this.mType, dataType, value);
        }

        void newData(Sensor.DataType dataType, double value) {
            if (!hasType(dataType)) {
                gainDataType(dataType);
                onSensorAvailable();
            }
            this.mLastUpdate = System.currentTimeMillis();
            onNewData(this.mType, dataType, value);
        }

        void newData(Sensor.DataType dataType, double value, long interval) {
            if (!hasType(dataType)) {
                gainDataType(dataType);
                onSensorAvailable();
            }
            this.mLastUpdate = System.currentTimeMillis();
            onNewData(this.mType, dataType, value, interval);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public long getLastUpdateTime(Sensor.DataType dataType) {
            if (equals(dataType)) {
                return this.mLastUpdate;
            }
            return 0L;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public boolean isBatteryOk() {
            return true;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor, com.kopin.solos.sensors.Sensor
        public boolean hasConfig() {
            return getType() == Sensor.SensorType.POWER && isInUse();
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor
        public SensorData getLastConfig() {
            return CableSensor.this.getLastConfig();
        }

        public void requestAntCalibration(boolean manual, boolean onOrOff) {
            if (isInUse() && getType() == Sensor.SensorType.POWER) {
                CableSensor.this.writeReq(new CableControl.RequestAntCalibration(this.mSlot, manual, onOrOff));
            }
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensor
        protected void writeReq(BTLESensorConfig.ConfigControlRequest req) {
            CableSensor.this.writeReq(req);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String stubName(int devId) {
        return String.format("ANT+ Device: %d", Integer.valueOf(devId));
    }
}
