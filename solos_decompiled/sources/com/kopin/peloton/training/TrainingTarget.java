package com.kopin.peloton.training;

import com.kopin.peloton.training.TrainingWorkout;

/* JADX INFO: loaded from: classes61.dex */
public class TrainingTarget {
    public TargetValue Maximum;
    public String Metric;
    public TargetValue Minimum;
    public TargetValue Threshold;
    public String Type;

    public enum TargetType {
        AboveThreshold,
        BelowThreshold,
        WithinRange
    }

    public TrainingTarget() {
    }

    public TrainingTarget(TrainingWorkout.Metric metric, TargetType targetType, TargetValue threshold, TargetValue minimum, TargetValue maximum) {
        this.Metric = metric.name();
        this.Type = targetType.name();
        this.Threshold = threshold;
        this.Minimum = minimum;
        this.Maximum = maximum;
    }
}
