package com.kopin.solos.util;

import android.animation.ValueAnimator;
import android.location.Location;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.kopin.solos.config.AppConfig;
import com.kopin.solos.metrics.MetricResource;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigate.helperclasses.CoordCalc;
import com.kopin.solos.navigate.helperclasses.LocationHelper;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.gps.LocationHandler;
import com.kopin.solos.share.Config;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes37.dex */
public class FakeRideData {
    private static final int ANIMATION_DURATION = 34000;
    private static final int ANIMATION_START_DELAY = 600;
    private static final int MIN_TIME_UPDATES = 750;
    private static boolean USE_FAKE_ARRAYS = true;
    private static MetricResource[] rideSensorKeys = {MetricResource.CADENCE, MetricResource.ELEVATION, MetricResource.HEARTRATE, MetricResource.OXYGENATION, MetricResource.POWER, MetricResource.SPEED};
    private static int[] rideSensorMins = {75, 606, 142, 77, 201, 3};
    private static int[] rideSensorMaxs = {95, 618, 158, 88, 240, 4};
    private static MetricResource[] runSensorKeys = {MetricResource.CADENCE, MetricResource.ELEVATION, MetricResource.HEARTRATE, MetricResource.OXYGENATION, MetricResource.POWER, MetricResource.STRIDE, MetricResource.SPEED};
    private static int[] runSensorMins = {150, 606, 140, 77, 202, 165, 2};
    private static int[] runSensorMaxs = {180, 618, 160, 88, 238, 175, 3};
    private static volatile boolean continueRide = false;
    private static FakeRouteFollower fakeRoute = null;
    private static double lastElevation = 610.0d;
    private static List<ValueAnimator> animators = new ArrayList();
    private static Map<MetricResource, Long> animatorsLastUpdate = new HashMap();
    private static long[] tripTimes = {0, 120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200, 1320, 1440, 1560, 1680, 1800, 1920, 2040, 2160, 2280, 2400, 2520, 2640, 2720, 2880, 3000};
    private static double[] tripLats = {36.114748d, 36.120711d, 36.126396d, 36.131942d, 36.137903d, 36.143725d, 36.14927d, 36.154953d, 36.158834d, 36.158973d, 36.158973d, 36.158834d, 36.156062d, 36.149409d, 36.143032d, 36.136883d, 36.13721d, 36.130555d, 36.124177d, 36.11766d, 36.114748d, 36.114609d, 36.114609d, 36.114609d, 36.114609d, 36.114609d};
    private static double[] tripLongs = {-115.172977d, -115.172291d, -115.168514d, -115.164909d, -115.160961d, -115.157013d, -115.153751d, -115.149975d, -115.146713d, -115.138474d, -115.128517d, -115.121651d, -115.118732d, -115.118732d, -115.118732d, -115.118732d, -115.118732d, -115.118732d, -115.118732d, -115.118732d, -115.121822d, -115.129375d, -115.13813d, -115.147057d, -115.153923d, -115.163879d};

    public static void startRandom(SensorsConnector.SensorDataSink mSensorsDataSink, SplitHelper mSplitHelper, final LocationHandler locationHandler, boolean isRide) {
        continueRide = true;
        if (isRide) {
            startAnimators(mSensorsDataSink, rideSensorKeys, rideSensorMins, rideSensorMaxs);
        } else {
            startAnimators(mSensorsDataSink, runSensorKeys, runSensorMins, runSensorMaxs);
        }
        new Thread(new Runnable() { // from class: com.kopin.solos.util.FakeRideData.1
            @Override // java.lang.Runnable
            public void run() {
                Utility.getTimeMilliseconds();
                long then2 = Utility.getTimeMilliseconds();
                Random r = new Random();
                int i = 0;
                int lastPoint = 0;
                int current = 0;
                double lat = 0.0d;
                double lon = 0.0d;
                while (FakeRideData.continueRide) {
                    try {
                        Thread.sleep(990L);
                    } catch (InterruptedException e) {
                    }
                    if (Config.FAKE_DATA && FakeRideData.USE_FAKE_ARRAYS) {
                        long now = Utility.getTimeMilliseconds();
                        long time = (now - then2) / 1000;
                        float randomSpeed = 14.0f + (11.0f * Math.abs((-1.0f) + r.nextFloat() + r.nextFloat()));
                        if (FakeRideData.fakeRoute == null) {
                            lastPoint = i;
                            if (time > FakeRideData.tripTimes[i]) {
                                lat = FakeRideData.tripLats[lastPoint];
                                lon = FakeRideData.tripLongs[lastPoint];
                                i++;
                                if (i >= FakeRideData.tripTimes.length) {
                                    i = 0;
                                    then2 = now;
                                }
                            } else if (lastPoint > 0 && time < FakeRideData.tripTimes[lastPoint] && time > FakeRideData.tripTimes[lastPoint - 1]) {
                                double lat2 = FakeRideData.tripLats[lastPoint - 1];
                                double lon2 = FakeRideData.tripLongs[lastPoint - 1];
                                double nextLat = FakeRideData.tripLats[lastPoint];
                                double nextLon = FakeRideData.tripLongs[lastPoint];
                                long diff = FakeRideData.tripTimes[lastPoint] - FakeRideData.tripTimes[lastPoint - 1];
                                double percent = ((time - FakeRideData.tripTimes[lastPoint - 1]) * 100) / diff;
                                lat = lat2 + ((nextLat - lat2) * (percent / 100.0d));
                                lon = lon2 + ((nextLon - lon2) * (percent / 100.0d));
                                current = -1;
                            }
                            if (lastPoint == 0 || lastPoint != current) {
                                Location location = new Location("");
                                location.setLatitude(lat);
                                location.setLongitude(lon);
                                location.setAltitude(FakeRideData.lastElevation);
                                locationHandler.onLocationChanged(location);
                            }
                        } else {
                            then2 = now;
                            Location location2 = FakeRideData.fakeRoute.onDistance(time * randomSpeed);
                            location2.setAltitude(FakeRideData.lastElevation);
                            locationHandler.onLocationChanged(location2);
                        }
                        current = lastPoint;
                    }
                }
            }
        }).start();
    }

    public static void stop() {
        continueRide = false;
        for (ValueAnimator valueAnimator : animators) {
            if (valueAnimator != null) {
                valueAnimator.end();
            }
        }
        animators.clear();
    }

    private static void startAnimators(final SensorsConnector.SensorDataSink mSensorsDataSink, MetricResource[] sensorKeys, int[] sensorMins, int[] sensorMaxs) {
        if (animators.isEmpty()) {
            int i = 0;
            int length = sensorKeys.length;
            for (int i2 = 0; i2 < length; i2++) {
                final MetricResource metric = sensorKeys[i2];
                animatorsLastUpdate.put(metric, 0L);
                final ValueAnimator valueAnim = metric == MetricResource.HEARTRATE ? ValueAnimator.ofInt(sensorMins[i], sensorMaxs[i]) : ValueAnimator.ofFloat(sensorMins[i], sensorMaxs[i]);
                valueAnim.setDuration(34000L);
                valueAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnim.setRepeatMode(2);
                valueAnim.setRepeatCount(-1);
                valueAnim.setStartDelay(600L);
                valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.kopin.solos.util.FakeRideData.2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float revsPerSec;
                        if (FakeRideData.continueRide && ((Long) FakeRideData.animatorsLastUpdate.get(metric)).longValue() < System.currentTimeMillis() - 750) {
                            FakeRideData.animatorsLastUpdate.put(metric, Long.valueOf(System.currentTimeMillis()));
                            switch (AnonymousClass3.$SwitchMap$com$kopin$solos$metrics$MetricResource[metric.ordinal()]) {
                                case 1:
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.CADENCE) || SensorsConnector.isSupportedType(Sensor.SensorType.RUNNING_SPEED_CADENCE)) {
                                        Double d = AppConfig.getValue(MetricResource.CADENCE);
                                        mSensorsDataSink.onCadence(d != null ? d.doubleValue() : ((Float) valueAnim.getAnimatedValue()).floatValue());
                                    }
                                    break;
                                case 2:
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.HEARTRATE)) {
                                        Double d2 = AppConfig.getValue(MetricResource.HEARTRATE);
                                        mSensorsDataSink.onHeartRate(d2 != null ? d2.intValue() : ((Integer) valueAnim.getAnimatedValue()).intValue());
                                    }
                                    break;
                                case 3:
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.OXYGEN)) {
                                        Double d3 = AppConfig.getValue(MetricResource.OXYGENATION);
                                        mSensorsDataSink.onOxygen(d3 != null ? d3.intValue() : ((Integer) valueAnim.getAnimatedValue()).intValue());
                                    }
                                    break;
                                case 4:
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.POWER) || SensorsConnector.isSupportedType(Sensor.SensorType.FOOT_POD)) {
                                        Double d4 = AppConfig.getValue(MetricResource.POWER);
                                        mSensorsDataSink.onBikePower(d4 != null ? d4.doubleValue() : ((Float) valueAnim.getAnimatedValue()).floatValue());
                                    }
                                    break;
                                case 5:
                                    Double d5 = AppConfig.getValue(MetricResource.SPEED);
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.SPEED)) {
                                        if (d5 != null) {
                                            double circumference = Prefs.getWheelSize();
                                            double metresPerSecond = d5.doubleValue() * 0.277778d;
                                            revsPerSec = (float) (metresPerSecond / circumference);
                                        } else {
                                            revsPerSec = ((Float) valueAnim.getAnimatedValue()).floatValue();
                                        }
                                        mSensorsDataSink.onWheelRevs(revsPerSec, 1000L);
                                    } else if (SensorsConnector.isSupportedType(Sensor.SensorType.RUNNING_SPEED_CADENCE)) {
                                        float pace = d5 != null ? (float) (d5.doubleValue() * 0.277778d) : ((Float) valueAnim.getAnimatedValue()).floatValue();
                                        mSensorsDataSink.onSpeed(pace);
                                        mSensorsDataSink.onDistance(pace);
                                    }
                                    break;
                                case 6:
                                    if (SensorsConnector.isSupportedType(Sensor.SensorType.STRIDE)) {
                                        Float f = Float.valueOf(((Float) valueAnim.getAnimatedValue()).floatValue());
                                        mSensorsDataSink.onStride(Float.valueOf(f.floatValue() / 100.0f).floatValue());
                                    }
                                    break;
                                case 7:
                                    double unused = FakeRideData.lastElevation = ((Float) valueAnim.getAnimatedValue()).floatValue();
                                    break;
                            }
                        }
                    }
                });
                valueAnim.start();
                animators.add(valueAnim);
                i++;
            }
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.util.FakeRideData$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$metrics$MetricResource = new int[MetricResource.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.CADENCE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.HEARTRATE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.OXYGENATION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.POWER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.SPEED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.STRIDE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$metrics$MetricResource[MetricResource.ELEVATION.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public static void followRoute(List<Waypoint> route) {
        if (route == null || route.size() < 2) {
            fakeRoute = null;
        } else {
            fakeRoute = new FakeRouteFollower(route);
        }
    }

    private static class FakeRouteFollower {
        private int mCur = 1;
        private double mLastLat;
        private Coordinate mLastLoc;
        private double mLastLong;
        private List<Waypoint> mRoute;

        public FakeRouteFollower(List<Waypoint> routeToFollow) {
            this.mRoute = routeToFollow;
            this.mLastLoc = routeToFollow.get(0);
        }

        public Location onDistance(double distance) {
            if (this.mCur < this.mRoute.size()) {
                int[] distAndBearing = CoordCalc.getDistanceResults(this.mRoute.get(this.mCur), this.mLastLoc);
                Log.d("FakeRouteFollower", "Distance this step: " + distance + ", distanceToWaypoint: " + distAndBearing[0] + ", " + distAndBearing[1]);
                if (distance > distAndBearing[0]) {
                    Log.d("FakeRouteFollower", "  Move to next Waypoint");
                    this.mCur++;
                    if (this.mCur < this.mRoute.size()) {
                        distAndBearing = CoordCalc.getDistanceResults(this.mRoute.get(this.mCur), this.mLastLoc);
                        Log.d("FakeRouteFollower", "Distance this step: " + distance + ", distanceToWaypoint: " + distAndBearing[0] + ", " + distAndBearing[1]);
                    } else {
                        Log.d("FakeRouteFollower", "  Past destination!");
                        distance = distAndBearing[0];
                    }
                }
                this.mLastLoc = CoordCalc.GetNewPoint(this.mLastLoc, distAndBearing[1], distance);
            }
            return LocationHelper.location(this.mLastLoc);
        }
    }
}
