package com.ua.sdk.sleep;

import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface SleepManager {
    Request deleteSleep(SleepRef sleepRef, DeleteCallback<SleepRef> deleteCallback);

    SleepRef deleteSleep(SleepRef sleepRef) throws UaException;

    Request fetchSleep(SleepRef sleepRef, FetchCallback<SleepMetric> fetchCallback);

    SleepMetric fetchSleep(SleepRef sleepRef) throws UaException;

    EntityList<SleepMetric> fetchSleepList(SleepListRef sleepListRef) throws UaException;

    Request fetchSleepList(SleepListRef sleepListRef, FetchCallback<EntityList<SleepMetric>> fetchCallback);

    SleepMetricBuilder getSleepMetricBuilder();

    Request updateSleep(SleepMetric sleepMetric, SaveCallback<SleepMetric> saveCallback);

    SleepMetric updateSleep(SleepMetric sleepMetric) throws UaException;
}
