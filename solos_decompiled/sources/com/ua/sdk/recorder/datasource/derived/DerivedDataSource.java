package com.ua.sdk.recorder.datasource.derived;

import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.recorder.datasource.DataSource;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public abstract class DerivedDataSource extends DataSource {
    public abstract void deriveDataPoint(RecorderCalculator recorderCalculator, DataSourceIdentifier dataSourceIdentifier, DataTypeRef dataTypeRef, DataPoint dataPoint);

    public abstract List<DataTypeRef> getDataTypeRefTriggers();

    protected DerivedDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs) {
        super(dataSourceIdentifier, dataTypeRefs);
    }
}
