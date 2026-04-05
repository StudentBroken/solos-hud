package com.ua.sdk.activitytimeseries;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityTimeSeries extends Resource {
    long getCalorieEpoch(int i);

    double getCalorieValue(int i);

    int getCaloriesSize();

    long getDistanceEpoch(int i);

    double getDistanceValue(int i);

    int getDistancesSize();

    String getRecorderIdentifier();

    String getRecorderTypeKey();

    long getStepEpoch(int i);

    int getStepValue(int i);

    int getStepsSize();
}
