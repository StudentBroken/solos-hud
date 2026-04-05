package com.ua.sdk.workout;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.io.IOException;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutTimeSeriesDataAdapter extends TypeAdapter<WorkoutTimeSeriesImpl> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, WorkoutTimeSeriesImpl value) throws IOException {
        if (value != null) {
            out.beginObject();
            if (value.workoutHeartRateEntryTimeSeries != null) {
                out.name("heartrate");
                out.beginArray();
                for (WorkoutHeartRateEntry workoutHeartRateEntry : value.workoutHeartRateEntryTimeSeries) {
                    out.beginArray();
                    out.value(workoutHeartRateEntry.getOffset());
                    out.value(workoutHeartRateEntry.getBpm());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutSpeedEntryTimeSeries != null) {
                out.name(BaseDataTypes.ID_SPEED);
                out.beginArray();
                for (WorkoutSpeedEntry workoutSpeedEntry : value.workoutSpeedEntryTimeSeries) {
                    out.beginArray();
                    out.value(workoutSpeedEntry.getOffset());
                    out.value(workoutSpeedEntry.getInstantaneousSpeed());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutCadenceEntryTimeSeries != null) {
                out.name("cadence");
                out.beginArray();
                for (WorkoutCadenceEntry workoutCadenceEntry : value.workoutCadenceEntryTimeSeries) {
                    out.beginArray();
                    out.value(workoutCadenceEntry.getOffset());
                    out.value(workoutCadenceEntry.getInstantaneousCadence());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutPowerEntryTimeSeries != null) {
                out.name("power");
                out.beginArray();
                for (WorkoutPowerEntry powerEntry : value.workoutPowerEntryTimeSeries) {
                    out.beginArray();
                    out.value(powerEntry.getOffset());
                    out.value(powerEntry.getInstantaneousPower());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutTorqueEntryTimeSeries != null) {
                out.name("torque");
                out.beginArray();
                for (WorkoutTorqueEntry torqueEntry : value.workoutTorqueEntryTimeSeries) {
                    out.beginArray();
                    out.value(torqueEntry.getOffset());
                    out.value(torqueEntry.getInstantaneousTorque());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutDistanceTimeSeries != null) {
                out.name(BaseDataTypes.ID_DISTANCE);
                out.beginArray();
                for (WorkoutDistanceEntry distanceEntry : value.workoutDistanceTimeSeries) {
                    out.beginArray();
                    out.value(distanceEntry.getOffset());
                    out.value(distanceEntry.getDistance());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutStepsEntryTimeSeries != null) {
                out.name(BaseDataTypes.ID_STEPS);
                out.beginArray();
                for (WorkoutStepsEntry stepsEntry : value.workoutStepsEntryTimeSeries) {
                    out.beginArray();
                    out.value(stepsEntry.getOffset());
                    out.value(stepsEntry.getInstantaneousSteps());
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutPositionEntryTimeSeries != null) {
                out.name("position");
                out.beginArray();
                for (WorkoutPositionEntry positionEntry : value.workoutPositionEntryTimeSeries) {
                    out.beginArray();
                    out.value(positionEntry.getOffset());
                    out.beginObject();
                    out.name(BaseDataTypes.ID_ELEVATION);
                    out.value(positionEntry.getElevation());
                    out.name("lat");
                    out.value(positionEntry.getLatitude());
                    out.name("lng");
                    out.value(positionEntry.getLongitude());
                    out.endObject();
                    out.endArray();
                }
                out.endArray();
            }
            if (value.workoutStopTimeEntryTimeSeries != null) {
                out.name("timer_stop");
                out.beginArray();
                for (WorkoutTimerStopEntry timerStopEntry : value.workoutStopTimeEntryTimeSeries) {
                    out.beginArray();
                    out.value(timerStopEntry.getOffset());
                    out.value(timerStopEntry.getStoppedTime());
                    out.endArray();
                }
                out.endArray();
            }
            out.endObject();
            return;
        }
        out.nullValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    /* JADX INFO: renamed from: read */
    public WorkoutTimeSeriesImpl read2(JsonReader in) throws IOException {
        WorkoutTimeSeriesImpl timeSeriesData = new WorkoutTimeSeriesImpl();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("heartrate")) {
                TimeSeriesImpl<WorkoutHeartRateEntry> heartRateEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset = in.nextDouble();
                    int heartRate = in.nextInt();
                    heartRateEntryTimeSeries.add(new WorkoutHeartRateEntryImpl(Double.valueOf(offset), Integer.valueOf(heartRate)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutHeartRateEntryTimeSeries = heartRateEntryTimeSeries;
            } else if (name.equals(BaseDataTypes.ID_SPEED)) {
                TimeSeriesImpl<WorkoutSpeedEntry> speedEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset2 = in.nextDouble();
                    double speed = in.nextDouble();
                    speedEntryTimeSeries.add(new WorkoutSpeedEntryImpl(Double.valueOf(offset2), Double.valueOf(speed)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutSpeedEntryTimeSeries = speedEntryTimeSeries;
            } else if (name.equals("cadence")) {
                TimeSeriesImpl<WorkoutCadenceEntry> cadenceEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset3 = in.nextDouble();
                    int cadence = in.nextInt();
                    cadenceEntryTimeSeries.add(new WorkoutCadenceEntryImpl(Double.valueOf(offset3), Integer.valueOf(cadence)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutCadenceEntryTimeSeries = cadenceEntryTimeSeries;
            } else if (name.equals("power")) {
                TimeSeriesImpl<WorkoutPowerEntry> powerEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset4 = in.nextDouble();
                    double power = in.nextDouble();
                    powerEntryTimeSeries.add(new WorkoutPowerEntryImpl(Double.valueOf(offset4), Double.valueOf(power)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutPowerEntryTimeSeries = powerEntryTimeSeries;
            } else if (name.equals("torque")) {
                TimeSeriesImpl<WorkoutTorqueEntry> torqueEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset5 = in.nextDouble();
                    double torque = in.nextDouble();
                    torqueEntryTimeSeries.add(new WorkoutTorqueEntryImpl(Double.valueOf(offset5), Double.valueOf(torque)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutTorqueEntryTimeSeries = torqueEntryTimeSeries;
            } else if (name.equals(BaseDataTypes.ID_DISTANCE)) {
                TimeSeriesImpl<WorkoutDistanceEntry> distanceEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset6 = in.nextDouble();
                    double distance = in.nextDouble();
                    distanceEntryTimeSeries.add(new WorkoutDistanceEntryImpl(Double.valueOf(offset6), Double.valueOf(distance)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutDistanceTimeSeries = distanceEntryTimeSeries;
            } else if (name.equals(BaseDataTypes.ID_STEPS)) {
                TimeSeriesImpl<WorkoutStepsEntry> stepsEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset7 = in.nextDouble();
                    int steps = in.nextInt();
                    stepsEntryTimeSeries.add(new WorkoutStepsEntryImpl(Double.valueOf(offset7), Integer.valueOf(steps)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutStepsEntryTimeSeries = stepsEntryTimeSeries;
            } else if (name.equals("position")) {
                TimeSeriesImpl<WorkoutPositionEntry> positionEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset8 = in.nextDouble();
                    Double elevation = null;
                    Double latitude = null;
                    Double longitude = null;
                    in.beginObject();
                    while (in.hasNext()) {
                        String name2 = in.nextName();
                        if (name2.equals(BaseDataTypes.ID_ELEVATION)) {
                            elevation = Double.valueOf(in.nextDouble());
                        } else if (name2.equals("lat")) {
                            latitude = Double.valueOf(in.nextDouble());
                        } else if (name2.equals("lng")) {
                            longitude = Double.valueOf(in.nextDouble());
                        } else {
                            in.skipValue();
                        }
                    }
                    in.endObject();
                    positionEntryTimeSeries.add(new WorkoutPositionEntryImpl(Double.valueOf(offset8), elevation, latitude, longitude));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutPositionEntryTimeSeries = positionEntryTimeSeries;
            } else if (name.equals("timer_stop")) {
                TimeSeriesImpl<WorkoutTimerStopEntry> timerStopEntryTimeSeries = new TimeSeriesImpl<>();
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    double offset9 = in.nextDouble();
                    double time = in.nextDouble();
                    timerStopEntryTimeSeries.add(new WorkoutTimerStopEntryImpl(Double.valueOf(offset9), Double.valueOf(time)));
                    in.endArray();
                }
                in.endArray();
                timeSeriesData.workoutStopTimeEntryTimeSeries = timerStopEntryTimeSeries;
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return timeSeriesData;
    }
}
