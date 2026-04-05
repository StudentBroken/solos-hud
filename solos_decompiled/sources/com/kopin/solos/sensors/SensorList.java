package com.kopin.solos.sensors;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.sensors.btle.BSXSensor;
import com.kopin.solos.sensors.btle.BTLESensor;
import com.kopin.solos.sensors.btle.CableSensor;
import com.kopin.solos.sensors.btle.ComboSensor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes28.dex */
public class SensorList {
    private static final long AUTO_CONNECT_DELAY = 10000;
    private static final String PREFERENCES_KEY = "savedDevices";
    private static final long SENSOR_CHECK_DELAY = 3300;
    private static final boolean SHOW_UNKNOWN_SENSORS = false;
    private static Context mContext;
    private static Handler mHandler;
    private static SensorListObserver mObs;
    private static SharedPreferences mPrefs;
    private static Timer taskTimer;
    private static ArrayList<Sensor> mList = new ArrayList<>();
    private static ArrayList<Sensor> mConnectedList = new ArrayList<>();
    private static ArrayList<Sensor> mSavedList = new ArrayList<>();
    private static boolean mTimerEnabled = false;
    private static final TimerTask mAutoConnect = new TimerTask() { // from class: com.kopin.solos.sensors.SensorList.1
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            BluetoothAdapter mBluetoothAdapter;
            try {
                if (SensorList.mTimerEnabled && (mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) != null && mBluetoothAdapter.isEnabled()) {
                    synchronized (SensorList.mSavedList) {
                        for (final Sensor params : SensorList.mSavedList) {
                            synchronized (SensorList.mConnectedList) {
                                if (!SensorList.mConnectedList.contains(params) && ((params.getConnectionState() == Sensor.ConnectionState.DISCONNECTED || params.getConnectionState() == Sensor.ConnectionState.DISCONNECTING) && SensorsConnector.isSupportedType(params.getType()))) {
                                    SensorList.mHandler.post(new Runnable() { // from class: com.kopin.solos.sensors.SensorList.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            params.connect();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private static final TimerTask mConnectionChecker = new TimerTask() { // from class: com.kopin.solos.sensors.SensorList.2
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (SensorList.mTimerEnabled) {
                synchronized (SensorList.mConnectedList) {
                    for (Sensor params : SensorList.mConnectedList) {
                        if (params.isInUse()) {
                            for (Sensor.DataType dataType : params.getDataTypes()) {
                                if (params.isUsedFor(dataType) && !params.isReceivingData(dataType)) {
                                    SensorList.mObs.onListChanged();
                                }
                            }
                        }
                    }
                }
                if (SensorsConnector.isAntBridgeConnected()) {
                    if (!SensorsConnector.isAntBridgeActive()) {
                        SensorList.mObs.onListChanged();
                    }
                    SensorsConnector.checkCable();
                }
            }
        }
    };
    private static final BroadcastReceiver sBTStateReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.sensors.SensorList.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                if (state == 10) {
                    synchronized (SensorList.mConnectedList) {
                        SensorList.mConnectedList.clear();
                    }
                    SensorsConnector.connectAntBridge(0);
                    ArrayList<Sensor> list = new ArrayList<>();
                    synchronized (SensorList.mList) {
                        list.addAll(SensorList.mList);
                    }
                    for (Sensor sensor : list) {
                        sensor.disconnect();
                    }
                }
            }
        }
    };

    public interface SensorListObserver {
        void onListChanged();
    }

    public static void init(Context context, SensorListObserver obs, SensorsConnector.ScannerListener scannerListener) {
        mContext = context;
        mObs = obs;
        mHandler = new Handler();
        mPrefs = context.getSharedPreferences(PREFERENCES_KEY, 0);
        processSensors(scannerListener);
        if (taskTimer == null) {
            taskTimer = new Timer();
            taskTimer.scheduleAtFixedRate(mAutoConnect, 10000L, 10000L);
            taskTimer.scheduleAtFixedRate(mConnectionChecker, SENSOR_CHECK_DELAY, SENSOR_CHECK_DELAY);
        }
        IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        context.registerReceiver(sBTStateReceiver, filter);
    }

    public static void setSavedSensors(Set<String> sensorsInfo, SensorsConnector.ScannerListener scannerListener) {
        if (sensorsInfo != null) {
            synchronized (mSavedList) {
                mSavedList.clear();
            }
            mPrefs.edit().putStringSet(PREFERENCES_KEY, sensorsInfo).apply();
        }
        processSensors(scannerListener);
    }

    private static void processSensors(SensorsConnector.ScannerListener scannerListener) {
        Sensor sensor;
        Set<String> saveList = mPrefs.getStringSet(PREFERENCES_KEY, new HashSet());
        for (String params : saveList) {
            if (params.startsWith("BTLE")) {
                sensor = BTLESensor.deserialize(params, mContext, scannerListener);
            } else if (params.startsWith("BSX")) {
                sensor = BSXSensor.deserialize(params, mContext, scannerListener);
            } else if (params.startsWith("COMBO") || params.startsWith("FOOT_POD")) {
                sensor = ComboSensor.deserialize(params, mContext, scannerListener);
            } else if (params.startsWith("CABLE")) {
                sensor = CableSensor.deserialize(params, mContext, scannerListener);
            } else {
                sensor = Sensor.deserialize(params, mContext);
            }
            if (sensor != null && sensor.hasType()) {
                synchronized (mSavedList) {
                    mSavedList.add(sensor);
                }
                add(sensor);
            }
        }
    }

    public static void stop() {
        mTimerEnabled = false;
        try {
            mContext.unregisterReceiver(sBTStateReceiver);
        } catch (IllegalArgumentException iae) {
            Log.e("SensorList", "receiver not currently registered when unregistering " + iae.getMessage());
        }
    }

    public static void clear() {
        synchronized (mList) {
            mList.clear();
        }
    }

    public static void shutdown() {
        mAutoConnect.cancel();
        mConnectionChecker.cancel();
    }

    static void checkNow() {
        mAutoConnect.run();
    }

    public static void add(Sensor sensor) {
        synchronized (mList) {
            for (Sensor s : mList) {
                if (s.equals(sensor)) {
                    return;
                }
            }
            mList.add(sensor);
        }
    }

    public static void remove(Sensor sensor) {
        synchronized (mList) {
            mList.remove(sensor);
        }
    }

    public static boolean hasSensor(String mac) {
        boolean z;
        synchronized (mList) {
            Iterator<Sensor> it = mList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                Sensor s = it.next();
                if (s.getId().contentEquals(mac)) {
                    z = true;
                    break;
                }
            }
        }
        return z;
    }

    public static boolean isSensorAvailable(Sensor.SensorType type) {
        return isSensorConnected(type.defaultDataType(), true);
    }

    public static boolean isSensorConnected(Sensor.DataType type) {
        return isSensorConnected(type, false);
    }

    public static boolean isSensorConnected(Sensor.DataType type, boolean ignoreData) {
        boolean z = false;
        if (type != Sensor.DataType.UNKOWN) {
            synchronized (mConnectedList) {
                for (Sensor params : mConnectedList) {
                    if (params.equals(type) && params.isUsedFor(type) && (ignoreData || params.isReceivingData(type))) {
                        z = true;
                        break;
                    }
                }
            }
        }
        return z;
    }

    public static Sensor getSensorProviding(Sensor.DataType type) {
        synchronized (mConnectedList) {
            for (Sensor params : mConnectedList) {
                if (params.isUsedFor(type)) {
                    return params;
                }
            }
            return null;
        }
    }

    public static ArrayList<Sensor> getList() {
        ArrayList<Sensor> list = new ArrayList<>();
        synchronized (mList) {
            for (Sensor s : mList) {
                if (s.hasType() && SensorsConnector.isSupportedType(s.getType())) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    public static ArrayList<Sensor> getConnectedAndSavedList() {
        ArrayList<Sensor> list = new ArrayList<>();
        synchronized (mSavedList) {
            list.addAll(mSavedList);
        }
        synchronized (mConnectedList) {
            for (Sensor s : mConnectedList) {
                if (!list.contains(s)) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    static void sensorConnected(Sensor sensor) {
        synchronized (mConnectedList) {
            if (sensor != null) {
                if (!mConnectedList.contains(sensor)) {
                    mConnectedList.add(sensor);
                }
            }
        }
    }

    static void sensorDisconnected(Sensor sensor) {
        synchronized (mConnectedList) {
            if (sensor != null) {
                mConnectedList.remove(sensor);
            }
        }
    }

    public static boolean hasConnectedDevices() {
        boolean z;
        synchronized (mConnectedList) {
            z = !mConnectedList.isEmpty();
        }
        return z;
    }

    public static void saveDevice(Sensor sensor) {
        synchronized (mSavedList) {
            for (Sensor s : mSavedList) {
                if (s.equals(sensor)) {
                    return;
                }
            }
            mSavedList.add(sensor);
            saveList();
        }
    }

    public static void forgetDevice(Sensor sensor) {
        synchronized (mSavedList) {
            boolean remove = true;
            while (remove) {
                remove = mSavedList.remove(sensor);
            }
        }
        saveList();
    }

    public static void forgetDevices(Sensor... sensors) {
        synchronized (mSavedList) {
            for (Sensor sensor : sensors) {
                mSavedList.remove(sensor);
            }
        }
        saveList();
    }

    public static boolean hasSavedDevices() {
        boolean z;
        synchronized (mSavedList) {
            z = !mSavedList.isEmpty();
        }
        return z;
    }

    static void reset() {
        synchronized (mSavedList) {
            mSavedList.clear();
        }
        saveList();
        synchronized (mList) {
            ArrayList<Sensor> list = (ArrayList) mList.clone();
            for (Sensor params : list) {
                params.disconnect();
            }
            mList.clear();
        }
        synchronized (mConnectedList) {
            mConnectedList.clear();
        }
        mObs.onListChanged();
    }

    public static boolean isDeviceSaved(Sensor sensor) {
        return mSavedList.contains(sensor);
    }

    private static void saveList() {
        HashSet<String> saveList = new HashSet<>(mSavedList.size());
        synchronized (mSavedList) {
            for (Sensor sensor : mSavedList) {
                saveList.add(sensor.serialize());
            }
        }
        mPrefs.edit().putStringSet(PREFERENCES_KEY, saveList).apply();
    }

    public static Set<String> getSavedList() {
        HashSet<String> savedList = new HashSet<>();
        savedList.addAll(mPrefs.getStringSet(PREFERENCES_KEY, new HashSet()));
        return savedList;
    }

    static void enable() {
        mTimerEnabled = true;
        mAutoConnect.run();
        mConnectionChecker.run();
    }

    static void disable() {
        mTimerEnabled = false;
        for (Sensor sensor : getList()) {
            if (sensor.isConnected()) {
                sensor.disconnect();
            }
        }
    }

    static void onNewSensorProvider() {
        mObs.onListChanged();
    }
}
