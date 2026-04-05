package com.kopin.peloton.ride;

import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class RideHeader extends Activity {
    private static final String format = "BikeId %s, RideType %s, RouteFollowedId %s, GhostRideId %s, TrainingId %s, distance %f, altChange %f, startTime %d, lapCount %d";
    public Double AltitudeRange;
    public String BikeId;
    public double Distance;
    public long Duration;
    public long EndTime;
    public double FunctionalThresholdPower;
    public String GhostRideId;
    public long LapCount;
    public Double OverallClimb;
    public LapStats OverallStats;
    public long PeakHeartRate;
    public double PerceivedExertion;
    public String RideType;
    public String RouteFollowedId;
    public long StartTime;
    public ArrayList<Metric> Targets;
    public String TrainingId;

    public enum TargetType {
        TargetAverageCadence,
        TargetAverageHeartrate,
        TargetAveragePower,
        TargetAverageSpeed,
        TargetAveragePace,
        TargetAverageStride;

        public static TargetType get(String name) {
            for (TargetType targetType : values()) {
                if (targetType.name().equalsIgnoreCase(name)) {
                    return targetType;
                }
            }
            return null;
        }
    }

    public RideHeader() {
        this.OverallStats = new LapStats();
        this.FunctionalThresholdPower = -2.147483648E9d;
        this.PeakHeartRate = -2147483648L;
        this.PerceivedExertion = -2.147483648E9d;
        this.Targets = new ArrayList<>();
    }

    public RideHeader(String name, String description, long startTime, long endTime, long duration, double distance) {
        super(name, description);
        this.OverallStats = new LapStats();
        this.FunctionalThresholdPower = -2.147483648E9d;
        this.PeakHeartRate = -2147483648L;
        this.PerceivedExertion = -2.147483648E9d;
        this.Targets = new ArrayList<>();
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Duration = duration;
        this.Distance = distance;
    }

    public Double getTarget(TargetType targetType) {
        for (Metric metric : this.Targets) {
            if (targetType == TargetType.get(metric.Name)) {
                return Double.valueOf(metric.ValueDouble != null ? metric.ValueDouble.doubleValue() : metric.ValueInteger.intValue());
            }
        }
        return null;
    }

    @Override // com.kopin.peloton.ride.Activity
    public String toString() {
        return super.toString() + "\n" + String.format(Locale.US, format, this.BikeId, this.RideType, this.RouteFollowedId, this.GhostRideId, this.TrainingId, Double.valueOf(this.Distance), this.AltitudeRange, Long.valueOf(this.StartTime), Long.valueOf(this.LapCount));
    }
}
