package com.ua.sdk.recorder.datasource.derived;

import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataPointImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.recorder.datasource.Average;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class SpeedSummaryDerivedDataSource extends DerivedDataSource {
    private Average<Double> average;
    private List<DataTypeRef> dataTypeRefTriggers;
    private boolean recordData;
    private DataPointImpl summaryDataPoint;

    protected SpeedSummaryDerivedDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs) {
        super(dataSourceIdentifier, dataTypeRefs);
        this.dataTypeRefTriggers = new ArrayList(Arrays.asList(BaseDataTypes.TYPE_SPEED.getRef()));
        this.average = new Average<>();
        this.recordData = false;
    }

    @Override // com.ua.sdk.recorder.datasource.derived.DerivedDataSource
    public void deriveDataPoint(RecorderCalculator recorderCalculator, DataSourceIdentifier triggerDataSourceIdentifier, DataTypeRef triggerDataTypeRef, DataPoint triggerDataPoint) {
        if (this.dataTypeRefTriggers.contains(triggerDataTypeRef) && this.recordData) {
            if (this.summaryDataPoint == null) {
                this.summaryDataPoint = new DataPointImpl();
                this.summaryDataPoint.setStartDatetime(triggerDataPoint.getDatetime());
            }
            this.summaryDataPoint.setDatetime(triggerDataPoint.getDatetime());
            DataField max = BaseDataTypes.FIELD_SPEED_MAX;
            DataField min = BaseDataTypes.FIELD_SPEED_MIN;
            DataField avg = BaseDataTypes.FIELD_SPEED_AVG;
            Double current = triggerDataPoint.getValueDouble(BaseDataTypes.FIELD_SPEED);
            Double currentMax = this.summaryDataPoint.getValueDouble(max);
            Double currentMin = this.summaryDataPoint.getValueDouble(min);
            if (currentMax == null || current.doubleValue() > currentMax.doubleValue()) {
                this.summaryDataPoint.setValue(max, current);
            }
            if (currentMin == null || current.doubleValue() < currentMin.doubleValue()) {
                this.summaryDataPoint.setValue(min, current);
            }
            this.average.addValue(current);
            this.summaryDataPoint.setValue(avg, Double.valueOf(this.average.getAverage()));
            recorderCalculator.updateDataPoint(this.dataSourceIdentifier, this.summaryDataPoint, BaseDataTypes.TYPE_SPEED_SUMMARY.getRef());
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
        this.average.reset();
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void startSegment() {
        this.recordData = true;
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void stopSegment() {
        this.recordData = false;
    }
}
