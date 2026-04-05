package com.ua.sdk.aggregate;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface Aggregate extends Entity<EntityRef<Aggregate>> {
    EntityRef<DataType> getDataTypeRef();

    Period getPeriod();

    List<AggregateSummary> getPeriods();

    AggregateSummary getSummary();

    EntityRef<User> getUserRef();
}
