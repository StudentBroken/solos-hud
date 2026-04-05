package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import com.ua.sdk.Convert;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataPointImpl;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.SensorHealth;
import com.ua.sdk.recorder.SensorStatus;
import com.ua.sdk.recorder.datasource.sensor.SensorDataSource;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient;
import com.ua.sdk.recorder.producer.SensorMessageProducer;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class BluetoothSensorDataSource extends SensorDataSource {
    private BluetoothClient bluetoothClient;
    private String deviceAddress;
    private BluetoothDeviceHealth deviceHealth;
    private MyBluetoothClientListener myBluetoothClientListener;

    public BluetoothSensorDataSource(DataSourceIdentifier dataSourceIdentifier, List<DataTypeRef> dataTypeRefs, SensorMessageProducer sensorMessageProducer, BluetoothClient bluetoothClient, String deviceAddress, SensorDataSource.SensorDataSourceListener listener) {
        super(dataSourceIdentifier, dataTypeRefs, sensorMessageProducer, listener);
        this.deviceHealth = new BluetoothDeviceHealth();
        this.myBluetoothClientListener = new MyBluetoothClientListener();
        this.bluetoothClient = bluetoothClient;
        this.deviceAddress = deviceAddress;
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void configure(RecorderContext context) {
        super.configure(context);
        if (this.bluetoothClient != null) {
            this.bluetoothClient.configure(context);
        }
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void connectDataSource() {
        if (this.bluetoothClient != null) {
            this.bluetoothClient.connect(this.myBluetoothClientListener, this.deviceAddress, this.recorderContext.getApplicationContext());
        }
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void disconnectDataSource() {
        if (this.bluetoothClient != null) {
            this.bluetoothClient.disconnect();
        }
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void startSegment() {
        if (this.bluetoothClient != null) {
            this.bluetoothClient.startSegment();
        }
    }

    @Override // com.ua.sdk.recorder.datasource.DataSource
    public void stopSegment() {
        if (this.bluetoothClient != null) {
            this.bluetoothClient.stopSegment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUpdateHealth() {
        SensorHealth newState = this.deviceHealth.calculateOverallHealth();
        if (newState != this.sensorHealth) {
            this.sensorHealth = newState;
            if (this.sensorDataSourceListener != null) {
                this.sensorDataSourceListener.onSensorStateChanged(this.dataSourceIdentifier, this.sensorStatus, this.sensorHealth);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUpdateStatus(SensorStatus status) {
        if (status != this.sensorStatus) {
            this.sensorStatus = status;
            if (status == SensorStatus.DISCONNECTED) {
                this.sensorHealth = SensorHealth.UNKNOWN;
            }
            if (this.sensorDataSourceListener != null) {
                this.sensorDataSourceListener.onSensorStateChanged(this.dataSourceIdentifier, this.sensorStatus, this.sensorHealth);
            }
        }
    }

    protected class MyBluetoothClientListener implements BluetoothClient.BluetoothClientListener {
        protected MyBluetoothClientListener() {
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onConnectionStatusChanged(SensorStatus status) {
            BluetoothSensorDataSource.this.checkUpdateStatus(status);
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onBatteryLevelMeasurement(long level) {
            BluetoothSensorDataSource.this.deviceHealth.setBatteryRemaining((int) level);
            BluetoothSensorDataSource.this.checkUpdateHealth();
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onRssiMeasurement(long strength) {
            BluetoothSensorDataSource.this.deviceHealth.setRssi((int) strength);
            BluetoothSensorDataSource.this.checkUpdateHealth();
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onHeartRateMeasurement(long heartRate, long joules) {
            long timestampInMillis = ((long) BluetoothSensorDataSource.this.clock.getTimestamp()) * 1000;
            DataPointImpl dataPoint = new DataPointImpl();
            dataPoint.setValue(BaseDataTypes.FIELD_HEART_RATE, Long.valueOf(heartRate));
            dataPoint.setDatetime(new Date(timestampInMillis));
            BluetoothSensorDataSource.this.sendData(dataPoint, BaseDataTypes.TYPE_HEART_RATE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            if (joules != -1) {
                DataPointImpl dataPointEnergy = new DataPointImpl();
                dataPointEnergy.setValue(BaseDataTypes.FIELD_ENERGY_EXPENDED, Long.valueOf(joules));
                dataPoint.setDatetime(new Date(timestampInMillis));
                BluetoothSensorDataSource.this.sendData(dataPointEnergy, BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onRscMeasurement(double speed, long cadence, double strideLength, double totalDistance, boolean isRunning) {
            long timestampInMillis = ((long) BluetoothSensorDataSource.this.clock.getTimestamp()) * 1000;
            DataPointImpl dataPointSpeed = new DataPointImpl();
            dataPointSpeed.setValue(BaseDataTypes.FIELD_SPEED, Double.valueOf(speed));
            dataPointSpeed.setDatetime(new Date(timestampInMillis));
            BluetoothSensorDataSource.this.sendData(dataPointSpeed, BaseDataTypes.TYPE_SPEED.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl dataPointCadence = new DataPointImpl();
            dataPointCadence.setDatetime(new Date(timestampInMillis));
            dataPointCadence.setValue(BaseDataTypes.FIELD_RUN_CADENCE, Long.valueOf(cadence));
            BluetoothSensorDataSource.this.sendData(dataPointCadence, BaseDataTypes.TYPE_RUN_CADENCE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            if (strideLength != -1.0d) {
                DataPointImpl strideDataPoint = new DataPointImpl();
                strideDataPoint.setDatetime(new Date(timestampInMillis));
                strideDataPoint.setValue(BaseDataTypes.FIELD_STRIDE_LENGTH, Double.valueOf(strideLength));
                BluetoothSensorDataSource.this.sendData(strideDataPoint, BaseDataTypes.TYPE_STRIDE_LENGTH.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (totalDistance != -1.0d) {
                DataPointImpl distanceDataPoint = new DataPointImpl();
                distanceDataPoint.setDatetime(new Date(timestampInMillis));
                distanceDataPoint.setValue(BaseDataTypes.FIELD_DISTANCE, Double.valueOf(totalDistance));
                BluetoothSensorDataSource.this.sendData(distanceDataPoint, BaseDataTypes.TYPE_DISTANCE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onCyclingPowerMeasurement(long power, double powerBalance, double accumulatedTorque, long wheelRevs, long crankRevs, long maxForce, long minForce, double maxTorque, double minTorque, long maxAngle, long minAngle, long topDeadSpotAngle, long bottomDeadSpotAngle, long totalEnergy) {
            long timeStampInMillis = ((long) BluetoothSensorDataSource.this.clock.getTimestamp()) * 1000;
            DataPointImpl powerDataPoint = new DataPointImpl();
            powerDataPoint.setDatetime(new Date(timeStampInMillis));
            powerDataPoint.setValue(BaseDataTypes.FIELD_CYCLING_POWER, Long.valueOf(power));
            BluetoothSensorDataSource.this.sendData(powerDataPoint, BaseDataTypes.TYPE_CYCLING_POWER.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            if (powerBalance != -1.0d) {
                DataPointImpl powerBalanceDataPoint = new DataPointImpl();
                powerBalanceDataPoint.setDatetime(new Date(timeStampInMillis));
                powerBalanceDataPoint.setValue(BaseDataTypes.FIELD_CYCLING_POWER_BALANCE, Double.valueOf(powerBalance));
                BluetoothSensorDataSource.this.sendData(powerBalanceDataPoint, BaseDataTypes.TYPE_CYCLING_POWER_BALANCE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (accumulatedTorque != -1.0d) {
                DataPointImpl torqueDataPoint = new DataPointImpl();
                torqueDataPoint.setDatetime(new Date(timeStampInMillis));
                torqueDataPoint.setValue(BaseDataTypes.FIELD_ACCUMULATED_TORQUE, Double.valueOf(accumulatedTorque));
                BluetoothSensorDataSource.this.sendData(torqueDataPoint, BaseDataTypes.TYPE_ACCUMULATED_TORQUE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (wheelRevs != -1) {
                DataPointImpl wheelRevsDataPoint = new DataPointImpl();
                wheelRevsDataPoint.setDatetime(new Date(timeStampInMillis));
                wheelRevsDataPoint.setValue(BaseDataTypes.FIELD_WHEEL_REVOLUTIONS, Long.valueOf(wheelRevs));
                BluetoothSensorDataSource.this.sendData(wheelRevsDataPoint, BaseDataTypes.TYPE_WHEEL_REVOLUTIONS.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (crankRevs != -1) {
                DataPointImpl crankRevDataPoint = new DataPointImpl();
                crankRevDataPoint.setDatetime(new Date(timeStampInMillis));
                crankRevDataPoint.setValue(BaseDataTypes.FIELD_CRANK_REVOLUTIONS, Long.valueOf(crankRevs));
                BluetoothSensorDataSource.this.sendData(crankRevDataPoint, BaseDataTypes.TYPE_CRANK_REVOLUTIONS.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (maxForce != -1 || minForce != -1) {
                DataPointImpl forceDataPoint = new DataPointImpl();
                forceDataPoint.setDatetime(new Date(timeStampInMillis));
                forceDataPoint.setValue(BaseDataTypes.FIELD_MAX_FORCE, Long.valueOf(maxForce));
                forceDataPoint.setValue(BaseDataTypes.FIELD_MIN_FORCE, Long.valueOf(minForce));
                BluetoothSensorDataSource.this.sendData(forceDataPoint, BaseDataTypes.TYPE_EXTREME_FORCES.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (maxTorque != -1.0d || minTorque != -1.0d) {
                DataPointImpl extremeTorqueDataPoint = new DataPointImpl();
                extremeTorqueDataPoint.setDatetime(new Date(timeStampInMillis));
                extremeTorqueDataPoint.setValue(BaseDataTypes.FIELD_MAX_TORQUE, Double.valueOf(maxTorque));
                extremeTorqueDataPoint.setValue(BaseDataTypes.FIELD_MIN_TORQUE, Double.valueOf(minTorque));
                BluetoothSensorDataSource.this.sendData(extremeTorqueDataPoint, BaseDataTypes.TYPE_EXTREME_TORQUE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (maxAngle != -1 || minAngle != -1) {
                DataPointImpl extremeAngleDataPoint = new DataPointImpl();
                extremeAngleDataPoint.setDatetime(new Date(timeStampInMillis));
                extremeAngleDataPoint.setValue(BaseDataTypes.FIELD_MAX_ANGLE, Long.valueOf(maxAngle));
                extremeAngleDataPoint.setValue(BaseDataTypes.FIELD_MIN_ANGLE, Long.valueOf(minAngle));
                BluetoothSensorDataSource.this.sendData(extremeAngleDataPoint, BaseDataTypes.TYPE_EXTREME_ANGLES.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (topDeadSpotAngle != -1) {
                DataPointImpl topDeadSpotDataPoint = new DataPointImpl();
                topDeadSpotDataPoint.setDatetime(new Date(timeStampInMillis));
                topDeadSpotDataPoint.setValue(BaseDataTypes.FIELD_TOP_DEAD_SPOT_ANGLE, Long.valueOf(topDeadSpotAngle));
                BluetoothSensorDataSource.this.sendData(topDeadSpotDataPoint, BaseDataTypes.TYPE_TOP_DEAD_SPOT_ANGLE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (bottomDeadSpotAngle != -1) {
                DataPointImpl bottomDeadSpotDataPoint = new DataPointImpl();
                bottomDeadSpotDataPoint.setDatetime(new Date(timeStampInMillis));
                bottomDeadSpotDataPoint.setValue(BaseDataTypes.FIELD_BOTTOM_DEAD_SPOT_ANGLE, Long.valueOf(bottomDeadSpotAngle));
                BluetoothSensorDataSource.this.sendData(bottomDeadSpotDataPoint, BaseDataTypes.TYPE_BOTTOM_DEAD_SPOT_ANGLE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (totalEnergy != -1) {
                DataPointImpl energyDataPoint = new DataPointImpl();
                energyDataPoint.setDatetime(new Date(timeStampInMillis));
                energyDataPoint.setValue(BaseDataTypes.FIELD_ENERGY_EXPENDED, Long.valueOf(totalEnergy));
                BluetoothSensorDataSource.this.sendData(energyDataPoint, BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onCSCMeasurement(long wheelRevs, long crankRevs) {
            long timeStampInMillis = ((long) BluetoothSensorDataSource.this.clock.getTimestamp()) * 1000;
            if (wheelRevs != -1) {
                DataPointImpl wheelRevsDataPoint = new DataPointImpl();
                wheelRevsDataPoint.setDatetime(new Date(timeStampInMillis));
                wheelRevsDataPoint.setValue(BaseDataTypes.FIELD_WHEEL_REVOLUTIONS, Long.valueOf(wheelRevs));
                BluetoothSensorDataSource.this.sendData(wheelRevsDataPoint, BaseDataTypes.TYPE_WHEEL_REVOLUTIONS.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
            if (crankRevs != -1) {
                DataPointImpl crankRevDataPoint = new DataPointImpl();
                crankRevDataPoint.setDatetime(new Date(timeStampInMillis));
                crankRevDataPoint.setValue(BaseDataTypes.FIELD_CRANK_REVOLUTIONS, Long.valueOf(crankRevs));
                BluetoothSensorDataSource.this.sendData(crankRevDataPoint, BaseDataTypes.TYPE_CRANK_REVOLUTIONS.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            }
        }

        @Override // com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClient.BluetoothClientListener
        public void onArmour39Measurement(long steps, long calories, double willPower, long posture, long cadence) {
            double timestamp = BluetoothSensorDataSource.this.clock.getTimestamp();
            long timestampInMillis = ((long) timestamp) * 1000;
            DataPointImpl stepsDataPoint = new DataPointImpl();
            stepsDataPoint.setDatetime(new Date(timestampInMillis));
            stepsDataPoint.setValue(BaseDataTypes.FIELD_STEPS, Long.valueOf(steps));
            BluetoothSensorDataSource.this.sendData(stepsDataPoint, BaseDataTypes.TYPE_STEPS.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl energyDataPoint = new DataPointImpl();
            energyDataPoint.setDatetime(new Date(timestampInMillis));
            energyDataPoint.setValue(BaseDataTypes.FIELD_ENERGY_EXPENDED, Convert.caloriesToJoules(Double.valueOf(calories)));
            BluetoothSensorDataSource.this.sendData(energyDataPoint, BaseDataTypes.TYPE_ENERGY_EXPENDED.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl willPowerDataPoint = new DataPointImpl();
            willPowerDataPoint.setDatetime(new Date(timestampInMillis));
            willPowerDataPoint.setValue(BaseDataTypes.FIELD_WILLPOWER, Double.valueOf(willPower));
            BluetoothSensorDataSource.this.sendData(willPowerDataPoint, BaseDataTypes.TYPE_WILLPOWER.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl postureDataPoint = new DataPointImpl();
            postureDataPoint.setDatetime(new Date(timestampInMillis));
            postureDataPoint.setValue(BaseDataTypes.FIELD_POSTURE, Long.valueOf(posture));
            BluetoothSensorDataSource.this.sendData(postureDataPoint, BaseDataTypes.TYPE_POSTURE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
            DataPointImpl runCadenceDataPoint = new DataPointImpl();
            runCadenceDataPoint.setDatetime(new Date(timestampInMillis));
            runCadenceDataPoint.setValue(BaseDataTypes.FIELD_RUN_CADENCE, Long.valueOf(cadence));
            BluetoothSensorDataSource.this.sendData(runCadenceDataPoint, BaseDataTypes.TYPE_RUN_CADENCE.getRef(), BluetoothSensorDataSource.this.dataSourceIdentifier);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BluetoothSensorDataSource)) {
            return false;
        }
        BluetoothSensorDataSource that = (BluetoothSensorDataSource) o;
        if (this.deviceAddress != null) {
            if (this.deviceAddress.equals(that.deviceAddress)) {
                return true;
            }
        } else if (that.deviceAddress == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.deviceAddress != null) {
            return this.deviceAddress.hashCode();
        }
        return 0;
    }
}
