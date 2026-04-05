package com.ua.sdk.recorder.save;

import com.ua.sdk.UaLog;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataFrame;
import com.ua.sdk.datapoint.DataFrameImpl;
import com.ua.sdk.datapoint.DataPoint;
import com.ua.sdk.datapoint.DataTypeRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutBuilder;
import com.ua.sdk.workout.WorkoutBuilderImpl;
import com.ua.sdk.workout.WorkoutNameGenerator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class RecordWorkoutV7Converter {
    private RecorderContext context;
    private List<DataFrame> data;
    private Double distance;
    private Date startDate;
    private WorkoutBuilder workoutBuilder = new WorkoutBuilderImpl();

    public RecordWorkoutV7Converter(List<DataFrame> data, RecorderContext context) {
        this.context = context;
        this.data = data;
    }

    public Workout buildWorkout(String workoutName) {
        constructWorkout();
        this.workoutBuilder.setName(workoutName);
        return this.workoutBuilder.build();
    }

    public Workout buildWorkout(WorkoutNameGenerator nameGenerator) {
        constructWorkout();
        if (this.startDate == null) {
            UaLog.error("Unable to build workout without startDate.");
            return null;
        }
        this.workoutBuilder.setName(nameGenerator.generateName(this.context.getUser(), this.context.getActivityType(), this.context.getApplicationContext(), this.startDate, this.distance));
        return this.workoutBuilder.build();
    }

    private void constructWorkout() {
        for (DataFrame dataFrame : this.data) {
            DataFrameImpl dataFrameImpl = (DataFrameImpl) dataFrame;
            extractTimeSeries(dataFrameImpl);
        }
        this.workoutBuilder.setActivityType(this.context.getActivityType().getRef());
        this.workoutBuilder.setPrivacy(Privacy.Level.PUBLIC);
        this.workoutBuilder.setTimeZone(TimeZone.getDefault());
        this.workoutBuilder.setCreateTime(this.startDate);
        this.workoutBuilder.setStartTime(this.startDate);
    }

    private void extractTimeSeries(DataFrameImpl dataFrame) {
        Set<DataTypeRef> dataTypeChanged = dataFrame.getDataTypesChanged();
        if (dataFrame.isSegmentStarted()) {
            double offset = dataFrame.getActiveTime().doubleValue();
            if (this.startDate == null) {
                setStartDate(dataFrame.getFirstSegmentStartTime().doubleValue());
            }
            if (!Double.isNaN(offset) && dataTypeChanged != null) {
                for (DataTypeRef dataType : dataTypeChanged) {
                    String dataTypeId = dataType.getId();
                    if (dataTypeId.equals(BaseDataTypes.TYPE_HEART_RATE.getId()) && dataFrame.getHeartRateDataPoint() != null) {
                        this.workoutBuilder.addHeartRateEvent(offset, dataFrame.getHeartRateDataPoint().getHeartRate().intValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_SPEED.getId()) && dataFrame.getSpeedDataPoint() != null) {
                        this.workoutBuilder.addSpeedEvent(offset, dataFrame.getSpeedDataPoint().getSpeed().doubleValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_RUN_CADENCE.getId()) && dataFrame.getRunCadenceDataPoint() != null) {
                        this.workoutBuilder.addCadenceEvent(offset, dataFrame.getRunCadenceDataPoint().getRunCadence().intValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_CYCLING_POWER.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_CYCLING_POWER.getRef()) != null) {
                        this.workoutBuilder.addPowerEvent(offset, dataFrame.getDataPoint(BaseDataTypes.TYPE_CYCLING_POWER.getRef()).getValueDouble(BaseDataTypes.FIELD_CYCLING_POWER).doubleValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_ACCUMULATED_TORQUE.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_ACCUMULATED_TORQUE.getRef()) != null) {
                        this.workoutBuilder.addTorqueEvent(offset, dataFrame.getDataPoint(BaseDataTypes.TYPE_ACCUMULATED_TORQUE.getRef()).getValueDouble(BaseDataTypes.FIELD_ACCUMULATED_TORQUE).doubleValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_DISTANCE.getId()) && dataFrame.getDistanceDataPoint() != null) {
                        this.workoutBuilder.addDistanceEvent(offset, dataFrame.getDistanceDataPoint().getDistance().doubleValue());
                        this.distance = dataFrame.getDistanceDataPoint().getDistance();
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_STEPS.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_STEPS.getRef()) != null) {
                        this.workoutBuilder.addStepsEvent(offset, dataFrame.getDataPoint(BaseDataTypes.TYPE_STEPS.getRef()).getValueLong(BaseDataTypes.FIELD_STEPS).intValue());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_LOCATION.getId()) && dataFrame.getLocationDataPoint() != null) {
                        this.workoutBuilder.addPositionEvent(offset, null, dataFrame.getLocationDataPoint().getLatitude(), dataFrame.getLocationDataPoint().getLongitude());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_WILLPOWER.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_WILLPOWER.getRef()) != null) {
                        this.workoutBuilder.setWillPower(dataFrame.getWillPowerDataPoint().getWillPower());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_ENERGY_EXPENDED.getId()) && dataFrame.getEnergyExpendedDataPoint() != null) {
                        this.workoutBuilder.setMetabolicEnergyTotal(dataFrame.getEnergyExpendedDataPoint().getEnergyExpended());
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_HEART_RATE_SUMMARY.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_HEART_RATE_SUMMARY.getRef()) != null) {
                        DataPoint dataPoint = dataFrame.getDataPoint(BaseDataTypes.TYPE_HEART_RATE_SUMMARY.getRef());
                        this.workoutBuilder.setHeartRateAggregates(Integer.valueOf(dataPoint.getValueLong(BaseDataTypes.FIELD_HEART_RATE_MAX).intValue()), Integer.valueOf(dataPoint.getValueLong(BaseDataTypes.FIELD_HEART_RATE_MIN).intValue()), Integer.valueOf(dataPoint.getValueLong(BaseDataTypes.FIELD_HEART_RATE_AVG).intValue()));
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_SPEED_SUMMARY.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_SPEED_SUMMARY.getRef()) != null) {
                        DataPoint dataPoint2 = dataFrame.getDataPoint(BaseDataTypes.TYPE_SPEED_SUMMARY.getRef());
                        this.workoutBuilder.setSpeedAggregates(dataPoint2.getValueDouble(BaseDataTypes.FIELD_SPEED_MAX), dataPoint2.getValueDouble(BaseDataTypes.FIELD_SPEED_MIN), dataPoint2.getValueDouble(BaseDataTypes.FIELD_SPEED_AVG));
                    } else if (dataTypeId.equals(BaseDataTypes.TYPE_RUN_CADENCE_SUMMARY.getId()) && dataFrame.getDataPoint(BaseDataTypes.TYPE_RUN_CADENCE_SUMMARY.getRef()) != null) {
                        DataPoint dataPoint3 = dataFrame.getDataPoint(BaseDataTypes.TYPE_RUN_CADENCE_SUMMARY.getRef());
                        this.workoutBuilder.setHeartRateAggregates(Integer.valueOf(dataPoint3.getValueLong(BaseDataTypes.FIELD_RUN_CADENCE_MAX).intValue()), Integer.valueOf(dataPoint3.getValueLong(BaseDataTypes.FIELD_RUN_CADENCE_MIN).intValue()), Integer.valueOf(dataPoint3.getValueLong(BaseDataTypes.FIELD_RUN_CADENCE_AVG).intValue()));
                    }
                }
            }
        }
        this.workoutBuilder.setTotalTime(dataFrame.getActiveTime(), dataFrame.getElapsedTime());
    }

    private void setStartDate(double startDateSeconds) {
        long startDateInMillis = ((long) startDateSeconds) * 1000;
        this.startDate = new Date(startDateInMillis);
    }
}
