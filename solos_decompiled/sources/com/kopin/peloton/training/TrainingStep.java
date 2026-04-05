package com.kopin.peloton.training;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes61.dex */
public class TrainingStep {
    public double Distance;
    public long Duration;
    public String IntensityClass;
    public String Name;
    public String Notes;
    public ArrayList<TrainingTarget> Targets;
    public String TriggerType;

    public enum Intensity {
        Active,
        WarmUp,
        CoolDown,
        Recover
    }

    public enum Trigger {
        Duration,
        Distance,
        Indefinite,
        TargetAchieved
    }

    public TrainingStep() {
        this.Targets = new ArrayList<>();
    }

    public TrainingStep(String name, String notes, long duration, double distance, Intensity intensity, Trigger trigger, ArrayList<TrainingTarget> targets) {
        this.Targets = new ArrayList<>();
        this.Name = name;
        this.Notes = notes;
        this.Duration = duration;
        this.Distance = distance;
        this.IntensityClass = intensity.name();
        this.TriggerType = trigger.name();
        this.Targets = targets;
    }
}
