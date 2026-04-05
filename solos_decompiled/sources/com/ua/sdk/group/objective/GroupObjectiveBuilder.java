package com.ua.sdk.group.objective;

import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.internal.Period;
import com.ua.sdk.internal.Precondition;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class GroupObjectiveBuilder {
    Criteria criteria;
    String dataTypeField;
    DataTypeRef dataTypeRef;
    Date endDate;
    int iteration;
    Date iterationEndDate;
    Date iterationStartDate;
    String name;
    Period period;
    Date startDate;

    public GroupObjectiveBuilder setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public GroupObjectiveBuilder setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public GroupObjectiveBuilder setIterationStartDate(Date iterationStartDate) {
        this.iterationStartDate = iterationStartDate;
        return this;
    }

    public GroupObjectiveBuilder setIterationEndDate(Date iterationEndDate) {
        this.iterationEndDate = iterationEndDate;
        return this;
    }

    public GroupObjectiveBuilder setIteration(int iteration) {
        this.iteration = iteration;
        return this;
    }

    public GroupObjectiveBuilder setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public GroupObjectiveBuilder setDataType(DataType dataType) {
        Precondition.isNotNull(dataType);
        this.dataTypeRef = dataType.getRef();
        return this;
    }

    public GroupObjectiveBuilder setDataField(DataField dataField) {
        Precondition.isNotNull(dataField);
        this.dataTypeField = dataField.getId();
        return this;
    }

    public GroupObjectiveBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public GroupObjectiveBuilder setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public GroupObjective build() {
        Precondition.isNotNull(this.dataTypeRef, "dataTypeRef");
        Precondition.isNotNull(this.dataTypeField, "dataTypeField");
        if (this.startDate == null && this.endDate == null) {
            Precondition.isNotNull(this.period);
        }
        if (this.period == null) {
            Precondition.isNotNull(this.startDate);
            Precondition.isNotNull(this.endDate);
        }
        GroupObjectiveImpl impl = new GroupObjectiveImpl();
        impl.setPeriod(this.period);
        impl.setStartDate(this.startDate);
        impl.setEndDate(this.endDate);
        impl.setIterationStartDate(this.iterationStartDate);
        impl.setIterationEndDate(this.iterationEndDate);
        impl.setIteration(Integer.valueOf(this.iteration));
        impl.setDataTypeField(this.dataTypeField);
        impl.setDataTypeRef(this.dataTypeRef);
        impl.setName(this.name);
        impl.setCriteria(this.criteria);
        return impl;
    }
}
