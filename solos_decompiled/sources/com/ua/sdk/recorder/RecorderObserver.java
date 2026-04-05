package com.ua.sdk.recorder;

import com.ua.sdk.datapoint.DataFrame;

/* JADX INFO: loaded from: classes65.dex */
public interface RecorderObserver {
    void onSegmentStateUpdated(DataFrame dataFrame);

    void onTimeUpdated(double d, double d2);
}
