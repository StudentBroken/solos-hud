package com.kopin.solos.pages;

import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes37.dex */
public class PageBoxInfo {
    public final String metric;
    public final String page;
    public final String target;
    public final String unit;
    public final String value;

    public PageBoxInfo(String metric, String value, String unit) {
        this(metric, metric, value, unit, "target_value");
    }

    public PageBoxInfo(String page, String metric, String value, String unit, String target) {
        this.page = page;
        this.metric = metric;
        this.value = value;
        this.unit = unit;
        this.target = target;
    }

    public boolean isMultiPage() {
        return this.page != this.metric;
    }

    public boolean isMatchMetric(MetricType metricType) {
        return this.metric.equalsIgnoreCase(metricType.getResource());
    }

    public boolean isMatchPage(MetricType metricType) {
        return this.page != null && this.page.equalsIgnoreCase(metricType.getResource());
    }

    public boolean isMatch(MetricType metricType) {
        return isMatchMetric(metricType) || isMatchPage(metricType);
    }

    public boolean isMatch(MetricType... metricTypes) {
        for (MetricType metricType : metricTypes) {
            if (isMatch(metricType)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "PageBoxInfo(" + this.metric + ", " + this.value + ", " + this.unit + ", " + this.target + ")";
    }
}
