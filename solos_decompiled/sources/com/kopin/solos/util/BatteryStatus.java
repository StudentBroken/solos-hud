package com.kopin.solos.util;

import android.util.Log;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;

/* JADX INFO: loaded from: classes37.dex */
public class BatteryStatus {
    private static final int BATTERY_CRITICAL_TIME = 60000;
    private static final int BATTERY_THRESHOLD_CRITICAL = 10;
    private static final int BATTERY_THRESHOLD_LOW = 20;
    private static final int BATTERY_THRESHOLD_MEDIUM = 40;
    private static final String TAG = "BatteryStatus";
    private final AppService mApp;
    private long warnCritical = 0;
    private BatteryWarning lastWarning = BatteryWarning.NONE;

    private enum BatteryWarning {
        NONE,
        CRITICAL(R.string.battery_level_critical),
        LOW(R.string.battery_level_20),
        MEDIUM(R.string.battery_level_40);

        int mWarningRes;

        BatteryWarning() {
            this.mWarningRes = 0;
        }

        BatteryWarning(int warningRes) {
            this.mWarningRes = 0;
            this.mWarningRes = warningRes;
        }

        public int getWarningRes() {
            return this.mWarningRes;
        }
    }

    public BatteryStatus(AppService app) {
        this.mApp = app;
    }

    public void updateStatus(PupilDevice.DeviceStatus status) {
        BatteryWarning warning = getBatteryWarning(status);
        if (warning != BatteryWarning.NONE) {
            this.mApp.sendDropDownNotification(AppService.PUPIL_IMG, 5000, this.mApp.getString(warning.getWarningRes()));
        }
    }

    public void reset() {
        this.warnCritical = 0L;
        this.lastWarning = BatteryWarning.NONE;
    }

    private BatteryWarning getBatteryWarning(PupilDevice.DeviceStatus deviceStatus) {
        if (deviceStatus.isOnCharge()) {
            reset();
        } else if (deviceStatus.hasBattery()) {
            BatteryWarning batteryCategory = getBatteryCategory(deviceStatus.getBattery());
            if (batteryCategory == BatteryWarning.CRITICAL) {
                if (System.currentTimeMillis() > this.warnCritical + 60000) {
                    this.warnCritical = System.currentTimeMillis();
                    this.lastWarning = batteryCategory;
                    Log.d(TAG, "battery " + batteryCategory.name());
                    return batteryCategory;
                }
            } else if (this.lastWarning != batteryCategory) {
                this.lastWarning = batteryCategory;
                Log.d(TAG, "battery " + batteryCategory.name());
                return batteryCategory;
            }
        }
        return BatteryWarning.NONE;
    }

    private BatteryWarning getBatteryCategory(int battery) {
        if (battery <= 10) {
            return BatteryWarning.CRITICAL;
        }
        if (battery <= 20) {
            return BatteryWarning.LOW;
        }
        if (battery <= 40) {
            return BatteryWarning.MEDIUM;
        }
        return BatteryWarning.NONE;
    }
}
