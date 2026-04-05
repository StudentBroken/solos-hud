package com.kopin.pupil.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import java.lang.reflect.Method;
import java.util.Set;

/* JADX INFO: loaded from: classes25.dex */
public class BluetoothUtilities {
    private static boolean connectAfterBond = false;
    private final Context mContext;
    private final DiscoveryListener mListener;
    private volatile boolean mDiscovery = false;
    private boolean mIsReceiverRegistered = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.bluetooth.BluetoothUtilities.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean bonded;
            bonded = false;
            switch (intent.getAction()) {
                case "android.bluetooth.device.action.FOUND":
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    BluetoothUtilities.this.mListener.onDeviceDiscovered(device);
                    return;
                case "android.bluetooth.device.action.BOND_STATE_CHANGED":
                    if (intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", 10) != 12) {
                        bonded = false;
                        break;
                    } else {
                        bonded = true;
                        break;
                    }
                    break;
                case "android.bluetooth.adapter.action.STATE_CHANGED":
                case "android.bluetooth.device.action.NAME_CHANGED":
                    break;
                case "android.bluetooth.adapter.action.DISCOVERY_FINISHED":
                    if (BluetoothUtilities.this.mDiscovery) {
                        BluetoothUtilities.this.mBluetoothAdapter.startDiscovery();
                        return;
                    }
                    return;
                default:
                    return;
            }
            BluetoothDevice device2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            BluetoothUtilities.this.mListener.onDeviceStateChanged(device2, BluetoothUtilities.connectAfterBond && bonded);
        }
    };
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public interface DiscoveryListener {
        void onDeviceDiscovered(BluetoothDevice bluetoothDevice);

        void onDeviceStateChanged(BluetoothDevice bluetoothDevice, boolean z);
    }

    public BluetoothUtilities(Context context, DiscoveryListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public boolean isBluetoothEnabled() {
        return this.mBluetoothAdapter.isEnabled();
    }

    public void register() {
        if (!this.mIsReceiverRegistered) {
            IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED");
            filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
            filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            filter.addAction("android.bluetooth.device.action.FOUND");
            filter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
            filter.addAction("android.bluetooth.device.action.NAME_CHANGED");
            filter.addAction("android.bluetooth.device.action.UUID");
            this.mContext.registerReceiver(this.mReceiver, filter);
            this.mIsReceiverRegistered = true;
        }
    }

    public void unregister() {
        if (this.mIsReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mIsReceiverRegistered = false;
        }
    }

    public void start() {
        this.mDiscovery = true;
        Set<BluetoothDevice> known = this.mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice dev : known) {
            this.mListener.onDeviceDiscovered(dev);
        }
        this.mBluetoothAdapter.startDiscovery();
    }

    public void stop() {
        this.mDiscovery = false;
        this.mBluetoothAdapter.cancelDiscovery();
    }

    @SuppressLint({"NewApi"})
    public static boolean pairDevice(BluetoothDevice device, boolean andConnect) {
        connectAfterBond = andConnect;
        if (Build.VERSION.SDK_INT <= 18) {
            try {
                Method method = device.getClass().getMethod("createBond", (Class[]) null);
                return ((Boolean) method.invoke(device, (Object[]) null)).booleanValue();
            } catch (Exception e) {
                return false;
            }
        }
        return device.createBond();
    }
}
