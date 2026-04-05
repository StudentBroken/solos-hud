package com.kopin.solos.sensors.btle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.btle.CableScanner;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes28.dex */
@SuppressLint({"NewApi"})
public class BTLEScanner {
    private static final boolean DEBUG_CONNECT_TO_CLOSEST_CABLE = false;
    private static final boolean DEEP_SCAN_FOR_SERVICES = false;
    private static Handler mDiscoveryHandler;
    private boolean mActive;
    private CableSensor mCable;
    private int mCableId;
    private CableScanner mCableScanner;
    private SensorsConnector.ScannerListener mCb;
    private Context mContext;
    private HashSet<BluetoothDevice> mDeepScanList = new HashSet<>();
    private final Runnable mInitialScanRunnable = new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLEScanner.1
        @Override // java.lang.Runnable
        public void run() {
            if (!BTLEScanner.this.mActive) {
                Log.d("Scanner", "InitialScan checking");
                if (BTLEScanner.this.isInitialScanRequired()) {
                    Log.d("Scanner", "  Starting");
                    BTLEScanner.this.start();
                    BTLEScanner.mDiscoveryHandler.postDelayed(BTLEScanner.this.mInitialScanRunnable, 60000L);
                    return;
                }
                return;
            }
            Log.d("Scanner", "InitialScan active");
            if (BTLEScanner.this.isInitialScanRequired()) {
                Log.d("Scanner", "  Still required");
                BTLEScanner.mDiscoveryHandler.postDelayed(BTLEScanner.this.mInitialScanRunnable, 60000L);
            } else {
                Log.d("Scanner", "  Ending");
                BTLEScanner.this.stop();
            }
        }
    };
    private final ScanCallback mScanResults = new ScanCallback() { // from class: com.kopin.solos.sensors.btle.BTLEScanner.2
        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int callbackType, ScanResult result) {
            final BluetoothDevice dev = result.getDevice();
            ScanRecord rec = result.getScanRecord();
            String name = rec.getDeviceName();
            if (name == null) {
                name = dev.getName();
            }
            if (name == null) {
                name = dev.getAddress();
            }
            if (name.indexOf(0) > 0) {
                name = name.substring(0, name.indexOf(0));
            }
            if (!SensorList.hasSensor(dev.getAddress()) && !BTLEScanner.this.mDeepScanList.contains(dev)) {
                Log.d("Scanner", "Got new device: " + name);
                byte[] mfid = result.getScanRecord().getManufacturerSpecificData(816);
                if (mfid != null && mfid.length == 5) {
                    Log.d("Scanner", "Has CABLE MFID data, with CABLE ID");
                    int newid = (((mfid[2] & 255) << 8) | (mfid[1] & 255)) & SupportMenu.USER_MASK;
                    name = String.format("cable#%05d", Integer.valueOf(newid));
                    Log.d("Scanner", "  reconstructed name: " + name);
                }
                BTLESensor sensor = null;
                if (mfid == null) {
                    sensor = BTLEScanner.this.createSensor(dev, dev.getName(), BTLEScanner.this.typeFromGattServiceUuids(rec.getServiceUuids()), true);
                }
                if (sensor != null) {
                    BTLEScanner.this.mCb.onSensorFound(sensor);
                    return;
                }
                if (mfid != null) {
                    if (BTLEScanner.this.mCable == null && CableSensor.isKnownCable(dev)) {
                        BTLESensor sensor2 = CableSensor.getCachedCable(BTLEScanner.this.mContext, dev, name, BTLEScanner.this.mCableScanner, BTLEScanner.this.mCb);
                        if (BTLEScanner.this.isTargetCable(name)) {
                            Log.d("Scanner", "Connecting to a previous CABLE");
                            BTLEScanner.this.foundCableDevice((CableSensor) sensor2);
                        }
                    }
                    if (BTLEScanner.this.mCable == null) {
                        if (name == null || !name.contains("#")) {
                            Log.d("Scanner", "Checking cache for ID for CABLE " + dev.getAddress());
                            CableScanner.CableDeviceStub cableStub = BTLEScanner.this.mCableScanner.getCachedCable(dev);
                            if (cableStub != null) {
                                Log.d("Scanner", "Got cached ID");
                                name = cableStub.name();
                            }
                        }
                        if (CableScanner.isCableName(name)) {
                            Log.d("Scanner", " device is CABLE, with known ID");
                            BTLESensor sensor3 = CableSensor.getCachedCable(BTLEScanner.this.mContext, dev, name, BTLEScanner.this.mCableScanner, BTLEScanner.this.mCb);
                            if (BTLEScanner.this.isTargetCable(name)) {
                                Log.d("Scanner", "Connecting to a previous CABLE");
                                BTLEScanner.this.foundCableDevice((CableSensor) sensor3);
                                return;
                            }
                            return;
                        }
                        Log.d("Scanner", " device is CABLE, connecting to get CABLE ID");
                        BTLEScanner.this.mDeepScanList.add(dev);
                        BTLEScanner.this.mCableScanner.checkDevice(dev, name, new CableScanner.DiscoveryListener() { // from class: com.kopin.solos.sensors.btle.BTLEScanner.2.1
                            @Override // com.kopin.solos.sensors.btle.CableScanner.DiscoveryListener
                            public void onDeviceDiscovered(CableScanner.CableDeviceStub device) {
                                if (BTLEScanner.this.isTargetCable(device.name())) {
                                    CableSensor sensor4 = CableSensor.getCachedCable(BTLEScanner.this.mContext, dev, device.name(), BTLEScanner.this.mCableScanner, BTLEScanner.this.mCb);
                                    Log.d("Scanner", "Connecting to a new CABLE");
                                    BTLEScanner.this.foundCableDevice(sensor4);
                                }
                            }
                        });
                        return;
                    }
                    return;
                }
                Log.d("Scanner", " unsure of sensor type, ignoring due to policy.");
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int errorCode) {
        }
    };
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.kopin.solos.sensors.btle.BTLEScanner.3
        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String name = "NO NAME";
            if (characteristic != null && characteristic.getValue() != null) {
                name = characteristic.getStringValue(0);
                if (name.indexOf(0) > 0) {
                    name = name.substring(0, name.indexOf(0));
                }
            }
            gatt.disconnect();
            if (CableScanner.isCableName(name)) {
                Log.d("Scanner", "Got CABLE Id, adding to cache");
                BTLEScanner.this.mCableScanner.addToCache(gatt.getDevice(), name);
            }
            BTLESensor sensor = BTLEScanner.this.createSensor(gatt.getDevice(), name, BTLEScanner.this.typeFromGattServices(gatt.getServices()), true);
            if (sensor != null) {
                BTLEScanner.this.mCb.onSensorFound(sensor);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothGattCharacteristic name;
            if (status == 0) {
                BluetoothGattService info = gatt.getService(BTLESensor.GATT_SERVICE_GENERIC);
                if (info == null || (name = info.getCharacteristic(BTLESensor.GATT_CHARACTERISTIC_DEVICE_NAME)) == null) {
                    BTLESensor sensor = BTLEScanner.this.createSensor(gatt.getDevice(), gatt.getDevice().getName(), BTLEScanner.this.typeFromGattServices(gatt.getServices()), true);
                    if (sensor != null) {
                        BTLEScanner.this.mCb.onSensorFound(sensor);
                    }
                } else {
                    if (gatt.readCharacteristic(name)) {
                    }
                    return;
                }
            }
            gatt.disconnect();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == 0) {
                if (newState == 2) {
                    Log.d("Scanner", "Connected to sensor: " + gatt.getDevice().getAddress() + " looking for services...");
                    if (!gatt.discoverServices()) {
                        Log.d("Scanner", "Unable to discover services on device " + gatt.getDevice().getAddress());
                        return;
                    }
                    return;
                }
                Log.d("Scanner", "Not connected to sensor: " + gatt.getDevice().getAddress() + " status: " + status + " connect: " + newState);
                return;
            }
            Log.d("Scanner", "Couldn't connect to sensor: " + gatt.getDevice().getAddress() + " status: " + status + " connect: " + newState);
        }
    };
    private BluetoothLeScanner mScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

    public BTLEScanner(Context context, SensorsConnector.ScannerListener cb) {
        this.mContext = context;
        this.mCb = cb;
        this.mCableScanner = new CableScanner(context);
        mDiscoveryHandler = new Handler();
        mDiscoveryHandler.postDelayed(this.mInitialScanRunnable, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInitialScanRequired() {
        if (this.mCable != null) {
            Log.d("Scanner", "InitialScan: Cable connected: " + this.mCable.isConnected());
            return !this.mCable.isConnected();
        }
        Log.d("Scanner", "InitialScan: Saved devices: " + SensorList.hasSavedDevices() + ", Connected devices: " + SensorList.hasConnectedDevices());
        return SensorList.hasSavedDevices() && !SensorList.hasConnectedDevices();
    }

    public void start() {
        if (this.mScanner == null) {
            this.mScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        }
        if (this.mScanner != null && !this.mActive) {
            this.mDeepScanList.clear();
            this.mScanner.startScan(this.mScanResults);
            this.mActive = true;
        }
        if (this.mCable != null) {
            this.mCable.startScan();
        }
    }

    public void stop() {
        if (this.mScanner != null && this.mActive) {
            this.mScanner.stopScan(this.mScanResults);
            this.mActive = false;
            mDiscoveryHandler.removeCallbacks(this.mInitialScanRunnable);
        }
        if (this.mCable != null) {
            this.mCable.stopScan();
        }
        this.mCableScanner.stop();
    }

    public void reset() {
        if (mDiscoveryHandler != null) {
            mDiscoveryHandler.removeCallbacksAndMessages(null);
        }
        if (this.mCable != null) {
            this.mCable.reset();
        }
    }

    public void setCableDevName(int id) {
        CableScanner.CableDeviceStub stub;
        if (id == 0) {
            this.mCableId = 0;
            Log.d("Scanner", "CABLE disabled");
            if (this.mCable != null) {
                this.mCable.disconnect();
                this.mCable = null;
                return;
            }
            return;
        }
        this.mCableId = id;
        Log.d("Scanner", "Our CABLE device ID is now: " + this.mCableId);
        if (this.mCableScanner != null && (stub = this.mCableScanner.find(id, false)) != null) {
            Log.d("Scanner", " Found it in the CABLE cache.");
            foundCableDevice(CableSensor.getCachedCable(this.mContext, stub.device(), stub.name(), null, this.mCb));
        }
    }

    public boolean isCableConnected() {
        if (this.mCable == null) {
            return false;
        }
        return this.mCable.isConnected();
    }

    public boolean isCableActive() {
        if (this.mCable == null) {
            return false;
        }
        return this.mCable.isActive();
    }

    public boolean isCableActive(int id) {
        if (this.mCable == null) {
            return false;
        }
        if (id == 0 || this.mCableId == 0) {
            return this.mCable.isConnected();
        }
        if (id == this.mCableId) {
            return this.mCable.isConnected();
        }
        return false;
    }

    public boolean hasCableId() {
        return this.mCableId != 0;
    }

    public void checkCable() {
        if (this.mCable != null) {
            this.mCable.checkForInactive();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTargetCable(String name) {
        return (this.mCableId == 0 || name == null || name.isEmpty() || !CableScanner.isCableName(name) || CableScanner.idFromName(name) != this.mCableId) ? false : true;
    }

    public Dialog configureSensor(Context context, BTLESensor sensor) {
        switch (sensor.getType()) {
            case POWER:
                return BTLESensorConfig.configurePowerSensor(context, sensor);
            default:
                return null;
        }
    }

    public void foundCableDevice(CableSensor cable) {
        if (this.mCable != null) {
            if (this.mCable.equals((Sensor) cable)) {
                Log.d("Scanner", "Ensuring CABLE is connected : " + this.mCable.getConnectionState());
                this.mCable.connect();
                return;
            } else {
                Log.d("Scanner", "Ignoring CABLE device " + cable.getId() + " as one is already connected");
                return;
            }
        }
        String name = cable.getName();
        Log.d("Scanner", "Cable device: " + name + " - looking for: " + this.mCableId);
        if (isTargetCable(name)) {
            this.mCable = cable;
            this.mCable.connect();
        }
    }

    public void checkCableDevice(CableSensor cable) {
        if (this.mCable != null && this.mCable.equals((Sensor) cable)) {
            Log.d("Scanner", "Ensuring CABLE is connected : " + this.mCable.getConnectionState());
            this.mCable.connect();
        }
    }

    static void runOnScannerThread(Runnable runnable, long timeout) {
        if (mDiscoveryHandler != null) {
            mDiscoveryHandler.postDelayed(runnable, timeout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Sensor.SensorType typeFromGattServices(List<BluetoothGattService> services) {
        Sensor.SensorType type = Sensor.SensorType.UNKNOWN;
        boolean footpod = false;
        if (services != null) {
            for (BluetoothGattService srv : services) {
                Log.d("Scanner", " scanned service: " + BTLESensor.getServiceName(srv.getUuid()));
                Sensor.SensorType service = BTLESensor.fromGattService(srv.getUuid());
                if (service != Sensor.SensorType.UNKNOWN) {
                    if (service == Sensor.SensorType.RUNNING_SPEED_CADENCE) {
                        footpod = true;
                    }
                    if (service == Sensor.SensorType.CABLE) {
                        type = service;
                    } else if (type == Sensor.SensorType.UNKNOWN) {
                        type = service;
                    } else if (type != Sensor.SensorType.CABLE) {
                        type = Sensor.SensorType.COMBO;
                    }
                }
            }
        }
        if (type == Sensor.SensorType.COMBO && footpod) {
            return Sensor.SensorType.FOOT_POD;
        }
        return type;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Sensor.SensorType typeFromGattServiceUuids(List<ParcelUuid> uuids) {
        Sensor.SensorType type = Sensor.SensorType.UNKNOWN;
        boolean footpod = false;
        if (uuids != null) {
            for (ParcelUuid uuid : uuids) {
                Log.d("Scanner", " scanned service: " + BTLESensor.getServiceName(uuid.getUuid()));
                Sensor.SensorType service = BTLESensor.fromGattService(uuid.getUuid());
                if (service != Sensor.SensorType.UNKNOWN) {
                    if (service == Sensor.SensorType.RUNNING_SPEED_CADENCE) {
                        footpod = true;
                    }
                    if (type == Sensor.SensorType.UNKNOWN) {
                        type = service;
                    } else {
                        type = footpod ? Sensor.SensorType.FOOT_POD : Sensor.SensorType.COMBO;
                    }
                }
            }
        }
        return type;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BTLESensor createSensor(BluetoothDevice dev, String name, Sensor.SensorType type, boolean allowCombo) {
        if (!SensorsConnector.isSupportedType(type)) {
            type = Sensor.SensorType.UNKNOWN;
        }
        switch (type) {
            case POWER:
            case SPEED_AND_CADENCE:
            case HEARTRATE:
            case RUNNING_SPEED_CADENCE:
                BTLESensor sensor = new BTLESensor(this.mContext, dev, type, this.mCb);
                return sensor;
            case CABLE:
                Log.d("Scanner", "Cable device: " + name + " - looking for: " + this.mCableId);
                if (!allowCombo || !isTargetCable(name)) {
                    return null;
                }
                foundCableDevice(CableSensor.getCachedCable(this.mContext, dev, name, this.mCableScanner, this.mCb));
                return null;
            case COMBO:
            case FOOT_POD:
                if (!allowCombo) {
                    return null;
                }
                BTLESensor sensor2 = new ComboSensor(this.mContext, dev, type == Sensor.SensorType.FOOT_POD, this.mCb);
                return sensor2;
            case OXYGEN:
                BTLESensor sensor3 = new BSXSensor(this.mContext, dev, this.mCb);
                return sensor3;
            default:
                BTLESensor sensor4 = new BTLESensor(this.mContext, dev, Sensor.SensorType.UNKNOWN, this.mCb);
                return sensor4;
        }
    }
}
