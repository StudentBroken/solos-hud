package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import android.content.Context;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.SensorStatus;

/* JADX INFO: loaded from: classes65.dex */
public interface BluetoothClient {

    public interface BluetoothClientListener {
        void onArmour39Measurement(long j, long j2, double d, long j3, long j4);

        void onBatteryLevelMeasurement(long j);

        void onCSCMeasurement(long j, long j2);

        void onConnectionStatusChanged(SensorStatus sensorStatus);

        void onCyclingPowerMeasurement(long j, double d, double d2, long j2, long j3, long j4, long j5, double d3, double d4, long j6, long j7, long j8, long j9, long j10);

        void onHeartRateMeasurement(long j, long j2);

        void onRscMeasurement(double d, long j, double d2, double d3, boolean z);

        void onRssiMeasurement(long j);
    }

    void configure(RecorderContext recorderContext);

    void connect(BluetoothClientListener bluetoothClientListener, String str, Context context);

    void disconnect();

    void startSegment();

    void stopSegment();
}
