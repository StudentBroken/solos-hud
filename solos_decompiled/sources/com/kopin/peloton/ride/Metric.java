package com.kopin.peloton.ride;

import com.kopin.peloton.ride.RideHeader;

/* JADX INFO: loaded from: classes61.dex */
public class Metric {
    public String Name;
    public Double ValueDouble;
    public Integer ValueInteger;

    public Metric() {
    }

    public Metric(String name, double d) {
        this.Name = name;
        this.ValueDouble = Double.valueOf(d);
    }

    public Metric(String name, int i) {
        this.Name = name;
        this.ValueInteger = Integer.valueOf(i);
    }

    public Metric(RideHeader.TargetType target, double d) {
        this(target.name(), d);
    }

    public Metric(RideHeader.TargetType target, int i) {
        this(target.name(), i);
    }
}
