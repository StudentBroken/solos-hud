package com.ua.sdk.actigraphy;

import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public interface Metric {
    AggregateValueImpl getAggregateValue();

    Date getEndDateTime();

    long getEpochTime(int i);

    long[] getEpochTimes();

    Date getStartDateTime();

    TimeZone getTimeZone();

    double getValue(int i);

    double[] getValues();

    void setAggregateValue(AggregateValueImpl aggregateValueImpl);

    void setEndDateTime(Date date);

    void setEpochTimes(long[] jArr);

    void setStartDateTime(Date date);

    void setTimeZone(TimeZone timeZone);

    void setValues(double[] dArr);
}
