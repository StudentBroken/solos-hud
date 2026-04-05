package com.ua.sdk.recorder;

import android.content.Context;
import com.ua.sdk.IntensityCalculator;
import com.ua.sdk.MetabolicEnergyCalculator;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitytype.ActivityTypeManager;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilder;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilderImpl;
import com.ua.sdk.device.DeviceBuilder;
import com.ua.sdk.device.DeviceBuilderImpl;
import com.ua.sdk.heartrate.HeartRateZonesManager;
import com.ua.sdk.recorder.RecorderImpl;
import com.ua.sdk.recorder.RecorderManager;
import com.ua.sdk.recorder.datasource.AbstractDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.derived.DerivedDataSourceConfigurationImpl;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothSensorDataSourceConfigurationImpl;
import com.ua.sdk.recorder.datasource.sensor.location.LocationSensorSensorDataSourceConfigurationImpl;
import com.ua.sdk.recorder.persistence.RecorderConfigurationDatabase;
import com.ua.sdk.user.UserManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderManagerImpl implements RecorderManager {
    private ActivityTypeManager activityTypeManager;
    private Context appContext;
    private ExecutorService executorService;
    private HeartRateZonesManager heartRateZonesManager;
    private IntensityCalculator intensityCalculator;
    private MetabolicEnergyCalculator metabolicEnergyCalculator;
    private RecorderConfigurationImpl recorderConfiguration;
    private RecorderConfigurationDatabase recorderConfigurationCache;
    private UserManager userManager;
    private Map<String, Recorder> recorders = new HashMap();
    private List<RecorderManagerObserver> recorderManagerObservers = new ArrayList();

    public RecorderManagerImpl(Context appContext, ExecutorService executorService, UserManager userManager, ActivityTypeManager activityTypeManager, HeartRateZonesManager heartRateZonesManager, MetabolicEnergyCalculator metabolicEnergyCalculator, IntensityCalculator intensityCalculator, RecorderConfigurationDatabase recorderConfigurationCache) {
        this.appContext = appContext;
        this.executorService = executorService;
        this.userManager = userManager;
        this.activityTypeManager = activityTypeManager;
        this.heartRateZonesManager = heartRateZonesManager;
        this.metabolicEnergyCalculator = metabolicEnergyCalculator;
        this.intensityCalculator = intensityCalculator;
        this.recorderConfigurationCache = recorderConfigurationCache;
        recoverCachedRecorders();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public void addRecorderManagerObserver(RecorderManagerObserver recorderManagerObserver) {
        this.recorderManagerObservers.add(recorderManagerObserver);
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public void removeRecorderManagerObserver(RecorderManagerObserver recorderManagerObserver) {
        this.recorderManagerObservers.add(recorderManagerObserver);
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public RecorderConfiguration createRecorderConfiguration() {
        return new RecorderConfigurationImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public BluetoothSensorDataSourceConfiguration createBluetoothDataSourceConfiguration() {
        return new BluetoothSensorDataSourceConfigurationImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public LocationSensorDataSourceConfiguration createLocationDataSourceConfiguration() {
        return new LocationSensorSensorDataSourceConfigurationImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public DerivedDataSourceConfiguration createDerivedDataSourceConfiguration() {
        return new DerivedDataSourceConfigurationImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public DataSourceIdentifierBuilder getDataSourceIdentifierBuilder() {
        return new DataSourceIdentifierBuilderImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public DeviceBuilder getDeviceBuilder() {
        return new DeviceBuilderImpl();
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public void createRecorder(RecorderConfiguration recorderConfiguration, RecorderManager.CreateCallback createCallback) {
        createRecorder(recorderConfiguration, false, createCallback);
    }

    private void createRecorder(final RecorderConfiguration recorderConfiguration, boolean isRecovery, final RecorderManager.CreateCallback createCallback) {
        final RecorderImpl recorder = new RecorderImpl(recorderConfiguration, this.appContext, this.userManager, this.activityTypeManager, this.heartRateZonesManager, this);
        this.recorderConfiguration = (RecorderConfigurationImpl) recorderConfiguration;
        recorder.configure(new RecorderImpl.RecorderConfigureCallback() { // from class: com.ua.sdk.recorder.RecorderManagerImpl.1
            @Override // com.ua.sdk.recorder.RecorderImpl.RecorderConfigureCallback
            public void onConfigureSuccess() {
                String name = ((RecorderConfigurationImpl) recorderConfiguration).getName();
                RecorderManagerImpl.this.recorders.put(name, recorder);
                createCallback.onCreated(recorder, null);
                for (RecorderManagerObserver recorderManagerObserver : RecorderManagerImpl.this.recorderManagerObservers) {
                    recorderManagerObserver.onRecorderCreated(name);
                }
            }

            @Override // com.ua.sdk.recorder.RecorderImpl.RecorderConfigureCallback
            public void onConfigureFailed(UaException e) {
                createCallback.onCreated(null, e);
            }
        });
        if (!isRecovery) {
            insertRecorderCache();
        }
    }

    @Override // com.ua.sdk.recorder.RecorderManager
    public Recorder getRecorder(String recorderName) {
        return this.recorders.get(recorderName);
    }

    protected void destroyRecorder(String name) {
        this.recorders.remove(name);
        this.recorderConfigurationCache.delete(name);
        for (RecorderManagerObserver recorderManagerObserver : this.recorderManagerObservers) {
            recorderManagerObserver.onRecorderDestroyed(name);
        }
    }

    private void recoverCachedRecorders() {
        if (this.userManager.getCurrentUserRef() != null) {
            List<RecorderConfiguration> cachedConfigs = this.recorderConfigurationCache.getAllEntries(this.userManager.getCurrentUserRef().getId());
            if (cachedConfigs == null) {
                UaLog.info("No recorders to recover");
                return;
            }
            for (RecorderConfiguration configuration : cachedConfigs) {
                final String name = ((RecorderConfigurationImpl) configuration).getName();
                UaLog.warn("Recovering Recorder... name=" + name);
                createRecorder(configuration, true, new RecorderManager.CreateCallback() { // from class: com.ua.sdk.recorder.RecorderManagerImpl.2
                    @Override // com.ua.sdk.recorder.RecorderManager.CreateCallback
                    public void onCreated(Recorder recorder, UaException ex) {
                        UaLog.warn("Recorder Successfully Recovered. name=" + name);
                        for (RecorderManagerObserver recorderManagerObserver : RecorderManagerImpl.this.recorderManagerObservers) {
                            recorderManagerObserver.onRecorderRecovered(name);
                        }
                    }
                });
            }
        }
    }

    private void insertRecorderCache() {
        DataSourceConfigurationList dataSourceConfigurationList = convertToSerializableList(this.recorderConfiguration.getDataSourceConfigurations());
        Date date = new Date();
        this.recorderConfigurationCache.insert(this.recorderConfiguration.getName(), this.recorderConfiguration.getUserRef().getId(), this.recorderConfiguration.getActivityTypeRef().getId(), date, date, dataSourceConfigurationList);
    }

    protected void addDataSourceRecorderCache(DataSourceConfiguration newDataSourceConfiguration) {
        this.recorderConfiguration.addDataSource(newDataSourceConfiguration);
        DataSourceConfigurationList dataSourceConfigurationList = convertToSerializableList(this.recorderConfiguration.getDataSourceConfigurations());
        this.recorderConfigurationCache.update(this.recorderConfiguration.getName(), new Date(), dataSourceConfigurationList);
    }

    protected void removeDataSourceRecorderCache(DataSourceIdentifier dataSourceIdentifier) {
        List<DataSourceConfiguration> dataSourceConfigurations = this.recorderConfiguration.getDataSourceConfigurations();
        Iterator<DataSourceConfiguration> it = dataSourceConfigurations.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object obj = (DataSourceConfiguration) it.next();
            if (dataSourceIdentifier.equals(((AbstractDataSourceConfiguration) obj).dataSourceIdentifier)) {
                dataSourceConfigurations.remove(obj);
                break;
            }
        }
        DataSourceConfigurationList dataSourceConfigurationList = convertToSerializableList(dataSourceConfigurations);
        this.recorderConfigurationCache.update(this.recorderConfiguration.getName(), new Date(), dataSourceConfigurationList);
    }

    private DataSourceConfigurationList convertToSerializableList(List<DataSourceConfiguration> dataSourceConfigurations) {
        DataSourceConfigurationList dataSourceConfigurationList = new DataSourceConfigurationList();
        for (DataSourceConfiguration dataSourceConfiguration : dataSourceConfigurations) {
            if (dataSourceConfiguration instanceof BluetoothSensorDataSourceConfiguration) {
                dataSourceConfigurationList.addBluetoothDataSourceConfiguration((BluetoothSensorDataSourceConfiguration) dataSourceConfiguration);
            } else if (dataSourceConfiguration instanceof LocationSensorDataSourceConfiguration) {
                dataSourceConfigurationList.addLocationDataSourceConfiguration((LocationSensorDataSourceConfiguration) dataSourceConfiguration);
            } else if (dataSourceConfiguration instanceof DerivedDataSourceConfiguration) {
                dataSourceConfigurationList.addDerivedDataSourceConfiguration((DerivedDataSourceConfiguration) dataSourceConfiguration);
            }
        }
        return dataSourceConfigurationList;
    }
}
