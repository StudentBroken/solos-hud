package com.kopin.peloton.ride;

/* JADX INFO: loaded from: classes61.dex */
public class Lap {
    public double Distance;
    public long Duration;
    public long EndTime;
    public long StartTime;
    public LapStats Stats = new LapStats();

    public Lap() {
    }

    public Lap(long startTime, long endTime, long duration, long distance) {
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Duration = duration;
        this.Distance = distance;
    }
}
