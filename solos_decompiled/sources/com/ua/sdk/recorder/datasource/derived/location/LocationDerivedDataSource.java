package com.ua.sdk.recorder.datasource.derived.location;

import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataPointImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.recorder.datasource.derived.DerivedDataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class LocationDerivedDataSource extends DerivedDataSource {
    private List<DataTypeRef> dataTypeRefTriggers;

    public LocationDerivedDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs) {
        super(dataSourceIdentifier, dataTypeRefs);
        this.dataTypeRefTriggers = new ArrayList(Arrays.asList(BaseDataTypes.TYPE_LOCATION.getRef()));
    }

    @Override // com.ua.sdk.recorder.datasource.derived.DerivedDataSource
    public void deriveDataPoint(RecorderCalculator recorderCalculator, DataSourceIdentifier triggerDataSourceIdentifier, DataTypeRef triggerDataTypeRef, DataPoint triggerDataPoint) {
        if (this.dataTypeRefTriggers.contains(triggerDataTypeRef)) {
            DataPointImpl derivedLocationDataPoint = new DataPointImpl();
            derivedLocationDataPoint.setValue(BaseDataTypes.FIELD_LATITUDE, triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE));
            derivedLocationDataPoint.setValue(BaseDataTypes.FIELD_LONGITUDE, triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE));
            derivedLocationDataPoint.setValue(BaseDataTypes.FIELD_HORIZONTAL_ACCURACY, triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_HORIZONTAL_ACCURACY));
            recorderCalculator.updateDataPoint(this.dataSourceIdentifier, derivedLocationDataPoint, BaseDataTypes.TYPE_LOCATION.getRef());
        }
    }

    @Override // com.ua.sdk.recorder.datasource.derived.DerivedDataSource
    public List<DataTypeRef> getDataTypeRefTriggers() {
        return this.dataTypeRefTriggers;
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void connectDataSource() {
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void disconnectDataSource() {
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void startSegment() {
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void stopSegment() {
    }
}
