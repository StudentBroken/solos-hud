package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import com.ua.sdk.recorder.SensorHealth;
import com.ua.sdk.recorder.datasource.sensor.DeviceHealth;

/* JADX INFO: loaded from: classes65.dex */
public class BluetoothDeviceHealth extends DeviceHealth {
    private int batteryRemaining;
    private int rssi;

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setBatteryRemaining(int batteryRemaining) {
        this.batteryRemaining = batteryRemaining;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.DeviceHealth
    public SensorHealth calculateOverallHealth() {
        SensorHealth health;
        int signalQuality = calculateRssi();
        if (signalQuality >= 70) {
            health = SensorHealth.VERY_GOOD;
        } else if (signalQuality >= 50) {
            health = SensorHealth.GOOD;
        } else if (signalQuality >= 30) {
            health = SensorHealth.AVERAGE;
        } else if (signalQuality >= 20) {
            health = SensorHealth.BAD;
        } else {
            health = SensorHealth.VERY_BAD;
        }
        if (this.batteryRemaining <= 25) {
            int ordinal = health.ordinal();
            if (ordinal < 4) {
                ordinal++;
            }
            if (this.batteryRemaining <= 10 && ordinal < 4) {
                ordinal++;
            }
            SensorHealth health2 = SensorHealth.values()[ordinal];
            return health2;
        }
        return health;
    }

    private int calculateRssi() {
        if (this.rssi <= -100) {
            return 0;
        }
        if (this.rssi >= -50) {
            return 100;
        }
        return (this.rssi + 100) * 2;
    }
}
