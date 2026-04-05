package com.kopin.pupil.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.kopin.accessory.base.Connection;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import java.io.IOException;
import java.util.UUID;

/* JADX INFO: loaded from: classes25.dex */
public class BluetoothConnector extends PupilConnector {
    public static final UUID SERVICE_UUID = UUID.fromString("000011A0-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BluetoothConnector";
    private BluetoothSocket mSocket;

    public BluetoothConnector(BluetoothDevice device, DeviceLister.ConnectionListener listener) throws IOException {
        super(TAG, listener);
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(SERVICE_UUID);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        this.mSocket = tmp;
    }

    @Override // com.kopin.pupil.PupilConnector
    protected boolean doReconnect() {
        Log.d(TAG, "Attempting reconnect...");
        if (this.mSocket == null) {
            Log.e(TAG, "  No socket (is Bluetooth off?)");
            return false;
        }
        BluetoothDevice remote = this.mSocket.getRemoteDevice();
        try {
            this.mSocket.close();
            Log.d(TAG, " old socket closed");
            this.mSocket = remote.createRfcommSocketToServiceRecord(SERVICE_UUID);
            if (this.mSocket == null) {
                Log.e(TAG, "  No socket (is Bluetooth off?)");
                return false;
            }
            Log.d(TAG, " new socket created");
            this.mSocket.connect();
            Log.d(TAG, " new socket connected");
            this.mConnection.clearQueuedPackets();
            this.mConnection.reopen(this.mSocket.getInputStream(), this.mSocket.getOutputStream());
            this.isReconnecting = false;
            if (this.mResponseListener != null) {
                this.mResponseListener.onReconnection();
            }
            Log.d(TAG, " reconnected.");
            return true;
        } catch (IOException ioe) {
            Log.d(TAG, "IO Exception", ioe);
            Log.d(TAG, " reconnect failed.");
            return false;
        }
    }

    public BluetoothDevice getDevice() {
        if (this.mSocket != null) {
            return this.mSocket.getRemoteDevice();
        }
        return null;
    }

    @Override // com.kopin.pupil.PupilConnector
    protected Connection doConnect() throws IOException {
        Log.d(TAG, "Attempting connect...");
        this.mSocket.connect();
        return new Connection(this.mSocket.getInputStream(), this.mSocket.getOutputStream());
    }

    @Override // com.kopin.pupil.PupilConnector
    protected void doClose() {
        try {
            if (this.mSocket != null) {
                this.mSocket.close();
            }
        } catch (IOException closeException) {
            Log.e(TAG, "", closeException);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        doClose();
    }
}
