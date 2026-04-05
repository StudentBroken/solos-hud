package com.kopin.solos.metrics;

import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class MetricData {
    private boolean inFront1;
    private boolean inFront2;
    private String metric1;
    private String metric2;
    private MetricType metricType1 = MetricType.NONE;
    private MetricType metricType2 = MetricType.NONE;
    private String target1;
    private String target2;
    private String title;

    public MetricData(String title, String metric1, String metric2, String target1, String target2) {
        this.title = title;
        this.metric1 = metric1;
        this.metric2 = metric2;
        this.target1 = target1;
        this.target2 = target2;
    }

    public MetricType getMetricType1() {
        return this.metricType1;
    }

    public void setMetricType1(MetricType metricType) {
        this.metricType1 = metricType;
    }

    public MetricType getMetricType2() {
        return this.metricType2;
    }

    public void setMetricType2(MetricType metricType) {
        this.metricType2 = metricType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetric1() {
        return this.metric1;
    }

    public void setMetric1(String metric1) {
        this.metric1 = metric1;
    }

    public String getMetric2() {
        return this.metric2;
    }

    public void setMetric2(String metric2) {
        this.metric2 = metric2;
    }

    public String getTarget1() {
        return this.target1;
    }

    public void setTarget1(String target1) {
        this.target1 = target1;
    }

    public String getTarget2() {
        return this.target2;
    }

    public void setTarget2(String target2) {
        this.target2 = target2;
    }

    public boolean isInFront1() {
        return this.inFront1;
    }

    public void setInFront1(boolean inFront1) {
        this.inFront1 = inFront1;
    }

    public boolean isInFront2() {
        return this.inFront2;
    }

    public void setInFront2(boolean inFront2) {
        this.inFront2 = inFront2;
    }

    public String toString() {
        return String.format("MetricData %s, metrics %s, %s. targets %s, %s", this.title, this.metric1, this.metric2, this.target1, this.target2);
    }
}
