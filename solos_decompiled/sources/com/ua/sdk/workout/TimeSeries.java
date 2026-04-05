package com.ua.sdk.workout;

import android.os.Parcelable;
import com.ua.sdk.workout.BaseTimeSeriesEntry;

/* JADX INFO: loaded from: classes65.dex */
public interface TimeSeries<T extends BaseTimeSeriesEntry> extends Iterable<T>, Parcelable {
    T get(int i);

    int getSize();
}
