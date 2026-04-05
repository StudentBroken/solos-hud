package com.ua.sdk.recorder.datasource.sensor.location;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.SystemClock;
import com.ua.sdk.UaLog;
import com.ua.sdk.recorder.datasource.RollingAverage;
import com.ua.sdk.recorder.datasource.sensor.location.LocationClient;

/* JADX INFO: loaded from: classes65.dex */
public class AndroidLocationClient implements LocationClient {
    private static final int ACCURACY_MOVING_AVG_COUNT = 3;
    private static final long DURATION_TO_FIX_LOST_MS = 10000;
    public static final float MINIMUM_DISTANCECHANGE_FOR_UPDATE_GPS = 0.0f;
    public static final long MINIMUM_TIME_BETWEEN_UPDATE_GPS = 1000;
    private MyLocationListener androidLocationListener;
    private LocationManager androidLocationManager;
    private HandlerThread handlerThread;
    private LocationClient.LocationClientListener locationClientListener;
    private long serviceStartRealtime = 0;
    private boolean serviceSeenFirstFix = false;
    private long previousLocationRealtime = 0;
    private boolean isRunning = false;
    private RollingAverage<Float> accuracyAccumulator = new RollingAverage<>(3);
    private boolean hasDispatched = false;
    private boolean curGpsEnabled = false;
    private boolean curGpsFix = false;
    private double curAccuracy = 0.0d;
    private boolean nextGpsEnabled = false;
    private boolean nextGpsFix = false;
    private double nextAccuracy = 0.0d;

    public AndroidLocationClient(LocationManager androidLocationManager) {
        this.androidLocationManager = androidLocationManager;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient
    public void connect(LocationClient.LocationClientListener listener) {
        this.locationClientListener = listener;
        this.serviceStartRealtime = SystemClock.elapsedRealtime();
        this.serviceSeenFirstFix = false;
        this.androidLocationListener = new MyLocationListener();
        this.androidLocationManager.requestLocationUpdates("gps", 1000L, 0.0f, this.androidLocationListener);
        this.androidLocationManager.addGpsStatusListener(this.androidLocationListener);
        this.isRunning = true;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient
    public void disconnect() {
        if (this.androidLocationListener != null) {
            this.androidLocationManager.removeUpdates(this.androidLocationListener);
        }
        this.isRunning = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnStatus() {
        if (!this.hasDispatched || this.curGpsEnabled != this.nextGpsEnabled || this.curGpsFix != this.nextGpsFix || this.curAccuracy != this.nextAccuracy) {
            this.hasDispatched = true;
            this.curGpsEnabled = this.nextGpsEnabled;
            this.curGpsFix = this.nextGpsFix;
            this.curAccuracy = this.nextAccuracy;
            this.locationClientListener.onStatus(this.curGpsEnabled, this.curGpsFix, Double.valueOf(this.curAccuracy).floatValue());
        }
    }

    protected class MyLocationListener implements LocationListener, GpsStatus.Listener {
        protected MyLocationListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                UaLog.warn("AndroidLocationClient bad location. location==null");
                return;
            }
            if (location.hasAccuracy() && location.getAccuracy() <= 0.0d) {
                UaLog.warn("AndroidLocationClient bad location. accuracy zero. " + location.toString());
                return;
            }
            if (location.getLatitude() == 0.0d && location.getLongitude() == 0.0d) {
                UaLog.warn("AndroidLocationClient bad location. lat/lng zero. " + location.toString());
                return;
            }
            if (location.getTime() != 0) {
                AndroidLocationClient.this.locationClientListener.onLocation(location);
                AndroidLocationClient.this.previousLocationRealtime = SystemClock.elapsedRealtime();
                if (location.hasAccuracy()) {
                    AndroidLocationClient.this.accuracyAccumulator.addValue(Float.valueOf(location.getAccuracy()));
                    AndroidLocationClient.this.nextAccuracy = AndroidLocationClient.this.accuracyAccumulator.getAverage();
                }
                AndroidLocationClient.this.dispatchOnStatus();
                return;
            }
            UaLog.warn("AndroidLocationClient bad location. time zero. " + location.toString());
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
            UaLog.info("AndroidLocationClient onStatusChanged " + provider + " " + status);
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
            UaLog.warn("AndroidLocationClient onProviderEnabled");
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
            UaLog.warn("AndroidLocationClient onProviderDisabled");
        }

        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int changeType) {
            switch (changeType) {
                case 1:
                    AndroidLocationClient.this.nextGpsEnabled = true;
                    AndroidLocationClient.this.nextGpsFix = false;
                    break;
                case 2:
                    AndroidLocationClient.this.nextGpsEnabled = false;
                    AndroidLocationClient.this.nextGpsFix = false;
                    break;
                case 3:
                    AndroidLocationClient.this.nextGpsEnabled = true;
                    AndroidLocationClient.this.nextGpsFix = true;
                    break;
                case 4:
                    AndroidLocationClient.this.nextGpsEnabled = true;
                    AndroidLocationClient.this.nextGpsFix = SystemClock.elapsedRealtime() - AndroidLocationClient.this.previousLocationRealtime < 10000;
                    break;
                default:
                    UaLog.warn("unknown GpsStatus event type. " + changeType);
                    return;
            }
            AndroidLocationClient.this.nextAccuracy = AndroidLocationClient.this.accuracyAccumulator.getAverage();
            AndroidLocationClient.this.dispatchOnStatus();
        }
    }
}
