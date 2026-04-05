package com.ua.sdk.actigraphy;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityList;
import com.ua.sdk.LocalDate;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyTransferObject extends ApiTransferObject {
    public static final String KEY_ACTIGRAPHY = "actigraphies";

    @SerializedName("_embedded")
    Map<String, ArrayList<Actigraphies>> actigraphies;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    Integer totalCount;

    static class Actigraphies extends ApiTransferObject {

        @SerializedName("aggregates")
        ActigraphyAggregatesIn actigraphyAggregates;

        @SerializedName("date")
        LocalDate date;

        @SerializedName("end_datetime_utc")
        Date endDate;

        @SerializedName("metrics")
        MetricsIn metricsIn;

        @SerializedName("start_datetime_utc")
        Date startDate;

        @SerializedName("start_datetime_timezone")
        String startTimeZone;

        @SerializedName("workouts")
        ArrayList<WorkoutSummariesIn> workoutSummaryIn;

        Actigraphies() {
        }
    }

    static class ActigraphyAggregatesIn {

        @SerializedName("active_time")
        AggregateValueIn activeTimeAggregate;

        @SerializedName("bodymass")
        AggregateValueIn bodymassAggregate;

        @SerializedName(BaseDataTypes.ID_DISTANCE)
        AggregateValueIn distanceAggregate;

        @SerializedName("energy_burned")
        AggregateValueIn energyBurnedAggregate;

        @SerializedName("sleep")
        AggregateValueIn sleepAggregate;

        @SerializedName(BaseDataTypes.ID_STEPS)
        AggregateValueIn stepsAggregate;

        ActigraphyAggregatesIn() {
        }
    }

    static class AggregateValueIn {

        @SerializedName("average")
        Double average;

        @SerializedName("count")
        Double count;

        @SerializedName("details")
        AggregateDetailsIn details;

        @SerializedName("latest")
        Double latest;

        @SerializedName("max")
        Double max;

        @SerializedName("min")
        Double min;

        @SerializedName("sum")
        Double sum;

        AggregateValueIn() {
        }
    }

    static class AggregateDetailsIn {

        @SerializedName("awake")
        AggregateValueIn awake;

        @SerializedName("deep_sleep")
        AggregateValueIn deepSleep;

        @SerializedName("light_sleep")
        AggregateValueIn lightSleep;

        @SerializedName("time_to_sleep")
        AggregateValueIn timeToSleep;

        @SerializedName("times_awakened")
        AggregateValueIn timesAwakened;

        AggregateDetailsIn() {
        }
    }

    static class MetricsIn {

        @SerializedName("bodymass")
        ArrayList<MetricDetails> bodymassMetrics;

        @SerializedName(BaseDataTypes.ID_DISTANCE)
        ArrayList<MetricDetails> distanceMetrics;

        @SerializedName("energy_burned")
        ArrayList<MetricDetails> energyBurnedMetrics;

        @SerializedName("sleep")
        ArrayList<MetricDetails> sleepMetrics;

        @SerializedName(BaseDataTypes.ID_STEPS)
        ArrayList<MetricDetails> stepsMetrics;

        MetricsIn() {
        }
    }

    static class Metrics {

        @SerializedName("end_datetime_utc")
        Date endTime;

        @SerializedName("start_datetime_utc")
        Date startTime;

        @SerializedName("start_datetime_timezone")
        String startTimeZone;

        @SerializedName("time_series")
        TimeSeriesIn timeSeries;

        Metrics() {
        }
    }

    static class MetricDetails extends Metrics {

        @SerializedName("aggregates")
        AggregateValueIn aggregate;

        MetricDetails() {
        }
    }

    static class TimeSeriesIn {

        @SerializedName("epoch_values")
        ArrayList<ArrayList<Double>> epochValues;

        @SerializedName("interval")
        Integer interval;

        TimeSeriesIn() {
        }
    }

    static class WorkoutSummariesIn {

        @SerializedName("activity_type_id")
        Integer activityTypeId;

        @SerializedName("aggregates")
        WorkoutAggregateDetails details;

        @SerializedName("end_datetime_utc")
        Date endDate;

        @SerializedName("name")
        String name;

        @SerializedName("start_datetime_utc")
        Date startDate;

        WorkoutSummariesIn() {
        }
    }

    static class WorkoutAggregateDetails {

        @SerializedName("details")
        ActigraphyAggregatesIn actigraphyAggregatesIn;

        WorkoutAggregateDetails() {
        }
    }

    public static EntityList<Actigraphy> toList(ActigraphyTransferObject transferObject) throws UaException {
        ActigraphyList list = new ActigraphyList();
        list.setLinkMap(transferObject.getLinkMap());
        list.setTotalCount(transferObject.totalCount.intValue());
        for (Actigraphies actigraphyIn : transferObject.getActigraphyList()) {
            ActigraphyImpl actigraphy = new ActigraphyImpl();
            ActigraphyAggregatesImpl actigraphyAggregates = new ActigraphyAggregatesImpl();
            actigraphyAggregates.setDistance(getAggregateValues(actigraphyIn.actigraphyAggregates.distanceAggregate));
            actigraphyAggregates.setBodyMass(getAggregateValues(actigraphyIn.actigraphyAggregates.bodymassAggregate));
            actigraphyAggregates.setActiveTime(getAggregateValues(actigraphyIn.actigraphyAggregates.activeTimeAggregate));
            actigraphyAggregates.setEnergyBurned(getAggregateValues(actigraphyIn.actigraphyAggregates.energyBurnedAggregate));
            actigraphyAggregates.setSleep(getAggregateValues(actigraphyIn.actigraphyAggregates.sleepAggregate));
            actigraphyAggregates.setSteps(getAggregateValues(actigraphyIn.actigraphyAggregates.stepsAggregate));
            ActigraphyMetricsImpl actigraphyMetrics = new ActigraphyMetricsImpl();
            actigraphyMetrics.setDistance(getMetricDetailValues(actigraphyIn.metricsIn.distanceMetrics));
            actigraphyMetrics.setEnergyBurned(getMetricDetailValues(actigraphyIn.metricsIn.energyBurnedMetrics));
            actigraphyMetrics.setSteps(getMetricDetailValues(actigraphyIn.metricsIn.stepsMetrics));
            actigraphyMetrics.setSleep(getMetricDetailValues(actigraphyIn.metricsIn.sleepMetrics));
            actigraphyMetrics.setBodyMass(getMetricDetailValues(actigraphyIn.metricsIn.bodymassMetrics));
            actigraphy.setAggregates(actigraphyAggregates);
            actigraphy.setMetrics(actigraphyMetrics);
            actigraphy.setWorkoutSummaries(getWorkoutSummaries(actigraphyIn.workoutSummaryIn));
            actigraphy.setDate(actigraphyIn.date);
            actigraphy.setTimeZone(getTimeZone(actigraphyIn.startTimeZone));
            actigraphy.setStartDateTime(actigraphyIn.startDate);
            actigraphy.setEndDateTime(actigraphyIn.endDate);
            actigraphy.setLinkMap(actigraphyIn.getLinkMap());
            list.add(actigraphy);
        }
        return list;
    }

    private static AggregateValueImpl getAggregateValues(AggregateValueIn aggregateValueIn) {
        AggregateValueImpl aggregateValue = new AggregateValueImpl();
        aggregateValue.setSum(aggregateValueIn.sum);
        aggregateValue.setMin(aggregateValueIn.min);
        aggregateValue.setMax(aggregateValueIn.max);
        aggregateValue.setLatest(aggregateValueIn.latest);
        aggregateValue.setCount(aggregateValueIn.count);
        aggregateValue.setAverage(aggregateValueIn.average);
        if (aggregateValueIn.details != null && aggregateValueIn.details.awake != null) {
            SleepAggregateDetailsImpl sleepAggregateDetails = new SleepAggregateDetailsImpl();
            sleepAggregateDetails.setAwake(aggregateValueIn.details.awake.sum);
            sleepAggregateDetails.setLightSleep(aggregateValueIn.details.lightSleep.sum);
            sleepAggregateDetails.setTimeToSleep(aggregateValueIn.details.timeToSleep.sum);
            sleepAggregateDetails.setTimesAwaken(Integer.valueOf(aggregateValueIn.details.timesAwakened.sum.intValue()));
            sleepAggregateDetails.setDeepSleep(aggregateValueIn.details.deepSleep.sum);
            aggregateValue.setAggregateDetails(sleepAggregateDetails);
        }
        return aggregateValue;
    }

    private static MetricImpl[] getMetricDetailValues(ArrayList<MetricDetails> metricDetails) {
        if (metricDetails == null || metricDetails.isEmpty()) {
            return null;
        }
        MetricImpl[] metrics = new MetricImpl[metricDetails.size()];
        for (MetricDetails details : metricDetails) {
            MetricImpl metric = new MetricImpl();
            metric.setStartDateTime(details.startTime);
            metric.setEndDateTime(details.endTime);
            metric.setTimeZone(getTimeZone(details.startTimeZone));
            metric.setAggregateValue(getAggregateValues(details.aggregate));
            if (details.timeSeries.epochValues != null) {
                int timeSeriesSize = details.timeSeries.epochValues.size();
                long[] epochValue = new long[timeSeriesSize];
                double[] values = new double[timeSeriesSize];
                for (int i = 0; i < timeSeriesSize; i++) {
                    double value = details.timeSeries.epochValues.get(i).get(0).doubleValue();
                    epochValue[i] = (long) (Math.round(value) * 1000.0d);
                    values[i] = details.timeSeries.epochValues.get(i).get(1).doubleValue();
                }
                metric.setEpochTimes(epochValue);
                metric.setValues(values);
            } else {
                metric.setEpochTimes(new long[]{0});
                metric.setValues(new double[]{0.0d});
            }
            metrics[metricDetails.indexOf(details)] = metric;
        }
        return metrics;
    }

    private static WorkoutSummaryImpl[] getWorkoutSummaries(ArrayList<WorkoutSummariesIn> workoutSummariesIn) {
        if (workoutSummariesIn == null || workoutSummariesIn.size() < 1) {
            return null;
        }
        WorkoutSummaryImpl[] workoutSummaries = new WorkoutSummaryImpl[workoutSummariesIn.size()];
        for (WorkoutSummariesIn summary : workoutSummariesIn) {
            WorkoutSummaryImpl workoutSummary = new WorkoutSummaryImpl();
            workoutSummary.setActivityTypeId(summary.activityTypeId.intValue());
            workoutSummary.setName(summary.name);
            workoutSummary.setStartDateTime(summary.startDate);
            workoutSummary.setEndDateTime(summary.endDate);
            ActigraphyAggregatesImpl actigraphyAggregates = new ActigraphyAggregatesImpl();
            actigraphyAggregates.setDistance(getAggregateValues(summary.details.actigraphyAggregatesIn.distanceAggregate));
            actigraphyAggregates.setEnergyBurned(getAggregateValues(summary.details.actigraphyAggregatesIn.energyBurnedAggregate));
            actigraphyAggregates.setSteps(getAggregateValues(summary.details.actigraphyAggregatesIn.stepsAggregate));
            workoutSummary.setWorkoutAggregates(actigraphyAggregates);
            workoutSummaries[workoutSummariesIn.indexOf(summary)] = workoutSummary;
        }
        return workoutSummaries;
    }

    private ArrayList<Actigraphies> getActigraphyList() {
        if (this.actigraphies == null) {
            return null;
        }
        return this.actigraphies.get(KEY_ACTIGRAPHY);
    }

    private static TimeZone getTimeZone(String timeZone) {
        if (timeZone == null) {
            return null;
        }
        return TimeZone.getTimeZone(timeZone);
    }
}
