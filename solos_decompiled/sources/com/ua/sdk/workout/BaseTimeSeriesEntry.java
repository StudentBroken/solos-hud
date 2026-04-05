package com.ua.sdk.workout;

import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface BaseTimeSeriesEntry<T> extends Comparable<T>, Parcelable {
    double getOffset();

    Date getTime();

    Long getTimeInMillis();
}
