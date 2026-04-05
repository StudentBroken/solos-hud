package com.kopin.solos.common.config;

import com.ua.sdk.datapoint.BaseDataTypes;

/* JADX INFO: loaded from: classes52.dex */
public enum MetricDataType {
    CADENCE,
    AVG_CADENCE,
    CALORIES,
    DISTANCE,
    ELEVATION,
    ELEVATION_CHANGE,
    FUNCTIONAL_THRESHOLD_POWER,
    HEART_RATE,
    HEART_RATE_ZONE,
    INTENSITY_FACTOR,
    NORMALISED_POWER,
    OVERALL_CLIMB,
    OXYGEN,
    POWER,
    POWER_BAR,
    AVG_POWER,
    SPEED,
    AVG_SPEED,
    TRAINING_STRESS_SCORE,
    TIME,
    TARGET_AVERAGE_CADENCE,
    TARGET_AVERAGE_HEART_RATE,
    TARGET_AVERAGE_POWER,
    TARGET_AVERAGE_SPEED,
    TARGET_AVERAGE_PACE,
    TARGET_DISTANCE,
    TARGET_TIME,
    STRIDE,
    PACE,
    AVG_PACE,
    STEP,
    AVG_STEP,
    TARGET_AVERAGE_STEP,
    KICK,
    KICK_BAR,
    AVG_KICK,
    TARGET_AVERAGE_KICK,
    QUAD_METRIC_OVERVIEW;

    public static MetricDataType getMetricDataType(String name) {
        for (MetricDataType dt : values()) {
            if (name.equalsIgnoreCase(dt.name())) {
                return dt;
            }
        }
        return null;
    }

    public static MetricDataType fromString(String metricName) {
        if (metricName.contains("cadence")) {
            return metricName.contains("target") ? TARGET_AVERAGE_CADENCE : CADENCE;
        }
        if (metricName.contains("calories")) {
            return CALORIES;
        }
        if (metricName.contains(BaseDataTypes.ID_DISTANCE)) {
            return metricName.contains("target") ? TARGET_DISTANCE : DISTANCE;
        }
        if (metricName.contains(BaseDataTypes.ID_ELEVATION)) {
            return metricName.contains("change") ? ELEVATION_CHANGE : ELEVATION;
        }
        if (metricName.contains("heart")) {
            return metricName.contains("target") ? TARGET_AVERAGE_HEART_RATE : metricName.contains("zone") ? HEART_RATE_ZONE : HEART_RATE;
        }
        if (metricName.contains(BaseDataTypes.ID_INTENSITY)) {
            return INTENSITY_FACTOR;
        }
        if (metricName.contains("normalised")) {
            return NORMALISED_POWER;
        }
        if (metricName.contains("climb")) {
            return OVERALL_CLIMB;
        }
        if (metricName.contains("oxygen")) {
            return OXYGEN;
        }
        if (metricName.contains("power_bar")) {
            return POWER_BAR;
        }
        if (metricName.contains("power")) {
            return metricName.contains("target") ? TARGET_AVERAGE_POWER : POWER;
        }
        if (metricName.contains(BaseDataTypes.ID_SPEED)) {
            return metricName.contains("target") ? TARGET_AVERAGE_SPEED : SPEED;
        }
        if (metricName.contains("time")) {
            return metricName.contains("countdown") ? TARGET_TIME : TIME;
        }
        if (metricName.contains("stride")) {
            return STRIDE;
        }
        if (metricName.contains("pace")) {
            return metricName.contains("target") ? TARGET_AVERAGE_PACE : PACE;
        }
        if (metricName.contains("step")) {
            return metricName.contains("target") ? TARGET_AVERAGE_STEP : STEP;
        }
        if (metricName.contains("kick_bar")) {
            return KICK_BAR;
        }
        if (metricName.contains("kick")) {
            return metricName.contains("target") ? TARGET_AVERAGE_KICK : KICK;
        }
        if (metricName.contains("overview")) {
            return QUAD_METRIC_OVERVIEW;
        }
        return null;
    }
}
