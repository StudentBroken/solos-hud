package com.kopin.solos.sensors.fake;

import android.app.Dialog;
import android.content.Context;
import com.kopin.solos.sensors.SensorsConnector;

/* JADX INFO: loaded from: classes28.dex */
public class FakeConfigurator {
    private FakeSensor mDummy;
    private SensorsConnector.ScannerListener mObs;

    public FakeConfigurator(Context context, SensorsConnector.ScannerListener obs) {
        this.mObs = obs;
        this.mDummy = new FakeSensor(obs);
    }

    public void load() {
        this.mObs.onSensorFound(this.mDummy);
    }

    public Dialog configureSensor(Context context, FakeSensor sensor) {
        switch (sensor.getType()) {
            case POWER:
                return FakePowerSensorConfig.configurePowerSensor(context, sensor);
            default:
                return null;
        }
    }
}
