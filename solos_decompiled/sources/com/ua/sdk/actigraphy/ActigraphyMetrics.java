package com.ua.sdk.actigraphy;

/* JADX INFO: loaded from: classes65.dex */
public interface ActigraphyMetrics {
    Metric[] getBodyMass();

    Metric[] getDistance();

    Metric[] getEnergyBurned();

    Metric[] getSleep();

    Metric[] getSteps();

    void setBodyMass(MetricImpl[] metricImplArr);

    void setDistance(MetricImpl[] metricImplArr);

    void setEnergyBurned(MetricImpl[] metricImplArr);

    void setSleep(MetricImpl[] metricImplArr);

    void setSteps(MetricImpl[] metricImplArr);
}
