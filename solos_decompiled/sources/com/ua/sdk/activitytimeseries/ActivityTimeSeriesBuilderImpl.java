package com.ua.sdk.activitytimeseries;

import com.ua.sdk.util.DoubleList;
import com.ua.sdk.util.IntList;
import com.ua.sdk.util.LongList;
import java.util.UUID;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesBuilderImpl implements ActivityTimeSeriesBuilder {
    public static final int MAX_EVENT_COUNT = 10800;
    public static final int MAX_INTERVAL_SECONDS = 604800;
    String recorderTypeKey = null;
    String recorderIdentifier = null;
    LongList stepEpochs = null;
    IntList stepValues = null;
    LongList calorieEpochs = null;
    DoubleList calorieValues = null;
    LongList distanceEpochs = null;
    DoubleList distanceValues = null;

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesBuilderImpl setRecorderTypeKey(String recorderTypeKey) {
        this.recorderTypeKey = recorderTypeKey;
        return this;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesBuilderImpl setRecorderIdentifier(String recorderIdentifier) {
        this.recorderIdentifier = recorderIdentifier;
        return this;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesBuilderImpl addSteps(long epochSeconds, int steps) {
        if (this.stepEpochs == null) {
            this.stepEpochs = new LongList();
            this.stepValues = new IntList();
        }
        this.stepEpochs.add(epochSeconds);
        this.stepValues.add(steps);
        return this;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesBuilderImpl addCalories(long epochSeconds, double joules) {
        if (this.calorieEpochs == null) {
            this.calorieEpochs = new LongList();
            this.calorieValues = new DoubleList();
        }
        this.calorieEpochs.add(epochSeconds);
        this.calorieValues.add(joules);
        return this;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesBuilderImpl addDistance(long epochSeconds, double meters) {
        if (this.distanceEpochs == null) {
            this.distanceEpochs = new LongList();
            this.distanceValues = new DoubleList();
        }
        this.distanceEpochs.add(epochSeconds);
        this.distanceValues.add(meters);
        return this;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesBuilder
    public ActivityTimeSeriesImpl build() {
        if (this.recorderTypeKey == null) {
            throw new IllegalArgumentException("recorderTypeKey must be set.");
        }
        if (this.recorderIdentifier == null) {
            this.recorderIdentifier = UUID.randomUUID().toString();
        }
        return new ActivityTimeSeriesImpl(this);
    }
}
