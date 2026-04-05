package com.kopin.peloton.ride;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class CorrectedElevation {
    public ArrayList<CorrectedElevationLap> ElevationLaps = new ArrayList<>();
    public ArrayList<ElevationDataPoint> Values = new ArrayList<>();

    public CorrectedElevation() {
    }

    public CorrectedElevation(List<ElevationDataPoint> values) {
        this.Values.addAll(values);
    }

    public CorrectedElevation(List<ElevationDataPoint> values, List<CorrectedElevationLap> lapStats) {
        this.Values.addAll(values);
        this.ElevationLaps.addAll(lapStats);
    }

    public void add(ElevationDataPoint metric) {
        this.Values.add(metric);
    }

    public void add(CorrectedElevationLap elevationlap) {
        this.ElevationLaps.add(elevationlap);
    }
}
