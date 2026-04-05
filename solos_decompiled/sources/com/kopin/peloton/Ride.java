package com.kopin.peloton;

import com.google.gson.Gson;
import com.kopin.peloton.ride.Lap;
import com.kopin.peloton.ride.RideHeader;
import com.kopin.peloton.ride.RideRecord;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class Ride extends RideHeader implements IWorkout {
    public ArrayList<Lap> Laps;
    public ArrayList<RideRecord> Records;
    public String RideId;

    public Ride() {
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RideId = "";
    }

    public Ride(String id) {
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RideId = "";
        this.RideId = id;
    }

    public Ride(String id, String name, String description, long startTime, long endTime, long duration, double distance) {
        super(name, description, startTime, endTime, duration, distance);
        this.Laps = new ArrayList<>();
        this.Records = new ArrayList<>();
        this.RideId = "";
        this.RideId = id;
        this.Id = id;
    }

    public void addLap(Lap lap) {
        this.Laps.add(lap);
    }

    public void addRecord(RideRecord rideRecord) {
        this.Records.add(rideRecord);
    }

    @Override // com.kopin.peloton.ride.RideHeader, com.kopin.peloton.ride.Activity
    public String toString() {
        return super.toString() + " \n RideId " + this.RideId;
    }

    public String getRideDataJson(boolean addLaps, boolean addRecords, boolean addOverallStats) {
        Gson gson = new Gson();
        StringBuilder builder = new StringBuilder("{ ");
        if (addLaps) {
            builder.append(" \"Laps\":");
            builder.append(gson.toJson(this.Laps));
            builder.append(",");
        }
        if (addRecords) {
            builder.append(" \"Records\":");
            builder.append(gson.toJson(this.Records));
            builder.append(",");
        }
        if (addOverallStats) {
            builder.append(" \"OverallStats\":");
            builder.append(gson.toJson(this.OverallStats));
            builder.append(",");
        }
        builder.append(" }");
        return builder.toString();
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
