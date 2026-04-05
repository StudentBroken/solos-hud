package com.kopin.solos.storage.settings;

import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.util.MetricType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class ConfigMetrics {
    private static List<MetricDataType> masterList = new ArrayList();
    private static List<MetricDataType> headsetList = new ArrayList();
    private static List<MetricDataType> rideList = new ArrayList();

    public static void init() {
        masterList.clear();
        List<MetricDataType> prefList = ConfigPrefs.getMetricConfigMaster();
        for (MetricDataType metricDataType : Config.METRICS_MASTER) {
            if (!Config.DEBUG_ALLOW_CONFIG_OVERRIDE || prefList == null || prefList.contains(metricDataType)) {
                masterList.add(metricDataType);
            }
        }
        if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE && prefList == null) {
            ConfigPrefs.setMetricConfigMaster(masterList);
        }
        headsetList.clear();
        List<MetricDataType> prefList2 = ConfigPrefs.getMetricConfigHeadset();
        for (MetricDataType metricDataType2 : Config.METRICS_HEADSET) {
            if (!Config.DEBUG_ALLOW_CONFIG_OVERRIDE || prefList2 == null || prefList2.contains(metricDataType2)) {
                headsetList.add(metricDataType2);
            }
        }
        if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE && prefList2 == null) {
            ConfigPrefs.setMetricConfigHeadset(headsetList);
        }
        headsetList.retainAll(masterList);
        rideList.clear();
        List<MetricDataType> prefList3 = ConfigPrefs.getMetricConfigRideScreen();
        for (MetricDataType metricDataType3 : Config.METRICS_RIDE_SCREEN) {
            if (!Config.DEBUG_ALLOW_CONFIG_OVERRIDE || prefList3 == null || prefList3.contains(metricDataType3)) {
                rideList.add(metricDataType3);
            }
        }
        if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE && prefList3 == null) {
            ConfigPrefs.setMetricConfigRideScreen(rideList);
        }
        rideList.retainAll(masterList);
    }

    public static List<MetricDataType> getHeadsetList() {
        return headsetList;
    }

    public static boolean isHeadsetMetricEnabled(MetricDataType metricType) {
        return headsetList.contains(metricType);
    }

    public static boolean isMetricEnabled(MetricDataType metricType) {
        return masterList.contains(metricType);
    }

    public static boolean isRideScreenMetricEnabled(MetricDataType metricType) {
        return rideList.contains(metricType);
    }

    public static List<MetricType> getMetricTypeList(List<MetricDataType> metricDataTypeList) {
        List<MetricType> list = new ArrayList<>();
        for (MetricDataType metricDataType : metricDataTypeList) {
            MetricType metricType = MetricType.getFromMetricDataType(metricDataType);
            if (metricType != null && !list.contains(metricType) && (metricType.getSensorType() == Sensor.DataType.UNKOWN || SensorsConnector.isAllowedType(metricType.getSensorType()))) {
                list.add(metricType);
            }
        }
        if (list.contains(MetricType.AVERAGE_PACE)) {
            list.add(MetricType.AVERAGE_PACE_GRAPH);
        }
        if (list.contains(MetricType.AVERAGE_SPEED)) {
            list.add(MetricType.AVERAGE_SPEED_GRAPH);
        }
        if (list.contains(MetricType.CADENCE)) {
            list.add(MetricType.CADENCE_GRAPH);
        }
        if (list.contains(MetricType.ELEVATION)) {
            list.add(MetricType.ELEVATION_GRAPH);
        }
        if (list.contains(MetricType.HEARTRATE)) {
            list.add(MetricType.HEARTRATE_GRAPH);
        }
        if (list.contains(MetricType.OXYGENATION)) {
            list.add(MetricType.OXYGENATION_GRAPH);
        }
        if (list.contains(MetricType.POWER)) {
            list.add(MetricType.POWER_GRAPH);
        }
        if (list.contains(MetricType.SPEED)) {
            list.add(MetricType.SPEED_GRAPH);
        }
        if (list.contains(MetricType.PACE)) {
            list.add(MetricType.PACE_GRAPH);
        }
        if (list.contains(MetricType.STEP)) {
            list.add(MetricType.STEP_GRAPH);
        }
        if (list.contains(MetricType.KICK)) {
            list.add(MetricType.KICK_GRAPH);
        }
        return list;
    }
}
