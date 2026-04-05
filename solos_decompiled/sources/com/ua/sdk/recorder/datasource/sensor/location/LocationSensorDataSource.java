package com.ua.sdk.recorder.datasource.sensor.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataPointImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.SensorStatus;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.datasource.sensor.location.LocationClient;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class LocationSensorDataSource extends SensorDataSource {
    private LocationDeviceHealth deviceHealth;
    private LocationClient locationClient;

    public LocationSensorDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs, SensorMessageProducer sensorMessageProducer, SensorDataSource.SensorDataSourceListener listener) {
        super(dataSourceIdentifier, dataTypeRefs, sensorMessageProducer, listener);
        this.deviceHealth = new LocationDeviceHealth();
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void configure(RecorderContext recorderContext) {
        super.configure(recorderContext);
        Context appContext = recorderContext.getApplicationContext();
        LocationManager locationManager = (LocationManager) appContext.getSystemService("location");
        this.locationClient = new AndroidLocationClient(locationManager);
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void connectDataSource() {
        this.locationClient.connect(new MyLocationClientListener());
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void disconnectDataSource() {
        this.locationClient.disconnect();
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void startSegment() {
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void stopSegment() {
    }

    protected class MyLocationClientListener implements LocationClient.LocationClientListener {
        protected MyLocationClientListener() {
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient.LocationClientListener
        public void onLocation(Location location) {
            double timestamp = LocationSensorDataSource.this.clock.getTimestamp();
            long timestampInMillis = ((long) timestamp) * 1000;
            DataPointImpl dataPoint = new DataPointImpl();
            dataPoint.setDatetime(new Date(timestampInMillis));
            dataPoint.setValue(BaseDataTypes.FIELD_LATITUDE, Double.valueOf(location.getLatitude()));
            dataPoint.setValue(BaseDataTypes.FIELD_LONGITUDE, Double.valueOf(location.getLongitude()));
            dataPoint.setValue(BaseDataTypes.FIELD_HORIZONTAL_ACCURACY, Double.valueOf(location.getAccuracy()));
            LocationSensorDataSource.this.sendData(dataPoint, BaseDataTypes.TYPE_LOCATION.getRef(), LocationSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl elevationDataPoint = new DataPointImpl();
            elevationDataPoint.setDatetime(new Date(timestampInMillis));
            elevationDataPoint.setValue(BaseDataTypes.FIELD_ELEVATION, Double.valueOf(location.getAltitude()));
            elevationDataPoint.setValue(BaseDataTypes.FIELD_VERTICAL_ACCURACY, Double.valueOf(location.getAccuracy()));
            LocationSensorDataSource.this.sendData(elevationDataPoint, BaseDataTypes.TYPE_ELEVATION.getRef(), LocationSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl bearingDataPoint = new DataPointImpl();
            bearingDataPoint.setDatetime(new Date(timestampInMillis));
            bearingDataPoint.setValue(BaseDataTypes.FIELD_BEARING, Double.valueOf(location.getBearing()));
            LocationSensorDataSource.this.sendData(bearingDataPoint, BaseDataTypes.TYPE_BEARING.getRef(), LocationSensorDataSource.this.dataSourceIdentifier);
            LocationSensorDataSource.this.deviceHealth.setAccuracy(location.getAccuracy());
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient.LocationClientListener
        public void onStatus(boolean gpsEnabled, boolean gpsFix, float accuracy) {
            LocationSensorDataSource.this.sensorHealth = LocationSensorDataSource.this.deviceHealth.calculateOverallHealth();
            if (!gpsEnabled) {
                LocationSensorDataSource.this.sensorStatus = SensorStatus.DISCONNECTED;
            } else if (gpsFix) {
                LocationSensorDataSource.this.sensorStatus = SensorStatus.CONNECTED;
            } else {
                LocationSensorDataSource.this.sensorStatus = SensorStatus.CONNECTING;
            }
            if (LocationSensorDataSource.this.sensorDataSourceListener != null) {
                LocationSensorDataSource.this.sensorDataSourceListener.onSensorStateChanged(LocationSensorDataSource.this.dataSourceIdentifier, LocationSensorDataSource.this.sensorStatus, LocationSensorDataSource.this.sensorHealth);
            }
        }
    }
}
