package com.kopin.solos.util;

import android.util.Log;
import com.kopin.pupil.SolosDevice;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.sensors.SensorsConnector;

/* JADX INFO: loaded from: classes37.dex */
public class ModeSwitch {
    private static final String TAG = "SolosModeSwitch";

    public static void disableDevices(HardwareReceiverService mService) {
        Log.d(TAG, "disableDevices");
        SensorsConnector.disable();
        SolosDevice.resetAntBridge();
        if (mService != null) {
            mService.getAppService().requestExit();
        }
    }

    public static void enableDevices(HardwareReceiverService mService) {
        Log.d(TAG, "enableDevices");
        SensorsConnector.enable();
        SolosDevice.checkForAntBridge();
        if (mService != null) {
            mService.getAppService().requestStart();
        }
    }
}
