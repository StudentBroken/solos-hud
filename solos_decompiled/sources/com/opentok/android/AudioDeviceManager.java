package com.opentok.android;

import android.content.Context;
import com.opentok.android.BaseAudioDevice;

/* JADX INFO: loaded from: classes15.dex */
public class AudioDeviceManager {
    static BaseAudioDevice defaultAudioDevice;

    public static void setAudioDevice(BaseAudioDevice device) throws IllegalStateException {
        if (device != defaultAudioDevice) {
            BaseAudioDevice old = null;
            if (defaultAudioDevice != null) {
                if (defaultAudioDevice.isActive()) {
                    throw new IllegalStateException("AudioDevice can only be changed before initialization.");
                }
                old = defaultAudioDevice;
            }
            if (device != null) {
                defaultAudioDevice = device;
                defaultAudioDevice.initNative();
                defaultAudioDevice.setAudioBus(new BaseAudioDevice.AudioBus(defaultAudioDevice));
            } else {
                defaultAudioDevice = null;
            }
            if (old != null) {
                old.destroyNative();
            }
        }
    }

    public static BaseAudioDevice getAudioDevice() {
        return defaultAudioDevice;
    }

    static void initializeDefaultDevice(Context context) {
        if (defaultAudioDevice == null) {
            OtLog.d("AUDIO_DEVICE creating default device", new Object[0]);
            defaultAudioDevice = new DefaultAudioDevice(context);
            defaultAudioDevice.initNative();
        }
        if (defaultAudioDevice.getAudioBus() == null) {
            defaultAudioDevice.setAudioBus(new BaseAudioDevice.AudioBus(defaultAudioDevice));
        }
    }
}
