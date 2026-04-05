package com.ua.sdk.recorder;

import com.ua.sdk.UaLog;
import com.ua.sdk.datapoint.DataFrame;
import com.ua.sdk.datapoint.DataFrameImpl;
import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.datasource.DataSource;
import com.ua.sdk.recorder.datasource.derived.DerivedDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderCalculator {
    private static final String TAG = "RecordSessionCalculator";
    private RecorderCalculatorListener listener;
    private List<DataSourceIdentifier> messageUpdatedDataSourceIdentifiers;
    private Set<DataTypeRef> messageUpdatedDataTypes;
    private boolean messageUpdatedSegmentState;
    private boolean messageUpdatedTime;
    private RecorderContext recorderContext;
    private boolean segmentStarted = false;
    private double segmentStartTimestamp = Double.NaN;
    private double segmentStopTimestamp = Double.NaN;
    private double activeTimeAdjustment = Double.NaN;
    private double activeTime = Double.NaN;
    private double firstSegmentStartTime = Double.NaN;
    private double elapsedTime = Double.NaN;
    private Map<String, DataPoint> dataPoints = new HashMap();
    private List<DataSource> dataSources = new ArrayList();
    private Map<DataTypeRef, List<DerivedDataSource>> derivedDataSourceTriggers = new HashMap();
    private Map<DataTypeRef, DataSourceIdentifier> primaryDataSource = new HashMap();

    protected interface RecorderCalculatorListener {
        boolean isDataTypeObserved(DataTypeRef dataTypeRef);

        boolean isSegmentStateObserved();

        boolean isTimeObserved();

        void onDataTypeUpdated(DataFrameImpl dataFrameImpl);

        void onSegmentStateUpdated(DataFrameImpl dataFrameImpl);

        void onTimeUpdated(DataFrameImpl dataFrameImpl);

        void storeDataFrame(DataFrame dataFrame);
    }

    public RecorderCalculator(RecorderContext recorderContext, RecorderCalculatorListener recorderCalculatorListener) {
        this.recorderContext = recorderContext;
        this.listener = recorderCalculatorListener;
    }

    public List<DataSource> getDataSources() {
        return this.dataSources;
    }

    private Map<String, DataPoint> getDataPoints() {
        return new HashMap(this.dataPoints);
    }

    private Map<DataTypeRef, DataSourceIdentifier> getPrimaryDataSources() {
        return new HashMap(this.primaryDataSource);
    }

    public void onProcessMessage(Message message) {
        this.messageUpdatedDataTypes = new HashSet();
        this.messageUpdatedDataSourceIdentifiers = new ArrayList();
        this.messageUpdatedTime = false;
        this.messageUpdatedSegmentState = false;
        message.processMessage(this);
        DataFrameImpl dataFrame = null;
        if (!this.messageUpdatedDataTypes.isEmpty() && this.messageUpdatedDataTypes.size() == this.messageUpdatedDataSourceIdentifiers.size()) {
            Iterator<DataTypeRef> it = this.messageUpdatedDataTypes.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DataTypeRef dataTypeRef = it.next();
                if (this.listener.isDataTypeObserved(dataTypeRef)) {
                    dataFrame = createDataFrame(this.messageUpdatedDataTypes, this.messageUpdatedDataSourceIdentifiers);
                    this.listener.onDataTypeUpdated(dataFrame);
                    break;
                }
            }
        }
        if (this.messageUpdatedTime && this.listener.isTimeObserved()) {
            if (dataFrame == null) {
                dataFrame = createDataFrame();
            }
            this.listener.onTimeUpdated(dataFrame);
        }
        if (this.messageUpdatedSegmentState && this.listener.isSegmentStateObserved()) {
            if (dataFrame == null) {
                dataFrame = createDataFrame();
            }
            this.listener.onSegmentStateUpdated(dataFrame);
        }
        if (dataFrame == null) {
            dataFrame = createDataFrame();
        }
        this.listener.storeDataFrame(dataFrame);
    }

    public void updateDataPoint(DataSourceIdentifier dataSourceIdentifier, DataPoint dataPoint, DataTypeRef dataTypeRef) {
        this.dataPoints.put(TypeSourceKey.createDataPointKey(dataTypeRef, dataSourceIdentifier), dataPoint);
        if (this.messageUpdatedDataTypes.add(dataTypeRef)) {
            this.messageUpdatedDataSourceIdentifiers.add(dataSourceIdentifier);
        }
        if (this.derivedDataSourceTriggers.containsKey(dataTypeRef)) {
            List<DerivedDataSource> derivedDataSources = this.derivedDataSourceTriggers.get(dataTypeRef);
            for (DerivedDataSource derivedDataSource : derivedDataSources) {
                derivedDataSource.deriveDataPoint(this, dataSourceIdentifier, dataTypeRef, dataPoint);
            }
        }
        UaLog.info("RecordSessionCalculator." + dataTypeRef.getId());
    }

    public void startSegment(DataSourceIdentifier dataSourceIdentifier, double timestamp) {
        if (!this.segmentStarted) {
            if (Double.isNaN(this.segmentStartTimestamp)) {
                this.activeTimeAdjustment = timestamp;
            } else {
                this.activeTimeAdjustment += timestamp - this.segmentStopTimestamp;
            }
            this.segmentStartTimestamp = timestamp;
            this.segmentStarted = true;
            if (Double.isNaN(this.firstSegmentStartTime)) {
                this.firstSegmentStartTime = timestamp;
            }
            for (DataSource dataSource : this.dataSources) {
                dataSource.startSegment();
            }
            updateTime(dataSourceIdentifier, timestamp);
            this.messageUpdatedSegmentState = true;
            UaLog.info("RecordSessionCalculator.startSegment " + timestamp);
            return;
        }
        UaLog.warn("RecordSessionCalculator.startSegment ignored. already started.");
    }

    public void stopSegment(DataSourceIdentifier dataSourceIdentifier, double timestamp) {
        if (this.segmentStarted) {
            this.segmentStopTimestamp = timestamp;
            this.segmentStarted = false;
            for (DataSource dataSource : this.dataSources) {
                dataSource.stopSegment();
            }
            updateTime(dataSourceIdentifier, timestamp);
            this.messageUpdatedSegmentState = true;
            UaLog.info("RecordSessionCalculator.stopSegment " + timestamp);
            return;
        }
        UaLog.info("RecordSessionCalculator.stopSegment ignored. already started.");
    }

    public void updateTime(DataSourceIdentifier dataSourceIdentifier, double timestamp) {
        if (!Double.isNaN(this.firstSegmentStartTime)) {
            this.elapsedTime = Math.round(timestamp - this.firstSegmentStartTime);
            if (this.segmentStarted) {
                this.activeTime = Math.round(timestamp - this.activeTimeAdjustment);
            }
        }
        this.messageUpdatedTime = true;
        UaLog.info("RecordSessionCalculator.updateTime " + timestamp);
    }

    public void updateRecorderContext(RecorderContext recorderContext) {
        for (DataSource dataSource : this.dataSources) {
            dataSource.configure(recorderContext);
        }
    }

    public void addDataSource(DataSource dataSource) {
        this.dataSources.add(dataSource);
        DataSourceIdentifier dataSourceIdentifier = dataSource.getDataSourceIdentifier();
        for (DataTypeRef dataTypeRef : dataSource.getDataTypeRefs()) {
            this.primaryDataSource.put(dataTypeRef, dataSourceIdentifier);
        }
        if (dataSource instanceof DerivedDataSource) {
            for (DataTypeRef dataTypeRefTrigger : ((DerivedDataSource) dataSource).getDataTypeRefTriggers()) {
                List<DerivedDataSource> derivedDataSources = this.derivedDataSourceTriggers.get(dataTypeRefTrigger);
                if (derivedDataSources == null) {
                    derivedDataSources = new ArrayList<>();
                }
                derivedDataSources.add((DerivedDataSource) dataSource);
                this.derivedDataSourceTriggers.put(dataTypeRefTrigger, derivedDataSources);
            }
        }
    }

    public void removeDataSource(DataSource dataSource) {
        this.dataSources.remove(dataSource);
    }

    public double getElapsedTime() {
        return this.elapsedTime;
    }

    public double getActiveTime() {
        return this.activeTime;
    }

    protected boolean isSegmentStarted() {
        return this.segmentStarted;
    }

    protected double getFirstSegmentStartTime() {
        return this.firstSegmentStartTime;
    }

    protected DataFrameImpl createDataFrame(Set<DataTypeRef> updatedDataTypes, List<DataSourceIdentifier> updatedDataSourceIdentifiers) {
        DataFrameImpl dataFrame = new DataFrameImpl();
        dataFrame.setDataPoints(getDataPoints());
        dataFrame.setPrimaryDataSources(getPrimaryDataSources());
        dataFrame.setFirstSegmentStartTime(Double.valueOf(getFirstSegmentStartTime()));
        dataFrame.setActiveTime(Double.valueOf(getActiveTime()));
        dataFrame.setElapsedTime(Double.valueOf(getElapsedTime()));
        dataFrame.setSegmentStarted(isSegmentStarted());
        dataFrame.setDataTypesChanged(updatedDataTypes);
        dataFrame.setDataSourceIdentifiersChanged(updatedDataSourceIdentifiers);
        return dataFrame;
    }

    protected DataFrameImpl createDataFrame() {
        return createDataFrame(null, null);
    }
}
