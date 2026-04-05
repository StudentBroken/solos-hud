package com.kopin.solos.storage;

import android.location.Location;
import com.kopin.solos.sensors.Sensor;

/* JADX INFO: loaded from: classes54.dex */
public abstract class DataListener {
    public void onSpeed(double metresPerSecond) {
    }

    public void onDistance(double metres) {
    }

    public void onCadence(double cadenceRPM) {
    }

    public void onHeartRate(int heartrate) {
    }

    public void onBikePower(double power) {
    }

    public void onAltitude(float value) {
    }

    public void onLocation(Location location) {
    }

    public void onButtonPress(Sensor.ButtonAction action) {
    }

    public void onOxygen(int oxygen) {
    }

    public void onStride(double stride) {
    }
}
