package com.kopin.peloton.training;

import com.kopin.peloton.ride.Activity;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes61.dex */
public class TrainingWorkout extends Activity {
    public double Distance;
    public long Duration;
    public ArrayList<Segment> Segments;
    public String SportType;
    public String TrainingId;
    public String TrainingType;

    public enum Metric {
        Unknown,
        Speed,
        Pace,
        HeartRate,
        Power,
        Cadence,
        Step,
        Kick,
        FTP,
        RPE;

        public static Metric get(String name) {
            for (Metric metric : values()) {
                if (metric.name().equalsIgnoreCase(name)) {
                    return metric;
                }
            }
            return Unknown;
        }
    }

    public enum Sport {
        Unknown,
        Ride,
        Run;

        static Sport get(String name) {
            for (Sport sport : values()) {
                if (sport.name().equalsIgnoreCase(name)) {
                    return sport;
                }
            }
            return Unknown;
        }
    }

    public TrainingWorkout() {
        this.TrainingId = "";
        this.Segments = new ArrayList<>();
    }

    public TrainingWorkout(String name, String description, double distance, long duration, Sport sport, Metric metric, ArrayList<Segment> segments) {
        super(name, description);
        this.TrainingId = "";
        this.Segments = new ArrayList<>();
        this.Distance = distance;
        this.Duration = duration;
        this.SportType = sport.name();
        this.TrainingType = metric.name();
        this.Segments = segments;
    }

    public void addHeader(TrainingWorkout header) {
        this.Id = header.Id;
        this.Name = header.Name;
        this.Description = header.Description;
        this.TrainingId = header.TrainingId;
        this.Distance = header.Distance;
        this.Duration = header.Duration;
        this.SportType = header.SportType;
        this.TrainingType = header.TrainingType;
    }

    public Sport getSportType() {
        return Sport.get(this.SportType);
    }

    public Metric getTrainingType() {
        return Metric.get(this.TrainingType);
    }

    public static class Segment {
        public int LoopCount;
        public ArrayList<Step> Steps;
        public String Type;

        public enum SegmentType {
            Step,
            RampUp,
            RampDown,
            Repeat;

            /* JADX INFO: Access modifiers changed from: private */
            public static SegmentType get(String name) {
                for (SegmentType segmentType : values()) {
                    if (segmentType.name().equalsIgnoreCase(name)) {
                        return segmentType;
                    }
                }
                return Step;
            }
        }

        public Segment() {
            this.Steps = new ArrayList<>();
        }

        public Segment(SegmentType type, int loopCount, ArrayList<Step> steps) {
            this.Steps = new ArrayList<>();
            this.Type = type.name();
            this.LoopCount = loopCount;
            this.Steps = steps;
        }

        public SegmentType getType() {
            return SegmentType.get(this.Type);
        }
    }

    public static class Step {
        public double Distance;
        public long Duration;
        public String IntensityClass;
        public String Name;
        public String Notes;
        public ArrayList<Target> Targets;
        public String TriggerType;

        public enum Intensity {
            Active,
            WarmUp,
            CoolDown,
            Recover;

            /* JADX INFO: Access modifiers changed from: private */
            public static Intensity get(String name) {
                for (Intensity intensity : values()) {
                    if (intensity.name().equalsIgnoreCase(name)) {
                        return intensity;
                    }
                }
                return Active;
            }
        }

        public enum Trigger {
            Duration,
            Distance,
            Indefinite,
            TargetAchieved;

            public static Trigger get(String name) {
                for (Trigger trigger : values()) {
                    if (trigger.name().equalsIgnoreCase(name)) {
                        return trigger;
                    }
                }
                return Indefinite;
            }
        }

        public Step() {
            this.Targets = new ArrayList<>();
        }

        public Step(String name, String notes, long duration, double distance, Intensity intensity, Trigger trigger, ArrayList<Target> targets) {
            this.Targets = new ArrayList<>();
            this.Name = name;
            this.Notes = notes;
            this.Duration = duration;
            this.Distance = distance;
            this.IntensityClass = intensity.name();
            this.TriggerType = trigger.name();
            this.Targets = targets;
        }

        public Intensity getIntensityClass() {
            return Intensity.get(this.IntensityClass);
        }

        public Trigger getTriggerType() {
            return Trigger.get(this.TriggerType);
        }
    }

    public static class Target {
        public TargetValue Maximum;
        public String Metric;
        public TargetValue Minimum;
        public TargetValue Threshold;
        public String Type;

        public enum TargetType {
            AboveThreshold,
            BelowThreshold,
            WithinRange;

            /* JADX INFO: Access modifiers changed from: private */
            public static TargetType get(String name) {
                for (TargetType targetType : values()) {
                    if (targetType.name().equalsIgnoreCase(name)) {
                        return targetType;
                    }
                }
                return AboveThreshold;
            }
        }

        public Target() {
        }

        public Target(Metric metric, TargetType targetType, TargetValue threshold, TargetValue minimum, TargetValue maximum) {
            this.Metric = metric.name();
            this.Type = targetType.name();
            this.Threshold = threshold;
            this.Minimum = minimum;
            this.Maximum = maximum;
        }

        public TargetType getType() {
            return TargetType.get(this.Type);
        }

        public Metric getMetric() {
            return Metric.get(this.Metric);
        }
    }
}
