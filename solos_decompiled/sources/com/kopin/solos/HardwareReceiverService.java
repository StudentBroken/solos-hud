package com.kopin.solos;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.widget.Toast;
import com.kopin.pupil.ConnectionManager;
import com.kopin.pupil.SolosDevice;
import com.kopin.pupil.bluetooth.DeviceLister;
import com.kopin.solos.RideControl;
import com.kopin.solos.common.SportType;
import com.kopin.solos.debug.RideReplayer;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.gps.LocationHandler;
import com.kopin.solos.sensors.utils.Utilities;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.AverageProvider;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.FakeRideData;
import com.kopin.solos.util.ModeSwitch;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes37.dex */
public class HardwareReceiverService extends Service {
    private static final int NOTIFICATION_ID = 12344;
    private static final boolean REQUIRE_PUPIL = true;
    private static final String TAG = "SolosService";
    private static long WAIT_AFTER_SPEED_SENSOR_LOSS = 5000;
    private AppService mAppService;
    private ConnectionManager mConnectionManager;
    private Handler mHandler;
    private LocationHandler mLocationHandler;
    private SplitHelper mSplitHelper;
    private long mThreadId;
    private HardwareBinder mBinder = new HardwareBinder();
    private HeadsetStateListener mHeadsetStateListener = null;
    private HashSet<InfoListener> mInfoListeners = new HashSet<>();
    private Set<DataListener> mDataListeners = Collections.newSetFromMap(new ConcurrentHashMap());
    private DataHolder mDataHolder = new DataHolder();
    private TimeHelper mTimeHelper = new TimeHelper();
    private boolean mUseGPSForDistance = false;
    private final SensorList.SensorListObserver mSensorListObserver = new SensorList.SensorListObserver() { // from class: com.kopin.solos.HardwareReceiverService.12
        @Override // com.kopin.solos.sensors.SensorList.SensorListObserver
        public void onListChanged() {
            HardwareReceiverService.this.refreshCallbacks();
        }
    };
    private final RideControl.RideObserver mRideObserver = new RideControl.RideObserver() { // from class: com.kopin.solos.HardwareReceiverService.13
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(RideControl.StartMode startMode) {
            HardwareReceiverService.this.resetDataHolder();
            if (Config.FAKE_DATA) {
                HardwareReceiverService.this.mUseGPSForDistance = false;
                HardwareReceiverService.this.startFakeData();
            } else {
                HardwareReceiverService.this.mUseGPSForDistance = true;
                HardwareReceiverService.this.mLocationHandler.requestDistanceUpdates();
            }
            HardwareReceiverService.this.mLocationHandler.addListener(HardwareReceiverService.this.mLocationListener);
            if (Prefs.isGPSEnabled()) {
                HardwareReceiverService.this.mLocationHandler.prepare(true);
            }
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(Workout.RideMode mode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            return true;
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(SavedWorkout ride) {
            HardwareReceiverService.this.stopFakeData();
            HardwareReceiverService.this.mLocationHandler.stopLocation();
            HardwareReceiverService.this.mLocationHandler.removeListener(HardwareReceiverService.this.mLocationListener);
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(boolean userOrAuto) {
            HardwareReceiverService.this.stopFakeData();
            HardwareReceiverService.this.sendNotification(NotificationType.PAUSE_RESUME);
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(boolean userOrAuto) {
            if (Config.FAKE_DATA) {
                HardwareReceiverService.this.startFakeData();
            }
            HardwareReceiverService.this.sendNotification(NotificationType.PAUSE_RESUME);
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(double lastDistance, long lastTime) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(int counter) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(boolean wasCancelled) {
        }
    };
    private Runnable mUseGPSRunnable = new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.15
        @Override // java.lang.Runnable
        public void run() {
            if (!HardwareReceiverService.this.mUseGPSForDistance && !SensorList.isSensorConnected(Sensor.DataType.SPEED) && !SensorList.isSensorConnected(Sensor.DataType.PACE) && HardwareReceiverService.this.isActiveRide()) {
                HardwareReceiverService.this.mUseGPSForDistance = true;
                HardwareReceiverService.this.mLocationHandler.requestDistanceUpdates();
            }
        }
    };
    private final SolosDevice.AntButtonResponseListener mAntButtonListener = new SolosDevice.AntButtonResponseListener() { // from class: com.kopin.solos.HardwareReceiverService.16
        @Override // com.kopin.pupil.SolosDevice.AntButtonResponseListener
        public void onCommand(int seq, int cmd) {
            switch (cmd) {
                case 0:
                    HardwareReceiverService.this.mSensorsDataSink.onButtonPress(Sensor.ButtonAction.BLUE_THINGY);
                    break;
                case 1:
                    HardwareReceiverService.this.mSensorsDataSink.onButtonPress(Sensor.ButtonAction.MENU);
                    break;
                case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
                    HardwareReceiverService.this.mSensorsDataSink.onButtonPress(Sensor.ButtonAction.LAP);
                    break;
                default:
                    Log.d(HardwareReceiverService.TAG, "Un recognised ANT+ button command " + cmd + ", seq " + seq);
                    break;
            }
        }
    };
    private final SensorsConnector.SensorDataSink mSensorsDataSink = new SensorsConnector.SensorDataSink() { // from class: com.kopin.solos.HardwareReceiverService.17
        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onSensorsChanged() {
            HardwareReceiverService.this.refreshCallbacks();
            HardwareReceiverService.this.updateUseLocation();
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onCableEvent(boolean isConnected, boolean isActive) {
            if (isConnected && !isActive) {
                Log.d(HardwareReceiverService.TAG, "Timeout in CABLE connection, requesting reset");
                SensorsConnector.connectAntBridge(0);
                SolosDevice.closeAntBridge();
                HardwareReceiverService.this.mHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SolosDevice.wakeAntBridge();
                    }
                }, 1000L);
            }
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onWheelRevs(double wheelRevs, long interval) {
            Log.d(HardwareReceiverService.TAG, "onWheelRevs: " + wheelRevs + " revs, " + interval + " ms");
            double circumference = Prefs.getWheelSize();
            if (!HardwareReceiverService.this.mUseGPSForDistance) {
                onDistance(wheelRevs * circumference);
            }
            double rpm = (60000.0d * wheelRevs) / interval;
            double revsPerSecond = (1000.0d * wheelRevs) / interval;
            double metresPerSecond = circumference * revsPerSecond;
            double speed = circumference * rpm;
            double d = (60.0d * speed) / 1000.0d;
            onSpeed(metresPerSecond);
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onDistance(final double metres) {
            Log.d(HardwareReceiverService.TAG, "onDistance: " + metres + " metres");
            HardwareReceiverService.this.mSplitHelper.onDistance(metres);
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.2
                @Override // java.lang.Runnable
                public void run() {
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onDistance(metres);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onSpeed(double metresPerSecond) {
            if (HardwareReceiverService.this.mUseGPSForDistance) {
                HardwareReceiverService.this.mLocationHandler.cancelDistanceUpdates();
            }
            HardwareReceiverService.this.mUseGPSForDistance = false;
            onSpeed(metresPerSecond, false);
        }

        public void onSpeed(final double metresPerSecond, boolean fromGPS) {
            Log.d(HardwareReceiverService.TAG, "onSpeed (" + (fromGPS ? "GPS" : "Sensor") + "): " + metresPerSecond + " m/s");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.3
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastSpeed(metresPerSecond);
                    RideControl.checkSpeed(metresPerSecond);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onSpeed(metresPerSecond);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onCadence(final double cadenceRPM) {
            Log.d(HardwareReceiverService.TAG, "onCadence: " + cadenceRPM + " rpm");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.4
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastCadence(cadenceRPM);
                    RideControl.checkCadence(cadenceRPM);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onCadence(cadenceRPM);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onHeartRate(final int heartRate) {
            Log.d(HardwareReceiverService.TAG, "onHeartRate: " + heartRate + " bpm");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.5
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastHeartRate(heartRate);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onHeartRate(heartRate);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onBikePower(final double power) {
            Log.d(HardwareReceiverService.TAG, "onBikePower: " + power + " watts");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.6
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastPower(power);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onBikePower(power);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onOxygen(final int oxygen) {
            Log.d(HardwareReceiverService.TAG, "onOxygen: " + oxygen + " %");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.7
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastOxygen(oxygen);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onOxygen(oxygen);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onStride(final double stride) {
            Log.d(HardwareReceiverService.TAG, "onStride: " + stride + " metres");
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.8
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastStride(stride);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onStride(stride);
                    }
                }
            });
        }

        public void onAltitude(final float altitude) {
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.9
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastElevation(altitude);
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onAltitude(altitude);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onLocation(final Location location) {
            if (HardwareReceiverService.this.mUseGPSForDistance) {
                onSpeed(location.getSpeed(), true);
            }
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.10
                @Override // java.lang.Runnable
                public void run() {
                    HardwareReceiverService.this.mDataHolder.setLastLocation(location);
                    if (location.hasAltitude()) {
                        HardwareReceiverService.this.mDataHolder.setLastElevation((float) location.getAltitude());
                    }
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onLocation(location);
                        if (location.hasAltitude()) {
                            listener.onAltitude((float) location.getAltitude());
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onButtonPress(final Sensor.ButtonAction button) {
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.11
                @Override // java.lang.Runnable
                public void run() {
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        listener.onButtonPress(button);
                    }
                }
            });
        }

        @Override // com.kopin.solos.sensors.SensorsConnector.SensorDataSink
        public void onSensorConfig(boolean ok, final String msg) {
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.17.12
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(HardwareReceiverService.this.getApplication(), msg, 1).show();
                }
            });
        }
    };
    private LocationHandler.InternalLocationListener mLocationListener = new LocationHandler.InternalLocationListener() { // from class: com.kopin.solos.HardwareReceiverService.18
        @Override // com.kopin.solos.sensors.gps.LocationHandler.InternalLocationListener
        public void onLocationChanged(Location location, double distanceFromLastLocation) {
            if (HardwareReceiverService.this.mUseGPSForDistance) {
                HardwareReceiverService.this.mSensorsDataSink.onDistance(distanceFromLastLocation);
            }
            HardwareReceiverService.this.mSensorsDataSink.onLocation(location);
        }
    };
    private ConnectionManager.ConnectionManagerListener mConnectionManagerListener = new ConnectionManager.ConnectionManagerListener() { // from class: com.kopin.solos.HardwareReceiverService.19
        @Override // com.kopin.pupil.ConnectionManager.ConnectionManagerListener
        public void onGarminRemoteButtonPress(final int button) {
            HardwareReceiverService.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.19.1
                @Override // java.lang.Runnable
                public void run() {
                    for (DataListener listener : HardwareReceiverService.this.mDataListeners) {
                        if (button == 1) {
                            listener.onButtonPress(Sensor.ButtonAction.LAP);
                        } else if (button == 2) {
                            listener.onButtonPress(Sensor.ButtonAction.MENU);
                        } else if (button == 3) {
                            listener.onButtonPress(Sensor.ButtonAction.BLUE_THINGY);
                        }
                    }
                }
            });
        }
    };

    public interface HeadsetStateListener {
        void onHeadsetConnected();

        void onHeadsetDisconnected();

        void onHeadsetReady();
    }

    public interface InfoListener {
        void onListChanged();

        void onNotification(NotificationType notificationType);
    }

    public enum NotificationType {
        START_STOP,
        PAUSE_RESUME,
        CONNECTION,
        PREPARE
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ((BaseSolosApplication) getApplication()).mService = this;
        Utility.setUnitSystem(Prefs.getUnitSystem());
        this.mHandler = new Handler();
        this.mThreadId = this.mHandler.getLooper().getThread().getId();
        SensorsConnector.init(this, this.mSensorsDataSink);
        SensorList.init(this, this.mSensorListObserver, SensorsConnector.getScannerListener());
        Platforms.init(this);
        this.mSplitHelper = new SplitHelper(this);
        this.mLocationHandler = new LocationHandler(this);
        Utility.markReady(this, false, HardwareReceiverService.class);
        this.mConnectionManager = new ConnectionManager(this);
        this.mConnectionManager.setConnectionListener(this.mConnectionManagerListener);
        this.mAppService = new AppService(this.mConnectionManager, this);
        this.mAppService.onCreate();
        if (((BaseSolosApplication) getApplication()).isDefaultModeActive()) {
            ModeSwitch.enableDevices(this);
        }
        LiveRide.init(getTimeHelper(), getSplitHelper());
        addDataListener(LiveRide.getDataListener());
        Navigator.init(this);
        RideControl.registerObserver(this.mRideObserver);
        RideReplayer.init(this.mSensorsDataSink);
    }

    public void startFakeData() {
        if (Config.FAKE_DATA) {
            this.mLocationHandler.prepare(true, true);
            FakeRideData.startRandom(this.mSensorsDataSink, this.mSplitHelper, this.mLocationHandler, LiveRide.getCurrentSport() == SportType.RIDE);
        }
    }

    public void stopFakeData() {
        FakeRideData.stop();
        this.mLocationHandler.prepare(true, false);
    }

    @Override // android.app.Service
    public void onDestroy() {
        SensorsConnector.stopDiscovery();
        this.mAppService.onDestroy();
        FakeRideData.stop();
        SensorList.shutdown();
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void addCallback(final InfoListener callback) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.1
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mInfoListeners.add(callback);
            }
        });
    }

    public void setHeadsetCallback(final HeadsetStateListener callback) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.2
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mHeadsetStateListener = callback;
            }
        });
    }

    public void removeCallback(final InfoListener callback) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.3
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mInfoListeners.remove(callback);
            }
        });
    }

    public void resetDataHolder() {
        if (this.mDataHolder != null) {
            this.mDataHolder.configure();
        }
    }

    public void configureDataHolder(Sensor.DataType dataType, String val) {
        if (this.mDataHolder != null) {
            this.mDataHolder.configure(dataType, val);
        }
    }

    public boolean isGPSEnabled() {
        return this.mLocationHandler.isEnabled(true);
    }

    public void prepareLocation() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.4
            @Override // java.lang.Runnable
            public void run() {
                if (Prefs.isGPSEnabled()) {
                    HardwareReceiverService.this.mLocationHandler.prepare(true);
                    Notification notif = HardwareReceiverService.this.createNotification();
                    HardwareReceiverService.this.startForeground(HardwareReceiverService.NOTIFICATION_ID, notif);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Notification createNotification() {
        PendingIntent activityIntent = PendingIntent.getActivity(this, 0, getPackageManager().getLaunchIntentForPackage(getPackageName()), 0);
        Notification.Builder nBuilder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            if (notificationManager.getNotificationChannel("Solos") == null) {
                NotificationChannel channel = new NotificationChannel("Solos", "Solos", 2);
                channel.setDescription("Solos is active");
                notificationManager.createNotificationChannel(channel);
            }
            nBuilder.setChannelId("Solos");
        }
        nBuilder.setContentTitle(getString(com.kopin.solos.storage.R.string.app_name));
        nBuilder.setContentText(getString(com.kopin.solos.storage.R.string.notification_message));
        nBuilder.setOngoing(true);
        nBuilder.setSmallIcon(com.kopin.solos.storage.R.drawable.notification_style1);
        nBuilder.setContentIntent(activityIntent);
        return nBuilder.build();
    }

    public void stopLocation() {
        this.mLocationHandler.stopLocation();
        stopForeground(true);
    }

    public Location getLocation() {
        Location loc = this.mDataHolder.getLastLocation();
        if (loc == null && this.mLocationHandler != null) {
            return this.mLocationHandler.getLocation();
        }
        return loc;
    }

    public boolean hasRecentLocation() {
        if (this.mLocationHandler != null) {
            return this.mLocationHandler.hasFix();
        }
        return false;
    }

    public void addLocationListener(final LocationHandler.InternalLocationListener listener) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.5
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mLocationHandler.addListener(listener);
            }
        });
    }

    public void removeLocationListener(final LocationHandler.InternalLocationListener listener) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.6
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mLocationHandler.removeListener(listener);
            }
        });
    }

    public void addDataListener(final DataListener listener) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.7
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mDataListeners.add(listener);
            }
        });
    }

    public void removeDataListener(final DataListener listener) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.8
            @Override // java.lang.Runnable
            public void run() {
                HardwareReceiverService.this.mDataListeners.remove(listener);
            }
        });
    }

    public double getLastCadence() {
        return this.mDataHolder.getLastCadence();
    }

    public int getLastOxygen() {
        return this.mDataHolder.getLastOxygen();
    }

    public double getLastSpeed() {
        return this.mDataHolder.getLastSpeed();
    }

    public double getLastSpeedForLocale() {
        return Conversion.speedForLocale(this.mDataHolder.getLastSpeed());
    }

    public double getLastPaceForLocale() {
        return Conversion.paceForLocale(this.mDataHolder.getLastPace());
    }

    public AverageProvider getAverageSpeedProvider() {
        return new AverageProvider() { // from class: com.kopin.solos.HardwareReceiverService.9
            @Override // com.kopin.solos.storage.util.AverageProvider
            public double getAverageForLocale() {
                return HardwareReceiverService.this.getAverageSpeedForLocale();
            }
        };
    }

    public AverageProvider getAveragePaceProvider() {
        return new AverageProvider() { // from class: com.kopin.solos.HardwareReceiverService.10
            @Override // com.kopin.solos.storage.util.AverageProvider
            public double getAverageForLocale() {
                return Conversion.speedToPaceForLocale(LiveRide.getAverageSpeed());
            }
        };
    }

    public double getAverageSpeedForLocale() {
        return Conversion.speedForLocale(LiveRide.getAverageSpeed());
    }

    public double getAveragePaceForLocale() {
        return LiveRide.getAveragePaceLocale();
    }

    public int getLastHeartRate() {
        return this.mDataHolder.getLastHeartRate();
    }

    public double getLastPower() {
        return this.mDataHolder.getLastPower();
    }

    public double getLastStride() {
        return this.mDataHolder.getLastStride();
    }

    public double getLastStrideForLocale() {
        return Conversion.strideForLocale(this.mDataHolder.getLastStride());
    }

    public double getLastPace() {
        return Conversion.speedToPace(this.mDataHolder.getLastSpeed());
    }

    public float getLastElevation() {
        return this.mDataHolder.getLastElevation();
    }

    public float getLastElevationForLocale() {
        return Conversion.elevationForLocale(getLastElevation());
    }

    public float getInitialElevation() {
        return this.mDataHolder.getInitialElevation();
    }

    public float getElevationChange() {
        if (getInitialElevation() == -2.14748365E9f || getLastElevation() == -2.14748365E9f) {
            return -2.14748365E9f;
        }
        return getLastElevation() - getInitialElevation();
    }

    public float getElevationChangeForLocale() {
        float elevChange = getElevationChange();
        if (elevChange != -2.14748365E9f) {
            return (float) Utility.convertToUserUnits(0, elevChange);
        }
        return -2.14748365E9f;
    }

    public double getLastDistance() {
        return this.mSplitHelper.getTotalDistance();
    }

    public boolean isActiveRide() {
        return LiveRide.isActiveRide();
    }

    public double getLastDistanceForLocale() {
        double distKm = Utilities.convertMetersToKilometers(this.mSplitHelper.getTotalDistance());
        return Prefs.isMetric() ? distKm : distKm * 0.62137d;
    }

    public void resetInitialElevation() {
        this.mDataHolder.resetInitialElevation();
    }

    public int getSplitElevation() {
        return (int) LiveRide.getCurrentRide().getElevation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshCallbacks() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.11
            @Override // java.lang.Runnable
            public void run() {
                for (InfoListener callback : HardwareReceiverService.this.mInfoListeners) {
                    callback.onListChanged();
                }
            }
        });
    }

    public TimeHelper getTimeHelper() {
        return this.mTimeHelper;
    }

    public void sendNotification(final NotificationType type) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.HardwareReceiverService.14
            @Override // java.lang.Runnable
            public void run() {
                for (InfoListener listener : HardwareReceiverService.this.mInfoListeners) {
                    listener.onNotification(type);
                }
            }
        });
    }

    public void onHeadsetConnected() {
        if (this.mHeadsetStateListener != null) {
            this.mHeadsetStateListener.onHeadsetConnected();
        }
        SolosDevice.wakeAntBridge(true);
    }

    public void onHeadsetReady(int cableId) {
        SolosDevice.wakeAntBridge();
        SolosDevice.listenForAntButtons(this.mAntButtonListener);
        SolosDevice.checkForAntBridge(cableId);
        if (this.mHeadsetStateListener != null) {
            this.mHeadsetStateListener.onHeadsetReady();
        }
    }

    public void onHeadsetDisconnected() {
        if (this.mHeadsetStateListener != null) {
            this.mHeadsetStateListener.onHeadsetDisconnected();
        }
        SolosDevice.listenForAntButtons(null);
        SensorsConnector.connectAntBridge(0);
    }

    public boolean isConnectedToVC() {
        return this.mAppService.isConnected() || this.mAppService.isReconnecting();
    }

    public boolean isReconnecting() {
        return this.mAppService.isReconnecting();
    }

    public SplitHelper getSplitHelper() {
        return this.mSplitHelper;
    }

    public AppService getAppService() {
        return this.mAppService;
    }

    public void connectToVC(Activity activity) {
        this.mAppService.refreshDoublePages();
        this.mConnectionManager.showConnectionDialog(activity, this.mAppService);
    }

    public void initiateConnectionToVC() {
        this.mConnectionManager.initiateConnection(this.mAppService);
    }

    public DeviceLister.ConnectionListener getVCConnectionListener() {
        return this.mConnectionManager.getConnectionListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnMainThread(Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseLocation() {
        if (SensorList.isSensorConnected(Sensor.DataType.SPEED) || Config.FAKE_DATA) {
            if (this.mUseGPSForDistance) {
                this.mLocationHandler.cancelDistanceUpdates();
            }
            this.mUseGPSForDistance = false;
        } else if (!this.mUseGPSForDistance) {
            this.mHandler.postDelayed(this.mUseGPSRunnable, WAIT_AFTER_SPEED_SENSOR_LOSS);
        }
    }

    public class HardwareBinder extends Binder {
        public HardwareBinder() {
        }

        public HardwareReceiverService getService() {
            return HardwareReceiverService.this;
        }
    }
}
