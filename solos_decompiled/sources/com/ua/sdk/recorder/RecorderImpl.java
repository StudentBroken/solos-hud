package com.ua.sdk.recorder;

import android.content.Context;
import android.os.Handler;
import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.LocalDate;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.activitytype.ActivityTypeManager;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.datapoint.DataFrame;
import com.ua.sdk.datapoint.DataFrameImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.heartrate.HeartRateZones;
import com.ua.sdk.heartrate.HeartRateZonesListRef;
import com.ua.sdk.heartrate.HeartRateZonesManager;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.recorder.RecorderCalculator;
import com.ua.sdk.recorder.data.DataFrameObserver;
import com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.DataSource;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.producer.CommandProducer;
import com.ua.sdk.recorder.producer.MessageProducer;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import com.ua.sdk.recorder.producer.TimeProducer;
import com.ua.sdk.recorder.save.RecorderWorkoutConverterImpl;
import com.ua.sdk.user.User;
import com.ua.sdk.user.UserManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderImpl implements Recorder {
    private static final int HANDLER_DISPATCH_DATA_TYPE_OBSERVERS = 1;
    private static final int HANDLER_DISPATCH_SEGMENT_STATE_OBSERVERS = 3;
    private static final int HANDLER_DISPATCH_SENSOR_STATE_OBSERVERS = 2;
    private static final int HANDLER_DISPATCH_TIME_OBSERVERS = 4;
    private ActivityTypeManager activityTypeManager;
    private CommandProducer commandProducer;
    private RecorderConfigurationImpl configuration;
    private Context context;
    private HeartRateZonesManager heartRateZonesManager;
    private boolean isConfigured;
    private boolean isConfiguring;
    private boolean isDestroying;
    private MessageQueue processorMessageQueue;
    private RecorderManagerImpl recordManager;
    private RecordProcessor recordProcessor;
    private RecorderCalculator recorderCalculator;
    private RecorderContext recorderContext;
    private SensorMessageProducer sensorMessageProducer;
    private TimeProducer timeProducer;
    private UserManager userManager;
    private List<MessageProducer> messageProducers = new ArrayList();
    private Map<DataTypeRef, List<DataFrameObserver>> dataFrameObservers = new ConcurrentHashMap();
    private List<SensorDataSourceObserver> sensorDataSourceObservers = new ArrayList();
    private List<RecorderObserver> recorderObservers = new ArrayList();
    private MyRecordSessionHandler handler = new MyRecordSessionHandler();

    @Deprecated
    private List<DataFrame> dataFrames = new ArrayList();

    protected interface RecorderConfigureCallback {
        void onConfigureFailed(UaException uaException);

        void onConfigureSuccess();
    }

    public RecorderImpl(RecorderConfiguration recorderConfiguration, Context context, UserManager userManager, ActivityTypeManager activityTypeManager, HeartRateZonesManager heartRateZonesManager, RecorderManagerImpl recordManager) {
        this.configuration = (RecorderConfigurationImpl) Precondition.isNotNull(recorderConfiguration);
        this.context = (Context) Precondition.isNotNull(context);
        this.userManager = (UserManager) Precondition.isNotNull(userManager);
        this.activityTypeManager = (ActivityTypeManager) Precondition.isNotNull(activityTypeManager);
        this.heartRateZonesManager = (HeartRateZonesManager) Precondition.isNotNull(heartRateZonesManager);
        this.recordManager = (RecorderManagerImpl) Precondition.isNotNull(recordManager);
        Precondition.isNotNull(this.configuration.getName(), "recorder name");
        Precondition.isNotNull(this.configuration.getActivityTypeRef(), "activity type");
        Precondition.isNotNull(this.configuration.getUserRef(), "user ref");
    }

    void configure(RecorderConfigureCallback callback) {
        this.isConfiguring = true;
        this.isConfigured = false;
        this.recorderContext = new RecorderContext();
        this.recorderContext.setApplicationContext(this.context);
        this.recorderContext.setName(this.configuration.getName());
        RecorderClock recorderClock = new RecorderClock();
        recorderClock.init();
        this.recorderContext.setClock(recorderClock);
        this.processorMessageQueue = new MessageQueue();
        this.commandProducer = new CommandProducer(recorderClock, this.processorMessageQueue);
        this.messageProducers.add(this.commandProducer);
        this.timeProducer = new TimeProducer(recorderClock, this.processorMessageQueue);
        this.messageProducers.add(this.timeProducer);
        this.sensorMessageProducer = new SensorMessageProducer(recorderClock, this.processorMessageQueue);
        this.messageProducers.add(this.sensorMessageProducer);
        this.userManager.fetchUser(this.configuration.getUserRef(), new MyFetchUserCallback(callback));
    }

    void onPostConfigureLoad(RecorderConfigureCallback callback) {
        this.recorderCalculator = new RecorderCalculator(this.recorderContext, new MyRecordCalculatorListener());
        this.recordProcessor = new RecordProcessor(this.configuration.getName(), this.processorMessageQueue, this.recorderCalculator);
        this.isConfigured = true;
        for (MessageProducer messageProducer : this.messageProducers) {
            messageProducer.beginRecorder();
        }
        for (DataSourceConfiguration dataSourceConfiguration : this.configuration.getDataSourceConfigurations()) {
            addDataSource(dataSourceConfiguration, false);
        }
        this.dataFrames.add(this.recorderCalculator.createDataFrame());
        this.recordProcessor.begin();
        this.isConfiguring = false;
        callback.onConfigureSuccess();
        if (this.isDestroying) {
            destroyRecorder();
        }
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void startSegment() {
        checkConfigured();
        this.commandProducer.produceStartSegment();
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void stopSegment() {
        checkConfigured();
        this.commandProducer.produceStopSegment();
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void destroy() {
        checkConfigured();
        this.isConfigured = false;
        this.isDestroying = true;
        if (!this.isConfiguring) {
            destroyRecorder();
        }
    }

    private void destroyRecorder() {
        this.recordProcessor.finish();
        for (MessageProducer messageProducer : this.messageProducers) {
            messageProducer.finishRecorder();
        }
        for (DataSource dataSource : this.recorderCalculator.getDataSources()) {
            dataSource.disconnectDataSource();
        }
        this.recordManager.destroyRecorder(this.configuration.getName());
    }

    @Override // com.ua.sdk.recorder.Recorder
    public DataFrame getDataFrame() {
        checkConfigured();
        if (this.dataFrames.isEmpty()) {
            return null;
        }
        return this.dataFrames.get(this.dataFrames.size() - 1);
    }

    @Override // com.ua.sdk.recorder.Recorder
    public List<DataFrame> getAllDataFrames() {
        checkConfigured();
        return new ArrayList(this.dataFrames);
    }

    @Override // com.ua.sdk.recorder.Recorder
    public RecorderWorkoutConverter getRecorderWorkoutConverter() {
        checkConfigured();
        return new RecorderWorkoutConverterImpl(getAllDataFrames(), this.recorderContext);
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void addDataSource(DataSourceConfiguration dataSourceConfiguration) {
        addDataSource(dataSourceConfiguration, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void addDataSource(DataSourceConfiguration dataSourceConfiguration, boolean isUpdate) {
        checkConfigured();
        DataSource dataSource = ((AbstractDataSourceConfiguration) dataSourceConfiguration).build(this.sensorMessageProducer, new MySensorDataSourceListener());
        dataSource.configure(this.recorderContext);
        dataSource.connectDataSource();
        this.recorderCalculator.addDataSource(dataSource);
        if (isUpdate) {
            this.recordManager.addDataSourceRecorderCache(dataSourceConfiguration);
        }
    }

    @Override // com.ua.sdk.recorder.Recorder
    public boolean removeDataSource(DataSourceIdentifier dataSourceIdentifier) {
        checkConfigured();
        for (DataSource dataSource : this.recorderCalculator.getDataSources()) {
            if (dataSource.getDataSourceIdentifier().equals(dataSourceIdentifier)) {
                dataSource.disconnectDataSource();
                this.recorderCalculator.removeDataSource(dataSource);
                this.recordManager.removeDataSourceRecorderCache(dataSourceIdentifier);
                return true;
            }
        }
        return false;
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void updateRecorderActivityType(ActivityTypeRef activityTypeRef) {
        checkConfigured();
        this.activityTypeManager.fetchActivityType(activityTypeRef, new MyUpdateActivityTypeFetchCallback());
    }

    @Override // com.ua.sdk.recorder.Recorder
    public List<DataSourceIdentifier> getDataSources() {
        checkConfigured();
        List<DataSourceIdentifier> dataSourceIdentifiers = new ArrayList<>();
        for (DataSource dataSource : this.recorderCalculator.getDataSources()) {
            dataSourceIdentifiers.add(dataSource.getDataSourceIdentifier());
        }
        return dataSourceIdentifiers;
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void addDataFrameObserver(DataFrameObserver dataFrameObserver, DataTypeRef... dataTypesRefs) {
        DataFrameImpl dataFrame = (DataFrameImpl) getDataFrame();
        for (DataTypeRef dataTypeRef : dataTypesRefs) {
            List<DataFrameObserver> typeObservers = this.dataFrameObservers.get(dataTypeRef);
            if (typeObservers == null) {
                typeObservers = new ArrayList<>();
                this.dataFrameObservers.put(dataTypeRef, typeObservers);
            }
            typeObservers.add(dataFrameObserver);
            if (dataFrame != null && dataFrame.getDataPoint(dataTypeRef) != null && dataFrame.getPrimaryDataSources().containsKey(dataTypeRef)) {
                dispatchDataFrame(dataFrame.getPrimaryDataSources().get(dataTypeRef), dataTypeRef, dataFrame);
            }
        }
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void removeDataFrameObserver(DataFrameObserver dataFrameObserver) {
        Iterator<Map.Entry<DataTypeRef, List<DataFrameObserver>>> itr = this.dataFrameObservers.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<DataTypeRef, List<DataFrameObserver>> entry = itr.next();
            entry.getValue().remove(dataFrameObserver);
            if (entry.getValue().isEmpty()) {
                itr.remove();
            }
        }
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void addSensorDataSourceObserver(SensorDataSourceObserver sensorDataSourceObserver) {
        checkConfigured();
        this.sensorDataSourceObservers.add(sensorDataSourceObserver);
        for (DataSource source : this.recorderCalculator.getDataSources()) {
            if (source instanceof SensorDataSource) {
                sensorDataSourceObserver.onSensorDataSourceStatus(source.getDataSourceIdentifier(), ((SensorDataSource) source).getSensorStatus(), ((SensorDataSource) source).getSensorHealth());
            }
        }
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void removeSensorDataSourceObserver(SensorDataSourceObserver sensorDataSourceObserver) {
        this.sensorDataSourceObservers.remove(sensorDataSourceObserver);
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void addRecorderObserver(RecorderObserver recorderObserver) {
        checkConfigured();
        this.recorderObservers.add(recorderObserver);
    }

    @Override // com.ua.sdk.recorder.Recorder
    public void removeRecorderObserver(RecorderObserver recorderObserver) {
        this.recorderObservers.remove(recorderObserver);
    }

    protected void dispatchDataFrame(DataSourceIdentifier dataSourceIdentifier, DataTypeRef dataTypeRef, DataFrame dataFrame) {
        android.os.Message msg = this.handler.obtainMessage(1, new DataFrameObserverData(dataSourceIdentifier, dataTypeRef, dataFrame));
        this.handler.sendMessage(msg);
    }

    protected void dispatchTime(DataFrameImpl dataFrame) {
        android.os.Message msg = this.handler.obtainMessage(4, dataFrame);
        this.handler.sendMessage(msg);
    }

    protected void dispatchSegmentState(DataFrameImpl dataFrame) {
        android.os.Message msg = this.handler.obtainMessage(3, dataFrame);
        this.handler.sendMessage(msg);
    }

    protected void dispatchSensorStateChanged(DataSourceIdentifier dataSourceIdentifier, SensorStatus status, SensorHealth health) {
        android.os.Message msg = this.handler.obtainMessage(2, new SensorDataSourceObserverData(dataSourceIdentifier, status, health));
        this.handler.sendMessage(msg);
    }

    private void checkConfigured() {
        if (!this.isConfigured) {
            throw new IllegalStateException("Cannot call method before configure callback is called");
        }
        if (this.isDestroying) {
            throw new IllegalStateException("Cannot call method after destroy is called");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAge(User user) {
        LocalDate birthDate = user.getBirthdate();
        Calendar today = Calendar.getInstance();
        if (birthDate == null) {
            return 35;
        }
        int age = today.get(1) - birthDate.getYear();
        if (today.get(2) < birthDate.getMonth()) {
            return age - 1;
        }
        if (today.get(2) == birthDate.getMonth() && today.get(5) < birthDate.getDayOfMonth()) {
            return age - 1;
        }
        return age;
    }

    protected class MyRecordCalculatorListener implements RecorderCalculator.RecorderCalculatorListener {
        protected MyRecordCalculatorListener() {
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public void storeDataFrame(DataFrame dataFrame) {
            RecorderImpl.this.dataFrames.add(dataFrame);
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public boolean isDataTypeObserved(DataTypeRef dataTypeRef) {
            return RecorderImpl.this.dataFrameObservers.containsKey(dataTypeRef);
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public void onDataTypeUpdated(DataFrameImpl dataFrame) {
            int index = 0;
            List<DataSourceIdentifier> dataSourceIdentifiers = dataFrame.getDataSourceIdentifiersChanged();
            for (DataTypeRef dataTypeRef : dataFrame.getDataTypesChanged()) {
                RecorderImpl.this.dispatchDataFrame(dataSourceIdentifiers.get(index), dataTypeRef, dataFrame);
                index++;
            }
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public boolean isTimeObserved() {
            return !RecorderImpl.this.recorderObservers.isEmpty();
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public void onTimeUpdated(DataFrameImpl dataFrame) {
            RecorderImpl.this.dispatchTime(dataFrame);
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public boolean isSegmentStateObserved() {
            return !RecorderImpl.this.recorderObservers.isEmpty();
        }

        @Override // com.ua.sdk.recorder.RecorderCalculator.RecorderCalculatorListener
        public void onSegmentStateUpdated(DataFrameImpl dataFrame) {
            RecorderImpl.this.dispatchSegmentState(dataFrame);
        }
    }

    protected class MySensorDataSourceListener implements SensorDataSource.SensorDataSourceListener {
        protected MySensorDataSourceListener() {
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.SensorDataSource.SensorDataSourceListener
        public void onSensorStateChanged(DataSourceIdentifier dataSourceIdentifier, SensorStatus status, SensorHealth health) {
            if (RecorderImpl.this.sensorDataSourceObservers != null && !RecorderImpl.this.sensorDataSourceObservers.isEmpty()) {
                RecorderImpl.this.dispatchSensorStateChanged(dataSourceIdentifier, status, health);
            }
        }
    }

    protected class MyRecordSessionHandler extends Handler {
        protected MyRecordSessionHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    DataFrameObserverData observerData = (DataFrameObserverData) msg.obj;
                    List<DataFrameObserver> typeObservers = (List) RecorderImpl.this.dataFrameObservers.get(observerData.dataTypeRef);
                    if (typeObservers != null) {
                        for (DataFrameObserver observer : typeObservers) {
                            observer.onDataFrameUpdated(observerData.dataSourceIdentifier, observerData.dataTypeRef, observerData.dataFrame);
                        }
                    }
                    break;
                case 2:
                    SensorDataSourceObserverData observerData2 = (SensorDataSourceObserverData) msg.obj;
                    for (SensorDataSourceObserver observer2 : RecorderImpl.this.sensorDataSourceObservers) {
                        observer2.onSensorDataSourceStatus(observerData2.dataSourceIdentifier, observerData2.status, observerData2.health);
                    }
                    break;
                case 3:
                    for (RecorderObserver recorderObserver : RecorderImpl.this.recorderObservers) {
                        recorderObserver.onSegmentStateUpdated((DataFrameImpl) msg.obj);
                    }
                    break;
                case 4:
                    DataFrameImpl dataFrame = (DataFrameImpl) msg.obj;
                    double activeTime = dataFrame.getActiveTime().doubleValue();
                    double elapsedTime = dataFrame.getElapsedTime().doubleValue();
                    for (RecorderObserver recorderObserver2 : RecorderImpl.this.recorderObservers) {
                        recorderObserver2.onTimeUpdated(activeTime, elapsedTime);
                    }
                    break;
                default:
                    UaLog.error("RecordSessionImpl unknown handler what");
                    break;
            }
        }
    }

    private class MyFetchUserCallback implements FetchCallback<User> {
        private RecorderConfigureCallback callback;

        private MyFetchUserCallback(RecorderConfigureCallback callback) {
            this.callback = callback;
        }

        @Override // com.ua.sdk.FetchCallback
        public void onFetched(User userEntity, UaException e) {
            if (userEntity != null) {
                RecorderImpl.this.recorderContext.setUser(userEntity);
                RecorderImpl.this.activityTypeManager.fetchActivityType(RecorderImpl.this.configuration.getActivityTypeRef(), new MyFetchActivityTypeCallback(this.callback));
            } else {
                this.callback.onConfigureFailed(e);
            }
        }
    }

    private class MyFetchActivityTypeCallback implements FetchCallback<ActivityType> {
        private RecorderConfigureCallback callback;

        private MyFetchActivityTypeCallback(RecorderConfigureCallback callback) {
            this.callback = callback;
        }

        @Override // com.ua.sdk.FetchCallback
        public void onFetched(ActivityType activityTypeEntity, UaException e) {
            if (activityTypeEntity != null) {
                RecorderImpl.this.recorderContext.setActivityType(activityTypeEntity);
                RecorderImpl.this.heartRateZonesManager.fetchHeartRateZonesList(HeartRateZonesListRef.getBuilder().setUser(RecorderImpl.this.recorderContext.getUser().getRef()).build(), new MyFetchHeartRateZonesCallback(this.callback));
            } else {
                this.callback.onConfigureFailed(e);
            }
        }
    }

    private class MyFetchHeartRateZonesCallback implements FetchCallback<EntityList<HeartRateZones>> {
        private RecorderConfigureCallback callback;

        private MyFetchHeartRateZonesCallback(RecorderConfigureCallback callback) {
            this.callback = callback;
        }

        @Override // com.ua.sdk.FetchCallback
        public void onFetched(EntityList<HeartRateZones> heartRateZonesEntityList, UaException e) {
            if (e != null) {
                this.callback.onConfigureFailed(e);
            }
            if (heartRateZonesEntityList == null || heartRateZonesEntityList.isEmpty()) {
                RecorderImpl.this.recorderContext.setHeartRateZones(RecorderImpl.this.heartRateZonesManager.calculateHeartRateZonesWithAge(RecorderImpl.this.getAge(RecorderImpl.this.recorderContext.getUser())));
            } else {
                RecorderImpl.this.recorderContext.setHeartRateZones(heartRateZonesEntityList.get(0));
            }
            RecorderImpl.this.onPostConfigureLoad(this.callback);
        }
    }

    private class MyUpdateActivityTypeFetchCallback implements FetchCallback<ActivityType> {
        private MyUpdateActivityTypeFetchCallback() {
        }

        @Override // com.ua.sdk.FetchCallback
        public void onFetched(ActivityType activityType, UaException e) {
            if (e == null) {
                RecorderImpl.this.recorderContext.setActivityType(activityType);
                RecorderImpl.this.commandProducer.produceRecorderContext(RecorderImpl.this.recorderContext);
            }
        }
    }

    private static class DataFrameObserverData {
        DataFrame dataFrame;
        DataSourceIdentifier dataSourceIdentifier;
        DataTypeRef dataTypeRef;

        DataFrameObserverData(DataSourceIdentifier dataSourceIdentifier, DataTypeRef dataTypeRef, DataFrame dataFrame) {
            this.dataSourceIdentifier = dataSourceIdentifier;
            this.dataTypeRef = dataTypeRef;
            this.dataFrame = dataFrame;
        }
    }

    private static class SensorDataSourceObserverData {
        DataSourceIdentifier dataSourceIdentifier;
        SensorHealth health;
        SensorStatus status;

        SensorDataSourceObserverData(DataSourceIdentifier dataSourceIdentifier, SensorStatus status, SensorHealth health) {
            this.dataSourceIdentifier = dataSourceIdentifier;
            this.status = status;
            this.health = health;
        }
    }
}
