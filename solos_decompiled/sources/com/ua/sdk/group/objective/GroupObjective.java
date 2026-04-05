package com.ua.sdk.group.objective;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.internal.Period;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupObjective extends Entity<EntityRef<GroupObjective>> {
    Criteria getCriteria();

    DataField getDataField();

    DataType getDataType();

    EntityRef<DataType> getDataTypeRef();

    Date getEndDate();

    Integer getIteration();

    Date getIterationEndDate();

    Date getIterationStartDate();

    String getName();

    Period getPeriod();

    Date getStartDate();

    void setCriteria(Criteria criteria);
}
