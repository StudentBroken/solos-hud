package com.kopin.solos;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.MotionEventCompat;
import com.goldeni.audio.GIAudioNative;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.NumberActionView;
import com.kopin.solos.menu.TextMenuAdapter;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class SensorWarnings {
    private static final String TAG = "SensorWarnings";
    private static Map<Warning, TextMenuAdapter.TextMenuItem> mMenuMap = new HashMap();
    private static Resources mRes;
    private static NumberActionView mWarningIcon;
    private static CustomActionProvider<TextMenuAdapter.TextMenuItem> mWarningsProvider;

    private enum Warning {
        CADENCE(R.string.warning_cadence),
        HEART_RATE(R.string.warning_heart_rate),
        POWER(R.string.warning_power),
        SPEED(R.string.warning_speed_gps),
        GPS(R.string.warning_location_no_gps_app),
        AUTO_START(R.string.warning_auto_start),
        REMOTE(R.string.warning_remote),
        HEADSET(R.string.warning_headset),
        ELEVATION(R.string.warning_gps_altitude),
        PACE(R.string.warning_pace_gps),
        STEP(R.string.warning_cadence),
        STRIDE(R.string.warning_stride),
        KICK(R.string.warning_power);

        int res;

        Warning(int stringRes) {
            this.res = stringRes;
        }

        int caption() {
            return this.res;
        }
    }

    public static void init(Context context) {
        mRes = context.getResources();
        mMenuMap.clear();
        mWarningIcon = new NumberActionView(context);
        mWarningIcon.setActiveColor(context.getResources().getColor(R.color.app_actionbar_divider));
        mWarningIcon.setInactiveColor(context.getResources().getColor(R.color.unfocused_grey));
        mWarningsProvider = new CustomActionProvider<>(context);
    }

    public static CustomActionProvider getWarningsProvider() {
        return mWarningsProvider;
    }

    public static void checkWarnings(List<MetricType> metrics, boolean bound, HardwareReceiverService mService) {
        if (mWarningsProvider != null) {
            mMenuMap.clear();
            for (MetricType page : metrics) {
                checkMetric(page, bound, mService);
            }
            checkGPS(bound && !mService.isGPSEnabled());
            checkAutoStart();
            checkRemote();
            addMenu(Warning.ELEVATION);
            checkHeadsetConnection(bound && !mService.isConnectedToVC());
        }
    }

    private static void refresh() {
        mWarningsProvider.clear();
        for (TextMenuAdapter.TextMenuItem menu : mMenuMap.values()) {
            mWarningsProvider.addMenuItem(menu);
        }
        mWarningsProvider.setActionView(mWarningIcon);
        mWarningIcon.setActive(mMenuMap.size() > 0);
        mWarningIcon.setNumber(mMenuMap.size());
    }

    public static void checkHeadsetConnection(boolean disconnected) {
        if (disconnected) {
            addMenu(Warning.HEADSET);
        } else if (!mMenuMap.containsKey(Warning.HEADSET)) {
            mMenuMap.remove(Warning.HEADSET);
        }
        refresh();
    }

    private static void addMenu(Warning warning) {
        if (!mMenuMap.containsKey(warning)) {
            TextMenuAdapter.TextMenuItem item = new TextMenuAdapter.TextMenuItem(mRes.getString(warning.caption()), warning.ordinal(), TextMenuAdapter.TextMenuType.SMALL);
            mMenuMap.put(warning, item);
        }
    }

    private static void addMenu(Warning warning, int stringRes) {
        if (!mMenuMap.containsKey(warning)) {
            TextMenuAdapter.TextMenuItem item = new TextMenuAdapter.TextMenuItem(mRes.getString(stringRes), warning.ordinal(), TextMenuAdapter.TextMenuType.SMALL);
            mMenuMap.put(warning, item);
        }
    }

    private static void addMenu(Warning warning, Sensor.DataType dataType) {
        if (!mMenuMap.containsKey(warning) && SensorsConnector.isAllowedType(dataType)) {
            TextMenuAdapter.TextMenuItem item = new TextMenuAdapter.TextMenuItem(mRes.getString(warning.caption()), warning.ordinal(), TextMenuAdapter.TextMenuType.SMALL);
            mMenuMap.put(warning, item);
        }
    }

    private static void checkMetric(MetricType metric, boolean bound, HardwareReceiverService mService) {
        if (metric != null) {
            switch (AnonymousClass1.$SwitchMap$com$kopin$solos$storage$util$MetricType[metric.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    if (!SensorList.isSensorConnected(Sensor.DataType.CADENCE)) {
                        addMenu(Warning.CADENCE, Sensor.DataType.CADENCE);
                        return;
                    }
                    return;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    if (!SensorList.isSensorConnected(Sensor.DataType.STEP)) {
                        addMenu(Warning.STEP, Sensor.DataType.STEP);
                        return;
                    }
                    return;
                case 10:
                case 11:
                case 12:
                default:
                    return;
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    if (!SensorList.isSensorConnected(Sensor.DataType.HEARTRATE)) {
                        addMenu(Warning.HEART_RATE, Sensor.DataType.HEARTRATE);
                        return;
                    }
                    return;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    if (!SensorList.isSensorConnected(Sensor.DataType.POWER)) {
                        addMenu(Warning.POWER, Sensor.DataType.POWER);
                        return;
                    }
                    return;
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                    if (!SensorList.isSensorConnected(Sensor.DataType.KICK)) {
                        addMenu(Warning.KICK, Sensor.DataType.KICK);
                        return;
                    }
                    return;
                case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
                case 30:
                case 31:
                case 32:
                case MotionEventCompat.AXIS_GENERIC_2 /* 33 */:
                case 34:
                    checkMetricSpeed(bound, mService, false);
                    return;
                case MotionEventCompat.AXIS_GENERIC_4 /* 35 */:
                case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
                case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                    checkMetricSpeed(bound, mService, true);
                    break;
                case 40:
                    break;
            }
            if (!SensorList.isSensorConnected(Sensor.DataType.STRIDE)) {
                addMenu(Warning.STRIDE, Sensor.DataType.STRIDE);
            }
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.SensorWarnings$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$storage$util$MetricType = new int[MetricType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE_GRAPH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_CADENCE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_CADENCE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP_GRAPH.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_STEP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_STEP.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_STEP.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_GRAPH.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_LAP_GRAPH.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_GRAPH.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_ZONES.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_HEARTRATE.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_HEARTRATE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_BAR.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_GRAPH.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_NORMALISED.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_POWER.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_POWER.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_BAR.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_GRAPH.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_KICK.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_KICK.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED_GRAPH.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED_GRAPH.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_SPEED.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_SPEED.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE_GRAPH.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_PACE.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_PACE.ordinal()] = 38;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_PACE.ordinal()] = 39;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STRIDE.ordinal()] = 40;
            } catch (NoSuchFieldError e40) {
            }
        }
    }

    private static void checkMetricSpeed(boolean bound, HardwareReceiverService hardwareReceiverService, boolean pace) {
        if (mMenuMap.containsKey(pace ? Warning.PACE : Warning.SPEED)) {
            return;
        }
        if (SensorList.isSensorConnected(pace ? Sensor.DataType.PACE : Sensor.DataType.SPEED)) {
            return;
        }
        if (SensorsConnector.isAllowedType(pace ? Sensor.DataType.PACE : Sensor.DataType.SPEED)) {
            Warning warning = pace ? Warning.PACE : Warning.SPEED;
            if (!Prefs.isGPSEnabled()) {
                addMenu(warning, pace ? R.string.warning_pace_no_gps_app : R.string.warning_speed_no_gps_app);
            } else if (bound) {
                if (!hardwareReceiverService.isGPSEnabled()) {
                    addMenu(warning, pace ? R.string.warning_pace_no_gps_sys : R.string.warning_speed_no_gps_sys);
                } else {
                    addMenu(warning, pace ? R.string.warning_pace_gps : R.string.warning_speed_gps);
                }
            }
        }
    }

    private static void checkGPS(boolean serverGPSDisabled) {
        if (!mMenuMap.containsKey(Warning.GPS)) {
            if (!Prefs.isGPSEnabled()) {
                addMenu(Warning.GPS);
            } else if (serverGPSDisabled) {
                addMenu(Warning.GPS, R.string.warning_location_no_gps_sys);
            }
        }
    }

    private static void checkAutoStart() {
        if (!mMenuMap.containsKey(Warning.AUTO_START) && Prefs.isAuto() && !Prefs.isAutoStartPossible()) {
            addMenu(Warning.AUTO_START);
        }
    }

    private static void checkRemote() {
        if (!mMenuMap.containsKey(Warning.REMOTE) && Prefs.hasAutoScreenChange()) {
        }
    }

    public static boolean hasHeadsetWarning() {
        return mMenuMap.containsKey(Warning.HEADSET);
    }

    public static void removeHeadsetWarning() {
        mMenuMap.remove(Warning.HEADSET);
        refresh();
    }
}
