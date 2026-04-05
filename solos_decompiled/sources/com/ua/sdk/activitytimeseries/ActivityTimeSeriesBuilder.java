package com.ua.sdk.activitytimeseries;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityTimeSeriesBuilder {
    ActivityTimeSeriesBuilder addCalories(long j, double d);

    ActivityTimeSeriesBuilder addDistance(long j, double d);

    ActivityTimeSeriesBuilder addSteps(long j, int i);

    ActivityTimeSeries build();

    ActivityTimeSeriesBuilder setRecorderIdentifier(String str);

    ActivityTimeSeriesBuilder setRecorderTypeKey(String str);
}
