package com.ua.sdk.datapoint;

import com.ua.sdk.Entity;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface DataType extends Entity<DataTypeRef> {
    String getDescription();

    List<DataField> getFields();

    String getId();

    DataPeriod getPeriod();
}
