package com.kopin.peloton;

import com.kopin.peloton.ride.Lap;
import com.kopin.peloton.ride.RideRecord;
import com.kopin.peloton.ride.RunHeader;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class Run extends RunHeader implements IWorkout {
    public ArrayList<Lap> Laps;
    public ArrayList<RideRecord> Records;
    public String RunId;

    public Run() {
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RunId = "";
    }

    public Run(String id) {
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RunId = "";
        this.RunId = id;
    }

    public Run(String id, String name, String description, long startTime, long endTime, long duration, double distance) {
        super(name, description, startTime, endTime, duration, distance);
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RunId = "";
        this.RunId = id;
        this.Id = id;
    }

    public void addLap(Lap lap) {
        this.Laps.add(lap);
    }

    public void addRecord(RideRecord record) {
        this.Records.add(record);
    }

    @Override // com.kopin.peloton.ride.RunHeader, com.kopin.peloton.ride.Activity
    public String toString() {
        return super.toString() + " \n RunId " + this.RunId;
    }

    @Override // com.kopin.peloton.IWorkout
    public List<Lap> getLaps() {
        return this.Laps;
    }

    @Override // com.kopin.peloton.IWorkout
    public List<RideRecord> getRecords() {
        return this.Records;
    }
}
