package com.kopin.solos.sensors.btle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorData;
import com.kopin.solos.sensors.btle.BTLESensorConfig;
import com.kopin.solos.sensors.btle.BTLESensorData;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.GattAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes28.dex */
public class BTLESensor extends Sensor {
    private static final long CONNECT_TIMEOUT = 10000;
    private static final long DISCONNECT_TIMEOUT = 3000;
    private static final long GATT_WRITE_TIMEOUT = 3000;
    private static final String TAG = "BTLESensor";
    private int mBatteryLevel;
    private final Runnable mConnectionWatchdog;
    private Context mContext;
    private BluetoothGattCharacteristic mControlPoint;
    private BluetoothDevice mDevice;
    private int mFailedConnections;
    private String mFwVersion;
    protected BluetoothGatt mGatt;
    private BluetoothGattCallback mGattCallback;
    private String mHwVersion;
    protected SensorData mLastConfig;
    protected long mLastUpdate;
    private long mLastWriteAck;
    private String mName;
    private ArrayList<BTLESensorConfig.ConfigControlRequest> mReqQueue;
    protected Sensor.ConnectionState mState;
    private Sensor.SensorType mType;
    private boolean mWriteActive;
    static final UUID GATT_SERVICE_GENERIC = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_SERVICE_DEVICE_INFO = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_SERVICE_BATTERY_STATUS = UUID.fromString(GattAttributes.BATTERY_SERVICE);
    protected static final UUID GATT_SERVICE_HEARTRATE = UUID.fromString(GattAttributes.HEART_RATE_SERVICE);
    protected static final UUID GATT_SERVICE_SPEED_AND_CADENCE = UUID.fromString(GattAttributes.CSC_SERVICE);
    protected static final UUID GATT_SERVICE_CRANK_POWER = UUID.fromString(GattAttributes.CYCLING_POWER_SERVICE);
    protected static final UUID GATT_SERVICE_RUNNING_SPEED_CADENCE = UUID.fromString(GattAttributes.RSC_SERVICE);
    static final UUID GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE = UUID.fromString("00002a54-0000-1000-8000-00805f9b34fb");
    static final UUID GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE = UUID.fromString("00002a5c-0000-1000-8000-00805f9b34fb");
    static final UUID GATT_CHARACTERISTIC_CRANK_POWER_FEATURE = UUID.fromString("00002a65-0000-1000-8000-00805f9b34fb");
    static final UUID GATT_CHARACTERISTIC_CRANK_POWER_CONTROL = UUID.fromString("00002a66-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_VENDOR_NAME = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_PRODUCT_NAME = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_BATTERY_LEVEL = UUID.fromString(GattAttributes.BATTERY_LEVEL_CHARACTERISTIC);
    static final UUID GATT_CHARACTERISTIC_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_FW_VERSION = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_HW_VERSION = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    private static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    static /* synthetic */ int access$008(BTLESensor x0) {
        int i = x0.mFailedConnections;
        x0.mFailedConnections = i + 1;
        return i;
    }

    public static String getServiceName(UUID uuid) {
        if (uuid.equals(GATT_SERVICE_DEVICE_INFO)) {
            return "Device Info";
        }
        if (uuid.equals(GATT_SERVICE_BATTERY_STATUS)) {
            return "Battery Status";
        }
        if (uuid.equals(GATT_SERVICE_HEARTRATE)) {
            return "HeartRate";
        }
        if (uuid.equals(GATT_SERVICE_SPEED_AND_CADENCE)) {
            return "Speed & Cadence";
        }
        if (uuid.equals(CableSensor.GATT_SERVICE_CABLE)) {
            return "CABLE Control";
        }
        if (uuid.equals(BSXSensor.GATT_SERVICE_OXYGEN)) {
            return "Oxygen";
        }
        if (uuid.equals(GATT_SERVICE_CRANK_POWER)) {
            return "Crank Power";
        }
        if (uuid.equals(GATT_SERVICE_RUNNING_SPEED_CADENCE)) {
            return "Running Speed & Cadence";
        }
        return "Unknown (" + uuid.toString() + ")";
    }

    static String getCharacteristicName(UUID uuid) {
        if (uuid.equals(GATT_CHARACTERISTIC_PRODUCT_NAME)) {
            return "Product Name";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_VENDOR_NAME)) {
            return "Vendor Name";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
            return "Battery Status";
        }
        if (uuid.equals(BTLESensorData.GATT_CHARACTERISTIC_HEARTRATE)) {
            return "HeartRate";
        }
        if (uuid.equals(BTLESensorData.GATT_CHARACTERISTIC_SPEED_AND_CADENCE)) {
            return "Speed & Cadence";
        }
        if (uuid.equals(BTLESensorData.GATT_CHARACTERISTIC_OXYGEN)) {
            return "Oxygen";
        }
        if (uuid.equals(BTLESensorData.GATT_CHARACTERISTIC_CRANK_POWER)) {
            return "Crank Power";
        }
        if (uuid.equals(BTLESensorData.GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG)) {
            return "Configuration";
        }
        if (uuid.equals(BTLESensorData.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE)) {
            return "Running Speed & Cadence";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_FW_VERSION)) {
            return "Firmware Version";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_HW_VERSION)) {
            return "Hardware Version";
        }
        return "Unknown (" + uuid.toString() + ")";
    }

    public static Sensor.SensorType fromGattService(UUID uuid) {
        if (uuid.equals(GATT_SERVICE_HEARTRATE)) {
            return Sensor.SensorType.HEARTRATE;
        }
        if (uuid.equals(GATT_SERVICE_SPEED_AND_CADENCE)) {
            return Sensor.SensorType.SPEED_AND_CADENCE;
        }
        if (uuid.equals(GATT_SERVICE_CRANK_POWER)) {
            return Sensor.SensorType.POWER;
        }
        if (uuid.equals(CableSensor.GATT_SERVICE_CABLE)) {
            return Sensor.SensorType.CABLE;
        }
        if (uuid.equals(BSXSensor.GATT_SERVICE_OXYGEN)) {
            return Sensor.SensorType.OXYGEN;
        }
        if (uuid.equals(GATT_SERVICE_RUNNING_SPEED_CADENCE)) {
            return Sensor.SensorType.RUNNING_SPEED_CADENCE;
        }
        return Sensor.SensorType.UNKNOWN;
    }

    protected BluetoothGattService[] getGattService() {
        if (this.mGatt == null) {
            return null;
        }
        switch (this.mType) {
            case HEARTRATE:
                return new BluetoothGattService[]{this.mGatt.getService(GATT_SERVICE_HEARTRATE), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            case SPEED:
            case CADENCE:
            case SPEED_AND_CADENCE:
                return new BluetoothGattService[]{this.mGatt.getService(GATT_SERVICE_SPEED_AND_CADENCE), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            case POWER:
                return new BluetoothGattService[]{this.mGatt.getService(GATT_SERVICE_CRANK_POWER), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            case OXYGEN:
                return new BluetoothGattService[]{this.mGatt.getService(BSXSensor.GATT_SERVICE_OXYGEN), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            case CABLE:
            case COMBO:
            case FOOT_POD:
                return new BluetoothGattService[]{this.mGatt.getService(CableSensor.GATT_SERVICE_CABLE), this.mGatt.getService(GATT_SERVICE_HEARTRATE), this.mGatt.getService(GATT_SERVICE_SPEED_AND_CADENCE), this.mGatt.getService(GATT_SERVICE_CRANK_POWER), this.mGatt.getService(GATT_SERVICE_RUNNING_SPEED_CADENCE), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            case RUNNING_SPEED_CADENCE:
                return new BluetoothGattService[]{this.mGatt.getService(GATT_SERVICE_RUNNING_SPEED_CADENCE), this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
            default:
                return new BluetoothGattService[]{this.mGatt.getService(GATT_SERVICE_DEVICE_INFO)};
        }
    }

    protected boolean isGattCharacteristic(BluetoothGattCharacteristic characteristic) {
        switch (this.mType) {
            case HEARTRATE:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_HEARTRATE);
            case SPEED:
            case CADENCE:
            case SPEED_AND_CADENCE:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_SPEED_AND_CADENCE);
            case POWER:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_CRANK_POWER);
            case OXYGEN:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_OXYGEN) || characteristic.getUuid().equals(BSXSensor.GATT_CHARACTERISTIC_OXYGEN_POS);
            case CABLE:
            case COMBO:
            case FOOT_POD:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_HEARTRATE) || characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_SPEED_AND_CADENCE) || characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_CRANK_POWER) || characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE);
            case RUNNING_SPEED_CADENCE:
                return characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE);
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gainCapabilitiesForGattCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_HEARTRATE)) {
            gainDataType(Sensor.DataType.HEARTRATE);
            return;
        }
        if (characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_SPEED_AND_CADENCE)) {
            gainDataType(Sensor.DataType.SPEED);
            gainDataType(Sensor.DataType.CADENCE);
            return;
        }
        if (characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE)) {
            gainDataType(Sensor.DataType.PACE);
            gainDataType(Sensor.DataType.STEP);
        } else if (characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_CRANK_POWER)) {
            gainDataType(Sensor.DataType.POWER);
            gainDataType(Sensor.DataType.KICK);
        } else if (characteristic.getUuid().equals(BTLESensorData.GATT_CHARACTERISTIC_OXYGEN)) {
            gainDataType(Sensor.DataType.OXYGEN);
        }
    }

    public BTLESensor(Context context, BluetoothDevice dev, Sensor.SensorType type, Sensor.SensorObserver obs) {
        this(context, dev, null, type, obs);
    }

    public BTLESensor(Context context, BluetoothDevice dev, String name, Sensor.SensorType type, Sensor.SensorObserver obs) {
        super(obs);
        this.mReqQueue = new ArrayList<>();
        this.mWriteActive = false;
        this.mLastWriteAck = 0L;
        this.mFailedConnections = 0;
        this.mLastConfig = SensorData.EMPTY;
        this.mBatteryLevel = -1;
        this.mConnectionWatchdog = new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.1
            @Override // java.lang.Runnable
            public void run() {
                Log.d(BTLESensor.TAG, "Connection watchdog for " + BTLESensor.this.getName() + " state: " + BTLESensor.this.mState);
                switch (AnonymousClass3.$SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[BTLESensor.this.mState.ordinal()]) {
                    case 1:
                    case 2:
                        synchronized (this) {
                            Log.d(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " has failed to connect");
                            if (BTLESensor.this.mGatt != null) {
                                BTLESensor.this.mGatt.disconnect();
                                BTLESensor.this.mGatt.close();
                            }
                            BTLESensor.this.mGatt = null;
                            BTLESensor.access$008(BTLESensor.this);
                            if (BTLESensor.this.mFailedConnections > 2) {
                                BTLESensor.this.onSensorTimeout();
                                BTLESensor.this.mFailedConnections = 0;
                            }
                            BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                            break;
                        }
                        return;
                    case 3:
                        BTLESensor.this.mFailedConnections = 0;
                        return;
                    case 4:
                        Log.d(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " has failed to disconnect cleanly");
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        return;
                    default:
                        return;
                }
            }
        };
        this.mGattCallback = new BluetoothGattCallback() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2
            private ArrayList<BluetoothGattCharacteristic> mCharsToRead = new ArrayList<>();
            private ArrayList<BluetoothGattCharacteristic> mCharsToSubscribe = new ArrayList<>();
            boolean isDisposed = false;

            private void readNext(BluetoothGatt gatt, BluetoothGattCharacteristic last) {
                if (this.mCharsToRead == null || this.mCharsToRead.isEmpty()) {
                    doSubscribe(gatt);
                    return;
                }
                BluetoothGattCharacteristic last2 = this.mCharsToRead.remove(0);
                BluetoothGattCharacteristic last3 = last2;
                if (last3 != null) {
                    gatt.readCharacteristic(last3);
                }
            }

            private void doSubscribe(BluetoothGatt gatt) {
                if (this.mCharsToSubscribe == null || this.mCharsToSubscribe.isEmpty()) {
                    BTLESensor.this.onServiceDiscoveryComplete();
                    return;
                }
                BluetoothGattCharacteristic chr = this.mCharsToSubscribe.remove(0);
                if (chr != null) {
                    Log.d(BTLESensor.TAG, "  Subscribe to " + BTLESensor.getCharacteristicName(chr.getUuid()));
                    boolean ok = subscribeToCharacteristic(gatt, chr, true);
                    Log.d(BTLESensor.TAG, ok ? "   Notifications on for characteristic" : "   Unable to set notification!");
                }
            }

            private boolean subscribeToCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean enabled) {
                for (BluetoothGattDescriptor desc : characteristic.getDescriptors()) {
                    if (desc.getUuid().equals(BTLESensorData.GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG)) {
                        boolean ok = gatt.setCharacteristicNotification(characteristic, enabled);
                        if (ok) {
                            int props = characteristic.getProperties();
                            if ((props & 16) != 0) {
                                desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                return gatt.writeDescriptor(desc);
                            }
                            if ((props & 32) != 0) {
                                desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                return gatt.writeDescriptor(desc);
                            }
                            return false;
                        }
                        return ok;
                    }
                }
                return false;
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == 0 && !BTLESensor.this.onServicesDiscovered()) {
                    BluetoothGattService[] services = BTLESensor.this.getGattService();
                    if (services == null || services.length == 0) {
                        gatt.disconnect();
                        return;
                    }
                    this.mCharsToSubscribe.clear();
                    this.mCharsToRead.clear();
                    for (BluetoothGattService service : services) {
                        if (service != null) {
                            for (BluetoothGattCharacteristic chr : service.getCharacteristics()) {
                                Log.d(BTLESensor.TAG, "  characteristic: " + BTLESensor.getCharacteristicName(chr.getUuid()));
                                List<BluetoothGattDescriptor> descs = chr.getDescriptors();
                                for (BluetoothGattDescriptor d : descs) {
                                    Log.d(BTLESensor.TAG, "    descriptor: " + d.getUuid().toString() + ", permissions: " + d.getPermissions());
                                }
                                Log.d(BTLESensor.TAG, "   properties: " + chr.getProperties() + ", permissions: " + chr.getPermissions());
                                if (BTLESensor.this.isGattCharacteristic(chr)) {
                                    this.mCharsToSubscribe.add(chr);
                                    BTLESensor.this.gainCapabilitiesForGattCharacteristic(chr);
                                    BTLESensor.this.onSensorAvailable();
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_CONTROL)) {
                                    this.mCharsToSubscribe.add(chr);
                                    BTLESensor.this.mControlPoint = chr;
                                } else if (!chr.getUuid().equals(BSXSensor.GATT_CHARACTERISTIC_OXYGEN_ENABLE)) {
                                    if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                                        this.mCharsToSubscribe.add(chr);
                                        this.mCharsToRead.add(chr);
                                    } else if (!chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                        if (!chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_FW_VERSION)) {
                                            if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_HW_VERSION)) {
                                                this.mCharsToRead.add(chr);
                                            }
                                        } else {
                                            this.mCharsToRead.add(chr);
                                        }
                                    } else {
                                        this.mCharsToRead.add(chr);
                                    }
                                } else {
                                    this.mCharsToRead.add(chr);
                                }
                            }
                        }
                    }
                    readNext(gatt, null);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
                if (newState == 2) {
                    Log.e(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " connected: err = " + status + " (was " + BTLESensor.this.mState + ")");
                    if (BTLESensor.this.mState == Sensor.ConnectionState.CONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.RECONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTED) {
                        BTLESensor.this.mState = Sensor.ConnectionState.CONNECTED;
                        if (0 == 0) {
                            BTLEScanner.runOnScannerThread(new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    gatt.discoverServices();
                                }
                            }, 500L);
                            return;
                        }
                        return;
                    }
                    Log.e(BTLESensor.TAG, "Spurious connected state for sensor: " + BTLESensor.this.getName() + " - " + BTLESensor.this.mState);
                    return;
                }
                if (newState == 0) {
                    Log.e(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " disconnected: err = " + status + " (was " + BTLESensor.this.mState + ")");
                    if (BTLESensor.this.mGatt == null || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTED) {
                        gatt.close();
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLESensor.this.onSensorUnavailable();
                        BTLESensor.this.onSensorDisconnected();
                        return;
                    }
                    if (BTLESensor.this.mState == Sensor.ConnectionState.CONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.RECONNECTING) {
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLEScanner.runOnScannerThread(new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.d(BTLESensor.TAG, "Retrying connection attempt...");
                                BTLESensor.this.connect();
                            }
                        }, 250L);
                    } else {
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLESensor.this.onSensorUnavailable();
                        BTLESensor.this.onSensorTimeout();
                    }
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(BTLESensor.TAG, "Descriptor read: " + descriptor.getUuid().toString() + " for characteristic: " + BTLESensor.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(BTLESensor.TAG, "Descriptor write: " + descriptor.getUuid().toString() + " for characteristic: " + BTLESensor.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
                readNext(gatt, null);
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                UUID chr = characteristic.getUuid();
                Log.d(BTLESensor.TAG, "Characteristic read: " + BTLESensor.getCharacteristicName(chr));
                byte[] val = characteristic.getValue();
                Log.v(BTLESensor.TAG, "  Value: " + BTLESensor.bytesToString(val));
                if (!BTLESensor.this.onCharacteristicRead(characteristic, status)) {
                    if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                        BTLESensor.this.mBatteryLevel = characteristic.getIntValue(17, 0).intValue();
                        Log.d(BTLESensor.TAG, "Battery Level = " + BTLESensor.this.mBatteryLevel);
                    } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE) || characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
                        BTLESensor.this.onConfigRead(BTLESensorData.fromCharacteristicValue(characteristic, characteristic.getValue()));
                    } else if (!characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
                        if (!characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_FW_VERSION)) {
                            if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_HW_VERSION)) {
                                if (characteristic.getValue() == null) {
                                    BTLESensor.this.mHwVersion = "0.0.0";
                                } else {
                                    BTLESensor.this.mHwVersion = characteristic.getStringValue(0);
                                }
                                Log.d(BTLESensor.TAG, "Device HW Version: " + BTLESensor.this.mHwVersion);
                            } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                if (characteristic.getValue() == null) {
                                    BTLESensor.this.mName = "";
                                } else {
                                    BTLESensor.this.mName = characteristic.getStringValue(0);
                                }
                                if (BTLESensor.this.mName.indexOf(0) > 0) {
                                    BTLESensor.this.mName = BTLESensor.this.mName.substring(0, BTLESensor.this.mName.indexOf(0));
                                }
                                Log.d(BTLESensor.TAG, "Device Name: " + BTLESensor.this.mName);
                            }
                        } else {
                            if (characteristic.getValue() == null) {
                                BTLESensor.this.mFwVersion = "0.0.0";
                            } else {
                                BTLESensor.this.mFwVersion = characteristic.getStringValue(0);
                            }
                            Log.d(BTLESensor.TAG, "Device FW Version: " + BTLESensor.this.mFwVersion);
                        }
                    } else {
                        BTLESensor.this.mLastConfig = new BTLESensorConfig.CrankPowerConfig();
                        ((BTLESensorConfig.CrankPowerConfig) BTLESensor.this.mLastConfig).flagsFromValue(characteristic.getValue());
                        BTLESensor.this.onConfigRead(BTLESensor.this.mLastConfig);
                    }
                    readNext(gatt, characteristic);
                }
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:38:0x01a1  */
            /* JADX WARN: Removed duplicated region for block: B:62:0x02c1  */
            @Override // android.bluetooth.BluetoothGattCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onCharacteristicChanged(android.bluetooth.BluetoothGatt r27, android.bluetooth.BluetoothGattCharacteristic r28) {
                /*
                    Method dump skipped, instruction units count: 1406
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.kopin.solos.sensors.btle.BTLESensor.AnonymousClass2.onCharacteristicChanged(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic):void");
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(BTLESensor.TAG, "Characteristic write: " + BTLESensor.getCharacteristicName(characteristic.getUuid()) + " (status = " + status + ")");
                if (!BTLESensor.this.onCharacteristicWrite(characteristic, status)) {
                    readNext(gatt, characteristic);
                }
            }
        };
        this.mContext = context;
        this.mDevice = dev;
        this.mName = name == null ? dev.getName() : name;
        this.mType = type;
        this.mState = Sensor.ConnectionState.DISCONNECTED;
    }

    protected BTLESensor(BTLESensor parent, String name, Sensor.SensorType type, Sensor.SensorObserver obs) {
        super(obs);
        this.mReqQueue = new ArrayList<>();
        this.mWriteActive = false;
        this.mLastWriteAck = 0L;
        this.mFailedConnections = 0;
        this.mLastConfig = SensorData.EMPTY;
        this.mBatteryLevel = -1;
        this.mConnectionWatchdog = new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.1
            @Override // java.lang.Runnable
            public void run() {
                Log.d(BTLESensor.TAG, "Connection watchdog for " + BTLESensor.this.getName() + " state: " + BTLESensor.this.mState);
                switch (AnonymousClass3.$SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[BTLESensor.this.mState.ordinal()]) {
                    case 1:
                    case 2:
                        synchronized (this) {
                            Log.d(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " has failed to connect");
                            if (BTLESensor.this.mGatt != null) {
                                BTLESensor.this.mGatt.disconnect();
                                BTLESensor.this.mGatt.close();
                            }
                            BTLESensor.this.mGatt = null;
                            BTLESensor.access$008(BTLESensor.this);
                            if (BTLESensor.this.mFailedConnections > 2) {
                                BTLESensor.this.onSensorTimeout();
                                BTLESensor.this.mFailedConnections = 0;
                            }
                            BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                            break;
                        }
                        return;
                    case 3:
                        BTLESensor.this.mFailedConnections = 0;
                        return;
                    case 4:
                        Log.d(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " has failed to disconnect cleanly");
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        return;
                    default:
                        return;
                }
            }
        };
        this.mGattCallback = new BluetoothGattCallback() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2
            private ArrayList<BluetoothGattCharacteristic> mCharsToRead = new ArrayList<>();
            private ArrayList<BluetoothGattCharacteristic> mCharsToSubscribe = new ArrayList<>();
            boolean isDisposed = false;

            private void readNext(BluetoothGatt gatt, BluetoothGattCharacteristic last) {
                if (this.mCharsToRead == null || this.mCharsToRead.isEmpty()) {
                    doSubscribe(gatt);
                    return;
                }
                BluetoothGattCharacteristic last2 = this.mCharsToRead.remove(0);
                BluetoothGattCharacteristic last3 = last2;
                if (last3 != null) {
                    gatt.readCharacteristic(last3);
                }
            }

            private void doSubscribe(BluetoothGatt gatt) {
                if (this.mCharsToSubscribe == null || this.mCharsToSubscribe.isEmpty()) {
                    BTLESensor.this.onServiceDiscoveryComplete();
                    return;
                }
                BluetoothGattCharacteristic chr = this.mCharsToSubscribe.remove(0);
                if (chr != null) {
                    Log.d(BTLESensor.TAG, "  Subscribe to " + BTLESensor.getCharacteristicName(chr.getUuid()));
                    boolean ok = subscribeToCharacteristic(gatt, chr, true);
                    Log.d(BTLESensor.TAG, ok ? "   Notifications on for characteristic" : "   Unable to set notification!");
                }
            }

            private boolean subscribeToCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean enabled) {
                for (BluetoothGattDescriptor desc : characteristic.getDescriptors()) {
                    if (desc.getUuid().equals(BTLESensorData.GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG)) {
                        boolean ok = gatt.setCharacteristicNotification(characteristic, enabled);
                        if (ok) {
                            int props = characteristic.getProperties();
                            if ((props & 16) != 0) {
                                desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                return gatt.writeDescriptor(desc);
                            }
                            if ((props & 32) != 0) {
                                desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                return gatt.writeDescriptor(desc);
                            }
                            return false;
                        }
                        return ok;
                    }
                }
                return false;
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == 0 && !BTLESensor.this.onServicesDiscovered()) {
                    BluetoothGattService[] services = BTLESensor.this.getGattService();
                    if (services == null || services.length == 0) {
                        gatt.disconnect();
                        return;
                    }
                    this.mCharsToSubscribe.clear();
                    this.mCharsToRead.clear();
                    for (BluetoothGattService service : services) {
                        if (service != null) {
                            for (BluetoothGattCharacteristic chr : service.getCharacteristics()) {
                                Log.d(BTLESensor.TAG, "  characteristic: " + BTLESensor.getCharacteristicName(chr.getUuid()));
                                List<BluetoothGattDescriptor> descs = chr.getDescriptors();
                                for (BluetoothGattDescriptor d : descs) {
                                    Log.d(BTLESensor.TAG, "    descriptor: " + d.getUuid().toString() + ", permissions: " + d.getPermissions());
                                }
                                Log.d(BTLESensor.TAG, "   properties: " + chr.getProperties() + ", permissions: " + chr.getPermissions());
                                if (BTLESensor.this.isGattCharacteristic(chr)) {
                                    this.mCharsToSubscribe.add(chr);
                                    BTLESensor.this.gainCapabilitiesForGattCharacteristic(chr);
                                    BTLESensor.this.onSensorAvailable();
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
                                    this.mCharsToRead.add(chr);
                                } else if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_CONTROL)) {
                                    this.mCharsToSubscribe.add(chr);
                                    BTLESensor.this.mControlPoint = chr;
                                } else if (!chr.getUuid().equals(BSXSensor.GATT_CHARACTERISTIC_OXYGEN_ENABLE)) {
                                    if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                                        this.mCharsToSubscribe.add(chr);
                                        this.mCharsToRead.add(chr);
                                    } else if (!chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                        if (!chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_FW_VERSION)) {
                                            if (chr.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_HW_VERSION)) {
                                                this.mCharsToRead.add(chr);
                                            }
                                        } else {
                                            this.mCharsToRead.add(chr);
                                        }
                                    } else {
                                        this.mCharsToRead.add(chr);
                                    }
                                } else {
                                    this.mCharsToRead.add(chr);
                                }
                            }
                        }
                    }
                    readNext(gatt, null);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
                if (newState == 2) {
                    Log.e(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " connected: err = " + status + " (was " + BTLESensor.this.mState + ")");
                    if (BTLESensor.this.mState == Sensor.ConnectionState.CONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.RECONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTED) {
                        BTLESensor.this.mState = Sensor.ConnectionState.CONNECTED;
                        if (0 == 0) {
                            BTLEScanner.runOnScannerThread(new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    gatt.discoverServices();
                                }
                            }, 500L);
                            return;
                        }
                        return;
                    }
                    Log.e(BTLESensor.TAG, "Spurious connected state for sensor: " + BTLESensor.this.getName() + " - " + BTLESensor.this.mState);
                    return;
                }
                if (newState == 0) {
                    Log.e(BTLESensor.TAG, "Sensor " + BTLESensor.this.getName() + " disconnected: err = " + status + " (was " + BTLESensor.this.mState + ")");
                    if (BTLESensor.this.mGatt == null || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.DISCONNECTED) {
                        gatt.close();
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLESensor.this.onSensorUnavailable();
                        BTLESensor.this.onSensorDisconnected();
                        return;
                    }
                    if (BTLESensor.this.mState == Sensor.ConnectionState.CONNECTING || BTLESensor.this.mState == Sensor.ConnectionState.RECONNECTING) {
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLEScanner.runOnScannerThread(new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensor.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.d(BTLESensor.TAG, "Retrying connection attempt...");
                                BTLESensor.this.connect();
                            }
                        }, 250L);
                    } else {
                        BTLESensor.this.mState = Sensor.ConnectionState.DISCONNECTED;
                        BTLESensor.this.onSensorUnavailable();
                        BTLESensor.this.onSensorTimeout();
                    }
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(BTLESensor.TAG, "Descriptor read: " + descriptor.getUuid().toString() + " for characteristic: " + BTLESensor.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(BTLESensor.TAG, "Descriptor write: " + descriptor.getUuid().toString() + " for characteristic: " + BTLESensor.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
                readNext(gatt, null);
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                UUID chr = characteristic.getUuid();
                Log.d(BTLESensor.TAG, "Characteristic read: " + BTLESensor.getCharacteristicName(chr));
                byte[] val = characteristic.getValue();
                Log.v(BTLESensor.TAG, "  Value: " + BTLESensor.bytesToString(val));
                if (!BTLESensor.this.onCharacteristicRead(characteristic, status)) {
                    if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                        BTLESensor.this.mBatteryLevel = characteristic.getIntValue(17, 0).intValue();
                        Log.d(BTLESensor.TAG, "Battery Level = " + BTLESensor.this.mBatteryLevel);
                    } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_SPEED_AND_CADENCE_FEATURE) || characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_RUNNING_SPEED_CADENCE_FEATURE)) {
                        BTLESensor.this.onConfigRead(BTLESensorData.fromCharacteristicValue(characteristic, characteristic.getValue()));
                    } else if (!characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_CRANK_POWER_FEATURE)) {
                        if (!characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_FW_VERSION)) {
                            if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_HW_VERSION)) {
                                if (characteristic.getValue() == null) {
                                    BTLESensor.this.mHwVersion = "0.0.0";
                                } else {
                                    BTLESensor.this.mHwVersion = characteristic.getStringValue(0);
                                }
                                Log.d(BTLESensor.TAG, "Device HW Version: " + BTLESensor.this.mHwVersion);
                            } else if (characteristic.getUuid().equals(BTLESensor.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                if (characteristic.getValue() == null) {
                                    BTLESensor.this.mName = "";
                                } else {
                                    BTLESensor.this.mName = characteristic.getStringValue(0);
                                }
                                if (BTLESensor.this.mName.indexOf(0) > 0) {
                                    BTLESensor.this.mName = BTLESensor.this.mName.substring(0, BTLESensor.this.mName.indexOf(0));
                                }
                                Log.d(BTLESensor.TAG, "Device Name: " + BTLESensor.this.mName);
                            }
                        } else {
                            if (characteristic.getValue() == null) {
                                BTLESensor.this.mFwVersion = "0.0.0";
                            } else {
                                BTLESensor.this.mFwVersion = characteristic.getStringValue(0);
                            }
                            Log.d(BTLESensor.TAG, "Device FW Version: " + BTLESensor.this.mFwVersion);
                        }
                    } else {
                        BTLESensor.this.mLastConfig = new BTLESensorConfig.CrankPowerConfig();
                        ((BTLESensorConfig.CrankPowerConfig) BTLESensor.this.mLastConfig).flagsFromValue(characteristic.getValue());
                        BTLESensor.this.onConfigRead(BTLESensor.this.mLastConfig);
                    }
                    readNext(gatt, characteristic);
                }
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:38:0x01a1  */
            /* JADX WARN: Removed duplicated region for block: B:62:0x02c1  */
            @Override // android.bluetooth.BluetoothGattCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onCharacteristicChanged(android.bluetooth.BluetoothGatt r27, android.bluetooth.BluetoothGattCharacteristic r28) {
                /*
                    Method dump skipped, instruction units count: 1406
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.kopin.solos.sensors.btle.BTLESensor.AnonymousClass2.onCharacteristicChanged(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic):void");
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(BTLESensor.TAG, "Characteristic write: " + BTLESensor.getCharacteristicName(characteristic.getUuid()) + " (status = " + status + ")");
                if (!BTLESensor.this.onCharacteristicWrite(characteristic, status)) {
                    readNext(gatt, characteristic);
                }
            }
        };
        this.mContext = parent.mContext;
        this.mDevice = parent.mDevice;
        this.mName = name == null ? this.mDevice.getName() : name;
        this.mType = type;
        this.mState = Sensor.ConnectionState.DISCONNECTED;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String serialize() {
        return "BTLE," + getId() + "," + getName() + "," + getType().ordinal();
    }

    public static Sensor deserialize(String params, Context context, Sensor.SensorObserver obs) {
        if (!params.startsWith("BTLE")) {
            return null;
        }
        String[] values = params.split(",");
        try {
            BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(values[1]);
            Sensor.SensorType type = Sensor.SensorType.values()[Integer.parseInt(values[3])];
            return new BTLESensor(context, dev, values[2], type, obs);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String getName() {
        if (this.mName == null || this.mName.isEmpty()) {
            this.mName = this.mDevice.getName();
        }
        return (this.mName == null || this.mName.isEmpty()) ? this.mDevice.getAddress() : this.mName;
    }

    public String getVersion() {
        return this.mFwVersion == null ? "0.0.0" : this.mFwVersion;
    }

    public String getHardwareVersion() {
        return this.mHwVersion == null ? "0.0.0" : this.mHwVersion;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String getId() {
        return this.mDevice.getAddress();
    }

    @Override // com.kopin.solos.sensors.Sensor
    public Sensor.SensorType getType() {
        return this.mType;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String getLastValue() {
        return super.getLastValue();
    }

    public SensorData getLastConfig() {
        return this.mLastConfig;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public long getLastUpdateTime(Sensor.DataType dataType) {
        if (equals(dataType)) {
            return this.mLastUpdate;
        }
        return 0L;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean equals(Sensor.SensorType type) {
        return this.mType.equals(type);
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void connect() {
        if (this.mType != Sensor.SensorType.UNKNOWN) {
            synchronized (this) {
                Log.d(TAG, "connect: request to connect sensor " + getName() + " state: " + this.mState);
                if (this.mState == Sensor.ConnectionState.DISCONNECTED) {
                    if (this.mGatt == null) {
                        Log.d(TAG, "connect: CONNECTING auto: true");
                        this.mState = Sensor.ConnectionState.CONNECTING;
                        this.mGatt = this.mDevice.connectGatt(this.mContext, true, this.mGattCallback);
                        BTLEScanner.runOnScannerThread(this.mConnectionWatchdog, 10000L);
                    } else {
                        Log.d(TAG, "connect: RECONNECTING");
                        if (this.mGatt.connect()) {
                            this.mState = Sensor.ConnectionState.RECONNECTING;
                        }
                    }
                }
            }
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void disconnect() {
        synchronized (this) {
            if (this.mGatt != null) {
                Log.d(TAG, "disconnect: Disconnecting sensor " + getName());
                clearReqs();
                this.mState = Sensor.ConnectionState.DISCONNECTING;
                this.mGatt.disconnect();
                this.mGatt = null;
                this.mControlPoint = null;
                BTLEScanner.runOnScannerThread(this.mConnectionWatchdog, 3000L);
            }
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.sensors.btle.BTLESensor$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState = new int[Sensor.ConnectionState.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[Sensor.ConnectionState.CONNECTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[Sensor.ConnectionState.RECONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[Sensor.ConnectionState.CONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[Sensor.ConnectionState.DISCONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType = new int[Sensor.SensorType.values().length];
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.HEARTRATE.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.SPEED.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.CADENCE.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.SPEED_AND_CADENCE.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.POWER.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.OXYGEN.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.CABLE.ordinal()] = 7;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.COMBO.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.FOOT_POD.ordinal()] = 9;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[Sensor.SensorType.RUNNING_SPEED_CADENCE.ordinal()] = 10;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    @Override // com.kopin.solos.sensors.Sensor
    public Sensor.ConnectionState getConnectionState() {
        return this.mState;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isBatteryOk() {
        return this.mBatteryLevel == -1 || this.mBatteryLevel > 25;
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onSensorConnected() {
        if (hasConfig()) {
            int i = AnonymousClass3.$SwitchMap$com$kopin$solos$sensors$Sensor$SensorType[this.mType.ordinal()];
        }
        clearReqs();
        super.onSensorConnected();
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected void onSensorDisconnected() {
        this.mLastConfig = SensorData.EMPTY;
        clearReqs();
        super.onSensorDisconnected();
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void activate() {
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void deactivate() {
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean hasConfig() {
        return (this.mControlPoint == null || this.mType != Sensor.SensorType.POWER || this.mLastConfig == null || this.mLastConfig == SensorData.EMPTY) ? false : true;
    }

    protected void onConfigRead(SensorData confdata) {
        if (confdata instanceof BTLESensorData.SpeedAndCadence) {
            BTLESensorData.SpeedAndCadence features = (BTLESensorData.SpeedAndCadence) confdata;
            if (this.mType.equals(Sensor.SensorType.SPEED_AND_CADENCE)) {
                if (!features.hasSpeedData()) {
                    this.mType = Sensor.SensorType.CADENCE;
                    loseDataType(Sensor.DataType.SPEED);
                } else if (!features.hasCadenceData()) {
                    this.mType = Sensor.SensorType.SPEED;
                    loseDataType(Sensor.DataType.CADENCE);
                }
            }
        } else if ((confdata instanceof BTLESensorConfig.CrankPowerConfig) && (((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).hasCadence() || ((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).hasSpeed())) {
            Log.d(TAG, "Sensor: " + getName() + " can provide Speed/Cadence!");
        }
        onSensorChanged();
    }

    protected boolean onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
        return false;
    }

    protected boolean onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        BTLESensorConfig.CrankPowerResponse resp;
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_CRANK_POWER_CONTROL)) {
            return false;
        }
        if (this.mLastConfig != null && (resp = ((BTLESensorConfig.CrankPowerConfig) this.mLastConfig).responseFromValue(characteristic.getValue())) != null) {
            onConfigMessage(resp.mSuccess, resp.mMessage);
        }
        return true;
    }

    protected boolean onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_CRANK_POWER_CONTROL) && !characteristic.getUuid().equals(CableSensor.GATT_CHARACTERISTIC_CABLE_CONTROL)) {
            return false;
        }
        if (status != 0) {
            Log.d(TAG, "Couldn't send command: status = " + status);
            onConfigMessage(false, "Couldn't send control command");
        }
        synchronized (this.mReqQueue) {
            this.mWriteActive = false;
            this.mLastWriteAck = System.currentTimeMillis();
            if (!this.mReqQueue.isEmpty()) {
                this.mWriteActive = doWriteReq(this.mReqQueue.remove(0));
            }
        }
        return true;
    }

    protected void clearReqs() {
        synchronized (this.mReqQueue) {
            this.mWriteActive = false;
            this.mReqQueue.clear();
        }
    }

    protected void writeReq(BTLESensorConfig.ConfigControlRequest req) {
        synchronized (this.mReqQueue) {
            if (this.mWriteActive) {
                if (this.mLastWriteAck > 0 && System.currentTimeMillis() - this.mLastWriteAck > 3000) {
                    Log.d(TAG, "writeReq: Too long since last write completed, clearing Q (" + this.mReqQueue.size() + ")");
                    clearReqs();
                    onSensorTimeout();
                } else {
                    this.mReqQueue.add(req);
                }
            }
            if (!this.mWriteActive) {
                this.mLastWriteAck = System.currentTimeMillis();
                this.mWriteActive = doWriteReq(req);
            }
        }
    }

    protected boolean doWriteReq(BTLESensorConfig.ConfigControlRequest req) {
        boolean zWriteCharacteristic = false;
        synchronized (this) {
            if (this.mControlPoint != null && this.mState == Sensor.ConnectionState.CONNECTED) {
                try {
                    Log.d(TAG, "TX: " + req.debugName());
                    Log.v(TAG, "   " + bytesToString(req.toValue()));
                    this.mControlPoint.setValue(req.toValue());
                    zWriteCharacteristic = this.mGatt.writeCharacteristic(this.mControlPoint);
                } catch (Exception e) {
                }
            }
        }
        return zWriteCharacteristic;
    }

    protected boolean onServicesDiscovered() {
        return false;
    }

    protected void onServiceDiscoveryComplete() {
        onSensorConnected();
    }

    static String bytesToString(byte[] data) {
        StringBuilder sb = new StringBuilder("[");
        boolean init = false;
        if (data != null) {
            for (byte b : data) {
                if (init) {
                    sb.append(", ");
                }
                sb.append(HEX_CHARS[(b >> 4) & 15]);
                sb.append(HEX_CHARS[b & 15]);
                init = true;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
