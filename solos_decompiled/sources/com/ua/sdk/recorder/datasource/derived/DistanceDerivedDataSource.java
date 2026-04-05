package com.ua.sdk.recorder.datasource.derived;

import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataPointImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderCalculator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class DistanceDerivedDataSource extends DerivedDataSource {
    private List<DataTypeRef> dataTypeRefTriggers;
    private boolean isStarted;
    private DataPoint previousDataPoint;
    private DataPoint previousDistanceDataPoint;

    public DistanceDerivedDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs) {
        super(dataSourceIdentifier, dataTypeRefs);
        this.dataTypeRefTriggers = new ArrayList(Arrays.asList(BaseDataTypes.TYPE_LOCATION.getRef()));
    }

    @Override // com.ua.sdk.recorder.datasource.derived.DerivedDataSource
    public void deriveDataPoint(RecorderCalculator recorderCalculator, DataSourceIdentifier triggerDataSourceIdentifier, DataTypeRef triggerDataTypeRef, DataPoint triggerDataPoint) {
        if (this.dataTypeRefTriggers.contains(triggerDataTypeRef) && this.isStarted) {
            if (this.previousDataPoint != null) {
                float[] distanceBearingResult = new float[2];
                DistanceBearingCalculator.calculateDistanceAndBearing(this.previousDataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE).doubleValue(), this.previousDataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE).doubleValue(), triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_LATITUDE).doubleValue(), triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_LONGITUDE).doubleValue(), distanceBearingResult);
                double curDistance = this.previousDistanceDataPoint == null ? 0.0d : this.previousDistanceDataPoint.getValueDouble(BaseDataTypes.FIELD_DISTANCE).doubleValue();
                double newDistance = curDistance + ((double) distanceBearingResult[0]);
                DataPointImpl distanceDataPoint = new DataPointImpl();
                distanceDataPoint.setDatetime(triggerDataPoint.getDatetime());
                distanceDataPoint.setValue(BaseDataTypes.FIELD_DISTANCE, Double.valueOf(newDistance));
                recorderCalculator.updateDataPoint(this.dataSourceIdentifier, distanceDataPoint, BaseDataTypes.TYPE_DISTANCE.getRef());
                this.previousDistanceDataPoint = distanceDataPoint;
            }
            this.previousDataPoint = triggerDataPoint;
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
        this.isStarted = true;
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void stopSegment() {
        this.isStarted = false;
    }
}
