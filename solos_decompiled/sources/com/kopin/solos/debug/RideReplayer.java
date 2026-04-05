package com.kopin.solos.debug;

import android.location.Location;
import com.kopin.solos.RideControl;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.RouteBuilder;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
import java.util.HashMap;

/* JADX INFO: loaded from: classes37.dex */
public class RideReplayer {
    private static final String TAG = "RideReplayer";
    private static SensorsConnector.SensorDataSink mDataSink;
    private static RidePlaybackThread mReplayThread;
    private static long mRideId = -1;
    public static final HashMap<Record.MetricData, String> RECORD_FIELD_MAP = new HashMap<>();

    static {
        RECORD_FIELD_MAP.put(Record.MetricData.DISTANCE, Record.MetricData.DISTANCE.name());
        RECORD_FIELD_MAP.put(Record.MetricData.SPEED, Record.MetricData.SPEED.name());
        RECORD_FIELD_MAP.put(Record.MetricData.CADENCE, Record.MetricData.CADENCE.name());
        RECORD_FIELD_MAP.put(Record.MetricData.HEARTRATE, Record.MetricData.HEARTRATE.name());
        RECORD_FIELD_MAP.put(Record.MetricData.POWER, Record.MetricData.POWER.name());
        RECORD_FIELD_MAP.put(Record.MetricData.OXYGEN, Record.MetricData.OXYGEN.name());
        RECORD_FIELD_MAP.put(Record.MetricData.CORRECTED_ALTITUDE, Record.MetricData.CORRECTED_ALTITUDE.name());
    }

    public static void init(SensorsConnector.SensorDataSink sink) {
        mDataSink = sink;
    }

    public static void selectRide(long rideId) {
        mRideId = rideId;
        long routeId = Route.getRouteIdForRide(mRideId);
        if (routeId != -1) {
            selectRoute(routeId);
        }
    }

    public static void selectRoute(long routeId) {
        if (Route.isNavRoute(routeId)) {
            NavigationRoute route = RouteBuilder.load(routeId);
            Navigator.setRoute(route, route.getStartPoint());
        }
    }

    public static boolean ready() {
        return (mRideId == -1 || mDataSink == null) ? false : true;
    }

    public static void start() {
        stop();
        if (mRideId != -1) {
            mReplayThread = new RidePlaybackThread(mRideId);
        }
        if (mReplayThread != null) {
            mReplayThread.start();
        }
    }

    public static void stop() {
        if (mReplayThread != null) {
            mReplayThread.end();
        }
        mReplayThread = null;
    }

    public static boolean isActive() {
        return mReplayThread != null && mReplayThread.isAlive();
    }

    private static class RidePlaybackThread extends Thread implements SavedWorkout.ForeachExtendedRecordCallback {
        private boolean mCancel;
        private long mStartTime;
        private SavedWorkout mWorkout;
        private final long mWorkoutId;

        RidePlaybackThread(long rideId) {
            super("Ride Playback");
            this.mCancel = false;
            this.mWorkoutId = rideId;
        }

        @Override // java.lang.Thread
        public synchronized void start() {
            this.mWorkout = SavedRides.getWorkout(LiveRide.getCurrentSport(), this.mWorkoutId);
            super.start();
        }

        public synchronized void end() {
            this.mCancel = true;
            interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.mStartTime = System.currentTimeMillis();
            RideControl.ready(true);
            this.mWorkout.foreachExtendedRecord(RideReplayer.RECORD_FIELD_MAP, this);
            RideControl.stop(true);
        }

        private long curTime() {
            return System.currentTimeMillis() - this.mStartTime;
        }

        @Override // com.kopin.solos.storage.SavedWorkout.ForeachExtendedRecordCallback
        public boolean onRecordValues(long timestamp, HashMap<String, Number> values, Coordinate position) {
            long now = curTime();
            if (timestamp > now) {
                synchronized (this) {
                    try {
                        wait(timestamp - now);
                    } catch (InterruptedException e) {
                        this.mCancel = true;
                        return false;
                    }
                }
            }
            if (position.hasData()) {
                Location loc = position.getLocation(false);
                if (values.containsKey(Record.MetricData.CORRECTED_ALTITUDE)) {
                    loc.setAltitude(values.get(Record.MetricData.CORRECTED_ALTITUDE).doubleValue());
                }
                RideReplayer.mDataSink.onLocation(loc);
            }
            for (String metric : values.keySet()) {
                Number val = values.get(metric);
                if (metric.equals(Record.MetricData.SPEED.name())) {
                    RideReplayer.mDataSink.onSpeed(val.doubleValue());
                } else if (metric.equals(Record.MetricData.DISTANCE.name())) {
                    RideReplayer.mDataSink.onDistance(val.doubleValue());
                } else if (metric.equals(Record.MetricData.CADENCE.name())) {
                    RideReplayer.mDataSink.onCadence(val.doubleValue());
                } else if (metric.equals(Record.MetricData.HEARTRATE.name())) {
                    RideReplayer.mDataSink.onHeartRate(val.intValue());
                } else if (metric.equals(Record.MetricData.POWER.name())) {
                    RideReplayer.mDataSink.onBikePower(val.doubleValue());
                } else if (metric.equals(Record.MetricData.OXYGEN.name())) {
                    RideReplayer.mDataSink.onOxygen(val.intValue());
                } else if (metric.equals(Record.MetricData.STRIDE.name())) {
                    RideReplayer.mDataSink.onStride(val.doubleValue());
                }
            }
            return !this.mCancel;
        }
    }
}
