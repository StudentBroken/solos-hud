package com.kopin.solos.config;

import com.kopin.solos.metrics.MetricResource;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes37.dex */
public class AppConfig {
    static final Map<MetricResource, MetricItem> metricItems = new HashMap();

    public static Double getValue(MetricResource metricResource) {
        MetricItem metricItem = metricItems.get(metricResource);
        if (metricItem != null) {
            return metricItem.getValue();
        }
        return null;
    }
}
