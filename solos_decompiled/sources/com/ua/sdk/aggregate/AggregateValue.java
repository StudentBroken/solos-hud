package com.ua.sdk.aggregate;

/* JADX INFO: loaded from: classes65.dex */
public interface AggregateValue {
    AggregateDetails getAggregateDetails();

    Double getAverage();

    Double getCount();

    Double getLatest();

    Double getMax();

    Double getMin();

    Double getSum();

    void setAggregateDetails(AggregateDetails aggregateDetails);

    void setAverage(Double d);

    void setCount(Double d);

    void setLatest(Double d);

    void setMax(Double d);

    void setMin(Double d);

    void setSum(Double d);
}
