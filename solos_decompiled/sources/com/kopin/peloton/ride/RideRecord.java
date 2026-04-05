package com.kopin.peloton.ride;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class RideRecord extends DataPoint {
    public ArrayList<Metric> Values = new ArrayList<>();

    public RideRecord() {
    }

    public RideRecord(List<Metric> values) {
        this.Values.addAll(values);
    }

    public void add(Metric metric) {
        this.Values.add(metric);
    }
}
