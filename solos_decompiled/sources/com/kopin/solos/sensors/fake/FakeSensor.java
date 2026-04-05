package com.kopin.solos.sensors.fake;

import android.os.Handler;
import com.kopin.solos.sensors.Sensor;

/* JADX INFO: loaded from: classes28.dex */
public class FakeSensor extends Sensor {
    private boolean isConnected;
    private Handler mHandler;
    private long mLastUpdate;
    private final Runnable sendData;
    private int uptime;

    static /* synthetic */ int access$010(FakeSensor x0) {
        int i = x0.uptime;
        x0.uptime = i - 1;
        return i;
    }

    protected FakeSensor(Sensor.SensorObserver obs) {
        super(obs);
        this.sendData = new Runnable() { // from class: com.kopin.solos.sensors.fake.FakeSensor.1
            @Override // java.lang.Runnable
            public void run() {
                double hr = ((int) Math.rint(Math.random() * 50.0d)) + 60;
                if (FakeSensor.this.uptime > 10) {
                    FakeSensor.this.mLastUpdate = System.currentTimeMillis();
                    FakeSensor.this.onNewData(Sensor.SensorType.POWER, Sensor.DataType.POWER, hr);
                }
                FakeSensor.access$010(FakeSensor.this);
                if (FakeSensor.this.uptime == 0) {
                    FakeSensor.this.disconnect();
                }
                if (FakeSensor.this.isConnected) {
                    FakeSensor.this.mHandler.postDelayed(this, 1000L);
                }
            }
        };
        this.mHandler = new Handler();
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean equals(Sensor.SensorType type) {
        return type.equals(Sensor.SensorType.POWER);
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String getName() {
        return "Dummy";
    }

    @Override // com.kopin.solos.sensors.Sensor
    public String getId() {
        return "Dummy";
    }

    @Override // com.kopin.solos.sensors.Sensor
    public Sensor.SensorType getType() {
        return Sensor.SensorType.POWER;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void connect() {
        this.isConnected = true;
        if (this.uptime == 0) {
            this.uptime = 30;
            this.mHandler.postDelayed(this.sendData, 100L);
        }
        this.mLastUpdate = System.currentTimeMillis();
        onSensorAvailable();
        onSensorConnected();
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void disconnect() {
        this.isConnected = false;
        onSensorUnavailable();
        onSensorDisconnected();
    }

    @Override // com.kopin.solos.sensors.Sensor
    public Sensor.ConnectionState getConnectionState() {
        return this.isConnected ? Sensor.ConnectionState.CONNECTED : Sensor.ConnectionState.DISCONNECTED;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean hasConfig() {
        return true;
    }

    void writeConfig(String key, String val) {
        onConfigMessage(true, key + ": " + val);
    }

    String readConfig(String key) {
        return null;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void activate() {
    }

    @Override // com.kopin.solos.sensors.Sensor
    public void deactivate() {
    }

    @Override // com.kopin.solos.sensors.Sensor
    protected long getLastUpdateTime(Sensor.DataType dataType) {
        return this.mLastUpdate;
    }

    @Override // com.kopin.solos.sensors.Sensor
    public boolean isBatteryOk() {
        return true;
    }
}
