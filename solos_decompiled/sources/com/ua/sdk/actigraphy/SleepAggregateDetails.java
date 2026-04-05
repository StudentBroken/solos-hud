package com.ua.sdk.actigraphy;

import com.ua.sdk.aggregate.AggregateDetails;

/* JADX INFO: loaded from: classes65.dex */
public interface SleepAggregateDetails extends AggregateDetails {
    Double getAwake();

    Double getDeepSleep();

    Double getLightSleep();

    Double getTimeToSleep();

    Integer getTimesAwaken();

    void setAwake(Double d);

    void setDeepSleep(Double d);

    void setLightSleep(Double d);

    void setTimeToSleep(Double d);

    void setTimesAwaken(Integer num);
}
