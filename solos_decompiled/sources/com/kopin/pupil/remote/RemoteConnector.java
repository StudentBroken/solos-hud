package com.kopin.pupil.remote;

import android.util.Log;
import com.kopin.accessory.base.Connection;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/* JADX INFO: loaded from: classes25.dex */
public class RemoteConnector extends PupilConnector {
    private static final short PORT = 5894;
    private static final String TAG = "RemoteConnector";
    private InetSocketAddress mRemoteAddr;
    private Socket mSocket;

    public RemoteConnector(InetAddress device, DeviceLister.ConnectionListener listener) {
        super(TAG, listener);
        this.mSocket = new Socket();
        this.mRemoteAddr = new InetSocketAddress(device, 5894);
    }

    @Override // com.kopin.pupil.PupilConnector
    protected boolean doReconnect() {
        try {
            if (this.mSocket != null && !this.mSocket.isClosed()) {
                this.mSocket.close();
            }
            this.mSocket = new Socket();
            this.mSocket.connect(this.mRemoteAddr);
            this.mConnection.clearQueuedPackets();
            this.mConnection.reopen(this.mSocket.getInputStream(), this.mSocket.getOutputStream());
            this.isReconnecting = false;
            getDefaultCaps();
            if (this.mResponseListener != null) {
                this.mResponseListener.onReconnection();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public InetAddress getDevice() {
        if (this.mSocket == null || !this.mSocket.isConnected()) {
            return null;
        }
        return this.mRemoteAddr.getAddress();
    }

    @Override // com.kopin.pupil.PupilConnector
    protected Connection doConnect() throws IOException {
        this.mSocket.connect(this.mRemoteAddr);
        return new Connection(this.mSocket.getInputStream(), this.mSocket.getOutputStream());
    }

    @Override // com.kopin.pupil.PupilConnector
    protected void doClose() {
        try {
            this.mSocket.close();
        } catch (IOException closeException) {
            Log.e(TAG, "", closeException);
        }
    }
}
