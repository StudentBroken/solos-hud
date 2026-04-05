package com.ua.sdk.sleep;

import android.os.Parcelable;
import com.ua.sdk.sleep.SleepMetric;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public interface SleepMetricBuilder extends Parcelable {
    SleepMetricBuilder addEvent(long j, SleepMetric.State state);

    SleepMetric build();

    SleepMetricBuilder setEndDateTime(Date date);

    SleepMetricBuilder setRecorderTypeKey(String str);

    SleepMetricBuilder setReferenceKey(String str);

    SleepMetricBuilder setStartDateTime(Date date);

    SleepMetricBuilder setStartTimeZone(TimeZone timeZone);

    SleepMetricBuilder setTimeToSleep(int i);

    SleepMetricBuilder setTimesAwakened(int i);

    SleepMetricBuilder setTotalDeepSleep(int i);

    SleepMetricBuilder setTotalLightSleep(int i);

    SleepMetricBuilder setTotalTimeAwake(int i);
}
