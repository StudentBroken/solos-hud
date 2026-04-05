package com.kopin.pupil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import java.util.List;

/* JADX INFO: loaded from: classes43.dex */
public class AriaDevice extends PupilDevice {
    private static final String TAG = "AriaDevice";

    public static void tryAutoConnect(Context ctx, final DeviceLister.ConnectionListener mConnectionListener) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(ctx, new BluetoothProfile.ServiceListener() { // from class: com.kopin.pupil.AriaDevice.1
            @Override // android.bluetooth.BluetoothProfile.ServiceListener
            public void onServiceDisconnected(int profile) {
            }

            @Override // android.bluetooth.BluetoothProfile.ServiceListener
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                List<BluetoothDevice> devices = proxy.getConnectedDevices();
                for (BluetoothDevice dev : devices) {
                    Log.d(AriaDevice.TAG, "headset: " + dev.getAddress());
                }
                if (!devices.isEmpty()) {
                    PupilDevice.tryConnect(devices.get(0), mConnectionListener, false);
                }
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(1, proxy);
            }
        }, 1);
    }

    public static void setOverrideResponseListener(PupilConnector.ResponseListener cb) {
        PupilDevice.setResponseListener(cb);
    }

    public static void sendAudioData(byte[] data, int len, int sampleRate) {
        PupilDevice.sendAudio(data, len, AudioCodec.PCM, sampleRate);
    }

    public static void sendAudioEnd(int id) {
        PupilDevice.sendAudioEnd(id);
    }

    public static void stopAudio() {
        PupilDevice.sendAudioEnd(10);
    }
}
