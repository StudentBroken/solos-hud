package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.kopin.pupil.aria.app.TimedAppState;
import com.ua.sdk.UaLog;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.SensorStatus;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes65.dex */
@TargetApi(18)
public class BluetoothConnection implements BluetoothClient {
    private static final int SCAN_RETRY_COUNT_MAX = 3;
    private BaseGattCallback baseGattCallback;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothManager bluetoothManager;
    private Context context;
    private String deviceAddress;
    protected BluetoothClient.BluetoothClientListener listener;
    private MyBluetoothStateReceiver myBluetoothStateReceiver;
    private MyLeScanCallbacks myLeScanCallbacks;
    private Timer timer;
    private MyStopScanRunnable myStopScanRunnable = new MyStopScanRunnable();
    private int retryCount = 0;
    private Handler handler = new Handler();

    static /* synthetic */ int access$308(BluetoothConnection x0) {
        int i = x0.retryCount;
        x0.retryCount = i + 1;
        return i;
    }

    public BluetoothConnection(BaseGattCallback baseGattCallback) {
        this.myLeScanCallbacks = new MyLeScanCallbacks();
        this.myBluetoothStateReceiver = new MyBluetoothStateReceiver();
        this.baseGattCallback = baseGattCallback;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient
    public void connect(BluetoothClient.BluetoothClientListener listener, String deviceAddress, Context context) {
        this.listener = listener;
        this.deviceAddress = deviceAddress;
        this.context = context;
        this.timer = new Timer("BluetoothClientTimer");
        this.baseGattCallback.setClientListener(listener);
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        context.registerReceiver(this.myBluetoothStateReceiver, intentFilter);
        attemptConnect();
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient
    public void configure(RecorderContext recorderContext) {
        if (this.bluetoothManager == null) {
            this.bluetoothManager = (BluetoothManager) recorderContext.getApplicationContext().getSystemService("bluetooth");
        }
        this.baseGattCallback.configure(recorderContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptConnect() {
        if (this.listener != null) {
            this.listener.onConnectionStatusChanged(SensorStatus.CONNECTING);
        }
        boolean initialized = initializeAdapter();
        if (initialized) {
            startScan();
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient
    public void disconnect() {
        UaLog.debug("Client has disconnected from device");
        this.baseGattCallback.disconnect();
        if (this.timer != null) {
            this.handler.removeCallbacks(this.myStopScanRunnable);
            stopScan();
            this.timer.cancel();
        }
        this.context.unregisterReceiver(this.myBluetoothStateReceiver);
        if (this.bluetoothGatt != null) {
            this.bluetoothGatt.disconnect();
            this.bluetoothGatt.close();
            this.bluetoothGatt = null;
        }
        if (this.listener != null) {
            this.listener.onConnectionStatusChanged(SensorStatus.DISCONNECTED);
        }
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient
    public void startSegment() {
        this.baseGattCallback.startSegment();
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient
    public void stopSegment() {
    }

    private boolean initializeAdapter() {
        this.bluetoothAdapter = this.bluetoothManager.getAdapter();
        if (this.bluetoothAdapter != null) {
            return true;
        }
        UaLog.error("Unable to get the bluetooth adapter.");
        this.listener.onConnectionStatusChanged(SensorStatus.DISCONNECTED);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startScan() {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.isEnabled()) {
            this.bluetoothAdapter.startLeScan(this.myLeScanCallbacks);
            this.handler.postDelayed(this.myStopScanRunnable, TimedAppState.DEFAULT_CONFIRM_TIMEOUT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopScan() {
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter.stopLeScan(this.myLeScanCallbacks);
        }
    }

    private class MyLeScanCallbacks implements BluetoothAdapter.LeScanCallback {
        private MyLeScanCallbacks() {
        }

        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getAddress().equals(BluetoothConnection.this.deviceAddress)) {
                UaLog.debug("Device found! Connecting to server the server now");
                BluetoothConnection.this.retryCount = 0;
                BluetoothConnection.this.handler.removeCallbacks(BluetoothConnection.this.myStopScanRunnable);
                BluetoothConnection.this.stopScan();
                if (BluetoothConnection.this.bluetoothGatt != null) {
                    BluetoothConnection.this.bluetoothGatt.disconnect();
                    BluetoothConnection.this.bluetoothGatt.close();
                    BluetoothConnection.this.bluetoothGatt = null;
                }
                BluetoothConnection.this.baseGattCallback.setConnectionLostListener(new MyConnectionLostListener());
                new Thread(new Runnable() { // from class: com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothConnection.MyLeScanCallbacks.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothConnection.this.bluetoothGatt = device.connectGatt(BluetoothConnection.this.context, false, BluetoothConnection.this.baseGattCallback);
                    }
                }).start();
            }
        }
    }

    protected class MyStopScanRunnable extends TimerTask {
        protected MyStopScanRunnable() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            BluetoothConnection.this.stopScan();
            UaLog.error("Unable to find device with address " + BluetoothConnection.this.deviceAddress);
            BluetoothConnection.access$308(BluetoothConnection.this);
            if (BluetoothConnection.this.retryCount <= 3) {
                UaLog.error("retry scan number " + BluetoothConnection.this.retryCount);
                BluetoothConnection.this.startScan();
            } else {
                BluetoothConnection.this.retryCount = 0;
                UaLog.debug("we will attempt to reconnect in 60 seconds");
                BluetoothConnection.this.timer.schedule(BluetoothConnection.this.new MyReconnectTask(), 60000L);
            }
        }
    }

    protected class MyReconnectTask extends TimerTask {
        protected MyReconnectTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            BluetoothConnection.this.stopScan();
            BluetoothConnection.this.attemptConnect();
        }
    }

    private class MyConnectionLostListener implements BaseGattCallback.ConnectionLostListener {
        private MyConnectionLostListener() {
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BaseGattCallback.ConnectionLostListener
        public void onScheduleReconnect(long delay) {
            BluetoothConnection.this.timer.schedule(BluetoothConnection.this.new MyReconnectTask(), delay);
        }
    }

    private class MyBluetoothStateReceiver extends BroadcastReceiver {
        private MyBluetoothStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int newState = intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE");
                if (newState == 12) {
                    BluetoothConnection.this.startScan();
                }
            }
        }
    }
}
