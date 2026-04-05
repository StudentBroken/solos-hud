package com.kopin.solos.sensors.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.debug.EventLog;
import java.util.HashSet;

/* JADX INFO: loaded from: classes28.dex */
public class LocationHandler implements LocationListener {
    private static final long MIN_DISTANCE_HIGH = 1;
    private static final long MIN_DISTANCE_LOW = 10;
    private static final long MIN_TIME_HIGH = 1000;
    private static final long MIN_TIME_LOW = 20000;
    private static final String TAG = "LocationHandler";
    private Context mContext;
    private Location mDistLocation;
    private float mDistanceFromLastLoc;
    private Float mLastNotifiedAltitude;
    private Location mLocation;
    private LocationManager mLocationManager;
    private volatile boolean mPaused;
    private Location mSplitLocation;
    private volatile boolean mUpdateDistance;
    private float mTotalDistance = 0.0f;
    private float mSplitDistance = 0.0f;
    private HashSet<InternalLocationListener> mListeners = new HashSet<>();
    private Handler mHandler = new Handler();

    public interface InternalLocationListener {
        void onLocationChanged(Location location, double d);
    }

    public LocationHandler(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.mContext = context.getApplicationContext();
    }

    public void prepare(boolean highPrecision) {
        prepare(highPrecision, false);
    }

    public void prepare(final boolean highPrecision, final boolean fakeDataOverride) {
        this.mHandler.post(new Runnable() { // from class: com.kopin.solos.sensors.gps.LocationHandler.1
            @Override // java.lang.Runnable
            public void run() {
                LocationHandler.this.doPrepare(highPrecision, fakeDataOverride);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doPrepare(boolean highPrecision, boolean fakeDataOverride) {
        if (fakeDataOverride) {
            this.mLocationManager.removeUpdates(this);
            this.mLocation = null;
            return;
        }
        if (isEnabled(highPrecision)) {
            boolean sendUpdate = !hasFix();
            if (highPrecision) {
                if (PermissionUtil.permitted(this.mContext, Permission.ACCESS_FINE_LOCATION)) {
                    this.mLocationManager.requestLocationUpdates("gps", 1000L, 1.0f, this);
                    this.mLocation = this.mLocationManager.getLastKnownLocation("gps");
                    if (this.mLocation == null) {
                        this.mLocation = this.mLocationManager.getLastKnownLocation("network");
                    }
                }
            } else if (PermissionUtil.permitted(this.mContext, Permission.ACCESS_COARSE_LOCATION)) {
                this.mLocationManager.requestLocationUpdates("network", MIN_TIME_LOW, 10.0f, this);
                this.mLocation = this.mLocationManager.getLastKnownLocation("network");
            }
            if (sendUpdate && this.mLocation != null) {
                this.mLocation.setSpeed(0.0f);
                if (hasFix()) {
                    for (InternalLocationListener listener : this.mListeners) {
                        listener.onLocationChanged(this.mLocation, 0.0d);
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mLocation = null;
    }

    public void start() {
        if (!this.mUpdateDistance) {
            this.mDistLocation = null;
            this.mSplitLocation = null;
            this.mUpdateDistance = true;
        }
    }

    public void pause() {
        if (this.mUpdateDistance && !this.mPaused) {
            this.mPaused = true;
            this.mDistLocation = null;
            this.mSplitLocation = null;
        }
    }

    public void resume() {
        if (this.mUpdateDistance && this.mPaused) {
            this.mDistLocation = null;
            this.mSplitLocation = null;
            this.mPaused = false;
        }
    }

    public void split() {
        this.mSplitDistance = 0.0f;
        this.mSplitLocation = null;
    }

    public void stop() {
        if (this.mUpdateDistance) {
            this.mUpdateDistance = false;
            this.mPaused = false;
        }
        stopLocation();
    }

    public void reset() {
        this.mUpdateDistance = false;
        this.mPaused = false;
        this.mTotalDistance = 0.0f;
        this.mSplitDistance = 0.0f;
        this.mDistLocation = null;
        this.mSplitLocation = null;
    }

    public void requestDistanceUpdates() {
        EventLog.d(EventLog.ModuleTag.SENSORS, "Switching to GPS for speed data");
        this.mUpdateDistance = true;
        this.mDistLocation = null;
    }

    public void cancelDistanceUpdates() {
        EventLog.d(EventLog.ModuleTag.SENSORS, "Switching to sensors for speed data");
        this.mUpdateDistance = false;
    }

    public void stopLocation() {
        this.mLocationManager.removeUpdates(this);
    }

    public boolean isEnabled(boolean highPrecision) {
        return this.mLocationManager.isProviderEnabled(highPrecision ? "gps" : "network");
    }

    public boolean hasFix() {
        if (this.mLocation == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        long then = this.mLocation.getTime();
        return now - then < MIN_TIME_LOW;
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        EventLog.d(EventLog.ModuleTag.SENSORS, "Location changed (" + location.getProvider() + "): " + location);
        this.mLocation = location;
        this.mDistanceFromLastLoc = 0.0f;
        if (this.mUpdateDistance) {
            if (this.mDistLocation != null) {
                this.mDistanceFromLastLoc = this.mDistLocation.distanceTo(location);
                this.mTotalDistance += this.mDistanceFromLastLoc;
            }
            if (this.mSplitLocation != null) {
                this.mSplitDistance += this.mSplitLocation.distanceTo(location);
            }
            this.mDistLocation = location;
            this.mSplitLocation = location;
        }
        Log.d(TAG, "  Distance from last Loc: " + this.mDistanceFromLastLoc + ", Split distance: " + this.mSplitDistance + ", Total distance: " + this.mTotalDistance);
        EventLog.d(EventLog.ModuleTag.SENSORS, "Got DISTANCE data from GPS, value = " + this.mDistanceFromLastLoc);
        if (location.hasAltitude()) {
            float alt = (float) location.getAltitude();
            if (this.mLastNotifiedAltitude != null && Math.abs(alt - this.mLastNotifiedAltitude.floatValue()) < 3.0f) {
                location.setAltitude(this.mLastNotifiedAltitude.floatValue());
            } else {
                this.mLastNotifiedAltitude = Float.valueOf(alt);
            }
        }
        for (InternalLocationListener listener : this.mListeners) {
            listener.onLocationChanged(location, this.mDistanceFromLastLoc);
        }
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String provider) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String provider) {
    }

    public float getSplitDistance() {
        return this.mSplitDistance;
    }

    public float getTotalDistance() {
        return this.mTotalDistance;
    }

    public void addListener(InternalLocationListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(InternalLocationListener listener) {
        this.mListeners.remove(listener);
    }

    public Location getLocation() {
        return this.mLocation;
    }
}
