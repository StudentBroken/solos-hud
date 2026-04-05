package com.ua.sdk.recorder;

import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.datapoint.DataFrame;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.data.DataFrameObserver;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface Recorder {
    void addDataFrameObserver(DataFrameObserver dataFrameObserver, DataTypeRef... dataTypeRefArr);

    void addDataSource(DataSourceConfiguration dataSourceConfiguration);

    void addRecorderObserver(RecorderObserver recorderObserver);

    void addSensorDataSourceObserver(SensorDataSourceObserver sensorDataSourceObserver);

    void destroy();

    @Deprecated
    List<DataFrame> getAllDataFrames();

    DataFrame getDataFrame();

    List<DataSourceIdentifier> getDataSources();

    RecorderWorkoutConverter getRecorderWorkoutConverter();

    void removeDataFrameObserver(DataFrameObserver dataFrameObserver);

    boolean removeDataSource(DataSourceIdentifier dataSourceIdentifier);

    void removeRecorderObserver(RecorderObserver recorderObserver);

    void removeSensorDataSourceObserver(SensorDataSourceObserver sensorDataSourceObserver);

    void startSegment();

    void stopSegment();

    void updateRecorderActivityType(ActivityTypeRef activityTypeRef);
}
