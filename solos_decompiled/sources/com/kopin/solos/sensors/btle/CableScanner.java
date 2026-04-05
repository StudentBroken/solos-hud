package com.kopin.solos.sensors.btle;

import android.R;
import android.annotation.SuppressLint;
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
import android.content.SharedPreferences;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/* JADX INFO: loaded from: classes28.dex */
@SuppressLint({"NewApi"})
public class CableScanner {
    static final int CABLE_INITIAL_PROXIMITY = -60;
    private static final String PREFERENCES_KEY = "cachedCableDevices";
    private static final String TAG = "CableScanner";
    private final Context mContext;
    private DeepScanner mDeepScanner;
    private DiscoveryListener mListener;
    private final SharedPreferences mPrefs;
    private static final UUID GATT_SERVICE_GENERIC = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    private static final UUID GATT_CHARACTERISTIC_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    private static final UUID NPE_SERVICE_CABLE = UUID.fromString("4b480001-6e6f-7274-6870-6f6c65656e67");
    private static final UUID NPE_SERVICE_DFU = UUID.fromString("00001530-1212-efde-1523-6f6c65656e67");
    private final BluetoothLeScanner mScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    private final ArrayList<BluetoothDevice> mScannedList = new ArrayList<>();
    private final ArrayList<CableDeviceStub> mCableCache = new ArrayList<>();
    private final ScanCallback mScanResults = new ScanCallback() { // from class: com.kopin.solos.sensors.btle.CableScanner.4
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice dev = result.getDevice();
            ScanRecord rec = result.getScanRecord();
            String name = rec.getDeviceName();
            if (name == null) {
                name = dev.getName();
            }
            if (name == null) {
                name = dev.getAddress();
            }
            if (CableScanner.this.mDeepScanner != null && !CableScanner.this.mScannedList.contains(dev)) {
                Log.d(CableScanner.TAG, "Got new device: " + name);
                CableScanner.this.mScannedList.add(dev);
                byte[] mfid = result.getScanRecord().getManufacturerSpecificData(816);
                if (mfid != null) {
                    CableScanner.this.mListener.onDeviceDiscovered(new CableDeviceStub(name, dev));
                } else if (CableScanner.this.hasCableUuid(rec.getServiceUuids())) {
                    CableScanner.this.mListener.onDeviceDiscovered(new CableDeviceStub(name, dev));
                } else {
                    Log.d(CableScanner.TAG, " unsure of sensor type, adding " + dev.getAddress() + " to deep scan list");
                    CableScanner.this.mDeepScanner.addToScanList(dev);
                }
            }
        }
    };

    public interface ConnectionListener {
        void onConnection(BluetoothGatt bluetoothGatt);

        void onConnectionFailed();
    }

    public interface DiscoveryListener {
        void onDeviceDiscovered(CableDeviceStub cableDeviceStub);
    }

    public static class CableDeviceStub {
        private final BluetoothDevice mDevice;
        private final String mName;

        private CableDeviceStub(String name, BluetoothDevice device) {
            this.mName = name;
            this.mDevice = device;
        }

        public String name() {
            return this.mName;
        }

        public BluetoothDevice device() {
            return this.mDevice;
        }

        public int idFromName() {
            return CableScanner.idFromName(this.mName);
        }

        public boolean isDfuMode() {
            return this.mName.contains("Dfu");
        }

        public boolean equals(CableDeviceStub o) {
            return this.mDevice.equals(o.mDevice);
        }

        public boolean equals(BluetoothDevice o) {
            return this.mDevice.equals(o);
        }

        public String toString() {
            return String.format("%s, (%s)", this.mName, this.mDevice.getAddress());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String serialize() {
            return "CABLE," + this.mDevice.getAddress() + "," + this.mName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static CableDeviceStub deserialize(String params) {
            if (!params.startsWith("CABLE")) {
                return null;
            }
            String[] values = params.split(",");
            try {
                BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(values[1]);
                return new CableDeviceStub(values[2], dev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public CableScanner(Context context) {
        this.mContext = context;
        this.mPrefs = context.getSharedPreferences(PREFERENCES_KEY, 0);
        Set<String> saveList = this.mPrefs.getStringSet(PREFERENCES_KEY, new HashSet());
        for (String saved : saveList) {
            Log.d(TAG, "add to CABLE Cache: " + saved);
            CableDeviceStub stub = CableDeviceStub.deserialize(saved);
            if (stub != null) {
                this.mCableCache.add(stub);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addToCache(CableDeviceStub device) {
        synchronized (this.mCableCache) {
            if (!this.mCableCache.contains(device)) {
                this.mCableCache.add(device);
                HashSet<String> saveList = new HashSet<>(this.mCableCache.size());
                for (CableDeviceStub stub : this.mCableCache) {
                    saveList.add(stub.serialize());
                }
                this.mPrefs.edit().putStringSet(PREFERENCES_KEY, saveList).apply();
            }
        }
    }

    void addToCache(BluetoothDevice dev, String name) {
        if (isCableName(name)) {
            addToCache(new CableDeviceStub(name, dev));
        }
    }

    boolean isKnownCable(BluetoothDevice dev) {
        synchronized (this.mCableCache) {
            for (CableDeviceStub c : this.mCableCache) {
                if (c.equals(dev)) {
                    return true;
                }
            }
            return false;
        }
    }

    CableDeviceStub getCachedCable(BluetoothDevice dev) {
        synchronized (this.mCableCache) {
            for (CableDeviceStub c : this.mCableCache) {
                if (c.equals(dev)) {
                    return c;
                }
            }
            return null;
        }
    }

    public CableDeviceStub find(int searchId, boolean dfuMode) {
        Log.d(TAG, "Looking in cache for CABLE with id: " + searchId + (dfuMode ? " (DFU)" : ""));
        synchronized (this.mCableCache) {
            if (this.mCableCache.isEmpty()) {
                Log.d(TAG, "  CACHE IS EMPTY");
                return null;
            }
            for (CableDeviceStub cached : this.mCableCache) {
                Log.d(TAG, "... " + cached.mName);
                if (cached.isDfuMode() == dfuMode && cached.idFromName() == searchId) {
                    Log.d(TAG, "  found in cache.");
                    return cached;
                }
            }
            return null;
        }
    }

    public void find(final int searchId, boolean dfuMode, final DiscoveryListener cb) {
        Log.d(TAG, "Searching for CABLE with id: " + searchId + (dfuMode ? " (DFU)" : ""));
        synchronized (this.mCableCache) {
            for (CableDeviceStub cached : this.mCableCache) {
                if (cached.isDfuMode() == dfuMode && cached.idFromName() == searchId) {
                    Log.d(TAG, "  found in cache.");
                    cb.onDeviceDiscovered(cached);
                    return;
                }
            }
            this.mDeepScanner = new DeepScanner(this.mContext, new DiscoveryListener() { // from class: com.kopin.solos.sensors.btle.CableScanner.1
                @Override // com.kopin.solos.sensors.btle.CableScanner.DiscoveryListener
                public void onDeviceDiscovered(CableDeviceStub device) {
                    Log.d(CableScanner.TAG, "  Found CABLE: " + device);
                    CableScanner.this.addToCache(device);
                    int id = device.idFromName();
                    if (id != 0 && searchId == id) {
                        cb.onDeviceDiscovered(device);
                        CableScanner.this.stop();
                    }
                }
            });
            this.mScannedList.clear();
            this.mDeepScanner.start();
            this.mScanner.startScan(this.mScanResults);
        }
    }

    public void start(final DiscoveryListener cb) {
        Log.d(TAG, "Searching for CABLE with any id..");
        this.mScannedList.clear();
        this.mDeepScanner = new DeepScanner(this.mContext, new DiscoveryListener() { // from class: com.kopin.solos.sensors.btle.CableScanner.2
            @Override // com.kopin.solos.sensors.btle.CableScanner.DiscoveryListener
            public void onDeviceDiscovered(CableDeviceStub device) {
                Log.d(CableScanner.TAG, "  Found CABLE: " + device);
                CableScanner.this.addToCache(device);
                cb.onDeviceDiscovered(device);
            }
        });
        this.mDeepScanner.start();
        this.mScanner.startScan(this.mScanResults);
    }

    public void stop() {
        if (this.mScanner != null) {
            Log.d(TAG, "Stop scan");
            if (this.mDeepScanner != null) {
                this.mDeepScanner.end();
                this.mDeepScanner = null;
            }
            this.mScanner.stopScan(this.mScanResults);
        }
    }

    void checkDevice(BluetoothDevice dev, String name, final DiscoveryListener cb) {
        if (this.mDeepScanner == null) {
            this.mDeepScanner = new DeepScanner(this.mContext, new DiscoveryListener() { // from class: com.kopin.solos.sensors.btle.CableScanner.3
                @Override // com.kopin.solos.sensors.btle.CableScanner.DiscoveryListener
                public void onDeviceDiscovered(CableDeviceStub device) {
                    Log.d(CableScanner.TAG, "  Found CABLE: " + device);
                    CableScanner.this.addToCache(device);
                    cb.onDeviceDiscovered(device);
                }
            });
        }
        this.mDeepScanner.addToScanList(dev);
        if (!this.mDeepScanner.isScanning) {
            this.mDeepScanner.start();
        }
    }

    static int idFromName(String name) {
        if (name.indexOf(0) > 0) {
            name = name.substring(0, name.indexOf(0));
        }
        String[] s = name.split("#");
        if (s.length <= 1) {
            return 0;
        }
        int id = Integer.parseInt(s[1]);
        return id;
    }

    static boolean isCableName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return name.startsWith("Solos#") || name.startsWith("cable#");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasCableUuid(List<ParcelUuid> serviceUuids) {
        return false;
    }

    private static class DeepScanner extends Thread {
        private boolean isScanning;
        private final Context mContext;
        private final DiscoveryListener mListener;
        private final ArrayList<BluetoothDevice> mDeepScanList = new ArrayList<>();
        private final BluetoothGattCallback mDeepScanCallback = new BluetoothGattCallback() { // from class: com.kopin.solos.sensors.btle.CableScanner.DeepScanner.1
            private final Object DisconnectLock = new Object();

            private void disconnectAndWait(BluetoothGatt gatt) {
                synchronized (this.DisconnectLock) {
                    gatt.disconnect();
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                String name = "NO NAME";
                if (characteristic != null && characteristic.getValue() != null) {
                    name = characteristic.getStringValue(0);
                    if (name.indexOf(0) > 0) {
                        name = name.substring(0, name.indexOf(0));
                    }
                }
                disconnectAndWait(gatt);
                if (DeepScanner.this.hasCableService(gatt.getServices())) {
                    DeepScanner.this.mListener.onDeviceDiscovered(new CableDeviceStub(name, gatt.getDevice()));
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                BluetoothGattCharacteristic name;
                if (status == 0) {
                    BluetoothGattService info = gatt.getService(CableScanner.GATT_SERVICE_GENERIC);
                    if (info == null || (name = info.getCharacteristic(CableScanner.GATT_CHARACTERISTIC_DEVICE_NAME)) == null) {
                        if (DeepScanner.this.hasCableService(gatt.getServices())) {
                            DeepScanner.this.mListener.onDeviceDiscovered(new CableDeviceStub(gatt.getDevice().getName(), gatt.getDevice()));
                        }
                    } else {
                        if (gatt.readCharacteristic(name)) {
                        }
                        return;
                    }
                }
                disconnectAndWait(gatt);
                DeepScanner.this.nextScan();
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (status == 0) {
                    if (newState == 2) {
                        Log.d(CableScanner.TAG, "Connected to sensor: " + gatt.getDevice().getAddress() + " looking for services...");
                        if (!gatt.discoverServices()) {
                            Log.d(CableScanner.TAG, "Unable to discover services on device " + gatt.getDevice().getAddress());
                        } else {
                            return;
                        }
                    } else {
                        Log.d(CableScanner.TAG, "Not connected to sensor: " + gatt.getDevice().getAddress() + " status: " + status + " connect: " + newState);
                        synchronized (this.DisconnectLock) {
                            this.DisconnectLock.notify();
                        }
                    }
                } else {
                    Log.d(CableScanner.TAG, "Couldn't connect to sensor: " + gatt.getDevice().getAddress() + " status: " + status + " connect: " + newState);
                    synchronized (this.DisconnectLock) {
                        this.DisconnectLock.notify();
                    }
                }
                DeepScanner.this.nextScan();
            }
        };

        DeepScanner(Context ctx, DiscoveryListener cb) {
            this.mContext = ctx;
            this.mListener = cb;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToScanList(BluetoothDevice dev) {
            if (!isInList(dev)) {
                synchronized (this.mDeepScanList) {
                    this.mDeepScanList.add(dev);
                    this.mDeepScanList.notify();
                }
            }
        }

        private boolean isInList(BluetoothDevice dev) {
            boolean zContains;
            synchronized (this.mDeepScanList) {
                zContains = this.mDeepScanList.contains(dev);
            }
            return zContains;
        }

        @Override // java.lang.Thread
        public synchronized void start() {
            this.isScanning = true;
            super.start();
        }

        public void end() {
            this.isScanning = false;
            interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!isInterrupted() && this.isScanning) {
                BluetoothDevice dev = null;
                try {
                    synchronized (this.mDeepScanList) {
                        if (this.mDeepScanList.isEmpty()) {
                            this.mDeepScanList.wait();
                        }
                        if (!this.mDeepScanList.isEmpty()) {
                            dev = this.mDeepScanList.remove(0);
                        }
                    }
                    if (dev == null) {
                        continue;
                    } else if (this.isScanning) {
                        if (dev.getName() == null || !dev.getName().startsWith("S")) {
                            Log.d(CableScanner.TAG, "  Device " + dev.getAddress() + " '" + dev.getName() + "' not Solos, skipping deep scan");
                        } else {
                            Log.d(CableScanner.TAG, "   Deep scan, connecting to discover more services from " + dev.getAddress());
                            dev.connectGatt(this.mContext, false, this.mDeepScanCallback);
                            synchronized (this.mDeepScanCallback) {
                                this.mDeepScanCallback.wait();
                            }
                        }
                    } else {
                        return;
                    }
                } catch (InterruptedException e) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void nextScan() {
            synchronized (this.mDeepScanCallback) {
                this.mDeepScanCallback.notify();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasCableService(List<BluetoothGattService> services) {
            Log.d(CableScanner.TAG, "Looking for CABLE services...");
            for (BluetoothGattService s : services) {
                Log.d(CableScanner.TAG, "  " + BTLESensor.getServiceName(s.getUuid()));
                if (s.getUuid().equals(CableScanner.NPE_SERVICE_DFU) || s.getUuid().equals(CableScanner.NPE_SERVICE_CABLE)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class CableDeviceAdapter extends ArrayAdapter<CableDeviceStub> {
        public CableDeviceAdapter(Context context) {
            super(context, R.layout.simple_list_item_1);
        }

        private boolean contains(CableDeviceStub object) {
            for (int i = 0; i < getCount(); i++) {
                CableDeviceStub test = getItem(i);
                if (test.equals(object)) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.widget.ArrayAdapter
        public void add(CableDeviceStub object) {
            if (!contains(object)) {
                super.add(object);
                notifyDataSetChanged();
            }
        }
    }
}
