package com.kopin.solos.cabledfu;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.kopin.solos.cabledfu.CableScanner;
import com.kopin.solos.cabledfu.DfuPackets;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.GattAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes38.dex */
public class CableDfu {
    static final int DFU_PACKET_SIZE = 20;
    private static final String TAG = "CableDFU";
    private DfuResponseListener mCallback;
    private Context mContext;
    private BluetoothGattCharacteristic mControlPoint;
    private BluetoothGattCharacteristic mDataPackets;
    private BluetoothDevice mDevice;
    private boolean mExpectedDisconnect;
    private String mFwVersion;
    protected BluetoothGatt mGatt;
    private String mHwVersion;
    private String mName;
    private static final UUID GATT_SERVICE_GENERIC = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_SERVICE_DEVICE_INFO = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_SERVICE_BATTERY_STATUS = UUID.fromString(GattAttributes.BATTERY_SERVICE);
    private static final UUID GATT_SERVICE_CABLE = UUID.fromString("4B480001-6E6F-7274-6870-6F6C65656E67");
    private static final UUID GATT_SERVICE_CABLE_DFU = UUID.fromString("00011553-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_SERVICE_DFU = UUID.fromString("00001530-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_SERVICE_SECURE_DFU = UUID.fromString("0000fe59-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_VENDOR_NAME = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_PRODUCT_NAME = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_BATTERY_LEVEL = UUID.fromString(GattAttributes.BATTERY_LEVEL_CHARACTERISTIC);
    private static final UUID GATT_CHARACTERISTIC_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_FW_VERSION = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_HW_VERSION = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_CABLE_CONTROL = UUID.fromString("4B480002-6E6F-7274-6870-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_CABLE_RX = UUID.fromString("4B480003-6E6F-7274-6870-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_CABLE_DFU_CONTROL = UUID.fromString("00011531-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_CABLE_DFU_PACKET = UUID.fromString("00011532-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_CABLE_DFU_VERSION = UUID.fromString("00011534-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_DFU_CONTROL = UUID.fromString("00001531-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_SECURE_DFU_CONTROL = UUID.fromString("8EC90001-F315-4F60-9FB8-838830DAEA50");
    private static final UUID GATT_CHARACTERISTIC_BUTTON_LESS_DFU_CONTROL = UUID.fromString("8ec90003-f315-4f60-9fb8-838830daea50");
    private static final UUID GATT_CHARACTERISTIC_DFU_PACKET = UUID.fromString("00001532-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_CHARACTERISTIC_SECURE_DFU_PACKET = UUID.fromString("8EC90002-F315-4F60-9FB8-838830DAEA50");
    private static final UUID GATT_CHARACTERISTIC_DFU_VERSION = UUID.fromString("00001534-1212-EFDE-1523-6F6C65656E67");
    private static final UUID GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG = UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR);
    private static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private ArrayList<DfuPackets.DfuPacket> mReqQueue = new ArrayList<>();
    private boolean mWriteActive = false;
    private int mBatteryLevel = -1;
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.kopin.solos.cabledfu.CableDfu.1
        private ArrayList<BluetoothGattCharacteristic> mCharsToRead = new ArrayList<>();
        private ArrayList<BluetoothGattCharacteristic> mCharsToSubscribe = new ArrayList<>();

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
                if (CableDfu.this.mCallback != null) {
                    CableDfu.this.mCallback.onReady();
                }
            } else {
                BluetoothGattCharacteristic chr = this.mCharsToSubscribe.remove(0);
                if (chr != null) {
                    Log.d(CableDfu.TAG, "  Subscribe to " + CableDfu.getCharacteristicName(chr.getUuid()));
                    boolean ok = subscribeToCharacteristic(gatt, chr, true);
                    Log.d(CableDfu.TAG, ok ? "   Notifications on for characteristic" : "   Unable to set notification!");
                }
            }
        }

        private boolean subscribeToCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean enabled) {
            for (BluetoothGattDescriptor desc : characteristic.getDescriptors()) {
                if (desc.getUuid().equals(CableDfu.GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG)) {
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
            if (status == 0 && !CableDfu.this.onServicesDiscovered()) {
                BluetoothGattService[] services = {CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_BATTERY_STATUS), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_DEVICE_INFO), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_CABLE), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_CABLE_DFU), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_GENERIC), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_DFU), CableDfu.this.mGatt.getService(CableDfu.GATT_SERVICE_SECURE_DFU)};
                this.mCharsToSubscribe.clear();
                this.mCharsToRead.clear();
                for (BluetoothGattService service : services) {
                    if (service != null) {
                        for (BluetoothGattCharacteristic chr : service.getCharacteristics()) {
                            Log.d(CableDfu.TAG, "  characteristic: " + CableDfu.getCharacteristicName(chr.getUuid()));
                            List<BluetoothGattDescriptor> descs = chr.getDescriptors();
                            for (BluetoothGattDescriptor d : descs) {
                                Log.d(CableDfu.TAG, "    descriptor: " + d.getUuid().toString() + ", permissions: " + d.getPermissions());
                            }
                            Log.d(CableDfu.TAG, "   properties: " + chr.getProperties() + ", permissions: " + chr.getPermissions());
                            if (!chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_FW_VERSION)) {
                                if (!chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_HW_VERSION)) {
                                    if (!chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_DFU_CONTROL) && !chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_CABLE_DFU_CONTROL) && !chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_BUTTON_LESS_DFU_CONTROL) && !chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_SECURE_DFU_CONTROL)) {
                                        if (chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_DFU_PACKET) || chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_CABLE_DFU_PACKET) || chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_SECURE_DFU_PACKET)) {
                                            CableDfu.this.mDataPackets = chr;
                                        } else if (chr.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                            this.mCharsToRead.add(chr);
                                        }
                                    } else {
                                        this.mCharsToSubscribe.add(chr);
                                        CableDfu.this.mControlPoint = chr;
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
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            boolean wasLoss = false;
            if (newState == 2) {
                gatt.discoverServices();
                if (CableDfu.this.mCallback != null) {
                    CableDfu.this.mCallback.onConnected(false);
                    return;
                }
                return;
            }
            if (newState == 0) {
                synchronized (CableDfu.this.mGattCallback) {
                    CableDfu.this.mGattCallback.notify();
                }
                Log.d(CableDfu.TAG, "Lost connection: status = " + status);
                if (CableDfu.this.mGatt != null && !CableDfu.this.mExpectedDisconnect) {
                    wasLoss = true;
                }
                if (CableDfu.this.mCallback != null) {
                    CableDfu.this.mCallback.onDisconnected(wasLoss);
                }
                CableDfu.this.cleanup();
                gatt.close();
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(CableDfu.TAG, "Descriptor read: " + descriptor.getUuid().toString() + " for characteristic: " + CableDfu.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(CableDfu.TAG, "Descriptor write: " + descriptor.getUuid().toString() + " for characteristic: " + CableDfu.getCharacteristicName(descriptor.getCharacteristic().getUuid()) + " (status = " + status + ")");
            readNext(gatt, null);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            UUID chr = characteristic.getUuid();
            Log.d(CableDfu.TAG, "Characteristic read: " + CableDfu.getCharacteristicName(chr));
            byte[] val = characteristic.getValue();
            Log.d(CableDfu.TAG, "  Value: " + CableDfu.bytesToString(val));
            if (!CableDfu.this.onCharacteristicRead(characteristic, status)) {
                if (!characteristic.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                    if (!characteristic.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_FW_VERSION)) {
                        if (!characteristic.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_HW_VERSION)) {
                            if (characteristic.getUuid().equals(CableDfu.GATT_CHARACTERISTIC_DEVICE_NAME)) {
                                if (characteristic.getValue() == null) {
                                    CableDfu.this.mName = "";
                                } else {
                                    CableDfu.this.mName = characteristic.getStringValue(0);
                                }
                                if (CableDfu.this.mName.indexOf(0) > 0) {
                                    CableDfu.this.mName = CableDfu.this.mName.substring(0, CableDfu.this.mName.indexOf(0));
                                }
                                Log.d(CableDfu.TAG, "Device Name: " + CableDfu.this.mName);
                            }
                        } else {
                            if (characteristic.getValue() == null) {
                                CableDfu.this.mHwVersion = "0.0.0";
                            } else {
                                CableDfu.this.mHwVersion = characteristic.getStringValue(0);
                            }
                            Log.d(CableDfu.TAG, "Device Hardware Version: " + CableDfu.this.mHwVersion);
                        }
                    } else {
                        if (characteristic.getValue() == null) {
                            CableDfu.this.mFwVersion = "0.0.0";
                        } else {
                            CableDfu.this.mFwVersion = characteristic.getStringValue(0);
                        }
                        Log.d(CableDfu.TAG, "Device FW Version: " + CableDfu.this.mFwVersion);
                    }
                } else {
                    CableDfu.this.mBatteryLevel = characteristic.getIntValue(17, 0).intValue();
                    Log.d(CableDfu.TAG, "Battery Level = " + CableDfu.this.mBatteryLevel);
                }
                readNext(gatt, characteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            UUID chr = characteristic.getUuid();
            if (!chr.equals(CableDfu.GATT_CHARACTERISTIC_BATTERY_LEVEL)) {
                if (!chr.equals(CableDfu.GATT_CHARACTERISTIC_DFU_CONTROL) && !chr.equals(CableDfu.GATT_CHARACTERISTIC_CABLE_DFU_CONTROL)) {
                    if (!chr.equals(CableDfu.GATT_CHARACTERISTIC_BUTTON_LESS_DFU_CONTROL)) {
                        if (!chr.equals(CableDfu.GATT_CHARACTERISTIC_SECURE_DFU_CONTROL)) {
                            if (chr.equals(CableDfu.GATT_CHARACTERISTIC_DFU_VERSION)) {
                                Log.d(CableDfu.TAG, "Version changed");
                                return;
                            }
                            return;
                        } else {
                            DfuPackets.DfuPacket response = DfuPackets.SecureDFUResponse.parseResponse(characteristic.getValue());
                            Log.d(CableDfu.TAG, "RX: << " + CableDfu.bytesToString(characteristic.getValue()));
                            Log.d(CableDfu.TAG, "RX: " + response.debugName());
                            if (CableDfu.this.mCallback != null) {
                                CableDfu.this.mCallback.onResponse(response);
                                return;
                            }
                            return;
                        }
                    }
                    DfuPackets.DfuPacket response2 = DfuPackets.fromButtonlessDfuValue(characteristic.getValue());
                    Log.d(CableDfu.TAG, "RX: << " + CableDfu.bytesToString(characteristic.getValue()));
                    Log.d(CableDfu.TAG, "RX: " + response2.debugName());
                    if (CableDfu.this.mCallback != null) {
                        CableDfu.this.mCallback.onResponse(response2);
                        return;
                    }
                    return;
                }
                DfuPackets.DfuPacket response3 = DfuPackets.fromValue(characteristic.getValue());
                Log.d(CableDfu.TAG, "RX: << " + CableDfu.bytesToString(characteristic.getValue()));
                Log.d(CableDfu.TAG, "RX: " + response3.debugName());
                if (CableDfu.this.mCallback != null) {
                    CableDfu.this.mCallback.onResponse(response3);
                    return;
                }
                return;
            }
            CableDfu.this.mBatteryLevel = characteristic.getIntValue(17, 0).intValue();
            Log.d(CableDfu.TAG, "Battery Level = " + CableDfu.this.mBatteryLevel);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(CableDfu.TAG, "Characteristic write: " + CableDfu.getCharacteristicName(characteristic.getUuid()) + " (status = " + status + ")");
            if (!CableDfu.this.onCharacteristicWrite(characteristic, status)) {
                readNext(gatt, characteristic);
            }
        }
    };

    public interface DfuResponseListener {
        void onConnected(boolean z);

        void onDisconnected(boolean z);

        void onReady();

        void onResponse(DfuPackets.DfuPacket dfuPacket);
    }

    public static String getServiceName(UUID uuid) {
        if (uuid.equals(GATT_SERVICE_DEVICE_INFO)) {
            return "Device Info";
        }
        if (uuid.equals(GATT_SERVICE_BATTERY_STATUS)) {
            return "Battery Status";
        }
        if (uuid.equals(GATT_SERVICE_CABLE)) {
            return "CABLE Control";
        }
        if (uuid.equals(GATT_SERVICE_SECURE_DFU)) {
            return "Secure DFU Service";
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
        if (uuid.equals(GATT_CHARACTERISTIC_FW_VERSION)) {
            return "Firmware Version";
        }
        if (uuid.equals(GATT_DESCRIPTOR_CHARACTERISTIC_CONFIG)) {
            return "Configuration";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_CABLE_DFU_CONTROL)) {
            return "DFU Control Point (CABLE)";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_CABLE_DFU_PACKET)) {
            return "DFU Packet (CABLE)";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_DFU_CONTROL)) {
            return "DFU Control Point";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_DFU_PACKET)) {
            return "DFU Packet";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_CABLE_CONTROL)) {
            return "CABLE Control Point";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_BUTTON_LESS_DFU_CONTROL)) {
            return "Buttonless DFU Control Point";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_SECURE_DFU_CONTROL)) {
            return "Secure DFU Control Point";
        }
        if (uuid.equals(GATT_CHARACTERISTIC_SECURE_DFU_PACKET)) {
            return "Secure DFU Packet";
        }
        return "Unknown (" + uuid.toString() + ")";
    }

    public CableDfu(Context context, CableScanner.CableDeviceStub dev) {
        this.mContext = context;
        this.mDevice = dev.device();
        this.mName = dev.name();
    }

    public String getName() {
        if (this.mName == null || this.mName.isEmpty()) {
            this.mName = this.mDevice.getName();
        }
        return (this.mName == null || this.mName.isEmpty()) ? this.mDevice.getAddress() : this.mName;
    }

    public String getId() {
        return this.mDevice.getAddress();
    }

    public String getFirmwareVersion() {
        return this.mFwVersion;
    }

    public String getHardwareVersion() {
        return this.mHwVersion;
    }

    public int getCableId() {
        return CableDevice.idFromName(this.mName);
    }

    public boolean isDfuMode() {
        return this.mName.contains("Dfu");
    }

    public void setCallback(DfuResponseListener cb) {
        this.mCallback = cb;
    }

    public void connect() {
        if (this.mGatt == null) {
            Log.d(TAG, "Connecting...");
            this.mGatt = this.mDevice.connectGatt(this.mContext, true, this.mGattCallback);
        }
    }

    public void expectDisconnection(boolean canDisconnect) {
        this.mExpectedDisconnect = canDisconnect;
    }

    public void disconnect() {
        disconnect(false);
    }

    public void disconnect(boolean andWait) {
        if (this.mGatt != null) {
            synchronized (this.mGattCallback) {
                try {
                    this.mGatt.disconnect();
                    if (andWait) {
                        Log.d(TAG, "  Waiting for disconnect");
                        this.mGattCallback.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            this.mDevice = null;
            this.mGatt = null;
            this.mControlPoint = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanup() {
        this.mExpectedDisconnect = false;
        this.mDevice = null;
        this.mGatt = null;
        this.mControlPoint = null;
    }

    public void reconnect() {
        this.mGatt = this.mDevice.connectGatt(this.mContext, true, this.mGattCallback);
    }

    public boolean isConnected() {
        return this.mGatt != null;
    }

    public boolean isBatteryOk() {
        return this.mBatteryLevel == -1 || this.mBatteryLevel > 25;
    }

    protected boolean onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
        return false;
    }

    protected boolean onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        return false;
    }

    protected boolean onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        if (!characteristic.getUuid().equals(GATT_CHARACTERISTIC_DFU_CONTROL) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_CABLE_DFU_CONTROL) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_DFU_PACKET) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_CABLE_DFU_PACKET) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_CABLE_CONTROL) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_BUTTON_LESS_DFU_CONTROL) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_SECURE_DFU_CONTROL) && !characteristic.getUuid().equals(GATT_CHARACTERISTIC_SECURE_DFU_PACKET)) {
            return false;
        }
        if (status != 0) {
            Log.d(TAG, "Couldn't send command: status = " + status);
        }
        synchronized (this.mReqQueue) {
            this.mWriteActive = false;
            if (!this.mReqQueue.isEmpty()) {
                this.mWriteActive = doWriteReq(this.mReqQueue.remove(0));
            }
            if (!this.mWriteActive) {
                this.mReqQueue.notify();
            }
        }
        return true;
    }

    void writeReq(DfuPackets.DfuPacket req) {
        writeReq(req, false);
    }

    void writeReq(DfuPackets.DfuPacket req, boolean andWait) {
        synchronized (this.mReqQueue) {
            if (this.mWriteActive) {
                this.mReqQueue.add(req);
            } else {
                this.mWriteActive = doWriteReq(req);
            }
        }
        if (andWait) {
            while (this.mWriteActive) {
                synchronized (this.mReqQueue) {
                    try {
                        Log.d(TAG, "Waiting for descriptor writes to complete");
                        this.mReqQueue.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    protected boolean doWriteReq(DfuPackets.DfuPacket req) {
        Log.d(TAG, "TX: " + req.debugName());
        Log.d(TAG, "TX: >> " + bytesToString(req.toValue()));
        if (this.mDataPackets != null && (req instanceof DfuPackets.DfuDataPacket)) {
            this.mDataPackets.setValue(req.toValue());
            return this.mGatt.writeCharacteristic(this.mDataPackets);
        }
        if (this.mControlPoint != null) {
            this.mControlPoint.setValue(req.toValue());
            return this.mGatt.writeCharacteristic(this.mControlPoint);
        }
        return false;
    }

    protected boolean onServicesDiscovered() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String bytesToString(byte[] data) {
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
