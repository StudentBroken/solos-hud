package com.kopin.pupil.diags;

import android.content.Context;
import com.kopin.pupil.PupilDevice;

/* JADX INFO: loaded from: classes25.dex */
public class Diagnostics {

    public static class BatteryLog {
        public static void start(Context context) {
            BatteryLogger.startLog(context);
        }

        public static String stop() {
            return BatteryLogger.stopLog();
        }

        public static boolean isActive() {
            return BatteryLogger.isActive();
        }
    }

    public static void onDeviceStatusChanged(PupilDevice.DeviceStatus status) {
        if (BatteryLogger.isActive() && status.hasBattery()) {
            int level = status.getBattery();
            BatteryLogger.writeEntry(level);
        }
    }
}
