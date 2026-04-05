package com.ua.sdk.recorder.datasource.derived.location;

import com.ua.sdk.UaLog;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataPointImpl;

/* JADX INFO: loaded from: classes65.dex */
public class KalmanFilter {
    public static final double KALMAN_NOISE_VALUE = 500.0d;
    private static boolean jniLoaded;
    private long kalmanFilterPtr = 0;
    private double previousTime = 0.0d;

    protected native long allocNative(double d);

    protected native double getLatNative(long j);

    protected native double getLngNative(long j);

    protected native void releaseNative(long j);

    protected native void updateNative(long j, double d, double d2, double d3);

    static {
        try {
            System.loadLibrary("KalmanFilterJni");
            jniLoaded = true;
        } catch (Throwable throwable) {
            jniLoaded = false;
            UaLog.error("KalmanFilterJni was NOT loaded. Entering graceful fail mode.", throwable);
        }
    }

    public KalmanFilter() {
        init(500.0d);
    }

    public DataPointImpl update(DataPointImpl dataPoint) {
        if (jniLoaded) {
            double durationSec = 0.0d;
            double currentTime = dataPoint.getDatetime().getTime() / 1000.0d;
            if (this.previousTime != 0.0d) {
                durationSec = currentTime - this.previousTime;
            }
            this.previousTime = currentTime;
            double lat = dataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE).doubleValue();
            double lng = dataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE).doubleValue();
            updateNative(this.kalmanFilterPtr, lat, lng, durationSec);
            DataPointImpl answer = new DataPointImpl(dataPoint);
            answer.setValue(BaseDataTypes.FIELD_LATITUDE, Double.valueOf(getLatNative(this.kalmanFilterPtr)));
            answer.setValue(BaseDataTypes.FIELD_LONGITUDE, Double.valueOf(getLngNative(this.kalmanFilterPtr)));
            return answer;
        }
        return new DataPointImpl(dataPoint);
    }

    private void init(double noiseValue) {
        this.kalmanFilterPtr = allocNative(noiseValue);
    }

    protected void finalize() throws Throwable {
        releaseNative(this.kalmanFilterPtr);
        super.finalize();
    }
}
