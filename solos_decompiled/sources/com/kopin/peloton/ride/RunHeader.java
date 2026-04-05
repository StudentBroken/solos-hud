package com.kopin.peloton.ride;

import com.kopin.peloton.ride.RideHeader;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class RunHeader extends Activity {
    private static final String format = "BikeId %s, RunType %s, RouteFollowedId %s, GhostRunId %s, TrainingId %s, distance %f, altChange %f, startTime %d, lapCount %d";
    public Double AltitudeRange;
    public double Distance;
    public long Duration;
    public long EndTime;
    public double FunctionalThresholdPower;
    public String GearId;
    public String GhostRunId;
    public long LapCount;
    public Double OverallClimb;
    public LapStats OverallStats;
    public long PeakHeartRate;
    public double PerceivedExertion;
    public String RouteFollowedId;
    public String RunType;
    public long StartTime;
    public ArrayList<Metric> Targets;
    public String TrainingId;

    public RunHeader() {
        this.OverallStats = new LapStats();
        this.FunctionalThresholdPower = -2.147483648E9d;
        this.PeakHeartRate = -2147483648L;
        this.PerceivedExertion = -2.147483648E9d;
        this.Targets = new ArrayList<>();
    }

    public RunHeader(String name, String description, long startTime, long endTime, long duration, double distance) {
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

    public Double getTarget(RideHeader.TargetType targetType) {
        for (Metric metric : this.Targets) {
            if (targetType == RideHeader.TargetType.get(metric.Name)) {
                return Double.valueOf(metric.ValueDouble != null ? metric.ValueDouble.doubleValue() : metric.ValueInteger.intValue());
            }
        }
        return null;
    }

    @Override // com.kopin.peloton.ride.Activity
    public String toString() {
        return super.toString() + "\n" + String.format(Locale.US, format, this.GearId, this.RunType, this.RouteFollowedId, this.GhostRunId, this.TrainingId, Double.valueOf(this.Distance), this.AltitudeRange, Long.valueOf(this.StartTime), Long.valueOf(this.LapCount));
    }
}
